package com.laurena.comprendremonchien

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.laurena.comprendremonchien.NiveauSituation
import com.laurena.comprendremonchien.PrioriteAction
import com.laurena.comprendremonchien.ResultatAnalyse
import com.laurena.comprendremonchien.nomChienAffiche
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private val Context.historiqueDataStore by preferencesDataStore(name = "historique_bilans")

data class BilanSauvegarde(
    val id: String,
    val date: String,
    val dateTimestamp: Long,
    val nomChien: String,
    val profilType: String,
    val prioriteAction: String,
    val niveauSituation: String,
    val peur: Int,
    val attachement: Int,
    val impulsivite: Int,
    val reactivite: Int,
    val hypothesePrincipale: String,
    val conseilPrincipal: String,
    val aDejaMordu: Boolean,
    // Bilan marqué comme « Bilan de référence » (un seul par animal) — absent des anciens bilans : false par défaut
    val estReference: Boolean = false
)

/** Clé qui permet de regrouper les bilans d'un même animal (insensible aux majuscules et aux espaces). */
fun BilanSauvegarde.cleAnimal(): String = nomChien.trim().lowercase(Locale.ROOT)

/**
 * Chaque animal a toujours un bilan de référence : celui choisi par l'utilisateur,
 * sinon automatiquement son bilan le plus ancien.
 */
fun List<BilanSauvegarde>.avecReferenceParDefaut(): List<BilanSauvegarde> {
    val idsReferenceAuto = groupBy { it.cleAnimal() }.values
        .filter { groupe -> groupe.none { it.estReference } }
        .mapNotNull { groupe -> groupe.minByOrNull { it.dateTimestamp }?.id }
        .toSet()
    return map { if (it.id in idsReferenceAuto) it.copy(estReference = true) else it }
}

object HistoriqueManager {

    private val HISTORIQUE_KEY = stringPreferencesKey("historique_bilans_json")

    suspend fun sauvegarderBilan(context: Context, nomChien: String, analyse: ResultatAnalyse) {
        val bilan = BilanSauvegarde(
            id = UUID.randomUUID().toString(),
            date = SimpleDateFormat(strDateFormatHistorique(), Locale.getDefault()).format(Date()),
            dateTimestamp = System.currentTimeMillis(),
            nomChien = nomChienAffiche(nomChien),
            profilType = analyse.profil.profilType,
            prioriteAction = analyse.prioriteAction.name,
            niveauSituation = analyse.niveauSituation.name,
            peur = analyse.peur,
            attachement = analyse.attachement,
            impulsivite = analyse.impulsivite,
            reactivite = analyse.reactivite,
            hypothesePrincipale = analyse.hypothesePrincipale,
            conseilPrincipal = analyse.conseilPrincipal,
            aDejaMordu = analyse.aDejaMordu
        )

        context.historiqueDataStore.edit { prefs ->
            val existingJson = prefs[HISTORIQUE_KEY] ?: "[]"
            val anciens = JSONArray(existingJson)
            // Le nouveau bilan est ajouté en tête, les anciens sont conservés à la suite
            // (JSONArray.put(0, …) remplaçait le premier bilan au lieu d'en ajouter un)
            val array = JSONArray().put(bilanToJson(bilan))
            for (i in 0 until anciens.length()) array.put(anciens.getJSONObject(i))
            prefs[HISTORIQUE_KEY] = array.toString()
        }
    }

    fun getBilans(context: Context): Flow<List<BilanSauvegarde>> {
        return context.historiqueDataStore.data.map { prefs ->
            val json = prefs[HISTORIQUE_KEY] ?: "[]"
            parseBilans(json).avecReferenceParDefaut()
        }
    }

