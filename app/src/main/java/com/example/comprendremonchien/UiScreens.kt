package com.example.comprendremonchien2

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Pets
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF8E4A2D),
    onPrimary = Color.White,
    background = Color(0xFFF4EFE8),
    surface = Color(0xFFF8F4EE),
    onSurface = Color(0xFF33231D),
    onBackground = Color(0xFF33231D),
    surfaceVariant = Color(0xFFF0E5DC),
    onSurfaceVariant = Color(0xFF75584C),
    outline = Color(0xFFE0D2C6),
    secondary = Color(0xFFB86A4A),
    tertiary = Color(0xFFD9A58F),
    error = Color(0xFFB94A48)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD39A7F),
    onPrimary = Color(0xFF3C1F14),
    background = Color(0xFF191411),
    surface = Color(0xFF231B17),
    onSurface = Color(0xFFF6EEE8),
    onBackground = Color(0xFFF6EEE8),
    surfaceVariant = Color(0xFF342923),
    onSurfaceVariant = Color(0xFFD2B9AB),
    outline = Color(0xFF56433B),
    secondary = Color(0xFFB86A4A),
    tertiary = Color(0xFFD9A58F),
    error = Color(0xFFD97C78)
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
    val Warning = Color(0xFFB94A48)
}

enum class DictionnaireCategorie(val titre: String) {
    DANGEREUX("Aliments dangereux"),
    AUTORISES("Aliments autorisés"),
    INGESTION("Que faire en cas d’ingestion"),
    DIGESTION("Digestion / herbe / vomissements / selles")
}

data class DictionnaireEntry(
    val categorie: DictionnaireCategorie,
    val titre: String,
    val resume: String,
    val contenu: String
)

data class ComportementEntry(
    val id: String,
    val titre: String,
    val resume: String,
    val explication: String,
    val queFaire: String,
    val aEviter: String
)

