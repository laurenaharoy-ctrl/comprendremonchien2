package com.laurena.comprendremonchien

import kotlin.math.roundToInt

// ═══════════════════════════════════════════════════════════
// HELPERS SEXE/STÉRILISATION
// 0 = mâle stérilisé, 1 = femelle stérilisée, 2 = mâle entier, 3 = femelle entière
// ═══════════════════════════════════════════════════════════

fun estSterilise(reponsesChoix: Map<String, Int>): Boolean =
    reponsesChoix["sterilise"] == 0 || reponsesChoix["sterilise"] == 1

fun estMaleEntier(reponsesChoix: Map<String, Int>): Boolean =
    reponsesChoix["sterilise"] == 2

fun estFemelleEntiere(reponsesChoix: Map<String, Int>): Boolean =
    reponsesChoix["sterilise"] == 3

object QuestionnaireEngine {

    fun convertirChoixEnPoints(question: QuestionChoix, indexChoisi: Int): Int {
        val scoreBase = question.scoreParOption?.getOrNull(indexChoisi) ?: when (indexChoisi) {
            0 -> 0; 1 -> 1; 2 -> 2; 3 -> 3; else -> 0
        }
        return scoreBase * question.poids
    }

    fun calculerPourcentageAxe(axe: Axe, questions: List<Question>, reponsesChoix: Map<String, Int>): Int {
        val questionsAxe = questions.filterIsInstance<QuestionChoix>().filter { it.axe == axe }
        if (questionsAxe.isEmpty()) return 0
        val scoreMax = questionsAxe.sumOf { q -> (q.scoreParOption?.maxOrNull() ?: 2) * q.poids }
        val score = questionsAxe.sumOf { q -> convertirChoixEnPoints(q, reponsesChoix[q.id] ?: 0) }
        if (scoreMax == 0) return 0
        return ((score.toFloat() / scoreMax.toFloat()) * 100f).roundToInt()
    }

    fun calculerScoreGlobal(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): Int =
        ((peur + attachement + impulsivite + reactivite) / 4f).roundToInt()

    fun calculerNiveauAxe(score: Int): NiveauAxe = when {
        score <= 29 -> NiveauAxe.PEU_MARQUE
        score <= 54 -> NiveauAxe.A_SURVEILLER
        score <= 74 -> NiveauAxe.MARQUE
        else -> NiveauAxe.TRES_MARQUE
    }

    // Délègue à AppStrings pour la traduction
    fun libelleNiveauAxe(niveau: NiveauAxe): String = strNiveauAxe(niveau)

    fun determinerProblemePrincipal(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): Axe =
        listOf(Axe.PEUR to peur, Axe.ATTACHEMENT to attachement, Axe.IMPULSIVITE to impulsivite, Axe.REACTIVITE to reactivite)
            .maxByOrNull { it.second }!!.first

    fun determinerProfilType(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val top = listOf(Axe.PEUR to peur, Axe.ATTACHEMENT to attachement, Axe.IMPULSIVITE to impulsivite, Axe.REACTIVITE to reactivite)
            .sortedByDescending { it.second }
        val first = top[0].first
        val second = top[1].first
        val firstScore = top[0].second
        if (firstScore <= 30) return if (isEnglish()) "Well-grounded companion" else "Compagnon bien ancré"
        return when {
            first == Axe.PEUR && second == Axe.REACTIVITE -> if (isEnglish()) "Sensitive explorer" else "Explorateur sensible"
            first == Axe.ATTACHEMENT && second == Axe.PEUR -> if (isEnglish()) "Close-at-heart" else "Cœur collé-serré"
            first == Axe.ATTACHEMENT && second == Axe.REACTIVITE -> if (isEnglish()) "Very attached" else "Très attaché"
            first == Axe.IMPULSIVITE && second == Axe.REACTIVITE -> if (isEnglish()) "Bursting with energy" else "Débordant d'énergie"
            first == Axe.IMPULSIVITE && second == Axe.PEUR -> if (isEnglish()) "Lively and sensitive" else "Vif et sensible"
            first == Axe.REACTIVITE -> if (isEnglish()) "Highly reactive dog" else "Chien très réactif"
            first == Axe.PEUR -> if (isEnglish()) "Watchful and emotional" else "Émotif vigilant"
            first == Axe.ATTACHEMENT -> if (isEnglish()) "Fusional" else "Fusionnel"
            first == Axe.IMPULSIVITE -> if (isEnglish()) "Sensitive engine" else "Moteur sensible"
            else -> if (isEnglish()) "Balanced profile" else "Profil équilibré"
        }
    }

    fun phraseHumaineProfil(nomChien: String, scoreGlobal: Int, profilType: String, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        val nom = nomChienAffiche(nomChien)
        return when {
            maxAxe <= 30 -> if (isEnglish())
                "$nom seems to be evolving on an overall stable and well-adapted basis."
            else
                "$nom semble évoluer sur une base globalement stable et adaptée."
            maxAxe <= 60 -> if (isEnglish())
                "$nom shows some fragile points, without them taking over entirely."
            else
                "$nom présente quelques points de fragilité, sans que cela ne prenne toute la place."
            else -> if (isEnglish())
                "$nom seems to be currently struggling in some situations. This reading remains indicative and should be compared with real daily observation."
            else
                "$nom semble actuellement en difficulté dans certaines situations. Cette lecture reste indicative et gagnerait à être confrontée à l'observation réelle de son quotidien."
        }
    }

