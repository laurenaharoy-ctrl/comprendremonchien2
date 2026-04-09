package com.example.comprendremonchien

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiNature
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class OnboardingSlide(
    val kicker: String,
    val titre: String,
    val description: String,
    val illustrationType: IllustrationType,
    val features: List<Pair<ImageVector, String>> = emptyList()
)

enum class IllustrationType {
    CHIEN_ASSIS,
    QUATRE_AXES,
    BILAN_COMPLET
}

val onboardingSlides = listOf(
    OnboardingSlide(
        kicker = "Bienvenue",
        titre = "Mieux comprendre votre chien",
        description = "Cette application vous aide à décoder les comportements de votre chien et à obtenir des pistes concrètes adaptées à son profil unique.",
        illustrationType = IllustrationType.CHIEN_ASSIS
    ),
    OnboardingSlide(
        kicker = "Comment ça marche",
        titre = "Un questionnaire, quatre dimensions",
        description = "En quelques minutes, vous explorez les quatre axes qui façonnent le comportement de votre chien au quotidien.",
        illustrationType = IllustrationType.QUATRE_AXES,
        features = listOf(
            Icons.Rounded.Psychology to "Sensibilité émotionnelle",
            Icons.Rounded.Favorite to "Besoin d'attachement",
            Icons.Rounded.EmojiNature to "Gestion de l'excitation",
            Icons.Rounded.Analytics to "Réactivité à l'environnement"
        )
    ),
    OnboardingSlide(
        kicker = "Ce que vous obtenez",
        titre = "Un bilan personnalisé complet",
        description = "À la fin du questionnaire, vous recevez une analyse détaillée avec des conseils concrets, un plan d'action et un PDF à partager avec votre vétérinaire.",
        illustrationType = IllustrationType.BILAN_COMPLET,
        features = listOf(
            Icons.Rounded.CheckCircle to "Analyse comportementale",
            Icons.Rounded.PictureAsPdf to "Export PDF 4 pages",
            Icons.Rounded.History to "Historique des bilans"
        )
    )
)

@Composable
fun OnboardingScreen(
    onTermine: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingSlides.size })
    val scope = rememberCoroutineScope()

    AppBackground {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.Companion.navigationBars)
        ) {
            // ── Bouton Passer ─────────────────────────────────────────────────
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Companion.CenterEnd
            ) {
                TextButton(onClick = onTermine) {
                    Text(
                        "Passer",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // ── Pager ─────────────────────────────────────────────────────────
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.Companion.weight(1f)
            ) { page ->
                OnboardingSlideContent(slide = onboardingSlides[page])
            }

            // ── Indicateurs + bouton ──────────────────────────────────────────
            Column(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Points indicateurs
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(onboardingSlides.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateFloatAsState(
                            targetValue = if (isSelected) 24f else 8f,
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                            label = "dot_$index"
                        )
                        Box(
                            modifier = Modifier.Companion
                                .height(8.dp)
                                .width(width.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) PremiumPalette.Primary
                                    else PremiumPalette.Border
                                )
                        )
                    }
                }

                // Bouton principal
                val estDernierSlide = pagerState.currentPage == onboardingSlides.lastIndex
                PrimaryGlowButton(
                    text = if (estDernierSlide) "Commencer" else "Suivant",
                    onClick = {
                        if (estDernierSlide) {
                            onTermine()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    leading = if (estDernierSlide) {
                        {
                            Icon(
                                Icons.Rounded.Pets,
                                contentDescription = null,
                                tint = Color.Companion.White
                            )
                        }
                    } else null
                )
            }
        }
    }
}

