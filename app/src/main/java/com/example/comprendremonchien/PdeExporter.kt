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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

object PdfExporter {

    // ─── Couleurs ───────────────────────────────────────────────
    private val COLOR_PRIMARY = Color.parseColor("#8E4A2D")
    private val COLOR_PRIMARY_SOFT = Color.parseColor("#B86A4A")
    private val COLOR_ACCENT = Color.parseColor("#D9A58F")
    private val COLOR_INK = Color.parseColor("#33231D")
    private val COLOR_INK_SOFT = Color.parseColor("#75584C")
    private val COLOR_BORDER = Color.parseColor("#E0D2C6")
    private val COLOR_WARM_BG = Color.parseColor("#FCF8F5")
    private val COLOR_WARM_BG_ALT = Color.parseColor("#F4ECE5")
    private val COLOR_WHITE = Color.WHITE

    private val COLOR_PRIORITE_FAIBLE = Color.parseColor("#9E8572")
    private val COLOR_PRIORITE_MODEREE = Color.parseColor("#B8845A")
    private val COLOR_PRIORITE_ELEVEE = Color.parseColor("#8E4A2D")
    private val COLOR_PRIORITE_URGENTE = Color.parseColor("#6B2D1A")

    private val COLOR_PRIORITE_FAIBLE_BG = Color.parseColor("#F4EDE6")
    private val COLOR_PRIORITE_MODEREE_BG = Color.parseColor("#F5E8DC")
    private val COLOR_PRIORITE_ELEVEE_BG = Color.parseColor("#F2E0D6")
    private val COLOR_PRIORITE_URGENTE_BG = Color.parseColor("#EDD8D0")

    // ── Couleur sobre pour la mention morsure ──
    private val COLOR_MORSURE_TEXTE = Color.parseColor("#B8845A")
    private val COLOR_MORSURE_BG = Color.parseColor("#F5E8DC")

    // ─── Dimensions ─────────────────────────────────────────────
    private const val PAGE_W = 595
    private const val PAGE_H = 842
    private const val MARGIN = 44f
    private val CONTENT_W = PAGE_W - MARGIN * 2
    private const val FOOTER_H = 60f
    private val CONTENT_BOTTOM = PAGE_H - MARGIN - FOOTER_H - 10f

