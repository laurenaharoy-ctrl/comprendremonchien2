package com.laurena.comprendremonchien

import android.os.LocaleList
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiNature
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.ui.graphics.vector.ImageVector

// ═══════════════════════════════════════════════════════════
// DÉTECTION DE LANGUE
// ═══════════════════════════════════════════════════════════

fun isEnglish(): Boolean {
    val locale = LocaleList.getDefault()[0]
    return locale.language == "en"
}

// ═══════════════════════════════════════════════════════════
// TEXTES GÉNÉRAUX
// ═══════════════════════════════════════════════════════════

fun strAppName() = if (isEnglish()) "Understanding My Dog" else "Comprendre mon chien"

// Boutons
fun strBtnDemarrerBilan() = if (isEnglish()) "Start the assessment" else "Démarrer le bilan"
fun strBtnReprendre() = if (isEnglish()) "Resume the questionnaire" else "Reprendre le questionnaire"
fun strBtnDictionnaire() = if (isEnglish()) "Dictionary" else "Dictionnaire"
fun strBtnAlimentation() = if (isEnglish()) "Feeding" else "Alimentation"
fun strBtnCommencer() = if (isEnglish()) "Get started" else "Commencer"
fun strBtnContinuer() = if (isEnglish()) "Continue" else "Continuer"
fun strBtnSuivant() = if (isEnglish()) "Next" else "Suivant"
fun strBtnRetour() = if (isEnglish()) "Back" else "Retour"
fun strBtnPasser() = if (isEnglish()) "Skip" else "Passer"
fun strBtnPartager() = if (isEnglish()) "Share" else "Partager"
fun strBtnExportPdf() = if (isEnglish()) "Export PDF" else "Export PDF"
fun strBtnCopierResume() = if (isEnglish()) "Copy summary" else "Copier le résumé"
fun strBtnRecommencer() = if (isEnglish()) "Start over" else "Recommencer depuis le début"
fun strBtnVoirLivres() = if (isEnglish()) "See my books" else "Voir mes livres"
fun strBtnRevoirIntroduction() = if (isEnglish()) "Review the introduction" else "Revoir l'introduction"
fun strBtnPolitiqueConfidentialite() = if (isEnglish()) "Privacy policy" else "Politique de confidentialité"
fun strBtnRetourRubriques() = if (isEnglish()) "Back to categories" else "Retour aux rubriques"
fun strBtnRetourCategorie() = if (isEnglish()) "Back to category" else "Retour à la catégorie"
fun strBtnSupprimer() = if (isEnglish()) "Delete" else "Supprimer"
fun strBtnSupprimerTout() = if (isEnglish()) "Delete all history" else "Supprimer tout l'historique"
fun strBtnSupprimerBilan() = if (isEnglish()) "Delete this assessment" else "Supprimer ce bilan"
fun strBtnAnnuler() = if (isEnglish()) "Cancel" else "Annuler"
fun strBtnEnvoyerSignalement() = if (isEnglish()) "Send report" else "Envoyer le signalement"

// Titres écrans
fun strScreenQuestionnaire() = if (isEnglish()) "Questionnaire" else "Questionnaire"
fun strScreenAnalyse() = if (isEnglish()) "Analysis" else "Analyse"
fun strScreenResultat() = if (isEnglish()) "Results" else "Résultat"
fun strScreenDictionnaire() = if (isEnglish()) "Behaviour dictionary" else "Dictionnaire comportemental"
fun strScreenFicheComportementale() = if (isEnglish()) "Behaviour fact sheet" else "Fiche comportementale"
fun strScreenAlimentation() = if (isEnglish()) "Feeding" else "Alimentation"
fun strScreenSignalement() = if (isEnglish()) "Report" else "Signalement"
fun strScreenHistorique() = if (isEnglish()) "Assessment history" else "Historique des bilans"
fun strScreenDetailBilan() = if (isEnglish()) "Assessment detail" else "Détail du bilan"
fun strScreenParametres() = if (isEnglish()) "Settings" else "Paramètres"

// ═══════════════════════════════════════════════════════════
// INTRODUCTION
// ═══════════════════════════════════════════════════════════

fun strIntroKicker() = if (isEnglish()) "Before you begin" else "Avant de commencer"
fun strIntroDuree() = if (isEnglish()) "This questionnaire will take about 5 minutes." else "Ce questionnaire vous prendra environ 5 minutes."
fun strIntroExplorerKicker() = if (isEnglish()) "What you will explore" else "Ce que vous allez explorer"
fun strIntroExplorer1() = if (isEnglish()) "Emotional sensitivity" else "Sa sensibilité émotionnelle"
fun strIntroExplorer2() = if (isEnglish()) "Need for attachment" else "Son besoin d'attachement"
fun strIntroExplorer3() = if (isEnglish()) "Excitement management" else "Sa gestion de l'excitation"
fun strIntroExplorer4() = if (isEnglish()) "Reactivity to the environment" else "Sa réactivité à l'environnement"

// ═══════════════════════════════════════════════════════════
// QUESTIONNAIRE
// ═══════════════════════════════════════════════════════════

