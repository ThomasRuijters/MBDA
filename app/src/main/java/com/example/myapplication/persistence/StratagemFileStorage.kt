package com.example.myapplication.persistence

import android.content.Context
import com.example.myapplication.domain.model.Stratagem
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class StratagemFileStore(private val context: Context) {
    private val fileName = "stratagems.json"

    fun saveStratagems(stratagems: List<Stratagem>) {
        try {
            val jsonArray = JSONArray()

            for (stratagem in stratagems) {
                val jsonObject = JSONObject().apply {
                    put("id", stratagem.id)
                    put("codename", stratagem.codename)
                    put("name", stratagem.name)
                    put("keys", JSONArray(stratagem.keys))
                    put("uses", stratagem.uses)
                    put("cooldown", stratagem.cooldown)
                    put("activation", stratagem.activation)
                    put("imageUrl", stratagem.imageUrl)
                    put("groupId", stratagem.groupId)
                    put("createdAt", stratagem.createdAt)
                    put("updatedAt", stratagem.updatedAt)
                }
                jsonArray.put(jsonObject)
            }

            val file = File(context.filesDir, fileName)
            val fos = FileOutputStream(file)
            fos.write(jsonArray.toString().toByteArray())
            fos.close();
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadStratagems(): List<Stratagem> {
        val stratagems = mutableListOf<Stratagem>()

        try {
            val file = File(context.filesDir, fileName)

            if(file.exists()) {
                val fis = FileInputStream(file)
                val json = fis.readBytes().toString(Charsets.UTF_8)
                fis.close()

                val jsonArray = JSONArray(json)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    val stratagem = Stratagem(
                        id = jsonObject.getInt("id"),
                        codename = jsonObject.getString("codename"),
                        name = jsonObject.getString("name"),
                        keys = List(jsonObject.getJSONArray("keys").length()) { jsonObject.getJSONArray("keys").getString(it) },
                        uses = jsonObject.getString("uses"),
                        cooldown = jsonObject.getInt("cooldown"),
                        activation = jsonObject.getInt("activation"),
                        imageUrl = jsonObject.getString("imageUrl"),
                        groupId = jsonObject.getInt("groupId"),
                        createdAt = jsonObject.getString("createdAt"),
                        updatedAt = jsonObject.getString("updatedAt")
                    )

                    stratagems.add(stratagem)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stratagems
    }
}