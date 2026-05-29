package com.laurena.comprendremonchien

import androidx.compose.animation.animateColorAsState
import com.laurena.comprendremonchien.PrioriteAction
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// ═══════════════════════════════════════════════════════════
// TOP BAR AVEC ACTIONS
// ═══════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumTopBarWithActions(
    title: String,
    onBack: (() -> Unit)?,
    actions: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = strContentDescRetour(),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
    )
}

// ═══════════════════════════════════════════════════════════
// FEEDBACK SCREEN
// ═══════════════════════════════════════════════════════════

data class CategorieSignalement(
    val id: String,
    val label: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun FeedbackScreen(
    modifier: Modifier = Modifier,
    ecranActuel: String,
    onEnvoyer: (categorie: String, ecran: String, message: String) -> Unit,
    onRetour: () -> Unit
) {
    val categories = strCategoriesSignalement()
    val ecrans = strEcransFeedback()

    var categorieSelectionnee by remember { mutableStateOf<CategorieSignalement?>(null) }
    var ecranSelectionne by remember { mutableStateOf(ecranActuel.ifBlank { ecrans.last() }) }
    var message by remember { mutableStateOf("") }
    var envoye by remember { mutableStateOf(false) }

    val peutEnvoyer = categorieSelectionnee != null && message.trim().length >= 10

    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (envoye) {
                PremiumCard(centered = true) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PremiumPalette.Primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.Send,
                            contentDescription = null,
                            tint = PremiumPalette.Primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        strFeedbackMerciTitre(),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        strFeedbackMerciTexte(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    PrimaryGlowButton(text = strBtnRetour(), onClick = onRetour)
                }
            } else {
                PremiumCard(centered = true) {
                    EditorialKicker(strFeedbackKicker(), centered = true)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        strFeedbackTitre(),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        strFeedbackSousTitre(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                PremiumCard {
                    EditorialKicker(strFeedbackTypeKicker())
                    Spacer(modifier = Modifier.height(12.dp))
                    categories.forEach { categorie ->
                        val selected = categorieSelectionnee?.id == categorie.id
                        val bgColor by animateColorAsState(
                            targetValue = if (selected) PremiumPalette.Accent.copy(alpha = 0.18f)
                            else if (isSystemInDarkTheme()) Color(0xFF231B17) else Color(0xFFF8F4EE),
                            animationSpec = tween(200),
                            label = "cat_bg_${categorie.id}"
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(bgColor)
                                .border(
                                    1.dp,
                                    if (selected) PremiumPalette.Primary
                                    else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { categorieSelectionnee = categorie }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selected) PremiumPalette.Primary.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    categorie.icon,
                                    contentDescription = null,
                                    tint = if (selected) PremiumPalette.Primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    categorie.label,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected) PremiumPalette.Primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    categorie.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                PremiumCard {
                    EditorialKicker(strFeedbackEcranKicker())
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ecrans.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { ecran ->
                                    val selected = ecranSelectionne == ecran
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (selected) PremiumPalette.Accent.copy(alpha = 0.18f)
                                                else if (isSystemInDarkTheme()) Color(0xFF231B17)
                                                else Color(0xFFF0E5DC)
                                            )
                                            .border(
                                                1.dp,
                                                if (selected) PremiumPalette.Primary
                                                else MaterialTheme.colorScheme.outline,
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable { ecranSelectionne = ecran }
                                            .padding(vertical = 10.dp, horizontal = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            ecran,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (selected) PremiumPalette.Primary
                                            else MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center,
                                            fontWeight = if (selected) FontWeight.SemiBold
                                            else FontWeight.Normal
                                        )
                                    }
                                }
                                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                PremiumCard {
                    EditorialKicker(strFeedbackDescriptionKicker())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        strFeedbackDescriptionTexte(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        placeholder = { Text(strFeedbackPlaceholder()) },
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PremiumPalette.Primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = PremiumPalette.Primary,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    if (message.trim().isNotEmpty() && message.trim().length < 10) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            strFeedbackDetailsHint(),
                            style = MaterialTheme.typography.bodySmall,
                            color = PremiumPalette.PrioriteModere
                        )
                    }
                }

                PrimaryGlowButton(
                    text = strBtnEnvoyerSignalement(),
                    onClick = {
                        if (peutEnvoyer) {
                            onEnvoyer(
                                categorieSelectionnee!!.label,
                                ecranSelectionne,
                                message.trim()
                            )
                            envoye = true
                        }
                    },
                    enabled = peutEnvoyer,
                    leading = {
                        Icon(
                            Icons.Rounded.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )

                if (!peutEnvoyer) {
                    Text(
                        buildString {
                            if (categorieSelectionnee == null) append(strFeedbackHintCategorie())
                            if (categorieSelectionnee == null && message.trim().length < 10) append(strFeedbackHintEt())
                            if (message.trim().length < 10) append(strFeedbackHintMessage())
                            append(strFeedbackHintPour())
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                PremiumCard(centered = true) {
                    EditorialKicker(strFeedbackConfidentialiteKicker(), centered = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        strFeedbackConfidentialiteTexte(),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// HISTORIQUE SCREEN
// ═══════════════════════════════════════════════════════════

@Composable
fun HistoriqueScreen(
    modifier: Modifier = Modifier,
    bilans: List<BilanSauvegarde>,
    onOuvrirBilan: (String) -> Unit,
    onSupprimerBilan: (String) -> Unit,
    onSupprimerTout: () -> Unit
) {
    var showConfirmSupprimerTout by remember { mutableStateOf(false) }

    if (showConfirmSupprimerTout) {
        AlertDialog(
            onDismissRequest = { showConfirmSupprimerTout = false },
            title = { Text(strHistoriqueSupprimerToutTitre()) },
            text = { Text(strHistoriqueSupprimerToutTexte()) },
            confirmButton = {
                Button(
                    onClick = { onSupprimerTout(); showConfirmSupprimerTout = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text(strBtnSupprimerTout(), color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmSupprimerTout = false }) { Text(strBtnAnnuler()) }
            }
        )
    }

    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PremiumCard(centered = true) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PremiumPalette.Primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.History,
                        contentDescription = null,
                        tint = PremiumPalette.Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    strScreenHistorique(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    if (bilans.isEmpty()) strHistoriqueAucun()
                    else strHistoriqueNbBilans(bilans.size),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (bilans.isEmpty()) {
                PremiumCard(centered = true) {
                    Icon(
                        Icons.Rounded.Pets,
                        contentDescription = null,
                        tint = PremiumPalette.Accent,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        strHistoriqueVideTexte(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                bilans.forEach { bilan ->
                    BilanHistoriqueItem(
                        bilan = bilan,
                        onOuvrir = { onOuvrirBilan(bilan.id) },
                        onSupprimer = { onSupprimerBilan(bilan.id) }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { showConfirmSupprimerTout = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color(0xFF3D1209)
                        else Color(0xFFFFF0EC),
                        contentColor = PremiumPalette.PrioriteUrgente
                    )
                ) {
                    Icon(Icons.Rounded.DeleteSweep, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strBtnSupprimerTout(), fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BilanHistoriqueItem(
    bilan: BilanSauvegarde,
    onOuvrir: () -> Unit,
    onSupprimer: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text(strHistoriqueSupprimerTitre()) },
            text = { Text(strHistoriqueSupprimerTexte(bilan.nomChien, bilan.date)) },
            confirmButton = {
                Button(
                    onClick = { onSupprimer(); showConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text(strBtnSupprimer(), color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text(strBtnAnnuler()) }
            }
        )
    }

    val priorite = bilan.prioriteActionEnum()
    val couleurPriorite = when (priorite) {
        PrioriteAction.FAIBLE -> PremiumPalette.PrioriteFaible
        PrioriteAction.MODEREE -> PremiumPalette.PrioriteModere
        PrioriteAction.ELEVEE -> PremiumPalette.PrioriteElevee
        PrioriteAction.URGENTE -> PremiumPalette.PrioriteUrgente
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onOuvrir),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft
        ),
        border = BorderStroke(
            1.dp,
            if (bilan.aDejaMordu) PremiumPalette.MorsureBorder
            else if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(bilan.nomChien, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(bilan.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (bilan.aDejaMordu) {
                        Icon(Icons.Rounded.Warning, contentDescription = strContentDescMorsure(), tint = PremiumPalette.PrioriteUrgente, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = { showConfirm = true }, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Rounded.Delete, contentDescription = strContentDescSupprimer(), tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text(bilan.profilType, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(couleurPriorite.copy(alpha = 0.12f)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text(strPrioriteAction(priorite), style = MaterialTheme.typography.labelSmall, color = couleurPriorite, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            QuatreAxesMini(peur = bilan.peur, attachement = bilan.attachement, impulsivite = bilan.impulsivite, reactivite = bilan.reactivite)
        }
    }
}

@Composable
fun QuatreAxesMini(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int) {
    val axes = listOf(
        strLibelleAxe(Axe.PEUR) to peur,
        strLibelleAxe(Axe.ATTACHEMENT) to attachement,
        strLibelleAxe(Axe.IMPULSIVITE) to impulsivite,
        strLibelleAxe(Axe.REACTIVITE) to reactivite
    )
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        axes.forEach { (label, score) ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(72.dp))
                Box(modifier = Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5))) {
                    Box(modifier = Modifier.fillMaxWidth((score / 100f).coerceIn(0f, 1f)).height(5.dp).clip(RoundedCornerShape(999.dp)).background(PremiumPalette.PrimarySoft))
                }
                Text(
                    strNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(score)),
                    style = MaterialTheme.typography.labelSmall,
                    color = PremiumPalette.PrimarySoft,
                    modifier = Modifier.width(76.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// HISTORIQUE DETAIL SCREEN
// ═══════════════════════════════════════════════════════════

@Composable
fun HistoriqueDetailScreen(
    modifier: Modifier = Modifier,
    bilan: BilanSauvegarde,
    onSupprimer: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text(strHistoriqueSupprimerTitre()) },
            text = { Text(if (isEnglish()) "This action cannot be undone." else "Cette action est irréversible.") },
            confirmButton = {
                Button(
                    onClick = { onSupprimer(); showConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text(strBtnSupprimer(), color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text(strBtnAnnuler()) }
            }
        )
    }

    val priorite = bilan.prioriteActionEnum()
    val situation = bilan.niveauSituationEnum()

    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (bilan.aDejaMordu) AlerteMorsureCard(bilan.nomChien)

            PremiumCard(centered = true) {
                EditorialKicker(strHistoriqueDetailKicker(), centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(bilan.nomChien, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(6.dp))
                Text(bilan.date, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                AccentChip(bilan.profilType)
            }

            PremiumCard(centered = true) {
                EditorialKicker(strHistoriqueSynthese(), centered = true)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)) {
                    val couleur = when (priorite) {
                        PrioriteAction.FAIBLE -> PremiumPalette.PrioriteFaible
                        PrioriteAction.MODEREE -> PremiumPalette.PrioriteModere
                        PrioriteAction.ELEVEE -> PremiumPalette.PrioriteElevee
                        PrioriteAction.URGENTE -> PremiumPalette.PrioriteUrgente
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(couleur.copy(alpha = 0.12f)).padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(
                            if (isEnglish()) "Priority: ${strPrioriteAction(priorite)}"
                            else "Priorité : ${strPrioriteAction(priorite)}",
                            color = couleur, fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC)).padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(strNiveauSituation(situation), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            PremiumCard(centered = true) {
                EditorialKicker(strHistoriqueCarte(), centered = true)
                Spacer(modifier = Modifier.height(14.dp))
                QuatreAxesMini(peur = bilan.peur, attachement = bilan.attachement, impulsivite = bilan.impulsivite, reactivite = bilan.reactivite)
            }

            PremiumCard(centered = true) {
                EditorialKicker(strHistoriqueLecture(), centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(bilan.hypothesePrincipale, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            }

            PremiumCard(centered = true) {
                EditorialKicker(strHistoriquePiste(), centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(bilan.conseilPrincipal, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
            }

            PremiumCard(centered = true) {
                EditorialKicker(strHistoriqueRappelKicker(), centered = true)
                Spacer(modifier = Modifier.height(8.dp))
                Text(strHistoriqueRappelTexte(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = { showConfirm = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color(0xFF3D1209) else Color(0xFFFFF0EC),
                    contentColor = PremiumPalette.PrioriteUrgente
                )
            ) {
                Icon(Icons.Rounded.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(strBtnSupprimerBilan(), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}