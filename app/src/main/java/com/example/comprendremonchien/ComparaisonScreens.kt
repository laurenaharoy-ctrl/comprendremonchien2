package com.laurena.comprendremonchien

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingFlat
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.abs

// ═══════════════════════════════════════════════════════════
// LOGIQUE DE COMPARAISON (partagée avec le PDF)
// ═══════════════════════════════════════════════════════════

/** Écart en dessous duquel un axe est considéré comme stable (sur 100). */
const val SEUIL_STABLE = 5

enum class Tendance { AMELIORATION, STABLE, A_SURVEILLER }

/** Un score qui baisse signifie que la difficulté diminue : c'est une amélioration. */
fun tendanceAxe(avant: Int, apres: Int): Tendance {
    val ecart = apres - avant
    return when {
        abs(ecart) < SEUIL_STABLE -> Tendance.STABLE
        ecart < 0 -> Tendance.AMELIORATION
        else -> Tendance.A_SURVEILLER
    }
}

fun libelleTendance(tendance: Tendance): String = when (tendance) {
    Tendance.AMELIORATION -> tr("En amélioration", "Improving", "Verbessert", "En mejora")
    Tendance.STABLE -> tr("Stable", "Stable", "Stabil", "Estable")
    Tendance.A_SURVEILLER -> tr("À surveiller", "To watch", "Im Auge behalten", "A vigilar")
}

data class EvolutionAxe(val label: String, val avant: Int, val apres: Int) {
    val ecart: Int get() = apres - avant
    val tendance: Tendance get() = tendanceAxe(avant, apres)
}

fun evolutionsAxes(avant: BilanSauvegarde, apres: BilanSauvegarde): List<EvolutionAxe> = listOf(
    EvolutionAxe(strLibelleAxe(Axe.PEUR), avant.peur, apres.peur),
    EvolutionAxe(strLibelleAxe(Axe.ATTACHEMENT), avant.attachement, apres.attachement),
    EvolutionAxe(strLibelleAxe(Axe.IMPULSIVITE), avant.impulsivite, apres.impulsivite),
    EvolutionAxe(strLibelleAxe(Axe.REACTIVITE), avant.reactivite, apres.reactivite)
)

fun texteEcart(ecart: Int): String = if (ecart > 0) "+$ecart" else "$ecart"

fun texteAvertissementComparaison(): String = tr(
    "Cette comparaison montre l'évolution des comportements observés par le propriétaire. Elle ne remplace pas l'avis d'un vétérinaire ou d'un comportementaliste.",
    "This comparison shows how the behaviors observed by the owner have changed. It does not replace the advice of a veterinarian or a behaviorist.",
    "Dieser Vergleich zeigt die Entwicklung der vom Halter beobachteten Verhaltensweisen. Er ersetzt nicht den Rat eines Tierarztes oder Verhaltensberaters.",
    "Esta comparación muestra la evolución de los comportamientos observados por el propietario. No sustituye la opinión de un veterinario o de un especialista en comportamiento."
)

/** Phrase de résumé : combien d'axes en amélioration, stables, à surveiller. */
fun texteResumeComparaison(evolutions: List<EvolutionAxe>): String {
    val amelio = evolutions.count { it.tendance == Tendance.AMELIORATION }
    val stables = evolutions.count { it.tendance == Tendance.STABLE }
    val surveiller = evolutions.count { it.tendance == Tendance.A_SURVEILLER }
    return tr(
        "En résumé, sur les ${evolutions.size} axes : $amelio en amélioration, $stables stable${if (stables > 1) "s" else ""}, $surveiller à surveiller.",
        "In short, across the ${evolutions.size} axes: $amelio improving, $stables stable, $surveiller to watch.",
        "Kurz gesagt, von den ${evolutions.size} Achsen: $amelio verbessert, $stables stabil, $surveiller im Auge zu behalten.",
        "En resumen, en los ${evolutions.size} ejes: $amelio en mejora, $stables estable${if (stables > 1) "s" else ""}, $surveiller a vigilar."
    )
}

fun texteBilanDeReference(): String = tr("Bilan de référence", "Reference assessment", "Referenz-Einschätzung", "Evaluación de referencia")

@Composable
private fun couleurTendance(tendance: Tendance): Color = when (tendance) {
    Tendance.AMELIORATION -> Color(0xFF4F7A4A)
    Tendance.STABLE -> MaterialTheme.colorScheme.onSurfaceVariant
    Tendance.A_SURVEILLER -> PremiumPalette.PrioriteUrgente
}

