package com.example.myapplication.domain.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.domain.model.Stratagem
import com.example.myapplication.persistence.StratagemFileStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StratagemRepository(private val context: Context) {
    private val requestQueue = Volley.newRequestQueue(context)
    private val fileStore = StratagemFileStore(context)

    private val apiUrl = "https://api-hellhub-collective.koyeb.app/api/stratagems?limit=90"

    fun fetch(onResponse: (List<Stratagem>) -> Unit, onError: (String) -> Unit) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, apiUrl, null,
            { response ->
                val stratagems = mutableListOf<Stratagem>()
                val data = response.getJSONArray("data")

                for (i in 0 until data.length()) {
                    val stratagemJson = data.getJSONObject(i)

                    val stratagem = Stratagem(
                        id = stratagemJson.getInt("id"),
                        codename = stratagemJson.getString("codename"),
                        name = stratagemJson.getString("name"),
                        keys = (0 until stratagemJson.getJSONArray("keys").length()).map { stratagemJson.getJSONArray("keys").getString(it) },
                        uses = stratagemJson.getString("uses"),
                        cooldown = stratagemJson.optInt("cooldown", 0),
                        activation = stratagemJson.optInt("activation", 0),
                        imageUrl = stratagemJson.getString("imageUrl"),
                        groupId = stratagemJson.getInt("groupId"),
                        createdAt = stratagemJson.getString("createdAt"),
                        updatedAt = stratagemJson.getString("updatedAt")
                    )

                    stratagems.add(stratagem)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    fileStore.saveStratagems(stratagems)
                    withContext(Dispatchers.Main) {
                        onResponse(stratagems)
                    }
                }
            },
            { error ->
                onError(error.toString())
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun getLocalStratagems(): List<Stratagem> {
        return fileStore.loadStratagems()
    }

    fun getLocalStratagemById(id: Int?): Stratagem? {
        if (id == null) {
            return null
        }
        val stratagem = fileStore.loadStratagems().firstOrNull { it.id == id }
        return stratagem
    }

    fun addStratagem(stratagem: Stratagem) {
        CoroutineScope(Dispatchers.IO).launch {
            val stratagems = fileStore.loadStratagems().toMutableList()

            val newId = stratagems.maxByOrNull { it.id ?: 0 }?.id?.plus(1) ?: 1
            val stratagemWithId = stratagem.copy(id = newId)

            stratagems.add(stratagemWithId)
            fileStore.saveStratagems(stratagems)
        }
    }

    fun updateStratagem(updatedStratagem: Stratagem) {
        CoroutineScope(Dispatchers.IO).launch {
            val stratagems = fileStore.loadStratagems().toMutableList()

            val index = stratagems.indexOfFirst { it.id == updatedStratagem.id }
            if (index != -1) {
                stratagems[index] = updatedStratagem
                fileStore.saveStratagems(stratagems)
            }
        }
    }

    fun deleteStratagem(id: Int?) {
        if (id == null) return

        CoroutineScope(Dispatchers.IO).launch {
            val stratagems = fileStore.loadStratagems().toMutableList()
            val filteredStratagems = stratagems.filter { it.id != id }
            fileStore.saveStratagems(filteredStratagems)
        }
    }
}

