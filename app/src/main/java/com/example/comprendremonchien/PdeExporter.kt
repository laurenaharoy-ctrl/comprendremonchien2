package com.example.comprendremonchien2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

object PdfExporter {

    fun exporterBilanPdf(
        context: Context,
        nomChien: String,
        analyse: ResultatAnalyse
    ): File {

        val document = PdfDocument()

        val pageWidth = 595
        val pageHeight = 842
        val margin = 40f
        val contentWidth = pageWidth - (margin * 2)

        val footerReservedHeight = 110f
        val contentBottomLimit = pageHeight - margin - footerReservedHeight

        val colorPrimary = Color.parseColor("#8E4A2D")
        val colorPrimarySoft = Color.parseColor("#B86A4A")
        val colorAccent = Color.parseColor("#D9A58F")
        val colorInk = Color.parseColor("#33231D")
        val colorInkSoft = Color.parseColor("#75584C")
        val colorBorder = Color.parseColor("#E6D7CC")
        val colorWarmBg = Color.parseColor("#FCF8F5")
        val colorWarmBgAlt = Color.parseColor("#F4ECE5")
        val colorCard = Color.parseColor("#FFFDFB")
        val colorShadow = Color.parseColor("#14000000")
        val colorError = Color.parseColor("#B94A48")
        val colorSuccess = Color.parseColor("#4CAF50")
        val colorWarning = Color.parseColor("#FF9800")
        val colorDanger = Color.parseColor("#D32F2F")

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 24f
            color = colorPrimary
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val bigNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 26f
            color = colorPrimary
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 12f
            color = colorInkSoft
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val sectionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 16f
            color = colorPrimary
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val sectionSmallPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 13f
            color = colorPrimary
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val subLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 11f
            color = colorInkSoft
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 10.9f
            color = colorInk
        }

