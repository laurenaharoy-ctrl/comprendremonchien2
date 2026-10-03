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

enum class AppLang { FR, EN, DE }

fun appLang(): AppLang = when (LocaleList.getDefault()[0].language) {
    "en" -> AppLang.EN
    "de" -> AppLang.DE
    else -> AppLang.FR
}

fun isEnglish(): Boolean = appLang() == AppLang.EN

fun isGerman(): Boolean = appLang() == AppLang.DE

/** Renvoie le texte dans la langue du téléphone : français (par défaut), anglais ou allemand. */
fun tr(fr: String, en: String, de: String): String = when (appLang()) {
    AppLang.EN -> en
    AppLang.DE -> de
    AppLang.FR -> fr
}

fun <T> trList(fr: T, en: T, de: T): T = when (appLang()) {
    AppLang.EN -> en
    AppLang.DE -> de
    AppLang.FR -> fr
}

// ═══════════════════════════════════════════════════════════
// TEXTES GÉNÉRAUX
// ═══════════════════════════════════════════════════════════

fun strAppName() = tr("Comprendre mon chien", "Understanding My Dog", "Meinen Hund verstehen")

// Boutons
fun strBtnDemarrerBilan() = tr("Démarrer le bilan", "Start the assessment", "Einschätzung starten")
fun strBtnReprendre() = tr("Reprendre le questionnaire", "Resume the questionnaire", "Fragebogen fortsetzen")
fun strBtnDictionnaire() = tr("Dictionnaire", "Dictionary", "Lexikon")
fun strBtnAlimentation() = tr("Alimentation", "Feeding", "Ernährung")
fun strBtnCommencer() = tr("Commencer", "Get started", "Los geht’s")
fun strBtnContinuer() = tr("Continuer", "Continue", "Weiter")
fun strBtnSuivant() = tr("Suivant", "Next", "Weiter")
fun strBtnRetour() = tr("Retour", "Back", "Zurück")
fun strBtnPasser() = tr("Passer", "Skip", "Überspringen")
fun strBtnPartager() = tr("Partager", "Share", "Teilen")
fun strBtnExportPdf() = tr("PDF", "PDF", "PDF")
fun strBtnCopierResume() = tr("Copier le résumé", "Copy summary", "Zusammenfassung kopieren")
fun strBtnRecommencer() = tr("Recommencer depuis le début", "Start over", "Von vorn beginnen")
fun strBtnVoirLivres() = tr("Voir mes livres", "See my books", "Meine Bücher ansehen")
fun strBtnRevoirIntroduction() = tr("Revoir l'introduction", "View the introduction again", "Einführung erneut ansehen")
fun strBtnPolitiqueConfidentialite() = tr("Politique de confidentialité", "Privacy policy", "Datenschutzerklärung")
fun strBtnRetourRubriques() = tr("Retour aux rubriques", "Back to categories", "Zurück zu den Rubriken")
fun strBtnRetourCategorie() = tr("Retour à la catégorie", "Back to category", "Zurück zur Kategorie")
fun strBtnSupprimer() = tr("Supprimer", "Delete", "Löschen")
fun strBtnSupprimerTout() = tr("Supprimer tout l'historique", "Delete all history", "Gesamten Verlauf löschen")
fun strBtnSupprimerBilan() = tr("Supprimer ce bilan", "Delete this assessment", "Diese Einschätzung löschen")
fun strBtnAnnuler() = tr("Annuler", "Cancel", "Abbrechen")
fun strBtnEnvoyerSignalement() = tr("Envoyer le signalement", "Send report", "Meldung senden")

// Titres écrans
fun strScreenQuestionnaire() = tr("Questionnaire", "Questionnaire", "Fragebogen")
fun strScreenAnalyse() = tr("Analyse", "Analysis", "Analyse")
fun strScreenResultat() = tr("Résultat", "Results", "Ergebnis")
fun strScreenDictionnaire() = tr("Dictionnaire comportemental", "Behaviour dictionary", "Verhaltenslexikon")
fun strScreenFicheComportementale() = tr("Fiche comportementale", "Behaviour fact sheet", "Infoblatt zum Verhalten")
fun strScreenAlimentation() = tr("Alimentation", "Feeding", "Ernährung")
fun strScreenSignalement() = tr("Signalement", "Report", "Meldung")
fun strScreenHistorique() = tr("Historique des bilans", "Assessment history", "Verlauf der Einschätzungen")
fun strScreenDetailBilan() = tr("Détail du bilan", "Assessment detail", "Details der Einschätzung")
fun strScreenParametres() = tr("Paramètres", "Settings", "Einstellungen")

// ═══════════════════════════════════════════════════════════
// INTRODUCTION
// ═══════════════════════════════════════════════════════════

fun strIntroKicker() = tr("Avant de commencer", "Before you begin", "Bevor Sie beginnen")
fun strIntroDuree() = tr("Ce questionnaire vous prendra environ 5 minutes.", "This questionnaire will take about 5 minutes.", "Dieser Fragebogen dauert etwa 5 Minuten.")
fun strIntroExplorerKicker() = tr("Ce que vous allez explorer", "What you will explore", "Was Sie erkunden werden")
fun strIntroExplorer1() = tr("Sa sensibilité émotionnelle", "Emotional sensitivity", "Seine emotionale Sensibilität")
fun strIntroExplorer2() = tr("Son besoin d'attachement", "Need for attachment", "Sein Bindungsbedürfnis")
fun strIntroExplorer3() = tr("Sa gestion de l'excitation", "Excitement management", "Sein Umgang mit Erregung")
fun strIntroExplorer4() = tr("Sa réactivité à l'environnement", "Reactivity to the environment", "Seine Reaktivität auf die Umgebung")

// ═══════════════════════════════════════════════════════════
// QUESTIONNAIRE
// ═══════════════════════════════════════════════════════════

fun strQuestionReponseLabel() = tr("Votre réponse", "Your answer", "Ihre Antwort")
fun strQuestionReponsePlaceholder() = tr("Ex. Rocky", "E.g. Rocky", "z. B. Rocky")
fun strQuestionHintTexte() = tr("Saisissez une réponse pour continuer", "Enter an answer to continue", "Geben Sie eine Antwort ein, um fortzufahren")
fun strQuestionHintChoix() = tr("Choisissez une réponse pour continuer", "Choose an answer to continue", "Wählen Sie eine Antwort, um fortzufahren")

