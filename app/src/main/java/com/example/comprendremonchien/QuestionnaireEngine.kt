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

    fun libelleNiveauAxe(niveau: NiveauAxe): String = when (niveau) {
        NiveauAxe.PEU_MARQUE -> "Peu marqué"
        NiveauAxe.A_SURVEILLER -> "À surveiller"
        NiveauAxe.MARQUE -> "Marqué"
        NiveauAxe.TRES_MARQUE -> "Très marqué"
    }

    fun determinerProblemePrincipal(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): Axe =
        listOf(Axe.PEUR to peur, Axe.ATTACHEMENT to attachement, Axe.IMPULSIVITE to impulsivite, Axe.REACTIVITE to reactivite)
            .maxByOrNull { it.second }!!.first

    fun determinerProfilType(peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val top = listOf(Axe.PEUR to peur, Axe.ATTACHEMENT to attachement, Axe.IMPULSIVITE to impulsivite, Axe.REACTIVITE to reactivite)
            .sortedByDescending { it.second }
        val first = top[0].first
        val second = top[1].first
        val firstScore = top[0].second
        if (firstScore <= 30) return "Compagnon bien ancré"
        return when {
            first == Axe.PEUR && second == Axe.REACTIVITE -> "Explorateur sensible"
            first == Axe.ATTACHEMENT && second == Axe.PEUR -> "Cœur collé-serré"
            first == Axe.ATTACHEMENT && second == Axe.REACTIVITE -> "Très attaché"
            first == Axe.IMPULSIVITE && second == Axe.REACTIVITE -> "Débordant d'énergie"
            first == Axe.IMPULSIVITE && second == Axe.PEUR -> "Vif et sensible"
            first == Axe.REACTIVITE -> "Chien très réactif"
            first == Axe.PEUR -> "Émotif vigilant"
            first == Axe.ATTACHEMENT -> "Fusionnel"
            first == Axe.IMPULSIVITE -> "Moteur sensible"
            else -> "Profil équilibré"
        }
    }

    fun phraseHumaineProfil(nomChien: String, scoreGlobal: Int, profilType: String, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        val nom = nomChienAffiche(nomChien)
        return when {
            maxAxe <= 30 -> "$nom semble évoluer sur une base globalement stable et adaptée."
            maxAxe <= 60 -> "$nom présente quelques points de fragilité, sans que cela ne prenne toute la place."
            else -> "$nom semble actuellement en difficulté dans certaines situations. Cette lecture reste indicative et gagnerait à être confrontée à l'observation réelle de son quotidien."
        }
    }

    fun genererProfilGlobal(nomChien: String, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): ProfilGlobal {
        val scoreGlobal = calculerScoreGlobal(peur, attachement, impulsivite, reactivite)
        val profilType = determinerProfilType(peur, attachement, impulsivite, reactivite)
        return when {
            peur <= 30 && attachement <= 30 && impulsivite <= 30 && reactivite <= 30 ->
                ProfilGlobal("Profil globalement équilibré", "Les réponses suggèrent un fonctionnement plutôt stable dans l'ensemble.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            peur >= 60 && reactivite >= 60 ->
                ProfilGlobal("Sensibilité émotionnelle et réactivité marquées", "Le profil suggère une sensibilité importante, avec des réactions plus visibles lorsque certaines situations deviennent difficiles à gérer.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            attachement >= 60 && peur >= 60 ->
                ProfilGlobal("Besoin de proximité avec fragilité émotionnelle", "Le fonctionnement évoque un besoin de repères relationnels forts associé à une sensibilité émotionnelle notable.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            attachement >= 60 && reactivite >= 60 ->
                ProfilGlobal("Proximité importante avec réactions intenses", "Le profil semble associer besoin de proximité et réactions plus marquées dans certains contextes.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            impulsivite >= 60 && reactivite >= 60 ->
                ProfilGlobal("Réactions rapides avec difficulté de contrôle", "Le profil suggère des montées émotionnelles rapides, avec une gestion plus difficile de certaines stimulations.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            impulsivite >= 60 && peur >= 60 ->
                ProfilGlobal("Sensibilité avec régulation difficile", "Le fonctionnement évoque à la fois une sensibilité émotionnelle et une difficulté à retrouver rapidement l'équilibre.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            reactivite >= 60 ->
                ProfilGlobal("Réactivité plus marquée", "Le profil suggère une tendance à réagir fortement à certains éléments de l'environnement.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            attachement >= 60 ->
                ProfilGlobal("Besoin de proximité plus important", "Les réponses font ressortir un besoin de proximité plus marqué que la moyenne.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            impulsivite >= 60 ->
                ProfilGlobal("Régulation plus difficile", "Le profil évoque une difficulté dans la gestion de l'excitation et des retours au calme.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            peur >= 60 ->
                ProfilGlobal("Sensibilité émotionnelle plus marquée", "Les réponses suggèrent une sensibilité plus importante à certains changements ou situations.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
            else ->
                ProfilGlobal("Profil à nuancer", "Les réponses font apparaître quelques points de vigilance, sans qu'un aspect ne domine clairement.", profilType, scoreGlobal, phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite))
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
            NiveauSituation.STABLE -> "À ce stade, la situation semble plutôt stable pour $nom."
            NiveauSituation.A_TRAVAILLER -> "La situation mérite probablement d'être travaillée de manière progressive pour $nom."
            NiveauSituation.SENSIBLE -> "La situation paraît plus sensible pour $nom et justifie une attention particulière."
        }
    }

    fun genererRaisonSituation(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse): String {
        val raisons = mutableListOf<String>()
        if (reponsesChoix["duree_probleme"] == 0) raisons += "Le caractère très récent du comportement invite à une vigilance particulière."
        if (reponsesChoix["evolution_probleme"] == 2) raisons += "Le fait que cela semble s'aggraver peut indiquer que le problème prend plus de place."
        if (contexte.physique >= 4) raisons += "Des signes physiques ou une gêne possible invitent à la prudence."
        return raisons.firstOrNull() ?: "L'ensemble des réponses invite surtout à avancer progressivement."
    }

    fun genererConseilsPratiquesPersonnalises(nomChien: String, reponsesChoix: Map<String, Int>,
                                              peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): List<String> {
        val scoreMax = maxOf(peur, attachement, impulsivite, reactivite)
        if (scoreMax == 0) return listOf("Continuer l'observation du quotidien et maintenir les repères déjà en place.")
        val conseils = mutableListOf<String>()
        if (estMaleEntier(reponsesChoix) && reactivite >= 50) conseils += "Chez un mâle entier, la réactivité peut être amplifiée par les hormones. Un avis vétérinaire sur la castration peut valoir la peine d'être discuté."
        if (estFemelleEntiere(reponsesChoix) && (peur >= 50 || impulsivite >= 50)) conseils += "Chez une femelle entière, certains comportements peuvent varier selon le cycle. Observer si les comportements s'intensifient à certaines périodes peut donner des repères utiles."
        val scores = listOf(Axe.PEUR to peur, Axe.ATTACHEMENT to attachement, Axe.IMPULSIVITE to impulsivite, Axe.REACTIVITE to reactivite)
        val axesDominants = scores.filter { it.second == scoreMax }.map { it.first }
        if (axesDominants.size > 1) {
            val texteAxes = axesDominants.joinToString(" et ") {
                when (it) {
                    Axe.PEUR -> "la sensibilité émotionnelle"
                    Axe.ATTACHEMENT -> "le besoin de proximité"
                    Axe.IMPULSIVITE -> "la régulation de l'excitation"
                    Axe.REACTIVITE -> "la réactivité à l'environnement"
                }
            }
            conseils += "Plusieurs dimensions semblent ressortir conjointement : $texteAxes. Une approche progressive, un axe après l'autre, paraît préférable."
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
        if (maxAxe <= 30) return "Les éléments recueillis ne mettent pas en évidence de difficulté comportementale marquée à ce stade."
        return when (axe) {
            Axe.PEUR -> "Les réactions observées semblent s'inscrire dans une sensibilité émotionnelle relativement élevée. Dans ce type de fonctionnement, certains changements ou situations peuvent être perçus comme plus intenses ou difficiles à gérer."
            Axe.ATTACHEMENT -> "Les éléments recueillis suggèrent un besoin de proximité relativement important. Dans ce type de fonctionnement, l'autonomie émotionnelle peut être encore fragile, ce qui peut rendre certaines séparations ou absences plus difficiles à vivre."
            Axe.IMPULSIVITE -> "Les réponses évoquent une difficulté possible dans la régulation de l'excitation. Il ne s'agit généralement pas d'un manque de volonté, mais plutôt d'un seuil de montée émotionnelle rapidement atteint, avec un retour au calme plus lent."
            Axe.REACTIVITE -> "Les éléments recueillis suggèrent une réactivité marquée face à certains éléments de son environnement. Ce type de réponse peut apparaître lorsque le chien se sent en tension, incertain ou dépassé dans certaines situations."
        }
    }

    fun conseilPrincipal(axe: Axe, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        if (maxAxe <= 30) return "À ce stade, aucun axe de travail prioritaire ne se dégage clairement. L'objectif peut simplement être de maintenir un cadre stable, cohérent et prévisible."
        return when (axe) {
            Axe.ATTACHEMENT -> "Une première piste consiste à travailler progressivement les moments de séparation, en restant sur des durées très courtes et maîtrisées. L'objectif est de renforcer la capacité du chien à rester apaisé sans dépendre constamment de la présence humaine."
            Axe.PEUR -> "Il est généralement pertinent de respecter les seuils de tolérance du chien. Travailler à distance des éléments déclencheurs, dans des conditions calmes, permet souvent de favoriser une évolution progressive."
            Axe.REACTIVITE -> "Une approche progressive basée sur la gestion de la distance et la réduction de la pression environnementale est souvent recommandée. L'objectif est de maintenir le chien dans une zone où il reste encore capable de traiter l'information."
            Axe.IMPULSIVITE -> "Structurer les interactions avec des temps courts et des pauses régulières peut aider à améliorer la régulation. Le travail consiste surtout à favoriser des retours au calme fréquents et prévisibles."
        }
    }

    fun genererPlanAction(axe: Axe, reponsesChoix: Map<String, Int>, nomChien: String): PlanAction {
        val aFaire = mutableListOf<String>()
        val aEviter = mutableListOf<String>()
        val aObserver = mutableListOf<String>()
        when (axe) {
            Axe.ATTACHEMENT -> {
                aFaire += "Réduire la charge émotionnelle autour des départs et des retours."
                aFaire += "Proposer progressivement de petits moments d'autonomie dans des situations faciles."
                aFaire += "Commencer par des absences très courtes et maîtrisées."
                aEviter += "Les rituels de départ ou de retrouvailles très marqués."
                aEviter += "Les absences trop longues ou trop difficiles d'emblée."
                aEviter += "Les réactions émotionnelles face aux manifestations liées à l'absence."
                aObserver += "Le moment précis où la tension apparaît."
                aObserver += "La capacité du chien à se poser seul dans les moments neutres."
                aObserver += "L'évolution lorsque les interactions deviennent plus prévisibles."
            }
            Axe.PEUR -> {
                aFaire += "Travailler à distance suffisante pour que le chien reste encore calme."
                aFaire += "Laisser le chien observer sans le contraindre."
                aFaire += "Créer des expériences positives dans des contextes maîtrisés."
                aEviter += "Forcer le chien à affronter ce qui l'inquiète."
                aEviter += "Réduire trop vite la distance."
                aEviter += "Maintenir le chien dans une situation où il est déjà en difficulté."
                aObserver += "La distance à laquelle la tension apparaît."
                aObserver += "Les signaux précoces de stress."
                aObserver += "Les contextes dans lesquels il reste à l'aise."
            }
            Axe.IMPULSIVITE -> {
                aFaire += "Structurer les interactions avec des séquences courtes et des pauses."
                aFaire += "Interrompre calmement les situations où l'excitation monte trop."
                aFaire += "Valoriser davantage les moments de calme."
                aEviter += "Les interactions trop longues ou trop stimulantes."
                aEviter += "Répondre à l'excitation par plus d'excitation."
                aEviter += "Attendre le débordement complet avant d'agir."
                aObserver += "La rapidité de montée en excitation."
                aObserver += "Le temps nécessaire pour retrouver le calme."
                aObserver += "Les situations qui déclenchent le plus vite les débordements."
            }
            Axe.REACTIVITE -> {
                aFaire += "Augmenter la distance avec les déclencheurs pour rester dans une zone gérable."
                aFaire += "Choisir des environnements plus faciles."
                aFaire += "Travailler dans des situations où le chien peut encore observer sans réagir."
                aEviter += "Les confrontations directes ou trop rapprochées."
                aEviter += "Les situations que le chien ne peut pas gérer."
                aEviter += "Insister une fois la réaction enclenchée."
                aObserver += "Les déclencheurs précis et leur intensité."
                aObserver += "La distance à laquelle le chien bascule."
                aObserver += "Les signaux annonciateurs juste avant la réaction."
            }
        }
        if (reponsesChoix["signe_physique"] == 2 || reponsesChoix["signe_physique"] == 3) aFaire += "Prévoir un avis vétérinaire pour écarter une cause physique associée."
        return PlanAction(aFaire.distinct().take(3), aEviter.distinct().take(3), aObserver.distinct().take(3))
    }

    fun genererMessageAide(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, niveauSituation: NiveauSituation, nomChien: String): String? {
        val nom = nomChienAffiche(nomChien)
        return when {
            reponsesChoix["a_deja_mordu"] == 1 -> "Le fait qu'il y ait déjà eu morsure justifie de ne pas rester seul avec cette situation. Un accompagnement professionnel individualisé est recommandé pour $nom."
            contexte.physique >= 4 -> "Certains éléments font penser qu'une gêne, une douleur ou une composante physique pourrait participer au problème. Un avis vétérinaire est recommandé pour $nom."
            reponsesChoix["apparition"] == 1 -> "Lorsque des comportements apparaissent brutalement, il est prudent d'écarter d'abord une cause médicale. Un point vétérinaire peut être utile pour $nom."
            niveauSituation == NiveauSituation.SENSIBLE -> "Au vu des réponses, la situation mérite un regard professionnel afin d'éviter qu'elle ne se fixe ou ne s'aggrave."
            else -> null
        }
    }

    fun detecterFacteursAggravants(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): List<String> {
        val facteurs = mutableListOf<String>()
        if (reponsesChoix["apparition"] == 1) facteurs += "Apparition brutale"
        if (reponsesChoix["evolution_probleme"] == 2) facteurs += "Comportement en aggravation"
        if (reponsesChoix["intensite_probleme"] == 3) facteurs += "Intensité très forte"
        if (reponsesChoix["generalisation_probleme"] == 2) facteurs += "Présence dans de nombreuses situations"
        if (contexte.physique >= 4) facteurs += "Suspicion de gêne ou cause physique"
        if (maxOf(peur, attachement, impulsivite, reactivite) >= 75) facteurs += "Niveau élevé sur au moins un axe"
        if (estMaleEntier(reponsesChoix) && reactivite >= 50) facteurs += "Mâle entier avec réactivité marquée"
        return facteurs.distinct()
    }

    fun detecterFacteursProtecteurs(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse): List<String> {
        val facteurs = mutableListOf<String>()
        if (reponsesChoix["evolution_probleme"] == 0) facteurs += "Une amélioration semble déjà présente"
        if (reponsesChoix["frequence_probleme"] == 0) facteurs += "Le comportement reste peu fréquent"
        if (reponsesChoix["intensite_probleme"] == 0 || reponsesChoix["intensite_probleme"] == 1) facteurs += "L'intensité reste encore contenue"
        if (reponsesChoix["generalisation_probleme"] == 0) facteurs += "Le problème semble limité à des contextes précis"
        if (contexte.scoreContexte <= 3) facteurs += "Le contexte global ne suggère pas une situation fortement dégradée"
        if (estSterilise(reponsesChoix)) facteurs += "Chien stérilisé — facteur stabilisant possible"
        return facteurs.distinct()
    }

    fun detecterHypothesePrincipale(reponsesChoix: Map<String, Int>, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int, contexte: ContexteAnalyse): String {
        return when {
            contexte.physique >= 4 -> "Les éléments recueillis invitent d'abord à écarter une composante physique avant d'aller plus loin dans l'interprétation comportementale."
            attachement >= 60 && (reponsesChoix["support_absences"] == 2 || reponsesChoix["pendant_absence"] == 2) -> "Les réponses peuvent évoquer une difficulté autour de la gestion de la séparation et de l'absence."
            peur >= 60 && reactivite >= 60 -> "Les éléments recueillis suggèrent une sensibilité émotionnelle associée à des réactions marquées face à l'environnement."
            impulsivite >= 60 && (reponsesChoix["jeu_comportement"] == 2 || reponsesChoix["calmer_apres_excitation"] == 2) -> "Les réponses orientent vers une difficulté possible dans la régulation émotionnelle, avec des montées en excitation rapides."
            reactivite >= 60 && reponsesChoix["reaction_chiens"] == 2 -> "Les réponses peuvent évoquer une réactivité importante dans les interactions avec les autres chiens."
            reactivite >= 60 && reponsesChoix["reaction_inconnus"] == 2 -> "Les réponses peuvent évoquer une réactivité importante face aux personnes inconnues."
            peur >= 60 -> "Les réponses suggèrent une sensibilité émotionnelle importante. Certains environnements ou situations peuvent être perçus comme plus difficiles à tolérer."
            attachement >= 60 -> "Le profil suggère surtout un besoin de proximité important, avec une autonomie émotionnelle qui paraît encore fragile dans certaines situations."
            impulsivite >= 60 -> "Les réponses orientent vers une difficulté possible dans la régulation émotionnelle, avec des montées rapides en excitation."
            reactivite >= 60 -> "Les éléments recueillis peuvent correspondre à une réactivité accrue face à certains éléments de son environnement."
            else -> "Aucune hypothèse dominante ne se dégage clairement à partir des réponses. Plusieurs facteurs peuvent être impliqués."
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
            reponsesChoix["a_deja_mordu"] == 1 -> PrioriteImmediate(PrioriteAction.URGENTE, "Priorité immédiate : sécuriser et se faire accompagner",
                "Comme il y a déjà eu morsure, la situation ne doit pas être banalisée pour $nom.",
                listOf("Éviter les situations à risque.", "Demander rapidement l'aide d'un professionnel du comportement."))
            contexte.physique >= 4 -> PrioriteImmediate(PrioriteAction.URGENTE, "Priorité immédiate : écarter une cause physique",
                "Des signes physiques sont signalés chez $nom.",
                listOf("Prendre un avis vétérinaire rapidement.", "Éviter les sollicitations difficiles en attendant."))
            priorite == PrioriteAction.ELEVEE || niveauSituation == NiveauSituation.SENSIBLE -> PrioriteImmediate(PrioriteAction.ELEVEE, "Priorité immédiate : agir sans tarder",
                "La situation semble suffisamment marquée pour justifier une action rapide pour $nom.",
                listOf("Alléger les contextes les plus difficiles.", "Envisager un accompagnement professionnel."))
            priorite == PrioriteAction.MODEREE -> PrioriteImmediate(PrioriteAction.MODEREE, "Priorité immédiate : avancer progressivement",
                "La situation mérite d'être prise au sérieux pour $nom.",
                listOf("Commencer un travail progressif sur les situations difficiles.", "Observer fréquence et contexte pendant quelques jours."))
            else -> PrioriteImmediate(PrioriteAction.FAIBLE, "Priorité immédiate : surveiller calmement",
                "Rien ne ressort comme urgent à ce stade pour $nom.",
                listOf("Continuer l'observation du quotidien.", "Maintenir un cadre stable et prévisible."))
        }
    }

    fun construireExplicationResultat(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): ExplicationResultat {
        val raisons = mutableListOf<String>()
        if (reponsesChoix["evolution_probleme"] == 2) raisons += "Le comportement semble s'aggraver."
        if (reponsesChoix["frequence_probleme"] == 2 || reponsesChoix["frequence_probleme"] == 3) raisons += "Le comportement paraît revenir fréquemment."
        if (reponsesChoix["intensite_probleme"] == 2 || reponsesChoix["intensite_probleme"] == 3) raisons += "L'intensité décrite paraît importante."
        if (raisons.isEmpty()) raisons += "Les réponses suggèrent surtout quelques points de vigilance."
        return ExplicationResultat(raisons.take(3),
            detecterFacteursAggravants(reponsesChoix, contexte, peur, attachement, impulsivite, reactivite),
            detecterFacteursProtecteurs(reponsesChoix, contexte))
    }

    fun genererSyntheseAvancee(nom: String, hypothese: String, priorite: PrioriteAction, aggravants: List<String>, protecteurs: List<String>): String {
        val intro = when (priorite) {
            PrioriteAction.FAIBLE -> "$nom présente un fonctionnement globalement stable avec quelques points de vigilance."
            PrioriteAction.MODEREE -> "$nom présente une difficulté réelle qui mérite une approche progressive."
            PrioriteAction.ELEVEE -> "$nom semble actuellement en difficulté sur un plan suffisamment marqué pour nécessiter une attention active."
            PrioriteAction.URGENTE -> "$nom présente des éléments qui justifient une attention rapide."
        }
        val aggr = if (aggravants.isNotEmpty()) "Les éléments qui majorent possiblement la situation sont : ${aggravants.joinToString(", ")}." else ""
        val prot = if (protecteurs.isNotEmpty()) "Les éléments plutôt favorables à ce stade sont : ${protecteurs.joinToString(", ")}." else ""
        return listOf(intro, "Hypothèse de lecture : $hypothese", aggr, prot).filter { it.isNotBlank() }.joinToString("\n\n")
    }

    // ═══════════════════════════════════════════════════════════
    // ORIGINES POSSIBLES
    // ═══════════════════════════════════════════════════════════

    fun genererOriginesPossibles(
        nomChien: String,
        axe: Axe,
        peur: Int,
        attachement: Int,
        impulsivite: Int,
        reactivite: Int,
        reponsesChoix: Map<String, Int>
    ): String {
        val nom = nomChienAffiche(nomChien)
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        if (maxAxe <= 30) return "$nom semble évoluer dans un équilibre global satisfaisant. Aucune origine comportementale particulière ne ressort à ce stade."

        return when (axe) {
            Axe.PEUR -> buildString {
                append("La sensibilité émotionnelle de $nom peut avoir plusieurs origines. ")
                append("Une socialisation précoce limitée — peu d'expositions variées pendant les premières semaines de vie — est souvent impliquée. ")
                append("Des expériences négatives passées, même ponctuelles, peuvent aussi laisser une empreinte durable sur la façon dont un chien perçoit son environnement. ")
                if (estMaleEntier(reponsesChoix)) append("Chez un mâle entier, le niveau hormonal peut parfois amplifier la vigilance et les réactions de prudence. ")
                if (reponsesChoix["age"] == 0) append("À moins d'un an, la sensibilité est souvent plus marquée : le chien est encore en train de construire ses repères. ")
                append("Dans certains cas, une prédisposition génétique joue également un rôle, indépendamment du vécu.")
            }
            Axe.ATTACHEMENT -> buildString {
                append("Le besoin de proximité important de $nom peut s'expliquer de plusieurs façons. ")
                append("Un sevrage trop précoce ou une séparation difficile dans les premières semaines de vie peut fragiliser la construction de l'autonomie émotionnelle. ")
                append("Un environnement très fusionnel — où le chien a rarement été exposé à des moments seul — peut aussi renforcer ce besoin. ")
                if (reponsesChoix["suit_partout"] == 2) append("Le fait de suivre constamment son humain peut être à la fois un symptôme et un facteur qui entretient cette dépendance relationnelle. ")
                append("Ce type de fonctionnement n'est pas une question de caractère ou de caprice : il reflète souvent une vraie difficulté à trouver un appui interne quand la présence rassurante n'est pas là.")
            }
            Axe.IMPULSIVITE -> buildString {
                append("La difficulté de $nom à réguler son excitation peut avoir plusieurs origines. ")
                append("Certains chiens ont un seuil d'activation naturellement bas : ils montent vite en intensité et redescendent plus lentement, quelle que soit l'éducation reçue. ")
                if (reponsesChoix["race_categorie"]?.let { it == 0 || it == 5 || it == 7 } == true) append("Certaines familles de races ont été sélectionnées pour un niveau d'énergie et de réactivité élevé, ce qui peut peser sur la régulation émotionnelle. ")
                append("Un manque de structure dans les interactions quotidiennes — jeux trop longs, trop intenses, sans pauses — peut aussi entretenir ce mode de fonctionnement. ")
                if (reponsesChoix["age"] == 0 || reponsesChoix["age"] == 1) append("À un jeune âge, le contrôle inhibiteur est encore en développement : une certaine impulsivité est souvent normale avant 2-3 ans. ")
                append("L'impulsivité n'est généralement pas un manque de volonté ou d'intelligence, mais une difficulté à freiner une montée émotionnelle déjà enclenchée.")
            }
            Axe.REACTIVITE -> buildString {
                append("La réactivité de $nom peut s'expliquer par une combinaison de facteurs. ")
                append("Une socialisation incomplète — peu de rencontres avec d'autres chiens, des personnes variées ou des environnements différents pendant les périodes sensibles — est souvent en cause. ")
                if (reponsesChoix["a_deja_mordu"] == 1) append("Le fait qu'il y ait déjà eu morsure peut indiquer que la réactivité a franchi un seuil important, parfois associé à une histoire de confrontations mal vécues. ")
                if (estMaleEntier(reponsesChoix)) append("Chez un mâle entier, les interactions avec d'autres mâles peuvent être plus tendues en raison de l'influence hormonale. ")
                append("Des expériences négatives répétées face à certains déclencheurs peuvent aussi avoir conduit le chien à anticiper la menace et à réagir de manière préventive. ")
                append("Dans certains cas, la réactivité est aussi une façon de gérer une distance de sécurité quand le chien se sent dépassé.")
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
        val originesPossibles = genererOriginesPossibles(
            reponsesTexte["nom_chien"].orEmpty(),
            problemePrincipal,
            peur, attachement, impulsivite, reactivite,
            reponsesChoix
        )
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

    fun titreSectionPourQuestion(questionId: String): String = when (questionId) {
        "nom_chien", "age", "sterilise" -> "Informations générales"
        "race_categorie" -> "Votre chien"
        "peur_stimuli", "adaptation_changements", "comportement_exterieur", "reaction_peur" -> "Sensibilité et peur"
        "support_absences", "pendant_absence", "suit_partout", "autre_personne_apaise", "proprete_maison", "si_non_quand" -> "Attachement et séparation"
        "calmer_apres_excitation", "jeu_comportement", "vole_objets", "poursuite_mouvement" -> "Excitation et impulsivité"
        "reaction_inconnus", "reaction_chiens", "a_deja_mordu", "defense_ressources" -> "Réactivité"
        "a_un_probleme" -> "Pour aller plus loin"
        else -> "Contexte actuel"
    }

    fun aideQuestion(questionId: String): String? = when (questionId) {
        "race_categorie" -> "Choisissez la famille qui ressemble le plus à votre chien. Pour un croisé, choisissez la dernière option."
        "sterilise" -> "La stérilisation influence certains comportements comme la réactivité ou les tensions entre chiens."
        "adaptation_changements" -> "Pensez aux changements d'habitudes, de lieu, de rythme ou d'environnement."
        "comportement_exterieur" -> "Répondez en pensant surtout aux promenades et sorties habituelles."
        "reaction_peur" -> "Choisissez la réaction la plus fréquente quand votre chien est inquiet."
        "support_absences" -> "Pensez au moment où vous partez et au temps où votre chien reste seul."
        "pendant_absence" -> "Répondez selon ce que vous observez ou ce que l'on vous rapporte."
        "si_non_quand" -> "Cette question sert seulement si votre chien n'est pas toujours propre."
        "calmer_apres_excitation" -> "Après le jeu, une sortie, une visite ou un moment stimulant."
        "jeu_comportement" -> "Par exemple s'il mordille fort, saute, déborde ou a du mal à s'arrêter."
        "reaction_inconnus" -> "Par exemple : aboiements, évitement, tension, grognements."
        "reaction_chiens" -> "Par exemple : tension, aboiements, charge, évitement ou agitation."
        "a_deja_mordu" -> "Même une morsure ponctuelle compte."
        "signe_physique" -> "Même un doute peut être utile à signaler."
        else -> null
    }
}

fun questionsApplication(): List<Question> {
    return listOf(
        QuestionTexte("nom_chien", "Quel est le nom de votre chien ?"),

        QuestionChoix("race_categorie", "À quelle famille de races appartient votre chien ?",
            listOf("Chiens de berger & troupeau", "Retrievers & Spaniels", "Terriers",
                "Molosses & Dogues", "Chiens nordiques & primitifs", "Lévriers & Races de course",
                "Races naines & compagnie", "Chiens de chasse & pisteurs", "Croisé / Bâtard / Race inconnue")),

        QuestionChoix("age", "Quel âge a votre chien ?",
            listOf("Moins d'1 an", "Entre 1 et 3 ans", "Entre 4 et 7 ans", "8 ans et +")),

        // 0 = mâle stérilisé, 1 = femelle stérilisée, 2 = mâle entier, 3 = femelle entière
        QuestionChoix("sterilise", "Votre chien est :",
            listOf("Un mâle stérilisé", "Une femelle stérilisée", "Un mâle entier", "Une femelle entière")),

        QuestionChoix("peur_stimuli", "Votre chien montre-t-il de la peur face à certaines situations ?",
            listOf("Jamais", "Parfois", "Souvent"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("adaptation_changements", "Votre chien a-t-il du mal à s'adapter aux changements ?",
            listOf("Non", "Un peu", "Oui"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("comportement_exterieur", "En promenade ou à l'extérieur, votre chien est plutôt :",
            listOf("Calme et détendu", "Excité / difficile à canaliser", "Craintif / en évitement"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_peur", "Quand votre chien a peur, il réagit plutôt comment ?",
            listOf("Il récupère vite", "Il se cache / fuit", "Il panique ou devient agressif"),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("support_absences", "Comment votre chien vit-il vos absences ?",
            listOf("Bien", "Moyennement", "Difficilement"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("pendant_absence", "Pendant vos absences, votre chien :",
            listOf("Reste calme", "Peut vocaliser ou s'agiter", "Détruit / aboie / panique"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("suit_partout", "Votre chien vous suit-il partout dans la maison ?",
            listOf("Non", "Parfois", "Il ne me quitte pratiquement pas"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("autre_personne_apaise", "La présence d'une autre personne suffit-elle à l'apaiser ?",
            listOf("Oui", "Il n'est vraiment apaisé qu'avec moi", "Je ne sais pas"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 2, 0)),

        QuestionChoix("proprete_maison", "Votre chien est-il propre à la maison ?",
            listOf("Oui", "Non", "Parfois"),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 2, 1)),

        QuestionChoix("si_non_quand", "Si votre chien n'est pas toujours propre, dans quelles situations ?",
            listOf("Lors de vos absences", "En votre présence", "La nuit", "De manière aléatoire")),

        QuestionChoix("calmer_apres_excitation", "Votre chien a-t-il du mal à se calmer après un moment excitant ?",
            listOf("Non", "Parfois", "Oui"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("jeu_comportement", "Quand il joue, votre chien :",
            listOf("Reste contrôlé", "Peut beaucoup s'exciter", "Les jeux deviennent difficiles à contrôler"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("vole_objets", "Votre chien vole-t-il de la nourriture ou des objets ?",
            listOf("Non", "Parfois", "Souvent"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("poursuite_mouvement", "Votre chien poursuit-il facilement ce qui bouge ?",
            listOf("Non", "Parfois", "Souvent"),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_inconnus", "Votre chien réagit-il difficilement aux personnes inconnues ?",
            listOf("Non", "Parfois", "Souvent"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_chiens", "Votre chien réagit-il difficilement aux autres chiens ?",
            listOf("Non", "Parfois", "Souvent"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("a_deja_mordu", "Votre chien a-t-il déjà mordu ?",
            listOf("Non", "Oui"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 4), poids = 2, signalCritique = true),

        QuestionChoix("defense_ressources", "Votre chien grogne-t-il ou devient-il tendu quand on s'approche de sa gamelle, de ses jouets ou de son couchage ?",
            listOf("Non, jamais", "Parfois, dans certaines situations", "Oui, c'est fréquent"),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 2, 4), signalAlerte = true),

        QuestionChoix("a_un_probleme", "Y a-t-il un comportement particulier qui vous préoccupe en ce moment ?",
            listOf("Oui, j'aimerais comprendre", "Non, tout va bien")),

        QuestionChoix("apparition", "Le comportement qui vous préoccupe est apparu :",
            listOf("Progressivement", "Du jour au lendemain, sans raison apparente", "Je ne sais pas vraiment")),

        QuestionChoix("situation_principale", "Il apparaît principalement :",
            listOf("Dans beaucoup de situations", "Surtout en votre absence", "Surtout à l'extérieur", "Surtout en votre présence")),

        QuestionChoix("duree_probleme", "Depuis combien de temps observez-vous ce comportement ?",
            listOf("Moins d'1 semaine", "1 à 4 semaines", "Plusieurs mois", "Depuis toujours")),

        QuestionChoix("evolution_probleme", "Ce comportement :",
            listOf("S'améliore", "Reste stable", "S'aggrave")),

        QuestionChoix("frequence_probleme", "À quelle fréquence cela se produit-il ?",
            listOf("Rarement", "Quelques fois par semaine", "Tous les jours", "Plusieurs fois par jour")),

        QuestionChoix("intensite_probleme", "Quand cela arrive, c'est plutôt :",
            listOf("Gérable facilement", "Gênant", "Difficile à gérer", "Perte de contrôle / dangereux")),

        QuestionChoix("generalisation_probleme", "Le comportement arrive plutôt :",
            listOf("Dans une situation bien précise", "Dans plusieurs situations différentes", "Dans la plupart des situations")),

        QuestionChoix("changement_recent", "Y a-t-il eu récemment un changement important dans sa vie ?",
            listOf("Aucun changement", "Un changement léger", "Un changement important (déménagement, bébé, séparation...)")),

        QuestionChoix("signe_physique", "Avez-vous remarqué un changement physique chez votre chien ces derniers temps ?",
            listOf("Non, rien de particulier", "Oui, il semble plus fatigué qu'avant",
                "Oui, il semble avoir mal ou être gêné dans ses mouvements", "Oui, autre chose a changé physiquement"))
    )
}