fun dictionnaireEntries(): List<DictionnaireEntry> {
    return listOf(
        DictionnaireEntry(
            categorie = DictionnaireCategorie.DANGEREUX,
            titre = "Quels aliments éviter absolument ?",
            resume = "Certains aliments humains peuvent être franchement problématiques pour le chien.",
            contenu = "Par prudence, il faut éviter les aliments connus pour poser problème comme le chocolat, l’oignon, l’ail, certains raisins, l’alcool, les os cuits ainsi que les aliments très gras, très salés ou fortement assaisonnés. En cas d’ingestion suspecte, surtout si le chien semble mal, il ne faut pas attendre si des symptômes inhabituels apparaissent."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.DANGEREUX,
            titre = "Les restes de table sont-ils risqués ?",
            resume = "Le petit bonus de table peut vite devenir un faux ami.",
            contenu = "Mieux vaut éviter les restes de table trop riches, trop salés, trop gras ou très transformés. Même si le chien les apprécie, ils peuvent déséquilibrer sa ration, favoriser des troubles digestifs, encourager la mendicité et rendre les repas moins stables au quotidien."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.DANGEREUX,
            titre = "Pourquoi faut-il éviter les os cuits ?",
            resume = "Un os cuit peut devenir une petite catastrophe silencieuse.",
            contenu = "Les os cuits peuvent se casser en fragments plus coupants et poser des problèmes digestifs ou mécaniques. Pour rester prudent, il vaut mieux les éviter. En cas d’ingestion suivie de douleur, vomissements, gêne ou comportement inhabituel, il faut demander un avis vétérinaire."
        ),

        DictionnaireEntry(
            categorie = DictionnaireCategorie.AUTORISES,
            titre = "Peut-on donner des fruits ou légumes ?",
            resume = "Parfois oui, mais pas en mode buffet libre.",
            contenu = "Certains fruits et légumes peuvent être proposés en petite quantité, selon leur nature et la tolérance du chien. L’idée générale est de rester prudent, d’éviter les aliments connus pour poser problème et de ne pas bouleverser la ration principale. En cas de doute sur un aliment précis, il vaut mieux vérifier avant de donner."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.AUTORISES,
            titre = "Friandises : utile ou risqué ?",
            resume = "Très utiles en éducation, moins drôles quand elles débordent du cadre.",
            contenu = "Les friandises peuvent être utiles si elles restent cohérentes avec la ration globale et si elles sont données en petite quantité. Le risque apparaît quand elles deviennent trop nombreuses, trop riches ou distribuées sans cadre, ce qui peut déséquilibrer l’alimentation et renforcer des comportements de demande."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.AUTORISES,
            titre = "Comment changer ses croquettes ?",
            resume = "Un changement trop rapide peut transformer l’estomac en tambour de machine à laver.",
            contenu = "Le changement d’aliment doit se faire progressivement sur plusieurs jours. On mélange d’abord une petite quantité du nouvel aliment avec l’ancien, puis on augmente peu à peu la part du nouveau. Cette transition aide à limiter les troubles digestifs et permet d’observer si le chien tolère bien la nouveauté."
        ),

        DictionnaireEntry(
            categorie = DictionnaireCategorie.INGESTION,
            titre = "Que faire si mon chien a mangé quelque chose de douteux ?",
            resume = "La meilleure réaction, c’est le calme et la rapidité, pas l’improvisation.",
            contenu = "Si votre chien a mangé un aliment suspect ou inhabituel, il faut d’abord éviter les remèdes maison improvisés. L’essentiel est de repérer ce qu’il a mangé, en quelle quantité approximative, quand cela s’est produit et comment il se comporte ensuite. En cas de doute, surtout s’il s’agit d’un aliment connu pour être problématique ou si des symptômes apparaissent, il faut contacter rapidement un vétérinaire."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.INGESTION,
            titre = "Quels signes doivent alerter après ingestion ?",
            resume = "Quand le corps tire l’alarme, il vaut mieux écouter tout de suite.",
            contenu = "Il faut être particulièrement attentif à des vomissements répétés, une diarrhée importante, un abattement, une agitation inhabituelle, des tremblements, une douleur, une gêne respiratoire ou tout changement brutal de comportement. En présence de ces signes, il est préférable de demander rapidement un avis vétérinaire."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.INGESTION,
            titre = "Faut-il attendre pour voir si ça passe ?",
            resume = "Parfois attendre rassure, parfois attendre complique.",
            contenu = "Quand l’aliment ingéré est potentiellement dangereux ou quand le chien présente déjà des symptômes, il vaut mieux ne pas temporiser. À l’inverse, pour un petit écart sans signe clinique, l’observation peut parfois suffire, mais la prudence reste la meilleure boussole. En cas d’hésitation, il est plus sûr de demander conseil."
        ),

        DictionnaireEntry(
            categorie = DictionnaireCategorie.DIGESTION,
            titre = "Pourquoi mon chien mange de l’herbe ?",
            resume = "Un comportement fréquent, souvent banal, mais à surveiller s’il devient répétitif.",
            contenu = "Manger de l’herbe peut avoir plusieurs explications. Certains chiens le font occasionnellement sans que cela soit inquiétant. Cela peut être lié à une habitude, à de l’exploration, à une gêne digestive légère ou simplement à un comportement opportuniste. Si cela devient très fréquent, s’accompagne de vomissements, d’un inconfort digestif, d’abattement ou d’un changement brutal de comportement, mieux vaut demander un avis vétérinaire."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.DIGESTION,
            titre = "Mon chien vomit, est-ce toujours grave ?",
            resume = "Un vomissement isolé n’a pas le même poids qu’une série en cascade.",
            contenu = "Un vomissement ponctuel peut parfois rester sans gravité apparente, mais des vomissements répétés, associés à de la douleur, de l’abattement, une diarrhée, un refus de boire ou un comportement inhabituel doivent pousser à demander un avis vétérinaire. Ce qui compte, ce n’est pas seulement le symptôme, mais aussi sa fréquence, son intensité et l’état général du chien."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.DIGESTION,
            titre = "Comment comprendre des selles anormales ?",
            resume = "Les selles racontent souvent une petite histoire digestive.",
            contenu = "Des selles plus molles, plus fréquentes, inhabituelles en couleur ou accompagnées d’inconfort peuvent refléter une difficulté digestive, un changement alimentaire trop rapide, un aliment mal toléré ou autre chose. Si cela dure, revient souvent ou s’accompagne d’autres symptômes, il faut faire le point avec un vétérinaire."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.DIGESTION,
            titre = "Mon chien mange trop vite",
            resume = "Avaler ses repas comme une fusée mérite parfois un petit ajustement.",
            contenu = "Certains chiens mangent très vite par excitation, habitude ou compétition passée. Cela peut favoriser l’inconfort digestif ou les régurgitations. Fractionner la ration, utiliser une gamelle adaptée ou ralentir la prise alimentaire peut aider. Si cela s’aggrave ou s’accompagne de troubles digestifs, un avis vétérinaire reste pertinent."
        ),
        DictionnaireEntry(
            categorie = DictionnaireCategorie.DIGESTION,
            titre = "Mon chien réclame tout le temps à manger",
            resume = "La demande peut venir de l’habitude, de l’ennui ou d’une ration mal adaptée.",
            contenu = "Un chien qui réclame souvent ne manque pas forcément de nourriture. La demande peut être liée à l’habitude, à l’ennui, à des friandises trop fréquentes, à une ration inadaptée ou à des horaires irréguliers. Si cette faim paraît excessive, récente ou accompagnée d’autres changements, il peut être utile de vérifier qu’il n’existe pas une cause médicale ou nutritionnelle."
        )
    )
}