// ═══════════════════════════════════════════════════════════
// CHARGEMENT
// ═══════════════════════════════════════════════════════════

fun strChargementMessages() = trList(
    listOf("Analyse en cours…", "Lecture du profil de votre chien…", "Préparation de votre bilan…"),
    listOf("Analysis in progress…", "Reading your dog's profile…", "Preparing your assessment…"),
    listOf("Analyse läuft…", "Das Profil Ihres Hundes wird gelesen…", "Ihre Einschätzung wird vorbereitet…")
)

// ═══════════════════════════════════════════════════════════
// RÉSULTAT
// ═══════════════════════════════════════════════════════════

fun strResultatKicker() = tr("Votre bilan", "Your assessment", "Ihre Einschätzung")
fun strResultatTitreBilan(nom: String) = tr("Bilan pour $nom", "Assessment for $nom", "Einschätzung für $nom")
fun strResultatLecturePrincipale() = tr("Lecture principale", "Main reading", "Wichtigste Deutung")
fun strResultatPriorite(p: String) = tr("Priorité : $p", "Priority: $p", "Priorität: $p")
fun strResultatRessent(nom: String) = tr("Ce que ressent probablement $nom", "What $nom is probably feeling", "Was $nom wahrscheinlich fühlt")
fun strResultatCoupOeil() = tr("En un coup d'œil", "At a glance", "Auf einen Blick")
fun strResultatFacteurs() = tr("Facteurs repérés", "Identified factors", "Erkannte Faktoren")
fun strResultatNiveauSituation() = tr("Niveau de situation", "Situation level", "Einschätzung der Lage")
fun strResultatInquieter() = tr("Faut-il s'inquiéter ?", "Should you be concerned?", "Muss man sich Sorgen machen?")
fun strResultatSePasse() = tr("Ce qui se passe probablement", "What is probably happening", "Was wahrscheinlich geschieht")
fun strResultatLevierPrincipal() = tr("Première piste concrète", "First concrete step", "Ein erster konkreter Ansatz")
fun strResultatPointAppui() = tr("Le point d'appui principal", "The main lever", "Der wichtigste Hebel")
fun strResultatPourquoi() = tr("Pourquoi est-il comme ça ?", "Why is your dog like this?", "Warum ist er so?")
fun strResultatComprendreAgir() = tr("Comprendre pour mieux agir", "Understand to act more effectively", "Verstehen, um besser zu handeln")
fun strResultatChangement() = tr("C'est souvent ici que le changement commence à prendre forme.", "This is often where change begins to take shape.", "Oft beginnt genau hier die Veränderung.")
fun strResultat3Jours() = tr("Les 3 prochains jours", "The next 3 days", "Die nächsten 3 Tage")
fun strResultatAFaire() = tr("À faire", "To do", "Zu tun")
fun strResultatAEviter() = tr("À éviter", "To avoid", "Zu vermeiden")
fun strResultatAObserver() = tr("À observer", "To observe", "Zu beobachten")
fun strResultatConseilsComplementaires() = tr("Conseils complémentaires", "Additional advice", "Weitere Tipps")
fun strResultatQuandAide() = tr("Quand demander de l'aide", "When to seek help", "Wann Sie Hilfe suchen sollten")
fun strResultatMorsurePro() = tr("Une morsure a été signalée — un accompagnement professionnel est recommandé.", "A bite has been reported — professional support is recommended.", "Es wurde ein Biss gemeldet – eine professionelle Begleitung wird empfohlen.")
fun strResultatImportant() = tr("Important", "Important", "Wichtig")
fun strResultatDisclaimer() = tr("Ce bilan reste indicatif. Il ne remplace ni un vétérinaire ni un professionnel du comportement.", "This assessment is indicative. It does not replace a vet or a behaviour professional.", "Diese Einschätzung ist nur ein Anhaltspunkt. Sie ersetzt weder einen Tierarzt noch eine Fachperson für Verhalten.")
fun strResultatAllerPlusLoin(nom: String) = tr("Pour aller plus loin avec $nom", "Going further with $nom", "Weiterführendes für $nom")
fun strResultatFichesComportementales() = tr("Fiches comportementales", "Behaviour fact sheets", "Infoblätter zum Verhalten")
fun strResultatReperes() = tr("Repères alimentation", "Feeding guidelines", "Ernährung auf einen Blick")
fun strResultatARetenir() = tr("À retenir", "Key takeaway", "Das Wichtigste")
fun strResultatLeLivre() = tr("Le livre", "The book", "Das Buch")
fun strResultatAllerPlusLoinLivre() = tr("Si vous souhaitez aller plus loin", "If you want to go further", "Wenn Sie tiefer einsteigen möchten")
fun strResultatCopie() = tr("Copié", "Copied", "Kopiert")
fun strResultatProfilRace() = tr("Profil de race", "Breed profile", "Rasseprofil")
fun strResultatPredispositions() = tr("Prédispositions fréquentes dans cette famille", "Common predispositions in this family", "Häufige Veranlagungen in dieser Gruppe")

// ═══════════════════════════════════════════════════════════
// MORSURE
// ═══════════════════════════════════════════════════════════

fun strMorsureTitre() = tr("ATTENTION — MORSURE SIGNALÉE", "WARNING — BITE REPORTED", "ACHTUNG – BISS GEMELDET")
fun strMorsuTexte(nom: String) = tr("Il y a déjà eu morsure chez $nom. Cette situation ne doit pas être banalisée.", "There has already been a bite involving $nom. This situation should not be taken lightly.", "$nom hat bereits gebissen. Diese Situation sollte nicht verharmlost werden.")
fun strMorsuConseil() = tr("Un accompagnement par un professionnel du comportement est fortement recommandé.", "Support from a behaviour professional is strongly recommended.", "Eine Begleitung durch eine Fachperson für Verhalten wird dringend empfohlen.")

// ═══════════════════════════════════════════════════════════
// DICTIONNAIRE COMPORTEMENTAL
// ═══════════════════════════════════════════════════════════

