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
        if (firstScore <= 30) return tr("Compagnon bien ancré", "Well-grounded companion", "Gut geerdeter Begleiter")
        return when {
            first == Axe.PEUR && second == Axe.REACTIVITE -> tr("Explorateur sensible", "Sensitive explorer", "Sensibler Entdecker")
            first == Axe.ATTACHEMENT && second == Axe.PEUR -> tr("Cœur collé-serré", "Close-at-heart", "Anhängliches Herz")
            first == Axe.ATTACHEMENT && second == Axe.REACTIVITE -> tr("Très attaché", "Very attached", "Sehr anhänglich")
            first == Axe.IMPULSIVITE && second == Axe.REACTIVITE -> tr("Débordant d'énergie", "Bursting with energy", "Voller Energie")
            first == Axe.IMPULSIVITE && second == Axe.PEUR -> tr("Vif et sensible", "Lively and sensitive", "Lebhaft und sensibel")
            first == Axe.REACTIVITE -> tr("Chien très réactif", "Highly reactive dog", "Sehr reaktiver Hund")
            first == Axe.PEUR -> tr("Émotif vigilant", "Watchful and emotional", "Wachsam und emotional")
            first == Axe.ATTACHEMENT -> tr("Fusionnel", "Fusional", "Eng verbunden")
            first == Axe.IMPULSIVITE -> tr("Moteur sensible", "Sensitive engine", "Sensibler Wirbelwind")
            else -> tr("Profil équilibré", "Balanced profile", "Ausgeglichenes Profil")
        }
    }

    fun phraseHumaineProfil(nomChien: String, scoreGlobal: Int, profilType: String, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        val nom = nomChienAffiche(nomChien)
        return when {
            maxAxe <= 30 -> tr("$nom semble évoluer sur une base globalement stable et adaptée.", "$nom seems to be evolving on an overall stable and well-adapted basis.", "$nom scheint sich auf einer insgesamt stabilen und angemessenen Grundlage zu entwickeln.")
            maxAxe <= 60 -> tr("$nom présente quelques points de fragilité, sans que cela ne prenne toute la place.", "$nom shows some fragile points, without them taking over entirely.", "$nom zeigt einige Schwachstellen, ohne dass diese alles bestimmen.")
            else -> tr("$nom semble actuellement en difficulté dans certaines situations. Cette lecture reste indicative et gagnerait à être confrontée à l'observation réelle de son quotidien.", "$nom seems to be currently struggling in some situations. This reading remains indicative and should be compared with real daily observation.", "$nom scheint derzeit in manchen Situationen Schwierigkeiten zu haben. Diese Lesart ist nur ein Anhaltspunkt und sollte mit der tatsächlichen Beobachtung seines Alltags abgeglichen werden.")
        }
    }

    fun genererProfilGlobal(nomChien: String, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): ProfilGlobal {
        val scoreGlobal = calculerScoreGlobal(peur, attachement, impulsivite, reactivite)
        val profilType = determinerProfilType(peur, attachement, impulsivite, reactivite)
        val ph = phraseHumaineProfil(nomChien, scoreGlobal, profilType, peur, attachement, impulsivite, reactivite)
        return when {
            peur <= 30 && attachement <= 30 && impulsivite <= 30 && reactivite <= 30 ->
                ProfilGlobal(
                    tr("Profil globalement équilibré", "Overall balanced profile", "Insgesamt ausgeglichenes Profil"),
                    tr("Les réponses suggèrent un fonctionnement plutôt stable dans l'ensemble.", "The responses suggest a fairly stable overall functioning.", "Die Antworten deuten insgesamt auf eine eher stabile Funktionsweise hin."),
                    profilType, scoreGlobal, ph)
            peur >= 60 && reactivite >= 60 ->
                ProfilGlobal(
                    tr("Sensibilité émotionnelle et réactivité marquées", "Emotional sensitivity and marked reactivity", "Ausgeprägte emotionale Sensibilität und Reaktivität"),
                    tr("Le profil suggère une sensibilité importante, avec des réactions plus visibles lorsque certaines situations deviennent difficiles à gérer.", "The profile suggests significant sensitivity, with more visible reactions when certain situations become difficult to manage.", "Das Profil deutet auf eine hohe Sensibilität hin, mit deutlicheren Reaktionen, wenn bestimmte Situationen schwer zu bewältigen werden."),
                    profilType, scoreGlobal, ph)
            attachement >= 60 && peur >= 60 ->
                ProfilGlobal(
                    tr("Besoin de proximité avec fragilité émotionnelle", "Need for closeness with emotional fragility", "Bedürfnis nach Nähe mit emotionaler Verletzlichkeit"),
                    tr("Le fonctionnement évoque un besoin de repères relationnels forts associé à une sensibilité émotionnelle notable.", "The profile suggests a need for strong relational anchors combined with notable emotional sensitivity.", "Die Funktionsweise deutet auf ein starkes Bedürfnis nach Halt in der Beziehung hin, verbunden mit einer deutlichen emotionalen Sensibilität."),
                    profilType, scoreGlobal, ph)
            attachement >= 60 && reactivite >= 60 ->
                ProfilGlobal(
                    tr("Proximité importante avec réactions intenses", "Strong closeness need with intense reactions", "Großes Bedürfnis nach Nähe mit intensiven Reaktionen"),
                    tr("Le profil semble associer besoin de proximité et réactions plus marquées dans certains contextes.", "The profile seems to combine a need for closeness with more marked reactions in certain contexts.", "Das Profil scheint ein Bedürfnis nach Nähe mit deutlicheren Reaktionen in bestimmten Situationen zu verbinden."),
                    profilType, scoreGlobal, ph)
            impulsivite >= 60 && reactivite >= 60 ->
                ProfilGlobal(
                    tr("Réactions rapides avec difficulté de contrôle", "Fast reactions with difficulty in control", "Schnelle Reaktionen mit Kontrollschwierigkeiten"),
                    tr("Le profil suggère des montées émotionnelles rapides, avec une gestion plus difficile de certaines stimulations.", "The profile suggests rapid emotional escalation, with more difficult management of certain stimulations.", "Das Profil deutet auf schnelle emotionale Anstiege hin, mit einem schwierigeren Umgang mit bestimmten Reizen."),
                    profilType, scoreGlobal, ph)
            impulsivite >= 60 && peur >= 60 ->
                ProfilGlobal(
                    tr("Sensibilité avec régulation difficile", "Sensitivity with difficult regulation", "Sensibilität mit schwieriger Regulation"),
                    tr("Le fonctionnement évoque à la fois une sensibilité émotionnelle et une difficulté à retrouver rapidement l'équilibre.", "The profile suggests both emotional sensitivity and difficulty returning quickly to balance.", "Die Funktionsweise deutet sowohl auf emotionale Sensibilität als auch auf Schwierigkeiten hin, schnell wieder ins Gleichgewicht zu finden."),
                    profilType, scoreGlobal, ph)
            reactivite >= 60 ->
                ProfilGlobal(
                    tr("Réactivité plus marquée", "More marked reactivity", "Ausgeprägtere Reaktivität"),
                    tr("Le profil suggère une tendance à réagir fortement à certains éléments de l'environnement.", "The profile suggests a tendency to react strongly to certain environmental elements.", "Das Profil deutet auf eine Neigung hin, stark auf bestimmte Elemente der Umgebung zu reagieren."),
                    profilType, scoreGlobal, ph)
            attachement >= 60 ->
                ProfilGlobal(
                    tr("Besoin de proximité plus important", "Greater need for closeness", "Größeres Bedürfnis nach Nähe"),
                    tr("Les réponses font ressortir un besoin de proximité plus marqué que la moyenne.", "The responses highlight a more marked need for closeness than average.", "Die Antworten zeigen ein überdurchschnittlich ausgeprägtes Bedürfnis nach Nähe."),
                    profilType, scoreGlobal, ph)
            impulsivite >= 60 ->
                ProfilGlobal(
                    tr("Régulation plus difficile", "More difficult regulation", "Schwierigere Regulation"),
                    tr("Le profil évoque une difficulté dans la gestion de l'excitation et des retours au calme.", "The profile suggests difficulty managing excitement and returning to calm.", "Das Profil deutet auf Schwierigkeiten beim Umgang mit Erregung und beim Zurückfinden zur Ruhe hin."),
                    profilType, scoreGlobal, ph)
            peur >= 60 ->
                ProfilGlobal(
                    tr("Sensibilité émotionnelle plus marquée", "More marked emotional sensitivity", "Ausgeprägtere emotionale Sensibilität"),
                    tr("Les réponses suggèrent une sensibilité plus importante à certains changements ou situations.", "The responses suggest greater sensitivity to certain changes or situations.", "Die Antworten deuten auf eine höhere Sensibilität gegenüber bestimmten Veränderungen oder Situationen hin."),
                    profilType, scoreGlobal, ph)
            else ->
                ProfilGlobal(
                    tr("Profil à nuancer", "Profile to be nuanced", "Differenziert zu betrachtendes Profil"),
                    tr("Les réponses font apparaître quelques points de vigilance, sans qu'un aspect ne domine clairement.", "The responses reveal some points of vigilance, without any single aspect clearly dominating.", "Die Antworten zeigen einige Punkte, die Aufmerksamkeit verdienen, ohne dass ein Aspekt klar überwiegt."),
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
            NiveauSituation.STABLE -> tr("À ce stade, la situation semble plutôt stable pour $nom.", "At this stage, the situation seems fairly stable for $nom.", "Im Moment scheint die Situation für $nom eher stabil zu sein.")
            NiveauSituation.A_TRAVAILLER -> tr("La situation mérite probablement d'être travaillée de manière progressive pour $nom.", "The situation probably deserves to be worked on progressively for $nom.", "An der Situation von $nom sollte wahrscheinlich schrittweise gearbeitet werden.")
            NiveauSituation.SENSIBLE -> tr("La situation paraît plus sensible pour $nom et justifie une attention particulière.", "The situation seems more sensitive for $nom and warrants particular attention.", "Die Situation von $nom wirkt heikler und verdient besondere Aufmerksamkeit.")
        }
    }

    fun genererRaisonSituation(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse): String {
        val raisons = mutableListOf<String>()
        if (reponsesChoix["duree_probleme"] == 0) raisons += tr("Le caractère très récent du comportement invite à une vigilance particulière.", "The very recent nature of the behaviour calls for particular vigilance.", "Dass das Verhalten erst ganz neu ist, erfordert besondere Aufmerksamkeit.")
        if (reponsesChoix["evolution_probleme"] == 2) raisons += tr("Le fait que cela semble s'aggraver peut indiquer que le problème prend plus de place.", "The fact that this seems to be getting worse may indicate the problem is taking more space.", "Dass es sich zu verschlimmern scheint, kann darauf hindeuten, dass das Problem mehr Raum einnimmt.")
        if (contexte.physique >= 4) raisons += tr("Des signes physiques ou une gêne possible invitent à la prudence.", "Physical signs or a possible discomfort call for caution.", "Körperliche Anzeichen oder ein mögliches Unwohlsein mahnen zur Vorsicht.")
        return raisons.firstOrNull() ?: tr("L'ensemble des réponses invite surtout à avancer progressivement.", "Overall, the responses suggest moving forward gradually.", "Die Antworten insgesamt legen vor allem ein schrittweises Vorgehen nahe.")
    }

    fun genererConseilsPratiquesPersonnalises(nomChien: String, reponsesChoix: Map<String, Int>,
                                              peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): List<String> {
        val nom = nomChienAffiche(nomChien)
        val scoreMax = maxOf(peur, attachement, impulsivite, reactivite)
        if (scoreMax == 0) return listOf(
            tr("Continuer l'observation du quotidien et maintenir les repères déjà en place.", "Continue daily observation and maintain the routines already in place.", "Beobachten Sie den Alltag weiter und behalten Sie die bestehenden Gewohnheiten bei.")
        )
        val conseils = mutableListOf<String>()
        if (reponsesChoix["age"] == 0 && reponsesChoix["proprete_type"] != null) conseils += tr("Renforcez une routine de sorties régulières (après les repas, les siestes et le jeu) et récompensez calmement chaque fois que $nom fait ses besoins dehors — la patience est essentielle à cet âge.", "Reinforce a regular outing routine (after meals, naps and play) and reward calmly each time $nom relieves itself outside — patience is key at this age.", "Festigen Sie eine Routine mit regelmäßigen Gassigängen (nach dem Fressen, nach dem Schlafen und nach dem Spielen) und belohnen Sie $nom ruhig, jedes Mal wenn er sich draußen löst – in diesem Alter ist Geduld entscheidend.")
        if (estMaleEntier(reponsesChoix) && reponsesChoix["age"] != 0 && (reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2)) conseils += tr("Notez si les traces sont de petites quantités sur des surfaces verticales plutôt qu'une vidange complète de la vessie — cela aide à confirmer un marquage territorial, et la stérilisation peut être discutée avec votre vétérinaire.", "Note whether the marks are small quantities on vertical surfaces rather than full-bladder emptying — this helps confirm territorial marking, and neutering can be discussed with your vet.", "Achten Sie darauf, ob es sich um kleine Mengen an senkrechten Flächen handelt statt um eine vollständige Blasenentleerung – das hilft, eine Reviermarkierung zu bestätigen, und eine Kastration kann mit Ihrem Tierarzt besprochen werden.")
        if (reponsesChoix["marquage_habitude_post_sterilisation"] == 0) conseils += tr("Notez la fréquence et les lieux de ce marquage pour identifier d'éventuels déclencheurs encore présents (odeurs, changement récent) — même si l'origine est devenue une habitude, un accompagnement comportemental ciblé peut aider $nom à désapprendre ce geste précis.", "Note the frequency and locations of this marking to identify any remaining triggers (smells, recent change) — even if the origin has become a habit, targeted behavioural support can help $nom unlearn this specific gesture.", "Notieren Sie Häufigkeit und Orte dieser Markierungen, um mögliche noch vorhandene Auslöser zu erkennen (Gerüche, kürzliche Veränderung) – auch wenn daraus eine Gewohnheit geworden ist, kann eine gezielte Verhaltensbegleitung $nom helfen, dieses Verhalten zu verlernen.")
        if (reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2) conseils += tr("Notez les lieux et horaires de chaque accident, ainsi que le comportement de $nom au moment d'uriner — ces observations aideront votre vétérinaire à orienter son diagnostic.", "Write down the location and time of each accident, along with $nom's behavior while urinating — these notes will help your vet's diagnosis.", "Notieren Sie Ort und Uhrzeit jedes Missgeschicks sowie das Verhalten von $nom beim Urinieren – diese Beobachtungen helfen Ihrem Tierarzt bei der Diagnose.")
        if (reponsesChoix["age"] == 3 && (reponsesChoix["senior_desorientation"] == 2 || reponsesChoix["senior_vocalise_nocturne"] == 2)) conseils += tr("Gardez l'environnement de $nom aussi stable et prévisible que possible — évitez de déplacer meubles, gamelles et couchage.", "Keep $nom's environment as stable and predictable as possible — avoid moving furniture, food and resting spots.", "Halten Sie die Umgebung von $nom so stabil und vorhersehbar wie möglich – vermeiden Sie es, Möbel, Näpfe und Liegeplatz umzustellen.")
        if (estMaleEntier(reponsesChoix) && reactivite >= 50) conseils += tr("Chez un mâle entier, la réactivité peut être amplifiée par les hormones. Un avis vétérinaire sur la castration peut valoir la peine d'être discuté.", "In an intact male, reactivity can be amplified by hormones. A vet's opinion on castration may be worth discussing.", "Bei einem unkastrierten Rüden kann die Reaktivität durch Hormone verstärkt werden. Es kann sich lohnen, eine Kastration mit dem Tierarzt zu besprechen.")
        if (estFemelleEntiere(reponsesChoix) && (peur >= 50 || impulsivite >= 50)) conseils += tr("Chez une femelle entière, certains comportements peuvent varier selon le cycle. Observer si les comportements s'intensifient à certaines périodes peut donner des repères utiles.", "In an intact female, some behaviours can vary with the cycle. Observing whether behaviours intensify at certain times can give useful landmarks.", "Bei einer unkastrierten Hündin können manche Verhaltensweisen je nach Zyklus schwanken. Zu beobachten, ob sie sich in bestimmten Phasen verstärken, kann hilfreiche Anhaltspunkte geben.")
        val scores = listOf(Axe.PEUR to peur, Axe.ATTACHEMENT to attachement, Axe.IMPULSIVITE to impulsivite, Axe.REACTIVITE to reactivite)
        val axesDominants = scores.filter { it.second == scoreMax }.map { it.first }
        if (axesDominants.size > 1) {
            val texteAxes = axesDominants.joinToString(tr(" et ", " and ", " und ")) {
                when (it) {
                    Axe.PEUR -> tr("la sensibilité émotionnelle", "emotional sensitivity", "die emotionale Sensibilität")
                    Axe.ATTACHEMENT -> tr("le besoin de proximité", "need for closeness", "das Bedürfnis nach Nähe")
                    Axe.IMPULSIVITE -> tr("la régulation de l'excitation", "excitement regulation", "die Regulation der Erregung")
                    Axe.REACTIVITE -> tr("la réactivité à l'environnement", "reactivity to the environment", "die Reaktivität auf die Umgebung")
                }
            }
            conseils += tr("Plusieurs dimensions semblent ressortir conjointement : $texteAxes. Une approche progressive, un axe après l'autre, paraît préférable.", "Several dimensions seem to stand out together: $texteAxes. A gradual approach, one axis at a time, seems preferable.", "Mehrere Dimensionen scheinen gleichzeitig hervorzutreten: $texteAxes. Ein schrittweises Vorgehen, eine Achse nach der anderen, erscheint sinnvoller.")
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

    fun explicationProbleme(axe: Axe, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int, reponsesChoix: Map<String, Int> = emptyMap()): String {
        if (reponsesChoix["age"] == 3 && (reponsesChoix["senior_desorientation"] == 2 || reponsesChoix["senior_vocalise_nocturne"] == 2)) return tr("Chez un chien âgé, la désorientation et les vocalises nocturnes inexpliquées peuvent refléter un vieillissement cérébral normal (déclin cognitif lié à l'âge) plutôt qu'un problème comportemental à corriger — un phénomène assez proche de ce que l'on observe parfois chez l'humain vieillissant.", "In an older dog, disorientation and unexplained night vocalizing can reflect normal brain aging (age-related cognitive decline) rather than a behavioral problem to correct — a phenomenon fairly close to what is sometimes observed in aging humans.", "Bei einem älteren Hund können Orientierungslosigkeit und unerklärliches nächtliches Bellen oder Winseln eher auf ein normales Altern des Gehirns (altersbedingter kognitiver Abbau) hinweisen als auf ein Verhaltensproblem, das korrigiert werden muss – ähnlich wie man es manchmal bei älteren Menschen beobachtet.")
        if (reponsesChoix["age"] == 0 && reponsesChoix["proprete_type"] != null) return tr("À moins d'un an, la propreté est souvent encore un apprentissage en cours plutôt qu'un problème comportemental. Le contrôle de la vessie et des intestins met du temps à se mettre en place, et certains accidents font partie normale de cet apprentissage.", "Under one year old, house-training is often still an ongoing learning process rather than a behavioral problem. Bladder and bowel control takes time to develop, and some accidents are a normal part of this learning curve.", "Unter einem Jahr ist die Stubenreinheit oft noch ein laufender Lernprozess und kein Verhaltensproblem. Die Kontrolle über Blase und Darm braucht Zeit, und einzelne Missgeschicke gehören zu diesem Lernen dazu.")
        if (reponsesChoix["marquage_habitude_post_sterilisation"] == 0) return tr("Ce marquage semble avoir débuté avant la stérilisation, à une période où les hormones jouaient un rôle. La cause hormonale d'origine a disparu, mais le geste s'est transformé en habitude acquise — il reste ancré même si la raison initiale n'existe plus. Ce type de marquage devenu habituel est souvent plus long à corriger qu'un marquage purement lié au stress ou aux hormones, car il faut désapprendre un geste répété plutôt que simplement réduire une source de tension.", "This marking seems to have started before neutering/spaying, at a time when hormones played a role. The original hormonal cause is gone, but the gesture has turned into a learned habit — it remains ingrained even though the initial reason no longer exists. This type of habitual marking is often longer to correct than marking purely linked to stress or hormones, since a repeated gesture needs to be unlearned rather than simply reducing a source of tension.", "Diese Markierung scheint vor der Kastration begonnen zu haben, in einer Zeit, in der Hormone eine Rolle spielten. Die ursprüngliche hormonelle Ursache ist verschwunden, doch das Verhalten hat sich zu einer erlernten Gewohnheit entwickelt – es bleibt bestehen, auch wenn der ursprüngliche Grund nicht mehr existiert. Eine solche zur Gewohnheit gewordene Markierung lässt sich oft langsamer korrigieren als eine rein stress- oder hormonbedingte, weil ein wiederholtes Verhalten verlernt werden muss, statt nur eine Spannungsquelle zu verringern.")
        if (estMaleEntier(reponsesChoix) && reponsesChoix["age"] != 0 && (reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2)) return tr("Chez un mâle entier, ce type de malpropreté peut correspondre à du marquage territorial plutôt qu'à une malpropreté classique — de petites quantités déposées sur des surfaces verticales, souvent déclenchées par la présence d'autres animaux ou des changements dans l'environnement, plutôt qu'un simple manque de contrôle de la vessie.", "In an intact male, this kind of house-soiling can correspond to territorial marking rather than classic uncleanliness — small amounts deposited on vertical surfaces, often triggered by the presence of other animals or changes in the environment, rather than a simple lack of bladder control.", "Bei einem unkastrierten Rüden kann diese Unsauberkeit eher einer Reviermarkierung entsprechen als einer klassischen Unsauberkeit – kleine Mengen an senkrechten Flächen, oft ausgelöst durch andere Tiere oder Veränderungen in der Umgebung, statt einer einfachen fehlenden Blasenkontrolle.")
        if (reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2) return tr("Cette malpropreté urinaire peut avoir des causes purement médicales (infection urinaire, calculs, incontinence liée à l'âge ou à la stérilisation, problème de prostate chez un mâle entier) qui produisent exactement les mêmes symptômes qu'un problème comportemental.", "This urinary house-soiling can have purely medical causes (urinary infection, bladder stones, age-related or post-neutering incontinence, prostate issues in an intact male) that produce the exact same symptoms as a behavioral problem.", "Diese Harn-Unsauberkeit kann rein medizinische Ursachen haben (Harnwegsinfektion, Blasensteine, alters- oder kastrationsbedingte Inkontinenz, Prostataprobleme beim unkastrierten Rüden), die genau dieselben Symptome hervorrufen wie ein Verhaltensproblem.")
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        if (maxAxe <= 30) return tr("Les éléments recueillis ne mettent pas en évidence de difficulté comportementale marquée à ce stade.", "The information collected does not highlight any marked behavioural difficulty at this stage.", "Die gesammelten Angaben zeigen derzeit keine ausgeprägte Verhaltensschwierigkeit.")
        return when (axe) {
            Axe.PEUR -> tr("Les réactions observées semblent s'inscrire dans une sensibilité émotionnelle relativement élevée. Dans ce type de fonctionnement, certains changements ou situations peuvent être perçus comme plus intenses ou difficiles à gérer.", "The observed reactions seem to fit within a relatively high emotional sensitivity. In this type of profile, certain changes or situations may be perceived as more intense or difficult to manage.", "Die beobachteten Reaktionen scheinen auf eine relativ hohe emotionale Sensibilität zurückzugehen. Bei dieser Funktionsweise können manche Veränderungen oder Situationen als intensiver oder schwerer zu bewältigen empfunden werden.")
            Axe.ATTACHEMENT -> tr("Les éléments recueillis suggèrent un besoin de proximité relativement important. Dans ce type de fonctionnement, l'autonomie émotionnelle peut être encore fragile, ce qui peut rendre certaines séparations ou absences plus difficiles à vivre.", "The information collected suggests a relatively strong need for closeness. In this type of profile, emotional independence can still be fragile, which may make certain separations or absences harder to cope with.", "Die gesammelten Angaben deuten auf ein relativ großes Bedürfnis nach Nähe hin. Bei dieser Funktionsweise kann die emotionale Selbstständigkeit noch zerbrechlich sein, wodurch manche Trennungen oder Abwesenheiten schwerer zu ertragen sind.")
            Axe.IMPULSIVITE -> tr("Les réponses évoquent une difficulté possible dans la régulation de l'excitation. Il ne s'agit généralement pas d'un manque de volonté, mais plutôt d'un seuil de montée émotionnelle rapidement atteint, avec un retour au calme plus lent.", "The responses suggest a possible difficulty in regulating excitement. This is generally not a lack of willpower, but rather a quickly-reached emotional activation threshold, with a slower return to calm.", "Die Antworten deuten auf mögliche Schwierigkeiten bei der Regulation der Erregung hin. Meist ist das kein mangelnder Wille, sondern eine schnell erreichte Schwelle des emotionalen Anstiegs, mit einer langsameren Rückkehr zur Ruhe.")
            Axe.REACTIVITE -> tr("Les éléments recueillis suggèrent une réactivité marquée face à certains éléments de son environnement. Ce type de réponse peut apparaître lorsque le chien se sent en tension, incertain ou dépassé dans certaines situations.", "The information collected suggests marked reactivity to certain elements of the environment. This type of response may appear when the dog feels tense, uncertain or overwhelmed in certain situations.", "Die gesammelten Angaben deuten auf eine ausgeprägte Reaktivität gegenüber bestimmten Elementen seiner Umgebung hin. Eine solche Reaktion kann auftreten, wenn sich der Hund in bestimmten Situationen angespannt, unsicher oder überfordert fühlt.")
        }
    }

    fun conseilPrincipal(axe: Axe, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): String {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        if (maxAxe <= 30) return tr("À ce stade, aucun axe de travail prioritaire ne se dégage clairement. L'objectif peut simplement être de maintenir un cadre stable, cohérent et prévisible.", "At this stage, no priority axis of work stands out clearly. The goal can simply be to maintain a stable, consistent and predictable framework.", "Im Moment zeichnet sich kein vorrangiger Arbeitsschwerpunkt klar ab. Ziel kann einfach sein, einen stabilen, stimmigen und vorhersehbaren Rahmen zu bewahren.")
        return when (axe) {
            Axe.ATTACHEMENT -> tr("Une première piste consiste à travailler progressivement les moments de séparation, en restant sur des durées très courtes et maîtrisées. L'objectif est de renforcer la capacité du chien à rester apaisé sans dépendre constamment de la présence humaine.", "A first step is to gradually work on moments of separation, keeping them very short and controlled. The aim is to strengthen the dog's ability to stay calm without constantly depending on human presence.", "Ein erster Ansatz besteht darin, schrittweise an Trennungsmomenten zu arbeiten, mit sehr kurzen und kontrollierten Zeitspannen. Ziel ist es, die Fähigkeit des Hundes zu stärken, ruhig zu bleiben, ohne ständig auf die Anwesenheit eines Menschen angewiesen zu sein.")
            Axe.PEUR -> tr("Il est généralement pertinent de respecter les seuils de tolérance du chien. Travailler à distance des éléments déclencheurs, dans des conditions calmes, permet souvent de favoriser une évolution progressive.", "It is generally relevant to respect the dog's tolerance thresholds. Working at a distance from triggering elements, in calm conditions, often helps to promote gradual progress.", "Es ist meist sinnvoll, die Toleranzschwellen des Hundes zu respektieren. In ruhigen Bedingungen und mit Abstand zu den Auslösern zu arbeiten, fördert oft eine schrittweise Entwicklung.")
            Axe.REACTIVITE -> tr("Une approche progressive basée sur la gestion de la distance et la réduction de la pression environnementale est souvent recommandée. L'objectif est de maintenir le chien dans une zone où il reste encore capable de traiter l'information.", "A gradual approach based on distance management and reducing environmental pressure is often recommended. The goal is to keep the dog in a zone where it can still process information.", "Häufig wird ein schrittweises Vorgehen empfohlen, das auf dem Umgang mit Abstand und der Verringerung des Drucks durch die Umgebung beruht. Ziel ist es, den Hund in einem Bereich zu halten, in dem er die Informationen noch verarbeiten kann.")
            Axe.IMPULSIVITE -> tr("Structurer les interactions avec des temps courts et des pauses régulières peut aider à améliorer la régulation. Le travail consiste surtout à favoriser des retours au calme fréquents et prévisibles.", "Structuring interactions with short sequences and regular pauses can help improve regulation. The work mainly consists of encouraging frequent and predictable returns to calm.", "Die Interaktionen mit kurzen Einheiten und regelmäßigen Pausen zu strukturieren, kann helfen, die Regulation zu verbessern. Es geht vor allem darum, häufige und vorhersehbare Rückkehr zur Ruhe zu fördern.")
        }
    }

    fun genererPlanAction(axe: Axe, reponsesChoix: Map<String, Int>, nomChien: String): PlanAction {
        val aFaire = mutableListOf<String>()
        val aEviter = mutableListOf<String>()
        val aObserver = mutableListOf<String>()
        when (axe) {
            Axe.ATTACHEMENT -> {
                aFaire += tr("Réduire la charge émotionnelle autour des départs et des retours.", "Reduce the emotional load around departures and returns.", "Die emotionale Aufladung rund um das Weggehen und Wiederkommen verringern.")
                aFaire += tr("Proposer progressivement de petits moments d'autonomie dans des situations faciles.", "Gradually introduce small moments of independence in easy situations.", "Nach und nach kleine Momente der Selbstständigkeit in einfachen Situationen anbieten.")
                aFaire += tr("Commencer par des absences très courtes et maîtrisées.", "Start with very short, controlled absences.", "Mit sehr kurzen und kontrollierten Abwesenheiten beginnen.")
                aEviter += tr("Les rituels de départ ou de retrouvailles très marqués.", "Highly marked departure or reunion rituals.", "Sehr betonte Abschieds- oder Begrüßungsrituale.")
                aEviter += tr("Les absences trop longues ou trop difficiles d'emblée.", "Absences that are too long or too difficult from the start.", "Zu lange oder von Anfang an zu schwierige Abwesenheiten.")
                aEviter += tr("Les réactions émotionnelles face aux manifestations liées à l'absence.", "Emotional reactions to absence-related behaviour.", "Emotionale Reaktionen auf Anzeichen, die mit der Abwesenheit zusammenhängen.")
                aObserver += tr("Le moment précis où la tension apparaît.", "The exact moment when tension appears.", "Den genauen Moment, in dem die Anspannung auftritt.")
                aObserver += tr("La capacité du chien à se poser seul dans les moments neutres.", "The dog's ability to settle alone during neutral moments.", "Die Fähigkeit des Hundes, in neutralen Momenten allein zur Ruhe zu kommen.")
                aObserver += tr("L'évolution lorsque les interactions deviennent plus prévisibles.", "Progress when interactions become more predictable.", "Die Entwicklung, wenn die Interaktionen vorhersehbarer werden.")
            }
            Axe.PEUR -> {
                aFaire += tr("Travailler à distance suffisante pour que le chien reste encore calme.", "Work at enough distance for the dog to remain calm.", "Mit so viel Abstand arbeiten, dass der Hund noch ruhig bleibt.")
                aFaire += tr("Laisser le chien observer sans le contraindre.", "Let the dog observe without forcing it.", "Den Hund beobachten lassen, ohne ihn zu zwingen.")
                aFaire += tr("Créer des expériences positives dans des contextes maîtrisés.", "Create positive experiences in controlled conditions.", "Positive Erfahrungen in kontrollierten Situationen schaffen.")
                aEviter += tr("Forcer le chien à affronter ce qui l'inquiète.", "Forcing the dog to face what worries it.", "Den Hund zwingen, sich dem zu stellen, was ihn beunruhigt.")
                aEviter += tr("Réduire trop vite la distance.", "Reducing the distance too quickly.", "Den Abstand zu schnell verringern.")
                aEviter += tr("Maintenir le chien dans une situation où il est déjà en difficulté.", "Keeping the dog in a situation where it is already struggling.", "Den Hund in einer Situation halten, in der er bereits überfordert ist.")
                aObserver += tr("La distance à laquelle la tension apparaît.", "The distance at which tension appears.", "Den Abstand, ab dem die Anspannung auftritt.")
                aObserver += tr("Les signaux précoces de stress.", "Early stress signals.", "Frühe Stresssignale.")
                aObserver += tr("Les contextes dans lesquels il reste à l'aise.", "The contexts in which the dog remains comfortable.", "Die Situationen, in denen er entspannt bleibt.")
            }
            Axe.IMPULSIVITE -> {
                aFaire += tr("Structurer les interactions avec des séquences courtes et des pauses.", "Structure interactions with short sequences and pauses.", "Die Interaktionen mit kurzen Abschnitten und Pausen strukturieren.")
                aFaire += tr("Interrompre calmement les situations où l'excitation monte trop.", "Calmly interrupt situations where excitement rises too high.", "Situationen, in denen die Erregung zu stark steigt, ruhig unterbrechen.")
                aFaire += tr("Valoriser davantage les moments de calme.", "Reward calm moments more.", "Ruhige Momente stärker belohnen.")
                aEviter += tr("Les interactions trop longues ou trop stimulantes.", "Interactions that are too long or too stimulating.", "Zu lange oder zu aufregende Interaktionen.")
                aEviter += tr("Répondre à l'excitation par plus d'excitation.", "Responding to excitement with more excitement.", "Auf Erregung mit noch mehr Erregung reagieren.")
                aEviter += tr("Attendre le débordement complet avant d'agir.", "Waiting for complete overflowing before acting.", "Warten, bis der Hund völlig überdreht ist, bevor man handelt.")
                aObserver += tr("La rapidité de montée en excitation.", "The speed at which excitement escalates.", "Wie schnell die Erregung steigt.")
                aObserver += tr("Le temps nécessaire pour retrouver le calme.", "The time needed to return to calm.", "Die Zeit, die er braucht, um wieder zur Ruhe zu finden.")
                aObserver += tr("Les situations qui déclenchent le plus vite les débordements.", "The situations that trigger overflowing most quickly.", "Die Situationen, die am schnellsten zum Überdrehen führen.")
            }
            Axe.REACTIVITE -> {
                aFaire += tr("Augmenter la distance avec les déclencheurs pour rester dans une zone gérable.", "Increase distance from triggers to stay in a manageable zone.", "Den Abstand zu den Auslösern vergrößern, um in einem handhabbaren Bereich zu bleiben.")
                aFaire += tr("Choisir des environnements plus faciles.", "Choose easier environments.", "Einfachere Umgebungen wählen.")
                aFaire += tr("Travailler dans des situations où le chien peut encore observer sans réagir.", "Work in situations where the dog can still observe without reacting.", "In Situationen arbeiten, in denen der Hund noch beobachten kann, ohne zu reagieren.")
                aEviter += tr("Les confrontations directes ou trop rapprochées.", "Direct or too-close confrontations.", "Direkte oder zu nahe Konfrontationen.")
                aEviter += tr("Les situations que le chien ne peut pas gérer.", "Situations the dog cannot manage.", "Situationen, die der Hund nicht bewältigen kann.")
                aEviter += tr("Insister une fois la réaction enclenchée.", "Insisting once the reaction has started.", "Beharren, wenn die Reaktion bereits ausgelöst ist.")
                aObserver += tr("Les déclencheurs précis et leur intensité.", "The specific triggers and their intensity.", "Die genauen Auslöser und ihre Intensität.")
                aObserver += tr("La distance à laquelle le chien bascule.", "The distance at which the dog tips over.", "Den Abstand, ab dem der Hund kippt.")
                aObserver += tr("Les signaux annonciateurs juste avant la réaction.", "Warning signals just before the reaction.", "Die Warnsignale kurz vor der Reaktion.")
            }
        }
        if (reponsesChoix["signe_physique"] == 2 || reponsesChoix["signe_physique"] == 3)
            aFaire += tr("Prévoir un avis vétérinaire pour écarter une cause physique associée.", "Plan a vet visit to rule out an associated physical cause.", "Einen Tierarzt aufsuchen, um eine begleitende körperliche Ursache auszuschließen.")
        return PlanAction(aFaire.distinct().take(3), aEviter.distinct().take(3), aObserver.distinct().take(3))
    }

    fun genererMessageAide(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, niveauSituation: NiveauSituation, nomChien: String): String? {
        val nom = nomChienAffiche(nomChien)
        return when {
            reponsesChoix["a_deja_mordu"] == 1 && reponsesChoix["cible_agression"] == 1 -> tr("Une morsure envers un autre animal a été signalée. Cela évoque souvent un manque de sociabilisation ou une cohabitation à reprendre autrement — un comportementaliste canin peut vous aider à reconstruire une présentation plus progressive et sécurisée entre les animaux.", "A bite toward another animal has been reported. This often points to a lack of socialization or a cohabitation that needs to be reframed — a dog behaviourist can help you rebuild a safer, more gradual introduction between animals.", "Es wurde ein Biss gegenüber einem anderen Tier gemeldet. Das deutet oft auf mangelnde Sozialisierung oder ein Zusammenleben hin, das neu aufgebaut werden sollte – eine Fachperson für Hundeverhalten kann Ihnen helfen, eine schrittweisere und sicherere Zusammenführung der Tiere zu gestalten.")
            reponsesChoix["a_deja_mordu"] == 1 -> tr("Le fait qu'il y ait déjà eu morsure envers une personne justifie de ne pas rester seul avec cette situation. Un accompagnement professionnel individualisé est recommandé pour $nom.", "The fact that there has already been a bite toward a person means this situation should not be handled alone. Individual professional support is recommended for $nom.", "Da es bereits einen Biss gegenüber einem Menschen gab, sollten Sie mit dieser Situation nicht allein bleiben. Eine individuelle professionelle Begleitung wird für $nom empfohlen.")
            reponsesChoix["age"] == 3 && (reponsesChoix["senior_desorientation"] == 2 || reponsesChoix["senior_vocalise_nocturne"] == 2) -> tr("Compte tenu de l'âge de $nom et des signes décrits, un bilan vétérinaire est recommandé en priorité pour écarter ou confirmer un déclin cognitif lié à l'âge avant d'envisager une approche comportementale.", "Given $nom's age and the signs described, a veterinary check-up is recommended as a priority to rule out or confirm age-related cognitive decline before considering a behavioural approach.", "Angesichts des Alters von $nom und der beschriebenen Anzeichen wird vorrangig eine tierärztliche Untersuchung empfohlen, um einen altersbedingten kognitiven Abbau auszuschließen oder zu bestätigen, bevor an einen verhaltensbezogenen Ansatz gedacht wird.")
            reponsesChoix["age"] == 0 && reponsesChoix["proprete_type"] != null -> tr("Ne consultez que si l'apprentissage de la propreté semble anormalement long à se mettre en place au-delà de quelques mois, ou si vous remarquez de la douleur, du sang, ou des efforts inhabituels quand $nom fait ses besoins.", "Consult a vet only if house-training seems unusually slow to settle beyond a few months, or if you notice pain, blood, or straining while $nom relieves itself.", "Suchen Sie nur dann einen Tierarzt auf, wenn die Stubenreinheit über einige Monate hinaus ungewöhnlich lange braucht oder wenn Sie Schmerzen, Blut oder ungewöhnliches Pressen bemerken, wenn $nom sich löst.")
            reponsesChoix["marquage_habitude_post_sterilisation"] == 0 -> tr("Ce marquage est devenu une habitude acquise plutôt qu'un comportement hormonal — un comportementaliste canin peut aider $nom à désapprendre ce geste précis, ce qui prend souvent plus de temps qu'un marquage lié au stress ou aux hormones.", "This marking has become a learned habit rather than a hormonal behaviour — a dog behaviourist can help $nom unlearn this specific gesture, which often takes longer than addressing stress- or hormone-related marking.", "Diese Markierung ist eher zu einer erlernten Gewohnheit als zu einem hormonellen Verhalten geworden – eine Fachperson für Hundeverhalten kann $nom helfen, dieses Verhalten zu verlernen, was oft länger dauert als bei einer stress- oder hormonbedingten Markierung.")
            estMaleEntier(reponsesChoix) && reponsesChoix["age"] != 0 && (reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2) -> tr("Si le marquage est confirmé, une visite vétérinaire reste utile pour écarter une cause urinaire ou prostatique et discuter de la stérilisation comme option pour $nom.", "If marking is confirmed, a vet visit can still be useful to rule out a urinary or prostate cause and to discuss neutering as an option for $nom.", "Wenn sich die Markierung bestätigt, bleibt ein Tierarztbesuch sinnvoll, um eine Harnwegs- oder Prostataursache auszuschließen und eine Kastration als Option für $nom zu besprechen.")
            reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2 -> tr("Un bilan vétérinaire est recommandé en priorité pour $nom pour écarter une infection urinaire, des calculs ou une incontinence avant d'envisager une approche comportementale.", "A veterinary check-up is recommended as a priority for $nom to rule out a urinary infection, bladder stones or incontinence before considering a behavioural approach.", "Für $nom wird vorrangig eine tierärztliche Untersuchung empfohlen, um eine Harnwegsinfektion, Blasensteine oder Inkontinenz auszuschließen, bevor an einen verhaltensbezogenen Ansatz gedacht wird.")
            contexte.physique >= 4 -> tr("Certains éléments font penser qu'une gêne, une douleur ou une composante physique pourrait participer au problème. Un avis vétérinaire est recommandé pour $nom.", "Some elements suggest that discomfort, pain or a physical component may be contributing to the problem. A vet's opinion is recommended for $nom.", "Einige Anzeichen lassen vermuten, dass ein Unwohlsein, ein Schmerz oder eine körperliche Ursache zum Problem beitragen könnte. Für $nom wird ein tierärztlicher Rat empfohlen.")
            reponsesChoix["apparition"] == 1 -> tr("Lorsque des comportements apparaissent brutalement, il est prudent d'écarter d'abord une cause médicale. Un point vétérinaire peut être utile pour $nom.", "When behaviours appear suddenly, it is wise to first rule out a medical cause. A vet check-up may be useful for $nom.", "Wenn Verhaltensweisen plötzlich auftreten, ist es ratsam, zuerst eine medizinische Ursache auszuschließen. Ein Tierarztbesuch kann für $nom sinnvoll sein.")
            niveauSituation == NiveauSituation.SENSIBLE -> tr("Au vu des réponses, la situation mérite un regard professionnel afin d'éviter qu'elle ne se fixe ou ne s'aggrave.", "Based on the responses, the situation warrants a professional's view to prevent it from becoming entrenched or worsening.", "Angesichts der Antworten verdient die Situation einen professionellen Blick, damit sie sich nicht festsetzt oder verschlimmert.")
            else -> null
        }
    }

    fun detecterFacteursAggravants(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): List<String> {
        val facteurs = mutableListOf<String>()
        if (reponsesChoix["apparition"] == 1) facteurs += tr("Apparition brutale", "Sudden onset", "Plötzliches Auftreten")
        if (reponsesChoix["evolution_probleme"] == 2) facteurs += tr("Comportement en aggravation", "Worsening behaviour", "Verhalten verschlimmert sich")
        if (reponsesChoix["intensite_probleme"] == 3) facteurs += tr("Intensité très forte", "Very high intensity", "Sehr hohe Intensität")
        if (reponsesChoix["generalisation_probleme"] == 2) facteurs += tr("Présence dans de nombreuses situations", "Present in many situations", "Tritt in vielen Situationen auf")
        if (contexte.physique >= 4) facteurs += tr("Suspicion de gêne ou cause physique", "Suspected discomfort or physical cause", "Verdacht auf Unwohlsein oder körperliche Ursache")
        if (maxOf(peur, attachement, impulsivite, reactivite) >= 75) facteurs += tr("Niveau élevé sur au moins un axe", "High level on at least one axis", "Hoher Wert auf mindestens einer Achse")
        if (estMaleEntier(reponsesChoix) && reactivite >= 50) facteurs += tr("Mâle entier avec réactivité marquée", "Intact male with marked reactivity", "Unkastrierter Rüde mit ausgeprägter Reaktivität")
        return facteurs.distinct()
    }

    fun detecterFacteursProtecteurs(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse): List<String> {
        val facteurs = mutableListOf<String>()
        if (reponsesChoix["evolution_probleme"] == 0) facteurs += tr("Une amélioration semble déjà présente", "An improvement already seems present", "Eine Verbesserung scheint bereits eingetreten")
        if (reponsesChoix["frequence_probleme"] == 0) facteurs += tr("Le comportement reste peu fréquent", "The behaviour remains infrequent", "Das Verhalten tritt eher selten auf")
        if (reponsesChoix["intensite_probleme"] == 0 || reponsesChoix["intensite_probleme"] == 1) facteurs += tr("L'intensité reste encore contenue", "Intensity remains contained", "Die Intensität bleibt noch begrenzt")
        if (reponsesChoix["generalisation_probleme"] == 0) facteurs += tr("Le problème semble limité à des contextes précis", "The problem seems limited to specific contexts", "Das Problem scheint auf bestimmte Situationen beschränkt")
        if (contexte.scoreContexte <= 3) facteurs += tr("Le contexte global ne suggère pas une situation fortement dégradée", "The overall context does not suggest a heavily degraded situation", "Die Gesamtsituation deutet nicht auf eine stark verschlechterte Lage hin")
        if (estSterilise(reponsesChoix)) facteurs += tr("Chien stérilisé — facteur stabilisant possible", "Neutered dog — possible stabilising factor", "Kastrierter Hund – möglicher stabilisierender Faktor")
        return facteurs.distinct()
    }

    fun detecterHypothesePrincipale(reponsesChoix: Map<String, Int>, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int, contexte: ContexteAnalyse): String {
        return when {
            contexte.physique >= 4 -> tr("Les éléments recueillis invitent d'abord à écarter une composante physique avant d'aller plus loin dans l'interprétation comportementale.", "The information collected suggests first ruling out a physical component before going further in behavioural interpretation.", "Die gesammelten Angaben legen nahe, zuerst eine körperliche Ursache auszuschließen, bevor man die Verhaltensdeutung weiterführt.")
            reponsesChoix["age"] == 3 && (reponsesChoix["senior_desorientation"] == 2 || reponsesChoix["senior_vocalise_nocturne"] == 2) -> tr("Certains signes (désorientation, vocalises nocturnes inexpliquées) peuvent évoquer un déclin cognitif lié à l'âge plutôt qu'un souci purement comportemental.", "Some signs (disorientation, unexplained night vocalizing) may suggest age-related cognitive changes rather than a purely behavioural issue.", "Manche Anzeichen (Orientierungslosigkeit, unerklärliches nächtliches Bellen oder Winseln) können eher auf einen altersbedingten kognitiven Abbau hinweisen als auf ein reines Verhaltensproblem.")
            reponsesChoix["age"] == 0 && reponsesChoix["proprete_type"] != null -> tr("À moins d'un an, cette malpropreté reflète le plus probablement un apprentissage de la propreté encore en cours plutôt qu'un souci comportemental à corriger.", "Under one year old, this house-soiling most likely reflects house-training still in progress rather than a behavioural issue to correct.", "Unter einem Jahr spiegelt diese Unsauberkeit höchstwahrscheinlich eine noch laufende Erziehung zur Stubenreinheit wider und kein Verhaltensproblem, das korrigiert werden muss.")
            reponsesChoix["marquage_habitude_post_sterilisation"] == 0 -> tr("Ce marquage semble avoir débuté avant la stérilisation et s'est transformé depuis en habitude acquise plutôt qu'en comportement hormonal.", "This marking seems to have started before neutering/spaying and has since become a learned habit rather than a hormonal behaviour.", "Diese Markierung scheint vor der Kastration begonnen zu haben und hat sich seitdem eher zu einer erlernten Gewohnheit als zu einem hormonellen Verhalten entwickelt.")
            estMaleEntier(reponsesChoix) && reponsesChoix["age"] != 0 && (reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2) -> tr("Chez un mâle entier, cette malpropreté peut correspondre à du marquage territorial plutôt qu'à une malpropreté classique.", "In an intact male, this house-soiling may correspond to territorial marking rather than classic uncleanliness.", "Bei einem unkastrierten Rüden kann diese Unsauberkeit eher einer Reviermarkierung entsprechen als einer klassischen Unsauberkeit.")
            reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2 -> tr("Puisque cette malpropreté concerne de l'urine, une cause médicale doit d'abord être écartée avec un vétérinaire.", "Since this house-soiling involves urine, a medical cause should be ruled out first with a veterinarian.", "Da diese Unsauberkeit Urin betrifft, sollte zuerst mit einem Tierarzt eine medizinische Ursache ausgeschlossen werden.")
            attachement >= 60 && reponsesChoix["vecu_absence"] == 2 -> tr("Les réponses peuvent évoquer une difficulté autour de la gestion de la séparation et de l'absence.", "The responses may suggest a difficulty around managing separation and absence.", "Die Antworten können auf Schwierigkeiten im Umgang mit Trennung und Abwesenheit hindeuten.")
            peur >= 60 && reactivite >= 60 -> tr("Les éléments recueillis suggèrent une sensibilité émotionnelle associée à des réactions marquées face à l'environnement.", "The information collected suggests emotional sensitivity combined with marked reactions to the environment.", "Die gesammelten Angaben deuten auf eine emotionale Sensibilität hin, verbunden mit deutlichen Reaktionen auf die Umgebung.")
            impulsivite >= 60 && reponsesChoix["regulation_excitation"] == 2 -> tr("Les réponses orientent vers une difficulté possible dans la régulation émotionnelle, avec des montées en excitation rapides.", "The responses point towards a possible difficulty in emotional regulation, with rapid escalations in excitement.", "Die Antworten deuten auf mögliche Schwierigkeiten bei der emotionalen Regulation hin, mit schnellen Anstiegen der Erregung.")
            reactivite >= 60 && reponsesChoix["reaction_chiens"] == 2 -> tr("Les réponses peuvent évoquer une réactivité importante dans les interactions avec les autres chiens.", "The responses may suggest significant reactivity in interactions with other dogs.", "Die Antworten können auf eine ausgeprägte Reaktivität im Kontakt mit anderen Hunden hindeuten.")
            reactivite >= 60 && reponsesChoix["reaction_inconnus"] == 2 -> tr("Les réponses peuvent évoquer une réactivité importante face aux personnes inconnues.", "The responses may suggest significant reactivity towards unknown people.", "Die Antworten können auf eine ausgeprägte Reaktivität gegenüber fremden Menschen hindeuten.")
            peur >= 60 -> tr("Les réponses suggèrent une sensibilité émotionnelle importante. Certains environnements ou situations peuvent être perçus comme plus difficiles à tolérer.", "The responses suggest significant emotional sensitivity. Some environments or situations may be perceived as more difficult to tolerate.", "Die Antworten deuten auf eine hohe emotionale Sensibilität hin. Manche Umgebungen oder Situationen können als schwerer erträglich empfunden werden.")
            attachement >= 60 -> tr("Le profil suggère surtout un besoin de proximité important, avec une autonomie émotionnelle qui paraît encore fragile dans certaines situations.", "The profile mainly suggests a strong need for closeness, with emotional independence that still seems fragile in some situations.", "Das Profil deutet vor allem auf ein großes Bedürfnis nach Nähe hin, mit einer emotionalen Selbstständigkeit, die in manchen Situationen noch zerbrechlich wirkt.")
            impulsivite >= 60 -> tr("Les réponses orientent vers une difficulté possible dans la régulation émotionnelle, avec des montées rapides en excitation.", "The responses point towards a possible difficulty in emotional regulation, with rapid excitement escalations.", "Die Antworten deuten auf mögliche Schwierigkeiten bei der emotionalen Regulation hin, mit schnellen Anstiegen der Erregung.")
            reactivite >= 60 -> tr("Les éléments recueillis peuvent correspondre à une réactivité accrue face à certains éléments de son environnement.", "The information collected may correspond to increased reactivity to certain elements of the environment.", "Die gesammelten Angaben können auf eine erhöhte Reaktivität gegenüber bestimmten Elementen seiner Umgebung hindeuten.")
            else -> tr("Aucune hypothèse dominante ne se dégage clairement à partir des réponses. Plusieurs facteurs peuvent être impliqués.", "No dominant hypothesis stands out clearly from the responses. Several factors may be involved.", "Aus den Antworten ergibt sich keine klar vorherrschende Hypothese. Mehrere Faktoren können beteiligt sein.")
        }
    }

    fun determinerPrioriteAction(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): PrioriteAction {
        val maxAxe = maxOf(peur, attachement, impulsivite, reactivite)
        return when {
            reponsesChoix["a_deja_mordu"] == 1 && reponsesChoix["cible_agression"] == 1 -> PrioriteAction.ELEVEE
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
            reponsesChoix["a_deja_mordu"] == 1 && reponsesChoix["cible_agression"] == 1 -> PrioriteImmediate(PrioriteAction.ELEVEE,
                tr("Priorité immédiate : reprendre la présentation avec les autres animaux", "Immediate priority: rework the introduction with other animals", "Sofortige Priorität: die Zusammenführung mit den anderen Tieren neu aufbauen"),
                tr("Comme il y a déjà eu morsure envers un autre animal, la cohabitation doit être reprise calmement pour $nom.", "Since there has already been a bite toward another animal, cohabitation should be reframed calmly for $nom.", "Da es bereits einen Biss gegenüber einem anderen Tier gab, sollte das Zusammenleben für $nom in Ruhe neu aufgebaut werden."),
                trList(listOf("Séparer temporairement les animaux en conflit.", "Envisager un accompagnement par un comportementaliste canin pour une réintroduction progressive."),
                    listOf("Temporarily separate the animals in conflict.", "Consider support from a dog behaviourist for a gradual reintroduction."),
                    listOf("Die Tiere im Konflikt vorübergehend trennen.", "Eine Begleitung durch eine Fachperson für Hundeverhalten für eine schrittweise Wiederzusammenführung in Betracht ziehen.")))
            reponsesChoix["a_deja_mordu"] == 1 -> PrioriteImmediate(PrioriteAction.URGENTE,
                tr("Priorité immédiate : sécuriser et se faire accompagner", "Immediate priority: secure and get support", "Sofortige Priorität: absichern und sich begleiten lassen"),
                tr("Comme il y a déjà eu morsure envers une personne, la situation ne doit pas être banalisée pour $nom.", "As there has already been a bite toward a person, the situation should not be minimised for $nom.", "Da es bereits einen Biss gegenüber einem Menschen gab, sollte die Situation von $nom nicht verharmlost werden."),
                trList(listOf("Éviter les situations à risque.", "Demander rapidement l'aide d'un professionnel du comportement."),
                    listOf("Avoid risky situations.", "Seek professional behaviour support quickly."),
                    listOf("Risikosituationen vermeiden.", "Rasch die Hilfe einer Fachperson für Verhalten suchen.")))
            contexte.physique >= 4 -> PrioriteImmediate(PrioriteAction.URGENTE,
                tr("Priorité immédiate : écarter une cause physique", "Immediate priority: rule out a physical cause", "Sofortige Priorität: eine körperliche Ursache ausschließen"),
                tr("Des signes physiques sont signalés chez $nom.", "Physical signs have been reported for $nom.", "Bei $nom werden körperliche Anzeichen gemeldet."),
                trList(listOf("Prendre un avis vétérinaire rapidement.", "Éviter les sollicitations difficiles en attendant."),
                    listOf("Get a vet's opinion quickly.", "Avoid difficult demands in the meantime."),
                    listOf("Rasch tierärztlichen Rat einholen.", "Schwierige Anforderungen in der Zwischenzeit vermeiden.")))
            priorite == PrioriteAction.ELEVEE || niveauSituation == NiveauSituation.SENSIBLE -> PrioriteImmediate(PrioriteAction.ELEVEE,
                tr("Priorité immédiate : agir sans tarder", "Immediate priority: act without delay", "Sofortige Priorität: ohne Verzögerung handeln"),
                tr("La situation semble suffisamment marquée pour justifier une action rapide pour $nom.", "The situation seems marked enough to warrant quick action for $nom.", "Die Situation scheint deutlich genug zu sein, um für $nom schnelles Handeln zu rechtfertigen."),
                trList(listOf("Alléger les contextes les plus difficiles.", "Envisager un accompagnement professionnel."),
                    listOf("Lighten the most difficult contexts.", "Consider professional support."),
                    listOf("Die schwierigsten Situationen entlasten.", "Eine professionelle Begleitung in Betracht ziehen.")))
            priorite == PrioriteAction.MODEREE -> PrioriteImmediate(PrioriteAction.MODEREE,
                tr("Priorité immédiate : avancer progressivement", "Immediate priority: move forward gradually", "Sofortige Priorität: schrittweise vorgehen"),
                tr("La situation mérite d'être prise au sérieux pour $nom.", "The situation deserves to be taken seriously for $nom.", "Die Situation von $nom sollte ernst genommen werden."),
                trList(listOf("Commencer un travail progressif sur les situations difficiles.", "Observer fréquence et contexte pendant quelques jours."),
                    listOf("Start progressive work on difficult situations.", "Observe frequency and context for a few days."),
                    listOf("Schrittweise an den schwierigen Situationen arbeiten.", "Einige Tage lang Häufigkeit und Kontext beobachten.")))
            else -> PrioriteImmediate(PrioriteAction.FAIBLE,
                tr("Priorité immédiate : surveiller calmement", "Immediate priority: watch calmly", "Sofortige Priorität: in Ruhe beobachten"),
                tr("Rien ne ressort comme urgent à ce stade pour $nom.", "Nothing stands out as urgent at this stage for $nom.", "Im Moment scheint für $nom nichts dringend zu sein."),
                trList(listOf("Continuer l'observation du quotidien.", "Maintenir un cadre stable et prévisible."),
                    listOf("Continue daily observation.", "Maintain a stable and predictable framework."),
                    listOf("Den Alltag weiter beobachten.", "Einen stabilen und vorhersehbaren Rahmen bewahren.")))
        }
    }

    fun construireExplicationResultat(reponsesChoix: Map<String, Int>, contexte: ContexteAnalyse, peur: Int, attachement: Int, impulsivite: Int, reactivite: Int): ExplicationResultat {
        val raisons = mutableListOf<String>()
        if (reponsesChoix["evolution_probleme"] == 2) raisons += tr("Le comportement semble s'aggraver.", "The behaviour seems to be getting worse.", "Das Verhalten scheint sich zu verschlimmern.")
        if (reponsesChoix["frequence_probleme"] == 2 || reponsesChoix["frequence_probleme"] == 3) raisons += tr("Le comportement paraît revenir fréquemment.", "The behaviour seems to recur frequently.", "Das Verhalten scheint häufig wiederzukehren.")
        if (reponsesChoix["intensite_probleme"] == 2 || reponsesChoix["intensite_probleme"] == 3) raisons += tr("L'intensité décrite paraît importante.", "The described intensity appears significant.", "Die beschriebene Intensität erscheint hoch.")
        if (raisons.isEmpty()) raisons += tr("Les réponses suggèrent surtout quelques points de vigilance.", "The responses suggest mainly a few points of vigilance.", "Die Antworten deuten vor allem auf einige Punkte hin, die Aufmerksamkeit verdienen.")
        return ExplicationResultat(raisons.take(3),
            detecterFacteursAggravants(reponsesChoix, contexte, peur, attachement, impulsivite, reactivite),
            detecterFacteursProtecteurs(reponsesChoix, contexte))
    }

    fun genererSyntheseAvancee(nom: String, hypothese: String, priorite: PrioriteAction, aggravants: List<String>, protecteurs: List<String>): String {
        val intro = when (priorite) {
            PrioriteAction.FAIBLE -> tr("$nom présente un fonctionnement globalement stable avec quelques points de vigilance.", "$nom shows an overall stable profile with a few points of vigilance.", "$nom zeigt eine insgesamt stabile Funktionsweise mit einigen Punkten, die Aufmerksamkeit verdienen.")
            PrioriteAction.MODEREE -> tr("$nom présente une difficulté réelle qui mérite une approche progressive.", "$nom shows a real difficulty that deserves a gradual approach.", "$nom zeigt eine echte Schwierigkeit, die ein schrittweises Vorgehen verdient.")
            PrioriteAction.ELEVEE -> tr("$nom semble actuellement en difficulté sur un plan suffisamment marqué pour nécessiter une attention active.", "$nom currently seems to be struggling sufficiently to require active attention.", "$nom scheint derzeit in einem Bereich Schwierigkeiten zu haben, der deutlich genug ist, um aktive Aufmerksamkeit zu erfordern.")
            PrioriteAction.URGENTE -> tr("$nom présente des éléments qui justifient une attention rapide.", "$nom shows elements that warrant prompt attention.", "$nom zeigt Anzeichen, die eine rasche Aufmerksamkeit rechtfertigen.")
        }
        val hypotheseLabel = tr("Hypothèse de lecture : $hypothese", "Reading hypothesis: $hypothese", "Lesehypothese: $hypothese")
        val aggr = if (aggravants.isNotEmpty())
            tr("Les éléments qui majorent possiblement la situation sont : ${aggravants.joinToString(", ")}.",
                "Elements possibly worsening the situation: ${aggravants.joinToString(", ")}.",
                "Folgende Punkte könnten die Situation verschärfen: ${aggravants.joinToString(", ")}.")
        else ""
        val prot = if (protecteurs.isNotEmpty())
            tr("Les éléments plutôt favorables à ce stade sont : ${protecteurs.joinToString(", ")}.",
                "Elements currently rather favourable: ${protecteurs.joinToString(", ")}.",
                "Folgende Punkte sind derzeit eher günstig: ${protecteurs.joinToString(", ")}.")
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
        if (maxAxe <= 30) return tr("$nom semble évoluer dans un équilibre global satisfaisant. Aucune origine comportementale particulière ne ressort à ce stade.", "$nom seems to be evolving in an overall satisfying balance. No particular behavioural origin stands out at this stage.", "$nom scheint sich in einem insgesamt zufriedenstellenden Gleichgewicht zu entwickeln. Zum jetzigen Zeitpunkt zeigt sich keine besondere verhaltensbezogene Ursache.")

        return when (axe) {
            Axe.PEUR -> buildString {
                when (appLang()) {
                    AppLang.EN -> {
                        append("$nom's emotional sensitivity may have several origins. ")
                        append("Limited early socialisation — few varied exposures during the first weeks of life — is often involved. ")
                        append("Past negative experiences, even isolated ones, can also leave a lasting mark on how a dog perceives its environment. ")
                        if (estMaleEntier(reponsesChoix)) append("In an intact male, hormonal levels can sometimes amplify alertness and cautious reactions. ")
                        if (reponsesChoix["age"] == 0) append("Under one year, sensitivity is often more pronounced: the dog is still building its landmarks. ")
                        append("In some cases, a genetic predisposition also plays a role, regardless of experience.")
                    }
                    AppLang.DE -> {
                        append("Die emotionale Sensibilität von $nom kann mehrere Ursachen haben. ")
                        append("Oft spielt eine eingeschränkte frühe Sozialisierung eine Rolle – wenig abwechslungsreiche Erfahrungen in den ersten Lebenswochen. ")
                        append("Frühere negative Erlebnisse, selbst einzelne, können ebenfalls dauerhaft prägen, wie ein Hund seine Umgebung wahrnimmt. ")
                        if (estMaleEntier(reponsesChoix)) append("Bei einem unkastrierten Rüden kann der Hormonspiegel die Wachsamkeit und vorsichtige Reaktionen manchmal verstärken. ")
                        if (reponsesChoix["age"] == 0) append("Unter einem Jahr ist die Sensibilität oft ausgeprägter: Der Hund baut seine Orientierungspunkte noch auf. ")
                        append("In manchen Fällen spielt auch eine genetische Veranlagung eine Rolle, unabhängig vom Erlebten.")
                    }
                    AppLang.FR -> {
                        append("La sensibilité émotionnelle de $nom peut avoir plusieurs origines. ")
                        append("Une socialisation précoce limitée — peu d'expositions variées pendant les premières semaines de vie — est souvent impliquée. ")
                        append("Des expériences négatives passées, même ponctuelles, peuvent aussi laisser une empreinte durable sur la façon dont un chien perçoit son environnement. ")
                        if (estMaleEntier(reponsesChoix)) append("Chez un mâle entier, le niveau hormonal peut parfois amplifier la vigilance et les réactions de prudence. ")
                        if (reponsesChoix["age"] == 0) append("À moins d'un an, la sensibilité est souvent plus marquée : le chien est encore en train de construire ses repères. ")
                        append("Dans certains cas, une prédisposition génétique joue également un rôle, indépendamment du vécu.")
                    }
                }
            }
            Axe.ATTACHEMENT -> buildString {
                when (appLang()) {
                    AppLang.EN -> {
                        append("$nom's strong need for closeness can be explained in several ways. ")
                        append("Too-early weaning or a difficult separation during the first weeks of life can weaken the building of emotional independence. ")
                        append("A very fusional environment — where the dog was rarely exposed to moments alone — can also reinforce this need. ")
                        if (reponsesChoix["suit_partout"] == 2) append("Constantly following its human can be both a symptom and a factor that maintains this relational dependency. ")
                        append("This type of profile is not a matter of character or whim: it often reflects a genuine difficulty finding internal support when the reassuring presence is not there.")
                    }
                    AppLang.DE -> {
                        append("Das große Bedürfnis von $nom nach Nähe lässt sich auf verschiedene Weise erklären. ")
                        append("Eine zu frühe Trennung von der Mutter oder eine schwierige Trennung in den ersten Lebenswochen kann den Aufbau emotionaler Selbstständigkeit schwächen. ")
                        append("Ein sehr enges Umfeld – in dem der Hund selten Momente allein erlebt hat – kann dieses Bedürfnis ebenfalls verstärken. ")
                        if (reponsesChoix["suit_partout"] == 2) append("Seinem Menschen ständig zu folgen, kann zugleich ein Anzeichen und ein Faktor sein, der diese Abhängigkeit in der Beziehung aufrechterhält. ")
                        append("Diese Funktionsweise ist keine Frage des Charakters oder einer Laune: Oft spiegelt sie eine echte Schwierigkeit wider, inneren Halt zu finden, wenn die beruhigende Anwesenheit fehlt.")
                    }
                    AppLang.FR -> {
                        append("Le besoin de proximité important de $nom peut s'expliquer de plusieurs façons. ")
                        append("Un sevrage trop précoce ou une séparation difficile dans les premières semaines de vie peut fragiliser la construction de l'autonomie émotionnelle. ")
                        append("Un environnement très fusionnel — où le chien a rarement été exposé à des moments seul — peut aussi renforcer ce besoin. ")
                        if (reponsesChoix["suit_partout"] == 2) append("Le fait de suivre constamment son humain peut être à la fois un symptôme et un facteur qui entretient cette dépendance relationnelle. ")
                        append("Ce type de fonctionnement n'est pas une question de caractère ou de caprice : il reflète souvent une vraie difficulté à trouver un appui interne quand la présence rassurante n'est pas là.")
                    }
                }
            }
            Axe.IMPULSIVITE -> buildString {
                when (appLang()) {
                    AppLang.EN -> {
                        append("$nom's difficulty regulating excitement may have several origins. ")
                        append("Some dogs have a naturally low activation threshold: they escalate quickly and come down more slowly, regardless of the training they have received. ")
                        if (reponsesChoix["race_categorie"]?.let { it == 0 || it == 5 || it == 7 } == true) append("Some breed families were selected for a high energy and reactivity level, which can weigh on emotional regulation. ")
                        append("A lack of structure in daily interactions — games that are too long, too intense, without breaks — can also maintain this pattern. ")
                        if (reponsesChoix["age"] == 0 || reponsesChoix["age"] == 1) append("At a young age, inhibitory control is still developing: some impulsivity is often normal before 2-3 years. ")
                        append("Impulsivity is generally not a lack of willpower or intelligence, but a difficulty braking an emotional escalation already underway.")
                    }
                    AppLang.DE -> {
                        append("Die Schwierigkeit von $nom, seine Erregung zu regulieren, kann mehrere Ursachen haben. ")
                        append("Manche Hunde haben von Natur aus eine niedrige Aktivierungsschwelle: Sie steigern sich schnell und kommen langsamer wieder herunter, unabhängig von ihrer Erziehung. ")
                        if (reponsesChoix["race_categorie"]?.let { it == 0 || it == 5 || it == 7 } == true) append("Manche Rassegruppen wurden auf ein hohes Energie- und Reaktionsniveau gezüchtet, was die emotionale Regulation belasten kann. ")
                        append("Fehlende Struktur in den alltäglichen Interaktionen – zu lange, zu intensive Spiele ohne Pausen – kann dieses Muster ebenfalls aufrechterhalten. ")
                        if (reponsesChoix["age"] == 0 || reponsesChoix["age"] == 1) append("In jungem Alter entwickelt sich die Impulskontrolle noch: Eine gewisse Impulsivität ist vor 2–3 Jahren oft normal. ")
                        append("Impulsivität ist in der Regel kein Mangel an Willen oder Intelligenz, sondern die Schwierigkeit, einen bereits begonnenen emotionalen Anstieg zu bremsen.")
                    }
                    AppLang.FR -> {
                        append("La difficulté de $nom à réguler son excitation peut avoir plusieurs origines. ")
                        append("Certains chiens ont un seuil d'activation naturellement bas : ils montent vite en intensité et redescendent plus lentement, quelle que soit l'éducation reçue. ")
                        if (reponsesChoix["race_categorie"]?.let { it == 0 || it == 5 || it == 7 } == true) append("Certaines familles de races ont été sélectionnées pour un niveau d'énergie et de réactivité élevé, ce qui peut peser sur la régulation émotionnelle. ")
                        append("Un manque de structure dans les interactions quotidiennes — jeux trop longs, trop intenses, sans pauses — peut aussi entretenir ce mode de fonctionnement. ")
                        if (reponsesChoix["age"] == 0 || reponsesChoix["age"] == 1) append("À un jeune âge, le contrôle inhibiteur est encore en développement : une certaine impulsivité est souvent normale avant 2-3 ans. ")
                        append("L'impulsivité n'est généralement pas un manque de volonté ou d'intelligence, mais une difficulté à freiner une montée émotionnelle déjà enclenchée.")
                    }
                }
            }
            Axe.REACTIVITE -> buildString {
                when (appLang()) {
                    AppLang.EN -> {
                        append("$nom's reactivity may be explained by a combination of factors. ")
                        append("Incomplete socialisation — few encounters with other dogs, varied people or different environments during sensitive periods — is often involved. ")
                        if (reponsesChoix["a_deja_mordu"] == 1) append("The fact that there has already been a bite may indicate that reactivity has crossed an important threshold, sometimes associated with a history of poorly-experienced confrontations. ")
                        if (estMaleEntier(reponsesChoix)) append("In an intact male, interactions with other males may be more tense due to hormonal influence. ")
                        append("Repeated negative experiences with certain triggers may also have led the dog to anticipate threat and react preventively. ")
                        append("In some cases, reactivity is also a way of managing a safety distance when the dog feels overwhelmed.")
                    }
                    AppLang.DE -> {
                        append("Die Reaktivität von $nom lässt sich durch ein Zusammenspiel mehrerer Faktoren erklären. ")
                        append("Oft ist eine unvollständige Sozialisierung beteiligt – wenige Begegnungen mit anderen Hunden, verschiedenen Menschen oder unterschiedlichen Umgebungen in den sensiblen Phasen. ")
                        if (reponsesChoix["a_deja_mordu"] == 1) append("Dass es bereits einen Biss gab, kann darauf hindeuten, dass die Reaktivität eine wichtige Schwelle überschritten hat, manchmal verbunden mit schlecht erlebten Konfrontationen in der Vergangenheit. ")
                        if (estMaleEntier(reponsesChoix)) append("Bei einem unkastrierten Rüden können Begegnungen mit anderen Rüden durch hormonellen Einfluss angespannter sein. ")
                        append("Wiederholte negative Erfahrungen mit bestimmten Auslösern können den Hund auch dazu gebracht haben, eine Bedrohung vorwegzunehmen und vorbeugend zu reagieren. ")
                        append("In manchen Fällen ist Reaktivität auch eine Art, einen Sicherheitsabstand zu wahren, wenn sich der Hund überfordert fühlt.")
                    }
                    AppLang.FR -> {
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
            explicationPrincipale = explicationProbleme(problemePrincipal, peur, attachement, impulsivite, reactivite, reponsesChoix),
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
            originesPossibles = originesPossibles,
            lieuResidence = reponsesChoix["lieu_residence"]
        )
    }

    fun doitAfficherQuestion(questionId: String, reponsesChoix: Map<String, Int>): Boolean {
        return when (questionId) {
            "senior_desorientation", "senior_vocalise_nocturne" -> reponsesChoix["age"] == 3
            "cible_agression" -> reponsesChoix["a_deja_mordu"] == 1
            "lieu_residence" -> showConsultation()
            "proprete_type" -> reponsesChoix["proprete_maison"] != 0
            "marquage_habitude_post_sterilisation" -> estSterilise(reponsesChoix) && (reponsesChoix["proprete_type"] == 0 || reponsesChoix["proprete_type"] == 2)
            "apparition", "situation_principale", "duree_probleme", "evolution_probleme",
            "frequence_probleme", "intensite_probleme", "generalisation_probleme",
            "changement_recent", "signe_physique" -> reponsesChoix["a_un_probleme"] != 1
            else -> true
        }
    }

    // Délègue à AppStrings pour la traduction
    fun titreSectionPourQuestion(questionId: String): String = strTitreSection(questionId)

    fun aideQuestion(questionId: String): String? = when (questionId) {
        "race_categorie" -> tr("Choisissez la famille qui ressemble le plus à votre chien. Pour un croisé, choisissez la dernière option.", "Choose the family that best matches your dog. For a mixed breed, choose the last option.", "Wählen Sie die Gruppe, die Ihrem Hund am ähnlichsten ist. Für einen Mischling wählen Sie die letzte Option.")
        "sterilise" -> tr("La stérilisation influence certains comportements comme la réactivité ou les tensions entre chiens.", "Neutering/spaying influences certain behaviours such as reactivity or tensions between dogs.", "Die Kastration beeinflusst manche Verhaltensweisen wie die Reaktivität oder Spannungen zwischen Hunden.")
        "senior_desorientation" -> tr("Par exemple, il semble perdu près de sa gamelle, d'une porte familière ou de son couchage habituel.", "For example, it seems lost near its bowl, a familiar door or its usual resting spot.", "Zum Beispiel wirkt er verloren in der Nähe seines Napfes, einer vertrauten Tür oder seines gewohnten Liegeplatzes.")
        "senior_vocalise_nocturne" -> tr("Il s'agit d'aboiements ou de gémissements forts et insistants, sans déclencheur évident.", "This means loud, insistent barking or whining with no obvious trigger.", "Gemeint ist lautes und beharrliches Bellen oder Winseln ohne erkennbaren Auslöser.")
        "adaptation_changements" -> tr("Pensez aux changements d'habitudes, de lieu, de rythme ou d'environnement.", "Think about changes in habits, place, rhythm or environment.", "Denken Sie an Veränderungen von Gewohnheiten, Ort, Rhythmus oder Umgebung.")
        "comportement_exterieur" -> tr("Répondez en pensant surtout aux promenades et sorties habituelles.", "Answer thinking mainly about usual walks and outings.", "Denken Sie bei Ihrer Antwort vor allem an die üblichen Spaziergänge und Ausflüge.")
        "reaction_peur" -> tr("Choisissez la réaction la plus fréquente quand votre chien est inquiet.", "Choose the most frequent reaction when your dog is worried.", "Wählen Sie die häufigste Reaktion, wenn Ihr Hund beunruhigt ist.")
        "vecu_absence" -> tr("Pensez à ce que vous observez à votre retour, ou ce que vos voisins vous rapportent.", "Think about what you observe on your return, or what your neighbours report.", "Denken Sie an das, was Sie bei Ihrer Rückkehr feststellen oder was Ihre Nachbarn Ihnen berichten.")
        "proprete_type" -> tr("Cette précision aide à distinguer un marquage territorial, une cause médicale, ou un apprentissage de la propreté encore en cours.", "This distinction helps tell apart territorial marking, a medical cause, or house-training still in progress.", "Diese Angabe hilft, eine Reviermarkierung, eine medizinische Ursache oder eine noch laufende Erziehung zur Stubenreinheit zu unterscheiden.")
        "marquage_habitude_post_sterilisation" -> tr("Même après la stérilisation, un geste appris auparavant peut persister comme une habitude.", "Even after neutering/spaying, a gesture learned beforehand can persist as a habit.", "Auch nach der Kastration kann ein zuvor erlerntes Verhalten als Gewohnheit bestehen bleiben.")
        "regulation_excitation" -> tr("Pensez au jeu, à une sortie, une visite, ou tout moment stimulant.", "Think about play, an outing, a visit, or any stimulating moment.", "Denken Sie an ein Spiel, einen Ausflug, einen Besuch oder jeden anderen aufregenden Moment.")
        "reaction_inconnus" -> tr("Par exemple : aboiements, évitement, tension, grognements.", "For example: barking, avoidance, tension, growling.", "Zum Beispiel: Bellen, Ausweichen, Anspannung, Knurren.")
        "reaction_chiens" -> tr("Par exemple : tension, aboiements, charge, évitement ou agitation.", "For example: tension, barking, lunging, avoidance or agitation.", "Zum Beispiel: Anspannung, Bellen, Vorpreschen, Ausweichen oder Unruhe.")
        "a_deja_mordu" -> tr("Même une morsure ponctuelle compte.", "Even a single isolated bite counts.", "Auch ein einmaliger Biss zählt.")
        "lieu_residence" -> tr("Pour vous proposer une consultation adaptée.", "To suggest a suitable consultation.", "Damit wir Ihnen eine passende Beratung vorschlagen können.")
        "cible_agression" -> tr("Cela permet de distinguer un enjeu de sécurité envers des personnes d'une difficulté de sociabilisation avec d'autres animaux.", "This helps tell apart a safety concern toward people from a socialization issue with other animals.", "So lässt sich ein Sicherheitsproblem gegenüber Menschen von einer Schwierigkeit im Umgang mit anderen Tieren unterscheiden.")
        "signe_physique" -> tr("Même un doute peut être utile à signaler.", "Even a doubt can be useful to mention.", "Auch ein Zweifel kann hilfreich sein.")
        else -> null
    }
}

fun questionsApplication(): List<Question> {
    return listOf(
        QuestionTexte("nom_chien",
            tr("Quel est le nom de votre chien ?", "What is your dog's name?", "Wie heißt Ihr Hund?")),

        QuestionChoix("race_categorie",
            tr("À quelle famille de races appartient votre chien ?", "Which breed family does your dog belong to?", "Zu welcher Rassegruppe gehört Ihr Hund?"),
            trList(listOf("Chiens de berger & troupeau", "Retrievers & Spaniels", "Terriers",
                "Molosses & Dogues", "Chiens nordiques & primitifs", "Lévriers & Races de course",
                "Races naines & compagnie", "Chiens de chasse & pisteurs", "Croisé / Bâtard / Race inconnue"),
                listOf("Herding & sheepdogs", "Retrievers & Spaniels", "Terriers",
                    "Molossers & Mastiffs", "Nordic & primitive dogs", "Sighthounds & racing dogs",
                    "Toy & companion breeds", "Hunting & tracking dogs", "Mixed breed / unknown breed"),
                listOf("Hüte- und Hirtenhunde", "Retriever & Spaniel", "Terrier", "Molosser & Doggen", "Nordische & ursprüngliche Hunde", "Windhunde & Rennhunde", "Klein- & Gesellschaftshunde", "Jagd- & Spürhunde", "Mischling / Rasse unbekannt"))),

        QuestionChoix("age",
            tr("Quel âge a votre chien ?", "How old is your dog?", "Wie alt ist Ihr Hund?"),
            trList(listOf("Moins d'1 an", "Entre 1 et 3 ans", "Entre 4 et 7 ans", "8 ans et +"),
                listOf("Under 1 year", "Between 1 and 3 years", "Between 4 and 7 years", "8 years and over"),
                listOf("Unter 1 Jahr", "Zwischen 1 und 3 Jahren", "Zwischen 4 und 7 Jahren", "8 Jahre und älter"))),

        QuestionChoix("sterilise",
            tr("Votre chien est :", "Your dog is:", "Ihr Hund ist:"),
            trList(listOf("Un mâle stérilisé", "Une femelle stérilisée", "Un mâle entier", "Une femelle entière"),
                listOf("A neutered male", "A spayed female", "An intact male", "An intact female"),
                listOf("Ein kastrierter Rüde", "Eine kastrierte Hündin", "Ein unkastrierter Rüde", "Eine unkastrierte Hündin"))),

        QuestionChoix("senior_desorientation",
            tr("Votre chien semble-t-il parfois désorienté ou perdu dans des endroits qu'il connaît bien ?", "Does your dog sometimes seem disoriented or lost in places it knows well?", "Wirkt Ihr Hund manchmal orientierungslos oder verloren an Orten, die er gut kennt?"),
            trList(listOf("Non, jamais", "Parfois, occasionnellement", "Oui, régulièrement"),
                listOf("No, never", "Sometimes, occasionally", "Yes, regularly"),
                listOf("Nein, nie", "Manchmal, gelegentlich", "Ja, regelmäßig"))),

        QuestionChoix("senior_vocalise_nocturne",
            tr("Depuis quelque temps, votre chien vocalise-t-il ou erre-t-il la nuit sans raison apparente (pas de faim, pas de demande d'attention identifiable) ?", "Has your dog recently been vocalizing or wandering at night without an apparent reason (not hungry, no identifiable demand for attention)?", "Bellt, winselt oder wandert Ihr Hund seit einiger Zeit nachts ohne erkennbaren Grund umher (kein Hunger, kein erkennbarer Wunsch nach Aufmerksamkeit)?"),
            trList(listOf("Non, jamais", "Parfois, occasionnellement", "Oui, régulièrement"),
                listOf("No, never", "Sometimes, occasionally", "Yes, regularly"),
                listOf("Nein, nie", "Manchmal, gelegentlich", "Ja, regelmäßig"))),

        QuestionChoix("adaptation_changements",
            tr("Votre chien a-t-il du mal à s'adapter aux changements ?", "Does your dog struggle to adapt to changes?", "Fällt es Ihrem Hund schwer, sich an Veränderungen anzupassen?"),
            trList(listOf("Non", "Un peu", "Oui"),
                listOf("No", "A little", "Yes"),
                listOf("Nein", "Ein wenig", "Ja")),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("comportement_exterieur",
            tr("En promenade ou à l'extérieur, face à une situation inhabituelle, votre chien est plutôt :", "On walks or outdoors, faced with an unusual situation, your dog is rather:", "Bei Spaziergängen oder draußen ist Ihr Hund in einer ungewohnten Situation eher:"),
            trList(listOf("Calme et confiant", "Prudent, observe avant d'agir", "Craintif, cherche à fuir ou à éviter"),
                listOf("Calm and confident", "Cautious, observes before acting", "Fearful, tries to flee or avoid"),
                listOf("Ruhig und selbstsicher", "Vorsichtig, beobachtet, bevor er handelt", "Ängstlich, versucht zu fliehen oder auszuweichen")),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_peur",
            tr("Quand votre chien a peur, il réagit plutôt comment ?", "When your dog is scared, how does it react?", "Wie reagiert Ihr Hund eher, wenn er Angst hat?"),
            trList(listOf("Il récupère vite", "Il se cache / fuit", "Il panique ou devient agressif"),
                listOf("It recovers quickly", "It hides / flees", "It panics or becomes aggressive"),
                listOf("Er erholt sich schnell", "Er versteckt sich / flieht", "Er gerät in Panik oder wird aggressiv")),
            axe = Axe.PEUR, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("vecu_absence",
            tr("Comment votre chien vit-il vos absences ?", "How does your dog experience your absences?", "Wie erlebt Ihr Hund Ihre Abwesenheiten?"),
            trList(listOf("Bien — il reste calme",
                "Moyennement — il peut vocaliser ou s'agiter un peu, puis se calme",
                "Difficilement — il vocalise ou s'agite de façon notable",
                "Très difficilement — il détruit, aboie fortement ou panique"),
                listOf("Well — it stays calm",
                    "So-so — it may vocalize or become a little restless, then settles",
                    "With difficulty — it vocalizes or becomes notably agitated",
                    "Very difficultly — it destroys, barks loudly or panics"),
                listOf("Gut – er bleibt ruhig", "Mittelmäßig – er kann bellen, winseln oder etwas unruhig werden, beruhigt sich dann aber", "Schwer – er bellt, winselt oder wird deutlich unruhig", "Sehr schwer – er zerstört, bellt laut oder gerät in Panik")),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 2, 4), signalAlerte = true),

        QuestionChoix("suit_partout",
            tr("Votre chien vous suit-il partout dans la maison ?", "Does your dog follow you everywhere in the house?", "Folgt Ihnen Ihr Hund im Haus überallhin?"),
            trList(listOf("Non", "Parfois", "Il ne me quitte pratiquement pas"),
                listOf("No", "Sometimes", "It barely leaves my side"),
                listOf("Nein", "Manchmal", "Er weicht mir kaum von der Seite")),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("autre_personne_apaise",
            tr("La présence d'une autre personne suffit-elle à l'apaiser ?", "Is the presence of another person enough to calm it?", "Reicht die Anwesenheit einer anderen Person aus, um ihn zu beruhigen?"),
            trList(listOf("Oui", "Il n'est vraiment apaisé qu'avec moi", "Je ne sais pas"),
                listOf("Yes", "It is only really calmed by me", "I don't know"),
                listOf("Ja", "Er beruhigt sich wirklich nur bei mir", "Ich weiß es nicht")),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 2, 0)),

        QuestionChoix("proprete_maison",
            tr("Votre chien est-il propre à la maison ?", "Is your dog house-trained?", "Ist Ihr Hund im Haus stubenrein?"),
            trList(listOf("Oui", "Non", "Parfois"),
                listOf("Yes", "No", "Sometimes"),
                listOf("Ja", "Nein", "Manchmal")),
            axe = Axe.ATTACHEMENT, scoreParOption = listOf(0, 2, 1)),

        QuestionChoix("proprete_type",
            tr("Il s'agit plutôt de :", "It is rather:", "Es handelt sich eher um:"),
            trList(listOf("Urine", "Selles", "Les deux"),
                listOf("Urine", "Stools", "Both"),
                listOf("Urin", "Kot", "Beides"))),

        QuestionChoix("marquage_habitude_post_sterilisation",
            tr("Ce marquage a-t-il débuté avant la stérilisation de votre chien ?", "Did this marking start before your dog was neutered/spayed?", "Hat diese Markierung vor der Kastration Ihres Hundes begonnen?"),
            trList(listOf("Oui, et ça a continué depuis", "Non, c'est apparu après la stérilisation", "Je ne sais pas / je l'ai adopté déjà stérilisé"),
                listOf("Yes, and it has continued since", "No, it appeared after neutering/spaying", "I don't know / I adopted them already neutered/spayed"),
                listOf("Ja, und es hat seitdem angehalten", "Nein, es ist nach der Kastration aufgetreten", "Ich weiß es nicht / ich habe ihn bereits kastriert übernommen"))),

        QuestionChoix("regulation_excitation",
            tr("Après un moment excitant (jeu, sortie, visite), votre chien :", "After an exciting moment (play, outing, visit), your dog:", "Wie verhält sich Ihr Hund nach einem aufregenden Moment (Spiel, Ausflug, Besuch)?"),
            trList(listOf("Reste contrôlé, se calme facilement",
                "Peut beaucoup s'exciter mais finit par se calmer",
                "A du mal à se calmer, les moments d'excitation deviennent difficiles à gérer"),
                listOf("Stays controlled, calms down easily",
                    "Can get very excited but ends up calming down",
                    "Struggles to calm down, exciting moments become hard to manage"),
                listOf("Er bleibt kontrolliert und beruhigt sich leicht", "Er kann sich stark aufregen, beruhigt sich aber schließlich", "Er kommt nur schwer zur Ruhe, aufregende Momente werden schwer zu handhaben")),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 4), signalAlerte = true),

        QuestionChoix("vole_objets",
            tr("Votre chien vole-t-il de la nourriture ou des objets ?", "Does your dog steal food or objects?", "Stiehlt Ihr Hund Futter oder Gegenstände?"),
            trList(listOf("Non", "Parfois", "Souvent"),
                listOf("No", "Sometimes", "Often"),
                listOf("Nein", "Manchmal", "Oft")),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("poursuite_mouvement",
            tr("Votre chien poursuit-il facilement ce qui bouge ?", "Does your dog easily chase moving things?", "Jagt Ihr Hund leicht allem nach, was sich bewegt?"),
            trList(listOf("Non", "Parfois", "Souvent"),
                listOf("No", "Sometimes", "Often"),
                listOf("Nein", "Manchmal", "Oft")),
            axe = Axe.IMPULSIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_inconnus",
            tr("Votre chien réagit-il difficilement aux personnes inconnues ?", "Does your dog react negatively to unknown people?", "Reagiert Ihr Hund schwierig auf fremde Menschen?"),
            trList(listOf("Non", "Parfois", "Souvent"),
                listOf("No", "Sometimes", "Often"),
                listOf("Nein", "Manchmal", "Oft")),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("reaction_chiens",
            tr("Votre chien réagit-il difficilement aux autres chiens ?", "Does your dog react negatively to other dogs?", "Reagiert Ihr Hund schwierig auf andere Hunde?"),
            trList(listOf("Non", "Parfois", "Souvent"),
                listOf("No", "Sometimes", "Often"),
                listOf("Nein", "Manchmal", "Oft")),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 1, 2)),

        QuestionChoix("a_deja_mordu",
            tr("Votre chien a-t-il déjà mordu ?", "Has your dog ever bitten?", "Hat Ihr Hund schon einmal gebissen?"),
            trList(listOf("Non", "Oui"),
                listOf("No", "Yes"),
                listOf("Nein", "Ja")),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 4), poids = 2, signalCritique = true),

        QuestionChoix("cible_agression",
            tr("Envers qui cela s'est-il produit ?", "Who was it directed at?", "Gegen wen hat sich das gerichtet?"),
            trList(listOf("Une personne", "Un autre animal (chien, chat...)", "Les deux"),
                listOf("A person", "Another animal (dog, cat...)", "Both"),
                listOf("Einen Menschen", "Ein anderes Tier (Hund, Katze …)", "Beide")),
            axe = Axe.REACTIVITE),

        QuestionChoix("defense_ressources",
            tr("Votre chien grogne-t-il ou devient-il tendu quand on s'approche de sa gamelle, de ses jouets ou de son couchage ?", "Does your dog growl or become tense when approached near its bowl, toys or bed?", "Knurrt Ihr Hund oder wird er angespannt, wenn man sich seinem Napf, seinem Spielzeug oder seinem Liegeplatz nähert?"),
            trList(listOf("Non, jamais", "Parfois, dans certaines situations", "Oui, c'est fréquent"),
                listOf("No, never", "Sometimes, in certain situations", "Yes, it happens often"),
                listOf("Nein, nie", "Manchmal, in bestimmten Situationen", "Ja, das kommt häufig vor")),
            axe = Axe.REACTIVITE, scoreParOption = listOf(0, 2, 4), signalAlerte = true),

        QuestionChoix("a_un_probleme",
            tr("Y a-t-il un comportement particulier qui vous préoccupe en ce moment ?", "Is there a particular behaviour you are concerned about right now?", "Gibt es derzeit ein bestimmtes Verhalten, das Sie beschäftigt?"),
            trList(listOf("Oui, j'aimerais comprendre", "Non, tout va bien"),
                listOf("Yes, I'd like to understand", "No, everything is fine"),
                listOf("Ja, ich möchte es verstehen", "Nein, alles ist in Ordnung"))),

        QuestionChoix("apparition",
            tr("Le comportement qui vous préoccupe est apparu :", "The behaviour you are concerned about appeared:", "Das Verhalten, das Sie beschäftigt, ist aufgetreten:"),
            trList(listOf("Progressivement", "Du jour au lendemain, sans raison apparente", "Je ne sais pas vraiment"),
                listOf("Gradually", "Suddenly, with no apparent reason", "I'm not really sure"),
                listOf("Nach und nach", "Von einem Tag auf den anderen, ohne erkennbaren Grund", "Ich weiß es nicht genau"))),

        QuestionChoix("situation_principale",
            tr("Il apparaît principalement :", "It mainly occurs:", "Es tritt hauptsächlich auf:"),
            trList(listOf("Dans beaucoup de situations", "Surtout en votre absence", "Surtout à l'extérieur", "Surtout en votre présence"),
                listOf("In many situations", "Mainly in your absence", "Mainly outdoors", "Mainly in your presence"),
                listOf("In vielen Situationen", "Vor allem in Ihrer Abwesenheit", "Vor allem draußen", "Vor allem in Ihrer Anwesenheit"))),

        QuestionChoix("duree_probleme",
            tr("Depuis combien de temps observez-vous ce comportement ?", "How long have you been observing this behaviour?", "Seit wann beobachten Sie dieses Verhalten?"),
            trList(listOf("Moins d'1 semaine", "1 à 4 semaines", "Plusieurs mois", "Depuis toujours"),
                listOf("Less than 1 week", "1 to 4 weeks", "Several months", "Always"),
                listOf("Weniger als 1 Woche", "1 bis 4 Wochen", "Mehrere Monate", "Schon immer"))),

        QuestionChoix("evolution_probleme",
            tr("Ce comportement :", "This behaviour:", "Dieses Verhalten:"),
            trList(listOf("S'améliore", "Reste stable", "S'aggrave"),
                listOf("Is improving", "Is stable", "Is getting worse"),
                listOf("Wird besser", "Bleibt stabil", "Verschlimmert sich"))),

        QuestionChoix("frequence_probleme",
            tr("À quelle fréquence cela se produit-il ?", "How often does it occur?", "Wie oft kommt es vor?"),
            trList(listOf("Rarement", "Quelques fois par semaine", "Tous les jours", "Plusieurs fois par jour"),
                listOf("Rarely", "A few times a week", "Every day", "Several times a day"),
                listOf("Selten", "Einige Male pro Woche", "Jeden Tag", "Mehrmals täglich"))),

        QuestionChoix("intensite_probleme",
            tr("Quand cela arrive, c'est plutôt :", "When it happens, it is rather:", "Wenn es passiert, ist es eher:"),
            trList(listOf("Gérable facilement", "Gênant", "Difficile à gérer", "Perte de contrôle / dangereux"),
                listOf("Easily manageable", "Annoying", "Hard to manage", "Loss of control / dangerous"),
                listOf("Leicht zu handhaben", "Störend", "Schwer zu handhaben", "Kontrollverlust / gefährlich"))),

        QuestionChoix("generalisation_probleme",
            tr("Le comportement arrive plutôt :", "The behaviour occurs rather:", "Das Verhalten tritt eher auf:"),
            trList(listOf("Dans une situation bien précise", "Dans plusieurs situations différentes", "Dans la plupart des situations"),
                listOf("In one specific situation", "In several different situations", "In most situations"),
                listOf("In einer ganz bestimmten Situation", "In mehreren verschiedenen Situationen", "In den meisten Situationen"))),

        QuestionChoix("changement_recent",
            tr("Y a-t-il eu récemment un changement important dans sa vie ?", "Has there been a major change in your dog's life recently?", "Gab es kürzlich eine wichtige Veränderung in seinem Leben?"),
            trList(listOf("Aucun changement", "Un changement léger", "Un changement important (déménagement, bébé, séparation...)"),
                listOf("No change", "A minor change", "A major change (move, baby, separation...)"),
                listOf("Keine Veränderung", "Eine kleine Veränderung", "Eine große Veränderung (Umzug, Baby, Trennung …)"))),

        QuestionChoix("signe_physique",
            tr("Avez-vous remarqué un changement physique chez votre chien ces derniers temps ?", "Have you noticed a physical change in your dog recently?", "Haben Sie in letzter Zeit eine körperliche Veränderung bei Ihrem Hund bemerkt?"),
            trList(listOf("Non, rien de particulier", "Oui, il semble plus fatigué qu'avant",
                "Oui, il semble avoir mal ou être gêné dans ses mouvements", "Oui, autre chose a changé physiquement"),
                listOf("No, nothing particular", "Yes, it seems more tired than usual",
                    "Yes, it seems to be in pain or has difficulty moving", "Yes, something else has changed physically"),
                listOf("Nein, nichts Besonderes", "Ja, er wirkt müder als früher", "Ja, er scheint Schmerzen zu haben oder in seinen Bewegungen eingeschränkt zu sein", "Ja, etwas anderes hat sich körperlich verändert"))),

        QuestionChoix("lieu_residence",
            tr("Où habitez-vous ?", "Where do you live?", "Wo wohnen Sie?"),
            trList(listOf("Dans l'Essonne (91)", "Ailleurs en France", "Dans un autre pays francophone (Belgique, Suisse, Luxembourg…)"),
                listOf("In Essonne (91)", "Elsewhere in France", "In another French-speaking country (Belgium, Switzerland, Luxembourg…)"),
                listOf("Im Département Essonne (91)", "Anderswo in Frankreich", "In einem anderen französischsprachigen Land (Belgien, Schweiz, Luxemburg …)")))
    )
}