@Composable
fun OnboardingSlideContent(slide: OnboardingSlide) {
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Illustration ──────────────────────────────────────────────────────
        Box(
            modifier = Modifier.Companion
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    Brush.Companion.radialGradient(
                        listOf(
                            PremiumPalette.PaperWarm,
                            PremiumPalette.Paper
                        )
                    )
                ),
            contentAlignment = Alignment.Companion.Center
        ) {
            when (slide.illustrationType) {
                IllustrationType.CHIEN_ASSIS -> ChienIllustration()
                IllustrationType.QUATRE_AXES -> QuatreAxesIllustration()
                IllustrationType.BILAN_COMPLET -> BilanIllustration()
            }
        }

        Spacer(modifier = Modifier.Companion.height(36.dp))

        // ── Kicker ────────────────────────────────────────────────────────────
        Text(
            text = slide.kicker.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Companion.Bold,
            color = PremiumPalette.PrimarySoft,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Companion.Center
        )

        Spacer(modifier = Modifier.Companion.height(10.dp))

        // ── Titre ─────────────────────────────────────────────────────────────
        Text(
            text = slide.titre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Companion.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Companion.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.Companion.height(14.dp))

        // ── Description ───────────────────────────────────────────────────────
        Text(
            text = slide.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Companion.Center,
            lineHeight = 24.sp
        )

        // ── Features ──────────────────────────────────────────────────────────
        if (slide.features.isNotEmpty()) {
            Spacer(modifier = Modifier.Companion.height(22.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                slide.features.forEach { (icon, label) ->
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(PremiumPalette.PaperSoft.copy(alpha = 0.7f))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier.Companion
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PremiumPalette.Primary.copy(alpha = 0.10f)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = PremiumPalette.Primary,
                                modifier = Modifier.Companion.size(18.dp)
                            )
                        }
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Companion.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

// ── Slide 1 : Chien assis stylisé ────────────────────────────────────────────
@Composable
fun ChienIllustration() {
    val primary = PremiumPalette.Primary
    val soft = PremiumPalette.PrimarySoft
    val accent = PremiumPalette.Accent
    val ink = PremiumPalette.Ink

    Canvas(modifier = Modifier.Companion.size(140.dp)) {
        val w = size.width
        val h = size.height
        val stroke =
            Stroke(width = 3.5f, cap = StrokeCap.Companion.Round, join = StrokeJoin.Companion.Round)

        translate(w * 0.5f, h * 0.5f) {
            // Corps (ellipse)
            drawOval(
                color = accent.copy(alpha = 0.35f),
                topLeft = Offset(-28f, -10f),
                size = Size(56f, 44f)
            )
            // Corps contour
            drawOval(
                color = soft,
                topLeft = Offset(-28f, -10f),
                size = Size(56f, 44f),
                style = stroke
            )

            // Tête (cercle)
            drawCircle(
                color = accent.copy(alpha = 0.35f),
                radius = 22f,
                center = Offset(0f, -38f)
            )
            drawCircle(
                color = primary,
                radius = 22f,
                center = Offset(0f, -38f),
                style = stroke
            )

            // Oreilles gauche
            val oreilleGauche = Path().apply {
                moveTo(-14f, -52f)
                cubicTo(-24f, -68f, -36f, -62f, -28f, -48f)
                close()
            }
            drawPath(oreilleGauche, color = soft.copy(alpha = 0.5f))
            drawPath(oreilleGauche, color = primary, style = stroke)

            // Oreille droite
            val oreilleDroite = Path().apply {
                moveTo(14f, -52f)
                cubicTo(24f, -68f, 36f, -62f, 28f, -48f)
                close()
            }
            drawPath(oreilleDroite, color = soft.copy(alpha = 0.5f))
            drawPath(oreilleDroite, color = primary, style = stroke)

            // Yeux
            drawCircle(color = ink, radius = 3.5f, center = Offset(-8f, -40f))
            drawCircle(color = ink, radius = 3.5f, center = Offset(8f, -40f))
            // Reflets yeux
            drawCircle(color = Color.Companion.White, radius = 1.2f, center = Offset(-6.5f, -41.5f))
            drawCircle(color = Color.Companion.White, radius = 1.2f, center = Offset(9.5f, -41.5f))

            // Truffe
            drawOval(
                color = ink,
                topLeft = Offset(-5f, -31f),
                size = Size(10f, 7f)
            )

            // Sourire
            val sourire = Path().apply {
                moveTo(-6f, -24f)
                cubicTo(-3f, -20f, 3f, -20f, 6f, -24f)
            }
            drawPath(
                sourire,
                color = primary,
                style = Stroke(width = 2.5f, cap = StrokeCap.Companion.Round)
            )

            // Queue
            val queue = Path().apply {
                moveTo(28f, 4f)
                cubicTo(44f, -8f, 52f, 4f, 44f, 14f)
            }
            drawPath(
                queue,
                color = soft,
                style = Stroke(width = 4f, cap = StrokeCap.Companion.Round)
            )

            // Pattes avant
            drawRoundRect(
                color = soft.copy(alpha = 0.5f),
                topLeft = Offset(-22f, 26f),
                size = Size(12f, 18f),
                cornerRadius = CornerRadius(6f)
            )
            drawRoundRect(
                color = primary,
                topLeft = Offset(-22f, 26f),
                size = Size(12f, 18f),
                cornerRadius = CornerRadius(6f),
                style = stroke
            )
            drawRoundRect(
                color = soft.copy(alpha = 0.5f),
                topLeft = Offset(10f, 26f),
                size = Size(12f, 18f),
                cornerRadius = CornerRadius(6f)
            )
            drawRoundRect(
                color = primary,
                topLeft = Offset(10f, 26f),
                size = Size(12f, 18f),
                cornerRadius = CornerRadius(6f),
                style = stroke
            )

            // Coeur au dessus
            drawCircle(
                color = PremiumPalette.PrioriteElevee.copy(alpha = 0.15f),
                radius = 10f,
                center = Offset(30f, -52f)
            )
            // Mini coeur simplifié
            drawCircle(
                color = PremiumPalette.PrioriteElevee,
                radius = 4f,
                center = Offset(30f, -54f),
                style = Stroke(width = 2f)
            )
        }
    }
}

// ── Slide 2 : 4 axes en radar simplifié ──────────────────────────────────────
@Composable
fun QuatreAxesIllustration() {
    val primary = PremiumPalette.Primary
    val soft = PremiumPalette.PrimarySoft
    val accent = PremiumPalette.Accent

    Canvas(modifier = Modifier.Companion.size(140.dp)) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.width * 0.38f
        val stroke = Stroke(width = 2.5f, cap = StrokeCap.Companion.Round)

        // Cercles de fond
        listOf(0.33f, 0.66f, 1f).forEach { ratio ->
            drawCircle(
                color = accent.copy(alpha = 0.15f),
                radius = r * ratio,
                center = Offset(cx, cy),
                style = Stroke(width = 1f)
            )
        }

        // Axes
        val angles = listOf(270f, 0f, 90f, 180f)
        val valeurs = listOf(0.75f, 0.55f, 0.80f, 0.45f) // valeurs illustratives
        val labels = listOf("Sensibilité", "Attachement", "Impulsivité", "Réactivité")

        // Lignes des axes
        angles.forEach { angle ->
            val rad = Math.toRadians(angle.toDouble())
            drawLine(
                color = accent.copy(alpha = 0.4f),
                start = Offset(cx, cy),
                end = Offset(
                    cx + (r * Math.cos(rad)).toFloat(),
                    cy + (r * Math.sin(rad)).toFloat()
                ),
                strokeWidth = 1.5f
            )
        }

        // Polygone des valeurs
        val points = angles.mapIndexed { i, angle ->
            val rad = Math.toRadians(angle.toDouble())
            Offset(
                cx + (r * valeurs[i] * Math.cos(rad)).toFloat(),
                cy + (r * valeurs[i] * Math.sin(rad)).toFloat()
            )
        }
        val path = Path().apply {
            moveTo(points[0].x, points[0].y)
            points.drop(1).forEach { lineTo(it.x, it.y) }
            close()
        }
        drawPath(path, color = primary.copy(alpha = 0.20f))
        drawPath(
            path,
            color = primary,
            style = Stroke(
                width = 2.5f,
                cap = StrokeCap.Companion.Round,
                join = StrokeJoin.Companion.Round
            )
        )

        // Points aux sommets
        points.forEach { pt ->
            drawCircle(color = primary, radius = 5f, center = pt)
            drawCircle(color = Color.Companion.White, radius = 2.5f, center = pt)
        }

        // Centre
        drawCircle(color = soft, radius = 5f, center = Offset(cx, cy))
    }
}

// ── Slide 3 : Bilan / PDF stylisé ────────────────────────────────────────────
@Composable
fun BilanIllustration() {
    val primary = PremiumPalette.Primary
    val soft = PremiumPalette.PrimarySoft
    val accent = PremiumPalette.Accent
    val border = PremiumPalette.Border

    Canvas(modifier = Modifier.Companion.size(140.dp)) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 2f, cap = StrokeCap.Companion.Round)

        // Page principale (légèrement inclinée)
        translate(w * 0.5f, h * 0.5f) {
            // Ombre page arrière
            drawRoundRect(
                color = border,
                topLeft = Offset(-28f, -36f),
                size = Size(60f, 76f),
                cornerRadius = CornerRadius(8f)
            )
            // Page principale
            drawRoundRect(
                color = Color.Companion.White,
                topLeft = Offset(-32f, -40f),
                size = Size(60f, 76f),
                cornerRadius = CornerRadius(8f)
            )
            drawRoundRect(
                color = border,
                topLeft = Offset(-32f, -40f),
                size = Size(60f, 76f),
                cornerRadius = CornerRadius(8f),
                style = stroke
            )

            // Bandeau terracotta en haut
            drawRoundRect(
                color = primary.copy(alpha = 0.85f),
                topLeft = Offset(-32f, -40f),
                size = Size(60f, 18f),
                cornerRadius = CornerRadius(8f)
            )

            // Lignes de texte simulées
            listOf(-14f, -4f, 6f, 16f).forEachIndexed { i, y ->
                val w2 = if (i % 2 == 0) 44f else 32f
                drawRoundRect(
                    color = accent.copy(alpha = 0.4f),
                    topLeft = Offset(-24f, y),
                    size = Size(w2, 4f),
                    cornerRadius = CornerRadius(2f)
                )
            }

            // Mini barre de score
            drawRoundRect(
                color = border,
                topLeft = Offset(-24f, 26f),
                size = Size(44f, 5f),
                cornerRadius = CornerRadius(3f)
            )
            drawRoundRect(
                color = soft,
                topLeft = Offset(-24f, 26f),
                size = Size(30f, 5f),
                cornerRadius = CornerRadius(3f)
            )

            // Badge check vert-terracotta
            drawCircle(
                color = primary,
                radius = 14f,
                center = Offset(22f, 28f)
            )
            // Check simplifié
            val check = Path().apply {
                moveTo(15f, 28f)
                lineTo(20f, 34f)
                lineTo(30f, 22f)
            }
            drawPath(
                check,
                color = Color.Companion.White,
                style = Stroke(
                    width = 3f,
                    cap = StrokeCap.Companion.Round,
                    join = StrokeJoin.Companion.Round
                )
            )
        }
    }
}

@Composable
fun AccueilIllustrationCard() {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF231B17) else PremiumPalette.PaperSoft
        ),
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF56433B) else PremiumPalette.Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            // Illustration chien + cercle décoratif
            Box(
                modifier = Modifier.Companion
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.Companion.radialGradient(
                            listOf(
                                PremiumPalette.Accent.copy(alpha = 0.25f),
                                PremiumPalette.PaperWarm.copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Companion.Center
            ) {
                ChienIllustration()
            }

            Spacer(modifier = Modifier.Companion.height(14.dp))

            Text(
                text = "Comprendre mon chien",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Companion.ExtraBold,
                color = PremiumPalette.Primary,
                textAlign = TextAlign.Companion.Center
            )

            Spacer(modifier = Modifier.Companion.height(6.dp))

            Text(
                text = "Analyse comportementale • Conseils personnalisés • Export PDF",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Companion.Center
            )
        }
    }
}