fun strDicoTitre() = tr("Dictionnaire comportemental", "Behaviour dictionary", "Verhaltenslexikon")
fun strDicoSousTitre() = tr("Repères pour mieux lire le langage du chien", "Pointers for reading your dog's body language", "Anhaltspunkte, um die Sprache des Hundes besser zu lesen")
fun strDicoRecherchePlaceholder() = tr("Rechercher une fiche…", "Search a fact sheet…", "Ein Infoblatt suchen…")
fun strDicoAucunResultat(q: String) = tr("Aucune fiche ne correspond à \"$q\".", "No fact sheet matches \"$q\".", "Kein Infoblatt passt zu „$q“.")
fun strDicoImportant() = tr("Important", "Important", "Wichtig")
fun strDicoDisclaimer() = tr("Ces fiches donnent des repères de lecture. Elles ne remplacent pas l'avis d'un professionnel.", "These fact sheets offer guidance for interpretation. They do not replace professional advice.", "Diese Infoblätter bieten Orientierungshilfen. Sie ersetzen nicht den Rat einer Fachperson.")
fun strDicoRappel() = tr("Un comportement isolé ne suffit pas toujours à conclure. Le contexte et l'ensemble du langage corporel comptent autant.", "An isolated behaviour is not always enough to draw conclusions. Context and overall body language matter just as much.", "Ein einzelnes Verhalten reicht nicht immer aus, um Schlüsse zu ziehen. Der Kontext und die gesamte Körpersprache zählen genauso.")
fun strDicoFicheKicker() = tr("Fiche comportementale", "Behaviour fact sheet", "Infoblatt zum Verhalten")
fun strDicoExplication() = tr("Explication", "Explanation", "Erklärung")
fun strDicoQueFaire() = tr("Que faire", "What to do", "Was tun")
fun strDicoAEviter() = tr("À éviter", "What to avoid", "Zu vermeiden")
fun strDicoRappelKicker() = tr("Rappel", "Reminder", "Zur Erinnerung")
fun strDicoFicheIntrouvable() = tr("Fiche introuvable.", "Fact sheet not found.", "Infoblatt nicht gefunden.")

// ═══════════════════════════════════════════════════════════
// ALIMENTATION
// ═══════════════════════════════════════════════════════════

fun strAlimTitre() = tr("Alimentation du chien", "Feeding your dog", "Ernährung des Hundes")
fun strAlimSousTitre() = tr("Repères pratiques pour nourrir votre chien sereinement.", "Practical guidance for feeding your dog with confidence.", "Praktische Anhaltspunkte, um Ihren Hund entspannt zu füttern.")
fun strAlimARetenirKicker() = tr("À retenir d'abord", "Keep in mind first", "Zuerst das Wichtigste")
fun strAlimRetenir1() = tr("Tout changement alimentaire doit être progressif.", "Any dietary change must be gradual.", "Jede Futterumstellung muss schrittweise erfolgen.")
fun strAlimRetenir2() = tr("Même un aliment banal pour l'humain peut être inadapté pour le chien.", "Even a food that seems harmless to humans may be unsuitable for dogs.", "Selbst ein für Menschen alltägliches Lebensmittel kann für Hunde ungeeignet sein.")
fun strAlimRetenir3() = tr("En cas d'ingestion suspecte ou de symptômes, la prudence passe avant l'attente.", "If your dog may have swallowed something harmful or shows symptoms, act with caution rather than wait.", "Bei verdächtiger Aufnahme oder Symptomen gilt: lieber vorsichtig als abwarten.")
fun strAlimImportant() = tr("Important", "Important", "Wichtig")
fun strAlimDisclaimer() = tr("Ce guide donne des repères généraux. Il ne remplace pas un vétérinaire.", "This guide provides general guidance. It does not replace a vet.", "Dieser Leitfaden gibt allgemeine Anhaltspunkte. Er ersetzt keinen Tierarzt.")
fun strAlimRappel() = tr("En cas de symptômes ou d'ingestion douteuse, privilégiez un avis vétérinaire.", "If symptoms appear or you suspect your dog has eaten something harmful, seek veterinary advice.", "Bei Symptomen oder fraglicher Aufnahme holen Sie lieber tierärztlichen Rat ein.")
fun strAlimCatDangereuxDesc() = tr("Les aliments à éviter pour ne pas faire d'erreur.", "Foods to avoid to stay on the safe side.", "Lebensmittel, die Sie meiden sollten, um keinen Fehler zu machen.")
fun strAlimCatAutorisesDesc() = tr("Les repères de base pour donner sans improviser.", "The basics for feeding safely without guessing.", "Die Grundregeln, um nichts dem Zufall zu überlassen.")
fun strAlimCatIngestionDesc() = tr("Les bons réflexes si le chien a avalé quelque chose.", "What to do if your dog has swallowed something.", "Was zu tun ist, wenn Ihr Hund etwas verschluckt hat.")
fun strAlimCatDigestionDesc() = tr("Herbe, vomissements, selles et petits signaux digestifs.", "Grass, vomiting, stools and small digestive signals.", "Gras, Erbrechen, Kot und kleine Verdauungssignale.")

// ═══════════════════════════════════════════════════════════
// PARAMÈTRES
// ═══════════════════════════════════════════════════════════

fun strParamsKicker() = tr("Paramètres", "Settings", "Einstellungen")
fun strParamsAppTitre() = tr("Comprendre mon chien", "Understanding My Dog", "Meinen Hund verstehen")
fun strParamsVersion(v: String) = tr("Version $v", "Version $v", "Version $v")
fun strParamsTutorielKicker() = tr("Tutoriel", "Tutorial", "Einführung")
fun strParamsTutorielTexte() = tr("Revoir la présentation de l'application depuis le début.", "Replay the app introduction from the start.", "Die Vorstellung der App noch einmal von vorn ansehen.")
fun strParamsConfidentialiteKicker() = tr("Confidentialité", "Privacy", "Datenschutz")
fun strParamsConfidentialiteTexte() = tr("Cette application ne collecte aucune donnée personnelle. Les bilans sont stockés uniquement sur votre appareil. Les notifications sont locales.", "This app does not collect any personal data. Assessments are stored on your device only. Notifications are local.", "Diese App erhebt keine personenbezogenen Daten. Die Einschätzungen werden nur auf Ihrem Gerät gespeichert. Die Benachrichtigungen sind lokal.")
fun strParamsAProposKicker() = tr("À propos", "About", "Über die App")
fun strParamsAProposTexte() = tr("Développée avec soin pour aider les maîtres à mieux comprendre leur chien.", "Designed with care to help owners better understand their dog.", "Mit Sorgfalt entwickelt, um Haltern zu helfen, ihren Hund besser zu verstehen.")

