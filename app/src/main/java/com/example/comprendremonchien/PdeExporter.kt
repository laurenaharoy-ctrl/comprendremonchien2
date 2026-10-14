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

    private val COLOR_PRIORITE_ELEVEE_BG = Color.parseColor("#F2E0D6")

    private val COLOR_MORSURE_TEXTE = Color.parseColor("#B8845A")
    private val COLOR_MORSURE_BG = Color.parseColor("#F5E8DC")

    private val COLOR_LIEN = Color.parseColor("#1155CC")

    // ─── Dimensions ─────────────────────────────────────────────
    private const val PAGE_W = 595
    private const val PAGE_H = 842
    private const val MARGIN = 44f
    private val CONTENT_W = PAGE_W - MARGIN * 2
    private const val FOOTER_H = 60f
    private val CONTENT_BOTTOM = PAGE_H - MARGIN - FOOTER_H - 10f
    private val QR_FOOTER_TOP = PAGE_H - MARGIN - 90f
    private val CONTENT_BOTTOM_QR = QR_FOOTER_TOP - 12f

    fun exporterBilanPdf(context: Context, nomChien: String, analyse: ResultatAnalyse): File {
        val nom = nomChienAffiche(nomChien)
        val date = SimpleDateFormat(tr("dd MMMM yyyy", "dd MMMM yyyy", "dd. MMMM yyyy", "d 'de' MMMM 'de' yyyy"), Locale.getDefault()).format(Date())
        val couleurPriorite = couleurPourPriorite(analyse.prioriteAction)
        val libellePriorite = libellePourPriorite(analyse.prioriteAction)

        // 1er passage : brouillon jetable, uniquement pour compter les pages
        val brouillon = PdfDocument()
        val totalPages = dessineTout(brouillon, nom, date, analyse, couleurPriorite, libellePriorite, 0)
        brouillon.close()

        // 2e passage : le vrai document, avec « Page X / total » correct
        val document = PdfDocument()
        dessineTout(document, nom, date, analyse, couleurPriorite, libellePriorite, totalPages)

        val nomFichierSafe = nom.lowercase(Locale.getDefault())
            .replace("\\s+".toRegex(), "_")
            .replace("[^a-z0-9_]+".toRegex(), "")

        val file = File(context.cacheDir, "bilan_${nomFichierSafe.ifBlank { "chien" }}.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return file
    }

    private fun dessineTout(
        document: PdfDocument, nom: String, date: String, analyse: ResultatAnalyse,
        couleurPriorite: Int, libellePriorite: String, totalPages: Int
    ): Int {
        val pager = Pager(document, totalPages)
        dessineCouverture(pager, nom, date, analyse, couleurPriorite, libellePriorite)
        dessineProfil(pager, nom, analyse)
        dessinePlan(pager, nom, analyse)
        dessineARetenir(pager, nom, analyse, couleurPriorite)
        if (showConsultation()) {
            dessineConsultation(pager)
        }
        pager.closePage()
        return pager.pageNum
    }

    // ════════════════════════════════════════════════════════════
    // PAGINATION — passe automatiquement à la page suivante
    // quand un bloc ne tient plus, au lieu de le couper
    // ════════════════════════════════════════════════════════════
    private class Pager(val document: PdfDocument, val totalPages: Int) {
        var pageNum = 0
        private var page: PdfDocument.Page? = null
        val canvas: Canvas get() = page!!.canvas
        var y = MARGIN
        var bottom = CONTENT_BOTTOM
        var header: String? = null
        private var startY = MARGIN

        fun newPage(title: String?) {
            closePage()
            pageNum++
            page = demarrerPage(document, pageNum)
            header = title
            y = MARGIN
            if (title != null) {
                drawPageHeader(page!!.canvas, title)
                y += 48f
            }
            startY = y
        }

        /** Renvoie true si une nouvelle page a été ouverte. */
        fun ensure(height: Float): Boolean {
            if (y + height > bottom && y > startY) {
                newPage(header)
                return true
            }
            return false
        }

        fun closePage(withQr: Boolean = false) {
            val p = page ?: return
            if (withQr) dessineFooterAvecQr(p.canvas, pageNum, totalPages)
            else dessineFooter(p.canvas, pageNum, totalPages)
            document.finishPage(p)
            page = null
        }
    }

    private fun Pager.sectionTitle(title: String, keepWith: Float = 40f) {
        ensure(48f + keepWith)
        drawSectionTitle(canvas, y, title)
        y += 48f
    }

    /** Hauteur d'une carte (pour garder un titre avec son contenu). */
    private fun cardHeight(text: String, paint: TextPaint): Float =
        measureStaticTextHeight(text, (CONTENT_W - 32f).toInt(), paint) + 32f

    /** Hauteur d'un paragraphe (pour garder un titre avec son contenu). */
    private fun paragraphHeight(text: String, paint: TextPaint): Float =
        measureStaticTextHeight(text, CONTENT_W.toInt(), paint)

    private fun Pager.card(text: String, fill: Int, border: Int, paint: TextPaint, spaceAfter: Float = 24f) {
        val h = cardHeight(text, paint)
        ensure(h)
        drawCard(canvas, MARGIN, y, PAGE_W - MARGIN, y + h, fill, border, 14f)
        drawStaticText(canvas, text, MARGIN + 16f, y + 16f, (CONTENT_W - 32f).toInt(), paint)
        y += h + spaceAfter
    }

    private fun Pager.paragraph(text: String, paint: TextPaint, spaceAfter: Float = 24f) {
        if (text.isBlank()) return
        val h = measureStaticTextHeight(text, CONTENT_W.toInt(), paint)
        ensure(h)
        drawStaticText(canvas, text, MARGIN, y, CONTENT_W.toInt(), paint)
        y += h + spaceAfter
    }

    private fun Pager.label(text: String) {
        ensure(20f + 30f)
        canvas.drawText(text, MARGIN, y, makePaint(11f, COLOR_PRIMARY, bold = true))
        y += 20f
    }

    private fun Pager.bullet(text: String) {
        val h = makeStaticLayout(text, (CONTENT_W - 18f).toInt(), makePaint(10.5f, COLOR_INK)).height.toFloat()
        if (ensure(h)) y += 20f
        y = drawBullet(canvas, y, text)
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 1 — Couverture
    // ════════════════════════════════════════════════════════════
    private fun dessineCouverture(
        pager: Pager, nom: String, date: String,
        analyse: ResultatAnalyse, couleurPriorite: Int, libellePriorite: String
    ) {
        pager.newPage(null)
        val canvas = pager.canvas

        val headerHeight = 190f
        drawRect(canvas, 0f, 0f, PAGE_W.toFloat(), headerHeight, COLOR_WARM_BG)

        drawLine(canvas, MARGIN, 26f, PAGE_W - MARGIN, 26f, COLOR_ACCENT, 0.8f)

        val appTitlePaint = makePaint(11f, COLOR_INK_SOFT, italic = true)
        val appTitle = strAppName()
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

        val sousTitrePaint = makePaint(12f, COLOR_INK_SOFT, italic = true)
        val sousTitre = strPdfBilanEmotionnel()
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

        // Si la couverture déborde, la page suivante reçoit un en-tête
        pager.header = strPdfBilanEmotionnel()
        pager.y = headerHeight + 20f

        drawLine(canvas, MARGIN, pager.y, PAGE_W - MARGIN, pager.y, COLOR_BORDER, 0.5f)
        pager.y += 24f

        pager.sectionTitle(strPdfEnUnCoup(), keepWith = 148f)

        val gridItems = listOf(
            strPdfAxePrincipal() to strLibelleAxe(analyse.problemePrincipal),
            strPdfSituation() to strNiveauSituation(analyse.niveauSituation),
            strPdfBesoin() to strBesoinPrincipal(analyse.problemePrincipal)
                .removePrefix("Besoin principal : ").removePrefix("Main need: ").removePrefix("Hauptbedürfnis: ").removePrefix("Necesidad principal: ").removeSuffix("."),
            strPdfAide() to aideAEnvisager(analyse)
        )
        pager.y = drawInfoGrid(pager.canvas, pager.y, gridItems)
        pager.y += 20f

        pager.card(analyse.messageSituation, COLOR_WARM_BG_ALT, COLOR_BORDER, makePaint(11f, COLOR_INK))
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 2 — Profil & Analyse
    // ════════════════════════════════════════════════════════════
    private fun dessineProfil(pager: Pager, nom: String, analyse: ResultatAnalyse) {
        pager.newPage(strPdfProfil(nom))

        pager.sectionTitle(strPdf4Axes())

        val axes = listOf(
            Triple(strLibelleAxe(Axe.PEUR), analyse.niveauPeur, analyse.peur),
            Triple(strLibelleAxe(Axe.ATTACHEMENT), analyse.niveauAttachement, analyse.attachement),
            Triple(strLibelleAxe(Axe.IMPULSIVITE), analyse.niveauImpulsivite, analyse.impulsivite),
            Triple(strLibelleAxe(Axe.REACTIVITE), analyse.niveauReactivite, analyse.reactivite)
        )
        axes.forEach { (label, niveau, score) ->
            pager.ensure(46f)
            pager.y = drawAxeBar(pager.canvas, pager.y, label, niveau, score)
            pager.y += 12f
        }
        pager.y += 14f

        val texte11 = makePaint(11f, COLOR_INK)

        pager.sectionTitle(strPdfHypothese(), keepWith = minOf(cardHeight(analyse.hypothesePrincipale, texte11), 300f))
        pager.card(analyse.hypothesePrincipale, COLOR_WHITE, COLOR_ACCENT, texte11)

        if (analyse.explicationPrincipale.isNotBlank()) {
            pager.sectionTitle(strPdfSePasse(), keepWith = minOf(paragraphHeight(analyse.explicationPrincipale, texte11), 300f))
            pager.paragraph(analyse.explicationPrincipale, texte11)
        }

        if (analyse.facteursAggravants.isNotEmpty() || analyse.facteursProtecteurs.isNotEmpty()) {
            pager.sectionTitle(strPdfFacteurs())

            if (analyse.facteursAggravants.isNotEmpty()) {
                pager.label(strPdfAggravants())
                analyse.facteursAggravants.forEach { pager.bullet(it) }
                pager.y += 10f
            }

            if (analyse.facteursProtecteurs.isNotEmpty()) {
                pager.label(strPdfProtecteurs())
                analyse.facteursProtecteurs.forEach { pager.bullet(it) }
            }
        }
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 3 — Plan d'action
    // ════════════════════════════════════════════════════════════
    private fun dessinePlan(pager: Pager, nom: String, analyse: ResultatAnalyse) {
        pager.newPage(strPdfPlanAction(nom))

        val texte11 = makePaint(11f, COLOR_INK)
        pager.sectionTitle(strPdfLevier(), keepWith = minOf(cardHeight(analyse.conseilPrincipal, texte11), 300f))
        pager.card(analyse.conseilPrincipal, COLOR_WARM_BG_ALT, COLOR_ACCENT, texte11)

        pager.sectionTitle(strPdfProchainsJours())

        pager.label(strResultatAFaire())
        analyse.planAction.aFaire.forEach { pager.bullet(it) }
        pager.y += 12f

        pager.label(strResultatAEviter())
        analyse.planAction.aEviter.forEach { pager.bullet(it) }
        pager.y += 12f

        pager.label(strResultatAObserver())
        analyse.planAction.aObserver.forEach { pager.bullet(it) }
        pager.y += 18f

        if (analyse.conseilsPratiques.isNotEmpty()) {
            pager.sectionTitle(strPdfConseils())
            analyse.conseilsPratiques.forEach { pager.bullet(it) }
            pager.y += 12f
        }

        if (analyse.aDejaMordu) {
            pager.card(
                strPdfMorsuTexte(), COLOR_MORSURE_BG, COLOR_MORSURE_TEXTE,
                makePaint(11f, COLOR_MORSURE_TEXTE, bold = true), spaceAfter = 10f
            )
        }

        val messageAide = analyse.messageAide
        if (!messageAide.isNullOrBlank() && !analyse.aDejaMordu) {
            pager.card(
                messageAide, COLOR_PRIORITE_ELEVEE_BG, COLOR_PRIORITE_ELEVEE,
                makePaint(11f, COLOR_PRIORITE_ELEVEE, bold = true), spaceAfter = 10f
            )
        }
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 4 — À retenir (avec pied de page QR code)
    // ════════════════════════════════════════════════════════════
    private fun dessineARetenir(
        pager: Pager, nom: String,
        analyse: ResultatAnalyse, couleurPriorite: Int
    ) {
        pager.newPage(strPdfARetenir())
        pager.bottom = CONTENT_BOTTOM_QR

        val recapText = buildString {
            append(strPdfRecapProfil(nom, if (isGerman()) analyse.profil.profilType else analyse.profil.profilType.lowercase(Locale.getDefault())))
            append("\n\n")
            append(strPdfRecapSituation(if (isGerman()) strNiveauSituation(analyse.niveauSituation) else strNiveauSituation(analyse.niveauSituation).lowercase(Locale.getDefault())))
            append("\n\n")
            append(strPdfRecapAxe(if (isGerman()) strLibelleAxe(analyse.problemePrincipal) else strLibelleAxe(analyse.problemePrincipal).lowercase(Locale.getDefault())))
            append("\n\n")
            append(strBesoinPrincipal(analyse.problemePrincipal))
        }

        val recapH = measureStaticTextHeight(
            recapText, (CONTENT_W - 48f).toInt(), makePaint(12f, COLOR_INK)
        ) + 48f

        pager.ensure(recapH)
        val canvas = pager.canvas
        drawCard(canvas, MARGIN, pager.y, PAGE_W - MARGIN, pager.y + recapH, COLOR_WARM_BG, COLOR_BORDER, 16f)
        drawRoundRect(canvas, MARGIN, pager.y, MARGIN + 5f, pager.y + recapH, 16f, couleurPriorite)
        drawStaticText(canvas, recapText, MARGIN + 24f, pager.y + 24f, (CONTENT_W - 48f).toInt(), makePaint(12f, COLOR_INK))
        pager.y += recapH + 32f

        val conclusion = strPdfConclusionTexte(nom)
        val conclusionPaint = makePaint(11f, COLOR_INK)
        pager.sectionTitle(strPdfConclusion(), keepWith = minOf(paragraphHeight(conclusion, conclusionPaint), 300f))
        pager.paragraph(conclusion, conclusionPaint, spaceAfter = 32f)

        val disclaimer = strPdfDisclaimer()
        val disclaimerPaint = makePaint(9.5f, COLOR_INK_SOFT)
        val disclaimerH = measureStaticTextHeight(disclaimer, CONTENT_W.toInt(), disclaimerPaint)
        pager.ensure(16f + disclaimerH)
        drawLine(pager.canvas, MARGIN, pager.y, PAGE_W - MARGIN, pager.y, COLOR_BORDER, 0.5f)
        pager.y += 16f
        drawStaticText(pager.canvas, disclaimer, MARGIN, pager.y, CONTENT_W.toInt(), disclaimerPaint)
        pager.y += disclaimerH

        pager.closePage(withQr = true)
        pager.bottom = CONTENT_BOTTOM
    }

    // ════════════════════════════════════════════════════════════
    // PAGE 5 — Consultation personnalisée (FR uniquement, page dédiée)
    // ════════════════════════════════════════════════════════════
    private fun dessineConsultation(pager: Pager) {
        pager.newPage(strConsultationTitre())
        val canvas = pager.canvas
        val y = MARGIN + 60f

        val titre = strConsultationTitre()
        val sousTitre = strConsultationSousTitre()
        val description = strConsultationDescription()
        val disclaimer = strConsultationDisclaimer()
        val prix = strConsultationPrix()
        val bouton = strConsultationAppel()
        val url = strConsultationTelephonePdf()

        val innerW = (CONTENT_W - 40f).toInt()
        val paintTitre = makePaint(16f, COLOR_PRIMARY, bold = true)
        val paintSousTitre = makePaint(12f, COLOR_PRIMARY_SOFT, bold = true)
        val paintDescription = makePaint(11f, COLOR_INK)
        val paintDisclaimer = makePaint(9.5f, COLOR_INK_SOFT)
        val paintPrix = makePaint(14f, COLOR_INK, bold = true)
        val paintLienLabel = makePaint(10.5f, COLOR_INK, bold = true)
        val paintLien = makePaint(11f, COLOR_LIEN, bold = true)

        val titreH = measureStaticTextHeight(titre, innerW, paintTitre)
        val sousTitreH = measureStaticTextHeight(sousTitre, innerW, paintSousTitre)
        val descriptionH = measureStaticTextHeight(description, innerW, paintDescription)
        val disclaimerH = measureStaticTextHeight(disclaimer, innerW, paintDisclaimer)

        val totalH = 24f + titreH + 12f + sousTitreH + 18f + descriptionH + 20f +
                disclaimerH + 24f + 24f + 18f + 20f + 24f

        val cardTop = y
        val cardBottom = y + totalH
        drawCard(canvas, MARGIN, cardTop, PAGE_W - MARGIN, cardBottom, COLOR_WARM_BG_ALT, COLOR_ACCENT, 18f)

        var cy = cardTop + 24f
        val cx = MARGIN + 20f

        drawStaticText(canvas, titre, cx, cy, innerW, paintTitre)
        cy += titreH + 12f

        drawStaticText(canvas, sousTitre, cx, cy, innerW, paintSousTitre)
        cy += sousTitreH + 18f

        drawStaticText(canvas, description, cx, cy, innerW, paintDescription)
        cy += descriptionH + 20f

        drawStaticText(canvas, disclaimer, cx, cy, innerW, paintDisclaimer)
        cy += disclaimerH + 24f

        canvas.drawText(prix, cx, cy, paintPrix)
        cy += 24f

        val lienLabel = "$bouton :"
        canvas.drawText(lienLabel, cx, cy, paintLienLabel)
        cy += 18f

        canvas.drawText(url, cx, cy, paintLien)
        val underlineY = cy + 2f
        canvas.drawLine(cx, underlineY, cx + paintLien.measureText(url), underlineY,
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = COLOR_LIEN; strokeWidth = 0.8f })
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
        val appLabel = strAppName()
        val appLabelPaint = makePaint(9f, COLOR_INK_SOFT, italic = true)
        canvas.drawText(appLabel, PAGE_W - MARGIN - appLabelPaint.measureText(appLabel), MARGIN + 18f, appLabelPaint)
    }

    private fun drawSectionTitle(canvas: Canvas, y: Float, title: String) {
        drawStaticText(canvas, title, MARGIN, y, CONTENT_W.toInt(), makePaint(13f, COLOR_PRIMARY, bold = true))
        drawLine(canvas, MARGIN, y + 22f, PAGE_W - MARGIN, y + 22f, COLOR_BORDER, 0.6f)
    }

    private fun drawAxeBar(canvas: Canvas, y: Float, label: String, niveau: NiveauAxe, score: Int): Float {
        val libelleNiveau = strNiveauAxe(niveau)
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

    private fun dessineFooter(canvas: Canvas, pageNum: Int, totalPages: Int) {
        val footerY = PAGE_H - MARGIN - 14f
        drawLine(canvas, MARGIN, footerY - 10f, PAGE_W - MARGIN, footerY - 10f, COLOR_BORDER, 0.5f)
        canvas.drawText(strPdfFooter(), MARGIN, footerY, makePaint(8f, COLOR_INK_SOFT))
        val pageLabel = strPdfPage(pageNum, totalPages)
        val pagePaint = makePaint(8f, COLOR_INK_SOFT)
        canvas.drawText(pageLabel, PAGE_W - MARGIN - pagePaint.measureText(pageLabel), footerY, pagePaint)
    }

    private fun dessineFooterAvecQr(canvas: Canvas, pageNum: Int, totalPages: Int) {
        val footerTop = QR_FOOTER_TOP
        drawCard(canvas, MARGIN, footerTop, PAGE_W - MARGIN, PAGE_H - MARGIN + 2f, COLOR_WARM_BG, COLOR_BORDER, 14f)

        val qrSize = 62
        val qrBitmap = generateQrCode("https://play.google.com/store/apps/details?id=com.laurena.comprendremonchien", 300)
        val qrLeft = (PAGE_W - MARGIN - qrSize - 14f).toInt()
        val qrTop = (footerTop + 14f).toInt()
        canvas.drawBitmap(qrBitmap, null, Rect(qrLeft, qrTop, qrLeft + qrSize, qrTop + qrSize), null)

        val tx = MARGIN + 14f
        canvas.drawText(strPdfGenereAuto(), tx, footerTop + 22f, makePaint(9f, COLOR_INK_SOFT))
        canvas.drawText(strPdfRetrouvez(), tx, footerTop + 40f, makePaint(8.5f, COLOR_INK_SOFT))
        canvas.drawText(strPdfAcceder(), tx, footerTop + 60f, makePaint(9.5f, COLOR_PRIMARY, bold = true))

        val pageLabel = strPdfPage(pageNum, totalPages)
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
    // HELPERS — Priorité / Aide
    // ════════════════════════════════════════════════════════════

    private fun couleurPourPriorite(p: PrioriteAction) = when (p) {
        PrioriteAction.FAIBLE -> COLOR_PRIORITE_FAIBLE
        PrioriteAction.MODEREE -> COLOR_PRIORITE_MODEREE
        PrioriteAction.ELEVEE -> COLOR_PRIORITE_ELEVEE
        PrioriteAction.URGENTE -> COLOR_PRIORITE_URGENTE
    }

    private fun libellePourPriorite(p: PrioriteAction) = strPrioriteAction(p)

    private fun aideAEnvisager(analyse: ResultatAnalyse) = when {
        analyse.aDejaMordu -> strPdfAideComportementalisteRec()
        analyse.prioriteAction == PrioriteAction.URGENTE -> strPdfAideProRapide()
        analyse.prioriteAction == PrioriteAction.ELEVEE -> strPdfAideComportementaliste()
        analyse.niveauSituation == NiveauSituation.SENSIBLE -> strPdfAideComportementaliste()
        analyse.reactivite >= 70 || analyse.peur >= 70 -> strPdfAideComportementaliste()
        analyse.impulsivite >= 70 || analyse.attachement >= 70 -> strPdfAideEducateur()
        else -> strPdfAideEducateurBesoin()
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