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
                        contentDescription = "Retour",
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

val categoriesSignalement = listOf(
    CategorieSignalement("bug", "Bug / Problème technique", "L'appli plante, un bouton ne fonctionne pas, un écran est bloqué…", Icons.Rounded.BugReport),
    CategorieSignalement("contenu", "Contenu incorrect", "Une information semble erronée, un texte est incompréhensible…", Icons.Rounded.HelpOutline),
    CategorieSignalement("suggestion", "Suggestion", "Une idée pour améliorer l'appli ou ajouter une fonctionnalité…", Icons.Rounded.Lightbulb),
    CategorieSignalement("autre", "Autre", "Tout ce qui ne rentre pas dans les catégories ci-dessus.", Icons.Rounded.MoreHoriz)
)

val ecransDisponibles = listOf(
    "Accueil", "Introduction", "Questionnaire", "Résultat",
    "Dictionnaire comportemental", "Alimentation", "Historique", "Général / Autre"
)

@Composable
fun FeedbackScreen(
    modifier: Modifier = Modifier,
    ecranActuel: String,
    onEnvoyer: (categorie: String, ecran: String, message: String) -> Unit,
    onRetour: () -> Unit
) {
    var categorieSelectionnee by remember { mutableStateOf<CategorieSignalement?>(null) }
    var ecranSelectionne by remember { mutableStateOf(ecranActuel.ifBlank { "Général / Autre" }) }
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
                        "Merci pour votre retour !",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Votre message a bien été transmis. Il contribuera à améliorer l'application.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    PrimaryGlowButton(text = "Retour", onClick = onRetour)
                }
            } else {
                PremiumCard(centered = true) {
                    EditorialKicker("Signalement", centered = true)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Quelque chose ne va pas ?",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Votre retour est précieux. Décrivez le problème ou la suggestion et nous en tiendrons compte.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                PremiumCard {
                    EditorialKicker("Type de signalement")
                    Spacer(modifier = Modifier.height(12.dp))
                    categoriesSignalement.forEach { categorie ->
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
                    EditorialKicker("Écran concerné")
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ecransDisponibles.chunked(2).forEach { row ->
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
                    EditorialKicker("Description")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Décrivez le problème ou votre suggestion avec le plus de détails possible.",
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
                        placeholder = { Text("Ex. : Sur l'écran résultat, le bouton Export PDF ne fonctionne pas…") },
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
                            "Ajoutez quelques détails pour nous aider à comprendre.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PremiumPalette.PrioriteModere
                        )
                    }
                }

                PrimaryGlowButton(
                    text = "Envoyer le signalement",
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
                            if (categorieSelectionnee == null) append("Choisissez une catégorie")
                            if (categorieSelectionnee == null && message.trim().length < 10) append(" et ")
                            if (message.trim().length < 10) append("rédigez un message")
                            append(" pour envoyer.")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                PremiumCard(centered = true) {
                    EditorialKicker("Confidentialité", centered = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Votre signalement est envoyé par email directement depuis votre application. Aucune donnée personnelle n'est collectée automatiquement.",
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
            title = { Text("Supprimer tout l'historique ?") },
            text = { Text("Cette action est irréversible. Tous les bilans sauvegardés seront supprimés définitivement.") },
            confirmButton = {
                Button(
                    onClick = { onSupprimerTout(); showConfirmSupprimerTout = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text("Supprimer tout", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmSupprimerTout = false }) { Text("Annuler") }
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
                    "Historique des bilans",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    if (bilans.isEmpty()) "Aucun bilan sauvegardé pour l'instant."
                    else "${bilans.size} bilan${if (bilans.size > 1) "s" else ""} sauvegardé${if (bilans.size > 1) "s" else ""}",
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
                        "Les bilans réalisés apparaîtront ici automatiquement après chaque questionnaire complété.",
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
                    Text("Supprimer tout l'historique", fontWeight = FontWeight.SemiBold)
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
            title = { Text("Supprimer ce bilan ?") },
            text = { Text("Le bilan de ${bilan.nomChien} du ${bilan.date} sera supprimé définitivement.") },
            confirmButton = {
                Button(
                    onClick = { onSupprimer(); showConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text("Supprimer", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Annuler") }
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
                        Icon(Icons.Rounded.Warning, contentDescription = "Morsure signalée", tint = PremiumPalette.PrioriteUrgente, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = { showConfirm = true }, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text(bilan.profilType, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(couleurPriorite.copy(alpha = 0.12f)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text(textePrioriteAction(priorite), style = MaterialTheme.typography.labelSmall, color = couleurPriorite, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            QuatreAxesMini(peur = bilan.peur, attachement = bilan.attachement, impulsivite = bilan.impulsivite, reactivite = bilan.reactivite)
        }
    }
}

@Composable
fun QuatreAxesMini(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int) {
    val axes = listOf("Sensibilité" to peur, "Attachement" to attachement, "Impulsivité" to impulsivite, "Réactivité" to reactivite)
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        axes.forEach { (label, score) ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(72.dp))
                Box(modifier = Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5))) {
                    Box(modifier = Modifier.fillMaxWidth((score / 100f).coerceIn(0f, 1f)).height(5.dp).clip(RoundedCornerShape(999.dp)).background(PremiumPalette.PrimarySoft))
                }
                Text(QuestionnaireEngine.libelleNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(score)), style = MaterialTheme.typography.labelSmall, color = PremiumPalette.PrimarySoft, modifier = Modifier.width(76.dp), textAlign = TextAlign.End)
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
            title = { Text("Supprimer ce bilan ?") },
            text = { Text("Cette action est irréversible.") },
            confirmButton = {
                Button(
                    onClick = { onSupprimer(); showConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text("Supprimer", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Annuler") }
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
                EditorialKicker("Bilan sauvegardé", centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(bilan.nomChien, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(6.dp))
                Text(bilan.date, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                AccentChip(bilan.profilType)
            }

            PremiumCard(centered = true) {
                EditorialKicker("Synthèse", centered = true)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)) {
                    val couleur = when (priorite) {
                        PrioriteAction.FAIBLE -> PremiumPalette.PrioriteFaible
                        PrioriteAction.MODEREE -> PremiumPalette.PrioriteModere
                        PrioriteAction.ELEVEE -> PremiumPalette.PrioriteElevee
                        PrioriteAction.URGENTE -> PremiumPalette.PrioriteUrgente
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(couleur.copy(alpha = 0.12f)).padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text("Priorité : ${textePrioriteAction(priorite)}", color = couleur, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC)).padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(texteNiveauSituation(situation), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            PremiumCard(centered = true) {
                EditorialKicker("Carte du profil", centered = true)
                Spacer(modifier = Modifier.height(14.dp))
                QuatreAxesMini(peur = bilan.peur, attachement = bilan.attachement, impulsivite = bilan.impulsivite, reactivite = bilan.reactivite)
            }

            PremiumCard(centered = true) {
                EditorialKicker("Lecture principale", centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(bilan.hypothesePrincipale, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            }

            PremiumCard(centered = true) {
                EditorialKicker("Première piste concrète", centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(bilan.conseilPrincipal, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
            }

            PremiumCard(centered = true) {
                EditorialKicker("Rappel", centered = true)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ce bilan est un enregistrement indicatif. La situation de votre chien peut avoir évolué depuis.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
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
                Text("Supprimer ce bilan", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}