// ═══════════════════════════════════════════════════════════
// HISTORIQUE
// ═══════════════════════════════════════════════════════════

fun strHistoriqueAucun() = tr("Aucun bilan sauvegardé pour l'instant.", "No saved assessments yet.", "Noch keine gespeicherte Einschätzung.")
fun strHistoriqueNbBilans(n: Int) = tr(
    if (n > 1) "$n bilans sauvegardés" else "$n bilan sauvegardé",
    if (n > 1) "$n saved assessments" else "$n saved assessment",
    if (n > 1) "$n gespeicherte Einschätzungen" else "$n gespeicherte Einschätzung"
)
fun strHistoriqueVideTexte() = tr("Les bilans réalisés apparaîtront ici automatiquement après chaque questionnaire complété.", "Completed assessments will appear here automatically after each questionnaire.", "Ihre Einschätzungen erscheinen hier automatisch nach jedem abgeschlossenen Fragebogen.")
fun strHistoriqueSupprimerTitre() = tr("Supprimer ce bilan ?", "Delete this assessment?", "Diese Einschätzung löschen?")
fun strHistoriqueSupprimerTexte(nom: String, date: String) = tr("Le bilan de $nom du $date sera supprimé définitivement.", "The assessment for $nom on $date will be permanently deleted.", "Die Einschätzung von $nom vom $date wird endgültig gelöscht.")
fun strHistoriqueSupprimerToutTitre() = tr("Supprimer tout l'historique ?", "Delete all history?", "Gesamten Verlauf löschen?")
fun strHistoriqueSupprimerToutTexte() = tr("Cette action est irréversible. Tous les bilans sauvegardés seront supprimés définitivement.", "This action cannot be undone. All saved assessments will be permanently deleted.", "Diese Aktion kann nicht rückgängig gemacht werden. Alle gespeicherten Einschätzungen werden endgültig gelöscht.")
fun strHistoriqueDetailKicker() = tr("Bilan sauvegardé", "Saved assessment", "Gespeicherte Einschätzung")
fun strHistoriqueSynthese() = tr("Synthèse", "Summary", "Zusammenfassung")
fun strHistoriqueCarte() = tr("Carte du profil", "Profile card", "Profilkarte")
fun strHistoriqueLecture() = tr("Lecture principale", "Main reading", "Wichtigste Deutung")
fun strHistoriquePiste() = tr("Première piste concrète", "First concrete step", "Ein erster konkreter Ansatz")
fun strHistoriqueRappelKicker() = tr("Rappel", "Reminder", "Zur Erinnerung")
fun strHistoriqueRappelTexte() = tr("Ce bilan est un enregistrement indicatif. La situation de votre chien peut avoir évolué depuis.", "This assessment is an indicative record. Your dog's situation may have changed since then.", "Diese Einschätzung ist eine unverbindliche Momentaufnahme. Die Situation Ihres Hundes kann sich seitdem verändert haben.")

// ═══════════════════════════════════════════════════════════
// FEEDBACK
// ═══════════════════════════════════════════════════════════

fun strFeedbackKicker() = tr("Signalement", "Report", "Meldung")
fun strFeedbackTitre() = tr("Quelque chose ne va pas ?", "Something wrong?", "Stimmt etwas nicht?")
fun strFeedbackSousTitre() = tr("Votre retour est précieux. Décrivez le problème ou la suggestion et nous en tiendrons compte.", "Your feedback is valuable. Describe the issue or suggestion and we will take it into account.", "Ihre Rückmeldung ist wertvoll. Beschreiben Sie das Problem oder Ihren Vorschlag, und wir werden ihn berücksichtigen.")
fun strFeedbackTypeKicker() = tr("Type de signalement", "Report type", "Art der Meldung")
fun strFeedbackEcranKicker() = tr("Écran concerné", "Screen concerned", "Betroffener Bildschirm")
fun strFeedbackDescriptionKicker() = tr("Description", "Description", "Beschreibung")
fun strFeedbackDescriptionTexte() = tr("Décrivez le problème ou votre suggestion avec le plus de détails possible.", "Describe the issue or your suggestion in as much detail as possible.", "Beschreiben Sie das Problem oder Ihren Vorschlag so genau wie möglich.")
fun strFeedbackPlaceholder() = tr("Ex. : Sur l'écran résultat, le bouton PDF ne fonctionne pas…", "E.g.: On the results screen, the PDF button doesn't work…", "z. B.: Auf dem Ergebnisbildschirm funktioniert die Schaltfläche PDF nicht…")
fun strFeedbackDetailsHint() = tr("Ajoutez quelques détails pour nous aider à comprendre.", "Add a few details to help us understand.", "Fügen Sie ein paar Details hinzu, damit wir es besser verstehen.")
fun strFeedbackHintCategorie() = tr("Choisissez une catégorie", "Choose a category", "Wählen Sie eine Kategorie")
fun strFeedbackHintMessage() = tr("rédigez un message", "write a message", "schreiben Sie eine Nachricht")
fun strFeedbackHintEt() = tr(" et ", " and ", " und ")
fun strFeedbackHintPour() = tr(" pour envoyer.", " to send.", " zum Senden.")
fun strFeedbackConfidentialiteKicker() = tr("Confidentialité", "Privacy", "Datenschutz")
fun strFeedbackConfidentialiteTexte() = tr("Votre signalement est envoyé par email directement depuis votre application. Aucune donnée personnelle n'est collectée automatiquement.", "Your report is sent by email directly from your app. No personal data is collected automatically.", "Ihre Meldung wird direkt aus Ihrer App per E-Mail gesendet. Es werden keine personenbezogenen Daten automatisch erhoben.")
fun strFeedbackMerciTitre() = tr("Merci pour votre retour !", "Thank you for your feedback!", "Danke für Ihre Rückmeldung!")
fun strFeedbackMerciTexte() = tr("Votre message a bien été transmis. Il contribuera à améliorer l'application.", "Your message has been sent. It will help improve the app.", "Ihre Nachricht wurde gesendet. Sie hilft, die App zu verbessern.")
fun strFeedbackAucuneAppliEmail() = tr("Aucune application email trouvée sur cet appareil.", "No email app found on this device.", "Auf diesem Gerät wurde keine E-Mail-App gefunden.")

