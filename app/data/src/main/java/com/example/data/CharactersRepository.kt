package com.example.data

import com.example.data.CharacterMapper.toCharacterList
import com.google.gson.JsonObject

class CharactersRepository(private val marvelAPI: MarvelAPI) {

    suspend fun getCharacters(): List<Character>? {
        val result = marvelAPI.getCharacters()
        return result?.toCharacterList()
    }
}

object CharacterMapper {

    fun JsonObject.toCharacterList(): List<Character> {
        val characters = this.getAsJsonObject("data").getAsJsonArray("results").asJsonArray.map {
            toCharacterModel(it.asJsonObject)
        }
        return characters
    }

    private fun toCharacterModel(data: JsonObject): Character {
        return Character(
            name = data.get("name").asString,
            description = data.get("description").asString,
            image = data.getAsJsonObject("thumbnail").get("path").asString.plus(".jpg").replace("http://", "https://"),
        )
    }
}