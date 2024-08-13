package com.example.domain

import com.example.data.Character
import com.example.data.CharactersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCharactersUseCase(private val charactersRepository: CharactersRepository) {

    suspend fun getCharacters(): List<Character>? = withContext(Dispatchers.IO) {
        charactersRepository.getCharacters()
    }

}