fun strQuestionReponseLabel() = if (isEnglish()) "Your answer" else "Votre réponse"
fun strQuestionReponsePlaceholder() = if (isEnglish()) "E.g. Rocky" else "Ex. Rocky"
fun strQuestionHintTexte() = if (isEnglish()) "Enter an answer to continue" else "Saisissez une réponse pour continuer"
fun strQuestionHintChoix() = if (isEnglish()) "Choose an answer to continue" else "Choisissez une réponse pour continuer"

// ═══════════════════════════════════════════════════════════
// CHARGEMENT
// ═══════════════════════════════════════════════════════════

fun strChargementMessages() = if (isEnglish())
    listOf("Analysis in progress…", "Reading your dog's profile…", "Preparing your assessment…")
else
    listOf("Analyse en cours…", "Lecture du profil de votre chien…", "Préparation de votre bilan…")

// ═══════════════════════════════════════════════════════════
// RÉSULTAT
// ═══════════════════════════════════════════════════════════

fun strResultatKicker() = if (isEnglish()) "Your assessment" else "Votre bilan"
fun strResultatTitreBilan(nom: String) = if (isEnglish()) "Assessment for $nom" else "Bilan pour $nom"
fun strResultatLecturePrincipale() = if (isEnglish()) "Main reading" else "Lecture principale"
fun strResultatPriorite(p: String) = if (isEnglish()) "Priority: $p" else "Priorité : $p"
fun strResultatRessent(nom: String) = if (isEnglish()) "What $nom is probably feeling" else "Ce que ressent probablement $nom"
fun strResultatCoupOeil() = if (isEnglish()) "At a glance" else "En un coup d'œil"
fun strResultatFacteurs() = if (isEnglish()) "Identified factors" else "Facteurs repérés"
fun strResultatNiveauSituation() = if (isEnglish()) "Situation level" else "Niveau de situation"
fun strResultatInquieter() = if (isEnglish()) "Should you be concerned?" else "Faut-il s'inquiéter ?"
fun strResultatSePasse() = if (isEnglish()) "What is probably happening" else "Ce qui se passe probablement"
fun strResultatLevierPrincipal() = if (isEnglish()) "First concrete step" else "Première piste concrète"
fun strResultatPointAppui() = if (isEnglish()) "The main lever" else "Le point d'appui principal"
fun strResultatPourquoi() = if (isEnglish()) "Why is your dog like this?" else "Pourquoi est-il comme ça ?"
fun strResultatComprendreAgir() = if (isEnglish()) "Understand to act better" else "Comprendre pour mieux agir"
fun strResultatChangement() = if (isEnglish()) "This is often where change begins to take shape." else "C'est souvent ici que le changement commence à prendre forme."
fun strResultat3Jours() = if (isEnglish()) "The next 3 days" else "Les 3 prochains jours"
fun strResultatAFaire() = if (isEnglish()) "To do" else "À faire"
fun strResultatAEviter() = if (isEnglish()) "To avoid" else "À éviter"
fun strResultatAObserver() = if (isEnglish()) "To observe" else "À observer"
fun strResultatConseilsComplementaires() = if (isEnglish()) "Additional advice" else "Conseils complémentaires"
fun strResultatQuandAide() = if (isEnglish()) "When to seek help" else "Quand demander de l'aide"
fun strResultatMorsurePro() = if (isEnglish()) "A bite has been reported — professional support is recommended." else "Une morsure a été signalée — un accompagnement professionnel est recommandé."
fun strResultatImportant() = if (isEnglish()) "Important" else "Important"
fun strResultatDisclaimer() = if (isEnglish()) "This assessment is indicative. It does not replace a vet or a behaviour professional." else "Ce bilan reste indicatif. Il ne remplace ni un vétérinaire ni un professionnel du comportement."
fun strResultatAllerPlusLoin(nom: String) = if (isEnglish()) "Going further with $nom" else "Pour aller plus loin avec $nom"
fun strResultatFichesComportementales() = if (isEnglish()) "Behaviour fact sheets" else "Fiches comportementales"
fun strResultatReperes() = if (isEnglish()) "Feeding guidelines" else "Repères alimentation"
fun strResultatARetenir() = if (isEnglish()) "Key takeaway" else "À retenir"
fun strResultatLeLivre() = if (isEnglish()) "The book" else "Le livre"
fun strResultatAllerPlusLoinLivre() = if (isEnglish()) "If you want to go further" else "Si vous souhaitez aller plus loin"
fun strResultatCopie() = if (isEnglish()) "Copied" else "Copié"
fun strResultatProfilRace() = if (isEnglish()) "Breed profile" else "Profil de race"
fun strResultatPredispositions() = if (isEnglish()) "Common predispositions in this family" else "Prédispositions fréquentes dans cette famille"

// ═══════════════════════════════════════════════════════════
// MORSURE
// ═══════════════════════════════════════════════════════════

fun strMorsureTitre() = if (isEnglish()) "WARNING — BITE REPORTED" else "ATTENTION — MORSURE SIGNALÉE"
fun strMorsuTexte(nom: String) = if (isEnglish()) "There has already been a bite involving $nom. This situation should not be taken lightly." else "Il y a déjà eu morsure chez $nom. Cette situation ne doit pas être banalisée."
fun strMorsuConseil() = if (isEnglish()) "Support from a behaviour professional is strongly recommended." else "Un accompagnement par un professionnel du comportement est fortement recommandé."

// ═══════════════════════════════════════════════════════════
// DICTIONNAIRE COMPORTEMENTAL
// ═══════════════════════════════════════════════════════════