// ═══════════════════════════════════════════════════════════
// ONBOARDING
// ═══════════════════════════════════════════════════════════

fun strOnboardingPasser() = tr("Passer", "Skip", "Überspringen")

// ═══════════════════════════════════════════════════════════
// NOTIFICATIONS
// ═══════════════════════════════════════════════════════════

fun strNotifChannelNom() = tr("Rappels bilan", "Assessment reminders", "Erinnerungen an die Einschätzung")
fun strNotifChannelDesc() = tr("Rappels pour refaire le bilan de votre chien", "Reminders to redo your dog's assessment", "Erinnerungen, die Einschätzung Ihres Hundes zu wiederholen")
fun strNotifTitre() = tr("Et si vous refaisiez le bilan ?", "Time for a new assessment?", "Zeit für eine neue Einschätzung?")
fun strNotifTexte(nom: String) = tr("Beaucoup de choses peuvent avoir évolué pour $nom 🐾", "A lot may have changed for $nom 🐾", "Bei $nom kann sich viel verändert haben 🐾")

// ═══════════════════════════════════════════════════════════
// TEXTE PARTAGÉ
// ═══════════════════════════════════════════════════════════

fun strPartageTitre(nom: String) = tr("Bilan émotionnel pour $nom", "Emotional assessment for $nom", "Emotionale Einschätzung für $nom")
fun strPartageHypothese() = tr("Hypothèse :", "Hypothesis:", "Hypothese:")
fun strPartagePriorite() = tr("Priorité :", "Priority:", "Priorität:")
fun strPartageScores() = tr("Scores :", "Scores:", "Werte:")
fun strPartageSensibilite(v: String) = tr("Sensibilité : $v", "Sensitivity: $v", "Sensibilität: $v")
fun strPartageAttachement(v: String) = tr("Attachement : $v", "Attachment: $v", "Bindung: $v")
fun strPartageImpulsivite(v: String) = tr("Impulsivité : $v", "Impulsivity: $v", "Impulsivität: $v")
fun strPartageReactivite(v: String) = tr("Réactivité : $v", "Reactivity: $v", "Reaktivität: $v")
fun strPartageIndicatif() = tr("⚠️ Bilan indicatif", "⚠️ Indicative assessment", "⚠️ Unverbindliche Einschätzung")

// ═══════════════════════════════════════════════════════════
// PDF
// ═══════════════════════════════════════════════════════════

fun strPdfBilanEmotionnel() = tr("Bilan émotionnel", "Emotional assessment", "Emotionale Einschätzung")
fun strPdfFooter() = tr("Comprendre mon chien  •  Bilan émotionnel indicatif", "Understanding My Dog  •  Indicative emotional assessment", "Meinen Hund verstehen  •  Unverbindliche emotionale Einschätzung")
fun strPdfPage(n: Int, total: Int = 4) = tr("Page $n / $total", "Page $n / $total", "Seite $n / $total")
fun strPdf4Axes() = tr("Les 4 dimensions", "The 4 dimensions", "Die 4 Dimensionen")
fun strPdfHypothese() = tr("Hypothèse de lecture", "Reading hypothesis", "Deutungshypothese")
fun strPdfSePasse() = tr("Ce qui se passe probablement", "What is probably happening", "Was wahrscheinlich geschieht")
fun strPdfFacteurs() = tr("Facteurs repérés", "Identified factors", "Erkannte Faktoren")
fun strPdfAggravants() = tr("Ce qui peut aggraver", "What may make things worse", "Was die Lage verschlimmern kann")
fun strPdfProtecteurs() = tr("Ce qui protège déjà", "What is already helping", "Was bereits schützt")
fun strPdfPlanAction(nom: String) = tr("Plan d'action pour $nom", "Action plan for $nom", "Aktionsplan für $nom")
fun strPdfLevier() = tr("Premier levier utile", "First useful lever", "Erster hilfreicher Hebel")
fun strPdfProchainsJours() = tr("Les prochains jours", "The coming days", "Die nächsten Tage")
fun strPdfConseils() = tr("Conseils complémentaires", "Additional advice", "Weitere Tipps")
fun strPdfARetenir() = tr("À retenir", "Key takeaway", "Das Wichtigste")
fun strPdfConclusion() = tr("Conclusion", "Conclusion", "Fazit")
fun strPdfConclusionTexte(nom: String) = tr("L'objectif n'est pas d'étiqueter $nom, mais d'aider à mieux lire ce qui se passe et à avancer de manière plus adaptée, plus concrète et plus rassurante.", "The goal is not to label $nom, but to help read the situation more clearly and move forward in a more adapted, concrete and reassuring way.", "Ziel ist es nicht, $nom ein Etikett aufzudrücken, sondern besser zu verstehen, was geschieht, und auf eine passendere, konkretere und beruhigendere Weise voranzukommen.")
fun strPdfDisclaimer() = tr("Ce bilan est indicatif. Il ne remplace pas l'avis d'un vétérinaire ni d'un professionnel du comportement animal. Il peut servir de base de discussion lors d'une consultation.", "This assessment is indicative. It does not replace the advice of a vet or an animal behaviour professional. It can be used as a basis for discussion during a consultation.", "Diese Einschätzung ist unverbindlich. Sie ersetzt nicht den Rat eines Tierarztes oder einer Fachperson für Tierverhalten. Sie kann als Gesprächsgrundlage bei einer Beratung dienen.")
fun strPdfGenereAuto() = tr("Document généré automatiquement", "Automatically generated document", "Automatisch erstelltes Dokument")
fun strPdfRetrouvez() = tr("Retrouvez l'application pour suivre l'évolution de votre chien.", "Find the app to track your dog's progress.", "Nutzen Sie die App, um die Entwicklung Ihres Hundes zu verfolgen.")
fun strPdfAcceder() = tr("Accéder à l'application — comprendremonchien.fr", "Access the app — comprendremonchien.fr", "Zur App – comprendremonchien.fr")
fun strPdfMorsuTexte() = tr("Une morsure a été signalée lors de ce bilan. Un accompagnement professionnel est recommandé pour évaluer la situation et sécuriser le quotidien.", "A bite was reported during this assessment. Professional support is recommended to evaluate the situation and make daily life safer.", "Bei dieser Einschätzung wurde ein Biss gemeldet. Eine professionelle Begleitung wird empfohlen, um die Situation zu beurteilen und den Alltag sicherer zu machen.")
fun strPdfEnUnCoup() = tr("En un coup d'œil", "At a glance", "Auf einen Blick")
fun strPdfAxePrincipal() = tr("Axe principal", "Main axis", "Hauptachse")
fun strPdfSituation() = tr("Situation", "Situation", "Situation")
fun strPdfBesoin() = tr("Besoin principal", "Main need", "Hauptbedürfnis")
fun strPdfAide() = tr("Aide à envisager", "Support to consider", "Mögliche Unterstützung")
fun strPdfProfil(nom: String) = tr("Profil de $nom", "$nom's profile", "Profil von $nom")
fun strPdfAideComportementalisteRec() = tr("Comportementaliste recommandé", "Behaviourist recommended", "Verhaltensberatung empfohlen")
fun strPdfAideProRapide() = tr("Professionnel rapidement", "See a professional soon", "Zeitnah Fachperson hinzuziehen")
fun strPdfAideComportementaliste() = tr("Comportementaliste", "Behaviourist", "Verhaltensberater")
fun strPdfAideEducateur() = tr("Éducateur canin", "Dog trainer", "Hundetrainer")
fun strPdfAideEducateurBesoin() = tr("Éducateur canin si besoin", "Dog trainer if needed", "Hundetrainer bei Bedarf")
fun strPdfRecapProfil(nom: String, profil: String) = tr("$nom présente surtout un profil $profil.", "$nom primarily shows a $profil profile.", "$nom zeigt vor allem ein Profil „$profil“.")
fun strPdfRecapSituation(s: String) = tr("Situation : $s.", "Situation: $s.", "Situation: $s.")
fun strPdfRecapAxe(a: String) = tr("Axe principal : $a.", "Main axis: $a.", "Hauptachse: $a.")

