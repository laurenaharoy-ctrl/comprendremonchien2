package com.example.comprendremonchien2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.FileProvider
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "comprendre_mon_chien_state")

sealed interface AppScreen {
    data object Accueil : AppScreen
    data object Onboarding : AppScreen
    data object Introduction : AppScreen
    data object Questionnaire : AppScreen
    data object Chargement : AppScreen
    data object Resultat : AppScreen
    data object Dictionnaire : AppScreen
    data class DictionnaireDetail(val ficheId: String) : AppScreen
    data object Alimentation : AppScreen
    data object Feedback : AppScreen
    data object Historique : AppScreen
    data class HistoriqueDetail(val bilanId: String) : AppScreen
}

fun AppScreen.toStorageValue(): String = when (this) {
    AppScreen.Accueil -> "accueil"
    AppScreen.Onboarding -> "accueil"
    AppScreen.Introduction -> "introduction"
    AppScreen.Questionnaire -> "questionnaire"
    AppScreen.Chargement -> "questionnaire"
    AppScreen.Resultat -> "resultat"
    AppScreen.Dictionnaire -> "dictionnaire"
    is AppScreen.DictionnaireDetail -> "dictionnaire_detail:${this.ficheId}"
    AppScreen.Alimentation -> "alimentation"
    AppScreen.Feedback -> "accueil"
    AppScreen.Historique -> "historique"
    is AppScreen.HistoriqueDetail -> "historique"
}

fun screenFromStorage(value: String): AppScreen = when {
    value == "introduction" -> AppScreen.Introduction
    value == "questionnaire" -> AppScreen.Questionnaire
    value == "resultat" -> AppScreen.Resultat
    value == "dictionnaire" -> AppScreen.Dictionnaire
    value.startsWith("dictionnaire_detail:") -> {
        val ficheId = value.removePrefix("dictionnaire_detail:")
        if (ficheId.isBlank()) AppScreen.Dictionnaire else AppScreen.DictionnaireDetail(ficheId)
    }
    value == "alimentation" -> AppScreen.Alimentation
    value == "historique" -> AppScreen.Historique
    else -> AppScreen.Accueil
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComprendreMonChienTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ComprendreMonChienApp()
                }
            }
        }
    }
}