fun strDicoTitre() = if (isEnglish()) "Behaviour dictionary" else "Dictionnaire comportemental"
fun strDicoSousTitre() = if (isEnglish()) "Landmarks for reading your dog's body language" else "Repères pour mieux lire le langage du chien"
fun strDicoRecherchePlaceholder() = if (isEnglish()) "Search a fact sheet…" else "Rechercher une fiche…"
fun strDicoAucunResultat(q: String) = if (isEnglish()) "No fact sheet matches \"$q\"." else "Aucune fiche ne correspond à \"$q\"."
fun strDicoImportant() = if (isEnglish()) "Important" else "Important"
fun strDicoDisclaimer() = if (isEnglish()) "These fact sheets provide reading landmarks. They do not replace professional advice." else "Ces fiches donnent des repères de lecture. Elles ne remplacent pas l'avis d'un professionnel."
fun strDicoRappel() = if (isEnglish()) "An isolated behaviour is not always enough to draw conclusions. Context and overall body language matter just as much." else "Un comportement isolé ne suffit pas toujours à conclure. Le contexte et l'ensemble du langage corporel comptent autant."
fun strDicoFicheKicker() = if (isEnglish()) "Behaviour fact sheet" else "Fiche comportementale"
fun strDicoExplication() = if (isEnglish()) "Explanation" else "Explication"
fun strDicoQueFaire() = if (isEnglish()) "What to do" else "Que faire"
fun strDicoAEviter() = if (isEnglish()) "What to avoid" else "À éviter"
fun strDicoRappelKicker() = if (isEnglish()) "Reminder" else "Rappel"
fun strDicoFicheIntrouvable() = if (isEnglish()) "Fact sheet not found." else "Fiche introuvable."

// ═══════════════════════════════════════════════════════════
// ALIMENTATION
// ═══════════════════════════════════════════════════════════

fun strAlimTitre() = if (isEnglish()) "Feeding your dog" else "Alimentation du chien"
fun strAlimSousTitre() = if (isEnglish()) "Practical landmarks for feeding your dog with confidence." else "Repères pratiques pour nourrir votre chien sereinement."
fun strAlimARetenirKicker() = if (isEnglish()) "Keep in mind first" else "À retenir d'abord"
fun strAlimRetenir1() = if (isEnglish()) "Any dietary change must be gradual." else "Tout changement alimentaire doit être progressif."
fun strAlimRetenir2() = if (isEnglish()) "Even a food that seems harmless to humans may be unsuitable for dogs." else "Même un aliment banal pour l'humain peut être inadapté pour le chien."
fun strAlimRetenir3() = if (isEnglish()) "If in doubt about ingestion or symptoms, caution comes before waiting." else "En cas d'ingestion suspecte ou de symptômes, la prudence passe avant l'attente."
fun strAlimImportant() = if (isEnglish()) "Important" else "Important"
fun strAlimDisclaimer() = if (isEnglish()) "This guide provides general landmarks. It does not replace a vet." else "Ce guide donne des repères généraux. Il ne remplace pas un vétérinaire."
fun strAlimRappel() = if (isEnglish()) "If symptoms appear or ingestion seems doubtful, seek veterinary advice." else "En cas de symptômes ou d'ingestion douteuse, privilégiez un avis vétérinaire."
fun strAlimCatDangereuxDesc() = if (isEnglish()) "Foods to avoid to stay on the safe side." else "Les aliments à éviter pour ne pas faire d'erreur."
fun strAlimCatAutorisesDesc() = if (isEnglish()) "Basic landmarks for giving food without guessing." else "Les repères de base pour donner sans improviser."
fun strAlimCatIngestionDesc() = if (isEnglish()) "The right reflexes if your dog has swallowed something." else "Les bons réflexes si le chien a avalé quelque chose."
fun strAlimCatDigestionDesc() = if (isEnglish()) "Grass, vomiting, stools and small digestive signals." else "Herbe, vomissements, selles et petits signaux digestifs."

// ═══════════════════════════════════════════════════════════
// PARAMÈTRES
// ═══════════════════════════════════════════════════════════

fun strParamsKicker() = if (isEnglish()) "Settings" else "Paramètres"
fun strParamsAppTitre() = if (isEnglish()) "Understanding My Dog" else "Comprendre mon chien"
fun strParamsVersion(v: String) = if (isEnglish()) "Version $v" else "Version $v"
fun strParamsTutorielKicker() = if (isEnglish()) "Tutorial" else "Tutoriel"
fun strParamsTutorielTexte() = if (isEnglish()) "Review the app presentation from the beginning." else "Revoir la présentation de l'application depuis le début."
fun strParamsConfidentialiteKicker() = if (isEnglish()) "Privacy" else "Confidentialité"
fun strParamsConfidentialiteTexte() = if (isEnglish()) "This app does not collect any personal data. Assessments are stored on your device only. Notifications are local." else "Cette application ne collecte aucune donnée personnelle. Les bilans sont stockés uniquement sur votre appareil. Les notifications sont locales."
fun strParamsAProposKicker() = if (isEnglish()) "About" else "À propos"
fun strParamsAProposTexte() = if (isEnglish()) "Designed with care to help owners better understand their dog." else "Développée avec soin pour aider les maîtres à mieux comprendre leur chien."

