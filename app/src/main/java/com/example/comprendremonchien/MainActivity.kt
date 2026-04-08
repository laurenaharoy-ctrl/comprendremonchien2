package com.example.comprendremonchien2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    data object Questionnaire : AppScreen
    data object Resultat : AppScreen
    data object Dictionnaire : AppScreen
    data class DictionnaireDetail(val ficheId: String) : AppScreen
    data object Alimentation : AppScreen
}

fun AppScreen.toStorageValue(): String = when (this) {
    AppScreen.Accueil -> "accueil"
    AppScreen.Questionnaire -> "questionnaire"
    AppScreen.Resultat -> "resultat"
    AppScreen.Dictionnaire -> "dictionnaire"
    is AppScreen.DictionnaireDetail -> "dictionnaire_detail:${this.ficheId}"
    AppScreen.Alimentation -> "alimentation"
}

fun screenFromStorage(value: String): AppScreen = when {
    value == "questionnaire" -> AppScreen.Questionnaire
    value == "resultat" -> AppScreen.Resultat
    value == "dictionnaire" -> AppScreen.Dictionnaire
    value.startsWith("dictionnaire_detail:") -> {
        val ficheId = value.removePrefix("dictionnaire_detail:")
        if (ficheId.isBlank()) AppScreen.Dictionnaire else AppScreen.DictionnaireDetail(ficheId)
    }
    value == "alimentation" -> AppScreen.Alimentation
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

    val reponsesTexte = remember { mutableStateMapOf<String, String>() }
    val reponsesChoix = remember { mutableStateMapOf<String, Int>() }

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

        val savedScreen = prefs[stringPreferencesKey("screen")] ?: "accueil"
        val savedIndex = prefs[intPreferencesKey("index")] ?: 0
        val texteJson = prefs[stringPreferencesKey("reponsesTexte")]
        val choixJson = prefs[stringPreferencesKey("reponsesChoix")]

        if (!texteJson.isNullOrBlank()) {
            val texteObj = JSONObject(texteJson)
            for (key in texteObj.keys()) {
                reponsesTexte[key] = texteObj.getString(key)
            }
        }

        if (!choixJson.isNullOrBlank()) {
            val choixObj = JSONObject(choixJson)
            for (key in choixObj.keys()) {
                reponsesChoix[key] = choixObj.getInt(key)
            }
        }

        hasSavedProgress = reponsesTexte.isNotEmpty() || reponsesChoix.isNotEmpty()

        if (hasSavedProgress) {
            screen = screenFromStorage(savedScreen)
            indexQuestion = savedIndex
        }

        isLoaded = true
    }

    val questionsVisibles = remember(reponsesChoix.toMap()) {
        questions.filter { questionDoitEtreAffichee(it, reponsesChoix) }
    }

    LaunchedEffect(questionsVisibles.size, isLoaded) {
        if (!isLoaded) return@LaunchedEffect
        if (questionsVisibles.isEmpty()) {
            indexQuestion = 0
        } else if (indexQuestion > questionsVisibles.lastIndex) {
            indexQuestion = questionsVisibles.lastIndex
        }
    }

    BackHandler(enabled = screen != AppScreen.Accueil) {
        when (screen) {
            AppScreen.Questionnaire -> {
                if (indexQuestion > 0) {
                    indexQuestion--
                    saveState()
                } else {
                    screen = AppScreen.Accueil
                    saveState()
                }
            }
            AppScreen.Resultat -> {
                screen = AppScreen.Questionnaire
                saveState()
            }
            AppScreen.Dictionnaire -> {
                screen = AppScreen.Accueil
                saveState()
            }
            is AppScreen.DictionnaireDetail -> {
                screen = AppScreen.Dictionnaire
                saveState()
            }
            AppScreen.Alimentation -> {
                screen = AppScreen.Accueil
                saveState()
            }
            AppScreen.Accueil -> Unit
        }
    }

    AppBackground {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                PremiumTopBar(
                    title = when (screen) {
                        AppScreen.Accueil -> "Comprendre mon chien"
                        AppScreen.Questionnaire -> "Questionnaire"
                        AppScreen.Resultat -> "Résultat"
                        AppScreen.Dictionnaire -> "Dictionnaire comportemental"
                        is AppScreen.DictionnaireDetail -> {
                            val ficheId = (screen as AppScreen.DictionnaireDetail).ficheId
                            val fiche = getComportementEntryById(ficheId)
                            fiche?.titre ?: "Fiche comportementale"
                        }
                        AppScreen.Alimentation -> "Alimentation"
                    },
                    onBack = if (screen != AppScreen.Accueil) {
                        {
                            when (screen) {
                                AppScreen.Questionnaire -> {
                                    if (indexQuestion > 0) {
                                        indexQuestion--
                                    } else {
                                        screen = AppScreen.Accueil
                                    }
                                    saveState()
                                }
                                AppScreen.Resultat -> {
                                    screen = AppScreen.Questionnaire
                                    saveState()
                                }
                                AppScreen.Dictionnaire -> {
                                    screen = AppScreen.Accueil
                                    saveState()
                                }
                                is AppScreen.DictionnaireDetail -> {
                                    screen = AppScreen.Dictionnaire
                                    saveState()
                                }
                                AppScreen.Alimentation -> {
                                    screen = AppScreen.Accueil
                                    saveState()
                                }
                                AppScreen.Accueil -> Unit
                            }
                        }
                    } else null
                )
            }
        ) { padding ->

            if (!isLoaded) {
                ChargementMinimal()
                return@Scaffold
            }

            when (screen) {
                AppScreen.Accueil -> {
                    AccueilScreen(
                        modifier = Modifier.padding(padding),
                        hasSavedProgress = hasSavedProgress,
                        onCommencer = {
                            reponsesTexte.clear()
                            reponsesChoix.clear()
                            indexQuestion = 0
                            screen = AppScreen.Questionnaire
                            clearSavedState()
                            saveState()
                        },
                        onReprendre = {
                            screen = AppScreen.Questionnaire
                            saveState()
                        },
                        onDictionnaire = {
                            screen = AppScreen.Dictionnaire
                            saveState()
                        },
                        onAlimentation = {
                            screen = AppScreen.Alimentation
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
                            onValeurChangee = {
                                reponsesTexte[question.id] = it
                                saveState()
                            },
                            onChoixSelectionne = {
                                reponsesChoix[question.id] = it
                                saveState()
                            },
                            onSuivant = {
                                if (indexQuestion < questionsVisibles.lastIndex) {
                                    indexQuestion++
                                    screen = AppScreen.Questionnaire
                                } else {
                                    screen = AppScreen.Resultat
                                }
                                saveState()
                            }
                        )
                    }
                }

                AppScreen.Resultat -> {
                    val analyse = QuestionnaireEngine.calculerResultat(
                        questions,
                        reponsesTexte,
                        reponsesChoix
                    )

                    val textePartage = construireTextePartageBilan(
                        reponsesTexte["nom_chien"].orEmpty(),
                        analyse
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
                            scope.launch {
                                snackbarHostState.showSnackbar("Copié")
                            }
                        },
                        onExportPdf = {
                            val file = PdfExporter.exporterBilanPdf(
                                context = context,
                                nomChien = reponsesTexte["nom_chien"].orEmpty(),
                                analyse = analyse
                            )

                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
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
                    DictionnaireDetailScreen(
                        modifier = Modifier.padding(padding),
                        ficheId = ficheId
                    )
                }

                AppScreen.Alimentation -> {
                    DictionnaireScreen(
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

fun questionDoitEtreAffichee(
    question: Question,
    reponsesChoix: Map<String, Int>
): Boolean {
    return when (question.id) {
        "si_non_quand" -> {
            val r = reponsesChoix["proprete_maison"]
            r == 1 || r == 2
        }
        else -> true
    }
}

fun construireTextePartageBilan(
    nomChien: String,
    analyse: ResultatAnalyse
): String {
    val nom = nomChienAffiche(nomChien)

    return """
        Bilan pour $nom
        
        Hypothèse :
        ${analyse.hypothesePrincipale}
        
        Priorité :
        ${textePrioriteAction(analyse.prioriteAction)}
        
        ${analyse.syntheseAvancee}
        
        Scores :
        Peur : ${analyse.peur}%
        Attachement : ${analyse.attachement}%
        Impulsivité : ${analyse.impulsivite}%
        Réactivité : ${analyse.reactivite}%
        
        ⚠️ Bilan indicatif
    """.trimIndent()
}