package com.laurena.comprendremonchien

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

/** PDF d'une page qui compare deux bilans du même animal (avant / après). */
object ComparaisonPdfExporter {

    private val COLOR_PRIMARY = Color.parseColor("#8E4A2D")
    private val COLOR_PRIMARY_SOFT = Color.parseColor("#B86A4A")
    private val COLOR_ACCENT = Color.parseColor("#D9A58F")
    private val COLOR_INK = Color.parseColor("#33231D")
    private val COLOR_INK_SOFT = Color.parseColor("#75584C")
    private val COLOR_BORDER = Color.parseColor("#E0D2C6")
    private val COLOR_WARM_BG = Color.parseColor("#FCF8F5")
    private val COLOR_BAR_BG = Color.parseColor("#EDE3DB")
    private val COLOR_AMELIORATION = Color.parseColor("#4F7A4A")
    private val COLOR_STABLE = Color.parseColor("#75584C")
    private val COLOR_A_SURVEILLER = Color.parseColor("#6B2D1A")
    private val COLOR_REFERENCE = Color.parseColor("#B8845A")

    private const val PAGE_W = 595
    private const val PAGE_H = 842
    private const val MARGIN = 44f
    private val CONTENT_W = PAGE_W - MARGIN * 2

    private fun t(fr: String, en: String, de: String, es: String) = tr(fr, en, de, es)
    private fun appName() = strAppName()

    // Bas de la zone de contenu (au-dessus du pied de page avec QR code)
    private val LIMITE_BAS = PAGE_H - MARGIN - 14f - 40f - 22f