// ═══════════════════════════════════════════════════════════
// HISTORIQUE
// ═══════════════════════════════════════════════════════════

fun strHistoriqueAucun() = if (isEnglish()) "No saved assessments yet." else "Aucun bilan sauvegardé pour l'instant."
fun strHistoriqueNbBilans(n: Int) = if (isEnglish())
    if (n > 1) "$n saved assessments" else "$n saved assessment"
else
    if (n > 1) "$n bilans sauvegardés" else "$n bilan sauvegardé"
fun strHistoriqueVideTexte() = if (isEnglish()) "Completed assessments will appear here automatically after each questionnaire." else "Les bilans réalisés apparaîtront ici automatiquement après chaque questionnaire complété."
fun strHistoriqueSupprimerTitre() = if (isEnglish()) "Delete this assessment?" else "Supprimer ce bilan ?"
fun strHistoriqueSupprimerTexte(nom: String, date: String) = if (isEnglish()) "The assessment for $nom on $date will be permanently deleted." else "Le bilan de $nom du $date sera supprimé définitivement."
fun strHistoriqueSupprimerToutTitre() = if (isEnglish()) "Delete all history?" else "Supprimer tout l'historique ?"
fun strHistoriqueSupprimerToutTexte() = if (isEnglish()) "This action cannot be undone. All saved assessments will be permanently deleted." else "Cette action est irréversible. Tous les bilans sauvegardés seront supprimés définitivement."
fun strHistoriqueDetailKicker() = if (isEnglish()) "Saved assessment" else "Bilan sauvegardé"
fun strHistoriqueSynthese() = if (isEnglish()) "Summary" else "Synthèse"
fun strHistoriqueCarte() = if (isEnglish()) "Profile card" else "Carte du profil"
fun strHistoriqueLecture() = if (isEnglish()) "Main reading" else "Lecture principale"
fun strHistoriquePiste() = if (isEnglish()) "First concrete step" else "Première piste concrète"
fun strHistoriqueRappelKicker() = if (isEnglish()) "Reminder" else "Rappel"
fun strHistoriqueRappelTexte() = if (isEnglish()) "This assessment is an indicative record. Your dog's situation may have changed since then." else "Ce bilan est un enregistrement indicatif. La situation de votre chien peut avoir évolué depuis."

// ═══════════════════════════════════════════════════════════
// FEEDBACK
// ═══════════════════════════════════════════════════════════

fun strFeedbackKicker() = if (isEnglish()) "Report" else "Signalement"
fun strFeedbackTitre() = if (isEnglish()) "Something wrong?" else "Quelque chose ne va pas ?"
fun strFeedbackSousTitre() = if (isEnglish()) "Your feedback is valuable. Describe the issue or suggestion and we will take it into account." else "Votre retour est précieux. Décrivez le problème ou la suggestion et nous en tiendrons compte."
fun strFeedbackTypeKicker() = if (isEnglish()) "Report type" else "Type de signalement"
fun strFeedbackEcranKicker() = if (isEnglish()) "Screen concerned" else "Écran concerné"
fun strFeedbackDescriptionKicker() = if (isEnglish()) "Description" else "Description"
fun strFeedbackDescriptionTexte() = if (isEnglish()) "Describe the issue or your suggestion in as much detail as possible." else "Décrivez le problème ou votre suggestion avec le plus de détails possible."
fun strFeedbackPlaceholder() = if (isEnglish()) "E.g.: On the results screen, the Export PDF button doesn't work…" else "Ex. : Sur l'écran résultat, le bouton Export PDF ne fonctionne pas…"
fun strFeedbackDetailsHint() = if (isEnglish()) "Add a few details to help us understand." else "Ajoutez quelques détails pour nous aider à comprendre."
fun strFeedbackHintCategorie() = if (isEnglish()) "Choose a category" else "Choisissez une catégorie"
fun strFeedbackHintMessage() = if (isEnglish()) "write a message" else "rédigez un message"
fun strFeedbackHintEt() = if (isEnglish()) " and " else " et "
fun strFeedbackHintPour() = if (isEnglish()) " to send." else " pour envoyer."
fun strFeedbackConfidentialiteKicker() = if (isEnglish()) "Privacy" else "Confidentialité"
fun strFeedbackConfidentialiteTexte() = if (isEnglish()) "Your report is sent by email directly from your app. No personal data is collected automatically." else "Votre signalement est envoyé par email directement depuis votre application. Aucune donnée personnelle n'est collectée automatiquement."
fun strFeedbackMerciTitre() = if (isEnglish()) "Thank you for your feedback!" else "Merci pour votre retour !"
fun strFeedbackMerciTexte() = if (isEnglish()) "Your message has been sent. It will help improve the app." else "Votre message a bien été transmis. Il contribuera à améliorer l'application."
fun strFeedbackAucuneAppliEmail() = if (isEnglish()) "No email app found on this device." else "Aucune application email trouvée sur cet appareil."

// ═══════════════════════════════════════════════════════════
// ONBOARDING
// ═══════════════════════════════════════════════════════════

fun strOnboardingPasser() = if (isEnglish()) "Skip" else "Passer"

// ═══════════════════════════════════════════════════════════
// NOTIFICATIONS
// ═══════════════════════════════════════════════════════════