// ═══════════════════════════════════════════════════════════
// PRIORITÉS / NIVEAUX
// ═══════════════════════════════════════════════════════════

fun strPrioriteAction(p: PrioriteAction) = when (p) {
    PrioriteAction.FAIBLE -> tr("Faible", "Low", "Gering")
    PrioriteAction.MODEREE -> tr("Modérée", "Moderate", "Mäßig")
    PrioriteAction.ELEVEE -> tr("Élevée", "High", "Hoch")
    PrioriteAction.URGENTE -> tr("Urgente", "Urgent", "Dringend")
}

fun strNiveauAxe(n: NiveauAxe) = when (n) {
    NiveauAxe.PEU_MARQUE -> tr("Peu marqué", "Mild", "Wenig ausgeprägt")
    NiveauAxe.A_SURVEILLER -> tr("À surveiller", "Worth watching", "Zu beobachten")
    NiveauAxe.MARQUE -> tr("Marqué", "Marked", "Ausgeprägt")
    NiveauAxe.TRES_MARQUE -> tr("Très marqué", "Very marked", "Stark ausgeprägt")
}

fun strNiveauSituation(n: NiveauSituation) = when (n) {
    NiveauSituation.STABLE -> tr("Stable", "Stable", "Stabil")
    NiveauSituation.A_TRAVAILLER -> tr("À travailler", "To work on", "Handlungsbedarf")
    NiveauSituation.SENSIBLE -> tr("Sensible", "Sensitive", "Heikel")
}

fun strLibelleAxe(axe: Axe) = when (axe) {
    Axe.PEUR -> tr("Sensibilité", "Sensitivity", "Sensibilität")
    Axe.ATTACHEMENT -> tr("Attachement", "Attachment", "Bindung")
    Axe.IMPULSIVITE -> tr("Impulsivité", "Impulsivity", "Impulsivität")
    Axe.REACTIVITE -> tr("Réactivité", "Reactivity", "Reaktivität")
}

// ═══════════════════════════════════════════════════════════
// SECTIONS QUESTIONNAIRE
// ═══════════════════════════════════════════════════════════

fun strTitreSection(questionId: String) = when (questionId) {
    "nom_chien", "age", "sterilise", "senior_desorientation", "senior_vocalise_nocturne" -> tr("Informations générales", "General information", "Allgemeine Angaben")
    "race_categorie" -> tr("Votre chien", "Your dog", "Ihr Hund")
    "adaptation_changements", "comportement_exterieur", "reaction_peur" ->
        tr("Sensibilité et peur", "Sensitivity and fear", "Sensibilität und Angst")
    "vecu_absence", "suit_partout", "autre_personne_apaise",
    "proprete_maison", "proprete_type", "marquage_habitude_post_sterilisation" ->
        tr("Attachement et séparation", "Attachment and separation", "Bindung und Trennung")
    "regulation_excitation", "vole_objets", "poursuite_mouvement" ->
        tr("Excitation et impulsivité", "Excitement and impulsivity", "Erregung und Impulsivität")
    "reaction_inconnus", "reaction_chiens", "a_deja_mordu", "cible_agression", "defense_ressources" ->
        tr("Réactivité", "Reactivity", "Reaktivität")
    "a_un_probleme", "lieu_residence" -> tr("Pour aller plus loin", "Going further", "Weiterführendes")
    else -> tr("Contexte actuel", "Current context", "Aktuelle Situation")
}

// ═══════════════════════════════════════════════════════════
// ÉCRANS FEEDBACK
// ═══════════════════════════════════════════════════════════

fun strEcransFeedback() = trList(
    listOf("Accueil", "Introduction", "Questionnaire", "Résultat", "Dictionnaire comportemental", "Alimentation", "Historique", "Général / Autre"),
    listOf("Home", "Introduction", "Questionnaire", "Results", "Behaviour dictionary", "Feeding", "History", "General / Other"),
    listOf("Startseite", "Einführung", "Fragebogen", "Ergebnis", "Verhaltenslexikon", "Ernährung", "Verlauf", "Allgemein / Sonstiges")
)

// ═══════════════════════════════════════════════════════════
// CATÉGORIES SIGNALEMENT
// ═══════════════════════════════════════════════════════════

