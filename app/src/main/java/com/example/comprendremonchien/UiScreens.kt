package com.example.comprendremonchien

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import kotlinx.coroutines.delay

private val LightColors = lightColorScheme(
    primary = Color(0xFF8E4A2D), onPrimary = Color.White,
    background = Color(0xFFF4EFE8), surface = Color(0xFFF8F4EE),
    onSurface = Color(0xFF33231D), onBackground = Color(0xFF33231D),
    surfaceVariant = Color(0xFFF0E5DC), onSurfaceVariant = Color(0xFF75584C),
    outline = Color(0xFFE0D2C6), secondary = Color(0xFFB86A4A),
    tertiary = Color(0xFFD9A58F), error = Color(0xFF8E4A2D)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD39A7F), onPrimary = Color(0xFF3C1F14),
    background = Color(0xFF191411), surface = Color(0xFF231B17),
    onSurface = Color(0xFFF6EEE8), onBackground = Color(0xFFF6EEE8),
    surfaceVariant = Color(0xFF342923), onSurfaceVariant = Color(0xFFD2B9AB),
    outline = Color(0xFF56433B), secondary = Color(0xFFB86A4A),
    tertiary = Color(0xFFD9A58F), error = Color(0xFFD39A7F)
)

object PremiumPalette {
    val Primary = Color(0xFF8E4A2D)
    val PrimarySoft = Color(0xFFB86A4A)
    val Accent = Color(0xFFD9A58F)
    val Paper = Color(0xFFF4EFE8)
    val PaperSoft = Color(0xFFF8F4EE)
    val PaperWarm = Color(0xFFF1E7DE)
    val Ink = Color(0xFF33231D)
    val InkSoft = Color(0xFF75584C)
    val InkMuted = Color(0xFFA2897C)
    val Border = Color(0xFFE0D2C6)
    val Warning = Color(0xFF8E4A2D)
    val PrioriteFaible = Color(0xFF9E8572)
    val PrioriteModere = Color(0xFFB8845A)
    val PrioriteElevee = Color(0xFF8E4A2D)
    val PrioriteUrgente = Color(0xFF6B2D1A)
    val PrioriteFaibleBg = Color(0xFFF4EDE6)
    val PrioriteModereBg = Color(0xFFF5E8DC)
    val PrioriteEleveeBg = Color(0xFFF2E0D6)
    val PrioriteUrgenteBg = Color(0xFFEDD8D0)
    val MorsureBg = Color(0xFF3D1209)
    val MorsureBorder = Color(0xFF8E2A10)
    val MorsuText = Color(0xFFFFF0EC)
}

enum class DictionnaireCategorie(val titre: String) {
    DANGEREUX("Aliments dangereux"),
    AUTORISES("Aliments autorisés"),
    INGESTION("Que faire en cas d'ingestion"),
    DIGESTION("Digestion / herbe / vomissements / selles")
}

data class DictionnaireEntry(val categorie: DictionnaireCategorie, val titre: String, val resume: String, val contenu: String)
data class ComportementEntry(val id: String, val titre: String, val resume: String, val explication: String, val queFaire: String, val aEviter: String)

fun dictionnaireEntries(): List<DictionnaireEntry> = listOf(
    DictionnaireEntry(DictionnaireCategorie.DANGEREUX, "Quels aliments éviter absolument ?", "Certains aliments humains peuvent être franchement problématiques pour le chien.", "Par prudence, il faut éviter les aliments connus pour poser problème comme le chocolat, l'oignon, l'ail, certains raisins, l'alcool, les os cuits ainsi que les aliments très gras, très salés ou fortement assaisonnés. En cas d'ingestion suspecte, surtout si le chien semble mal, il ne faut pas attendre si des symptômes inhabituels apparaissent."),
    DictionnaireEntry(DictionnaireCategorie.DANGEREUX, "Les restes de table sont-ils risqués ?", "Le petit bonus de table peut vite devenir un faux ami.", "Mieux vaut éviter les restes de table trop riches, trop salés, trop gras ou très transformés. Même si le chien les apprécie, ils peuvent déséquilibrer sa ration, favoriser des troubles digestifs, encourager la mendicité et rendre les repas moins stables au quotidien."),
    DictionnaireEntry(DictionnaireCategorie.DANGEREUX, "Pourquoi faut-il éviter les os cuits ?", "Un os cuit peut devenir une petite catastrophe silencieuse.", "Les os cuits peuvent se casser en fragments plus coupants et poser des problèmes digestifs ou mécaniques. Pour rester prudent, il vaut mieux les éviter. En cas d'ingestion suivie de douleur, vomissements, gêne ou comportement inhabituel, il faut demander un avis vétérinaire."),
    DictionnaireEntry(DictionnaireCategorie.AUTORISES, "Peut-on donner des fruits ou légumes ?", "Parfois oui, mais pas en mode buffet libre.", "Certains fruits et légumes peuvent être proposés en petite quantité, selon leur nature et la tolérance du chien. L'idée générale est de rester prudent, d'éviter les aliments connus pour poser problème et de ne pas bouleverser la ration principale. En cas de doute sur un aliment précis, il vaut mieux vérifier avant de donner."),
    DictionnaireEntry(DictionnaireCategorie.AUTORISES, "Friandises : utile ou risqué ?", "Très utiles en éducation, moins drôles quand elles débordent du cadre.", "Les friandises peuvent être utiles si elles restent cohérentes avec la ration globale et si elles sont données en petite quantité. Le risque apparaît quand elles deviennent trop nombreuses, trop riches ou distribuées sans cadre."),
    DictionnaireEntry(DictionnaireCategorie.AUTORISES, "Comment changer ses croquettes ?", "Un changement trop rapide peut transformer l'estomac en tambour de machine à laver.", "Le changement d'aliment doit se faire progressivement sur plusieurs jours. On mélange d'abord une petite quantité du nouvel aliment avec l'ancien, puis on augmente peu à peu la part du nouveau."),
    DictionnaireEntry(DictionnaireCategorie.INGESTION, "Que faire si mon chien a mangé quelque chose de douteux ?", "La meilleure réaction, c'est le calme et la rapidité, pas l'improvisation.", "Si votre chien a mangé un aliment suspect ou inhabituel, il faut d'abord éviter les remèdes maison improvisés. En cas de doute, surtout s'il s'agit d'un aliment connu pour être problématique ou si des symptômes apparaissent, il faut contacter rapidement un vétérinaire."),
    DictionnaireEntry(DictionnaireCategorie.INGESTION, "Quels signes doivent alerter après ingestion ?", "Quand le corps tire l'alarme, il vaut mieux écouter tout de suite.", "Il faut être particulièrement attentif à des vomissements répétés, une diarrhée importante, un abattement, une agitation inhabituelle, des tremblements, une douleur, une gêne respiratoire ou tout changement brutal de comportement."),
    DictionnaireEntry(DictionnaireCategorie.INGESTION, "Faut-il attendre pour voir si ça passe ?", "Parfois attendre rassure, parfois attendre complique.", "Quand l'aliment ingéré est potentiellement dangereux ou quand le chien présente déjà des symptômes, il vaut mieux ne pas temporiser. En cas d'hésitation, il est plus sûr de demander conseil."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "Pourquoi mon chien mange de l'herbe ?", "Un comportement fréquent, souvent banal, mais à surveiller s'il devient répétitif.", "Manger de l'herbe peut avoir plusieurs explications. Certains chiens le font occasionnellement sans que cela soit inquiétant. Si cela devient très fréquent ou s'accompagne de vomissements ou d'abattement, mieux vaut demander un avis vétérinaire."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "Mon chien vomit, est-ce toujours grave ?", "Un vomissement isolé n'a pas le même poids qu'une série en cascade.", "Un vomissement ponctuel peut parfois rester sans gravité apparente, mais des vomissements répétés, associés à de la douleur, de l'abattement, une diarrhée, un refus de boire ou un comportement inhabituel doivent pousser à demander un avis vétérinaire."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "Comment comprendre des selles anormales ?", "Les selles racontent souvent une petite histoire digestive.", "Des selles plus molles, plus fréquentes, inhabituelles en couleur ou accompagnées d'inconfort peuvent refléter une difficulté digestive. Si cela dure ou s'accompagne d'autres symptômes, il faut faire le point avec un vétérinaire."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "Mon chien mange trop vite", "Avaler ses repas comme une fusée mérite parfois un petit ajustement.", "Certains chiens mangent très vite par excitation ou habitude. Cela peut favoriser l'inconfort digestif. Fractionner la ration ou utiliser une gamelle adaptée peut aider."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "Mon chien réclame tout le temps à manger", "La demande peut venir de l'habitude, de l'ennui ou d'une ration mal adaptée.", "Un chien qui réclame souvent ne manque pas forcément de nourriture. La demande peut être liée à l'habitude, à l'ennui, à des friandises trop fréquentes ou à des horaires irréguliers.")
)