fun strNotifChannelNom() = if (isEnglish()) "Assessment reminders" else "Rappels bilan"
fun strNotifChannelDesc() = if (isEnglish()) "Reminders to redo your dog's assessment" else "Rappels pour refaire le bilan de votre chien"
fun strNotifTitre() = if (isEnglish()) "Time for a new assessment?" else "Et si vous refaisiez le bilan ?"
fun strNotifTexte(nom: String) = if (isEnglish()) "A lot may have changed for $nom 🐾" else "Beaucoup de choses peuvent avoir évolué pour $nom 🐾"

// ═══════════════════════════════════════════════════════════
// TEXTE PARTAGÉ
// ═══════════════════════════════════════════════════════════

fun strPartageTitre(nom: String) = if (isEnglish()) "Emotional assessment for $nom" else "Bilan émotionnel pour $nom"
fun strPartageHypothese() = if (isEnglish()) "Hypothesis:" else "Hypothèse :"
fun strPartagePriorite() = if (isEnglish()) "Priority:" else "Priorité :"
fun strPartageScores() = if (isEnglish()) "Scores:" else "Scores :"
fun strPartageSensibilite(v: String) = if (isEnglish()) "Sensitivity: $v" else "Sensibilité : $v"
fun strPartageAttachement(v: String) = if (isEnglish()) "Attachment: $v" else "Attachement : $v"
fun strPartageImpulsivite(v: String) = if (isEnglish()) "Impulsivity: $v" else "Impulsivité : $v"
fun strPartageReactivite(v: String) = if (isEnglish()) "Reactivity: $v" else "Réactivité : $v"
fun strPartageIndicatif() = if (isEnglish()) "⚠️ Indicative assessment" else "⚠️ Bilan indicatif"

// ═══════════════════════════════════════════════════════════
// PDF
// ═══════════════════════════════════════════════════════════

fun strPdfBilanEmotionnel() = if (isEnglish()) "Emotional assessment" else "Bilan émotionnel"
fun strPdfFooter() = if (isEnglish()) "Understanding My Dog  •  Indicative emotional assessment" else "Comprendre mon chien  •  Bilan émotionnel indicatif"
fun strPdfPage(n: Int, total: Int = 4) = if (isEnglish()) "Page $n / $total" else "Page $n / $total"
fun strPdf4Axes() = if (isEnglish()) "The 4 dimensions" else "Les 4 dimensions"
fun strPdfHypothese() = if (isEnglish()) "Reading hypothesis" else "Hypothèse de lecture"
fun strPdfSePasse() = if (isEnglish()) "What is probably happening" else "Ce qui se passe probablement"
fun strPdfFacteurs() = if (isEnglish()) "Identified factors" else "Facteurs repérés"
fun strPdfAggravants() = if (isEnglish()) "What may make things worse" else "Ce qui peut aggraver"
fun strPdfProtecteurs() = if (isEnglish()) "What is already helping" else "Ce qui protège déjà"
fun strPdfPlanAction(nom: String) = if (isEnglish()) "Action plan for $nom" else "Plan d'action pour $nom"
fun strPdfLevier() = if (isEnglish()) "First useful lever" else "Premier levier utile"
fun strPdfProchainsJours() = if (isEnglish()) "The coming days" else "Les prochains jours"
fun strPdfConseils() = if (isEnglish()) "Additional advice" else "Conseils complémentaires"
fun strPdfARetenir() = if (isEnglish()) "Key takeaway" else "À retenir"
fun strPdfConclusion() = if (isEnglish()) "Conclusion" else "Conclusion"
fun strPdfConclusionTexte(nom: String) = if (isEnglish())
    "The goal is not to label $nom, but to help read the situation more clearly and move forward in a more adapted, concrete and reassuring way."
else
    "L'objectif n'est pas d'étiqueter $nom, mais d'aider à mieux lire ce qui se passe et à avancer de manière plus adaptée, plus concrète et plus rassurante."
fun strPdfDisclaimer() = if (isEnglish())
    "This assessment is indicative. It does not replace the advice of a vet or an animal behaviour professional. It can be used as a basis for discussion during a consultation."
else
    "Ce bilan est indicatif. Il ne remplace pas l'avis d'un vétérinaire ni d'un professionnel du comportement animal. Il peut servir de base de discussion lors d'une consultation."
fun strPdfGenereAuto() = if (isEnglish()) "Automatically generated document" else "Document généré automatiquement"
fun strPdfRetrouvez() = if (isEnglish()) "Find the app to track your dog's progress." else "Retrouvez l'application pour suivre l'évolution de votre chien."
fun strPdfAcceder() = if (isEnglish()) "Access the app — comprendremonchien.fr" else "Accéder à l'application — comprendremonchien.fr"
fun strPdfMorsuTexte() = if (isEnglish())
    "A bite was reported during this assessment. Professional support is recommended to evaluate the situation and make daily life safer."
else
    "Une morsure a été signalée lors de ce bilan. Un accompagnement professionnel est recommandé pour évaluer la situation et sécuriser le quotidien."
