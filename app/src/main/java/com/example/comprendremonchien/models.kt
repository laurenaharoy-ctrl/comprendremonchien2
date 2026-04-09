package com.example.comprendremonchien

enum class Axe { PEUR, ATTACHEMENT, IMPULSIVITE, REACTIVITE }

enum class NiveauVigilance { FAIBLE, MODEREE, ELEVEE }

enum class NiveauSituation { STABLE, A_TRAVAILLER, SENSIBLE }

enum class PrioriteAction {
    FAIBLE,
    MODEREE,
    ELEVEE,
    URGENTE
}

enum class NiveauAxe {
    PEU_MARQUE,
    A_SURVEILLER,
    MARQUE,
    TRES_MARQUE
}

sealed class Question(
    open val id: String,
    open val titre: String
)

data class QuestionTexte(
    override val id: String,
    override val titre: String
) : Question(id, titre)

data class QuestionChoix(
    override val id: String,
    override val titre: String,
    val options: List<String>,
    val axe: Axe? = null,
    val scoreParOption: List<Int>? = null,
    val poids: Int = 1,
    val signalAlerte: Boolean = false,
    val signalCritique: Boolean = false
) : Question(id, titre)

data class ProfilGlobal(
    val titre: String,
    val resume: String,
    val profilType: String,
    val scoreGlobal: Int,
    val phraseHumaine: String
)

data class ContexteAnalyse(
    val temporalite: Int,
    val evolution: Int,
    val frequence: Int,
    val intensite: Int,
    val generalisation: Int,
    val changement: Int,
    val physique: Int,
    val scoreContexte: Int
)

data class PlanAction(
    val aFaire: List<String>,
    val aEviter: List<String>,
    val aObserver: List<String>
)

data class ExplicationResultat(
    val raisonsPrincipales: List<String>,
    val facteursAggravants: List<String>,
    val facteursProtecteurs: List<String>
)

data class PrioriteImmediate(
    val niveau: PrioriteAction,
    val titre: String,
    val message: String,
    val actionsImmediates: List<String>
)

data class ResultatAnalyse(
    val peur: Int,
    val attachement: Int,
    val impulsivite: Int,
    val reactivite: Int,

    val niveauPeur: NiveauAxe,
    val niveauAttachement: NiveauAxe,
    val niveauImpulsivite: NiveauAxe,
    val niveauReactivite: NiveauAxe,

    val profil: ProfilGlobal,
    val vigilance: NiveauVigilance,
    val niveauSituation: NiveauSituation,
    val contexte: ContexteAnalyse,
    val problemePrincipal: Axe,
    val problemesImportants: List<Axe>,

    val explicationPrincipale: String,
    val conseilPrincipal: String,
    val conseilsPratiques: List<String>,
    val planAction: PlanAction,

    val messageSituation: String,
    val raisonSituation: String,
    val messageAide: String?,

    val apparitionBrutale: Boolean,
    val aDejaMordu: Boolean,

    val hypothesePrincipale: String,
    val prioriteAction: PrioriteAction,

    val prioriteImmediate: PrioriteImmediate,
    val explicationResultat: ExplicationResultat,

    val facteursAggravants: List<String>,
    val facteursProtecteurs: List<String>,
    val syntheseAvancee: String,

    // ── NOUVEAU : Race ────────────────────────────────────────────────────────
    val raceCategorie: String? = null,
    val racePrecise: String? = null
)

data class SavedQuestionnaireState(
    val hasInProgress: Boolean = false,
    val ecran: String = "accueil",
    val indexQuestion: Int = 0,
    val reponsesTexte: Map<String, String> = emptyMap(),
    val reponsesChoix: Map<String, Int> = emptyMap()
)

data class QuestionnaireUiState(
    val isReady: Boolean = false,
    val hasSavedProgress: Boolean = false,
    val ecran: AppScreen = AppScreen.Accueil,
    val indexQuestion: Int = 0,
    val reponsesTexte: Map<String, String> = emptyMap(),
    val reponsesChoix: Map<String, Int> = emptyMap()
)

fun niveauPourcentage(pourcentage: Int): String {
    return when {
        pourcentage <= 30 -> "faible"
        pourcentage <= 60 -> "modéré"
        else -> "élevé"
    }
}

fun nomChienAffiche(nom: String): String =
    nom.trim().ifBlank { "votre chien" }

fun texteVigilance(niveau: NiveauVigilance, nomChien: String = ""): String {
    val nom = nomChienAffiche(nomChien)
    return when (niveau) {
        NiveauVigilance.FAIBLE ->
            "À ce stade, rien ne ressort comme particulièrement préoccupant pour $nom."
        NiveauVigilance.MODEREE ->
            "Quelques éléments méritent une attention particulière pour $nom."
        NiveauVigilance.ELEVEE ->
            "Certaines réponses invitent à ne pas laisser la situation s'installer seule pour $nom."
    }
}

fun texteNiveauSituation(niveau: NiveauSituation): String {
    return when (niveau) {
        NiveauSituation.STABLE -> "Stable"
        NiveauSituation.A_TRAVAILLER -> "À travailler"
        NiveauSituation.SENSIBLE -> "Sensible"
    }
}

fun textePrioriteAction(priorite: PrioriteAction): String {
    return when (priorite) {
        PrioriteAction.FAIBLE -> "Faible"
        PrioriteAction.MODEREE -> "Modérée"
        PrioriteAction.ELEVEE -> "Élevée"
        PrioriteAction.URGENTE -> "Urgente"
    }
}

fun libelleAxe(axe: Axe): String = when (axe) {
    Axe.PEUR -> "Sensibilité"
    Axe.ATTACHEMENT -> "Attachement"
    Axe.IMPULSIVITE -> "Impulsivité"
    Axe.REACTIVITE -> "Réactivité"
}

fun resumeEmotionnel(axe: Axe): String = when (axe) {
    Axe.PEUR -> "Sensible et facilement impacté par son environnement"
    Axe.ATTACHEMENT -> "Très attaché, difficile à détacher"
    Axe.IMPULSIVITE -> "Monte vite en excitation"
    Axe.REACTIVITE -> "Réagit rapidement aux stimuli"
}

fun intentionChien(axe: Axe): String = when (axe) {
    Axe.PEUR -> "Il essaie surtout de gérer ce qui lui fait peur."
    Axe.ATTACHEMENT -> "Il cherche à rester en sécurité avec vous."
    Axe.IMPULSIVITE -> "Il tente de gérer son excitation."
    Axe.REACTIVITE -> "Il essaie de répondre à un environnement trop intense."
}

fun besoinPrincipal(axe: Axe): String = when (axe) {
    Axe.PEUR -> "Besoin principal : se sentir en sécurité."
    Axe.ATTACHEMENT -> "Besoin principal : gagner en autonomie."
    Axe.IMPULSIVITE -> "Besoin principal : apprendre à redescendre."
    Axe.REACTIVITE -> "Besoin principal : retrouver du calme."
}

fun phraseFin(nomChien: String = ""): String {
    val nom = nomChienAffiche(nomChien)
    return "Chaque chien est unique. Ce bilan donne des repères pour $nom, mais l'observation du quotidien reste essentielle."
}