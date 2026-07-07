package com.laurena.comprendremonchien

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
    DANGEREUX(if (isEnglish()) "Dangerous foods" else "Aliments dangereux"),
    AUTORISES(if (isEnglish()) "Permitted foods" else "Aliments autorisés"),
    INGESTION(if (isEnglish()) "What to do after ingestion" else "Que faire en cas d'ingestion"),
    DIGESTION(if (isEnglish()) "Digestion / grass / vomiting / stools" else "Digestion / herbe / vomissements / selles")
}

data class DictionnaireEntry(val categorie: DictionnaireCategorie, val titre: String, val resume: String, val contenu: String)
data class ComportementEntry(val id: String, val titre: String, val resume: String, val explication: String, val queFaire: String, val aEviter: String)

fun dictionnaireEntries(): List<DictionnaireEntry> = if (isEnglish()) listOf(
    DictionnaireEntry(DictionnaireCategorie.DANGEREUX, "Which foods should be absolutely avoided?", "Some human foods can be genuinely problematic for dogs.", "As a precaution, avoid foods known to cause problems such as chocolate, onion, garlic, certain grapes, alcohol, cooked bones, as well as very fatty, very salty or heavily seasoned foods. In case of suspicious ingestion, especially if the dog seems unwell, do not wait if unusual symptoms appear."),
    DictionnaireEntry(DictionnaireCategorie.DANGEREUX, "Are table scraps risky?", "The little table bonus can quickly become a false friend.", "It is better to avoid table scraps that are too rich, too salty, too fatty or highly processed. Even if the dog enjoys them, they can unbalance his diet, encourage digestive problems, encourage begging and make mealtimes less stable day to day."),
    DictionnaireEntry(DictionnaireCategorie.DANGEREUX, "Why should cooked bones be avoided?", "A cooked bone can become a silent little disaster.", "Cooked bones can break into sharper fragments and cause digestive or mechanical problems. To stay safe, it is better to avoid them. If ingestion is followed by pain, vomiting, discomfort or unusual behaviour, seek veterinary advice."),
    DictionnaireEntry(DictionnaireCategorie.AUTORISES, "Can fruit or vegetables be given?", "Sometimes yes, but not as an open buffet.", "Some fruits and vegetables can be offered in small quantities, depending on their nature and the dog's tolerance. The general idea is to stay cautious, avoid foods known to cause problems and not upset the main diet. If in doubt about a specific food, it is better to check before giving."),
    DictionnaireEntry(DictionnaireCategorie.AUTORISES, "Treats: useful or risky?", "Very useful in training, less fun when they go beyond the framework.", "Treats can be useful if they remain consistent with the overall diet and are given in small quantities. The risk appears when they become too numerous, too rich or given without a framework."),
    DictionnaireEntry(DictionnaireCategorie.AUTORISES, "How to change kibble?", "Too quick a change can turn the stomach into a washing machine drum.", "A food change should be made gradually over several days. Start by mixing a small amount of the new food with the old, then gradually increase the proportion of the new one."),
    DictionnaireEntry(DictionnaireCategorie.INGESTION, "What to do if my dog ate something doubtful?", "The best reaction is calmness and speed, not improvisation.", "If your dog has eaten a suspicious or unusual food, first avoid improvised home remedies. If in doubt, especially if it is a food known to be problematic or if symptoms appear, contact a vet quickly."),
    DictionnaireEntry(DictionnaireCategorie.INGESTION, "Which signs should raise concern after ingestion?", "When the body sounds the alarm, it is best to listen straight away.", "Be particularly alert to repeated vomiting, significant diarrhoea, lethargy, unusual agitation, trembling, pain, discomfort, breathing difficulties or any sudden change in behaviour."),
    DictionnaireEntry(DictionnaireCategorie.INGESTION, "Should you wait to see if it passes?", "Sometimes waiting is reassuring, sometimes waiting complicates things.", "When the ingested food is potentially dangerous or when the dog already shows symptoms, it is better not to wait. If in doubt, it is safer to seek advice."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "Why does my dog eat grass?", "A frequent behaviour, often harmless, but worth watching if it becomes repetitive.", "Eating grass can have several explanations. Some dogs do it occasionally without it being worrying. If it becomes very frequent or is accompanied by vomiting or lethargy, it is better to seek veterinary advice."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "My dog is vomiting — is it always serious?", "An isolated vomit does not carry the same weight as a cascade of them.", "An occasional vomit can sometimes remain of no apparent concern, but repeated vomiting associated with pain, lethargy, diarrhoea, refusal to drink or unusual behaviour should prompt a vet consultation."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "How to understand abnormal stools?", "Stools often tell a small digestive story.", "Looser, more frequent stools, unusual in colour or accompanied by discomfort may reflect a digestive difficulty. If this persists or is accompanied by other symptoms, a vet check-up is needed."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "My dog eats too fast", "Swallowing meals like a rocket sometimes deserves a small adjustment.", "Some dogs eat very fast out of excitement or habit. This can encourage digestive discomfort. Splitting the portion or using an appropriate bowl can help."),
    DictionnaireEntry(DictionnaireCategorie.DIGESTION, "My dog always begs for food", "The demand can come from habit, boredom or a poorly adapted diet.", "A dog that often begs is not necessarily lacking food. The demand may be linked to habit, boredom, too frequent treats or irregular mealtimes.")
) else listOf(
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

fun comportementEntries(): List<ComportementEntry> = if (isEnglish()) listOf(
    ComportementEntry("queue-remue", "Wagging tail", "Not always a sign of joy — read the whole body.", "A wagging tail does not automatically mean the dog is happy. The tail mainly indicates a state of emotional activation.", "Observe the whole body before interacting.", "Assuming a wagging tail automatically allows contact."),
    ComportementEntry("queue-basse", "Low or tucked tail", "A frequent signal of worry or discomfort.", "When the tail drops very low or tucks under the belly, the dog may be intimidated, stressed or withdrawing.", "Give space, soften the approach and reduce pressure.", "Forcing the dog to advance, greet or stay in a situation that bothers him."),
    ComportementEntry("baillement", "Yawning outside tiredness", "Often a calming signal or mild tension.", "The dog may yawn when not tired. It is sometimes a way to regulate emotion.", "Slow down, take a break, simplify the exercise or interaction.", "Reducing this signal to simple fatigue."),
    ComportementEntry("leche-truffe", "Quick nose lick", "A subtle signal of tension or appeasement.", "This quick tongue flick often appears when the dog tries to calm himself or ease the exchange.", "Move slightly sideways and give the dog more initiative.", "Continuing to insist physically or verbally."),
    ComportementEntry("tourne-tete", "Turning the head away", "The dog is often trying to avoid pressure.", "Turning the head is a polite way to make the exchange less direct.", "Soften your posture and reduce social pressure.", "Interpreting this as ignoring or refusing to obey."),
    ComportementEntry("oreilles-arriere", "Ears pinned back", "A signal to read with the rest of the body.", "Ears back can accompany fear, discomfort or intense emotion.", "Read the overall posture and give the dog time.", "Analysing a single signal without considering context."),
    ComportementEntry("corps-fige", "Frozen body", "An important signal, often just before a reaction.", "Freezing is a stop of movement. The dog suspends his behaviour because he is assessing the situation.", "Interrupt the approach and increase distance.", "Continuing to touch or approach a frozen dog."),
    ComportementEntry("grognement", "Growling", "A useful and precious warning.", "A growl is clear communication saying the dog is not comfortable. Punishing this signal does not help.", "Stop the source of discomfort and calmly assess the situation.", "Punishing, challenging or provoking the dog."),
    ComportementEntry("montre-dents", "Showing teeth", "The alert level becomes higher.", "When the dog shows his teeth, he is expressing a very clear boundary.", "Create distance immediately without sudden movements.", "Looking for a power struggle."),
    ComportementEntry("aplati-sol", "Lying belly on the floor", "Can express fear, inhibition or withdrawal.", "A dog that flattens himself is often trying to be discreet in a situation he is experiencing badly.", "Make the situation more predictable and calm.", "Pulling on the lead to make him move forward."),
    ComportementEntry("sur-le-dos", "Rolling onto his back", "Not always an invitation for stroking.", "This posture can express vulnerability or an attempt at appeasement.", "Observe before touching, especially if the dog seems tense.", "Automatically stroking the belly."),
    ComportementEntry("patte-levee", "Raised front paw", "Often associated with hesitation or analysis.", "A raised paw can show that the dog is observing, hesitating or assessing the situation.", "Give him time to understand.", "Taking this signal only as a cute posture."),
    ComportementEntry("secouement", "Body shake", "A frequent way to release tension.", "The dog may shake after a meeting or a strong emotion.", "Let this moment of release happen.", "Immediately chaining on to something too intense."),
    ComportementEntry("haletement", "Panting without effort", "Can reveal stress, heat or discomfort.", "Panting is not always linked to exercise. It can accompany emotional tension.", "Check the context and watch if it repeats.", "Automatically attributing this to simple excitement."),
    ComportementEntry("aboiement-alerte", "Alert barking", "The dog is signalling a presence or change.", "Faced with an unusual noise or arrival, the dog may bark to warn.", "Stay calm then redirect towards a calmer behaviour.", "Shouting back."),
    ComportementEntry("aboiement-frustration", "Frustration barking", "The dog is struggling with waiting or restricted access.", "When a door stays closed or an expected activity is delayed, some dogs bark out of frustration.", "Work on waiting progressively and reward calm.", "Multiplying commands when the dog is already overflowing."),
    ComportementEntry("approche-arc", "Arced approach", "A polite social code for dogs.", "Balanced dogs often avoid frontal approaches. They arc slightly and manage distance better.", "Respect indirect approaches, especially between dogs.", "Forcing frontal meetings with a taut lead."),
    ComportementEntry("renifle-sol", "Suddenly sniffing the ground", "Can help the dog reduce pressure.", "Sniffing can be a way to decompress in an emotionally charged situation.", "Let this decompression space exist.", "Immediately pulling on the lead to regain attention."),
    ComportementEntry("appel-au-jeu", "Play bow", "Front end low, rear end raised, loose movements.", "The play bow signals a playful intention.", "Check that the other partner responds with pleasure.", "Confusing sudden agitation with real shared play."),
    ComportementEntry("suit-humain", "Follows his owner everywhere", "Can be normal or signal dependency.", "Some dogs follow out of social habit. Others show a more anxious attachment.", "Observe reactions during short absences.", "Seeing this behaviour only as proof of love."),
    ComportementEntry("destruction-absence", "Destruction during absences", "Often linked to stress, boredom or loneliness.", "A dog that destroys when alone is not acting out of revenge.", "Review absence management and enrich the environment.", "Punishing after the fact."),
    ComportementEntry("hypervigilance", "Hypervigilance", "The dog seems constantly on alert.", "A hypervigilant dog watches his environment a lot and reacts quickly to changes.", "Increase predictability, calm times and daily security.", "Overwhelming him with stimulation to habituate him too quickly."),
    ComportementEntry("coup-de-chaleur", "Heatstroke", "A veterinary emergency requiring quick action and the right response.", "Dogs regulate their temperature poorly through the skin and rely mainly on panting. In hot weather, after exertion, or without shade and water, body temperature can rise dangerously within minutes. Brachycephalic breeds (bulldogs, pugs...), older dogs, overweight dogs or those with a thick coat are particularly at risk. Warning signs include intense panting, excessive drooling, dark red or bluish gums, vomiting, disorientation or collapse.", "Immediately move the dog to shade or a cool area, wet the body and paws with lukewarm water (never ice-cold), offer water freely without forcing, and contact a vet urgently even if the dog seems to improve.", "Using ice-cold water or ice packs directly on the skin, forcing the dog to drink all at once, or waiting to see if it passes before calling a vet.")
) else listOf(
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
    ComportementEntry("hypervigilance", "Hypervigilance", "Le chien semble constamment sur le qui-vive.", "Un chien hypervigilant surveille beaucoup son environnement et réagit vite aux changements.", "Augmenter la prévisibilité, les temps calmes et la sécurité du quotidien.", "Le saturer de stimulations pour l'habituer trop vite."),
    ComportementEntry("coup-de-chaleur", "Coup de chaleur", "Une urgence vétérinaire qui demande une réaction rapide et les bons gestes.", "Le chien régule mal sa température par la peau et compte surtout sur le halètement. En cas de forte chaleur, d'effort ou de manque d'ombre et d'eau, sa température corporelle peut monter dangereusement en quelques minutes. Les races brachycéphales (bouledogues, carlins...), les chiens âgés, en surpoids ou à pelage épais sont particulièrement à risque. Les signes d'alerte incluent un halètement intense, une salivation excessive, des gencives rouge foncé ou bleutées, des vomissements, une désorientation ou un effondrement.", "Déplacez immédiatement le chien à l'ombre ou au frais, mouillez-le à l'eau tiède (jamais glacée) sur le corps et les pattes, proposez de l'eau à volonté sans forcer, et contactez un vétérinaire en urgence même si l'état semble s'améliorer.", "Utiliser de l'eau glacée ou des packs de glace directement sur la peau, forcer le chien à boire d'un coup, ou attendre de voir si ça passe avant d'appeler un vétérinaire.")
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
        navigationIcon = { if (onBack != null) IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = strContentDescRetour(), tint = MaterialTheme.colorScheme.onBackground) } },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun ChargementMinimal() {
    AppBackground { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PremiumPalette.Primary) } }
}