fun strPdfEnUnCoup() = if (isEnglish()) "At a glance" else "En un coup d'œil"
fun strPdfAxePrincipal() = if (isEnglish()) "Main axis" else "Axe principal"
fun strPdfSituation() = if (isEnglish()) "Situation" else "Situation"
fun strPdfBesoin() = if (isEnglish()) "Main need" else "Besoin principal"
fun strPdfAide() = if (isEnglish()) "Support to consider" else "Aide à envisager"
fun strPdfProfil(nom: String) = if (isEnglish()) "Profile of $nom" else "Profil de $nom"
fun strPdfAideComportementalisteRec() = if (isEnglish()) "Behaviourist recommended" else "Comportementaliste recommandé"
fun strPdfAideProRapide() = if (isEnglish()) "Professional quickly" else "Professionnel rapidement"
fun strPdfAideComportementaliste() = if (isEnglish()) "Behaviourist" else "Comportementaliste"
fun strPdfAideEducateur() = if (isEnglish()) "Dog trainer" else "Éducateur canin"
fun strPdfAideEducateurBesoin() = if (isEnglish()) "Dog trainer if needed" else "Éducateur canin si besoin"
fun strPdfRecapProfil(nom: String, profil: String) = if (isEnglish()) "$nom primarily shows a $profil profile." else "$nom présente surtout un profil $profil."
fun strPdfRecapSituation(s: String) = if (isEnglish()) "Situation: $s." else "Situation : $s."
fun strPdfRecapAxe(a: String) = if (isEnglish()) "Main axis: $a." else "Axe principal : $a."

// ═══════════════════════════════════════════════════════════
// PRIORITÉS / NIVEAUX
// ═══════════════════════════════════════════════════════════

fun strPrioriteAction(p: PrioriteAction) = when (p) {
    PrioriteAction.FAIBLE -> if (isEnglish()) "Low" else "Faible"
    PrioriteAction.MODEREE -> if (isEnglish()) "Moderate" else "Modérée"
    PrioriteAction.ELEVEE -> if (isEnglish()) "High" else "Élevée"
    PrioriteAction.URGENTE -> if (isEnglish()) "Urgent" else "Urgente"
}

fun strNiveauAxe(n: NiveauAxe) = when (n) {
    NiveauAxe.PEU_MARQUE -> if (isEnglish()) "Low" else "Peu marqué"
    NiveauAxe.A_SURVEILLER -> if (isEnglish()) "Watch" else "À surveiller"
    NiveauAxe.MARQUE -> if (isEnglish()) "Marked" else "Marqué"
    NiveauAxe.TRES_MARQUE -> if (isEnglish()) "Very marked" else "Très marqué"
}

fun strNiveauSituation(n: NiveauSituation) = when (n) {
    NiveauSituation.STABLE -> if (isEnglish()) "Stable" else "Stable"
    NiveauSituation.A_TRAVAILLER -> if (isEnglish()) "To work on" else "À travailler"
    NiveauSituation.SENSIBLE -> if (isEnglish()) "Sensitive" else "Sensible"
}

fun strLibelleAxe(axe: Axe) = when (axe) {
    Axe.PEUR -> if (isEnglish()) "Sensitivity" else "Sensibilité"
    Axe.ATTACHEMENT -> if (isEnglish()) "Attachment" else "Attachement"
    Axe.IMPULSIVITE -> if (isEnglish()) "Impulsivity" else "Impulsivité"
    Axe.REACTIVITE -> if (isEnglish()) "Reactivity" else "Réactivité"
}

// ═══════════════════════════════════════════════════════════
// SECTIONS QUESTIONNAIRE
// ═══════════════════════════════════════════════════════════

fun strTitreSection(questionId: String) = when (questionId) {
    "nom_chien", "age", "sterilise", "senior_desorientation", "senior_vocalise_nocturne" -> if (isEnglish()) "General information" else "Informations générales"
    "race_categorie" -> if (isEnglish()) "Your dog" else "Votre chien"
    "peur_stimuli", "adaptation_changements", "comportement_exterieur", "reaction_peur" ->
        if (isEnglish()) "Sensitivity and fear" else "Sensibilité et peur"
    "support_absences", "pendant_absence", "suit_partout", "autre_personne_apaise",
    "proprete_maison", "si_non_quand", "proprete_type", "marquage_habitude_post_sterilisation" ->
        if (isEnglish()) "Attachment and separation" else "Attachement et séparation"
    "calmer_apres_excitation", "jeu_comportement", "vole_objets", "poursuite_mouvement" ->
        if (isEnglish()) "Excitement and impulsivity" else "Excitation et impulsivité"
    "reaction_inconnus", "reaction_chiens", "a_deja_mordu", "cible_agression", "defense_ressources" ->
        if (isEnglish()) "Reactivity" else "Réactivité"
    "a_un_probleme" -> if (isEnglish()) "Going further" else "Pour aller plus loin"
    else -> if (isEnglish()) "Current context" else "Contexte actuel"
}

// ═══════════════════════════════════════════════════════════
// ÉCRANS FEEDBACK
// ═══════════════════════════════════════════════════════════

fun strEcransFeedback() = if (isEnglish())
    listOf("Home", "Introduction", "Questionnaire", "Results", "Behaviour dictionary", "Feeding", "History", "General / Other")
else
    listOf("Accueil", "Introduction", "Questionnaire", "Résultat", "Dictionnaire comportemental", "Alimentation", "Historique", "Général / Autre")

// ═══════════════════════════════════════════════════════════
// CATÉGORIES SIGNALEMENT
// ═══════════════════════════════════════════════════════════