private fun iconeTendance(tendance: Tendance): ImageVector = when (tendance) {
    // Flèche vers le haut = ça va mieux, vers le bas = à surveiller
    Tendance.AMELIORATION -> Icons.AutoMirrored.Rounded.TrendingUp
    Tendance.STABLE -> Icons.AutoMirrored.Rounded.TrendingFlat
    Tendance.A_SURVEILLER -> Icons.AutoMirrored.Rounded.TrendingDown
}

// ═══════════════════════════════════════════════════════════
// HISTORIQUE AVEC MODE « COMPARER »
// ═══════════════════════════════════════════════════════════

@Composable
fun HistoriqueComparableScreen(
    modifier: Modifier = Modifier,
    bilans: List<BilanSauvegarde>,
    onOuvrirBilan: (String) -> Unit,
    onSupprimerBilan: (String) -> Unit,
    onSupprimerTout: () -> Unit,
    onBasculerReference: (String) -> Unit,
    onComparer: (idAvant: String, idApres: String) -> Unit,
    // Nom de l'animal quand la liste ne contient que ses bilans (titre « Bilans de … »)
    nomAnimal: String? = null
) {
    var showConfirmSupprimerTout by remember { mutableStateOf(false) }
    var modeComparer by remember { mutableStateOf(false) }
    val selection = remember { mutableStateListOf<String>() }

    // La comparaison n'est proposée que si au moins un animal a deux bilans ou plus
    val comparaisonPossible = bilans.groupBy { it.cleAnimal() }.values.any { it.size >= 2 }
    val animalSelectionne = selection.firstNotNullOfOrNull { id -> bilans.firstOrNull { it.id == id }?.cleAnimal() }

    fun activerModeComparer() {
        selection.clear()
        // Pré-sélection : le bilan de référence et le dernier bilan du même animal
        val animal = bilans.groupBy { it.cleAnimal() }.values.firstOrNull { it.size >= 2 } ?: return
        val parDate = animal.sortedBy { it.dateTimestamp }
        val dernier = parDate.last()
        // Si la référence est le dernier bilan, on le compare au bilan précédent
        val reference = parDate.firstOrNull { it.estReference && it.id != dernier.id } ?: parDate[parDate.size - 2]
        selection.add(reference.id)
        selection.add(dernier.id)
        modeComparer = true
    }

    if (showConfirmSupprimerTout) {
        AlertDialog(
            onDismissRequest = { showConfirmSupprimerTout = false },
            title = {
                Text(
                    if (nomAnimal != null) tr("Supprimer les bilans de $nomAnimal ?", "Delete $nomAnimal's assessments?", "Einschätzungen von $nomAnimal löschen?", "¿Eliminar las evaluaciones de $nomAnimal?")
                    else tr("Supprimer tout l'historique ?", "Delete all history?", "Gesamten Verlauf löschen?", "¿Eliminar todo el historial?")
                )
            },
            text = { Text(tr("Cette action est irréversible. Ces bilans seront supprimés définitivement.", "This action cannot be undone. These assessments will be permanently deleted.", "Diese Aktion kann nicht rückgängig gemacht werden. Diese Einschätzungen werden endgültig gelöscht.", "Esta acción no se puede deshacer. Estas evaluaciones se eliminarán definitivamente.")) },
            confirmButton = {
                Button(onClick = { onSupprimerTout(); showConfirmSupprimerTout = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text(tr("Supprimer", "Delete", "Löschen", "Eliminar"), color = Color.White) }
            },
            dismissButton = { TextButton(onClick = { showConfirmSupprimerTout = false }) { Text(tr("Annuler", "Cancel", "Abbrechen", "Cancelar")) } }
        )
    }

    EditorialContainer(
        modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            PremiumCard(centered = true) {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(PremiumPalette.Primary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.History, contentDescription = null, tint = PremiumPalette.Primary, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    if (nomAnimal != null) texteBilansDe(nomAnimal)
                    else tr("Historique des bilans", "Assessment history", "Verlauf der Einschätzungen", "Historial de evaluaciones"),
                    style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    if (bilans.isEmpty()) {
                        tr("Aucun bilan sauvegardé pour l'instant.", "No saved assessments yet.", "Noch keine gespeicherte Einschätzung.", "Todavía no hay evaluaciones guardadas.")
                    } else {
                        tr("${bilans.size} bilan${if (bilans.size > 1) "s" else ""} sauvegardé${if (bilans.size > 1) "s" else ""}", "${bilans.size} saved assessment${if (bilans.size > 1) "s" else ""}", "${bilans.size} gespeicherte Einschätzung${if (bilans.size > 1) "en" else ""}", if (bilans.size > 1) "${bilans.size} evaluaciones guardadas" else "${bilans.size} evaluación guardada")
                    },
                    textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (bilans.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        tr("Le premier bilan est le bilan de référence. Pour en choisir un autre, touchez son étoile.", "The first assessment is the reference. To choose another one, tap its star.", "Die erste Einschätzung ist die Referenz. Um eine andere zu wählen, tippen Sie auf ihren Stern.", "La primera evaluación es la de referencia. Para elegir otra, toca su estrella."),
                        textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (comparaisonPossible) {
                    Spacer(modifier = Modifier.height(14.dp))
                    if (!modeComparer) {
                        Button(
                            onClick = { activerModeComparer() },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.Primary, contentColor = Color.White)
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.CompareArrows, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(tr("Comparer deux bilans", "Compare two assessments", "Zwei Einschätzungen vergleichen", "Comparar dos evaluaciones"), fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Text(
                            tr("Cochez deux bilans du même animal.", "Tick two assessments for the same animal.", "Wählen Sie zwei Einschätzungen desselben Tieres aus.", "Marca dos evaluaciones del mismo animal."),
                            textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(
                                onClick = { modeComparer = false; selection.clear() },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(tr("Annuler", "Cancel", "Abbrechen", "Cancelar"))
                            }
                            Button(
                                onClick = {
                                    val choisis = selection.mapNotNull { id -> bilans.firstOrNull { it.id == id } }.sortedBy { it.dateTimestamp }
                                    if (choisis.size == 2) onComparer(choisis[0].id, choisis[1].id)
                                },
                                enabled = selection.size == 2,
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.Primary, contentColor = Color.White)
                            ) {
                                Text(tr("Comparer", "Compare", "Vergleichen", "Comparar"), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            if (bilans.isEmpty()) {
                PremiumCard(centered = true) {
                    Icon(Icons.Rounded.Pets, contentDescription = null, tint = PremiumPalette.Accent, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        tr("Les bilans réalisés apparaîtront ici automatiquement après chaque questionnaire complété.", "Completed assessments will appear here automatically after each questionnaire.", "Ihre Einschätzungen erscheinen hier automatisch nach jedem abgeschlossenen Fragebogen.", "Las evaluaciones aparecerán aquí automáticamente después de cada cuestionario completado."),
                        textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                bilans.forEach { bilan ->
                    val estCoche = bilan.id in selection
                    // Une fois un bilan coché, seuls les bilans du même animal restent sélectionnables (2 au maximum)
                    val cochable = estCoche || (selection.size < 2 && (animalSelectionne == null || animalSelectionne == bilan.cleAnimal()))
                    BilanComparableItem(
                        bilan = bilan,
                        modeComparer = modeComparer,
                        estCoche = estCoche,
                        cochable = cochable,
                        onCocher = {
                            if (estCoche) selection.remove(bilan.id)
                            else if (cochable) selection.add(bilan.id)
                        },
                        onOuvrir = { onOuvrirBilan(bilan.id) },
                        onSupprimer = { selection.remove(bilan.id); onSupprimerBilan(bilan.id) },
                        onBasculerReference = { onBasculerReference(bilan.id) }
                    )
                }
                if (!modeComparer) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = { showConfirmSupprimerTout = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) Color(0xFF3D1209) else Color(0xFFFFF0EC),
                            contentColor = PremiumPalette.PrioriteUrgente
                        )
                    ) {
                        Icon(Icons.Rounded.DeleteSweep, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (nomAnimal != null) tr("Supprimer les bilans de $nomAnimal", "Delete $nomAnimal's assessments", "Einschätzungen von $nomAnimal löschen", "Eliminar las evaluaciones de $nomAnimal")
                            else tr("Supprimer tout l'historique", "Delete all history", "Gesamten Verlauf löschen", "Eliminar todo el historial"),
                            fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun texteBilansDe(nom: String): String = tr("Bilans de $nom", "$nom's assessments", "Einschätzungen von $nom", "Evaluaciones de $nom")

// ═══════════════════════════════════════════════════════════
// LISTE DES ANIMAUX (quand l'historique contient plusieurs animaux)
// ═══════════════════════════════════════════════════════════

@Composable
fun AnimauxHistoriqueScreen(
    modifier: Modifier = Modifier,
    bilans: List<BilanSauvegarde>,
    onOuvrirAnimal: (cle: String) -> Unit,
    onSupprimerTout: () -> Unit
) {
    var showConfirmSupprimerTout by remember { mutableStateOf(false) }
    // Animaux triés par bilan le plus récent
    val animaux = bilans.groupBy { it.cleAnimal() }.values.sortedByDescending { groupe -> groupe.maxOf { it.dateTimestamp } }

    if (showConfirmSupprimerTout) {
        AlertDialog(
            onDismissRequest = { showConfirmSupprimerTout = false },
            title = { Text(tr("Supprimer tout l'historique ?", "Delete all history?", "Gesamten Verlauf löschen?", "¿Eliminar todo el historial?")) },
            text = { Text(tr("Cette action est irréversible. Tous les bilans de tous les animaux seront supprimés définitivement.", "This action cannot be undone. All assessments for all animals will be permanently deleted.", "Diese Aktion kann nicht rückgängig gemacht werden. Alle Einschätzungen aller Tiere werden endgültig gelöscht.", "Esta acción no se puede deshacer. Todas las evaluaciones de todos los animales se eliminarán definitivamente.")) },
            confirmButton = {
                Button(onClick = { onSupprimerTout(); showConfirmSupprimerTout = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text(tr("Supprimer tout", "Delete all", "Alles löschen", "Eliminar todo"), color = Color.White) }
            },
            dismissButton = { TextButton(onClick = { showConfirmSupprimerTout = false }) { Text(tr("Annuler", "Cancel", "Abbrechen", "Cancelar")) } }
        )
    }

    EditorialContainer(
        modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            PremiumCard(centered = true) {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(PremiumPalette.Primary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.History, contentDescription = null, tint = PremiumPalette.Primary, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    tr("Historique des bilans", "Assessment history", "Verlauf der Einschätzungen", "Historial de evaluaciones"),
                    style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    tr("Choisissez un animal pour voir ses bilans.", "Choose an animal to see its assessments.", "Wählen Sie ein Tier, um seine Einschätzungen zu sehen.", "Elige un animal para ver sus evaluaciones."),
                    textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            animaux.forEach { groupe ->
                val dernier = groupe.maxByOrNull { it.dateTimestamp } ?: return@forEach
                val nb = groupe.size
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onOuvrirAnimal(dernier.cleAnimal()) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft),
                    border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(42.dp).clip(CircleShape).background(PremiumPalette.Primary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Pets, contentDescription = null, tint = PremiumPalette.Primary, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(texteBilansDe(dernier.nomChien), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                tr(
                                    "$nb bilan${if (nb > 1) "s" else ""} · dernier le ${dernier.date}",
                                    "$nb assessment${if (nb > 1) "s" else ""} · latest on ${dernier.date}",
                                    "$nb Einschätzung${if (nb > 1) "en" else ""} · zuletzt am ${dernier.date}",
                                    "$nb evaluaci${if (nb > 1) "ones" else "ón"} · la última el ${dernier.date}"
                                ),
                                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { showConfirmSupprimerTout = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color(0xFF3D1209) else Color(0xFFFFF0EC),
                    contentColor = PremiumPalette.PrioriteUrgente
                )
            ) {
                Icon(Icons.Rounded.DeleteSweep, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(tr("Supprimer tout l'historique", "Delete all history", "Gesamten Verlauf löschen", "Eliminar todo el historial"), fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BilanComparableItem(
    bilan: BilanSauvegarde,
    modeComparer: Boolean,
    estCoche: Boolean,
    cochable: Boolean,
    onCocher: () -> Unit,
    onOuvrir: () -> Unit,
    onSupprimer: () -> Unit,
    onBasculerReference: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }
    var showConfirmReference by remember { mutableStateOf(false) }

    if (showConfirmReference) {
        AlertDialog(
            onDismissRequest = { showConfirmReference = false },
            title = { Text(tr("Changer le bilan de référence ?", "Change the reference assessment?", "Referenz-Einschätzung ändern?", "¿Cambiar la evaluación de referencia?")) },
            text = {
                Text(tr(
                    "Le bilan du ${bilan.date} deviendra le nouveau bilan de référence de ${bilan.nomChien}. Les prochaines comparaisons partiront de ce bilan.",
                    "The assessment from ${bilan.date} will become ${bilan.nomChien}'s new reference. Future comparisons will start from this assessment.",
                    "Die Einschätzung vom ${bilan.date} wird die neue Referenz von ${bilan.nomChien}. Künftige Vergleiche gehen von dieser Einschätzung aus.",
                    "La evaluación del ${bilan.date} será la nueva referencia de ${bilan.nomChien}. Las próximas comparaciones partirán de esta evaluación."
                ))
            },
            confirmButton = {
                Button(onClick = { onBasculerReference(); showConfirmReference = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.Primary)
                ) { Text(tr("Oui", "Yes", "Ja", "Sí"), color = Color.White) }
            },
            dismissButton = { TextButton(onClick = { showConfirmReference = false }) { Text(tr("Non", "No", "Nein", "No")) } }
        )
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text(tr("Supprimer ce bilan ?", "Delete this assessment?", "Diese Einschätzung löschen?", "¿Eliminar esta evaluación?")) },
            text = { Text(tr("Le bilan de ${bilan.nomChien} du ${bilan.date} sera supprimé définitivement.", "The assessment for ${bilan.nomChien} on ${bilan.date} will be permanently deleted.", "Die Einschätzung von ${bilan.nomChien} vom ${bilan.date} wird endgültig gelöscht.", "La evaluación de ${bilan.nomChien} del ${bilan.date} se eliminará definitivamente.")) },
            confirmButton = {
                Button(onClick = { onSupprimer(); showConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.PrioriteUrgente)
                ) { Text(tr("Supprimer", "Delete", "Löschen", "Eliminar"), color = Color.White) }
            },
            dismissButton = { TextButton(onClick = { showConfirm = false }) { Text(tr("Annuler", "Cancel", "Abbrechen", "Cancelar")) } }
        )
    }

    val priorite = bilan.prioriteActionEnum()
    val couleurPriorite = when (priorite) {
        PrioriteAction.FAIBLE -> PremiumPalette.PrioriteFaible
        PrioriteAction.MODEREE -> PremiumPalette.PrioriteModere
        PrioriteAction.ELEVEE -> PremiumPalette.PrioriteElevee
        PrioriteAction.URGENTE -> PremiumPalette.PrioriteUrgente
    }
    val bordure = when {
        modeComparer && estCoche -> PremiumPalette.Primary
        bilan.aDejaMordu -> PremiumPalette.MorsureBorder
        isSystemInDarkTheme() -> Color(0xFF56433B)
        else -> PremiumPalette.Border
    }

    Card(
        modifier = Modifier.fillMaxWidth()
            .alpha(if (modeComparer && !cochable) 0.45f else 1f)
            .clickable(onClick = if (modeComparer) onCocher else onOuvrir),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft),
        border = BorderStroke(if (modeComparer && estCoche) 2.dp else 1.dp, bordure)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                if (modeComparer) {
                    Checkbox(
                        checked = estCoche,
                        onCheckedChange = { onCocher() },
                        enabled = cochable,
                        colors = CheckboxDefaults.colors(checkedColor = PremiumPalette.Primary)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(bilan.nomChien, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(bilan.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    if (bilan.aDejaMordu) Icon(Icons.Rounded.Warning, contentDescription = null, tint = PremiumPalette.PrioriteUrgente, modifier = Modifier.size(18.dp))
                    // L'étoile pleine indique la référence ; une étoile vide permet de changer de référence (avec confirmation)
                    IconButton(onClick = { if (!bilan.estReference) showConfirmReference = true }, modifier = Modifier.size(34.dp)) {
                        Icon(
                            if (bilan.estReference) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                            contentDescription = texteBilanDeReference(),
                            tint = if (bilan.estReference) PremiumPalette.PrioriteModere else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (!modeComparer) {
                        IconButton(onClick = { showConfirm = true }, modifier = Modifier.size(34.dp)) {
                            Icon(Icons.Rounded.Delete, contentDescription = tr("Supprimer", "Delete", "Löschen", "Eliminar"), tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
            if (bilan.estReference) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(PremiumPalette.PrioriteModere.copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text(texteBilanDeReference(), style = MaterialTheme.typography.labelSmall, color = PremiumPalette.PrioriteModere, fontWeight = FontWeight.SemiBold)
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

// ═══════════════════════════════════════════════════════════
// ÉCRAN DE COMPARAISON
// ═══════════════════════════════════════════════════════════

@Composable
fun ComparaisonBilansScreen(
    modifier: Modifier = Modifier,
    avant: BilanSauvegarde,
    apres: BilanSauvegarde,
    onPartagerPdf: () -> Unit
) {
    val evolutions = evolutionsAxes(avant, apres)

    EditorialContainer(
        modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            PremiumCard(centered = true) {
                EditorialKicker(tr("Comparaison des bilans", "Assessment comparison", "Vergleich der Einschätzungen", "Comparación de evaluaciones"), centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(apres.nomChien, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CarteDate(modifier = Modifier.weight(1f), titre = tr("Avant", "Before", "Vorher", "Antes"), bilan = avant)
                    CarteDate(modifier = Modifier.weight(1f), titre = tr("Après", "After", "Nachher", "Después"), bilan = apres)
                }
            }

            PremiumCard {
                EditorialKicker(tr("Évolution par axe", "Change by axis", "Entwicklung je Achse", "Evolución por eje"), centered = true)
                Spacer(modifier = Modifier.height(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    evolutions.forEach { LigneEvolution(it) }
                }
            }

            PremiumCard {
                EditorialKicker(tr("Synthèse", "Summary", "Zusammenfassung", "Resumen"), centered = true)
                Spacer(modifier = Modifier.height(12.dp))
                Text(texteResumeComparaison(evolutions), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = PremiumPalette.Primary)
                Spacer(modifier = Modifier.height(12.dp))
                LigneAvantApres(
                    label = tr("Priorité", "Priority", "Priorität", "Prioridad"),
                    avant = textePrioriteAction(avant.prioriteActionEnum()),
                    apres = textePrioriteAction(apres.prioriteActionEnum())
                )
                Spacer(modifier = Modifier.height(10.dp))
                LigneAvantApres(
                    label = tr("Situation", "Situation", "Situation", "Situación"),
                    avant = texteNiveauSituation(avant.niveauSituationEnum()),
                    apres = texteNiveauSituation(apres.niveauSituationEnum())
                )
            }

            PremiumCard(centered = true) {
                EditorialKicker(tr("Rappel", "Note", "Zur Erinnerung", "Recuerda"), centered = true)
                Spacer(modifier = Modifier.height(8.dp))
                Text(texteAvertissementComparaison(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = onPartagerPdf,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.Primary, contentColor = Color.White)
            ) {
                Icon(Icons.Rounded.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(tr("Partager le PDF", "Share PDF", "PDF teilen", "Compartir el PDF"), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CarteDate(modifier: Modifier = Modifier, titre: String, bilan: BilanSauvegarde) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(18.dp))
            .background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titre.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PremiumPalette.PrimarySoft)
        Spacer(modifier = Modifier.height(4.dp))
        Text(bilan.date, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        if (bilan.estReference) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(texteBilanDeReference(), style = MaterialTheme.typography.labelSmall, color = PremiumPalette.PrioriteModere, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun LigneEvolution(evolution: EvolutionAxe) {
    val couleur = couleurTendance(evolution.tendance)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(evolution.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Icon(iconeTendance(evolution.tendance), contentDescription = null, tint = couleur, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(libelleTendance(evolution.tendance), style = MaterialTheme.typography.labelLarge, color = couleur, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        BarreScore(label = tr("Avant", "Before", "Vorher", "Antes"), score = evolution.avant, couleur = PremiumPalette.Accent)
        Spacer(modifier = Modifier.height(4.dp))
        BarreScore(label = tr("Après", "After", "Nachher", "Después"), score = evolution.apres, couleur = PremiumPalette.PrimarySoft)
    }
}

@Composable
private fun BarreScore(label: String, score: Int, couleur: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(64.dp))
        Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5))) {
            Box(modifier = Modifier.fillMaxWidth((score / 100f).coerceIn(0f, 1f)).height(6.dp).clip(RoundedCornerShape(999.dp)).background(couleur))
        }
        Text(
            QuestionnaireEngine.libelleNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(score)),
            style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(96.dp), textAlign = TextAlign.End
        )
    }
}

@Composable
private fun LigneAvantApres(label: String, avant: String, apres: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            tr("Avant : $avant", "Before: $avant", "Vorher: $avant", "Antes: $avant"),
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            tr("Après : $apres", "After: $apres", "Nachher: $apres", "Después: $apres"),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}