fun comportementEntries(): List<ComportementEntry> = listOf(
    ComportementEntry("queue-remue", "Queue qui remue", "Pas toujours synonyme de joie, il faut lire tout le corps.", "Un chien qui remue la queue n'est pas automatiquement heureux. La queue indique surtout un état d'activation émotionnelle.", "Observer l'ensemble du corps avant d'interagir.", "Penser que la queue qui bouge autorise forcément le contact."),
    ComportementEntry("queue-basse", "Queue basse ou rentrée", "Signal fréquent d'inquiétude ou de malaise.", "Quand la queue descend très bas ou se replie sous le ventre, le chien peut être impressionné, stressé ou en retrait.", "Laisser de l'espace, adoucir l'approche et réduire la pression.", "Forcer le chien à avancer, saluer ou rester dans une situation qui le gêne."),
    ComportementEntry("baillement", "Bâillement hors fatigue", "Souvent un signal d'apaisement ou de tension légère.", "Le chien peut bâiller quand il n'est pas fatigué. C'est parfois une manière de réguler son émotion.", "Ralentir, faire une pause, simplifier l'exercice ou l'interaction.", "Réduire ce signal à de la simple fatigue."),
    ComportementEntry("leche-truffe", "Lèchement rapide de truffe", "Petit signal discret de tension ou d'apaisement.", "Ce coup de langue rapide apparaît souvent quand le chien essaie de se calmer ou d'apaiser l'échange.", "Se mettre légèrement de côté et laisser davantage d'initiative au chien.", "Continuer à insister physiquement ou verbalement."),
    ComportementEntry("tourne-tete", "Tourner la tête", "Le chien cherche souvent à éviter la pression.", "Tourner la tête est une façon polie de rendre l'échange moins direct.", "Adoucir sa posture et diminuer la pression sociale.", "Interpréter cela comme de l'ignorance ou un refus d'obéir."),
    ComportementEntry("oreilles-arriere", "Oreilles plaquées en arrière", "Signal à lire avec le reste du corps.", "Des oreilles en arrière peuvent accompagner la peur, l'inconfort ou une émotion intense.", "Lire la posture globale et donner du temps au chien.", "Analyser un seul signal sans tenir compte du contexte."),
    ComportementEntry("corps-fige", "Corps figé", "Signal important, souvent juste avant une réaction.", "Le figement est un arrêt du mouvement. Le chien suspend son comportement car il évalue la situation.", "Interrompre l'approche et augmenter la distance.", "Continuer à toucher ou à approcher un chien figé."),
    ComportementEntry("grognement", "Grognement", "Avertissement utile et précieux.", "Le grognement est une communication claire qui dit que le chien n'est pas à l'aise. Punir ce signal n'aide pas.", "Arrêter la source d'inconfort et analyser calmement la situation.", "Punir, défier ou provoquer le chien."),
    ComportementEntry("montre-dents", "Montrer les dents", "Le niveau d'alerte devient plus élevé.", "Quand le chien montre les dents, il exprime une limite très claire.", "Créer immédiatement de la distance sans gestes brusques.", "Chercher le rapport de force."),
    ComportementEntry("aplati-sol", "Se coucher ventre au sol", "Peut traduire peur, inhibition ou repli.", "Un chien qui s'aplatit essaie souvent de se faire discret dans une situation qu'il vit mal.", "Rendre la situation plus prévisible et plus calme.", "Tirer sur la laisse pour le faire avancer."),
    ComportementEntry("sur-le-dos", "Se mettre sur le dos", "Pas toujours une invitation aux caresses.", "Cette posture peut exprimer de la vulnérabilité ou une tentative d'apaisement.", "Observer avant de toucher, surtout si le chien paraît tendu.", "Caresser automatiquement le ventre."),
    ComportementEntry("patte-levee", "Patte avant levée", "Souvent associée à l'hésitation ou à l'analyse.", "Une patte levée peut montrer que le chien observe, hésite ou évalue la situation.", "Lui laisser du temps pour comprendre.", "Prendre ce signal uniquement pour une posture mignonne."),
    ComportementEntry("secouement", "Secouement du corps", "Façon fréquente d'évacuer une tension.", "Le chien peut se secouer après une rencontre ou une émotion forte.", "Laisser ce moment de relâchement exister.", "Réenchaîner immédiatement sur quelque chose de trop intense."),
    ComportementEntry("haletement", "Halètement sans effort", "Peut révéler stress, chaleur ou inconfort.", "Le halètement n'est pas toujours lié à l'exercice. Il peut accompagner une tension émotionnelle.", "Vérifier le contexte et surveiller si cela se répète.", "Attribuer automatiquement cela à de la simple excitation."),
    ComportementEntry("aboiement-alerte", "Aboiement d'alerte", "Le chien signale une présence ou un changement.", "Face à un bruit ou une arrivée inhabituelle, le chien peut aboyer pour prévenir.", "Rester calme puis rediriger vers un comportement plus posé.", "Crier en retour."),
    ComportementEntry("aboiement-frustration", "Aboiement de frustration", "Le chien gère mal l'attente ou la privation d'accès.", "Quand une porte reste fermée ou qu'une activité attendue tarde, certains chiens aboient par frustration.", "Travailler l'attente progressivement et valoriser le calme.", "Multiplier les ordres quand le chien déborde déjà."),
    ComportementEntry("approche-arc", "Approche en arc de cercle", "Code social poli chez le chien.", "Les chiens équilibrés évitent souvent l'approche frontale. Ils contournent légèrement et gèrent mieux la distance.", "Respecter les approches indirectes, surtout entre chiens.", "Forcer des rencontres frontales, laisse tendue."),
    ComportementEntry("renifle-sol", "Renifler le sol soudainement", "Peut aider le chien à faire baisser la pression.", "Renifler peut être un moyen de décompression dans une situation émotionnellement chargée.", "Laisser ce sas de décompression exister.", "Tirer immédiatement sur la laisse pour récupérer l'attention."),
    ComportementEntry("appel-au-jeu", "Posture d'invitation au jeu", "Avant-main basse, arrière-train relevé, gestes souples.", "La révérence de jeu sert à indiquer une intention ludique.", "Vérifier que l'autre partenaire répond avec plaisir.", "Confondre agitation brusque et vrai jeu partagé."),
    ComportementEntry("suit-humain", "Suit son humain partout", "Peut être banal ou signaler une dépendance.", "Certains chiens suivent par habitude sociale. D'autres montrent un attachement plus anxieux.", "Observer les réactions lors des absences courtes.", "Voir ce comportement uniquement comme une preuve d'amour."),
    ComportementEntry("destruction-absence", "Destructions en absence", "Souvent liées au stress, à l'ennui ou à la solitude.", "Un chien qui détruit quand il est seul n'agit pas par vengeance.", "Revoir la gestion de l'absence et enrichir l'environnement.", "Punir après coup."),
    ComportementEntry("hypervigilance", "Hypervigilance", "Le chien semble constamment sur le qui-vive.", "Un chien hypervigilant surveille beaucoup son environnement et réagit vite aux changements.", "Augmenter la prévisibilité, les temps calmes et la sécurité du quotidien.", "Le saturer de stimulations pour l'habituer trop vite.")
)