    fun genererProfilGlobal(nomChien: String, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): ProfilGlobal {
        val scoreGlobal = calculerScoreGlobal(peur, attachement, impulsivite, reactivite)
        val profilType = determinerProfilType(peur, attachement, impulsivite, reactivite)
        val ph = phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite)
        return when {
            peur <= 30 && attachement <= 30 && impulsivite <= 30 && reactivite <= 30 ->
                ProfilGlobal(
                    if (isEnglish()) "Overall balanced profile" else "Profil globalement équilibré",
                    if (isEnglish()) "The responses suggest a fairly stable overall functioning." else "Les réponses suggèrent un fonctionnement plutôt stable dans l'ensemble.",
                    profilType, scoreGlobal, ph)
            peur >= 60 && reactivite >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "Emotional sensitivity and marked reactivity" else "Sensibilité émotionnelle et réactivité marquées",
                    if (isEnglish()) "The profile suggests significant sensitivity, with more visible reactions when certain situations become difficult to manage." else "Le profil suggère une sensibilité importante, avec des réactions plus visibles lorsque certaines situations deviennent difficiles à gérer.",
                    profilType, scoreGlobal, ph)
            attachement >= 60 && peur >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "Need for closeness with emotional fragility" else "Besoin de proximité avec fragilité émotionnelle",
                    if (isEnglish()) "The profile suggests a need for strong relational anchors combined with notable emotional sensitivity." else "Le fonctionnement évoque un besoin de repères relationnels forts associé à une sensibilité émotionnelle notable.",
                    profilType, scoreGlobal, ph)
            attachement >= 60 && reactivite >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "Strong closeness need with intense reactions" else "Proximité importante avec réactions intenses",
                    if (isEnglish()) "The profile seems to combine a need for closeness with more marked reactions in certain contexts." else "Le profil semble associer besoin de proximité et réactions plus marquées dans certains contextes.",
                    profilType, scoreGlobal, ph)
            impulsivite >= 60 && reactivite >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "Fast reactions with difficulty in control" else "Réactions rapides avec difficulté de contrôle",
                    if (isEnglish()) "The profile suggests rapid emotional escalation, with more difficult management of certain stimulations." else "Le profil suggère des montées émotionnelles rapides, avec une gestion plus difficile de certaines stimulations.",
                    profilType, scoreGlobal, ph)
            impulsivite >= 60 && peur >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "Sensitivity with difficult regulation" else "Sensibilité avec régulation difficile",
                    if (isEnglish()) "The profile suggests both emotional sensitivity and difficulty returning quickly to balance." else "Le fonctionnement évoque à la fois une sensibilité émotionnelle et une difficulté à retrouver rapidement l'équilibre.",
                    profilType, scoreGlobal, ph)
            reactivite >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "More marked reactivity" else "Réactivité plus marquée",
                    if (isEnglish()) "The profile suggests a tendency to react strongly to certain environmental elements." else "Le profil suggère une tendance à réagir fortement à certains éléments de l'environnement.",
                    profilType, scoreGlobal, ph)
            attachement >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "Greater need for closeness" else "Besoin de proximité plus important",
                    if (isEnglish()) "The responses highlight a more marked need for closeness than average." else "Les réponses font ressortir un besoin de proximité plus marqué que la moyenne.",
                    profilType, scoreGlobal, ph)
            impulsivite >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "More difficult regulation" else "Régulation plus difficile",
                    if (isEnglish()) "The profile suggests difficulty managing excitement and returning to calm." else "Le profil évoque une difficulté dans la gestion de l'excitation et des retours au calme.",
                    profilType, scoreGlobal, ph)
            peur >= 60 ->
                ProfilGlobal(
                    if (isEnglish()) "More marked emotional sensitivity" else "Sensibilité émotionnelle plus marquée",
                    if (isEnglish()) "The responses suggest greater sensitivity to certain changes or situations." else "Les réponses suggèrent une sensibilité plus importante à certains changements ou situations.",
                    profilType, scoreGlobal, ph)
            else ->
                ProfilGlobal(
                    if (isEnglish()) "Profile to be nuanced" else "Profil à nuancer",
                    if (isEnglish()) "The responses reveal some points of vigilance, without any single aspect clearly dominating." else "Les réponses font apparaître quelques points de vigilance, sans qu'un aspect ne domine clairement.",
                    profilType, scoreGlobal, ph)
        }
    }

    fun calculerContexte(reponsesChoix: Map<String, Int>): ContexteAnalyse {
        val temporalite = when (reponsesChoix["duree_probleme"]) { 0 -> 2; 1 -> 1; 2 -> 0; 3 -> 0; else -> 0 }
        val evolution = when (reponsesChoix["evolution_probleme"]) { 0 -> 0; 1 -> 1; 2 -> 3; else -> 0 }
        val frequence = when (reponsesChoix["frequence_probleme"]) { 0 -> 0; 1 -> 1; 2 -> 2; 3 -> 3; else -> 0 }
        val intensite = when (reponsesChoix["intensite_probleme"]) { 0 -> 0; 1 -> 1; 2 -> 3; 3 -> 4; else -> 0 }
        val generalisation = when (reponsesChoix["generalisation_probleme"]) { 0 -> 0; 1 -> 1; 2 -> 2; else -> 0 }
        val changement = when (reponsesChoix["changement_recent"]) { 0 -> 0; 1 -> 1; 2 -> 3; else -> 0 }
        val physique = when (reponsesChoix["signe_physique"]) { 0 -> 0; 1 -> 2; 2 -> 4; 3 -> 4; else -> 0 }
        val scoreContexte = temporalite + evolution + frequence + intensite + generalisation + changement + physique
        return ContexteAnalyse(temporalite, evolution, frequence, intensite, generalisation, changement, physique, scoreContexte)
    }

    fun calculerNiveauVigilance(questions: List<Question>, reponsesChoix: Map<String, Int>,
                                peur: Int, attachement: Int, impulsivite: Int, reactivite: Int, contexte: ContexteAnalyse): NiveauVigilance {
        val questionsChoix = questions.filterIsInstance<QuestionChoix>()
        val critiqueDetecte = questionsChoix.any { q -> q.signalCritique && (reponsesChoix[q.id] ?: 0) > 0 }
        val nbAlertes = questionsChoix.count { q -> q.signalAlerte && (reponsesChoix[q.id] ?: 0) >= 2 }
        val scoreMax = maxOf(peur, attachement, impulsivite, reactivite)
        return when {
            critiqueDetecte -> NiveauVigilance.ELEVEE
            contexte.physique >= 4 -> NiveauVigilance.ELEVEE
            reponsesChoix["apparition"] == 1 && scoreMax >= 50 -> NiveauVigilance.ELEVEE
            contexte.scoreContexte >= 10 -> NiveauVigilance.ELEVEE
            nbAlertes >= 2 -> NiveauVigilance.MODEREE
            scoreMax >= 70 -> NiveauVigilance.MODEREE
            contexte.scoreContexte >= 6 -> NiveauVigilance.MODEREE
            estMaleEntier(reponsesChoix) && reactivite >= 50 -> NiveauVigilance.MODEREE
            estFemelleEntiere(reponsesChoix) && (peur >= 50 || impulsivite >= 50) -> NiveauVigilance.MODEREE
            else -> NiveauVigilance.FAIBLE
        }
    }

    fun calculerNiveauSituation(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse,
                                peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): NiveauSituation {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        return when {
            contexte.physique >= 4 -> NiveauSituation.SENSIBLE
            reponsesChoix["a_deja_mordu"] == 1 -> NiveauSituation.SENSIBLE
            reponsesChoix["evolution_probleme"] == 2 && (reponsesChoix["intensite_probleme"] == 3 || reponsesChoix["duree_probleme"] == 0) -> NiveauSituation.SENSIBLE
            contexte.scoreContexte >= 10 -> NiveauSituation.SENSIBLE
            maxAxe >= 75 && contexte.scoreContexte >= 6 -> NiveauSituation.SENSIBLE
            contexte.scoreContexte >= 5 -> NiveauSituation.A_TRAVAILLER
            maxAxe >= 55 -> NiveauSituation.A_TRAVAILLER
            else -> NiveauSituation.STABLE
        }
    }

    fun genererMessageSituation(niveauSituation: NiveauSituation, nomChien: String): String {
        val nom = nomChienAffiche(nomChien)
        return when (niveauSituation) {
            NiveauSituation.STABLE -> if (isEnglish())
                "At this stage, the situation seems fairly stable for $nom."
            else
                "À ce stade, la situation semble plutôt stable pour $nom."
            NiveauSituation.A_TRAVAILLER -> if (isEnglish())
                "The situation probably deserves to be worked on progressively for $nom."
            else
                "La situation mérite probablement d'être travaillée de manière progressive pour $nom."
            NiveauSituation.SENSIBLE -> if (isEnglish())
                "The situation seems more sensitive for $nom and warrants particular attention."
            else
                "La situation paraît plus sensible pour $nom et justifie une attention particulière."
        }
    }

    fun genererRaisonSituation(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse): String {
        val raisons = mutableListOf<String>()
        if (reponsesChoix["duree_probleme"] == 0) raisons += if (isEnglish())
            "The very recent nature of the behaviour calls for particular vigilance."
        else
            "Le caractère très récent du comportement invite à une vigilance particulière."
        if (reponsesChoix["evolution_probleme"] == 2) raisons += if (isEnglish())
            "The fact that this seems to be getting worse may indicate the problem is taking more space."
        else
            "Le fait que cela semble s'aggraver peut indiquer que le problème prend plus de place."
        if (contexte.physique >= 4) raisons += if (isEnglish())
            "Physical signs or a possible discomfort call for caution."
        else
            "Des signes physiques ou une gêne possible invitent à la prudence."
        return raisons.firstOrNull() ?: if (isEnglish())
            "Overall, the responses suggest moving forward gradually."
        else
            "L'ensemble des réponses invite surtout à avancer progressivement."
    }

    fun genererConseilsPratiquesPersonnalises(nomChien: String, reponsesChoix: Map<String, Int>,
                                              peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): List<String> {
        val scoreMax = maxOf(peur, attachement, impulsivite, reactivite)
        if (scoreMax == 0) return listOf(
            if (isEnglish()) "Continue daily observation and maintain the routines already in place."
            else "Continuer l'observation du quotidien et maintenir les repères déjà en place."
        )
        val conseils = mutableListOf<String>()
        if (estMaleEntier(reponsesChoix) && reactivite >= 50) conseils += if (isEnglish())
            "In an intact male, reactivity can be amplified by hormones. A vet's opinion on castration may be worth discussing."
        else
            "Chez un mâle entier, la réactivité peut être amplifiée par les hormones. Un avis vétérinaire sur la castration peut valoir la peine d'être discuté."
        if (estFemelleEntiere(reponsesChoix) && (peur >= 50 || impulsivite >= 50)) conseils += if (isEnglish())
            "In an intact female, some behaviours can vary with the cycle. Observing whether behaviours intensify at certain times can give useful landmarks."
        else
            "Chez une femelle entière, certains comportements peuvent varier selon le cycle. Observer si les comportements s'intensifient à certaines périodes peut donner des repères utiles."
        val scores = listOf(Axe.PEUR to peur, Axe.ATTACHEMENT to attachement, Axe.IMPULSIVITE to impulsivite, Axe.REACTIVITE to reactivite)
        val axesDominants = scores.filter { it.second == scoreMax }.map { it.first }
        if (axesDominants.size > 1) {
            val texteAxes = axesDominants.joinToString(if (isEnglish()) " and " else " et ") {
                when (it) {
                    Axe.PEUR -> if (isEnglish()) "emotional sensitivity" else "la sensibilité émotionnelle"
                    Axe.ATTACHEMENT -> if (isEnglish()) "need for closeness" else "le besoin de proximité"
                    Axe.IMPULSIVITE -> if (isEnglish()) "excitement regulation" else "la régulation de l'excitation"
                    Axe.REACTIVITE -> if (isEnglish()) "reactivity to the environment" else "la réactivité à l'environnement"
                }
            }
            conseils += if (isEnglish())
                "Several dimensions seem to stand out together: $texteAxes. A gradual approach, one axis at a time, seems preferable."
            else
                "Plusieurs dimensions semblent ressortir conjointement : $texteAxes. Une approche progressive, un axe après l'autre, paraît préférable."
        }
        return conseils.distinct().take(4)
    }

    fun determinerProblemesImportants(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): List<Axe> {
        return mutableListOf<Axe>().apply {
            if (peur >= 70) add(Axe.PEUR)
            if (attachement >= 70) add(Axe.ATTACHEMENT)
            if (impulsivite >= 70) add(Axe.IMPULSIVITE)
            if (reactivite >= 70) add(Axe.REACTIVITE)
        }
    }

    fun explicationProbleme(axe: Axe, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        if (maxAxe <= 30) return if (isEnglish())
            "The information collected does not highlight any marked behavioural difficulty at this stage."
        else
            "Les éléments recueillis ne mettent pas en évidence de difficulté comportementale marquée à ce stade."
        return when (axe) {
            Axe.PEUR -> if (isEnglish())
                "The observed reactions seem to fit within a relatively high emotional sensitivity. In this type of profile, certain changes or situations may be perceived as more intense or difficult to manage."
            else
                "Les réactions observées semblent s'inscrire dans une sensibilité émotionnelle relativement élevée. Dans ce type de fonctionnement, certains changements ou situations peuvent être perçus comme plus intenses ou difficiles à gérer."
            Axe.ATTACHEMENT -> if (isEnglish())
                "The information collected suggests a relatively strong need for closeness. In this type of profile, emotional independence can still be fragile, which may make certain separations or absences harder to cope with."
            else
                "Les éléments recueillis suggèrent un besoin de proximité relativement important. Dans ce type de fonctionnement, l'autonomie émotionnelle peut être encore fragile, ce qui peut rendre certaines séparations ou absences plus difficiles à vivre."
            Axe.IMPULSIVITE -> if (isEnglish())
                "The responses suggest a possible difficulty in regulating excitement. This is generally not a lack of willpower, but rather a quickly-reached emotional activation threshold, with a slower return to calm."
            else
                "Les réponses évoquent une difficulté possible dans la régulation de l'excitation. Il ne s'agit généralement pas d'un manque de volonté, mais plutôt d'un seuil de montée émotionnelle rapidement atteint, avec un retour au calme plus lent."
            Axe.REACTIVITE -> if (isEnglish())
                "The information collected suggests marked reactivity to certain elements of the environment. This type of response may appear when the dog feels tense, uncertain or overwhelmed in certain situations."
            else
                "Les éléments recueillis suggèrent une réactivité marquée face à certains éléments de son environnement. Ce type de réponse peut apparaître lorsque le chien se sent en tension, incertain ou dépassé dans certaines situations."
        }
    }

    fun conseilPrincipal(axe: Axe, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        if (maxAxe <= 30) return if (isEnglish())
            "At this stage, no priority axis of work stands out clearly. The goal can simply be to maintain a stable, consistent and predictable framework."
        else
            "À ce stade, aucun axe de travail prioritaire ne se dégage clairement. L'objectif peut simplement être de maintenir un cadre stable, cohérent et prévisible."
        return when (axe) {
            Axe.ATTACHEMENT -> if (isEnglish())
                "A first step is to gradually work on moments of separation, keeping them very short and controlled. The aim is to strengthen the dog's ability to stay calm without constantly depending on human presence."
            else
                "Une première piste consiste à travailler progressivement les moments de séparation, en restant sur des durées très courtes et maîtrisées. L'objectif est de renforcer la capacité du chien à rester apaisé sans dépendre constamment de la présence humaine."
            Axe.PEUR -> if (isEnglish())
                "It is generally relevant to respect the dog's tolerance thresholds. Working at a distance from triggering elements, in calm conditions, often helps to promote gradual progress."
            else
                "Il est généralement pertinent de respecter les seuils de tolérance du chien. Travailler à distance des éléments déclencheurs, dans des conditions calmes, permet souvent de favoriser une évolution progressive."
            Axe.REACTIVITE -> if (isEnglish())
                "A gradual approach based on distance management and reducing environmental pressure is often recommended. The goal is to keep the dog in a zone where it can still process information."
            else
                "Une approche progressive basée sur la gestion de la distance et la réduction de la pression environnementale est souvent recommandée. L'objectif est de maintenir le chien dans une zone où il reste encore capable de traiter l'information."
            Axe.IMPULSIVITE -> if (isEnglish())
                "Structuring interactions with short sequences and regular pauses can help improve regulation. The work mainly consists of encouraging frequent and predictable returns to calm."
            else
                "Structurer les interactions avec des temps courts et des pauses régulières peut aider à améliorer la régulation. Le travail consiste surtout à favoriser des retours au calme fréquents et prévisibles."
        }
    }

    fun genererPlanAction(axe: Axe, reponsesChoix: Map<String, Int>, nomChien: String): PlanAction {
        val aFaire = mutableListOf<String>()
        val aEviter = mutableListOf<String>()
        val aObserver = mutableListOf<String>()
        when (axe) {
            Axe.ATTACHEMENT -> {
                aFaire += if (isEnglish()) "Reduce the emotional load around departures and returns." else "Réduire la charge émotionnelle autour des départs et des retours."
                aFaire += if (isEnglish()) "Gradually introduce small moments of independence in easy situations." else "Proposer progressivement de petits moments d'autonomie dans des situations faciles."
                aFaire += if (isEnglish()) "Start with very short, controlled absences." else "Commencer par des absences très courtes et maîtrisées."
                aEviter += if (isEnglish()) "Highly marked departure or reunion rituals." else "Les rituels de départ ou de retrouvailles très marqués."
                aEviter += if (isEnglish()) "Absences that are too long or too difficult from the start." else "Les absences trop longues ou trop difficiles d'emblée."
                aEviter += if (isEnglish()) "Emotional reactions to absence-related behaviour." else "Les réactions émotionnelles face aux manifestations liées à l'absence."
                aObserver += if (isEnglish()) "The exact moment when tension appears." else "Le moment précis où la tension apparaît."
                aObserver += if (isEnglish()) "The dog's ability to settle alone during neutral moments." else "La capacité du chien à se poser seul dans les moments neutres."
                aObserver += if (isEnglish()) "Progress when interactions become more predictable." else "L'évolution lorsque les interactions deviennent plus prévisibles."
            }
            Axe.PEUR -> {
                aFaire += if (isEnglish()) "Work at enough distance for the dog to remain calm." else "Travailler à distance suffisante pour que le chien reste encore calme."
                aFaire += if (isEnglish()) "Let the dog observe without forcing it." else "Laisser le chien observer sans le contraindre."
                aFaire += if (isEnglish()) "Create positive experiences in controlled conditions." else "Créer des expériences positives dans des contextes maîtrisés."
                aEviter += if (isEnglish()) "Forcing the dog to face what worries it." else "Forcer le chien à affronter ce qui l'inquiète."
                aEviter += if (isEnglish()) "Reducing the distance too quickly." else "Réduire trop vite la distance."
                aEviter += if (isEnglish()) "Keeping the dog in a situation where it is already struggling." else "Maintenir le chien dans une situation où il est déjà en difficulté."
                aObserver += if (isEnglish()) "The distance at which tension appears." else "La distance à laquelle la tension apparaît."
                aObserver += if (isEnglish()) "Early stress signals." else "Les signaux précoces de stress."
                aObserver += if (isEnglish()) "The contexts in which the dog remains comfortable." else "Les contextes dans lesquels il reste à l'aise."
            }
            Axe.IMPULSIVITE -> {
                aFaire += if (isEnglish()) "Structure interactions with short sequences and pauses." else "Structurer les interactions avec des séquences courtes et des pauses."
                aFaire += if (isEnglish()) "Calmly interrupt situations where excitement rises too high." else "Interrompre calmement les situations où l'excitation monte trop."
                aFaire += if (isEnglish()) "Reward calm moments more." else "Valoriser davantage les moments de calme."
                aEviter += if (isEnglish()) "Interactions that are too long or too stimulating." else "Les interactions trop longues ou trop stimulantes."
                aEviter += if (isEnglish()) "Responding to excitement with more excitement." else "Répondre à l'excitation par plus d'excitation."
                aEviter += if (isEnglish()) "Waiting for complete overflowing before acting." else "Attendre le débordement complet avant d'agir."
                aObserver += if (isEnglish()) "The speed at which excitement escalates." else "La rapidité de montée en excitation."
                aObserver += if (isEnglish()) "The time needed to return to calm." else "Le temps nécessaire pour retrouver le calme."
                aObserver += if (isEnglish()) "The situations that trigger overflowing most quickly." else "Les situations qui déclenchent le plus vite les débordements."
            }
            Axe.REACTIVITE -> {
                aFaire += if (isEnglish()) "Increase distance from triggers to stay in a manageable zone." else "Augmenter la distance avec les déclencheurs pour rester dans une zone gérable."
                aFaire += if (isEnglish()) "Choose easier environments." else "Choisir des environnements plus faciles."
                aFaire += if (isEnglish()) "Work in situations where the dog can still observe without reacting." else "Travailler dans des situations où le chien peut encore observer sans réagir."
                aEviter += if (isEnglish()) "Direct or too-close confrontations." else "Les confrontations directes ou trop rapprochées."
                aEviter += if (isEnglish()) "Situations the dog cannot manage." else "Les situations que le chien ne peut pas gérer."
                aEviter += if (isEnglish()) "Insisting once the reaction has started." else "Insister une fois la réaction enclenchée."
                aObserver += if (isEnglish()) "The specific triggers and their intensity." else "Les déclencheurs précis et leur intensité."
                aObserver += if (isEnglish()) "The distance at which the dog tips over." else "La distance à laquelle le chien bascule."
                aObserver += if (isEnglish()) "Warning signals just before the reaction." else "Les signaux annonciateurs juste avant la réaction."
            }
        }
        if (reponsesChoix["signe_physique"] == 2 || reponsesChoix["signe_physique"] == 3)
            aFaire += if (isEnglish())
                "Plan a vet visit to rule out an associated physical cause."
            else
                "Prévoir un avis vétérinaire pour écarter une cause physique associée."
        return PlanAction(aFaire.distinct().take(3), aEviter.distinct().take(3), aObserver.distinct().take(3))
    }

    fun genererMessageAide(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, niveauSituation: NiveauSituation, nomChien: String): String? {
        val nom = nomChienAffiche(nomChien)
        return when {
            reponsesChoix["a_deja_mordu"] == 1 -> if (isEnglish())
                "The fact that there has already been a bite means this situation should not be handled alone. Individual professional support is recommended for $nom."
            else
                "Le fait qu'il y ait déjà eu morsure justifie de ne pas rester seul avec cette situation. Un accompagnement professionnel individualisé est recommandé pour $nom."
            contexte.physique >= 4 -> if (isEnglish())
                "Some elements suggest that discomfort, pain or a physical component may be contributing to the problem. A vet's opinion is recommended for $nom."
            else
                "Certains éléments font penser qu'une gêne, une douleur ou une composante physique pourrait participer au problème. Un avis vétérinaire est recommandé pour $nom."
            reponsesChoix["apparition"] == 1 -> if (isEnglish())
                "When behaviours appear suddenly, it is wise to first rule out a medical cause. A vet check-up may be useful for $nom."
            else
                "Lorsque des comportements apparaissent brutalement, il est prudent d'écarter d'abord une cause médicale. Un point vétérinaire peut être utile pour $nom."
            niveauSituation == NiveauSituation.SENSIBLE -> if (isEnglish())
                "Based on the responses, the situation warrants a professional's view to prevent it from becoming entrenched or worsening."
            else
                "Au vu des réponses, la situation mérite un regard professionnel afin d'éviter qu'elle ne se fixe ou ne s'aggrave."
            else -> null
        }
    }

    fun detecterFacteursAggravants(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): List<String> {
        val facteurs = mutableListOf<String>()
        if (reponsesChoix["apparition"] == 1) facteurs += if (isEnglish()) "Sudden onset" else "Apparition brutale"
        if (reponsesChoix["evolution_probleme"] == 2) facteurs += if (isEnglish()) "Worsening behaviour" else "Comportement en aggravation"
        if (reponsesChoix["intensite_probleme"] == 3) facteurs += if (isEnglish()) "Very high intensity" else "Intensité très forte"
        if (reponsesChoix["generalisation_probleme"] == 2) facteurs += if (isEnglish()) "Present in many situations" else "Présence dans de nombreuses situations"
        if (contexte.physique >= 4) facteurs += if (isEnglish()) "Suspected discomfort or physical cause" else "Suspicion de gêne ou cause physique"
        if (maxOf(peur, attachement, impulsivite, reactivite) >= 75) facteurs += if (isEnglish()) "High level on at least one axis" else "Niveau élevé sur au moins un axe"
        if (estMaleEntier(reponsesChoix) && reactivite >= 50) facteurs += if (isEnglish()) "Intact male with marked reactivity" else "Mâle entier avec réactivité marquée"
        return facteurs.distinct()
    }

    fun detecterFacteursProtecteurs(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse): List<String> {
        val facteurs = mutableListOf<String>()
        if (reponsesChoix["evolution_probleme"] == 0) facteurs += if (isEnglish()) "An improvement already seems present" else "Une amélioration semble déjà présente"
        if (reponsesChoix["frequence_probleme"] == 0) facteurs += if (isEnglish()) "The behaviour remains infrequent" else "Le comportement reste peu fréquent"
        if (reponsesChoix["intensite_probleme"] == 0 || reponsesChoix["intensite_probleme"] == 1) facteurs += if (isEnglish()) "Intensity remains contained" else "L'intensité reste encore contenue"
        if (reponsesChoix["generalisation_probleme"] == 0) facteurs += if (isEnglish()) "The problem seems limited to specific contexts" else "Le problème semble limité à des contextes précis"
        if (contexte.scoreContexte <= 3) facteurs += if (isEnglish()) "The overall context does not suggest a heavily degraded situation" else "Le contexte global ne suggère pas une situation fortement dégradée"
        if (estSterilise(reponsesChoix)) facteurs += if (isEnglish()) "Neutered dog — possible stabilising factor" else "Chien stérilisé — facteur stabilisant possible"
        return facteurs.distinct()
    }

    fun detecterHypothesePrincipale(reponsesChoix: Map<String, Int>, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int, contexte: ContexteAnalyse): String {
        return when {
            contexte.physique >= 4 -> if (isEnglish())
                "The information collected suggests first ruling out a physical component before going further in behavioural interpretation."
            else
                "Les éléments recueillis invitent d'abord à écarter une composante physique avant d'aller plus loin dans l'interprétation comportementale."
            attachement >= 60 && (reponsesChoix["support_absences"] == 2 || reponsesChoix["pendant_absence"] == 2) -> if (isEnglish())
                "The responses may suggest a difficulty around managing separation and absence."
            else
                "Les réponses peuvent évoquer une difficulté autour de la gestion de la séparation et de l'absence."
            peur >= 60 && reactivite >= 60 -> if (isEnglish())
                "The information collected suggests emotional sensitivity combined with marked reactions to the environment."
            else
                "Les éléments recueillis suggèrent une sensibilité émotionnelle associée à des réactions marquées face à l'environnement."
            impulsivite >= 60 && (reponsesChoix["jeu_comportement"] == 2 || reponsesChoix["calmer_apres_excitation"] == 2) -> if (isEnglish())
                "The responses point towards a possible difficulty in emotional regulation, with rapid escalations in excitement."
            else
                "Les réponses orientent vers une difficulté possible dans la régulation émotionnelle, avec des montées en excitation rapides."
            reactivite >= 60 && reponsesChoix["reaction_chiens"] == 2 -> if (isEnglish())
                "The responses may suggest significant reactivity in interactions with other dogs."
            else
                "Les réponses peuvent évoquer une réactivité importante dans les interactions avec les autres chiens."
            reactivite >= 60 && reponsesChoix["reaction_inconnus"] == 2 -> if (isEnglish())
                "The responses may suggest significant reactivity towards unknown people."
            else
                "Les réponses peuvent évoquer une réactivité importante face aux personnes inconnues."
            peur >= 60 -> if (isEnglish())
                "The responses suggest significant emotional sensitivity. Some environments or situations may be perceived as more difficult to tolerate."
            else
                "Les réponses suggèrent une sensibilité émotionnelle importante. Certains environnements ou situations peuvent être perçus comme plus difficiles à tolérer."
            attachement >= 60 -> if (isEnglish())
                "The profile mainly suggests a strong need for closeness, with emotional independence that still seems fragile in some situations."
            else
                "Le profil suggère surtout un besoin de proximité important, avec une autonomie émotionnelle qui paraît encore fragile dans certaines situations."
            impulsivite >= 60 -> if (isEnglish())
                "The responses point towards a possible difficulty in emotional regulation, with rapid excitement escalations."
            else
                "Les réponses orientent vers une difficulté possible dans la régulation émotionnelle, avec des montées rapides en excitation."
            reactivite >= 60 -> if (isEnglish())
                "The information collected may correspond to increased reactivity to certain elements of the environment."
            else
                "Les éléments recueillis peuvent correspondre à une réactivité accrue face à certains éléments de son environnement."
            else -> if (isEnglish())
                "No dominant hypothesis stands out clearly from the responses. Several factors may be involved."
            else
                "Aucune hypothèse dominante ne se dégage clairement à partir des réponses. Plusieurs facteurs peuvent être impliqués."
        }
    }

    fun determinerPrioriteAction(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): PrioriteAction {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        return when {
            reponsesChoix["a_deja_mordu"] == 1 -> PrioriteAction.URGENTE
            contexte.physique >= 4 -> PrioriteAction.URGENTE
            reponsesChoix["apparition"] == 1 && reponsesChoix["intensite_probleme"] == 3 -> PrioriteAction.URGENTE
            reponsesChoix["evolution_probleme"] == 2 && (reponsesChoix["generalisation_probleme"] == 2 || reponsesChoix["intensite_probleme"] == 3) -> PrioriteAction.ELEVEE
            contexte.scoreContexte >= 10 -> PrioriteAction.ELEVEE
            maxAxe >= 75 -> PrioriteAction.ELEVEE
            contexte.scoreContexte >= 5 -> PrioriteAction.MODEREE
            maxAxe >= 55 -> PrioriteAction.MODEREE
            else -> PrioriteAction.FAIBLE
        }
    }

    fun construirePrioriteImmediate(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, priorite: PrioriteAction, niveauSituation: NiveauSituation, nomChien: String): PrioriteImmediate {
        val nom = nomChienAffiche(nomChien)
        return when {
            reponsesChoix["a_deja_mordu"] == 1 -> PrioriteImmediate(PrioriteAction.URGENTE,
                if (isEnglish()) "Immediate priority: secure and get support" else "Priorité immédiate : sécuriser et se faire accompagner",
                if (isEnglish()) "As there has already been a bite, the situation should not be minimised for $nom." else "Comme il y a déjà eu morsure, la situation ne doit pas être banalisée pour $nom.",
                if (isEnglish()) listOf("Avoid risky situations.", "Seek professional behaviour support quickly.") else listOf("Éviter les situations à risque.", "Demander rapidement l'aide d'un professionnel du comportement."))
            contexte.physique >= 4 -> PrioriteImmediate(PrioriteAction.URGENTE,
                if (isEnglish()) "Immediate priority: rule out a physical cause" else "Priorité immédiate : écarter une cause physique",
                if (isEnglish()) "Physical signs have been reported for $nom." else "Des signes physiques sont signalés chez $nom.",
                if (isEnglish()) listOf("Get a vet's opinion quickly.", "Avoid difficult demands in the meantime.") else listOf("Prendre un avis vétérinaire rapidement.", "Éviter les sollicitations difficiles en attendant."))
            priorite == PrioriteAction.ELEVEE || niveauSituation == NiveauSituation.SENSIBLE -> PrioriteImmediate(PrioriteAction.ELEVEE,
                if (isEnglish()) "Immediate priority: act without delay" else "Priorité immédiate : agir sans tarder",
                if (isEnglish()) "The situation seems marked enough to warrant quick action for $nom." else "La situation semble suffisamment marquée pour justifier une action rapide pour $nom.",
                if (isEnglish()) listOf("Lighten the most difficult contexts.", "Consider professional support.") else listOf("Alléger les contextes les plus difficiles.", "Envisager un accompagnement professionnel."))
            priorite == PrioriteAction.MODEREE -> PrioriteImmediate(PrioriteAction.MODEREE,
                if (isEnglish()) "Immediate priority: move forward gradually" else "Priorité immédiate : avancer progressivement",
                if (isEnglish()) "The situation deserves to be taken seriously for $nom." else "La situation mérite d'être prise au sérieux pour $nom.",
                if (isEnglish()) listOf("Start progressive work on difficult situations.", "Observe frequency and context for a few days.") else listOf("Commencer un travail progressif sur les situations difficiles.", "Observer fréquence et contexte pendant quelques jours."))
            else -> PrioriteImmediate(PrioriteAction.FAIBLE,
                if (isEnglish()) "Immediate priority: watch calmly" else "Priorité immédiate : surveiller calmement",
                if (isEnglish()) "Nothing stands out as urgent at this stage for $nom." else "Rien ne ressort comme urgent à ce stade pour $nom.",
                if (isEnglish()) listOf("Continue daily observation.", "Maintain a stable and predictable framework.") else listOf("Continuer l'observation du quotidien.", "Maintenir un cadre stable et prévisible."))
        }
    }

    fun construireExplicationResultat(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): ExplicationResultat {
        val raisons = mutableListOf<String>()
        if (reponsesChoix["evolution_probleme"] == 2) raisons += if (isEnglish()) "The behaviour seems to be getting worse." else "Le comportement semble s'aggraver."
        if (reponsesChoix["frequence_probleme"] == 2 || reponsesChoix["frequence_probleme"] == 3) raisons += if (isEnglish()) "The behaviour seems to recur frequently." else "Le comportement paraît revenir fréquemment."
        if (reponsesChoix["intensite_probleme"] == 2 || reponsesChoix["intensite_probleme"] == 3) raisons += if (isEnglish()) "The described intensity appears significant." else "L'intensité décrite paraît importante."
        if (raisons.isEmpty()) raisons += if (isEnglish()) "The responses suggest mainly a few points of vigilance." else "Les réponses suggèrent surtout quelques points de vigilance."
        return ExplicationResultat(raisons.take(3),
            detecterFacteursAggravants(reponsesChoix, contexte, peur, attachement, impulsivite, reactivite),
            detecterFacteursProtecteurs(reponsesChoix, contexte))
    }

    fun genererSyntheseAvancee(nom: String, hypothese: String, priorite: PrioriteAction, aggravants: List<String>, protecteurs: List<String>): String {
        val intro = when (priorite) {
            PrioriteAction.FAIBLE -> if (isEnglish()) "$nom shows an overall stable profile with a few points of vigilance." else "$nom présente un fonctionnement globalement stable avec quelques points de vigilance."
            PrioriteAction.MODEREE -> if (isEnglish()) "$nom shows a real difficulty that deserves a gradual approach." else "$nom présente une difficulté réelle qui mérite une approche progressive."
            PrioriteAction.ELEVEE -> if (isEnglish()) "$nom currently seems to be struggling sufficiently to require active attention." else "$nom semble actuellement en difficulté sur un plan suffisamment marqué pour nécessiter une attention active."
            PrioriteAction.URGENTE -> if (isEnglish()) "$nom shows elements that warrant prompt attention." else "$nom présente des éléments qui justifient une attention rapide."
        }
        val hypotheseLabel = if (isEnglish()) "Reading hypothesis: $hypothese" else "Hypothèse de lecture : $hypothese"
        val aggr = if (aggravants.isNotEmpty())
            if (isEnglish()) "Elements possibly worsening the situation: ${aggravants.joinToString(", ")}."
            else "Les éléments qui majorent possiblement la situation sont : ${aggravants.joinToString(", ")}."
        else ""
        val prot = if (protecteurs.isNotEmpty())
            if (isEnglish()) "Elements currently rather favourable: ${protecteurs.joinToString(", ")}."
            else "Les éléments plutôt favorables à ce stade sont : ${protecteurs.joinToString(", ")}."
        else ""
        return listOf(intro, hypotheseLabel, aggr, prot).filter { it.isNotBlank() }.joinToString("\n\n")
    }

    fun genererOriginesPossibles(
        nomChien: String, axe: Axe,
        peur: Int, attachement: Int, impulsivite: Int, reactivite: Int,
        reponsesChoix: Map<String, Int>
    ): String {
        val nom = nomChienAffiche(nomChien)
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        if (maxAxe <= 30) return if (isEnglish())
            "$nom seems to be evolving in an overall satisfying balance. No particular behavioural origin stands out at this stage."
        else
            "$nom semble évoluer dans un équilibre global satisfaisant. Aucune origine comportementale particulière ne ressort à ce stade."

        return when (axe) {
            Axe.PEUR -> buildString {
                if (isEnglish()) {
                    append("$nom's emotional sensitivity may have several origins. ")
                    append("Limited early socialisation — few varied exposures during the first weeks of life — is often involved. ")
                    append("Past negative experiences, even isolated ones, can also leave a lasting mark on how a dog perceives its environment. ")
                    if (estMaleEntier(reponsesChoix)) append("In an intact male, hormonal levels can sometimes amplify alertness and cautious reactions. ")
                    if (reponsesChoix["age"] == 0) append("Under one year, sensitivity is often more pronounced: the dog is still building its landmarks. ")
                    append("In some cases, a genetic predisposition also plays a role, regardless of experience.")
                } else {
                    append("La sensibilité émotionnelle de $nom peut avoir plusieurs origines. ")
                    append("Une socialisation précoce limitée — peu d'expositions variées pendant les premières semaines de vie — est souvent impliquée. ")
                    append("Des expériences négatives passées, même ponctuelles, peuvent aussi laisser une empreinte durable sur la façon dont un chien perçoit son environnement. ")
                    if (estMaleEntier(reponsesChoix)) append("Chez un mâle entier, le niveau hormonal peut parfois amplifier la vigilance et les réactions de prudence. ")
                    if (reponsesChoix["age"] == 0) append("À moins d'un an, la sensibilité est souvent plus marquée : le chien est encore en train de construire ses repères. ")
                    append("Dans certains cas, une prédisposition génétique joue également un rôle, indépendamment du vécu.")
                }
            }
            Axe.ATTACHEMENT -> buildString {
                if (isEnglish()) {
                    append("$nom's strong need for closeness can be explained in several ways. ")
                    append("Too-early weaning or a difficult separation during the first weeks of life can weaken the building of emotional independence. ")
                    append("A very fusional environment — where the dog was rarely exposed to moments alone — can also reinforce this need. ")
                    if (reponsesChoix["suit_partout"] == 2) append("Constantly following its human can be both a symptom and a factor that maintains this relational dependency. ")
                    append("This type of profile is not a matter of character or whim: it often reflects a genuine difficulty finding internal support when the reassuring presence is not there.")
                } else {
                    append("Le besoin de proximité important de $nom peut s'expliquer de plusieurs façons. ")
                    append("Un sevrage trop précoce ou une séparation difficile dans les premières semaines de vie peut fragiliser la construction de l'autonomie émotionnelle. ")
                    append("Un environnement très fusionnel — où le chien a rarement été exposé à des moments seul — peut aussi renforcer ce besoin. ")
                    if (reponsesChoix["suit_partout"] == 2) append("Le fait de suivre constamment son humain peut être à la fois un symptôme et un facteur qui entretient cette dépendance relationnelle. ")
                    append("Ce type de fonctionnement n'est pas une question de caractère ou de caprice : il reflète souvent une vraie difficulté à trouver un appui interne quand la présence rassurante n'est pas là.")
                }
            }
            Axe.IMPULSIVITE -> buildString {
                if (isEnglish()) {
                    append("$nom's difficulty regulating excitement may have several origins. ")
                    append("Some dogs have a naturally low activation threshold: they escalate quickly and come down more slowly, regardless of the training they have received. ")
                    if (reponsesChoix["race_categorie"]?.let { it == 0 || it == 5 || it == 7 } == true) append("Some breed families were selected for a high energy and reactivity level, which can weigh on emotional regulation. ")
                    append("A lack of structure in daily interactions — games that are too long, too intense, without breaks — can also maintain this pattern. ")
                    if (reponsesChoix["age"] == 0 || reponsesChoix["age"] == 1) append("At a young age, inhibitory control is still developing: some impulsivity is often normal before 2-3 years. ")
                    append("Impulsivity is generally not a lack of willpower or intelligence, but a difficulty braking an emotional escalation already underway.")
                } else {
                    append("La difficulté de $nom à réguler son excitation peut avoir plusieurs origines. ")
                    append("Certains chiens ont un seuil d'activation naturellement bas : ils montent vite en intensité et redescendent plus lentement, quelle que soit l'éducation reçue. ")
                    if (reponsesChoix["race_categorie"]?.let { it == 0 || it == 5 || it == 7 } == true) append("Certaines familles de races ont été sélectionnées pour un niveau d'énergie et de réactivité élevé, ce qui peut peser sur la régulation émotionnelle. ")
                    append("Un manque de structure dans les interactions quotidiennes — jeux trop longs, trop intenses, sans pauses — peut aussi entretenir ce mode de fonctionnement. ")
                    if (reponsesChoix["age"] == 0 || reponsesChoix["age"] == 1) append("À un jeune âge, le contrôle inhibiteur est encore en développement : une certaine impulsivité est souvent normale avant 2-3 ans. ")
                    append("L'impulsivité n'est généralement pas un manque de volonté ou d'intelligence, mais une difficulté à freiner une montée émotionnelle déjà enclenchée.")
                }
            }
            Axe.REACTIVITE -> buildString {
                if (isEnglish()) {
                    append("$nom's reactivity may be explained by a combination of factors. ")
                    append("Incomplete socialisation — few encounters with other dogs, varied people or different environments during sensitive periods — is often involved. ")
                    if (reponsesChoix["a_deja_mordu"] == 1) append("The fact that there has already been a bite may indicate that reactivity has crossed an important threshold, sometimes associated with a history of poorly-experienced confrontations. ")
                    if (estMaleEntier(reponsesChoix)) append("In an intact male, interactions with other males may be more tense due to hormonal influence. ")
                    append("Repeated negative experiences with certain triggers may also have led the dog to anticipate threat and react preventively. ")
                    append("In some cases, reactivity is also a way of managing a safety distance when the dog feels overwhelmed.")
                } else {
                    append("La réactivité de $nom peut s'expliquer par une combinaison de facteurs. ")
                    append("Une socialisation incomplète — peu de rencontres avec d'autres chiens, des personnes variées ou des environnements différents pendant les périodes sensibles — est souvent en cause. ")
                    if (reponsesChoix["a_deja_mordu"] == 1) append("Le fait qu'il y ait déjà eu morsure peut indiquer que la réactivité a franchi un seuil important, parfois associé à une histoire de confrontations mal vécues. ")
                    if (estMaleEntier(reponsesChoix)) append("Chez un mâle entier, les interactions avec d'autres mâles peuvent être plus tendues en raison de l'influence hormonale. ")
                    append("Des expériences négatives répétées face à certains déclencheurs peuvent aussi avoir conduit le chien à anticiper la menace et à réagir de manière préventive. ")
                    append("Dans certains cas, la réactivité est aussi une façon de gérer une distance de sécurité quand le chien se sent dépassé.")
                }
            }
        }
    }

    private val listeCategoriesRaces = listOf(
        "Chiens de berger & troupeau", "Retrievers & Spaniels", "Terriers",
        "Molosses & Dogues", "Chiens nordiques & primitifs", "Lévriers & Races de course",
        "Races naines & compagnie", "Chiens de chasse & pisteurs", "Croisé / Bâtard / Race inconnue"
    )

    fun calculerResultat(questions: List<Question>, reponsesTexte: Map<String, String>, reponsesChoix: Map<String, Int>): ResultatAnalyse {
        val peur = calculerPourcentageAxe(Axe.PEUR, questions, reponsesChoix)
        val attachement = calculerPourcentageAxe(Axe.ATTACHEMENT, questions, reponsesChoix)
        val impulsivite = calculerPourcentageAxe(Axe.IMPULSIVITE, questions, reponsesChoix)
        val reactivite = calculerPourcentageAxe(Axe.REACTIVITE, questions, reponsesChoix)
        val profil = genererProfilGlobal(reponsesTexte["nom_chien"].orEmpty(), peur, attachement, impulsivite, reactivite)
        val contexte = calculerContexte(reponsesChoix)
        val vigilance = calculerNiveauVigilance(questions, reponsesChoix, peur, attachement, impulsivite, reactivite, contexte)
        val niveauSituation = calculerNiveauSituation(reponsesChoix, contexte, peur, attachement, impulsivite, reactivite)
        val problemePrincipal = determinerProblemePrincipal(peur, attachement, impulsivite, reactivite)
        val planAction = genererPlanAction(problemePrincipal, reponsesChoix, reponsesTexte["nom_chien"].orEmpty())
        val hypothesePrincipale = detecterHypothesePrincipale(reponsesChoix, peur, attachement, impulsivite, reactivite, contexte)
        val prioriteAction = determinerPrioriteAction(reponsesChoix, contexte, peur, attachement, impulsivite, reactivite)
        val facteursAggravants = detecterFacteursAggravants(reponsesChoix, contexte, peur, attachement, impulsivite, reactivite)
        val facteursProtecteurs = detecterFacteursProtecteurs(reponsesChoix, contexte)
        val prioriteImmediate = construirePrioriteImmediate(reponsesChoix, contexte, prioriteAction, niveauSituation, reponsesTexte["nom_chien"].orEmpty())
        val explicationResultat = construireExplicationResultat(reponsesChoix, contexte, peur, attachement, impulsivite, reactivite)
        val syntheseAvancee = genererSyntheseAvancee(nomChienAffiche(reponsesTexte["nom_chien"].orEmpty()), hypothesePrincipale, prioriteAction, facteursAggravants, facteursProtecteurs)
        val originesPossibles = genererOriginesPossibles(reponsesTexte["nom_chien"].orEmpty(), problemePrincipal, peur, attachement, impulsivite, reactivite, reponsesChoix)
        val raceCategorieTexte = reponsesChoix["race_categorie"]?.let { listeCategoriesRaces.getOrNull(it) }
        return ResultatAnalyse(
            peur = peur, attachement = attachement, impulsivite = impulsivite, reactivite = reactivite,
            niveauPeur = calculerNiveauAxe(peur), niveauAttachement = calculerNiveauAxe(attachement),
            niveauImpulsivite = calculerNiveauAxe(impulsivite), niveauReactivite = calculerNiveauAxe(reactivite),
            profil = profil, vigilance = vigilance, niveauSituation = niveauSituation, contexte = contexte,
            problemePrincipal = problemePrincipal, problemesImportants = determinerProblemesImportants(peur, attachement, impulsivite, reactivite),
            explicationPrincipale = explicationProbleme(problemePrincipal, peur, attachement, impulsivite, reactivite),
            conseilPrincipal = conseilPrincipal(problemePrincipal, peur, attachement, impulsivite, reactivite),
            conseilsPratiques = genererConseilsPratiquesPersonnalises(reponsesTexte["nom_chien"].orEmpty(), reponsesChoix, peur, attachement, impulsivite, reactivite),
            planAction = planAction,
            messageSituation = genererMessageSituation(niveauSituation, reponsesTexte["nom_chien"].orEmpty()),
            raisonSituation = genererRaisonSituation(reponsesChoix, contexte),
            messageAide = genererMessageAide(reponsesChoix, contexte, niveauSituation, reponsesTexte["nom_chien"].orEmpty()),
            apparitionBrutale = reponsesChoix["apparition"] == 1,
            aDejaMordu = reponsesChoix["a_deja_mordu"] == 1,
            hypothesePrincipale = hypothesePrincipale, prioriteAction = prioriteAction,
            prioriteImmediate = prioriteImmediate, explicationResultat = explicationResultat,
            facteursAggravants = facteursAggravants, facteursProtecteurs = facteursProtecteurs,
            syntheseAvancee = syntheseAvancee, raceCategorie = raceCategorieTexte, racePrecise = null,
            originesPossibles = originesPossibles
        )
    }

    fun doitAfficherQuestion(questionId: String, reponsesChoix: Map<String, Int>): Boolean {
        return when (questionId) {
            "si_non_quand" -> { val p = reponsesChoix["proprete_maison"]; p == 1 || p == 2 }
            "apparition", "situation_principale", "duree_probleme", "evolution_probleme",
            "frequence_probleme", "intensite_probleme", "generalisation_probleme",
            "changement_recent", "signe_physique" -> reponsesChoix["a_un_probleme"] != 1
            else -> true
        }
    }

    // Délègue à AppStrings pour la traduction
    fun titreSectionPourQuestion(questionId: String): String = strTitreSection(questionId)

    fun aideQuestion(questionId: String): String? = when (questionId) {
        "race_categorie" -> if (isEnglish())
            "Choose the family that best matches your dog. For a mixed breed, choose the last option."
        else
            "Choisissez la famille qui ressemble le plus à votre chien. Pour un croisé, choisissez la dernière option."
        "sterilise" -> if (isEnglish())
            "Neutering/spaying influences certain behaviours such as reactivity or tensions between dogs."
        else
            "La stérilisation influence certains comportements comme la réactivité ou les tensions entre chiens."
        "adaptation_changements" -> if (isEnglish())
            "Think about changes in habits, place, rhythm or environment."
        else
            "Pensez aux changements d'habitudes, de lieu, de rythme ou d'environnement."
        "comportement_exterieur" -> if (isEnglish())
            "Answer thinking mainly about usual walks and outings."
        else
            "Répondez en pensant surtout aux promenades et sorties habituelles."
        "reaction_peur" -> if (isEnglish())
            "Choose the most frequent reaction when your dog is worried."
        else
            "Choisissez la réaction la plus fréquente quand votre chien est inquiet."
        "support_absences" -> if (isEnglish())
            "Think about when you leave and the time your dog spends alone."
        else
            "Pensez au moment où vous partez et au temps où votre chien reste seul."
        "pendant_absence" -> if (isEnglish())
            "Answer based on what you observe or what you are told."
        else
            "Répondez selon ce que vous observez ou ce que l'on vous rapporte."
        "si_non_quand" -> if (isEnglish())
            "This question only applies if your dog is not always house-trained."
        else
            "Cette question sert seulement si votre chien n'est pas toujours propre."
        "calmer_apres_excitation" -> if (isEnglish())
            "After play, an outing, a visit or a stimulating moment."
        else
            "Après le jeu, une sortie, une visite ou un moment stimulant."
        "jeu_comportement" -> if (isEnglish())
            "For example if biting hard, jumping, overflowing or struggling to stop."
        else
            "Par exemple s'il mordille fort, saute, déborde ou a du mal à s'arrêter."
        "reaction_inconnus" -> if (isEnglish())
            "For example: barking, avoidance, tension, growling."
        else
            "Par exemple : aboiements, évitement, tension, grognements."
        "reaction_chiens" -> if (isEnglish())
            "For example: tension, barking, lunging, avoidance or agitation."
        else
            "Par exemple : tension, aboiements, charge, évitement ou agitation."
        "a_deja_mordu" -> if (isEnglish())
            "Even a single isolated bite counts."
        else
            "Même une morsure ponctuelle compte."
        "signe_physique" -> if (isEnglish())
            "Even a doubt can be useful to mention."
        else
            "Même un doute peut être utile à signaler."
        else -> null
    }
}