    fun exporterBilanPdf(context: Context, nomChien: String, analyse: ResultatAnalyse): File {
        val document = PdfDocument()
        val nom = nomChienAffiche(nomChien)
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        val couleurPriorite = couleurPourPriorite(analyse.prioriteAction)
        val libellePriorite = libellePourPriorite(analyse.prioriteAction)

        dessinePage1(document, nom, date, analyse, couleurPriorite, libellePriorite)
        dessinePage2(document, nom, analyse)
        dessinePage3(document, nom, analyse)
        dessinePage4(document, nom, analyse, couleurPriorite)

        val nomFichierSafe = nom.lowercase(Locale.getDefault())
            .replace("\\s+".toRegex(), "_")
            .replace("[^a-z0-9_]+".toRegex(), "")

        val file = File(context.cacheDir, "bilan_${nomFichierSafe.ifBlank { "chien" }}.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return file
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 1 — Couverture
    // ════════════════════════════════════════════════════════════
    private fun dessinePage1(
        document: PdfDocument, nom: String, date: String,
        analyse: ResultatAnalyse, couleurPriorite: Int, libellePriorite: String
    ) {
        val page = demarrerPage(document, 1)
        val canvas = page.canvas
        var y: Float

        val headerHeight = 190f
        drawRect(canvas, 0f, 0f, PAGE_W.toFloat(), headerHeight, COLOR_WARM_BG)

        drawLine(canvas, MARGIN, 26f, PAGE_W - MARGIN, 26f, COLOR_ACCENT, 0.8f)

        val appTitlePaint = makePaint(11f, COLOR_INK_SOFT, italic = true)
        val appTitle = "Comprendre mon chien"
        val appTitleW = appTitlePaint.measureText(appTitle)
        canvas.drawText(appTitle, (PAGE_W - appTitleW) / 2f, 44f, appTitlePaint)

        val centerX = PAGE_W / 2f
        drawLine(canvas, centerX - 70f, 54f, centerX - 10f, 54f, COLOR_ACCENT, 0.8f)
        drawLine(canvas, centerX + 10f, 54f, centerX + 70f, 54f, COLOR_ACCENT, 0.8f)
        drawCircle(canvas, centerX, 54f, 2f, COLOR_ACCENT)

        val datePaint = makePaint(9f, COLOR_INK_SOFT)
        canvas.drawText(date, PAGE_W - MARGIN - datePaint.measureText(date), 44f, datePaint)

        val nomPaint = makePaint(34f, COLOR_PRIMARY, bold = true)
        val nomW = nomPaint.measureText(nom)
        canvas.drawText(nom, (PAGE_W - nomW) / 2f, 96f, nomPaint)

        // ── MODIFICATION : "Bilan comportemental" → "Bilan émotionnel" ──
        val sousTitrePaint = makePaint(12f, COLOR_INK_SOFT, italic = true)
        val sousTitre = "Bilan émotionnel"
        val sousTitreW = sousTitrePaint.measureText(sousTitre)
        canvas.drawText(sousTitre, (PAGE_W - sousTitreW) / 2f, 118f, sousTitrePaint)

        drawLine(canvas, MARGIN + 40f, 130f, PAGE_W - MARGIN - 40f, 130f, COLOR_BORDER, 0.8f)

        val badgeText = libellePriorite.uppercase(Locale.getDefault())
        val badgePaint = makePaint(9f, COLOR_WHITE, bold = true)
        val badgeTextW = badgePaint.measureText(badgeText)
        val badgeW = max(120f, badgeTextW + 36f)
        val badgeLeft = (PAGE_W - badgeW) / 2f
        drawRoundRect(canvas, badgeLeft, 140f, badgeLeft + badgeW, 162f, 11f, couleurPriorite)
        canvas.drawText(badgeText, (PAGE_W - badgeTextW) / 2f, 155f, badgePaint)

        drawStaticText(
            canvas, analyse.profil.phraseHumaine,
            MARGIN, 170f, CONTENT_W.toInt(),
            makePaint(11f, COLOR_INK, italic = true),
            Layout.Alignment.ALIGN_CENTER
        )

        y = headerHeight + 20f

        drawLine(canvas, MARGIN, y, PAGE_W - MARGIN, y, COLOR_BORDER, 0.5f)
        y += 24f

        drawSectionTitle(canvas, y, "En un coup d\u2019\u0153il")
        y += 48f

        val gridItems = listOf(
            "Axe principal" to libelleAxe(analyse.problemePrincipal),
            "Situation" to texteNiveauSituation(analyse.niveauSituation),
            "Besoin principal" to besoinPrincipal(analyse.problemePrincipal)
                .removePrefix("Besoin principal : ").removeSuffix("."),
            "Aide \u00e0 envisager" to aideAEnvisager(analyse)
        )
        y = drawInfoGrid(canvas, y, gridItems)
        y += 20f

        val situationH = measureStaticTextHeight(
            analyse.messageSituation, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_INK)
        ) + 32f

        if (y + situationH < CONTENT_BOTTOM) {
            drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + situationH, COLOR_WARM_BG_ALT, COLOR_BORDER, 14f)
            drawStaticText(canvas, analyse.messageSituation, MARGIN + 16f, y + 16f, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_INK))
        }