@Composable
fun ComprendreMonChienApp() {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val questions = remember { questionsApplication() }

    var indexQuestion by remember { mutableStateOf(0) }
    var screen by remember { mutableStateOf<AppScreen>(AppScreen.Accueil) }
    var hasSavedProgress by remember { mutableStateOf(false) }
    var isLoaded by remember { mutableStateOf(false) }
    var screenAvantFeedback by remember { mutableStateOf<AppScreen>(AppScreen.Accueil) }

    val reponsesTexte = remember { mutableStateMapOf<String, String>() }
    val reponsesChoix = remember { mutableStateMapOf<String, Int>() }

    // Historique
    val bilans by HistoriqueManager.getBilans(context).collectAsState(initial = emptyList())

    fun saveState() {
        scope.launch {
            context.dataStore.edit { prefs ->
                prefs[stringPreferencesKey("screen")] = screen.toStorageValue()
                prefs[intPreferencesKey("index")] = indexQuestion
                prefs[stringPreferencesKey("reponsesTexte")] =
                    JSONObject(reponsesTexte.toMap()).toString()
                prefs[stringPreferencesKey("reponsesChoix")] =
                    JSONObject(reponsesChoix.toMap()).toString()
            }
            hasSavedProgress = reponsesTexte.isNotEmpty() || reponsesChoix.isNotEmpty()
        }
    }

    fun clearSavedState() {
        scope.launch {
            context.dataStore.edit { prefs -> prefs.clear() }
            hasSavedProgress = false
        }
    }

    LaunchedEffect(Unit) {
        val prefs = context.dataStore.data.first()
        val onboardingFait = prefs[stringPreferencesKey("onboarding_done")] == "true"
        val savedScreen = prefs[stringPreferencesKey("screen")] ?: "accueil"
        val savedIndex = prefs[intPreferencesKey("index")] ?: 0
        val texteJson = prefs[stringPreferencesKey("reponsesTexte")]
        val choixJson = prefs[stringPreferencesKey("reponsesChoix")]

        if (!texteJson.isNullOrBlank()) {
            val texteObj = JSONObject(texteJson)
            for (key in texteObj.keys()) reponsesTexte[key] = texteObj.getString(key)
        }
        if (!choixJson.isNullOrBlank()) {
            val choixObj = JSONObject(choixJson)
            for (key in choixObj.keys()) reponsesChoix[key] = choixObj.getInt(key)
        }

        hasSavedProgress = reponsesTexte.isNotEmpty() || reponsesChoix.isNotEmpty()

        screen = when {
            !onboardingFait -> AppScreen.Onboarding
            hasSavedProgress -> screenFromStorage(savedScreen)
            else -> AppScreen.Accueil
        }
        if (hasSavedProgress && onboardingFait) indexQuestion = savedIndex

        isLoaded = true
    }

    fun marquerOnboardingFait() {
        scope.launch {
            context.dataStore.edit { prefs ->
                prefs[stringPreferencesKey("onboarding_done")] = "true"
            }
        }
    }

    val questionsVisibles = remember(reponsesChoix.toMap()) {
        questions.filter { questionDoitEtreAffichee(it, reponsesChoix) }
    }

    LaunchedEffect(questionsVisibles.size, isLoaded) {
        if (!isLoaded) return@LaunchedEffect
        if (questionsVisibles.isEmpty()) indexQuestion = 0
        else if (indexQuestion > questionsVisibles.lastIndex) indexQuestion = questionsVisibles.lastIndex
    }

    // ── Envoi email feedback ──────────────────────────────────────────────────
    fun envoyerFeedbackEmail(categorie: String, ecran: String, message: String, version: String) {
        val sujet = "[Comprendre mon chien] $categorie — $ecran"
        val corps = """
Catégorie : $categorie
Écran concerné : $ecran
Version appli : $version

Message :
$message

---
Envoyé depuis l'application Comprendre mon chien
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("laurenaharoy@yahoo.fr"))
            putExtra(Intent.EXTRA_SUBJECT, sujet)
            putExtra(Intent.EXTRA_TEXT, corps)
        }
        try {
            context.startActivity(Intent.createChooser(intent, "Envoyer le signalement"))
        } catch (e: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar("Aucune application email trouvée sur cet appareil.")
            }
        }
    }

    BackHandler(enabled = screen != AppScreen.Accueil && screen != AppScreen.Onboarding) {
        when (screen) {
            AppScreen.Introduction -> screen = AppScreen.Accueil
            AppScreen.Questionnaire -> {
                if (indexQuestion > 0) { indexQuestion--; saveState() }
                else { screen = AppScreen.Introduction; saveState() }
            }
            AppScreen.Chargement -> { screen = AppScreen.Questionnaire; saveState() }
            AppScreen.Resultat -> { screen = AppScreen.Questionnaire; saveState() }
            AppScreen.Dictionnaire -> { screen = AppScreen.Accueil; saveState() }
            is AppScreen.DictionnaireDetail -> { screen = AppScreen.Dictionnaire; saveState() }
            AppScreen.Alimentation -> { screen = AppScreen.Accueil; saveState() }
            AppScreen.Feedback -> { screen = screenAvantFeedback }
            AppScreen.Historique -> { screen = AppScreen.Accueil; saveState() }
            is AppScreen.HistoriqueDetail -> { screen = AppScreen.Historique }
            AppScreen.Accueil, AppScreen.Onboarding -> Unit
        }
    }

    AppBackground {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                val titreEcran = when (screen) {
                    AppScreen.Accueil -> "Comprendre mon chien"
                    AppScreen.Onboarding -> ""
                    AppScreen.Introduction -> "Avant de commencer"
                    AppScreen.Questionnaire -> "Questionnaire"
                    AppScreen.Chargement -> "Analyse"
                    AppScreen.Resultat -> "Résultat"
                    AppScreen.Dictionnaire -> "Dictionnaire comportemental"
                    is AppScreen.DictionnaireDetail -> {
                        val ficheId = (screen as AppScreen.DictionnaireDetail).ficheId
                        getComportementEntryById(ficheId)?.titre ?: "Fiche comportementale"
                    }
                    AppScreen.Alimentation -> "Alimentation"
                    AppScreen.Feedback -> "Signalement"
                    AppScreen.Historique -> "Historique des bilans"
                    is AppScreen.HistoriqueDetail -> "Détail du bilan"
                }

                val onBack: (() -> Unit)? = if (screen != AppScreen.Accueil && screen != AppScreen.Chargement) {
                    {
                        when (screen) {
                            AppScreen.Introduction -> screen = AppScreen.Accueil
                            AppScreen.Questionnaire -> {
                                if (indexQuestion > 0) indexQuestion--
                                else screen = AppScreen.Introduction
                                saveState()
                            }
                            AppScreen.Resultat -> { screen = AppScreen.Questionnaire; saveState() }
                            AppScreen.Dictionnaire -> { screen = AppScreen.Accueil; saveState() }
                            is AppScreen.DictionnaireDetail -> { screen = AppScreen.Dictionnaire; saveState() }
                            AppScreen.Alimentation -> { screen = AppScreen.Accueil; saveState() }
                            AppScreen.Feedback -> screen = screenAvantFeedback
                            AppScreen.Historique -> { screen = AppScreen.Accueil; saveState() }
                            is AppScreen.HistoriqueDetail -> screen = AppScreen.Historique
                            else -> Unit
                        }
                    }
                } else null

                // Actions dans la TopBar selon l'écran
                val actions: @Composable () -> Unit = {
                    when (screen) {
                        // Historique accessible depuis l'accueil
                        AppScreen.Accueil -> {
                            if (bilans.isNotEmpty()) {
                                IconButton(onClick = {
                                    screen = AppScreen.Historique
                                    saveState()
                                }) {
                                    Icon(
                                        Icons.Rounded.History,
                                        contentDescription = "Historique des bilans",
                                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                        // Bouton feedback sur tous les écrans sauf Feedback lui-même et Chargement
                        AppScreen.Chargement, AppScreen.Feedback, AppScreen.Onboarding -> Unit
                        else -> {
                            IconButton(onClick = {
                                screenAvantFeedback = screen
                                screen = AppScreen.Feedback
                            }) {
                                Icon(
                                    Icons.Rounded.Feedback,
                                    contentDescription = "Signaler un problème",
                                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }

                PremiumTopBarWithActions(
                    title = titreEcran,
                    onBack = onBack,
                    actions = actions
                )
            }
        ) { padding ->

            if (!isLoaded) {
                ChargementMinimal()
                return@Scaffold
            }

            when (screen) {
                AppScreen.Onboarding -> {
                    OnboardingScreen(
                        onTermine = {
                            marquerOnboardingFait()
                            screen = AppScreen.Accueil
                        }
                    )
                }

                AppScreen.Accueil -> {
                    AccueilScreen(
                        modifier = Modifier.padding(padding),
                        hasSavedProgress = hasSavedProgress,
                        onCommencer = { screen = AppScreen.Introduction },
                        onReprendre = { screen = AppScreen.Questionnaire; saveState() },
                        onDictionnaire = { screen = AppScreen.Dictionnaire; saveState() },
                        onAlimentation = { screen = AppScreen.Alimentation; saveState() }
                    )
                }

                AppScreen.Introduction -> {
                    IntroductionScreen(
                        modifier = Modifier.padding(padding),
                        onCommencer = {
                            reponsesTexte.clear()
                            reponsesChoix.clear()
                            indexQuestion = 0
                            screen = AppScreen.Questionnaire
                            clearSavedState()
                            saveState()
                        }
                    )
                }

                AppScreen.Questionnaire -> {
                    if (questionsVisibles.isNotEmpty()) {
                        val question = questionsVisibles[indexQuestion]
                        QuestionnaireScreen(
                            modifier = Modifier.padding(padding),
                            question = question,
                            progress = (indexQuestion + 1f) / questionsVisibles.size,
                            numero = indexQuestion + 1,
                            total = questionsVisibles.size,
                            valeurTexte = reponsesTexte[question.id].orEmpty(),
                            choixSelectionne = reponsesChoix[question.id],
                            onValeurChangee = { reponsesTexte[question.id] = it; saveState() },
                            onChoixSelectionne = { reponsesChoix[question.id] = it; saveState() },
                            onSuivant = {
                                if (indexQuestion < questionsVisibles.lastIndex) indexQuestion++
                                else screen = AppScreen.Chargement
                                saveState()
                            }
                        )
                    }
                }

                AppScreen.Chargement -> {
                    ChargementAnalyseScreen(
                        modifier = Modifier.padding(padding),
                        onTermine = { screen = AppScreen.Resultat; saveState() }
                    )
                }

                AppScreen.Resultat -> {
                    val analyse = QuestionnaireEngine.calculerResultat(
                        questions, reponsesTexte, reponsesChoix
                    )

                    // Auto-sauvegarde dans l'historique
                    LaunchedEffect(Unit) {
                        HistoriqueManager.sauvegarderBilan(
                            context,
                            reponsesTexte["nom_chien"].orEmpty(),
                            analyse
                        )
                    }

                    val textePartage = construireTextePartageBilan(
                        reponsesTexte["nom_chien"].orEmpty(), analyse
                    )

                    ResultatScreen(
                        modifier = Modifier.padding(padding),
                        nomChien = reponsesTexte["nom_chien"].orEmpty(),
                        analyse = analyse,
                        onShare = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, textePartage)
                            }
                            context.startActivity(Intent.createChooser(intent, "Partager"))
                        },
                        onCopy = {
                            clipboard.setText(AnnotatedString(textePartage))
                            scope.launch { snackbarHostState.showSnackbar("Copié") }
                        },
                        onExportPdf = {
                            val file = PdfExporter.exporterBilanPdf(
                                context = context,
                                nomChien = reponsesTexte["nom_chien"].orEmpty(),
                                analyse = analyse
                            )
                            val uri = FileProvider.getUriForFile(
                                context, "${context.packageName}.fileprovider", file
                            )
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Partager PDF"))
                        },
                        onRecommencer = {
                            reponsesTexte.clear()
                            reponsesChoix.clear()
                            indexQuestion = 0
                            screen = AppScreen.Accueil
                            clearSavedState()
                        }
                    )
                }

                AppScreen.Dictionnaire -> {
                    DictionnaireInfoScreen(
                        modifier = Modifier.padding(padding),
                        onOpenFiche = { ficheId ->
                            screen = AppScreen.DictionnaireDetail(ficheId)
                            saveState()
                        }
                    )
                }

                is AppScreen.DictionnaireDetail -> {
                    val ficheId = (screen as AppScreen.DictionnaireDetail).ficheId
                    DictionnaireDetailScreen(modifier = Modifier.padding(padding), ficheId = ficheId)
                }

                AppScreen.Alimentation -> {
                    DictionnaireScreen(modifier = Modifier.padding(padding))
                }

                AppScreen.Feedback -> {
                    FeedbackScreen(
                        modifier = Modifier.padding(padding),
                        ecranActuel = screenAvantFeedback.toStorageValue(),
                        onEnvoyer = { categorie, ecran, message ->
                            val version = try {
                                context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "?"
                            } catch (e: Exception) { "?" }
                            envoyerFeedbackEmail(categorie, ecran, message, version)
                        },
                        onRetour = { screen = screenAvantFeedback }
                    )
                }

                AppScreen.Historique -> {
                    HistoriqueScreen(
                        modifier = Modifier.padding(padding),
                        bilans = bilans,
                        onOuvrirBilan = { bilanId -> screen = AppScreen.HistoriqueDetail(bilanId) },
                        onSupprimerBilan = { bilanId ->
                            scope.launch { HistoriqueManager.supprimerBilan(context, bilanId) }
                        },
                        onSupprimerTout = {
                            scope.launch { HistoriqueManager.supprimerTout(context) }
                        }
                    )
                }

                is AppScreen.HistoriqueDetail -> {
                    val bilanId = (screen as AppScreen.HistoriqueDetail).bilanId
                    val bilan = bilans.firstOrNull { it.id == bilanId }
                    if (bilan != null) {
                        HistoriqueDetailScreen(
                            modifier = Modifier.padding(padding),
                            bilan = bilan,
                            onSupprimer = {
                                scope.launch {
                                    HistoriqueManager.supprimerBilan(context, bilanId)
                                    screen = AppScreen.Historique
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

fun questionDoitEtreAffichee(question: Question, reponsesChoix: Map<String, Int>): Boolean {
    return when (question.id) {
        "si_non_quand" -> { val r = reponsesChoix["proprete_maison"]; r == 1 || r == 2 }
        else -> true
    }
}

fun construireTextePartageBilan(nomChien: String, analyse: ResultatAnalyse): String {
    val nom = nomChienAffiche(nomChien)
    return """
Bilan pour $nom

Hypothèse :
${analyse.hypothesePrincipale}

Priorité :
${textePrioriteAction(analyse.prioriteAction)}

${analyse.syntheseAvancee}

Scores :
Sensibilité : ${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauPeur)}
Attachement : ${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauAttachement)}
Impulsivité : ${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauImpulsivite)}
Réactivité : ${QuestionnaireEngine.libelleNiveauAxe(analyse.niveauReactivite)}

⚠️ Bilan indicatif
    """.trimIndent()
}