        val bodyBoldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 10.9f
            color = colorInk
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val smallPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 9.4f
            color = colorInkSoft
        }

        val tinyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 8.6f
            color = colorInkSoft
        }

        val alertPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 10.9f
            color = colorError
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = colorBorder
            strokeWidth = 1f
        }

        val cardStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = colorBorder
            strokeWidth = 1f
            style = Paint.Style.STROKE
        }

        val footerLinkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 10.5f
            color = colorPrimary
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = colorShadow
            style = Paint.Style.FILL
        }

        var y = margin

        fun newPage() {
            document.finishPage(page)
            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            page = document.startPage(pageInfo)
            canvas = page.canvas
            y = margin
        }

        fun ensureSpace(space: Float) {
            if (y + space > contentBottomLimit) newPage()
        }

        fun wrapLines(text: String, paint: Paint, maxWidth: Float): List<String> {
            if (text.isBlank()) return listOf("")
            val words = text.trim().split(Regex("\\s+"))
            val lines = mutableListOf<String>()
            var line = ""

            for (word in words) {
                val test = if (line.isEmpty()) word else "$line $word"
                if (paint.measureText(test) <= maxWidth) {
                    line = test
                } else {
                    if (line.isNotEmpty()) lines += line
                    line = word
                }
            }

            if (line.isNotEmpty()) lines += line
            return lines
        }

        fun paragraphHeight(text: String, paint: Paint = bodyPaint, spacing: Float = 8f): Float {
            val paragraphs = text.split("\n")
            var total = 0f

            paragraphs.forEachIndexed { index, raw ->
                val p = raw.trim()
                if (p.isEmpty()) {
                    total += paint.textSize + 4f
                } else {
                    val lines = wrapLines(p, paint, contentWidth)
                    total += lines.size * (paint.textSize + 6f)
                    total += spacing
                }

                if (index != paragraphs.lastIndex) {
                    total += 4f
                }
            }
            return total
        }

        fun drawParagraph(text: String, spacing: Float = 8f, paint: Paint = bodyPaint) {
            val paragraphs = text.split("\n")

            paragraphs.forEachIndexed { paragraphIndex, rawParagraph ->
                val p = rawParagraph.trim()

                if (p.isEmpty()) {
                    ensureSpace(paint.textSize + 8f)
                    y += paint.textSize + 3f
                } else {
                    val lines = wrapLines(p, paint, contentWidth)

                    lines.forEach { line ->
                        ensureSpace(paint.textSize + 10f)
                        canvas.drawText(line, margin, y, paint)
                        y += paint.textSize + 6f
                    }

                    y += spacing
                }

                if (paragraphIndex != paragraphs.lastIndex) {
                    ensureSpace(6f)
                    y += 4f
                }
            }
        }

        fun drawLine(spacingAfter: Float = 16f) {
            ensureSpace(16f)
            canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
            y += spacingAfter
        }

        fun drawCentered(text: String, paint: Paint, space: Float = 18f) {
            ensureSpace(28f)
            val width = paint.measureText(text)
            canvas.drawText(text, (pageWidth - width) / 2f, y, paint)
            y += space
        }

        fun drawCard(
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            fillColor: Int = colorCard,
            borderColor: Int = colorBorder,
            radius: Float = 16f
        ) {
            canvas.drawRoundRect(
                left + 2f,
                top + 3f,
                right + 2f,
                bottom + 3f,
                radius,
                radius,
                shadowPaint
            )

            canvas.drawRoundRect(
                left,
                top,
                right,
                bottom,
                radius,
                radius,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = fillColor
                    style = Paint.Style.FILL
                }
            )

            canvas.drawRoundRect(
                left,
                top,
                right,
                bottom,
                radius,
                radius,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = borderColor
                    style = Paint.Style.STROKE
                    strokeWidth = 1f
                }
            )
        }

        fun drawBullet(text: String, paint: Paint = bodyPaint) {
            val bulletIndent = 18f
            val textStart = margin + bulletIndent
            val maxWidth = contentWidth - bulletIndent
            val lines = wrapLines(text, paint, maxWidth)
            val lineHeight = paint.textSize + 6f
            val blockHeight = (lines.size * lineHeight) + 4f

            ensureSpace(blockHeight + 2f)

            val firstLineY = y
            val centerY = firstLineY - (paint.textSize * 0.33f)

            canvas.drawCircle(
                margin + 6f,
                centerY,
                2.3f,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = colorPrimarySoft
                    style = Paint.Style.FILL
                }
            )

            lines.forEach { line ->
                canvas.drawText(line, textStart, y, paint)
                y += lineHeight
            }

            y += 2f
        }

        fun bullets(items: List<String>, paint: Paint = bodyPaint) {
            items.forEach { drawBullet(it, paint) }
            y += 8f
        }

        fun section(title: String, introSpace: Float = 14f) {
            val minBlockAfterTitle = 30f
            ensureSpace(introSpace + 34f + minBlockAfterTitle)
            y += introSpace
            canvas.drawText(title, margin, y, sectionPaint)
            y += 10f
            canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
            y += 16f
        }

        fun keyValue(label: String, value: String) {
            val labelWidth = 120f
            val lines = wrapLines(value, bodyPaint, contentWidth - labelWidth)
            val lineHeight = bodyPaint.textSize + 5f
            val estimatedHeight = max(24f, (lines.size * lineHeight) + 8f)

            ensureSpace(estimatedHeight)

            canvas.drawText(label, margin, y, subLabelPaint)

            lines.forEachIndexed { index, line ->
                if (index == 0) {
                    canvas.drawText(line, margin + labelWidth, y, bodyPaint)
                } else {
                    y += lineHeight
                    canvas.drawText(line, margin + labelWidth, y, bodyPaint)
                }
            }

            y += 17f
        }

        fun drawSimpleBox(
            text: String,
            fillColor: Int,
            borderColor: Int,
            textPaint: Paint = bodyPaint,
            radius: Float = 14f,
            paddingHorizontal: Float = 14f,
            paddingVertical: Float = 12f
        ) {
            val lines = text.split("\n")
                .flatMap { wrapLines(it, textPaint, contentWidth - (paddingHorizontal * 2)) }

            val lineHeight = textPaint.textSize + 6f
            val estimatedHeight =
                (lines.size * lineHeight) + (paddingVertical * 2) + 2f

            ensureSpace(estimatedHeight + 10f)

            val top = y
            val bottom = y + estimatedHeight

            drawCard(
                left = margin,
                top = top,
                right = pageWidth - margin,
                bottom = bottom,
                fillColor = fillColor,
                borderColor = borderColor,
                radius = radius
            )

            y = top + paddingVertical + textPaint.textSize

            lines.forEach { line ->
                canvas.drawText(line, margin + paddingHorizontal, y, textPaint)
                y += lineHeight
            }

            y += 14f
        }

        fun drawPriorityBox(
            title: String,
            levelLabel: String,
            message: String,
            items: List<String>,
            borderColor: Int,
            fillColor: Int
        ) {
            val barWidth = 8f
            val padding = 16f
            val badgeHeight = 22f
            val badgeWidth = 110f

            val messageLines = wrapLines(message, bodyPaint, contentWidth - padding * 2)
            val itemLineHeight = bodyPaint.textSize + 6f
            val itemHeights = items.map { wrapLines(it, bodyPaint, contentWidth - padding * 2 - 18f).size * itemLineHeight + 2f }
            val totalItemsHeight = itemHeights.sum()

            val boxHeight =
                20f +
                        24f +
                        badgeHeight +
                        10f +
                        (messageLines.size * (bodyPaint.textSize + 6f)) +
                        10f +
                        totalItemsHeight +
                        20f

            ensureSpace(boxHeight + 10f)

            val top = y
            val bottom = y + boxHeight
            val left = margin
            val right = pageWidth - margin

            drawCard(
                left = left,
                top = top,
                right = right,
                bottom = bottom,
                fillColor = fillColor,
                borderColor = borderColor,
                radius = 18f
            )

            canvas.drawRoundRect(
                left,
                top,
                left + barWidth,
                bottom,
                18f,
                18f,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = borderColor
                    style = Paint.Style.FILL
                }
            )

            var innerY = top + 22f
            val contentLeft = left + padding
            val contentRight = right - padding

            canvas.drawText(title, contentLeft, innerY, sectionSmallPaint)
            innerY += 26f

            val badgeLeft = contentLeft
            val badgeTop = innerY - 14f
            val badgeRight = badgeLeft + badgeWidth
            val badgeBottom = badgeTop + badgeHeight

            canvas.drawRoundRect(
                badgeLeft,
                badgeTop,
                badgeRight,
                badgeBottom,
                11f,
                11f,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = borderColor
                    style = Paint.Style.FILL
                }
            )

            val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 9.5f
                color = Color.WHITE
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            canvas.drawText(levelLabel, badgeLeft + 12f, badgeTop + 15f, badgeTextPaint)
            innerY += 20f

            messageLines.forEach { line ->
                canvas.drawText(line, contentLeft, innerY, bodyPaint)
                innerY += bodyPaint.textSize + 6f
            }

            innerY += 6f

            items.forEach { item ->
                val lines = wrapLines(item, bodyPaint, contentRight - contentLeft - 18f)
                val bulletY = innerY - (bodyPaint.textSize * 0.35f)

                canvas.drawCircle(
                    contentLeft + 5f,
                    bulletY,
                    2.2f,
                    Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = colorPrimary
                        style = Paint.Style.FILL
                    }
                )

                lines.forEach { line ->
                    canvas.drawText(line, contentLeft + 16f, innerY, bodyPaint)
                    innerY += bodyPaint.textSize + 6f
                }

                innerY += 2f
            }

            y = bottom + 16f
        }

        fun drawInfoGrid(items: List<Pair<String, String>>) {
            val colGap = 12f
            val cardWidth = (contentWidth - colGap) / 2f
            val labelTopPadding = 16f
            val valueTopPadding = 36f
            val bottomPadding = 16f

            items.chunked(2).forEach { row ->
                val rowHeights = row.map { (_, value) ->
                    val valueLines = wrapLines(value, bodyBoldPaint, cardWidth - 20f)
                    val lineHeight = bodyBoldPaint.textSize + 5f
                    labelTopPadding + 8f + (valueLines.size * lineHeight) + bottomPadding
                }
                val cardHeight = rowHeights.maxOrNull() ?: 60f

                ensureSpace(cardHeight + 12f)
                val rowTop = y

                row.forEachIndexed { index, (label, value) ->
                    val left = margin + index * (cardWidth + colGap)
                    val top = rowTop
                    val right = left + cardWidth
                    val bottom = rowTop + cardHeight

                    drawCard(
                        left = left,
                        top = top,
                        right = right,
                        bottom = bottom,
                        fillColor = colorCard,
                        borderColor = colorBorder,
                        radius = 14f
                    )

                    canvas.drawText(label, left + 10f, top + labelTopPadding, smallPaint)

                    var localY = top + valueTopPadding
                    val valueLines = wrapLines(value, bodyBoldPaint, cardWidth - 20f)
                    valueLines.forEach { line ->
                        canvas.drawText(line, left + 10f, localY, bodyBoldPaint)
                        localY += bodyBoldPaint.textSize + 5f
                    }
                }

                y += cardHeight + 12f
            }

            y += 4f
        }

        fun drawHeaderCard(
            nom: String,
            date: String,
            phrase: String,
            statut: String,
            statutColor: Int
        ) {
            val top = y
            val height = 138f

            ensureSpace(height + 16f)

            drawCard(
                left = margin,
                top = top,
                right = pageWidth - margin,
                bottom = top + height,
                fillColor = colorWarmBg,
                borderColor = colorBorder,
                radius = 20f
            )

            val metaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 9.8f
                color = colorInkSoft
            }

            val phrasePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 11f
                color = colorInk
            }

            canvas.drawText(date, pageWidth - margin - 105f, top + 20f, metaPaint)
            canvas.drawText("COMPRENDRE MON CHIEN", margin + 18f, top + 22f, tinyPaint)
            canvas.drawText(nom, margin + 18f, top + 54f, bigNamePaint)
            canvas.drawText("Bilan comportemental", margin + 18f, top + 78f, subtitlePaint)

            val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 9.6f
                color = Color.WHITE
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            val badgeLabel = statut.uppercase(Locale.getDefault())
            val badgeWidth = max(96f, badgeTextPaint.measureText(badgeLabel) + 24f)

            canvas.drawRoundRect(
                margin + 18f,
                top + 90f,
                margin + 18f + badgeWidth,
                top + 112f,
                11f,
                11f,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = statutColor
                    style = Paint.Style.FILL
                }
            )

            canvas.drawText(badgeLabel, margin + 30f, top + 105f, badgeTextPaint)

            val phraseLines = wrapLines(
                phrase,
                phrasePaint,
                pageWidth - margin - (margin + 18f) - 24f
            )

            var phraseY = top + 128f
            phraseLines.take(2).forEach { line ->
                canvas.drawText(line, margin + 18f, phraseY, phrasePaint)
                phraseY += phrasePaint.textSize + 5f
            }

            y = top + height + 18f
        }

        fun drawFooterQrOnly() {
            val footerTop = pageHeight - margin - 90f
            val footerBottom = pageHeight - margin
            val footerLeft = margin
            val footerRight = pageWidth - margin

            canvas.drawRoundRect(
                footerLeft,
                footerTop,
                footerRight,
                footerBottom,
                16f,
                16f,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = colorWarmBgAlt
                    style = Paint.Style.FILL
                }
            )

            canvas.drawRoundRect(
                footerLeft,
                footerTop,
                footerRight,
                footerBottom,
                16f,
                16f,
                cardStrokePaint
            )

            val qrSize = 64
            val qrBitmap = generateQrCode(
                text = "https://comprendremonchien.fr",
                size = 300
            )

            val qrLeft = (footerRight - qrSize - 14f).toInt()
            val qrTop = (footerTop + 13f).toInt()

            canvas.drawBitmap(
                qrBitmap,
                null,
                Rect(
                    qrLeft,
                    qrTop,
                    qrLeft + qrSize,
                    qrTop + qrSize
                ),
                null
            )

            val textX = footerLeft + 14f
            canvas.drawText("Document généré automatiquement", textX, footerTop + 24f, smallPaint)
            canvas.drawText("Retrouvez l’application pour suivre l’évolution", textX, footerTop + 44f, tinyPaint)
            canvas.drawText("et refaire un bilan plus tard si besoin.", textX, footerTop + 58f, tinyPaint)
            canvas.drawText("Accéder à l’application", textX, footerTop + 76f, footerLinkPaint)
        }

        fun generateQrCode(text: String, size: Int): Bitmap {
            val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

            for (x in 0 until size) {
                for (yy in 0 until size) {
                    bitmap.setPixel(
                        x,
                        yy,
                        if (bitMatrix[x, yy]) colorPrimary else Color.WHITE
                    )
                }
            }

            return bitmap
        }

        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        val nom = nomChienAffiche(nomChien)
        val nomFichierSafe = nom
            .lowercase(Locale.getDefault())
            .replace("\\s+".toRegex(), "_")
            .replace("[^a-z0-9_]+".toRegex(), "")

        val besoinTexte = besoinPrincipal(analyse.problemePrincipal)
            .removePrefix("Besoin principal : ")
            .removeSuffix(".")
            .trim()

        val couleurPrioriteImmediate = when (analyse.prioriteImmediate.niveau) {
            PrioriteAction.FAIBLE -> colorSuccess
            PrioriteAction.MODEREE -> colorWarning
            PrioriteAction.ELEVEE -> colorError
            PrioriteAction.URGENTE -> colorDanger
        }

        val fondPrioriteImmediate = when (analyse.prioriteImmediate.niveau) {
            PrioriteAction.FAIBLE -> Color.parseColor("#EEF8EF")
            PrioriteAction.MODEREE -> Color.parseColor("#FFF5E8")
            PrioriteAction.ELEVEE -> Color.parseColor("#FDEDEC")
            PrioriteAction.URGENTE -> Color.parseColor("#FDE7E6")
        }

        val libellePriorite = when (analyse.prioriteImmediate.niveau) {
            PrioriteAction.FAIBLE -> "Priorité faible"
            PrioriteAction.MODEREE -> "À surveiller"
            PrioriteAction.ELEVEE -> "Vigilance renforcée"
            PrioriteAction.URGENTE -> "Action rapide"
        }

        val aideAEnvisager = when {
            analyse.aDejaMordu -> "Comportementaliste rapidement"
            analyse.prioriteAction == PrioriteAction.URGENTE -> "Accompagnement professionnel rapidement"
            analyse.prioriteAction == PrioriteAction.ELEVEE -> "Comportementaliste"
            analyse.niveauSituation == NiveauSituation.SENSIBLE -> "Comportementaliste"
            analyse.reactivite >= 70 || analyse.peur >= 70 -> "Comportementaliste"
            analyse.impulsivite >= 70 || analyse.attachement >= 70 -> "Éducateur canin ou comportementaliste"
            else -> "Éducateur canin si besoin"
        }

        drawHeaderCard(
            nom = nom,
            date = date,
            phrase = analyse.profil.phraseHumaine,
            statut = libellePriorite,
            statutColor = couleurPrioriteImmediate
        )

        section("Priorité immédiate", introSpace = 0f)
        drawPriorityBox(
            title = analyse.prioriteImmediate.titre,
            levelLabel = libellePriorite,
            message = analyse.prioriteImmediate.message,
            items = analyse.prioriteImmediate.actionsImmediates,
            borderColor = couleurPrioriteImmediate,
            fillColor = fondPrioriteImmediate
        )

        section("En bref")
        drawParagraph(analyse.profil.phraseHumaine, spacing = 10f)

        drawInfoGrid(
            listOf(
                "Axe principal" to libelleAxe(analyse.problemePrincipal),
                "Situation" to texteNiveauSituation(analyse.niveauSituation),
                "Besoin principal" to besoinTexte,
                "Aide à envisager" to aideAEnvisager
            )
        )

        section("Pourquoi ce résultat")
        drawParagraph(
            "Les éléments qui pèsent le plus dans cette lecture sont les suivants.",
            8f,
            bodyPaint
        )

        drawParagraph("Raisons principales", 4f, bodyBoldPaint)
        bullets(analyse.explicationResultat.raisonsPrincipales)

        if (analyse.explicationResultat.facteursAggravants.isNotEmpty()) {
            drawParagraph("Ce qui peut aggraver", 4f, bodyBoldPaint)
            bullets(analyse.explicationResultat.facteursAggravants)
        }

        if (analyse.explicationResultat.facteursProtecteurs.isNotEmpty()) {
            drawParagraph("Ce qui protège déjà", 4f, bodyBoldPaint)
            bullets(analyse.explicationResultat.facteursProtecteurs)
        }

        section("Hypothèse principale")
        drawSimpleBox(
            text = analyse.hypothesePrincipale,
            fillColor = colorCard,
            borderColor = colorAccent,
            textPaint = bodyPaint
        )

        section("Lecture")
        drawParagraph(analyse.explicationPrincipale)

        section("Profil")
        keyValue("Type de profil", analyse.profil.profilType)
        keyValue("Score global", "${analyse.profil.scoreGlobal}/100")
        drawParagraph(analyse.profil.resume)

        y += 2f
        keyValue(
            "Peur",
            "${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauPeur)} (${analyse.peur}%)"
        )
        keyValue(
            "Attachement",
            "${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauAttachement)} (${analyse.attachement}%)"
        )
        keyValue(
            "Impulsivité",
            "${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauImpulsivite)} (${analyse.impulsivite}%)"
        )
        keyValue(
            "Réactivité",
            "${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauReactivite)} (${analyse.reactivite}%)"
        )

        if (analyse.problemesImportants.isNotEmpty()) {
            drawParagraph("Points les plus marqués", 4f, bodyBoldPaint)
            bullets(analyse.problemesImportants.map { libelleAxe(it) })
        }

        section("Premier levier utile")
        drawSimpleBox(
            text = analyse.conseilPrincipal,
            fillColor = colorWarmBgAlt,
            borderColor = colorAccent,
            textPaint = bodyPaint
        )

        section("Situation actuelle")
        drawParagraph(analyse.messageSituation)
        drawParagraph(analyse.raisonSituation)

        if (analyse.aDejaMordu) {
            drawParagraph(
                "Il y a déjà eu morsure. Il vaut mieux ne pas banaliser la situation.",
                6f,
                alertPaint
            )
        }

        analyse.messageAide
            ?.takeIf { it.isNotBlank() && !analyse.aDejaMordu }
            ?.let {
                drawParagraph(it, 6f, alertPaint)
            }

        section("Plan d'action")
        drawParagraph("À faire", 4f, bodyBoldPaint)
        bullets(analyse.planAction.aFaire)

        drawParagraph("À éviter", 4f, bodyBoldPaint)
        bullets(analyse.planAction.aEviter)

        drawParagraph("À observer", 4f, bodyBoldPaint)
        bullets(analyse.planAction.aObserver)

        if (analyse.conseilsPratiques.isNotEmpty()) {
            section("Conseils complémentaires")
            bullets(analyse.conseilsPratiques)
        }

        if (analyse.facteursAggravants.isNotEmpty() || analyse.facteursProtecteurs.isNotEmpty()) {
            section("Facteurs repérés")

            if (analyse.facteursAggravants.isNotEmpty()) {
                drawParagraph("Ce qui peut aggraver", 4f, bodyBoldPaint)
                bullets(analyse.facteursAggravants)
            }

            if (analyse.facteursProtecteurs.isNotEmpty()) {
                drawParagraph("Ce qui protège déjà", 4f, bodyBoldPaint)
                bullets(analyse.facteursProtecteurs)
            }
        }

        section("Vigilance et aide")
        drawParagraph(texteVigilance(analyse.vigilance, nom))
        drawParagraph(
            "Cette synthèse peut servir de base de travail à présenter à un vétérinaire, un éducateur canin ou un comportementaliste animalier."
        )
        drawParagraph(
            "Ce bilan aide à repérer la situation, mais ne remplace pas un vétérinaire ni un professionnel du comportement.",
            6f,
            bodyPaint
        )

        section("À retenir")
        drawSimpleBox(
            text = """
${nom} présente surtout un profil ${analyse.profil.profilType.lowercase(Locale.getDefault())}.
La situation actuelle est évaluée comme ${texteNiveauSituation(analyse.niveauSituation).lowercase(Locale.getDefault())}.
Le premier levier utile est : ${analyse.conseilPrincipal}
            """.trimIndent(),
            fillColor = colorWarmBg,
            borderColor = colorAccent,
            textPaint = bodyPaint
        )

        section("Conclusion")
        drawParagraph(
            "L’objectif n’est pas d’étiqueter $nom, mais d’aider à mieux lire ce qui se passe et à avancer de manière plus adaptée, plus concrète et plus rassurante."
        )

        ensureSpace(120f)
        y += 8f
        drawFooterQrOnly()

        document.finishPage(page)

        val file = File(
            context.cacheDir,
            "bilan_${nomFichierSafe.ifBlank { "chien" }}.pdf"
        )

        FileOutputStream(file).use {
            document.writeTo(it)
        }

        document.close()

        return file
    }
}