fun comportementEntries(): List<ComportementEntry> {
    return listOf(
        ComportementEntry(
            id = "queue-remue",
            titre = "Queue qui remue",
            resume = "Pas toujours synonyme de joie, il faut lire tout le corps.",
            explication = "Un chien qui remue la queue n’est pas automatiquement heureux. La queue indique surtout un état d’activation émotionnelle. Une queue souple avec un corps détendu évoque souvent une émotion positive. Une queue rapide sur un corps raide peut au contraire signaler de la tension, de l’excitation ou de la vigilance.",
            queFaire = "Observer l’ensemble du corps avant d’interagir : oreilles, regard, posture, respiration et mobilité.",
            aEviter = "Penser que la queue qui bouge autorise forcément le contact."
        ),
        ComportementEntry(
            id = "queue-basse",
            titre = "Queue basse ou rentrée",
            resume = "Signal fréquent d’inquiétude ou de malaise.",
            explication = "Quand la queue descend très bas ou se replie sous le ventre, le chien peut être impressionné, stressé ou en retrait. Cela apparaît souvent face à un inconnu, un bruit fort, une situation nouvelle ou une interaction trop insistante.",
            queFaire = "Laisser de l’espace, adoucir l’approche et réduire la pression.",
            aEviter = "Forcer le chien à avancer, saluer ou rester dans une situation qui le gêne."
        ),
        ComportementEntry(
            id = "baillement",
            titre = "Bâillement hors fatigue",
            resume = "Souvent un signal d’apaisement ou de tension légère.",
            explication = "Le chien peut bâiller quand il n’est pas fatigué. C’est parfois une manière de réguler son émotion dans une situation confuse, trop intense ou un peu inconfortable.",
            queFaire = "Ralentir, faire une pause, simplifier l’exercice ou l’interaction.",
            aEviter = "Réduire ce signal à de la simple fatigue."
        ),
        ComportementEntry(
            id = "leche-truffe",
            titre = "Lèchement rapide de truffe",
            resume = "Petit signal discret de tension ou d’apaisement.",
            explication = "Ce coup de langue rapide apparaît souvent quand le chien essaie de se calmer ou d’apaiser l’échange. Il est fréquent lors d’approches frontales, de gestes brusques ou de caresses non demandées.",
            queFaire = "Se mettre légèrement de côté et laisser davantage d’initiative au chien.",
            aEviter = "Continuer à insister physiquement ou verbalement."
        ),
        ComportementEntry(
            id = "tourne-tete",
            titre = "Tourner la tête",
            resume = "Le chien cherche souvent à éviter la pression.",
            explication = "Tourner la tête est une façon polie de rendre l’échange moins direct. C’est un signal fréquent face à un regard fixe, une main tendue trop vite ou une proximité subie.",
            queFaire = "Adoucir sa posture et diminuer la pression sociale.",
            aEviter = "Interpréter cela comme de l’ignorance ou un refus d’obéir."
        ),
        ComportementEntry(
            id = "oreilles-arriere",
            titre = "Oreilles plaquées en arrière",
            resume = "Signal à lire avec le reste du corps.",
            explication = "Des oreilles en arrière peuvent accompagner la peur, l’inconfort ou une émotion intense. Avec un corps bas et tendu, ce signal évoque souvent un malaise réel.",
            queFaire = "Lire la posture globale et donner du temps au chien.",
            aEviter = "Analyser un seul signal sans tenir compte du contexte."
        ),
        ComportementEntry(
            id = "corps-fige",
            titre = "Corps figé",
            resume = "Signal important, souvent juste avant une réaction.",
            explication = "Le figement est un arrêt du mouvement. Le chien suspend son comportement car il évalue la situation. Ce signal mérite d’être respecté, car il peut précéder une fuite, un grognement ou une défense plus nette.",
            queFaire = "Interrompre l’approche et augmenter la distance.",
            aEviter = "Continuer à toucher ou à approcher un chien figé."
        ),
        ComportementEntry(
            id = "grognement",
            titre = "Grognement",
            resume = "Avertissement utile et précieux.",
            explication = "Le grognement est une communication claire qui dit que le chien n’est pas à l’aise. Punir ce signal n’aide pas. Cela masque l’alarme sans traiter le problème de fond.",
            queFaire = "Arrêter la source d’inconfort et analyser calmement la situation.",
            aEviter = "Punir, défier ou provoquer le chien."
        ),
        ComportementEntry(
            id = "montre-dents",
            titre = "Montrer les dents",
            resume = "Le niveau d’alerte devient plus élevé.",
            explication = "Quand le chien montre les dents, il exprime une limite très claire. Ce signal apparaît souvent quand l’inconfort est important ou quand les signaux plus subtils ont été ignorés.",
            queFaire = "Créer immédiatement de la distance sans gestes brusques.",
            aEviter = "Chercher le rapport de force."
        ),
        ComportementEntry(
            id = "aplati-sol",
            titre = "Se coucher ventre au sol",
            resume = "Peut traduire peur, inhibition ou repli.",
            explication = "Un chien qui s’aplatit essaie souvent de se faire discret dans une situation qu’il vit mal. Cela se voit chez des chiens sensibles, impressionnés ou dépassés.",
            queFaire = "Rendre la situation plus prévisible et plus calme.",
            aEviter = "Tirer sur la laisse pour le faire avancer."
        ),
        ComportementEntry(
            id = "sur-le-dos",
            titre = "Se mettre sur le dos",
            resume = "Pas toujours une invitation aux caresses.",
            explication = "Cette posture peut parfois accompagner un vrai moment de détente, mais elle peut aussi exprimer de la vulnérabilité ou une tentative d’apaisement. Le contexte et la souplesse du corps sont essentiels.",
            queFaire = "Observer avant de toucher, surtout si le chien paraît tendu.",
            aEviter = "Caresser automatiquement le ventre."
        ),
        ComportementEntry(
            id = "patte-levee",
            titre = "Patte avant levée",
            resume = "Souvent associée à l’hésitation ou à l’analyse.",
            explication = "Une patte levée peut montrer que le chien observe, hésite ou évalue la situation. Il n’est pas totalement relâché, même s’il n’est pas forcément en peur.",
            queFaire = "Lui laisser du temps pour comprendre.",
            aEviter = "Prendre ce signal uniquement pour une posture mignonne."
        ),
        ComportementEntry(
            id = "secouement",
            titre = "Secouement du corps",
            resume = "Façon fréquente d’évacuer une tension.",
            explication = "Le chien peut se secouer après une rencontre, une émotion forte ou une petite contrainte. C’est souvent une bonne façon de relâcher la pression.",
            queFaire = "Laisser ce moment de relâchement exister.",
            aEviter = "Réenchaîner immédiatement sur quelque chose de trop intense."
        ),
        ComportementEntry(
            id = "haletement",
            titre = "Halètement sans effort",
            resume = "Peut révéler stress, chaleur ou inconfort.",
            explication = "Le halètement n’est pas toujours lié à l’exercice. Il peut aussi accompagner la chaleur, une tension émotionnelle, une douleur ou un inconfort général.",
            queFaire = "Vérifier le contexte et surveiller si cela se répète.",
            aEviter = "Attribuer automatiquement cela à de la simple excitation."
        ),
        ComportementEntry(
            id = "aboiement-alerte",
            titre = "Aboiement d’alerte",
            resume = "Le chien signale une présence ou un changement.",
            explication = "Face à un bruit, une arrivée ou un mouvement inhabituel, le chien peut aboyer pour prévenir. Ce n’est pas forcément de l’agressivité. Il annonce quelque chose qu’il juge important.",
            queFaire = "Rester calme puis rediriger vers un comportement plus posé.",
            aEviter = "Crier en retour."
        ),
        ComportementEntry(
            id = "aboiement-frustration",
            titre = "Aboiement de frustration",
            resume = "Le chien gère mal l’attente ou la privation d’accès.",
            explication = "Quand une porte reste fermée, qu’un congénère est inaccessible ou qu’une activité attendue tarde, certains chiens aboient par frustration. C’est souvent un trop-plein émotionnel.",
            queFaire = "Travailler l’attente progressivement et valoriser le calme.",
            aEviter = "Multiplier les ordres quand le chien déborde déjà."
        ),
        ComportementEntry(
            id = "approche-arc",
            titre = "Approche en arc de cercle",
            resume = "Code social poli chez le chien.",
            explication = "Les chiens équilibrés évitent souvent l’approche frontale. Ils contournent légèrement, ralentissent et gèrent mieux la distance. C’est une rencontre plus confortable.",
            queFaire = "Respecter les approches indirectes, surtout entre chiens.",
            aEviter = "Forcer des rencontres frontales, laisse tendue."
        ),
        ComportementEntry(
            id = "renifle-sol",
            titre = "Renifler le sol soudainement",
            resume = "Peut aider le chien à faire baisser la pression.",
            explication = "Renifler n’est pas toujours un manque d’attention. Cela peut être un moyen de décompression dans une situation socialement ou émotionnellement chargée.",
            queFaire = "Laisser ce sas de décompression exister.",
            aEviter = "Tirer immédiatement sur la laisse pour récupérer l’attention."
        ),
        ComportementEntry(
            id = "appel-au-jeu",
            titre = "Posture d’invitation au jeu",
            resume = "Avant-main basse, arrière-train relevé, gestes souples.",
            explication = "La révérence de jeu sert à indiquer une intention ludique. Elle cadre l’interaction et dit en quelque sorte que ce qui suit doit rester dans un registre de jeu.",
            queFaire = "Vérifier que l’autre partenaire répond avec plaisir et que l’échange reste équilibré.",
            aEviter = "Confondre agitation brusque et vrai jeu partagé."
        ),
        ComportementEntry(
            id = "suit-humain",
            titre = "Suit son humain partout",
            resume = "Peut être banal ou signaler une dépendance.",
            explication = "Certains chiens suivent par habitude sociale. D’autres montrent un attachement plus anxieux, avec difficulté à supporter la séparation ou le manque de contrôle.",
            queFaire = "Observer les réactions lors des absences courtes et renforcer l’autonomie progressivement.",
            aEviter = "Voir ce comportement uniquement comme une preuve d’amour."
        ),
        ComportementEntry(
            id = "destruction-absence",
            titre = "Destructions en absence",
            resume = "Souvent liées au stress, à l’ennui ou à la solitude.",
            explication = "Un chien qui détruit quand il est seul n’agit pas par vengeance. Il peut chercher à s’occuper, à évacuer une tension ou à exprimer une difficulté émotionnelle plus profonde.",
            queFaire = "Revoir la gestion de l’absence et enrichir l’environnement.",
            aEviter = "Punir après coup."
        ),
        ComportementEntry(
            id = "hypervigilance",
            titre = "Hypervigilance",
            resume = "Le chien semble constamment sur le qui-vive.",
            explication = "Un chien hypervigilant surveille beaucoup son environnement, réagit vite aux sons, mouvements ou changements, et a du mal à redescendre. Cela peut user tout son équilibre émotionnel.",
            queFaire = "Augmenter la prévisibilité, les temps calmes et la sécurité du quotidien.",
            aEviter = "Le saturer de stimulations pour l’habituer trop vite."
        )
    )
}

