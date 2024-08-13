package com.example.domain

import com.example.data.Character
import com.example.data.CharactersRepository

class GetCharactersUseCase(private val charactersRepository: CharactersRepository) {

    suspend fun getCharacters(): List<Character>? {
        return charactersRepository.getCharacters()
    }

}