fun questionsApplication(): List<Question> {
    return listOf(
        QuestionTexte("nom_chien",
            if (isEnglish()) "What is your dog's name?" else "Quel est le nom de votre chien ?"),

        QuestionChoix("race_categorie",
            if (isEnglish()) "Which breed family does your dog belong to?" else "À quelle famille de races appartient votre chien ?",
            if (isEnglish()) listOf("Herding & sheepdogs", "Retrievers & Spaniels", "Terriers",
                "Molossers & Mastiffs", "Nordic & primitive dogs", "Sighthounds & racing dogs",
                "Toy & companion breeds", "Hunting & tracking dogs", "Mixed breed / unknown breed")
            else listOf("Chiens de berger & troupeau", "Retrievers & Spaniels", "Terriers",
                "Molosses & Dogues", "Chiens nordiques & primitifs", "Lévriers & Races de course",
                "Races naines & compagnie", "Chiens de chasse & pisteurs", "Croisé / Bâtard / Race inconnue")),

        QuestionChoix("age",
            if (isEnglish()) "How old is your dog?" else "Quel âge a votre chien ?",
            if (isEnglish()) listOf("Under 1 year", "Between 1 and 3 years", "Between 4 and 7 years", "8 years and over")
            else listOf("Moins d'1 an", "Entre 1 et 3 ans", "Entre 4 et 7 ans", "8 ans et +")),

        QuestionChoix("sterilise",
            if (isEnglish()) "Your dog is:" else "Votre chien est :",
            if (isEnglish()) listOf("A neutered male", "A spayed female", "An intact male", "An intact female")
            else listOf("Un mâle stérilisé", "Une femelle stérilisée", "Un mâle entier", "Une femelle entière")),

        QuestionChoix("peur_stimuli",
            if (isEnglish()) "Does your dog show fear in certain situations?" else "Votre chien montre-t-il de la peur face à certaines situations ?",
            if (isEnglish()) listOf("Never", "Sometimes", "Often") else listOf("Jamais", "Parfois", "Souvent"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("adaptation_changements",
            if (isEnglish()) "Does your dog struggle to adapt to changes?" else "Votre chien a-t-il du mal à s'adapter aux changements ?",
            if (isEnglish()) listOf("No", "A little", "Yes") else listOf("Non", "Un peu", "Oui"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("comportement_exterieur",
            if (isEnglish()) "On walks or outdoors, your dog is rather:" else "En promenade ou à l'extérieur, votre chien est plutôt :",
            if (isEnglish()) listOf("Calm and relaxed", "Excited / hard to manage", "Fearful / avoidant")
            else listOf("Calme et détendu", "Excité / difficile à canaliser", "Craintif / en évitement"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_peur",
            if (isEnglish()) "When your dog is scared, how does it react?" else "Quand votre chien a peur, il réagit plutôt comment ?",
            if (isEnglish()) listOf("It recovers quickly", "It hides / flees", "It panics or becomes aggressive")
            else listOf("Il récupère vite", "Il se cache / fuit", "Il panique ou devient agressif"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("support_absences",
            if (isEnglish()) "How does your dog handle your absences?" else "Comment votre chien vit-il vos absences ?",
            if (isEnglish()) listOf("Well", "So-so", "With difficulty") else listOf("Bien", "Moyennement", "Difficilement"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("pendant_absence",
            if (isEnglish()) "While you are away, your dog:" else "Pendant vos absences, votre chien :",
            if (isEnglish()) listOf("Stays calm", "May vocalise or become restless", "Destroys / barks / panics")
            else listOf("Reste calme", "Peut vocaliser ou s'agiter", "Détruit / aboie / panique"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("suit_partout",
            if (isEnglish()) "Does your dog follow you everywhere in the house?" else "Votre chien vous suit-il partout dans la maison ?",
            if (isEnglish()) listOf("No", "Sometimes", "It barely leaves my side")
            else listOf("Non", "Parfois", "Il ne me quitte pratiquement pas"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("autre_personne_apaise",
            if (isEnglish()) "Is the presence of another person enough to calm it?" else "La présence d'une autre personne suffit-elle à l'apaiser ?",
            if (isEnglish()) listOf("Yes", "It is only really calmed by me", "I don't know")
            else listOf("Oui", "Il n'est vraiment apaisé qu'avec moi", "Je ne sais pas"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 2, 0)),

        QuestionChoix("proprete_maison",
            if (isEnglish()) "Is your dog house-trained?" else "Votre chien est-il propre à la maison ?",
            if (isEnglish()) listOf("Yes", "No", "Sometimes") else listOf("Oui", "Non", "Parfois"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 2, 1)),

        QuestionChoix("si_non_quand",
            if (isEnglish()) "If your dog is not always house-trained, in which situations?" else "Si votre chien n'est pas toujours propre, dans quelles situations ?",
            if (isEnglish()) listOf("During your absences", "In your presence", "At night", "Randomly")
            else listOf("Lors de vos absences", "En votre présence", "La nuit", "De manière aléatoire")),

        QuestionChoix("calmer_apres_excitation",
            if (isEnglish()) "Does your dog struggle to calm down after an exciting moment?" else "Votre chien a-t-il du mal à se calmer après un moment excitant ?",
            if (isEnglish()) listOf("No", "Sometimes", "Yes") else listOf("Non", "Parfois", "Oui"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("jeu_comportement",
            if (isEnglish()) "When playing, your dog:" else "Quand il joue, votre chien :",
            if (isEnglish()) listOf("Stays controlled", "Can get very excited", "Play becomes hard to control")
            else listOf("Reste contrôlé", "Peut beaucoup s'exciter", "Les jeux deviennent difficiles à contrôler"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("vole_objets",
            if (isEnglish()) "Does your dog steal food or objects?" else "Votre chien vole-t-il de la nourriture ou des objets ?",
            if (isEnglish()) listOf("No", "Sometimes", "Often") else listOf("Non", "Parfois", "Souvent"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("poursuite_mouvement",
            if (isEnglish()) "Does your dog easily chase moving things?" else "Votre chien poursuit-il facilement ce qui bouge ?",
            if (isEnglish()) listOf("No", "Sometimes", "Often") else listOf("Non", "Parfois", "Souvent"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_inconnus",
            if (isEnglish()) "Does your dog react negatively to unknown people?" else "Votre chien réagit-il difficilement aux personnes inconnues ?",
            if (isEnglish()) listOf("No", "Sometimes", "Often") else listOf("Non", "Parfois", "Souvent"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_chiens",
            if (isEnglish()) "Does your dog react negatively to other dogs?" else "Votre chien réagit-il difficilement aux autres chiens ?",
            if (isEnglish()) listOf("No", "Sometimes", "Often") else listOf("Non", "Parfois", "Souvent"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("a_deja_mordu",
            if (isEnglish()) "Has your dog ever bitten?" else "Votre chien a-t-il déjà mordu ?",
            if (isEnglish()) listOf("No", "Yes") else listOf("Non", "Oui"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 4), poids = 2, signalCritique = true),

        QuestionChoix("defense_ressources",
            if (isEnglish()) "Does your dog growl or become tense when approached near its bowl, toys or bed?"
            else "Votre chien grogne-t-il ou devient-il tendu quand on s'approche de sa gamelle, de ses jouets ou de son couchage ?",
            if (isEnglish()) listOf("No, never", "Sometimes, in certain situations", "Yes, it happens often")
            else listOf("Non, jamais", "Parfois, dans certaines situations", "Oui, c'est fréquent"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 2, 4), signalAlerte = true),

        QuestionChoix("a_un_probleme",
            if (isEnglish()) "Is there a particular behaviour you are concerned about right now?"
            else "Y a-t-il un comportement particulier qui vous préoccupe en ce moment ?",
            if (isEnglish()) listOf("Yes, I'd like to understand", "No, everything is fine")
            else listOf("Oui, j'aimerais comprendre", "Non, tout va bien")),

        QuestionChoix("apparition",
            if (isEnglish()) "The behaviour you are concerned about appeared:" else "Le comportement qui vous préoccupe est apparu :",
            if (isEnglish()) listOf("Gradually", "Suddenly, with no apparent reason", "I'm not really sure")
            else listOf("Progressivement", "Du jour au lendemain, sans raison apparente", "Je ne sais pas vraiment")),

        QuestionChoix("situation_principale",
            if (isEnglish()) "It mainly occurs:" else "Il apparaît principalement :",
            if (isEnglish()) listOf("In many situations", "Mainly in your absence", "Mainly outdoors", "Mainly in your presence")
            else listOf("Dans beaucoup de situations", "Surtout en votre absence", "Surtout à l'extérieur", "Surtout en votre présence")),

        QuestionChoix("duree_probleme",
            if (isEnglish()) "How long have you been observing this behaviour?" else "Depuis combien de temps observez-vous ce comportement ?",
            if (isEnglish()) listOf("Less than 1 week", "1 to 4 weeks", "Several months", "Always")
            else listOf("Moins d'1 semaine", "1 à 4 semaines", "Plusieurs mois", "Depuis toujours")),

        QuestionChoix("evolution_probleme",
            if (isEnglish()) "This behaviour:" else "Ce comportement :",
            if (isEnglish()) listOf("Is improving", "Is stable", "Is getting worse")
            else listOf("S'améliore", "Reste stable", "S'aggrave")),

        QuestionChoix("frequence_probleme",
            if (isEnglish()) "How often does it occur?" else "À quelle fréquence cela se produit-il ?",
            if (isEnglish()) listOf("Rarely", "A few times a week", "Every day", "Several times a day")
            else listOf("Rarement", "Quelques fois par semaine", "Tous les jours", "Plusieurs fois par jour")),

        QuestionChoix("intensite_probleme",
            if (isEnglish()) "When it happens, it is rather:" else "Quand cela arrive, c'est plutôt :",
            if (isEnglish()) listOf("Easily manageable", "Annoying", "Hard to manage", "Loss of control / dangerous")
            else listOf("Gérable facilement", "Gênant", "Difficile à gérer", "Perte de contrôle / dangereux")),

        QuestionChoix("generalisation_probleme",
            if (isEnglish()) "The behaviour occurs rather:" else "Le comportement arrive plutôt :",
            if (isEnglish()) listOf("In one specific situation", "In several different situations", "In most situations")
            else listOf("Dans une situation bien précise", "Dans plusieurs situations différentes", "Dans la plupart des situations")),

        QuestionChoix("changement_recent",
            if (isEnglish()) "Has there been a major change in your dog's life recently?" else "Y a-t-il eu récemment un changement important dans sa vie ?",
            if (isEnglish()) listOf("No change", "A minor change", "A major change (move, baby, separation...)")
            else listOf("Aucun changement", "Un changement léger", "Un changement important (déménagement, bébé, séparation...)")),

        QuestionChoix("signe_physique",
            if (isEnglish()) "Have you noticed a physical change in your dog recently?" else "Avez-vous remarqué un changement physique chez votre chien ces derniers temps ?",
            if (isEnglish()) listOf("No, nothing particular", "Yes, it seems more tired than usual",
                "Yes, it seems to be in pain or has difficulty moving", "Yes, something else has changed physically")
            else listOf("Non, rien de particulier", "Oui, il semble plus fatigué qu'avant",
                "Oui, il semble avoir mal ou être gêné dans ses mouvements", "Oui, autre chose a changé physiquement"))
    )
}