fun getComportementEntryById(id: String): ComportementEntry? {
    return comportementEntries().firstOrNull { it.id == id }
}

@Composable
fun ComprendreMonChienTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = MaterialTheme.typography.copy(
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
            ),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            headlineSmall = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            titleLarge = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            titleMedium = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        ),
        content = content
    )
}

@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val brush = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            listOf(
                Color(0xFF241B17),
                Color(0xFF1D1613),
                Color(0xFF171210)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFF8F4EE),
                Color(0xFFF4EFE8),
                Color(0xFFF1E7DE)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.08f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White,
                            Color.Transparent
                        )
                    )
                )
        )

        content()
    }
}

@Composable
fun EditorialContainer(
    modifier: Modifier = Modifier,
    maxWidth: Int = 760,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxWidth.dp),
            content = content
        )
    }
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(22.dp),
    centered: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft
        ),
        border = BorderStroke(
            1.dp,
            if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
        ) {
            content()
        }
    }
}

@Composable
fun AccentChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC)
            )
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EditorialKicker(text: String, centered: Boolean = false) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = if (centered) TextAlign.Center else TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PrimaryGlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PremiumPalette.Primary,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFCFB3A5),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leading?.invoke()
            if (leading != null) Spacer(modifier = Modifier.width(8.dp))
            Text(text)
        }
    }
}