fun strCategoriesSignalement(): List<CategorieSignalement> {
    return trList(
        listOf(
            CategorieSignalement("bug", "Bug / Problème technique", "L'appli plante, un bouton ne fonctionne pas, un écran est bloqué…", Icons.Rounded.BugReport),
            CategorieSignalement("contenu", "Contenu incorrect", "Une information semble erronée, un texte est incompréhensible…", Icons.Rounded.HelpOutline),
            CategorieSignalement("suggestion", "Suggestion", "Une idée pour améliorer l'appli ou ajouter une fonctionnalité…", Icons.Rounded.Lightbulb),
            CategorieSignalement("autre", "Autre", "Tout ce qui ne rentre pas dans les catégories ci-dessus.", Icons.Rounded.MoreHoriz)
        ),
        listOf(
            CategorieSignalement("bug", "Bug / Technical issue", "The app crashes, a button doesn't work, a screen is frozen…", Icons.Rounded.BugReport),
            CategorieSignalement("contenu", "Incorrect content", "Some information seems wrong, a text is unclear…", Icons.Rounded.HelpOutline),
            CategorieSignalement("suggestion", "Suggestion", "An idea to improve the app or add a feature…", Icons.Rounded.Lightbulb),
            CategorieSignalement("autre", "Other", "Anything that doesn't fit the other categories.", Icons.Rounded.MoreHoriz)
        ),
        listOf(
            CategorieSignalement("bug", "Fehler / Technisches Problem", "Die App stürzt ab, eine Schaltfläche funktioniert nicht, ein Bildschirm hängt…", Icons.Rounded.BugReport),
            CategorieSignalement("contenu", "Fehlerhafter Inhalt", "Eine Information scheint falsch, ein Text ist unverständlich…", Icons.Rounded.HelpOutline),
            CategorieSignalement("suggestion", "Vorschlag", "Eine Idee, um die App zu verbessern oder eine Funktion hinzuzufügen…", Icons.Rounded.Lightbulb),
            CategorieSignalement("autre", "Sonstiges", "Alles, was in keine der obigen Kategorien passt.", Icons.Rounded.MoreHoriz)
        )
    )
}

// ═══════════════════════════════════════════════════════════
// FORMAT DATE HISTORIQUE
// ═══════════════════════════════════════════════════════════

fun strDateFormatHistorique() = tr("dd MMMM yyyy 'à' HH'h'mm", "MMMM dd, yyyy 'at' HH:mm", "dd. MMMM yyyy 'um' HH:mm")

// ═══════════════════════════════════════════════════════════
// CONTENT DESCRIPTIONS
// ═══════════════════════════════════════════════════════════

fun strContentDescRetour() = tr("Retour", "Back", "Zurück")
fun strContentDescHistorique() = tr("Historique des bilans", "Assessment history", "Verlauf der Einschätzungen")
fun strContentDescParametres() = tr("Paramètres", "Settings", "Einstellungen")
fun strContentDescSignalement() = tr("Signaler un problème", "Report an issue", "Ein Problem melden")
fun strContentDescMorsure() = tr("Morsure signalée", "Bite reported", "Biss gemeldet")
fun strContentDescSupprimer() = tr("Supprimer", "Delete", "Löschen")
fun strContentDescLogo() = tr("Logo Comprendre mon chien", "Understanding My Dog logo", "Logo Meinen Hund verstehen")

// ═══════════════════════════════════════════════════════════
// CHOOSERS PARTAGE
// ═══════════════════════════════════════════════════════════

fun strPartageChooser() = tr("Partager", "Share", "Teilen")
fun strPartagePdfChooser() = tr("Partager PDF", "Share PDF", "PDF teilen")
fun strSignalementChooser() = tr("Envoyer le signalement", "Send report", "Meldung senden")

// ═══════════════════════════════════════════════════════════
// RÉSUMÉ ÉMOTIONNEL (Models.kt)
// ═══════════════════════════════════════════════════════════

fun strResumeEmotionnel(axe: Axe) = when (axe) {
    Axe.PEUR -> tr("Sensible et facilement impacté par son environnement", "Sensitive and easily affected by the environment", "Sensibel und leicht von seiner Umgebung beeinflusst")
    Axe.ATTACHEMENT -> tr("Très attaché, difficile à détacher", "Very attached, finds it hard to be apart", "Sehr anhänglich, tut sich schwer mit Trennungen")
    Axe.IMPULSIVITE -> tr("Monte vite en excitation", "Gets excited quickly", "Gerät schnell in Erregung")
    Axe.REACTIVITE -> tr("Réagit rapidement aux stimuli", "Reacts quickly to stimuli", "Reagiert schnell auf Reize")
}

fun strIntentionChien(axe: Axe) = when (axe) {
    Axe.PEUR -> tr("Il essaie surtout de gérer ce qui lui fait peur.", "He is mainly trying to manage what frightens him.", "Er versucht vor allem, mit dem umzugehen, was ihm Angst macht.")
    Axe.ATTACHEMENT -> tr("Il cherche à rester en sécurité avec vous.", "He is looking to stay safe with you.", "Er sucht bei Ihnen Sicherheit.")
    Axe.IMPULSIVITE -> tr("Il tente de gérer son excitation.", "He is trying to manage his excitement.", "Er versucht, mit seiner Erregung umzugehen.")
    Axe.REACTIVITE -> tr("Il essaie de répondre à un environnement trop intense.", "He is trying to cope with an environment that is too intense.", "Er versucht, mit einer zu intensiven Umgebung zurechtzukommen.")
}

fun strBesoinPrincipal(axe: Axe) = when (axe) {
    Axe.PEUR -> tr("Besoin principal : se sentir en sécurité.", "Main need: to feel safe.", "Hauptbedürfnis: sich sicher fühlen.")
    Axe.ATTACHEMENT -> tr("Besoin principal : gagner en autonomie.", "Main need: to build independence.", "Hauptbedürfnis: selbstständiger werden.")
    Axe.IMPULSIVITE -> tr("Besoin principal : apprendre à redescendre.", "Main need: to learn to calm back down.", "Hauptbedürfnis: lernen, wieder herunterzukommen.")
    Axe.REACTIVITE -> tr("Besoin principal : retrouver du calme.", "Main need: to find calm again.", "Hauptbedürfnis: wieder zur Ruhe finden.")
}

fun strPhraseFin(nom: String) = tr("Chaque chien est unique. Ce bilan donne des repères pour $nom, mais l'observation du quotidien reste essentielle.", "Every dog is unique. This assessment offers pointers for $nom, but daily observation remains essential.", "Jeder Hund ist einzigartig. Diese Einschätzung gibt Anhaltspunkte für $nom, doch die Beobachtung im Alltag bleibt unerlässlich.")