    /**
     * Fait d'un bilan le « Bilan de référence » de son animal.
     * Un seul bilan de référence par animal : les autres bilans du même animal perdent la marque.
     */
    suspend fun definirReference(context: Context, id: String) {
        context.historiqueDataStore.edit { prefs ->
            val bilans = parseBilans(prefs[HISTORIQUE_KEY] ?: "[]")
            val cible = bilans.firstOrNull { it.id == id } ?: return@edit
            val cle = cible.cleAnimal()
            val miseAJour = bilans.map { bilan ->
                when {
                    bilan.id == id -> bilan.copy(estReference = true)
                    bilan.cleAnimal() == cle -> bilan.copy(estReference = false)
                    else -> bilan
                }
            }
            val array = JSONArray()
            miseAJour.forEach { array.put(bilanToJson(it)) }
            prefs[HISTORIQUE_KEY] = array.toString()
        }
    }

    /** Supprime tous les bilans d'un même animal (clé = nom normalisé, voir cleAnimal). */
    suspend fun supprimerBilansAnimal(context: Context, cle: String) {
        context.historiqueDataStore.edit { prefs ->
            val restants = parseBilans(prefs[HISTORIQUE_KEY] ?: "[]").filter { it.cleAnimal() != cle }
            val array = JSONArray()
            restants.forEach { array.put(bilanToJson(it)) }
            prefs[HISTORIQUE_KEY] = array.toString()
        }
    }

    suspend fun supprimerBilan(context: Context, id: String) {
        context.historiqueDataStore.edit { prefs ->
            val existingJson = prefs[HISTORIQUE_KEY] ?: "[]"
            val array = JSONArray(existingJson)
            val newArray = JSONArray()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                if (obj.getString("id") != id) newArray.put(obj)
            }
            prefs[HISTORIQUE_KEY] = newArray.toString()
        }
    }

    suspend fun supprimerTout(context: Context) {
        context.historiqueDataStore.edit { prefs -> prefs[HISTORIQUE_KEY] = "[]" }
    }

    private fun bilanToJson(bilan: BilanSauvegarde): JSONObject {
        return JSONObject().apply {
            put("id", bilan.id)
            put("date", bilan.date)
            put("dateTimestamp", bilan.dateTimestamp)
            put("nomChien", bilan.nomChien)
            put("profilType", bilan.profilType)
            put("prioriteAction", bilan.prioriteAction)
            put("niveauSituation", bilan.niveauSituation)
            put("peur", bilan.peur)
            put("attachement", bilan.attachement)
            put("impulsivite", bilan.impulsivite)
            put("reactivite", bilan.reactivite)
            put("hypothesePrincipale", bilan.hypothesePrincipale)
            put("conseilPrincipal", bilan.conseilPrincipal)
            put("aDejaMordu", bilan.aDejaMordu)
            put("estReference", bilan.estReference)
        }
    }

    private fun parseBilans(json: String): List<BilanSauvegarde> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                BilanSauvegarde(
                    id = obj.getString("id"),
                    date = obj.getString("date"),
                    dateTimestamp = obj.getLong("dateTimestamp"),
                    nomChien = obj.getString("nomChien"),
                    profilType = obj.getString("profilType"),
                    prioriteAction = obj.getString("prioriteAction"),
                    niveauSituation = obj.getString("niveauSituation"),
                    peur = obj.getInt("peur"),
                    attachement = obj.getInt("attachement"),
                    impulsivite = obj.getInt("impulsivite"),
                    reactivite = obj.getInt("reactivite"),
                    hypothesePrincipale = obj.getString("hypothesePrincipale"),
                    conseilPrincipal = obj.getString("conseilPrincipal"),
                    aDejaMordu = obj.getBoolean("aDejaMordu"),
                    estReference = obj.optBoolean("estReference", false)
                )
            }
        } catch (e: Exception) { emptyList() }
    }
}

fun BilanSauvegarde.prioriteActionEnum(): PrioriteAction =
    try { PrioriteAction.valueOf(prioriteAction) } catch (e: Exception) { PrioriteAction.FAIBLE }

fun BilanSauvegarde.niveauSituationEnum(): NiveauSituation =
    try { NiveauSituation.valueOf(niveauSituation) } catch (e: Exception) { NiveauSituation.STABLE }