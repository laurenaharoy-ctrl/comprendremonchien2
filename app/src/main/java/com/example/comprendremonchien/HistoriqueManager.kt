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
    val aDejaMordu: Boolean
)

object HistoriqueManager {

    private val HISTORIQUE_KEY = stringPreferencesKey("historique_bilans_json")

    // ── Sauvegarder un bilan ─────────────────────────────────────────────────
    suspend fun sauvegarderBilan(context: Context, nomChien: String, analyse: ResultatAnalyse) {
        val bilan = BilanSauvegarde(
            id = UUID.randomUUID().toString(),
            date = SimpleDateFormat("dd MMMM yyyy 'à' HH'h'mm", Locale.FRENCH).format(Date()),
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
            val array = JSONArray(existingJson)
            array.put(0, bilanToJson(bilan)) // Ajouter en tête de liste
            prefs[HISTORIQUE_KEY] = array.toString()
        }
    }

    // ── Lire tous les bilans ──────────────────────────────────────────────────
    fun getBilans(context: Context): Flow<List<BilanSauvegarde>> {
        return context.historiqueDataStore.data.map { prefs ->
            val json = prefs[HISTORIQUE_KEY] ?: "[]"
            parseBilans(json)
        }
    }

    // ── Supprimer un bilan par ID ─────────────────────────────────────────────
    suspend fun supprimerBilan(context: Context, id: String) {
        context.historiqueDataStore.edit { prefs ->
            val existingJson = prefs[HISTORIQUE_KEY] ?: "[]"
            val array = JSONArray(existingJson)
            val newArray = JSONArray()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                if (obj.getString("id") != id) {
                    newArray.put(obj)
                }
            }
            prefs[HISTORIQUE_KEY] = newArray.toString()
        }
    }

    // ── Supprimer tous les bilans ─────────────────────────────────────────────
    suspend fun supprimerTout(context: Context) {
        context.historiqueDataStore.edit { prefs ->
            prefs[HISTORIQUE_KEY] = "[]"
        }
    }

    // ── Helpers JSON ──────────────────────────────────────────────────────────
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
                    aDejaMordu = obj.getBoolean("aDejaMordu")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// ── Extensions utiles ─────────────────────────────────────────────────────────
fun BilanSauvegarde.prioriteActionEnum(): PrioriteAction =
    try { PrioriteAction.valueOf(prioriteAction) } catch (e: Exception) { PrioriteAction.FAIBLE }

fun BilanSauvegarde.niveauSituationEnum(): NiveauSituation =
    try { NiveauSituation.valueOf(niveauSituation) } catch (e: Exception) { NiveauSituation.STABLE }