@Composable
fun SecondaryPremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFF0E5DC),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun ChargementMinimal() {
    AppBackground {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PremiumPalette.Primary)
        }
    }
}

@Composable
fun AccueilScreen(
    modifier: Modifier = Modifier,
    hasSavedProgress: Boolean,
    onCommencer: () -> Unit,
    onReprendre: () -> Unit,
    onDictionnaire: () -> Unit,
    onAlimentation: () -> Unit
) {
    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BookCoverCard()

            PrimaryGlowButton(
                text = "Analyse",
                onClick = onCommencer,
                leading = {
                    Icon(Icons.Rounded.Pets, contentDescription = null, tint = Color.White)
                }
            )

            SecondaryPremiumButton(
                text = "Dictionnaire",
                onClick = onDictionnaire,
                leading = {
                    Icon(Icons.Rounded.MenuBook, contentDescription = null)
                }
            )

            SecondaryPremiumButton(
                text = "Alimentation",
                onClick = onAlimentation,
                leading = {
                    Icon(Icons.Rounded.MenuBook, contentDescription = null)
                }
            )

            if (hasSavedProgress) {
                SecondaryPremiumButton(
                    text = "Reprendre le questionnaire",
                    onClick = onReprendre,
                    leading = {
                        Icon(Icons.Rounded.AutoStories, contentDescription = null)
                    }
                )
            }

            Text(
                text = "Choisissez directement l’espace qui vous intéresse.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
    }
}