fun getComportementEntryById(id: String): ComportementEntry? = comportementEntries().firstOrNull { it.id == id }

@Composable
fun ComprendreMonChienTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = MaterialTheme.typography.copy(
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            titleLarge = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            titleMedium = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        ),
        content = content
    )
}

@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val brush = if (isSystemInDarkTheme())
        Brush.verticalGradient(listOf(Color(0xFF241B17), Color(0xFF1D1613), Color(0xFF171210)))
    else
        Brush.verticalGradient(listOf(Color(0xFFF8F4EE), Color(0xFFF4EFE8), Color(0xFFF1E7DE)))
    Box(modifier = modifier.fillMaxSize().background(brush)) {
        Box(modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.08f).background(Brush.radialGradient(colors = listOf(Color.White, Color.Transparent))))
        content()
    }
}

@Composable
fun EditorialContainer(modifier: Modifier = Modifier, maxWidth: Int = 760, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Column(modifier = Modifier.fillMaxWidth().widthIn(max = maxWidth.dp), content = content)
    }
}

@Composable
fun PremiumCard(modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(22.dp), centered: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft),
        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(contentPadding), horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start) { content() }
    }
}

@Composable
fun AccentChip(text: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC)).padding(horizontal = 12.dp, vertical = 7.dp)) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SectionChip(text: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(if (isSystemInDarkTheme()) Color(0xFF3D2920) else Color(0xFFEDD8CC)).padding(horizontal = 14.dp, vertical = 8.dp)) {
        Text(text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = PremiumPalette.Primary)
    }
}

@Composable
fun EditorialKicker(text: String, centered: Boolean = false) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = if (centered) TextAlign.Center else TextAlign.Start, modifier = Modifier.fillMaxWidth())
}

@Composable
fun PrimaryGlowButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, leading: (@Composable () -> Unit)? = null) {
    Button(onClick = onClick, enabled = enabled, modifier = modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PremiumPalette.Primary, contentColor = Color.White, disabledContainerColor = Color(0xFFCFB3A5), disabledContentColor = Color.White.copy(alpha = 0.8f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leading?.invoke()
            if (leading != null) Spacer(modifier = Modifier.width(8.dp))
            Text(text)
        }
    }
}