fun strTexteVigilance(niveau: NiveauVigilance, nom: String) = when (niveau) {
    NiveauVigilance.FAIBLE -> tr("À ce stade, rien ne ressort comme particulièrement préoccupant pour $nom.", "At this stage, nothing stands out as particularly concerning for $nom.", "Im Moment deutet nichts auf etwas besonders Besorgniserregendes bei $nom hin.")
    NiveauVigilance.MODEREE -> tr("Quelques éléments méritent une attention particulière pour $nom.", "A few elements deserve particular attention for $nom.", "Einige Punkte verdienen bei $nom besondere Aufmerksamkeit.")
    NiveauVigilance.ELEVEE -> tr("Certaines réponses invitent à ne pas laisser la situation s'installer seule pour $nom.", "Some responses suggest it would be best not to let the situation develop on its own for $nom.", "Einige Antworten legen nahe, die Situation bei $nom nicht einfach sich selbst zu überlassen.")
}

// ═══════════════════════════════════════════════════════════
// ONBOARDING SLIDES
// ═══════════════════════════════════════════════════════════

fun strOnboardingSlides(): List<OnboardingSlide> {
    return trList(
        listOf(
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
        ),
        listOf(
            OnboardingSlide(
                kicker = "Welcome",
                titre = "Understanding My Dog",
                description = "This app helps you decode your dog's behaviour and get concrete advice tailored to their unique profile.",
                illustrationType = IllustrationType.CHIEN_ASSIS
            ),
            OnboardingSlide(
                kicker = "How it works",
                titre = "One questionnaire, four dimensions",
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
        ),
        listOf(
            OnboardingSlide(
                kicker = "Willkommen",
                titre = "Meinen Hund verstehen",
                description = "Diese App hilft Ihnen, das Verhalten Ihres Hundes zu entschlüsseln und konkrete Ansätze zu erhalten, die zu seinem einzigartigen Profil passen.",
                illustrationType = IllustrationType.CHIEN_ASSIS
            ),
            OnboardingSlide(
                kicker = "So funktioniert’s",
                titre = "Ein Fragebogen, vier Dimensionen",
                description = "In wenigen Minuten erkunden Sie die vier Achsen, die das Verhalten Ihres Hundes im Alltag prägen.",
                illustrationType = IllustrationType.QUATRE_AXES,
                features = listOf<Pair<ImageVector, String>>(
                    Icons.Rounded.Psychology to "Emotionale Sensibilität",
                    Icons.Rounded.Favorite to "Bindungsbedürfnis",
                    Icons.Rounded.EmojiNature to "Umgang mit Erregung",
                    Icons.Rounded.Analytics to "Reaktivität auf die Umgebung"
                )
            ),
            OnboardingSlide(
                kicker = "Was Sie erhalten",
                titre = "Eine vollständige, persönliche Einschätzung",
                description = "Am Ende des Fragebogens erhalten Sie eine ausführliche emotionale Einschätzung mit konkreten Tipps, einem Aktionsplan und einem PDF, das Sie mit Ihrem Tierarzt teilen können.",
                illustrationType = IllustrationType.BILAN_COMPLET,
                features = listOf<Pair<ImageVector, String>>(
                    Icons.Rounded.CheckCircle to "Emotionale Einschätzung",
                    Icons.Rounded.PictureAsPdf to "4-seitiger PDF-Export",
                    Icons.Rounded.History to "Verlauf der Einschätzungen"
                )
            )
        )
    )
}

// CONSULTATION PERSONNALISÉE (FR uniquement)
// ═══════════════════════════════════════════════════════════

fun showConsultation(): Boolean = appLang() == AppLang.FR

const val CONSULTATION_BOOKING_URL = "https://tidycal.com/laurenaharoy/30-minute-meeting"

const val CONSULTATION_BOOKING_URL_1H = "https://tidycal.com/laurenaharoy/consultation-comportementale-1-heure"

const val CGV_URL = "https://laurenaharoy-ctrl.github.io/comprendremonchien2/cgv.html"

const val WEBSITE_URL = "https://comportementaliste91.fr"

const val TELEPHONE_NATIONAL = "06 20 65 78 88"

const val TELEPHONE_INTERNATIONAL = "+33 6 20 65 78 88"

const val TELEPHONE_URI = "tel:+33620657888"

fun strConsultationFormule30() = "Consultation conseil — 30 min — 35 €"

fun strConsultationFormule60() = "Consultation comportementale — 1 heure — 50 €"

fun strConsultationCGV() = "Consulter les Conditions Générales de Vente"

fun strConsultationSite() = "Visiter mon site internet"

fun strConsultationTitre() = "Besoin d'aide pour interpréter ce bilan ?"

fun strConsultationSousTitre() = "Consultation personnalisée du bilan émotionnel de votre chien"

fun strConsultationDescription() = "Vous avez reçu le bilan émotionnel de votre animal et vous souhaitez mieux comprendre ses résultats ?\n\nJe vous propose deux formats de consultation personnalisée, selon vos besoins : un échange conseil de 30 minutes pour une première orientation, ou une consultation comportementale d'1 heure pour construire un plan d'accompagnement plus approfondi.\n\nPour prendre rendez-vous, appelez-moi directement : nous ferons connaissance et choisirons ensemble la formule la plus adaptée."

fun strConsultationDisclaimer() = "Cette consultation ne remplace pas une consultation vétérinaire et ne constitue pas un accompagnement comportemental complet à elle seule.\n\nEn cas de changement brutal de comportement, douleur, malpropreté soudaine, agressivité inhabituelle ou symptôme physique, consultez d'abord un vétérinaire."

fun strConsultationPrix() = "35 € / 30 min  •  50 € / 1 h"

fun strConsultationBouton(lieu: Int? = null) = "Appeler le " + if (lieu == 2) TELEPHONE_INTERNATIONAL else TELEPHONE_NATIONAL

fun strConsultationAppel() = "Pour prendre rendez-vous, appelez-moi"

fun strConsultationTelephonePdf() = "$TELEPHONE_NATIONAL  (depuis l'étranger : $TELEPHONE_INTERNATIONAL)"

fun strConsultationModalite(lieu: Int?) = when (lieu) {
    0 -> "Consultation en visio ou en présentiel (Essonne)"
    else -> "Consultation en visio"
}