@Composable
fun BookCoverCard() {
    PremiumCard(
        centered = true,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            PremiumPalette.PrimarySoft.copy(alpha = 0.22f),
                            PremiumPalette.Accent.copy(alpha = 0.18f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Pets,
                contentDescription = null,
                tint = PremiumPalette.Primary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Choisissez ce que vous voulez consulter.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Analyse, dictionnaire et alimentation sont accessibles dès l’accueil.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuestionnaireScreen(
    modifier: Modifier = Modifier,
    question: Question,
    progress: Float,
    numero: Int,
    total: Int,
    valeurTexte: String,
    choixSelectionne: Int?,
    onValeurChangee: (String) -> Unit,
    onChoixSelectionne: (Int) -> Unit,
    onSuivant: () -> Unit
) {
    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(insets = WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            PremiumCard {
                EditorialKicker("Chapitre $numero", centered = false)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Étape $numero sur $total",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                val animatedProgress by animateFloatAsState(
                    progress.coerceIn(0f, 1f),
                    label = "progress"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(
                            if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5),
                            RoundedCornerShape(999.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(10.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(PremiumPalette.Primary, PremiumPalette.PrimarySoft)
                                ),
                                RoundedCornerShape(999.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (question) {
                    is QuestionTexte -> {
                        PremiumCard {
                            AccentChip("Réponse libre")
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = question.titre,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Une information simple pour personnaliser le bilan.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            OutlinedTextField(
                                value = valeurTexte,
                                onValueChange = onValeurChangee,
                                label = { Text("Votre réponse") },
                                placeholder = { Text("Ex. Rocky") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
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
                        }
                    }

                    is QuestionChoix -> {
                        PremiumCard {
                            AccentChip("Choix unique")
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = question.titre,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Choisissez la réponse qui ressemble le plus à votre quotidien.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(18.dp))

                            question.options.forEachIndexed { index, option ->
                                ChoiceRow(
                                    text = option,
                                    selected = choixSelectionne == index,
                                    onClick = { onChoixSelectionne(index) }
                                )
                                if (index != question.options.lastIndex) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryGlowButton(
                text = "Continuer",
                onClick = onSuivant,
                enabled = when (question) {
                    is QuestionTexte -> valeurTexte.isNotBlank()
                    is QuestionChoix -> choixSelectionne != null
                }
            )
        }
    }
}

@Composable
fun ChoiceRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .background(
                if (selected) PremiumPalette.Accent.copy(alpha = 0.20f)
                else if (isSystemInDarkTheme()) Color(0xFF231B17) else Color(0xFFF8F4EE),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (selected) PremiumPalette.Primary else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(
                    if (selected) PremiumPalette.Primary else Color.Transparent,
                    CircleShape
                )
                .border(
                    2.dp,
                    if (selected) PremiumPalette.Primary else MaterialTheme.colorScheme.outline,
                    CircleShape
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ResultatScreen(
    modifier: Modifier = Modifier,
    nomChien: String,
    analyse: ResultatAnalyse,
    onShare: () -> Unit,
    onCopy: () -> Unit,
    onExportPdf: () -> Unit,
    onRecommencer: () -> Unit
) {
    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp),
        maxWidth = 780
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PremiumCard(centered = true) {
                EditorialKicker("Lecture finale", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Bilan pour ${nomChienAffiche(nomChien)}",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Profil type : ${analyse.profil.profilType}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                AccentChip("${analyse.profil.scoreGlobal}/100")
            }

            PremiumCard(centered = true) {
                EditorialKicker("Important", centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Ce bilan reste indicatif. Il ne remplace ni un vétérinaire ni un professionnel du comportement.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            PremiumCard(centered = true) {
                EditorialKicker("Lecture rapide", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Ce qui ressort le plus chez ${nomChienAffiche(nomChien)} est une difficulté liée à ${libelleAxe(analyse.problemePrincipal).lowercase()}.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            PremiumCard(centered = true) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "Lecture principale",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        analyse.hypothesePrincipale,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val couleur = when (analyse.prioriteAction) {
                        PrioriteAction.FAIBLE -> Color(0xFF4CAF50)
                        PrioriteAction.MODEREE -> Color(0xFFFF9800)
                        PrioriteAction.ELEVEE -> Color(0xFFE53935)
                        PrioriteAction.URGENTE -> Color(0xFFD32F2F)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(couleur.copy(alpha = 0.15f))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "Priorité : ${textePrioriteAction(analyse.prioriteAction)}",
                            color = couleur,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            SimpleRadarCard(analyse = analyse)

            FacteursCard(analyse = analyse)

            PremiumCard(centered = true) {
                EditorialKicker("Niveau de situation", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                AccentChip(texteNiveauSituation(analyse.niveauSituation))

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    analyse.messageSituation,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    analyse.raisonSituation,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            PremiumCard(centered = true) {
                EditorialKicker("Lecture générale", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    analyse.profil.titre,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    analyse.profil.resume,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    analyse.profil.phraseHumaine,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            PremiumCard(centered = true) {
                EditorialKicker("Lecture émotionnelle", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    resumeEmotionnel(analyse.problemePrincipal),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    intentionChien(analyse.problemePrincipal),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    besoinPrincipal(analyse.problemePrincipal),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            PremiumCard(centered = true) {
                EditorialKicker("Niveau d’attention", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    texteVigilance(analyse.vigilance, nomChien),
                    textAlign = TextAlign.Center
                )

                if (analyse.aDejaMordu) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Comme il y a déjà eu morsure, il est important de demander conseil à un professionnel afin d’éviter que la situation ne s’aggrave.",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            PremiumCard(centered = true) {
                EditorialKicker("Ce qui se passe probablement", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    analyse.explicationPrincipale,
                    textAlign = TextAlign.Center
                )
            }

            HighlightAdviceCard(
                title = "Première piste concrète",
                advice = analyse.conseilPrincipal
            )

            PremiumCard(centered = true) {
                EditorialKicker("Les 3 prochains jours", centered = true)

                Spacer(modifier = Modifier.height(14.dp))

                SubsectionTitle("À faire")
                Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aFaire.forEach {
                    Bullet(it, centered = true)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                SubsectionTitle("À éviter")
                Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aEviter.forEach {
                    Bullet(it, centered = true)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                SubsectionTitle("À observer")
                Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aObserver.forEach {
                    Bullet(it, centered = true)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (analyse.conseilsPratiques.isNotEmpty()) {
                PremiumCard(centered = true) {
                    EditorialKicker("Conseils complémentaires", centered = true)

                    Spacer(modifier = Modifier.height(12.dp))

                    analyse.conseilsPratiques.forEach {
                        Bullet(it, centered = true)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            analyse.messageAide?.let { message ->
                PremiumCard(centered = true) {
                    EditorialKicker("Quand demander de l’aide", centered = true)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        message,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            PremiumCard(centered = true) {
                EditorialKicker("À retenir", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    phraseFin(nomChien),
                    textAlign = TextAlign.Center
                )
            }

            PrimaryGlowButton(
                text = "Partager le bilan",
                onClick = onShare
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryPremiumButton(
                    text = "Copier",
                    onClick = onCopy,
                    modifier = Modifier.weight(1f)
                )

                SecondaryPremiumButton(
                    text = "Export PDF",
                    onClick = onExportPdf,
                    modifier = Modifier.weight(1f)
                )
            }

            TextButton(
                onClick = onRecommencer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recommencer")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HighlightAdviceCard(
    title: String,
    advice: String
) {
    val backgroundBrush = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            listOf(
                Color(0xFF3A2A23),
                Color(0xFF2A1F1A)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFF3E4DA),
                Color(0xFFECD8CB)
            )
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            1.dp,
            if (isSystemInDarkTheme()) Color(0xFF6A4D41) else Color(0xFFD8B9A7)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundBrush)
                .padding(horizontal = 24.dp, vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccentChip("Le point d’appui principal")

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = advice,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "C’est souvent ici que le changement commence à prendre forme.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SubsectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ScoreLine(label: String, value: Int, centered: Boolean = false) {
    val animated by animateFloatAsState((value / 100f).coerceIn(0f, 1f), label = "score")
    Column(
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
    ) {
        if (centered) {
            Text(
                label,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            AccentChip("$value% • ${niveauPourcentage(value)}")
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    label,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                AccentChip("$value% • ${niveauPourcentage(value)}")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(
                    if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5),
                    RoundedCornerShape(999.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animated)
                    .height(10.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(PremiumPalette.Primary, PremiumPalette.PrimarySoft)
                        ),
                        RoundedCornerShape(999.dp)
                    )
            )
        }
    }
}

@Composable
fun Bullet(text: String, centered: Boolean = false) {
    if (centered) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(7.dp)
                    .background(PremiumPalette.PrimarySoft, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text,
                modifier = Modifier.widthIn(max = 560.dp),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(7.dp)
                    .background(PremiumPalette.PrimarySoft, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SimpleRadarCard(analyse: ResultatAnalyse) {
    PremiumCard(centered = true) {
        EditorialKicker("Carte du profil", centered = true)

        Spacer(modifier = Modifier.height(14.dp))

        ScoreLine("Sensibilité / peur", analyse.peur, centered = false)
        Spacer(modifier = Modifier.height(14.dp))

        ScoreLine("Attachement / dépendance", analyse.attachement, centered = false)
        Spacer(modifier = Modifier.height(14.dp))

        ScoreLine("Impulsivité / contrôle", analyse.impulsivite, centered = false)
        Spacer(modifier = Modifier.height(14.dp))

        ScoreLine("Réactivité", analyse.reactivite, centered = false)
    }
}

@Composable
fun FacteursCard(analyse: ResultatAnalyse) {
    if (analyse.facteursAggravants.isEmpty() && analyse.facteursProtecteurs.isEmpty()) return

    PremiumCard(centered = true) {
        EditorialKicker("Facteurs repérés", centered = true)

        if (analyse.facteursAggravants.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            SubsectionTitle("Ce qui aggrave")
            Spacer(modifier = Modifier.height(8.dp))

            analyse.facteursAggravants.forEach {
                Bullet(it, centered = true)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (analyse.facteursProtecteurs.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            SubsectionTitle("Ce qui protège")
            Spacer(modifier = Modifier.height(8.dp))

            analyse.facteursProtecteurs.forEach {
                Bullet(it, centered = true)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DictionnaireInfoScreen(
    modifier: Modifier = Modifier,
    onOpenFiche: (String) -> Unit
) {
    val fiches = remember { comportementEntries() }

    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp),
        maxWidth = 780
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PremiumCard(centered = true) {
                EditorialKicker("Dictionnaire comportemental", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Repères pour mieux lire le langage du chien",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Choisissez une fiche pour ouvrir une explication détaillée. Le contenu alimentation reste séparé dans le bouton « Alimentation ».",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            fiches.forEach { fiche ->
                ComportementListItem(
                    entry = fiche,
                    onClick = { onOpenFiche(fiche.id) }
                )
            }

            PremiumCard(centered = true) {
                EditorialKicker("Important", centered = true)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Ces fiches donnent des repères de lecture. Elles n’évaluent pas à elles seules une situation et ne remplacent pas l’avis d’un professionnel si le chien présente des réactions préoccupantes.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun DictionnaireDetailScreen(
    modifier: Modifier = Modifier,
    ficheId: String
) {
    val fiche = remember(ficheId) { getComportementEntryById(ficheId) }

    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp),
        maxWidth = 780
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (fiche == null) {
                PremiumCard(centered = true) {
                    EditorialKicker("Fiche", centered = true)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Fiche introuvable.",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                PremiumCard(centered = true) {
                    EditorialKicker("Fiche comportementale", centered = true)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = fiche.titre,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = fiche.resume,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                PremiumCard {
                    EditorialKicker("Explication")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = fiche.explication,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                PremiumCard {
                    EditorialKicker("Que faire")

                    Spacer(modifier = Modifier.height(12.dp))

                    Bullet(fiche.queFaire)
                }

                PremiumCard {
                    EditorialKicker("À éviter")

                    Spacer(modifier = Modifier.height(12.dp))

                    Bullet(fiche.aEviter)
                }

                PremiumCard(centered = true) {
                    EditorialKicker("Rappel", centered = true)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Un comportement isolé ne suffit pas toujours à conclure. Le contexte, l’environnement, l’historique du chien et l’ensemble du langage corporel comptent autant que le signal lui-même.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ComportementListItem(
    entry: ComportementEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft
        ),
        border = BorderStroke(
            1.dp,
            if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.titre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = entry.resume,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DictionnaireScreen(
    modifier: Modifier = Modifier
) {
    val entries = remember { dictionnaireEntries() }
    val selectedCategoryState = remember { mutableStateOf<DictionnaireCategorie?>(null) }
    val selectedEntryState = remember { mutableStateOf<DictionnaireEntry?>(null) }

    val categories = remember {
        listOf(
            DictionnaireCategorie.DANGEREUX,
            DictionnaireCategorie.AUTORISES,
            DictionnaireCategorie.INGESTION,
            DictionnaireCategorie.DIGESTION
        )
    }

    EditorialContainer(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp),
        maxWidth = 780
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                selectedEntryState.value != null -> {
                    val entry = selectedEntryState.value!!

                    PremiumCard {
                        AccentChip(entry.categorie.titre)

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = entry.titre,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = entry.contenu,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        SecondaryPremiumButton(
                            text = "Retour à la catégorie",
                            onClick = { selectedEntryState.value = null },
                            leading = {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                            }
                        )
                    }

                    PremiumCard(centered = true) {
                        EditorialKicker("Rappel", centered = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "En cas de symptômes, de douleur, d’ingestion douteuse ou de changement brutal, il faut privilégier un avis vétérinaire.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                selectedCategoryState.value != null -> {
                    val categorie = selectedCategoryState.value!!
                    val items = entries.filter { it.categorie == categorie }

                    PremiumCard(centered = true) {
                        EditorialKicker("Dictionnaire", centered = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = categorie.titre,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Sélectionne une fiche pour lire le détail.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items.forEach { entry ->
                        DictionnaireListItem(
                            entry = entry,
                            onClick = { selectedEntryState.value = entry }
                        )
                    }

                    SecondaryPremiumButton(
                        text = "Retour aux rubriques",
                        onClick = { selectedCategoryState.value = null },
                        leading = {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                        }
                    )
                }

                else -> {
                    PremiumCard(centered = true) {
                        EditorialKicker("Repères alimentation", centered = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Dictionnaire",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Choisissez une rubrique pour accéder plus vite aux bons repères du quotidien.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    PremiumCard {
                        EditorialKicker("À retenir d’abord")

                        Spacer(modifier = Modifier.height(12.dp))

                        Bullet("Tout changement alimentaire doit être progressif.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Bullet("Même un aliment banal pour l’humain peut être inadapté pour le chien.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Bullet("En cas d’ingestion suspecte ou de symptômes, la prudence passe avant l’attente.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Bullet("Vomissements répétés, douleur, abattement ou diarrhée importante justifient un avis vétérinaire.")
                    }

                    categories.forEach { categorie ->
                        DictionnaireCategoryButton(
                            categorie = categorie,
                            onClick = { selectedCategoryState.value = categorie }
                        )
                    }

                    PremiumCard(centered = true) {
                        EditorialKicker("Important", centered = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Ce dictionnaire donne des repères généraux. Il ne remplace pas un vétérinaire, surtout en cas de doute, d’ingestion inhabituelle, de douleur ou de symptômes digestifs.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DictionnaireCategoryButton(
    categorie: DictionnaireCategorie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft
        ),
        border = BorderStroke(
            1.dp,
            if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categorie.titre,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (categorie) {
                        DictionnaireCategorie.DANGEREUX -> "Les aliments à éviter pour ne pas faire d’erreur."
                        DictionnaireCategorie.AUTORISES -> "Les repères de base pour donner sans improviser."
                        DictionnaireCategorie.INGESTION -> "Les bons réflexes si le chien a avalé quelque chose."
                        DictionnaireCategorie.DIGESTION -> "Herbe, vomissements, selles et petits signaux digestifs."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DictionnaireListItem(
    entry: DictionnaireEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft
        ),
        border = BorderStroke(
            1.dp,
            if (isSystemInDarkTheme()) Color(0xFF56433B) else PremiumPalette.Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.titre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = entry.resume,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}