@Composable
fun ChargementAnalyseScreen(modifier: Modifier = Modifier, onTermine: () -> Unit) {
    val messages = strChargementMessages()
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
            PrimaryGlowButton(text = strBtnDemarrerBilan(), onClick = onCommencer, leading = { Icon(Icons.Rounded.Pets, contentDescription = null, tint = Color.White) })
            SecondaryPremiumButton(text = strBtnDictionnaire(), onClick = onDictionnaire, leading = { Icon(Icons.Rounded.MenuBook, contentDescription = null) })
            SecondaryPremiumButton(text = strBtnAlimentation(), onClick = onAlimentation, leading = { Icon(Icons.Rounded.Star, contentDescription = null) })
            if (hasSavedProgress) SecondaryPremiumButton(text = strBtnReprendre(), onClick = onReprendre, leading = { Icon(Icons.Rounded.AutoStories, contentDescription = null) })
        }
    }
}

@Composable
fun IntroductionScreen(modifier: Modifier = Modifier, onCommencer: () -> Unit) {
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).padding(horizontal = 20.dp, vertical = 8.dp)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(8.dp))
            PremiumCard(centered = true) {
                EditorialKicker(strIntroKicker(), centered = true); Spacer(modifier = Modifier.height(12.dp))
                Text(strIntroDuree(), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            }
            PremiumCard {
                EditorialKicker(strIntroExplorerKicker()); Spacer(modifier = Modifier.height(14.dp))
                Bullet(strIntroExplorer1()); Spacer(modifier = Modifier.height(8.dp))
                Bullet(strIntroExplorer2()); Spacer(modifier = Modifier.height(8.dp))
                Bullet(strIntroExplorer3()); Spacer(modifier = Modifier.height(8.dp))
                Bullet(strIntroExplorer4())
            }
            Spacer(modifier = Modifier.height(4.dp))
            PrimaryGlowButton(text = strBtnCommencer(), onClick = onCommencer, leading = { Icon(Icons.Rounded.Pets, contentDescription = null, tint = Color.White) })
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
    val titreSection = strTitreSection(question.id)
    val boutonActif = when (question) { is QuestionTexte -> valeurTexte.isNotBlank(); is QuestionChoix -> choixSelectionne != null }
    val scrollStateQuestion = rememberScrollState()
    LaunchedEffect(question.id) { scrollStateQuestion.scrollTo(0) }

    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).padding(horizontal = 20.dp, vertical = 10.dp)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            PremiumCard {
                SectionChip(titreSection)
                Spacer(modifier = Modifier.height(10.dp))
                Text("$numero ${if (isEnglish()) "of" else "sur"} $total", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(12.dp))
                val animatedProgress by animateFloatAsState(progress.coerceIn(0f, 1f), label = "progress")
                Box(modifier = Modifier.fillMaxWidth().height(10.dp).background(if (isSystemInDarkTheme()) Color(0xFF342923) else Color(0xFFE9DED5), RoundedCornerShape(999.dp))) {
                    Box(modifier = Modifier.fillMaxWidth(animatedProgress).height(10.dp).background(Brush.horizontalGradient(listOf(PremiumPalette.Primary, PremiumPalette.PrimarySoft)), RoundedCornerShape(999.dp)))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.weight(1f).verticalScroll(scrollStateQuestion), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                when (question) {
                    is QuestionTexte -> {
                        PremiumCard {
                            Text(question.titre, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(18.dp))
                            OutlinedTextField(
                                value = valeurTexte, onValueChange = onValeurChangee,
                                label = { Text(strQuestionReponseLabel()) },
                                placeholder = { Text(strQuestionReponsePlaceholder()) },
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
                PrimaryGlowButton(text = strBtnContinuer(), onClick = onSuivant, enabled = boutonActif)
                if (!boutonActif) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(when (question) { is QuestionTexte -> strQuestionHintTexte(); is QuestionChoix -> strQuestionHintChoix() }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
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
    val context = LocalContext.current
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp), maxWidth = 780) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            PremiumCard(centered = true) {
                EditorialKicker(strResultatKicker(), centered = true); Spacer(modifier = Modifier.height(10.dp))
                Text(strResultatTitreBilan(nomChienAffiche(nomChien)), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(8.dp))
                AccentChip(analyse.profil.profilType)
            }
            if (!analyse.raceCategorie.isNullOrBlank() || !analyse.racePrecise.isNullOrBlank()) RaceCard(raceCategorie = analyse.raceCategorie, racePrecise = analyse.racePrecise)
            PremiumCard(centered = true) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EditorialKicker(strResultatLecturePrincipale(), centered = true); Spacer(modifier = Modifier.height(8.dp))
                    Text(analyse.hypothesePrincipale, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(couleurFondPriorite(analyse.prioriteAction)).padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(strResultatPriorite(strPrioriteAction(analyse.prioriteAction)), color = couleurPriorite(analyse.prioriteAction), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            PremiumCard(centered = true) {
                EditorialKicker(strResultatRessent(nomChienAffiche(nomChien)), centered = true); Spacer(modifier = Modifier.height(10.dp))
                Text(analyse.profil.phraseHumaine, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
                Text(strResumeEmotionnel(analyse.problemePrincipal), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, color = PremiumPalette.Primary); Spacer(modifier = Modifier.height(8.dp))
                Text(strIntentionChien(analyse.problemePrincipal), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface); Spacer(modifier = Modifier.height(6.dp))
                Text(strBesoinPrincipal(analyse.problemePrincipal), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            PremiumCard(centered = true) { EditorialKicker(strResultatCoupOeil(), centered = true); Spacer(modifier = Modifier.height(14.dp)); QuatreAxesGrid(analyse = analyse) }

            PremiumCard(centered = true) {
                EditorialKicker(strResultatNiveauSituation(), centered = true); Spacer(modifier = Modifier.height(10.dp))
                AccentChip(strNiveauSituation(analyse.niveauSituation)); Spacer(modifier = Modifier.height(14.dp))
                Text(analyse.messageSituation, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(10.dp))
                Text(analyse.raisonSituation, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            PremiumCard(centered = true) { EditorialKicker(strResultatInquieter(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strTexteVigilance(analyse.vigilance, nomChien), textAlign = TextAlign.Center) }
            PremiumCard(centered = true) { EditorialKicker(strResultatSePasse(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(analyse.explicationPrincipale, textAlign = TextAlign.Center) }
            HighlightAdviceCard(title = strResultatLevierPrincipal(), advice = analyse.conseilPrincipal)
            OriginesPossiblesCard(origines = analyse.originesPossibles)
            PremiumCard(centered = true) {
                EditorialKicker(strResultat3Jours(), centered = true); Spacer(modifier = Modifier.height(14.dp))
                SubsectionTitle(strResultatAFaire()); Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aFaire.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) }
                Spacer(modifier = Modifier.height(8.dp)); SubsectionTitle(strResultatAEviter()); Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aEviter.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) }
                Spacer(modifier = Modifier.height(8.dp)); SubsectionTitle(strResultatAObserver()); Spacer(modifier = Modifier.height(8.dp))
                analyse.planAction.aObserver.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) }
            }
            if (analyse.conseilsPratiques.isNotEmpty()) {
                PremiumCard(centered = true) { EditorialKicker(strResultatConseilsComplementaires(), centered = true); Spacer(modifier = Modifier.height(12.dp)); analyse.conseilsPratiques.forEach { Bullet(it, centered = true); Spacer(modifier = Modifier.height(8.dp)) } }
            }
            if (analyse.messageAide != null || analyse.aDejaMordu) {
                PremiumCard(centered = true) {
                    EditorialKicker(strResultatQuandAide(), centered = true); Spacer(modifier = Modifier.height(10.dp))
                    if (analyse.aDejaMordu) Text(strResultatMorsurePro(), textAlign = TextAlign.Center, color = PremiumPalette.PrioriteModere, fontWeight = FontWeight.SemiBold)
                    else analyse.messageAide?.let { Text(it, textAlign = TextAlign.Center, color = PremiumPalette.PrioriteUrgente, fontWeight = FontWeight.SemiBold) }
                }
            }
            PremiumCard(centered = true) { EditorialKicker(strResultatImportant(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strResultatDisclaimer(), textAlign = TextAlign.Center) }
            FichesRecommandeesCard(analyse = analyse, nomChien = nomChienAffiche(nomChien), onOpenFiche = onOpenFiche, onOpenAlimentation = onOpenAlimentation)
            PremiumCard(centered = true) { EditorialKicker(strResultatARetenir(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strPhraseFin(nomChien), textAlign = TextAlign.Center) }
            PremiumCard(centered = true) {
                EditorialKicker(strResultatLeLivre(), centered = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(strResultatAllerPlusLoinLivre(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(14.dp))
                PrimaryGlowButton(
                    text = strBtnVoirLivres(),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(if (isEnglish()) "https://understanding-my-dog.carrd.co" else "https://comprendre-mon-chien.carrd.co"))
                        context.startActivity(intent)
                    },
                    leading = { Icon(Icons.Rounded.MenuBook, contentDescription = null, tint = Color.White) }
                )
            }
            ConsultationCard()
            ActionButtonsGrid(onShare = onShare, onCopy = onCopy, onExportPdf = onExportPdf, onRecommencer = onRecommencer)
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.HorizontalDivider(color = PremiumPalette.Border, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 40.dp))
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onRecommencer, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(6.dp))
                Text(strBtnRecommencer(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ConsultationCard() {
    if (!showConsultation()) return
    val context = LocalContext.current
    val backgroundBrush = if (isSystemInDarkTheme())
        Brush.verticalGradient(listOf(Color(0xFF2E2018), Color(0xFF231B14)))
    else
        Brush.verticalGradient(listOf(Color(0xFFF5EBE0), Color(0xFFEEE0D2)))
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF5A4035) else Color(0xFFD4B8A8))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(backgroundBrush).padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditorialKicker(strConsultationTitre(), centered = true)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                strConsultationSousTitre(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = PremiumPalette.Primary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(strConsultationDescription(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                    .background(if (isSystemInDarkTheme()) Color(0xFF2A1F1A) else Color(0xFFF4EDE6))
                    .padding(14.dp)
            ) {
                Text(
                    strConsultationDisclaimer(),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                strConsultationPrix(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(14.dp))
            PrimaryGlowButton(
                text = strConsultationBouton(),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CONSULTATION_BOOKING_URL))
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun OriginesPossiblesCard(origines: String) {
    val backgroundBrush = if (isSystemInDarkTheme())
        Brush.verticalGradient(listOf(Color(0xFF2E2018), Color(0xFF231B14)))
    else
        Brush.verticalGradient(listOf(Color(0xFFF5EBE0), Color(0xFFEEE0D2)))
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color(0xFF5A4035) else Color(0xFFD4B8A8))
    ) {
        Column(modifier = Modifier.fillMaxWidth().background(backgroundBrush).padding(horizontal = 24.dp, vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AccentChip(strResultatComprendreAgir())
            Spacer(modifier = Modifier.height(14.dp))
            EditorialKicker(strResultatPourquoi(), centered = true)
            Spacer(modifier = Modifier.height(12.dp))
            Text(origines, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
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
                Text(strMorsureTitre(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold, color = PremiumPalette.PrioriteUrgente, textAlign = TextAlign.Center)
                Icon(Icons.Rounded.Warning, contentDescription = null, tint = PremiumPalette.PrioriteUrgente, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(strMorsuTexte(nomChien), style = MaterialTheme.typography.titleMedium, color = PremiumPalette.PrioriteUrgente, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text(strMorsuConseil(), color = if (isDark) Color(0xFFFFCFC5) else Color(0xFF5C1A0A), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun QuatreAxesGrid(analyse: ResultatAnalyse) {
    val axes = listOf(
        Triple(strLibelleAxe(Axe.PEUR), analyse.niveauPeur, analyse.peur),
        Triple(strLibelleAxe(Axe.ATTACHEMENT), analyse.niveauAttachement, analyse.attachement),
        Triple(strLibelleAxe(Axe.IMPULSIVITE), analyse.niveauImpulsivite, analyse.impulsivite),
        Triple(strLibelleAxe(Axe.REACTIVITE), analyse.niveauReactivite, analyse.reactivite)
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        axes.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { (label, niveau, score) ->
                    val animated by animateFloatAsState((score / 100f).coerceIn(0f, 1f), label = "axe_$label")
                    Column(
                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp))
                            .background(if (isSystemInDarkTheme()) Color(0xFF231B17) else PremiumPalette.PaperSoft)
                            .border(1.dp, PremiumPalette.Border, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        Text(strNiveauAxe(niveau), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PremiumPalette.Primary, textAlign = TextAlign.Center)
                        Box(modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(999.dp)).background(PremiumPalette.PrimarySoft.copy(alpha = 0.2f))) {
                            Box(modifier = Modifier.fillMaxWidth(animated).height(5.dp).clip(RoundedCornerShape(999.dp)).background(PremiumPalette.PrimarySoft))
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
        EditorialKicker(strResultatAllerPlusLoin(nomChien)); Spacer(modifier = Modifier.height(14.dp))
        if (fichesBehavior.isNotEmpty()) {
            Text(strResultatFichesComportementales(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = PremiumPalette.PrimarySoft); Spacer(modifier = Modifier.height(8.dp))
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
            Text(strResultatReperes(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = PremiumPalette.PrimarySoft); Spacer(modifier = Modifier.height(8.dp))
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
    val entries = comportementEntries()
    return when (analyse.problemePrincipal) {
        Axe.PEUR -> listOf("corps-fige", "oreilles-arriere", "queue-basse")
        Axe.ATTACHEMENT -> listOf("suit-humain", "destruction-absence", "aboiement-frustration")
        Axe.IMPULSIVITE -> listOf("haletement", "appel-au-jeu", "secouement")
        Axe.REACTIVITE -> listOf("grognement", "corps-fige", "aboiement-alerte")
    }.mapNotNull { id -> entries.firstOrNull { it.id == id }?.let { id to it.titre } }
}

fun recommanderFichesAlimentation(analyse: ResultatAnalyse): List<String> {
    val entries = dictionnaireEntries()
    return if (analyse.contexte.physique >= 2)
        entries.filter { it.categorie == DictionnaireCategorie.INGESTION }.take(2).map { it.titre }
    else
        entries.filter { it.categorie == DictionnaireCategorie.AUTORISES }.take(2).map { it.titre }
}

@Composable
fun ActionButtonsGrid(onShare: () -> Unit, onCopy: () -> Unit, onExportPdf: () -> Unit, onRecommencer: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionButton(text = strBtnPartager(), icon = Icons.Rounded.Share, primary = true, modifier = Modifier.weight(1f), onClick = onShare)
            ActionButton(text = strBtnExportPdf(), icon = Icons.Rounded.PictureAsPdf, primary = true, modifier = Modifier.weight(1f), onClick = onExportPdf)
        }
        ActionButton(text = strBtnCopierResume(), icon = Icons.Rounded.ContentCopy, primary = false, modifier = Modifier.fillMaxWidth(), onClick = onCopy)
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
        EditorialKicker(strResultatProfilRace(), centered = true); Spacer(modifier = Modifier.height(10.dp))
        Text(nomAffiche, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PremiumPalette.Primary, textAlign = TextAlign.Center)
        if (!raceCategorie.isNullOrBlank() && !racePrecise.isNullOrBlank()) { Spacer(modifier = Modifier.height(4.dp)); Text(raceCategorie, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center) }
        if (predispositions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp)); Text(strResultatPredispositions(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()); Spacer(modifier = Modifier.height(8.dp))
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
            AccentChip(strResultatPointAppui()); Spacer(modifier = Modifier.height(14.dp))
            Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
            Text(advice, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
            Text(strResultatChangement(), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SubsectionTitle(text: String) { Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center) }

@Composable
fun ScoreLine(label: String, value: Int, centered: Boolean = false) {
    val animated by animateFloatAsState((value / 100f).coerceIn(0f, 1f), label = "score")
    Column(horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start) {
        if (centered) { Text(label, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(8.dp)); AccentChip(strNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(value))) }
        else { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(label, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface); Spacer(modifier = Modifier.width(8.dp)); AccentChip(strNiveauAxe(QuestionnaireEngine.calculerNiveauAxe(value))) } }
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
fun DictionnaireInfoScreen(modifier: Modifier = Modifier, onOpenFiche: (String) -> Unit) {
    val fiches = remember { comportementEntries() }
    var recherche by remember { mutableStateOf("") }
    val fichesFiltrees = remember(recherche) { if (recherche.isBlank()) fiches else fiches.filter { it.titre.contains(recherche, ignoreCase = true) || it.resume.contains(recherche, ignoreCase = true) } }
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp), maxWidth = 780) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            PremiumCard(centered = true) {
                EditorialKicker(strDicoTitre(), centered = true); Spacer(modifier = Modifier.height(10.dp))
                Text(strDicoSousTitre(), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(value = recherche, onValueChange = { recherche = it }, placeholder = { Text(strDicoRecherchePlaceholder()) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PremiumPalette.Primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline, focusedTextColor = MaterialTheme.colorScheme.onSurface, unfocusedTextColor = MaterialTheme.colorScheme.onSurface, cursorColor = PremiumPalette.Primary, focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface))
            }
            if (fichesFiltrees.isEmpty()) PremiumCard(centered = true) { Text(strDicoAucunResultat(recherche), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            else fichesFiltrees.forEach { fiche -> ComportementListItem(entry = fiche, onClick = { onOpenFiche(fiche.id) }) }
            PremiumCard(centered = true) { EditorialKicker(strDicoImportant(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strDicoDisclaimer(), textAlign = TextAlign.Center) }
        }
    }
}

@Composable
fun DictionnaireDetailScreen(modifier: Modifier = Modifier, ficheId: String) {
    val fiche = remember(ficheId) { getComportementEntryById(ficheId) }
    EditorialContainer(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp), maxWidth = 780) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (fiche == null) PremiumCard(centered = true) { Text(strDicoFicheIntrouvable(), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center) }
            else {
                PremiumCard(centered = true) { EditorialKicker(strDicoFicheKicker(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(fiche.titre, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(10.dp)); Text(fiche.resume, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                PremiumCard { EditorialKicker(strDicoExplication()); Spacer(modifier = Modifier.height(12.dp)); Text(fiche.explication, style = MaterialTheme.typography.bodyLarge) }
                PremiumCard { EditorialKicker(strDicoQueFaire()); Spacer(modifier = Modifier.height(12.dp)); Bullet(fiche.queFaire) }
                PremiumCard { EditorialKicker(strDicoAEviter()); Spacer(modifier = Modifier.height(12.dp)); Bullet(fiche.aEviter) }
                PremiumCard(centered = true) { EditorialKicker(strDicoRappelKicker(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strDicoRappel(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
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
                    PremiumCard { AccentChip(entry.categorie.titre); Spacer(modifier = Modifier.height(14.dp)); Text(entry.titre, style = MaterialTheme.typography.headlineSmall); Spacer(modifier = Modifier.height(12.dp)); Text(entry.contenu, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(modifier = Modifier.height(18.dp)); SecondaryPremiumButton(strBtnRetourCategorie(), onClick = { selectedEntryState.value = null }, leading = { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null) }) }
                    PremiumCard(centered = true) { EditorialKicker(if (isEnglish()) "Reminder" else "Rappel", centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strAlimRappel(), textAlign = TextAlign.Center) }
                }
                selectedCategoryState.value != null -> {
                    val categorie = selectedCategoryState.value!!
                    val items = entries.filter { it.categorie == categorie }
                    PremiumCard(centered = true) { EditorialKicker(strAlimTitre(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(categorie.titre, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center) }
                    items.forEach { entry -> DictionnaireListItem(entry = entry, onClick = { selectedEntryState.value = entry }) }
                    SecondaryPremiumButton(strBtnRetourRubriques(), onClick = { selectedCategoryState.value = null }, leading = { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null) })
                }
                else -> {
                    PremiumCard(centered = true) { EditorialKicker(strAlimTitre(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strAlimSousTitre(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    PremiumCard { EditorialKicker(strAlimARetenirKicker()); Spacer(modifier = Modifier.height(12.dp)); Bullet(strAlimRetenir1()); Spacer(modifier = Modifier.height(8.dp)); Bullet(strAlimRetenir2()); Spacer(modifier = Modifier.height(8.dp)); Bullet(strAlimRetenir3()) }
                    categories.forEach { categorie -> DictionnaireCategoryButton(categorie = categorie, onClick = { selectedCategoryState.value = categorie }) }
                    PremiumCard(centered = true) { EditorialKicker(strAlimImportant(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strAlimDisclaimer(), textAlign = TextAlign.Center) }
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
                Text(when (categorie) {
                    DictionnaireCategorie.DANGEREUX -> strAlimCatDangereuxDesc()
                    DictionnaireCategorie.AUTORISES -> strAlimCatAutorisesDesc()
                    DictionnaireCategorie.INGESTION -> strAlimCatIngestionDesc()
                    DictionnaireCategorie.DIGESTION -> strAlimCatDigestionDesc()
                }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            PremiumCard(centered = true) { EditorialKicker(strParamsKicker(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strParamsAppTitre(), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(6.dp)); AccentChip(strParamsVersion(version)) }
            PremiumCard { EditorialKicker(strParamsTutorielKicker()); Spacer(modifier = Modifier.height(12.dp)); Text(strParamsTutorielTexte(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(modifier = Modifier.height(14.dp)); SecondaryPremiumButton(strBtnRevoirIntroduction(), onClick = onRevoirOnboarding, leading = { Icon(Icons.Rounded.AutoStories, contentDescription = null) }) }
            PremiumCard {
                EditorialKicker(strParamsConfidentialiteKicker()); Spacer(modifier = Modifier.height(12.dp))
                Text(strParamsConfidentialiteTexte(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(modifier = Modifier.height(14.dp))
                SecondaryPremiumButton(strBtnPolitiqueConfidentialite(), onClick = { val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://laurenaharoy-ctrl.github.io/comprendremonchien2/confidentialite.html")); context.startActivity(intent) }, leading = { Icon(Icons.Rounded.MenuBook, contentDescription = null) })
            }
            PremiumCard(centered = true) { EditorialKicker(strParamsAProposKicker(), centered = true); Spacer(modifier = Modifier.height(10.dp)); Text(strParamsAProposTexte(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center) }
        }
    }
}