fun strCategoriesSignalement(): List<CategorieSignalement> {
    return if (isEnglish()) listOf(
        CategorieSignalement("bug", "Bug / Technical issue", "The app crashes, a button doesn't work, a screen is frozen…", Icons.Rounded.BugReport),
        CategorieSignalement("contenu", "Incorrect content", "Some information seems wrong, a text is unclear…", Icons.Rounded.HelpOutline),
        CategorieSignalement("suggestion", "Suggestion", "An idea to improve the app or add a feature…", Icons.Rounded.Lightbulb),
        CategorieSignalement("autre", "Other", "Anything that doesn't fit the other categories.", Icons.Rounded.MoreHoriz)
    ) else listOf(
        CategorieSignalement("bug", "Bug / Problème technique", "L'appli plante, un bouton ne fonctionne pas, un écran est bloqué…", Icons.Rounded.BugReport),
        CategorieSignalement("contenu", "Contenu incorrect", "Une information semble erronée, un texte est incompréhensible…", Icons.Rounded.HelpOutline),
        CategorieSignalement("suggestion", "Suggestion", "Une idée pour améliorer l'appli ou ajouter une fonctionnalité…", Icons.Rounded.Lightbulb),
        CategorieSignalement("autre", "Autre", "Tout ce qui ne rentre pas dans les catégories ci-dessus.", Icons.Rounded.MoreHoriz)
    )
}

// ═══════════════════════════════════════════════════════════
// FORMAT DATE HISTORIQUE
// ═══════════════════════════════════════════════════════════

fun strDateFormatHistorique() = if (isEnglish()) "MMMM dd, yyyy 'at' HH:mm" else "dd MMMM yyyy 'à' HH'h'mm"

// ═══════════════════════════════════════════════════════════
// CONTENT DESCRIPTIONS
// ═══════════════════════════════════════════════════════════

fun strContentDescRetour() = if (isEnglish()) "Back" else "Retour"
fun strContentDescHistorique() = if (isEnglish()) "Assessment history" else "Historique des bilans"
fun strContentDescParametres() = if (isEnglish()) "Settings" else "Paramètres"
fun strContentDescSignalement() = if (isEnglish()) "Report an issue" else "Signaler un problème"
fun strContentDescMorsure() = if (isEnglish()) "Bite reported" else "Morsure signalée"
fun strContentDescSupprimer() = if (isEnglish()) "Delete" else "Supprimer"
fun strContentDescLogo() = if (isEnglish()) "Understanding My Dog logo" else "Logo Comprendre mon chien"

// ═══════════════════════════════════════════════════════════
// CHOOSERS PARTAGE
// ═══════════════════════════════════════════════════════════

fun strPartageChooser() = if (isEnglish()) "Share" else "Partager"
fun strPartagePdfChooser() = if (isEnglish()) "Share PDF" else "Partager PDF"
fun strSignalementChooser() = if (isEnglish()) "Send report" else "Envoyer le signalement"

// ═══════════════════════════════════════════════════════════
// RÉSUMÉ ÉMOTIONNEL (Models.kt)
// ═══════════════════════════════════════════════════════════

fun strResumeEmotionnel(axe: Axe) = when (axe) {
    Axe.PEUR -> if (isEnglish()) "Sensitive and easily affected by the environment" else "Sensible et facilement impacté par son environnement"
    Axe.ATTACHEMENT -> if (isEnglish()) "Very attached, hard to detach" else "Très attaché, difficile à détacher"
    Axe.IMPULSIVITE -> if (isEnglish()) "Gets excited quickly" else "Monte vite en excitation"
    Axe.REACTIVITE -> if (isEnglish()) "Reacts quickly to stimuli" else "Réagit rapidement aux stimuli"
}

fun strIntentionChien(axe: Axe) = when (axe) {
    Axe.PEUR -> if (isEnglish()) "He is mainly trying to manage what frightens him." else "Il essaie surtout de gérer ce qui lui fait peur."
    Axe.ATTACHEMENT -> if (isEnglish()) "He is looking to stay safe with you." else "Il cherche à rester en sécurité avec vous."
    Axe.IMPULSIVITE -> if (isEnglish()) "He is trying to manage his excitement." else "Il tente de gérer son excitation."
    Axe.REACTIVITE -> if (isEnglish()) "He is trying to respond to an overly intense environment." else "Il essaie de répondre à un environnement trop intense."
}

fun strBesoinPrincipal(axe: Axe) = when (axe) {
    Axe.PEUR -> if (isEnglish()) "Main need: to feel safe." else "Besoin principal : se sentir en sécurité."
    Axe.ATTACHEMENT -> if (isEnglish()) "Main need: to build independence." else "Besoin principal : gagner en autonomie."
    Axe.IMPULSIVITE -> if (isEnglish()) "Main need: learning to come back down." else "Besoin principal : apprendre à redescendre."
    Axe.REACTIVITE -> if (isEnglish()) "Main need: to find calm again." else "Besoin principal : retrouver du calme."
}

fun strPhraseFin(nom: String) = if (isEnglish())
    "Every dog is unique. This assessment provides landmarks for $nom, but daily observation remains essential."
else
    "Chaque chien est unique. Ce bilan donne des repères pour $nom, mais l'observation du quotidien reste essentielle."