@Composable
fun SecondaryPremiumButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, leading: (@Composable () -> Unit)? = null) {
    Button(onClick = onClick, modifier = modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC), contentColor = MaterialTheme.colorScheme.onSurface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leading?.invoke()
            if (leading != null) Spacer(modifier = Modifier.width(8.dp))
            Text(text)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumTopBar(title: String, onBack: (() -> Unit)?) {
    CenterAlignedTopAppBar(
        title = { Text(title, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = { if (onBack != null) IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour", tint = MaterialTheme.colorScheme.onBackground) } },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun ChargementMinimal() {
    AppBackground { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PremiumPalette.Primary) } }
}

@Composable
fun ChargementAnalyseScreen(modifier: Modifier = Modifier, onTermine: () -> Unit) {
    val messages = listOf("Analyse en cours...", "Lecture du profil de votre chien...", "Préparation de votre bilan...")
    var messageIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { repeat(messages.size) { messageIndex = it; delay(700) }; delay(400); onTermine() }
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dot1Alpha by infiniteTransition.animateFloat(0.3f, 1f, infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "d1")
    val dot2Alpha by infiniteTransition.animateFloat(0.3f, 1f, infiniteRepeatable(tween(600, delayMillis = 200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "d2")
    val dot3Alpha by infiniteTransition.animateFloat(0.3f, 1f, infiniteRepeatable(tween(600, delayMillis = 400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "d3")
    AppBackground {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(28.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.size(14.dp).alpha(dot1Alpha).background(PremiumPalette.Primary, CircleShape))
                    Box(modifier = Modifier.size(14.dp).alpha(dot2Alpha).background(PremiumPalette.PrimarySoft, CircleShape))
                    Box(modifier = Modifier.size(14.dp).alpha(dot3Alpha).background(PremiumPalette.Accent, CircleShape))
                }
                Text(messages[messageIndex], style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun AccueilScreen(modifier: Modifier = Modifier, hasSavedProgress: Boolean, onCommencer: () -> Unit, onReprendre: () -> Unit, onDictionnaire: () -> Unit, onAlimentation: () -> Unit) {
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).padding(horizontal = 20.dp, vertical = 8.dp)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically), horizontalAlignment = Alignment.CenterHorizontally) {
            AccueilIllustrationCard()
            PrimaryGlowButton(text = "Démarrer le bilan", onClick = onCommencer, leading = { Icon(Icons.Rounded.Pets, contentDescription = null, tint = Color.White) })
            SecondaryPremiumButton(text = "Dictionnaire", onClick = onDictionnaire, leading = { Icon(Icons.Rounded.MenuBook, contentDescription = null) })
            SecondaryPremiumButton(text = "Alimentation", onClick = onAlimentation, leading = { Icon(Icons.Rounded.Star, contentDescription = null) })
            if (hasSavedProgress) SecondaryPremiumButton(text = "Reprendre le questionnaire", onClick = onReprendre, leading = { Icon(Icons.Rounded.AutoStories, contentDescription = null) })
        }
    }
}

@Composable
fun IntroductionScreen(modifier: Modifier = Modifier, onCommencer: () -> Unit) {
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).padding(horizontal = 20.dp, vertical = 8.dp)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(8.dp))
            PremiumCard(centered = true) {
                EditorialKicker("Avant de commencer", centered = true); Spacer(modifier = Modifier.height(12.dp))
                Text("Ce questionnaire vous prendra environ 5 minutes.", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            }
            PremiumCard {
                EditorialKicker("Ce que vous allez explorer"); Spacer(modifier = Modifier.height(14.dp))
                Bullet("Sa sensibilité émotionnelle"); Spacer(modifier = Modifier.height(8.dp))
                Bullet("Son besoin d'attachement"); Spacer(modifier = Modifier.height(8.dp))
                Bullet("Sa gestion de l'excitation"); Spacer(modifier = Modifier.height(8.dp))
                Bullet("Sa réactivité à l'environnement")
            }
            Spacer(modifier = Modifier.height(4.dp))
            PrimaryGlowButton(text = "Commencer", onClick = onCommencer, leading = { Icon(Icons.Rounded.Pets, contentDescription = null, tint = Color.White) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun QuestionnaireScreen(
    modifier: Modifier = Modifier, question: Question, progress: Float,
    numero: Int, total: Int, nomChien: String = "", valeurTexte: String,
    choixSelectionne: Int?, onValeurChangee: (String) -> Unit,
    onChoixSelectionne: (Int) -> Unit, onSuivant: () -> Unit
) {
    val titreSection = QuestionnaireEngine.titreSectionPourQuestion(question.id)
    val boutonActif = when (question) { is QuestionTexte -> valeurTexte.isNotBlank(); is QuestionChoix -> choixSelectionne != null }

    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).padding(horizontal = 20.dp, vertical = 10.dp)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            PremiumCard {
                SectionChip(titreSection)
                Spacer(modifier = Modifier.height(10.dp))
                Text("$numero sur $total", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(12.dp))
                val animatedProgress by animateFloatAsState(progress.coerceIn(0f, 1f), label = "progress")
                Box(modifier = Modifier.fillMaxWidth().height(10.dp).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5), RoundedCornerShape(999.dp))) {
                    Box(modifier = Modifier.fillMaxWidth(animatedProgress).height(10.dp).background(Brush.horizontalGradient(listOf(PremiumPalette.Primary, PremiumPalette.PrimarySoft)), RoundedCornerShape(999.dp)))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                when (question) {
                    is QuestionTexte -> {
                        PremiumCard {
                            Text(question.titre, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(18.dp))
                            OutlinedTextField(
                                value = valeurTexte, onValueChange = onValeurChangee,
                                label = { Text("Votre réponse") }, placeholder = { Text("Ex. Rocky") },
                                singleLine = true, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PremiumPalette.Primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline, focusedTextColor = MaterialTheme.colorScheme.onSurface, unfocusedTextColor = MaterialTheme.colorScheme.onSurface, cursorColor = PremiumPalette.Primary, focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
                            )
                        }
                    }
                    is QuestionChoix -> {
                        PremiumCard {
                            Text(question.titre, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                            val aide = QuestionnaireEngine.aideQuestion(question.id)
                            if (aide != null) { Spacer(modifier = Modifier.height(8.dp)); Text(aide, style = MaterialTheme.typography.bodySmall, color = PremiumPalette.PrimarySoft, fontWeight = FontWeight.Medium) }
                            Spacer(modifier = Modifier.height(18.dp))
                            question.options.forEachIndexed { index, option ->
                                ChoiceRow(text = option, selected = choixSelectionne == index, onClick = { onChoixSelectionne(index) })
                                if (index != question.options.lastIndex) Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                PrimaryGlowButton(text = "Continuer", onClick = onSuivant, enabled = boutonActif)
                if (!boutonActif) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(when (question) { is QuestionTexte -> "Saisissez une réponse pour continuer"; is QuestionChoix -> "Choisissez une réponse pour continuer" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun ChoiceRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .background(if (selected) PremiumPalette.Accent.copy(alpha = 0.20f) else if (isSystemInDarkTheme()) Color(0xFF231B17) else Color(0xFFF8F4EE), RoundedCornerShape(20.dp))
            .border(1.dp, if (selected) PremiumPalette.Primary else MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(22.dp).background(if (selected) PremiumPalette.Primary else Color.Transparent, CircleShape).border(2.dp, if (selected) PremiumPalette.Primary else MaterialTheme.colorScheme.outline, CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun ResultatScreen(
    modifier: Modifier = Modifier, nomChien: String, analyse: ResultatAnalyse,
    onShare: () -> Unit, onCopy: () -> Unit, onExportPdf: () -> Unit,
    onRecommencer: () -> Unit, onOpenFiche: (String) -> Unit = {}, onOpenAlimentation: () -> Unit = {}
) {
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp), maxWidth = 780) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            PremiumCard(centered = true) {
                EditorialKicker("Votre bilan", centered = true); Spacer(modifier = Modifier.height(10.dp))
                Text("Bilan pour ${nomChienAffiche(nomChien)}", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(8.dp))
                AccentChip(analyse.profil.profilType)
            }
            if (!analyse.raceCategorie.isNullOrBlank() || !analyse.racePrecise.isNullOrBlank()) RaceCard(raceCategorie = analyse.raceCategorie, racePrecise = analyse.racePrecise)
            PremiumCard(centered = true) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EditorialKicker("Lecture principale", centered = true); Spacer(modifier = Modifier.height(8.dp))
                    Text(analyse.hypothesePrincipale, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(couleurFondPriorite(analyse.prioriteAction)).padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text("Priorité : ${textePrioriteAction(analyse.prioriteAction)}", color = couleurPriorite(analyse.prioriteAction), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            PremiumCard(centered = true) {
                EditorialKicker("Ce que ressent probablement ${nomChienAffiche(nomChien)}", centered = true); Spacer(modifier = Modifier.height(10.dp))
                Text(analyse.profil.phraseHumaine, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
                Text(resumeEmotionnel(analyse.problemePrincipal), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, color = PremiumPalette.Primary); Spacer(modifier = Modifier.height(8.dp))
                Text(intentionChien(analyse.problemePrincipal), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface); Spacer(modifier = Modifier.height(6.dp))
                Text(besoinPrincipal(analyse.problemePrincipal), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            PremiumCard(centered = true) { EditorialKicker("En un coup d'œil", centered = true); Spacer(modifier = Modifier.height(14.dp)); QuatreAxesGrid(analyse = analyse) }
            FacteursCard(analyse = analyse)
            PremiumCard(centered = true) {
                EditorialKicker("Niveau de situation", centered = true); Spacer(modifier = Modifier.height(10.dp))
                AccentChip(texteNiveauSituation(analyse.niveauSituation)); Spacer(modifier = Modifier.height(14.dp))
                Text(analyse.messageSituation, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(10.dp))
                Text(analyse.raisonSituation, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            PremiumCard(centered = true) { EditorialKicker("Faut-il s'inquiéter ?", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(texteVigilance(analyse.vigilance, nomChien), textAlign = TextAlign.Center) }
            PremiumCard(centered = true) { EditorialKicker("Ce qui se passe probablement", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(analyse.explicationPrincipale, textAlign = TextAlign.Center) }
            HighlightAdviceCard(title = "Première piste concrète", advice = analyse.conseilPrincipal)
            PremiumCard(centered = true) {
                EditorialKicker("Les 3 prochains jours", centered = true); Spacer(modifier = Modifier.height(14.dp))
                SubsectionTitle("À faire"); Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aFaire.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) }
                Spacer(modifier = Modifier.height(8.dp)); SubsectionTitle("À éviter"); Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aEviter.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) }
                Spacer(modifier = Modifier.height(8.dp)); SubsectionTitle("À observer"); Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aObserver.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) }
            }
            if (analyse.conseilsPratiques.isNotEmpty()) {
                PremiumCard(centered = true) { EditorialKicker("Conseils complémentaires", centered = true); Spacer(modifier = Modifier.height(12.dp)); analyse.conseilsPratiques.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) } }
            }
            if (analyse.messageAide != null || analyse.aDejaMordu) {
                PremiumCard(centered = true) {
                    EditorialKicker("Quand demander de l'aide", centered = true); Spacer(modifier = Modifier.height(10.dp))
                    if (analyse.aDejaMordu) Text("Une morsure a été signalée — un accompagnement professionnel est recommandé.", textAlign = TextAlign.Center, color = PremiumPalette.PrioriteModere, fontWeight = FontWeight.SemiBold)
                    else analyse.messageAide?.let { Text(it, textAlign = TextAlign.Center, color = PremiumPalette.PrioriteUrgente, fontWeight = FontWeight.SemiBold) }
                }
            }
            PremiumCard(centered = true) { EditorialKicker("Important", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("Ce bilan reste indicatif. Il ne remplace ni un vétérinaire ni un professionnel du comportement.", textAlign = TextAlign.Center) }
            FichesRecommandeesCard(analyse = analyse, nomChien = nomChienAffiche(nomChien), onOpenFiche = onOpenFiche, onOpenAlimentation = onOpenAlimentation)
            PremiumCard(centered = true) { EditorialKicker("À retenir", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(phraseFin(nomChien), textAlign = TextAlign.Center) }
            ActionButtonsGrid(onShare = onShare, onCopy = onCopy, onExportPdf = onExportPdf, onRecommencer = onRecommencer)
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.HorizontalDivider(color = PremiumPalette.Border, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 40.dp))
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onRecommencer, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Recommencer depuis le début", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AlerteMorsureCard(nomChien: String) {
    val isDark = isSystemInDarkTheme()
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF3D1209) else Color(0xFFFFF0EC)), border = BorderStroke(2.dp, PremiumPalette.MorsureBorder)) {
        Column(modifier = Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Rounded.Warning, contentDescription = null, tint = PremiumPalette.PrioriteUrgente, modifier = Modifier.size(22.dp))
                Text("ATTENTION — MORSURE SIGNALÉE", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold, color = PremiumPalette.PrioriteUrgente, textAlign = TextAlign.Center)
                Icon(Icons.Rounded.Warning, contentDescription = null, tint = PremiumPalette.PrioriteUrgente, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text("Il y a déjà eu morsure chez $nomChien. Cette situation ne doit pas être banalisée.", style = MaterialTheme.typography.titleMedium, color = PremiumPalette.PrioriteUrgente, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Un accompagnement par un professionnel du comportement est fortement recommandé.", color = if (isDark) Color(0xFFFFCFC5) else Color(0xFF5C1A0A), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun QuatreAxesGrid(analyse: ResultatAnalyse) {
    val axes = listOf(Triple("Sensibilité", analyse.niveauPeur, analyse.peur), Triple("Attachement", analyse.niveauAttachement, analyse.attachement), Triple("Impulsivité", analyse.niveauImpulsivite, analyse.impulsivite), Triple("Réactivité", analyse.niveauReactivite, analyse.reactivite))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        axes.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { (label, niveau, score) ->
                    val couleurNiveau = when (niveau) { NiveauAxe.PEU_MARQUE -> PremiumPalette.PrioriteFaible; NiveauAxe.A_SURVEILLER -> PremiumPalette.PrioriteModere; NiveauAxe.MARQUE -> PremiumPalette.PrioriteElevee; NiveauAxe.TRES_MARQUE -> PremiumPalette.PrioriteUrgente }
                    val fondNiveau = when (niveau) { NiveauAxe.PEU_MARQUE -> PremiumPalette.PrioriteFaibleBg; NiveauAxe.A_SURVEILLER -> PremiumPalette.PrioriteModereBg; NiveauAxe.MARQUE -> PremiumPalette.PrioriteEleveeBg; NiveauAxe.TRES_MARQUE -> PremiumPalette.PrioriteUrgenteBg }
                    val animated by animateFloatAsState((score / 100f).coerceIn(0f, 1f), label = "axe_$label")
                    Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(if (isSystemInDarkTheme()) Color(0xFF2A1F1A) else fondNiveau).border(1.dp, couleurNiveau.copy(alpha = 0.3f), RoundedCornerShape(16.dp)).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        Text(QuestionnaireEngine.libelleNiveauAxe(niveau), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = couleurNiveau, textAlign = TextAlign.Center)
                        Box(modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(999.dp)).background(couleurNiveau.copy(alpha = 0.2f))) {
                            Box(modifier = Modifier.fillMaxWidth(animated).height(5.dp).clip(RoundedCornerShape(999.dp)).background(couleurNiveau))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FichesRecommandeesCard(analyse: ResultatAnalyse, nomChien: String, onOpenFiche: (String) -> Unit, onOpenAlimentation: () -> Unit) {
    val fichesBehavior = recommanderFichesComportement(analyse)
    val fichesAlim = recommanderFichesAlimentation(analyse)
    if (fichesBehavior.isEmpty() && fichesAlim.isEmpty()) return
    PremiumCard(centered = false) {
        EditorialKicker("Pour aller plus loin avec $nomChien"); Spacer(modifier = Modifier.height(14.dp))
        if (fichesBehavior.isNotEmpty()) {
            Text("Fiches comportementales", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = PremiumPalette.PrimarySoft); Spacer(modifier = Modifier.height(8.dp))
            fichesBehavior.forEach { (ficheId, titre) ->
                Row(modifier = Modifier.fillMaxWidth().clickable { onOpenFiche(ficheId) }.clip(RoundedCornerShape(12.dp)).background(if (isSystemInDarkTheme()) Color(0xFF2A1F1A) else Color(0xFFF4EDE6)).padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(titre, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                    Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = PremiumPalette.PrimarySoft, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        if (fichesAlim.isNotEmpty()) {
            if (fichesBehavior.isNotEmpty()) Spacer(modifier = Modifier.height(8.dp))
            Text("Repères alimentation", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = PremiumPalette.PrimarySoft); Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth().clickable { onOpenAlimentation() }.clip(RoundedCornerShape(12.dp)).background(if (isSystemInDarkTheme()) Color(0xFF2A1F1A) else Color(0xFFF4EDE6)).padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) { fichesAlim.forEach { titre -> Text(titre, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface); if (titre != fichesAlim.last()) Spacer(modifier = Modifier.height(4.dp)) } }
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = PremiumPalette.PrimarySoft, modifier = Modifier.size(18.dp))
            }
        }
    }
}

fun recommanderFichesComportement(analyse: ResultatAnalyse): List<Pair<String, String>> {
    val maxAxe = maxOf(analyse.peur, analyse.attachement, analyse.impulsivite, analyse.reactivite)
    if (maxAxe < 30) return emptyList()
    return when (analyse.problemePrincipal) {
        Axe.PEUR -> listOf("corps-fige" to "Corps figé", "oreilles-arriere" to "Oreilles plaquées en arrière", "queue-basse" to "Queue basse ou rentrée")
        Axe.ATTACHEMENT -> listOf("suit-humain" to "Suit son humain partout", "destruction-absence" to "Destructions en absence", "aboiement-frustration" to "Aboiement de frustration")
        Axe.IMPULSIVITE -> listOf("haletement" to "Halètement sans effort", "appel-au-jeu" to "Posture d'invitation au jeu", "secouement" to "Secouement du corps")
        Axe.REACTIVITE -> listOf("grognement" to "Grognement", "corps-fige" to "Corps figé", "aboiement-alerte" to "Aboiement d'alerte")
    }
}

fun recommanderFichesAlimentation(analyse: ResultatAnalyse): List<String> {
    return if (analyse.contexte.physique >= 2) listOf("Que faire si mon chien a mangé quelque chose de douteux ?", "Quels signes doivent alerter après ingestion ?")
    else listOf("Peut-on donner des fruits ou légumes ?", "Friandises : utile ou risqué ?")
}

@Composable
fun ActionButtonsGrid(onShare: () -> Unit, onCopy: () -> Unit, onExportPdf: () -> Unit, onRecommencer: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionButton(text = "Partager", icon = Icons.Rounded.Share, primary = true, modifier = Modifier.weight(1f), onClick = onShare)
            ActionButton(text = "Export PDF", icon = Icons.Rounded.PictureAsPdf, primary = true, modifier = Modifier.weight(1f), onClick = onExportPdf)
        }
        ActionButton(text = "Copier le résumé", icon = Icons.Rounded.ContentCopy, primary = false, modifier = Modifier.fillMaxWidth(), onClick = onCopy)
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, primary: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier.height(52.dp), shape = RoundedCornerShape(18.dp),
        colors = if (primary) ButtonDefaults.buttonColors(containerColor = PremiumPalette.Primary, contentColor = Color.White)
        else ButtonDefaults.buttonColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC), contentColor = MaterialTheme.colorScheme.onSurface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

fun couleurPriorite(p: PrioriteAction) = when (p) { PrioriteAction.FAIBLE -> PremiumPalette.PrioriteFaible; PrioriteAction.MODEREE -> PremiumPalette.PrioriteModere; PrioriteAction.ELEVEE -> PremiumPalette.PrioriteElevee; PrioriteAction.URGENTE -> PremiumPalette.PrioriteUrgente }
fun couleurFondPriorite(p: PrioriteAction) = when (p) { PrioriteAction.FAIBLE -> PremiumPalette.PrioriteFaibleBg; PrioriteAction.MODEREE -> PremiumPalette.PrioriteModereBg; PrioriteAction.ELEVEE -> PremiumPalette.PrioriteEleveeBg; PrioriteAction.URGENTE -> PremiumPalette.PrioriteUrgenteBg }

@Composable
fun RaceCard(raceCategorie: String?, racePrecise: String?) {
    val categorieId = when (raceCategorie) { "Chiens de berger & troupeau" -> "bergers"; "Retrievers & Spaniels" -> "retrievers"; "Terriers" -> "terriers"; "Molosses & Dogues" -> "molosses"; "Chiens nordiques & primitifs" -> "nordiques"; "Lévriers & Races de course" -> "levriers"; "Races naines & compagnie" -> "nains"; "Chiens de chasse & pisteurs" -> "chasse"; else -> "croise" }
    val categorie = categoriesRaces.firstOrNull { it.id == categorieId }
    val nuance = if (!racePrecise.isNullOrBlank()) getNuanceAnalyse(racePrecise) ?: categorie?.nuanceAnalyse else categorie?.nuanceAnalyse
    val predispositions = if (!racePrecise.isNullOrBlank()) getPredispositions(racePrecise).ifEmpty { categorie?.predispositions ?: emptyList() } else categorie?.predispositions ?: emptyList()
    val nomAffiche = when { !racePrecise.isNullOrBlank() -> racePrecise; !raceCategorie.isNullOrBlank() -> raceCategorie; else -> return }
    PremiumCard(centered = true) {
        EditorialKicker("Profil de race", centered = true); Spacer(modifier = Modifier.height(10.dp))
        Text(nomAffiche, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PremiumPalette.Primary, textAlign = TextAlign.Center)
        if (!raceCategorie.isNullOrBlank() && !racePrecise.isNullOrBlank()) { Spacer(modifier = Modifier.height(4.dp)); Text(raceCategorie, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center) }
        if (predispositions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp)); Text("Prédispositions fréquentes dans cette famille", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()); Spacer(modifier = Modifier.height(8.dp))
            predispositions.forEach { pred -> Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) { Box(modifier = Modifier.padding(top = 7.dp).size(6.dp).background(PremiumPalette.Accent, CircleShape)); Spacer(modifier = Modifier.width(8.dp)); Text(pred, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = 500.dp)) } }
        }
        if (!nuance.isNullOrBlank()) { Spacer(modifier = Modifier.height(14.dp)); Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (isSystemInDarkTheme()) Color(0xFF2A1F1A) else Color(0xFFF4EDE6)).padding(14.dp)) { Text(nuance, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center) } }
    }
}

@Composable
fun HighlightAdviceCard(title: String, advice: String) {
    val backgroundBrush = if (isSystemInDarkTheme()) Brush.verticalGradient(listOf(Color(0xFF3A2A23), Color(0xFF2A1F1A))) else Brush.verticalGradient(listOf(Color(0xFFF3E4DA), Color(0xFFECD8CB)))
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(30.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent), border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF6A4D41) else Color(0xFFD8B9A7))) {
        Column(modifier = Modifier.fillMaxWidth().background(backgroundBrush).padding(horizontal = 24.dp, vertical = 26.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AccentChip("Le point d'appui principal"); Spacer(modifier = Modifier.height(14.dp))
            Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
            Text(advice, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
            Text("C'est souvent ici que le changement commence à prendre forme.", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SubsectionTitle(text: String) { Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center) }

@Composable
fun ScoreLine(label: String, value: Int, centered: Boolean = false) {
    val animated by animateFloatAsState((value / 100f).coerceIn(0f, 1f), label = "score")
    Column(horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start) {
        if (centered) { Text(label, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(8.dp)); AccentChip(QuestionnaireEngine.libelleNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(value))) }
        else { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(label, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface); Spacer(modifier = Modifier.width(8.dp)); AccentChip(QuestionnaireEngine.libelleNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(value))) } }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth().height(10.dp).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5), RoundedCornerShape(999.dp))) {
            Box(modifier = Modifier.fillMaxWidth(animated).height(10.dp).background(Brush.horizontalGradient(listOf(PremiumPalette.Primary, PremiumPalette.PrimarySoft)), RoundedCornerShape(999.dp)))
        }
    }
}

@Composable
fun Bullet(text: String, centered: Boolean = false) {
    if (centered) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) { Box(modifier = Modifier.padding(top = 8.dp).size(7.dp).background(PremiumPalette.PrimarySoft, CircleShape)); Spacer(modifier = Modifier.width(10.dp)); Text(text, modifier = Modifier.widthIn(max = 560.dp), color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center) }
    } else {
        Row(verticalAlignment = Alignment.Top) { Box(modifier = Modifier.padding(top = 8.dp).size(7.dp).background(PremiumPalette.PrimarySoft, CircleShape)); Spacer(modifier = Modifier.width(10.dp)); Text(text, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface) }
    }
}

@Composable
fun FacteursCard(analyse: ResultatAnalyse) {
    if (analyse.facteursAggravants.isEmpty() && analyse.facteursProtecteurs.isEmpty()) return
    PremiumCard(centered = true) {
        EditorialKicker("Facteurs repérés", centered = true)
        if (analyse.facteursAggravants.isNotEmpty()) { Spacer(modifier = Modifier.height(12.dp)); SubsectionTitle("Ce qui peut aggraver"); Spacer(modifier = Modifier.height(8.dp)); analyse.facteursAggravants.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) } }
        if (analyse.facteursProtecteurs.isNotEmpty()) { Spacer(modifier = Modifier.height(12.dp)); SubsectionTitle("Ce qui protège"); Spacer(modifier = Modifier.height(8.dp)); analyse.facteursProtecteurs.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) } }
    }
}

@Composable
fun DictionnaireInfoScreen(modifier: Modifier = Modifier, onOpenFiche: (String) -> Unit) {
    val fiches = remember { comportementEntries() }
    var recherche by remember { mutableStateOf("") }
    val fichesFiltrees = remember(recherche) { if (recherche.isBlank()) fiches else fiches.filter { it.titre.contains(recherche, ignoreCase = true) || it.resume.contains(recherche, ignoreCase = true) } }
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp), maxWidth = 780) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            PremiumCard(centered = true) {
                EditorialKicker("Dictionnaire comportemental", centered = true); Spacer(modifier = Modifier.height(10.dp))
                Text("Repères pour mieux lire le langage du chien", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(value = recherche, onValueChange = { recherche = it }, placeholder = { Text("Rechercher une fiche...") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PremiumPalette.Primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline, focusedTextColor = MaterialTheme.colorScheme.onSurface, unfocusedTextColor = MaterialTheme.colorScheme.onSurface, cursorColor = PremiumPalette.Primary, focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface))
            }
            if (fichesFiltrees.isEmpty()) PremiumCard(centered = true) { Text("Aucune fiche ne correspond à \"$recherche\".", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            else fichesFiltrees.forEach { fiche -> ComportementListItem(entry = fiche, onClick = { onOpenFiche(fiche.id) }) }
            PremiumCard(centered = true) { EditorialKicker("Important", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("Ces fiches donnent des repères de lecture. Elles ne remplacent pas l'avis d'un professionnel.", textAlign = TextAlign.Center) }
        }
    }
}

@Composable
fun DictionnaireDetailScreen(modifier: Modifier = Modifier, ficheId: String) {
    val fiche = remember(ficheId) { getComportementEntryById(ficheId) }
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp), maxWidth = 780) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (fiche == null) PremiumCard(centered = true) { Text("Fiche introuvable.", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center) }
            else {
                PremiumCard(centered = true) { EditorialKicker("Fiche comportementale", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(fiche.titre, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(10.dp)); Text(fiche.resume, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                PremiumCard { EditorialKicker("Explication"); Spacer(modifier = Modifier.height(12.dp)); Text(fiche.explication, style = MaterialTheme.typography.bodyLarge) }
                PremiumCard { EditorialKicker("Que faire"); Spacer(modifier = Modifier.height(12.dp)); Bullet(fiche.queFaire) }
                PremiumCard { EditorialKicker("À éviter"); Spacer(modifier = Modifier.height(12.dp)); Bullet(fiche.aEviter) }
                PremiumCard(centered = true) { EditorialKicker("Rappel", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("Un comportement isolé ne suffit pas toujours à conclure. Le contexte et l'ensemble du langage corporel comptent autant.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        }
    }
}

@Composable
fun ComportementListItem(entry: ComportementEntry, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft), border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) { Text(entry.titre, style = MaterialTheme.typography.titleMedium); Spacer(modifier = Modifier.height(8.dp)); Text(entry.resume, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Spacer(modifier = Modifier.width(12.dp)); Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun DictionnaireScreen(modifier: Modifier = Modifier) {
    val entries = remember { dictionnaireEntries() }
    val selectedCategoryState = remember { mutableStateOf<DictionnaireCategorie?>(null) }
    val selectedEntryState = remember { mutableStateOf<DictionnaireEntry?>(null) }
    val categories = remember { listOf(DictionnaireCategorie.DANGEREUX, DictionnaireCategorie.AUTORISES, DictionnaireCategorie.INGESTION, DictionnaireCategorie.DIGESTION) }
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp), maxWidth = 780) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            when {
                selectedEntryState.value != null -> {
                    val entry = selectedEntryState.value!!
                    PremiumCard { AccentChip(entry.categorie.titre); Spacer(modifier = Modifier.height(14.dp)); Text(entry.titre, style = MaterialTheme.typography.headlineSmall); Spacer(modifier = Modifier.height(12.dp)); Text(entry.contenu, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(modifier = Modifier.height(18.dp)); SecondaryPremiumButton("Retour à la catégorie", onClick = { selectedEntryState.value = null }, leading = { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null) }) }
                    PremiumCard(centered = true) { EditorialKicker("Rappel", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("En cas de symptômes ou d'ingestion douteuse, privilégiez un avis vétérinaire.", textAlign = TextAlign.Center) }
                }
                selectedCategoryState.value != null -> {
                    val categorie = selectedCategoryState.value!!
                    val items = entries.filter { it.categorie == categorie }
                    PremiumCard(centered = true) { EditorialKicker("Alimentation", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(categorie.titre, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center) }
                    items.forEach { entry -> DictionnaireListItem(entry = entry, onClick = { selectedEntryState.value = entry }) }
                    SecondaryPremiumButton("Retour aux rubriques", onClick = { selectedCategoryState.value = null }, leading = { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null) })
                }
                else -> {
                    PremiumCard(centered = true) { EditorialKicker("Alimentation du chien", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("Repères pratiques pour nourrir votre chien sereinement.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    PremiumCard { EditorialKicker("À retenir d'abord"); Spacer(modifier = Modifier.height(12.dp)); Bullet("Tout changement alimentaire doit être progressif."); Spacer(modifier = Modifier.height(8.dp)); Bullet("Même un aliment banal pour l'humain peut être inadapté pour le chien."); Spacer(modifier = Modifier.height(8.dp)); Bullet("En cas d'ingestion suspecte ou de symptômes, la prudence passe avant l'attente.") }
                    categories.forEach { categorie -> DictionnaireCategoryButton(categorie = categorie, onClick = { selectedCategoryState.value = categorie }) }
                    PremiumCard(centered = true) { EditorialKicker("Important", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("Ce guide donne des repères généraux. Il ne remplace pas un vétérinaire.", textAlign = TextAlign.Center) }
                }
            }
        }
    }
}

@Composable
fun DictionnaireCategoryButton(categorie: DictionnaireCategorie, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft), border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(categorie.titre, style = MaterialTheme.typography.titleLarge); Spacer(modifier = Modifier.height(6.dp))
                Text(when (categorie) { DictionnaireCategorie.DANGEREUX -> "Les aliments à éviter pour ne pas faire d'erreur."; DictionnaireCategorie.AUTORISES -> "Les repères de base pour donner sans improviser."; DictionnaireCategorie.INGESTION -> "Les bons réflexes si le chien a avalé quelque chose."; DictionnaireCategorie.DIGESTION -> "Herbe, vomissements, selles et petits signaux digestifs." }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(12.dp)); Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun DictionnaireListItem(entry: DictionnaireEntry, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft), border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) { Text(entry.titre, style = MaterialTheme.typography.titleMedium); Spacer(modifier = Modifier.height(8.dp)); Text(entry.resume, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Spacer(modifier = Modifier.width(12.dp)); Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ParametresScreen(modifier: Modifier = Modifier, onRevoirOnboarding: () -> Unit) {
    val context = LocalContext.current
    val version = remember { try { context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0" } catch (e: Exception) { "1.0" } }
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp)) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PremiumCard(centered = true) { EditorialKicker("Paramètres", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("Comprendre mon chien", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(6.dp)); AccentChip("Version $version") }
            PremiumCard { EditorialKicker("Tutoriel"); Spacer(modifier = Modifier.height(12.dp)); Text("Revoir la présentation de l'application depuis le début.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(modifier = Modifier.height(14.dp)); SecondaryPremiumButton("Revoir l'introduction", onClick = onRevoirOnboarding, leading = { Icon(Icons.Rounded.AutoStories, contentDescription = null) }) }
            PremiumCard {
                EditorialKicker("Confidentialité"); Spacer(modifier = Modifier.height(12.dp))
                Text("Cette application ne collecte aucune donnée personnelle. Les bilans sont stockés uniquement sur votre appareil. Les notifications sont locales.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(modifier = Modifier.height(14.dp))
                SecondaryPremiumButton("Politique de confidentialité", onClick = { val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://laurenaharoy-ctrl.github.io/comprendremonchien2/confidentialite.html")); context.startActivity(intent) }, leading = { Icon(Icons.Rounded.MenuBook, contentDescription = null) })
            }
            PremiumCard(centered = true) { EditorialKicker("À propos", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text("Développée avec soin pour aider les maîtres à mieux comprendre leur chien.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center) }
        }
    }
}