        dessineFooter(canvas, 1)
        document.finishPage(page)
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 2 — Profil & Analyse
    // ════════════════════════════════════════════════════════════
    private fun dessinePage2(document: PdfDocument, nom: String, analyse: ResultatAnalyse) {
        val page = demarrerPage(document, 2)
        val canvas = page.canvas
        var y = MARGIN

        drawPageHeader(canvas, "Profil de $nom")
        y += 48f

        drawSectionTitle(canvas, y, "Les 4 dimensions")
        y += 48f

        val axes = listOf(
            Triple("Sensibilit\u00e9 / Peur", analyse.niveauPeur, analyse.peur),
            Triple("Attachement", analyse.niveauAttachement, analyse.attachement),
            Triple("Impulsivit\u00e9", analyse.niveauImpulsivite, analyse.impulsivite),
            Triple("R\u00e9activit\u00e9", analyse.niveauReactivite, analyse.reactivite)
        )
        axes.forEach { (label, niveau, score) ->
            y = drawAxeBar(canvas, y, label, niveau, score)
            y += 12f
        }
        y += 14f

        if (y < CONTENT_BOTTOM - 60f) {
            drawSectionTitle(canvas, y, "Hypoth\u00e8se de lecture")
            y += 48f

            val hypotheseH = measureStaticTextHeight(
                analyse.hypothesePrincipale, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_INK)
            ) + 32f

            if (y + hypotheseH < CONTENT_BOTTOM) {
                drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + hypotheseH, COLOR_WHITE, COLOR_ACCENT, 14f)
                drawStaticText(canvas, analyse.hypothesePrincipale, MARGIN + 16f, y + 16f, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_INK))
                y += hypotheseH + 24f
            }
        }

        if (y < CONTENT_BOTTOM - 60f) {
            drawSectionTitle(canvas, y, "Ce qui se passe probablement")
            y += 48f

            val explicationH = measureStaticTextHeight(
                analyse.explicationPrincipale, CONTENT_W.toInt(), makePaint(11f, COLOR_INK)
            )
            if (y + explicationH < CONTENT_BOTTOM) {
                drawStaticText(canvas, analyse.explicationPrincipale, MARGIN, y, CONTENT_W.toInt(), makePaint(11f, COLOR_INK))
                y += explicationH + 24f
            }
        }

        if (y < CONTENT_BOTTOM - 60f &&
            (analyse.facteursAggravants.isNotEmpty() || analyse.facteursProtecteurs.isNotEmpty())
        ) {
            drawSectionTitle(canvas, y, "Facteurs rep\u00e9r\u00e9s")
            y += 48f

            if (analyse.facteursAggravants.isNotEmpty() && y < CONTENT_BOTTOM - 30f) {
                canvas.drawText("Ce qui peut aggraver", MARGIN, y, makePaint(11f, COLOR_PRIMARY, bold = true))
                y += 20f
                analyse.facteursAggravants.forEach { if (y < CONTENT_BOTTOM - 20f) y = drawBullet(canvas, y, it) }
                y += 10f
            }

            if (analyse.facteursProtecteurs.isNotEmpty() && y < CONTENT_BOTTOM - 30f) {
                canvas.drawText("Ce qui prot\u00e8ge d\u00e9j\u00e0", MARGIN, y, makePaint(11f, COLOR_PRIMARY, bold = true))
                y += 20f
                analyse.facteursProtecteurs.forEach { if (y < CONTENT_BOTTOM - 20f) y = drawBullet(canvas, y, it) }
            }
        }

        dessineFooter(canvas, 2)
        document.finishPage(page)
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 3 — Plan d'action
    // ════════════════════════════════════════════════════════════
    private fun dessinePage3(document: PdfDocument, nom: String, analyse: ResultatAnalyse) {
        val page = demarrerPage(document, 3)
        val canvas = page.canvas
        var y = MARGIN

        drawPageHeader(canvas, "Plan d\u2019action pour $nom")
        y += 48f

        drawSectionTitle(canvas, y, "Premier levier utile")
        y += 48f

        val levierH = measureStaticTextHeight(
            analyse.conseilPrincipal, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_INK)
        ) + 32f

        if (y + levierH < CONTENT_BOTTOM) {
            drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + levierH, COLOR_WARM_BG_ALT, COLOR_ACCENT, 14f)
            drawStaticText(canvas, analyse.conseilPrincipal, MARGIN + 16f, y + 16f, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_INK))
            y += levierH + 24f
        }

        if (y < CONTENT_BOTTOM - 60f) {
            drawSectionTitle(canvas, y, "Les prochains jours")
            y += 48f

            if (y < CONTENT_BOTTOM - 20f) {
                canvas.drawText("\u00c0 faire", MARGIN, y, makePaint(11f, COLOR_PRIMARY, bold = true))
                y += 20f
                analyse.planAction.aFaire.forEach { if (y < CONTENT_BOTTOM - 20f) y = drawBullet(canvas, y, it) }
                y += 12f
            }

            if (y < CONTENT_BOTTOM - 20f) {
                canvas.drawText("\u00c0 \u00e9viter", MARGIN, y, makePaint(11f, COLOR_PRIMARY, bold = true))
                y += 20f
                analyse.planAction.aEviter.forEach { if (y < CONTENT_BOTTOM - 20f) y = drawBullet(canvas, y, it) }
                y += 12f
            }

            if (y < CONTENT_BOTTOM - 20f) {
                canvas.drawText("\u00c0 observer", MARGIN, y, makePaint(11f, COLOR_PRIMARY, bold = true))
                y += 20f
                analyse.planAction.aObserver.forEach { if (y < CONTENT_BOTTOM - 20f) y = drawBullet(canvas, y, it) }
                y += 18f
            }
        }

        if (analyse.conseilsPratiques.isNotEmpty() && y < CONTENT_BOTTOM - 60f) {
            drawSectionTitle(canvas, y, "Conseils compl\u00e9mentaires")
            y += 48f
            analyse.conseilsPratiques.forEach { if (y < CONTENT_BOTTOM - 20f) y = drawBullet(canvas, y, it) }
            y += 12f
        }

        // ── MODIFICATION : morsure sobre, orange doux, sans alarmisme ──
        if (analyse.aDejaMordu && y < CONTENT_BOTTOM - 40f) {
            val morsuText = "Une morsure a \u00e9t\u00e9 signal\u00e9e lors de ce bilan. Un accompagnement professionnel est recommand\u00e9 pour \u00e9valuer la situation et s\u00e9curiser le quotidien."
            val morsuH = measureStaticTextHeight(morsuText, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_MORSURE_TEXTE, bold = true)) + 32f
            if (y + morsuH < CONTENT_BOTTOM) {
                drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + morsuH, COLOR_MORSURE_BG, COLOR_MORSURE_TEXTE, 14f)
                drawStaticText(canvas, morsuText, MARGIN + 16f, y + 16f, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_MORSURE_TEXTE, bold = true))
                y += morsuH + 10f
            }
        }

        // Message aide si pas de morsure
        val messageAide = analyse.messageAide
        if (!messageAide.isNullOrBlank() && !analyse.aDejaMordu && y < CONTENT_BOTTOM - 40f) {
            val alertH = measureStaticTextHeight(messageAide, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_PRIORITE_ELEVEE, bold = true)) + 32f
            if (y + alertH < CONTENT_BOTTOM) {
                drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + alertH, COLOR_PRIORITE_ELEVEE_BG, COLOR_PRIORITE_ELEVEE, 14f)
                drawStaticText(canvas, messageAide, MARGIN + 16f, y + 16f, (CONTENT_W - 32f).toInt(), makePaint(11f, COLOR_PRIORITE_ELEVEE, bold = true))
            }
        }

        dessineFooter(canvas, 3)
        document.finishPage(page)
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 4 — À retenir
    // ════════════════════════════════════════════════════════════
    private fun dessinePage4(
        document: PdfDocument, nom: String,
        analyse: ResultatAnalyse, couleurPriorite: Int
    ) {
        val page = demarrerPage(document, 4)
        val canvas = page.canvas
        var y = MARGIN

        drawPageHeader(canvas, "\u00c0 retenir")
        y += 48f

        val recapText = buildString {
            append("$nom pr\u00e9sente surtout un profil ${analyse.profil.profilType.lowercase(Locale.getDefault())}.\n\n")
            append("Situation\u00a0: ${texteNiveauSituation(analyse.niveauSituation).lowercase(Locale.getDefault())}.\n\n")
            append("Axe principal\u00a0: ${libelleAxe(analyse.problemePrincipal).lowercase(Locale.getDefault())}.\n\n")
            append(besoinPrincipal(analyse.problemePrincipal))
        }

        val recapH = measureStaticTextHeight(
            recapText, (CONTENT_W - 48f).toInt(), makePaint(12f, COLOR_INK)
        ) + 48f

        drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + recapH, COLOR_WARM_BG, COLOR_BORDER, 16f)
        drawRoundRect(canvas, MARGIN, y, MARGIN + 5f, y + recapH, 16f, couleurPriorite)
        drawStaticText(canvas, recapText, MARGIN + 24f, y + 24f, (CONTENT_W - 48f).toInt(), makePaint(12f, COLOR_INK))
        y += recapH + 32f

        if (y < CONTENT_BOTTOM - 60f) {
            drawSectionTitle(canvas, y, "Conclusion")
            y += 48f

            val conclusion = "L\u2019objectif n\u2019est pas d\u2019\u00e9tiqueter $nom, mais d\u2019aider \u00e0 mieux lire ce qui se passe et \u00e0 avancer de mani\u00e8re plus adapt\u00e9e, plus concr\u00e8te et plus rassurante."
            val conclusionH = measureStaticTextHeight(conclusion, CONTENT_W.toInt(), makePaint(11f, COLOR_INK))
            if (y + conclusionH < CONTENT_BOTTOM) {
                drawStaticText(canvas, conclusion, MARGIN, y, CONTENT_W.toInt(), makePaint(11f, COLOR_INK))
                y += conclusionH + 32f
            }
        }

        if (y < CONTENT_BOTTOM - 40f) {
            drawLine(canvas, MARGIN, y, PAGE_W - MARGIN, y, COLOR_BORDER, 0.5f)
            y += 16f
            val disclaimer = "Ce bilan est indicatif. Il ne remplace pas l\u2019avis d\u2019un v\u00e9t\u00e9rinaire ni d\u2019un professionnel du comportement animal. Il peut servir de base de discussion lors d\u2019une consultation."
            val disclaimerH = measureStaticTextHeight(disclaimer, CONTENT_W.toInt(), makePaint(9.5f, COLOR_INK_SOFT))
            if (y + disclaimerH < CONTENT_BOTTOM) {
                drawStaticText(canvas, disclaimer, MARGIN, y, CONTENT_W.toInt(), makePaint(9.5f, COLOR_INK_SOFT))
            }
        }

        dessineFooterAvecQr(canvas)
        document.finishPage(page)
    }

    // ════════════════════════════════════════════════════════════
    // HELPERS — Dessin
    // ════════════════════════════════════════════════════════════

    private fun demarrerPage(document: PdfDocument, num: Int): PdfDocument.Page {
        val info = PdfDocument.PageInfo.Builder(PAGE_W, PAGE_H, num).create()
        val page = document.startPage(info)
        page.canvas.drawColor(COLOR_WHITE)
        return page
    }

    private fun drawPageHeader(canvas: Canvas, title: String) {
        drawRect(canvas, 0f, 0f, PAGE_W.toFloat(), MARGIN + 28f, COLOR_WARM_BG)
        drawLine(canvas, 0f, MARGIN + 28f, PAGE_W.toFloat(), MARGIN + 28f, COLOR_BORDER, 0.5f)
        drawStaticText(canvas, title, MARGIN, MARGIN + 6f, (CONTENT_W / 2).toInt(), makePaint(9f, COLOR_INK_SOFT, bold = true))
        val appLabel = "Comprendre mon chien"
        val appLabelPaint = makePaint(9f, COLOR_INK_SOFT, italic = true)
        canvas.drawText(appLabel, PAGE_W - MARGIN - appLabelPaint.measureText(appLabel), MARGIN + 18f, appLabelPaint)
    }

    private fun drawSectionTitle(canvas: Canvas, y: Float, title: String) {
        drawStaticText(canvas, title, MARGIN, y, CONTENT_W.toInt(), makePaint(13f, COLOR_PRIMARY, bold = true))
        drawLine(canvas, MARGIN, y + 22f, PAGE_W - MARGIN, y + 22f, COLOR_BORDER, 0.6f)
    }

    private fun drawAxeBar(canvas: Canvas, y: Float, label: String, niveau: NiveauAxe, score: Int): Float {
        val libelleNiveau = QuestionnaireEngine.libelleNiveauAxe(niveau)
        val fillRatio = (score / 100f).coerceIn(0f, 1f)
        val barH = 7f

        drawStaticText(canvas, label, MARGIN, y, (CONTENT_W * 0.6f).toInt(), makePaint(10.5f, COLOR_INK))
        val niveauPaint = makePaint(10.5f, COLOR_PRIMARY_SOFT, bold = true)
        canvas.drawText(libelleNiveau, PAGE_W - MARGIN - niveauPaint.measureText(libelleNiveau), y + 13f, niveauPaint)

        val barY = y + 18f
        canvas.drawRoundRect(RectF(MARGIN, barY, MARGIN + CONTENT_W, barY + barH), barH / 2, barH / 2,
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#EDE3DB"); style = Paint.Style.FILL })
        if (fillRatio > 0f) {
            canvas.drawRoundRect(RectF(MARGIN, barY, MARGIN + CONTENT_W * fillRatio, barY + barH), barH / 2, barH / 2,
                Paint(Paint.ANTI_ALIAS_FLAG).apply { color = COLOR_PRIMARY_SOFT; style = Paint.Style.FILL })
        }
        return y + 34f
    }

    private fun drawBullet(canvas: Canvas, y: Float, text: String): Float {
        val textX = MARGIN + 18f
        val paint = makePaint(10.5f, COLOR_INK)
        canvas.drawCircle(MARGIN + 6f, y - 2f, 2.5f,
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = COLOR_PRIMARY_SOFT; style = Paint.Style.FILL })
        val sl = makeStaticLayout(text, (CONTENT_W - 18f).toInt(), paint)
        canvas.save()
        canvas.translate(textX, y - paint.textSize * 1.33f)
        sl.draw(canvas)
        canvas.restore()
        return y + sl.height + 5f
    }

    private fun drawInfoGrid(canvas: Canvas, startY: Float, items: List<Pair<String, String>>): Float {
        val colGap = 12f
        val cardW = (CONTENT_W - colGap) / 2f
        var y = startY

        items.chunked(2).forEach { row ->
            val cardH = 64f
            row.forEachIndexed { index, (label, value) ->
                val left = MARGIN + index * (cardW + colGap)
                drawCard(canvas, left, y, left + cardW, y + cardH, COLOR_WARM_BG, COLOR_BORDER, 12f)
                drawStaticText(canvas, label.uppercase(Locale.getDefault()), left + 12f, y + 4f, (cardW - 24f).toInt(), makePaint(8f, COLOR_INK_SOFT, bold = true))
                val valueLayout = makeStaticLayout(value, (cardW - 24f).toInt(), makePaint(11f, COLOR_PRIMARY, bold = true))
                canvas.save()
                canvas.translate(left + 12f, y + 28f)
                valueLayout.draw(canvas)
                canvas.restore()
            }
            y += cardH + 10f
        }
        return y
    }

    private fun dessineFooter(canvas: Canvas, pageNum: Int) {
        val footerY = PAGE_H - MARGIN - 14f
        drawLine(canvas, MARGIN, footerY - 10f, PAGE_W - MARGIN, footerY - 10f, COLOR_BORDER, 0.5f)
        // ── MODIFICATION : "Bilan comportemental" → "Bilan émotionnel" dans le footer ──
        canvas.drawText("Comprendre mon chien  \u2022  Bilan \u00e9motionnel indicatif", MARGIN, footerY, makePaint(8f, COLOR_INK_SOFT))
        val pageLabel = "Page $pageNum / 4"
        val pagePaint = makePaint(8f, COLOR_INK_SOFT)
        canvas.drawText(pageLabel, PAGE_W - MARGIN - pagePaint.measureText(pageLabel), footerY, pagePaint)
    }

    private fun dessineFooterAvecQr(canvas: Canvas) {
        val footerTop = PAGE_H - MARGIN - 90f
        drawCard(canvas, MARGIN, footerTop, PAGE_W - MARGIN, PAGE_H - MARGIN + 2f, COLOR_WARM_BG, COLOR_BORDER, 14f)

        val qrSize = 62
        val qrBitmap = generateQrCode("https://comprendremonchien.fr", 300)
        val qrLeft = (PAGE_W - MARGIN - qrSize - 14f).toInt()
        val qrTop = (footerTop + 14f).toInt()
        canvas.drawBitmap(qrBitmap, null, Rect(qrLeft, qrTop, qrLeft + qrSize, qrTop + qrSize), null)

        val tx = MARGIN + 14f
        canvas.drawText("Document g\u00e9n\u00e9r\u00e9 automatiquement", tx, footerTop + 22f, makePaint(9f, COLOR_INK_SOFT))
        canvas.drawText("Retrouvez l\u2019application pour suivre l\u2019\u00e9volution de votre chien.", tx, footerTop + 40f, makePaint(8.5f, COLOR_INK_SOFT))
        canvas.drawText("Acc\u00e9der \u00e0 l\u2019application \u2014 comprendremonchien.fr", tx, footerTop + 60f, makePaint(9.5f, COLOR_PRIMARY, bold = true))

        val pageLabel = "Page 4 / 4"
        val pagePaint = makePaint(8f, COLOR_INK_SOFT)
        canvas.drawText(pageLabel, PAGE_W - MARGIN - pagePaint.measureText(pageLabel), PAGE_H - MARGIN - 4f, pagePaint)
    }

    // ════════════════════════════════════════════════════════════
    // HELPERS — StaticLayout
    // ════════════════════════════════════════════════════════════

    private fun drawStaticText(
        canvas: Canvas, text: String, x: Float, y: Float,
        maxWidth: Int, paint: Paint,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
    ) {
        val tp = if (paint is TextPaint) paint else TextPaint(paint)
        val sl = makeStaticLayout(text, maxWidth, tp, alignment)
        canvas.save()
        canvas.translate(x, y)
        sl.draw(canvas)
        canvas.restore()
    }

    private fun measureStaticTextHeight(text: String, maxWidth: Int, paint: Paint): Float {
        val tp = if (paint is TextPaint) paint else TextPaint(paint)
        return makeStaticLayout(text, maxWidth, tp).height.toFloat()
    }

    @Suppress("DEPRECATION")
    private fun makeStaticLayout(
        text: String, maxWidth: Int, paint: Paint,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
    ): StaticLayout {
        val tp = if (paint is TextPaint) paint else TextPaint(paint)
        return StaticLayout(text, tp, maxWidth.coerceAtLeast(1), alignment, 1.3f, 0f, false)
    }

    // ════════════════════════════════════════════════════════════
    // HELPERS — Formes
    // ════════════════════════════════════════════════════════════

    private fun drawCard(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, fillColor: Int, borderColor: Int, radius: Float) {
        val rect = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rect, radius, radius, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = fillColor; style = Paint.Style.FILL })
        canvas.drawRoundRect(rect, radius, radius, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = borderColor; style = Paint.Style.STROKE; strokeWidth = 1f })
    }

    private fun drawRoundRect(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, radius: Float, color: Int) {
        canvas.drawRoundRect(RectF(left, top, right, bottom), radius, radius, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; style = Paint.Style.FILL })
    }

    private fun drawRect(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, color: Int) {
        canvas.drawRect(left, top, right, bottom, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; style = Paint.Style.FILL })
    }

    private fun drawLine(canvas: Canvas, x1: Float, y1: Float, x2: Float, y2: Float, color: Int, width: Float) {
        canvas.drawLine(x1, y1, x2, y2, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; strokeWidth = width })
    }

    private fun drawCircle(canvas: Canvas, cx: Float, cy: Float, r: Float, color: Int) {
        canvas.drawCircle(cx, cy, r, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; style = Paint.Style.FILL })
    }

    // ════════════════════════════════════════════════════════════
    // HELPERS — Paint
    // ════════════════════════════════════════════════════════════

    private fun makePaint(size: Float, color: Int, bold: Boolean = false, italic: Boolean = false): TextPaint {
        return TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = size * 1.33f
            this.color = color
            typeface = when {
                bold && italic -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                bold -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                italic -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                else -> Typeface.DEFAULT
            }
        }
    }

    // ════════════════════════════════════════════════════════════
    // HELPERS — Priorité
    // ════════════════════════════════════════════════════════════

    private fun couleurPourPriorite(p: PrioriteAction) = when (p) {
        PrioriteAction.FAIBLE -> COLOR_PRIORITE_FAIBLE
        PrioriteAction.MODEREE -> COLOR_PRIORITE_MODEREE
        PrioriteAction.ELEVEE -> COLOR_PRIORITE_ELEVEE
        PrioriteAction.URGENTE -> COLOR_PRIORITE_URGENTE
    }

    private fun libellePourPriorite(p: PrioriteAction) = when (p) {
        PrioriteAction.FAIBLE -> "Priorit\u00e9 faible"
        PrioriteAction.MODEREE -> "\u00c0 surveiller"
        PrioriteAction.ELEVEE -> "Vigilance renforc\u00e9e"
        PrioriteAction.URGENTE -> "Action rapide"
    }

    private fun aideAEnvisager(analyse: ResultatAnalyse) = when {
        analyse.aDejaMordu -> "Comportementaliste recommandé"
        analyse.prioriteAction == PrioriteAction.URGENTE -> "Professionnel rapidement"
        analyse.prioriteAction == PrioriteAction.ELEVEE -> "Comportementaliste"
        analyse.niveauSituation == NiveauSituation.SENSIBLE -> "Comportementaliste"
        analyse.reactivite >= 70 || analyse.peur >= 70 -> "Comportementaliste"
        analyse.impulsivite >= 70 || analyse.attachement >= 70 -> "\u00c9ducateur canin"
        else -> "\u00c9ducateur canin si besoin"
    }

    // ════════════════════════════════════════════════════════════
    // QR Code
    // ════════════════════════════════════════════════════════════

    private fun generateQrCode(text: String, size: Int): Bitmap {
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) COLOR_PRIMARY else COLOR_WHITE)
            }
        }
        return bitmap
    }
}