    fun exporterComparaisonPdf(context: Context, avant: BilanSauvegarde, apres: BilanSauvegarde): File {
        val document = PdfDocument()
        var numPage = 1
        var page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_W, PAGE_H, numPage).create())
        var canvas = page.canvas
        canvas.drawColor(Color.WHITE)
        dessineEntete(canvas)
        var y = MARGIN + 54f

        // Passe à une nouvelle page si le bloc suivant ne tient pas
        fun assurer(hauteur: Float) {
            if (y + hauteur > LIMITE_BAS) {
                dessineFooter(canvas)
                document.finishPage(page)
                numPage++
                page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_W, PAGE_H, numPage).create())
                canvas = page.canvas
                canvas.drawColor(Color.WHITE)
                dessineEntete(canvas)
                y = MARGIN + 46f
            }
        }

        val evolutions = evolutionsAxes(avant, apres)

        // Titre
        y = drawText(canvas, apres.nomChien, MARGIN, y, CONTENT_W.toInt(), makePaint(22f, COLOR_PRIMARY, bold = true), Layout.Alignment.ALIGN_CENTER)
        y = drawText(canvas, t("Évolution entre deux bilans", "Change between two assessments", "Entwicklung zwischen zwei Einschätzungen", "Evolución entre dos evaluaciones"),
            MARGIN, y + 2f, CONTENT_W.toInt(), makePaint(11f, COLOR_INK_SOFT, italic = true), Layout.Alignment.ALIGN_CENTER)

        // Cartes des dates
        y += 16f
        val gap = 12f
        val cardW = (CONTENT_W - gap) / 2f
        val cardH = 66f
        drawDateCard(canvas, MARGIN, y, cardW, cardH, t("AVANT", "BEFORE", "VORHER", "ANTES"), avant)
        drawDateCard(canvas, MARGIN + cardW + gap, y, cardW, cardH, t("APRÈS", "AFTER", "NACHHER", "DESPUÉS"), apres)
        y += cardH + 14f

        // Évolution par axe
        assurer(32f + 56f)
        y = drawSectionTitle(canvas, y, t("Évolution par axe", "Change by axis", "Entwicklung je Achse", "Evolución por eje"))
        evolutions.forEach { evolution ->
            assurer(56f)
            y = drawEvolution(canvas, y, evolution)
        }

        // Synthèse
        y += 6f
        assurer(32f + 90f)
        y = drawSectionTitle(canvas, y, t("Synthèse", "Summary", "Zusammenfassung", "Resumen"))
        y = drawText(canvas, texteResumeComparaison(evolutions), MARGIN, y, CONTENT_W.toInt(), makePaint(11f, COLOR_PRIMARY, bold = true))
        y += 8f
        y = drawLigneAvantApres(canvas, y, t("Priorité", "Priority", "Priorität", "Prioridad"),
            textePrioriteAction(avant.prioriteActionEnum()), textePrioriteAction(apres.prioriteActionEnum()))
        y = drawLigneAvantApres(canvas, y, t("Situation", "Situation", "Situation", "Situación"),
            texteNiveauSituation(avant.niveauSituationEnum()), texteNiveauSituation(apres.niveauSituationEnum()))

        // Avertissement
        y += 10f
        val avertPaint = makePaint(9.5f, COLOR_INK_SOFT, italic = true)
        val avertLayout = makeStaticLayout(texteAvertissementComparaison(), (CONTENT_W - 28f).toInt(), avertPaint)
        val avertH = avertLayout.height + 24f
        assurer(avertH)
        drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + avertH, COLOR_WARM_BG, COLOR_BORDER, 12f)
        canvas.save()
        canvas.translate(MARGIN + 14f, y + 12f)
        avertLayout.draw(canvas)
        canvas.restore()

        dessineFooter(canvas)

        document.finishPage(page)

        val nomFichierSafe = apres.nomChien.lowercase(Locale.getDefault())
            .replace("\\s+".toRegex(), "_")
            .replace("[^a-z0-9_]+".toRegex(), "")
        val prefix = t("comparaison", "comparison", "vergleich", "comparacion")
        val file = File(context.cacheDir, "${prefix}_${nomFichierSafe.ifBlank { t("chien", "dog", "hund", "perro") }}.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return file
    }

    // ════════════════════════════════════════════════════════════
    // BLOCS
    // ════════════════════════════════════════════════════════════

    private fun dessineEntete(canvas: Canvas) {
        canvas.drawRect(0f, 0f, PAGE_W.toFloat(), MARGIN + 28f, fill(COLOR_WARM_BG))
        drawLine(canvas, 0f, MARGIN + 28f, PAGE_W.toFloat(), MARGIN + 28f, COLOR_BORDER, 0.5f)
        val titreEntete = t("Comparaison de bilans", "Assessment comparison", "Vergleich der Einschätzungen", "Comparación de evaluaciones")
        canvas.drawText(titreEntete, MARGIN, MARGIN + 18f, makePaint(9f, COLOR_INK_SOFT, bold = true))
        val appPaint = makePaint(9f, COLOR_INK_SOFT, italic = true)
        canvas.drawText(appName(), PAGE_W - MARGIN - appPaint.measureText(appName()), MARGIN + 18f, appPaint)
    }

    private fun drawDateCard(canvas: Canvas, left: Float, top: Float, w: Float, h: Float, titre: String, bilan: BilanSauvegarde) {
        drawCard(canvas, left, top, left + w, top + h, COLOR_WARM_BG, COLOR_BORDER, 12f)
        canvas.drawText(titre, left + 12f, top + 18f, makePaint(8.5f, COLOR_PRIMARY_SOFT, bold = true))
        drawText(canvas, bilan.date, left + 12f, top + 24f, (w - 24f).toInt(), makePaint(10f, COLOR_INK))
        if (bilan.estReference) {
            canvas.drawText(texteBilanDeReference(), left + 12f, top + h - 10f, makePaint(8.5f, COLOR_REFERENCE, bold = true))
        }
    }

    private fun drawSectionTitle(canvas: Canvas, y: Float, title: String): Float {
        canvas.drawText(title, MARGIN, y + 14f, makePaint(13f, COLOR_PRIMARY, bold = true))
        drawLine(canvas, MARGIN, y + 22f, PAGE_W - MARGIN, y + 22f, COLOR_BORDER, 0.6f)
        return y + 32f
    }

    private fun drawEvolution(canvas: Canvas, y: Float, evolution: EvolutionAxe): Float {
        val couleur = when (evolution.tendance) {
            Tendance.AMELIORATION -> COLOR_AMELIORATION
            Tendance.STABLE -> COLOR_STABLE
            Tendance.A_SURVEILLER -> COLOR_A_SURVEILLER
        }
        val labelPaint = makePaint(11f, COLOR_INK, bold = true)
        canvas.drawText(evolution.label, MARGIN, y + 12f, labelPaint)
        // Flèche vers le haut = ça va mieux, vers le bas = à surveiller
        val fleche = when (evolution.tendance) {
            Tendance.AMELIORATION -> "↗"
            Tendance.STABLE -> "→"
            Tendance.A_SURVEILLER -> "↘"
        }
        val tendanceTexte = "$fleche  ${libelleTendance(evolution.tendance)}"
        val tendancePaint = makePaint(10f, couleur, bold = true)
        canvas.drawText(tendanceTexte, PAGE_W - MARGIN - tendancePaint.measureText(tendanceTexte), y + 12f, tendancePaint)

        var barY = y + 20f
        barY = drawBarre(canvas, barY, t("Avant", "Before", "Vorher", "Antes"), evolution.avant, COLOR_ACCENT)
        barY = drawBarre(canvas, barY, t("Après", "After", "Nachher", "Después"), evolution.apres, COLOR_PRIMARY_SOFT)
        return barY + 4f
    }

    private fun drawBarre(canvas: Canvas, y: Float, label: String, score: Int, couleur: Int): Float {
        val labelW = 60f
        val niveauW = 96f
        val barH = 6f
        val labelPaint = makePaint(8.5f, COLOR_INK_SOFT)
        canvas.drawText(label, MARGIN, y + 7f, labelPaint)
        val barLeft = MARGIN + labelW
        val barRight = PAGE_W - MARGIN - niveauW - 8f
        canvas.drawRoundRect(RectF(barLeft, y, barRight, y + barH), barH / 2, barH / 2, fill(COLOR_BAR_BG))
        val ratio = (score / 100f).coerceIn(0f, 1f)
        if (ratio > 0f) {
            canvas.drawRoundRect(RectF(barLeft, y, barLeft + (barRight - barLeft) * ratio, y + barH), barH / 2, barH / 2, fill(couleur))
        }
        val niveau = QuestionnaireEngine.libelleNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(score))
        val niveauPaint = makePaint(8.5f, COLOR_INK_SOFT)
        canvas.drawText(niveau, PAGE_W - MARGIN - niveauPaint.measureText(niveau), y + 7f, niveauPaint)
        return y + 16f
    }

    private fun drawLigneAvantApres(canvas: Canvas, y: Float, label: String, avant: String, apres: String): Float {
        canvas.drawText(label, MARGIN, y + 12f, makePaint(10.5f, COLOR_INK, bold = true))
        val texte = t("Avant : $avant   →   Après : $apres", "Before: $avant   →   After: $apres", "Vorher: $avant   →   Nachher: $apres", "Antes: $avant   →   Después: $apres")
        val bas = drawText(canvas, texte, MARGIN, y + 16f, CONTENT_W.toInt(), makePaint(10f, COLOR_INK_SOFT))
        return bas + 8f
    }

    private fun dessineFooter(canvas: Canvas) {
        val qrSize = 40
        val footerY = PAGE_H - MARGIN - 14f
        drawLine(canvas, MARGIN, footerY - qrSize - 10f, PAGE_W - MARGIN, footerY - qrSize - 10f, COLOR_BORDER, 0.5f)
        val qrTop = (footerY - qrSize - 2f).toInt()
        val qrLeft = (PAGE_W - MARGIN - qrSize).toInt()
        canvas.drawBitmap(generateQrCode("https://play.google.com/store/apps/details?id=com.laurena.comprendremonchien", 300), null, Rect(qrLeft, qrTop, qrLeft + qrSize, qrTop + qrSize), null)
        canvas.drawText(
            t("Document généré automatiquement par l'application ${appName()}", "Automatically generated by the ${appName()} app", "Automatisch erstellt mit der App ${appName()}", "Documento generado automáticamente por la aplicación ${appName()}"),
            MARGIN, footerY - 22f, makePaint(8.5f, COLOR_INK_SOFT)
        )
        if (appLang() == AppLang.FR) {
            canvas.drawText("Consultations : laurenaharoy.com", MARGIN, footerY - 4f, makePaint(9.5f, COLOR_PRIMARY, bold = true))
        } else {
            canvas.drawText(
                t("", "Scan the QR code to open the app", "QR-Code scannen, um die App zu öffnen", "Escanea el código QR para acceder a la aplicación"),
                MARGIN, footerY - 4f, makePaint(9f, COLOR_PRIMARY, bold = true)
            )
        }
    }

    // ════════════════════════════════════════════════════════════
    // HELPERS — Dessin
    // ════════════════════════════════════════════════════════════

    private fun drawText(canvas: Canvas, text: String, x: Float, y: Float, maxWidth: Int, paint: TextPaint, alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL): Float {
        val sl = makeStaticLayout(text, maxWidth, paint, alignment)
        canvas.save()
        canvas.translate(x, y)
        sl.draw(canvas)
        canvas.restore()
        return y + sl.height
    }

    private fun makeStaticLayout(text: String, maxWidth: Int, paint: TextPaint, alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL): StaticLayout =
        StaticLayout.Builder.obtain(text, 0, text.length, paint, maxWidth.coerceAtLeast(1))
            .setAlignment(alignment)
            .setLineSpacing(0f, 1.3f)
            .setIncludePad(false)
            .build()

    private fun drawCard(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, fillColor: Int, borderColor: Int, radius: Float) {
        val rect = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rect, radius, radius, fill(fillColor))
        canvas.drawRoundRect(rect, radius, radius, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = borderColor; style = Paint.Style.STROKE; strokeWidth = 1f })
    }

    private fun drawLine(canvas: Canvas, x1: Float, y1: Float, x2: Float, y2: Float, color: Int, width: Float) {
        canvas.drawLine(x1, y1, x2, y2, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; strokeWidth = width })
    }

    private fun fill(color: Int): Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; style = Paint.Style.FILL }

    private fun makePaint(size: Float, color: Int, bold: Boolean = false, italic: Boolean = false): TextPaint {
        return TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = size * 1.33f
            this.color = color
            typeface = Typeface.create(
                Typeface.DEFAULT,
                when {
                    bold && italic -> Typeface.BOLD_ITALIC
                    bold -> Typeface.BOLD
                    italic -> Typeface.ITALIC
                    else -> Typeface.NORMAL
                }
            )
        }
    }

    private fun generateQrCode(text: String, size: Int): Bitmap {
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}