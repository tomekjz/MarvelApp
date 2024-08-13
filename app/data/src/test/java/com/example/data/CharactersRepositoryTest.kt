package com.example.capitalonetest.data

import com.example.data.Character
import com.example.data.CharacterMapper.toCharacterList
import com.example.data.CharactersRepository
import com.example.data.MarvelAPI
import com.google.gson.JsonObject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharactersRepositoryTest {

    private lateinit var repository: CharactersRepository
    private val marvelAPI: MarvelAPI = mockk()
    private val testJsonObject: JsonObject = mockk()

    @Before
    fun setUp() {
        repository = CharactersRepository(marvelAPI)
    }

    @Test
    fun `getCharacters should return a list of characters when API call is successful`() = runTest {
        // Arrange
        coEvery { marvelAPI.getCharacters() } returns testJsonObject
        coEvery { testJsonObject.toCharacterList() } returns listOf(
            Character("Spider-Man", "Friendly neighborhood Spider-Man", "https://path/to/spiderman.jpg"),
            Character("Iron Man", "Genius billionaire playboy philanthropist", "https://path/to/ironman.jpg")
        )

        // Act
        val result = repository.getCharacters()

        // Assert
        assertEquals(2, result?.size)
        assertEquals("Spider-Man", result?.get(0)?.name)
        assertEquals("Iron Man", result?.get(1)?.name)

        coVerify { marvelAPI.getCharacters() }
        verify { testJsonObject.toCharacterList() }
    }

    @Test
    fun `getCharacters should return null when API call fails`() = runTest {
        // Arrange
        coEvery { marvelAPI.getCharacters() } returns null

        // Act
        val result = repository.getCharacters()

        // Assert
        assertEquals(null, result)

        coVerify { marvelAPI.getCharacters() }
    }
}