fun strTexteVigilance(niveau: NiveauVigilance, nom: String) = when (niveau) {
    NiveauVigilance.FAIBLE -> if (isEnglish()) "At this stage, nothing stands out as particularly concerning for $nom." else "À ce stade, rien ne ressort comme particulièrement préoccupant pour $nom."
    NiveauVigilance.MODEREE -> if (isEnglish()) "A few elements deserve particular attention for $nom." else "Quelques éléments méritent une attention particulière pour $nom."
    NiveauVigilance.ELEVEE -> if (isEnglish()) "Some responses suggest it would be best not to let the situation develop on its own for $nom." else "Certaines réponses invitent à ne pas laisser la situation s'installer seule pour $nom."
}

// ═══════════════════════════════════════════════════════════
// ONBOARDING SLIDES
// ═══════════════════════════════════════════════════════════

fun strOnboardingSlides(): List<OnboardingSlide> {
    return if (isEnglish()) listOf(
        OnboardingSlide(
            kicker = "Welcome",
            titre = "Understanding My Dog",
            description = "This app helps you decode your dog's behaviour and get concrete advice tailored to his unique profile.",
            illustrationType = IllustrationType.CHIEN_ASSIS
        ),
        OnboardingSlide(
            kicker = "How it works",
            titre = "A questionnaire, four dimensions",
            description = "In just a few minutes, you explore the four axes that shape your dog's everyday behaviour.",
            illustrationType = IllustrationType.QUATRE_AXES,
            features = listOf<Pair<ImageVector, String>>(
                Icons.Rounded.Psychology to "Emotional sensitivity",
                Icons.Rounded.Favorite to "Need for attachment",
                Icons.Rounded.EmojiNature to "Excitement management",
                Icons.Rounded.Analytics to "Reactivity to the environment"
            )
        ),
        OnboardingSlide(
            kicker = "What you get",
            titre = "A complete personalised assessment",
            description = "At the end of the questionnaire, you receive a detailed emotional assessment with concrete advice, an action plan and a PDF to share with your vet.",
            illustrationType = IllustrationType.BILAN_COMPLET,
            features = listOf<Pair<ImageVector, String>>(
                Icons.Rounded.CheckCircle to "Emotional assessment",
                Icons.Rounded.PictureAsPdf to "4-page PDF export",
                Icons.Rounded.History to "Assessment history"
            )
        )
    ) else listOf(
        OnboardingSlide(
            kicker = "Bienvenue",
            titre = "Comprendre mon chien",
            description = "Cette application vous aide à décoder les comportements de votre chien et à obtenir des pistes concrètes adaptées à son profil unique.",
            illustrationType = IllustrationType.CHIEN_ASSIS
        ),
        OnboardingSlide(
            kicker = "Comment ça marche",
            titre = "Un questionnaire, quatre dimensions",
            description = "En quelques minutes, vous explorez les quatre axes qui façonnent le comportement de votre chien au quotidien.",
            illustrationType = IllustrationType.QUATRE_AXES,
            features = listOf<Pair<ImageVector, String>>(
                Icons.Rounded.Psychology to "Sensibilité émotionnelle",
                Icons.Rounded.Favorite to "Besoin d'attachement",
                Icons.Rounded.EmojiNature to "Gestion de l'excitation",
                Icons.Rounded.Analytics to "Réactivité à l'environnement"
            )
        ),
        OnboardingSlide(
            kicker = "Ce que vous obtenez",
            titre = "Un bilan personnalisé complet",
            description = "À la fin du questionnaire, vous recevez un bilan émotionnel détaillé avec des conseils concrets, un plan d'action et un PDF à partager avec votre vétérinaire.",
            illustrationType = IllustrationType.BILAN_COMPLET,
            features = listOf<Pair<ImageVector, String>>(
                Icons.Rounded.CheckCircle to "Bilan émotionnel",
                Icons.Rounded.PictureAsPdf to "Export PDF 4 pages",
                Icons.Rounded.History to "Historique des bilans"
            )
        )
    )
}

// ═══════════════════════════════════════════════════════════
// CONSULTATION PERSONNALISÉE (FR uniquement)
// ═══════════════════════════════════════════════════════════

fun showConsultation(): Boolean = !isEnglish()

const val CONSULTATION_BOOKING_URL = "https://tidycal.com/laurenaharoy/30-minute-meeting"

fun strConsultationTitre() = "Besoin d'aide pour interpréter ce bilan ?"

fun strConsultationSousTitre() = "Consultation personnalisée du bilan émotionnel de votre chien"

fun strConsultationDescription() = "Vous avez reçu le bilan émotionnel de votre animal et vous souhaitez mieux comprendre ses résultats ?\n\nJe vous propose une consultation personnalisée de 30 minutes en visio pour vous aider à mettre les scores en perspective avec le quotidien de votre chien.\n\nPensez à m'envoyer votre bilan PDF par email avant notre rendez-vous, via le bouton Partager de l'application, à l'adresse laurenaharoy@gmail.com."

fun strConsultationDisclaimer() = "Cette consultation ne remplace pas une consultation vétérinaire et ne constitue pas une thérapie comportementale complète.\n\nEn cas de changement brutal de comportement, douleur, malpropreté soudaine, agressivité inhabituelle ou symptôme physique, consultez d'abord un vétérinaire."

fun strConsultationPrix() = "35 € / 30 minutes"

fun strConsultationBouton() = "Réserver ma consultation"