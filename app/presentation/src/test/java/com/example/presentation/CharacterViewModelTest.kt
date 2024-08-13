package com.example.capitalonetest.presentation

import com.example.presentation.CharacterViewModel
import com.example.presentation.Event
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterViewModelTest {

    private var getCharactersUseCase: com.example.domain.GetCharactersUseCase = mockk()

    private lateinit var viewModel: CharacterViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // Initialize ViewModel
        viewModel = CharacterViewModel(getCharactersUseCase)
    }

    @Test
    fun `init should set uiEvent to Success when getCharactersUseCase returns a list`() = runTest {
        // Arrange
        val characters = listOf(Character(name = "name", image = "image", description = "description"), Character(name = "name2", image = "image2", description = "description2"))
        coEvery { getCharactersUseCase.getCharacters() } returns characters

        // Act
        viewModel = CharacterViewModel(getCharactersUseCase) // Re-initialize to trigger init block

        // Assert
        assertTrue(viewModel.uiEvent.value is Event.Success)
        val successEvent = viewModel.uiEvent.value as Event.Success
        assertEquals(characters, successEvent.data)
    }

    @Test
    fun `WHEN a null value is returned for characters THEN show error messages`() = runTest {
        // Arrange
        coEvery { getCharactersUseCase.getCharacters() } returns null

        // Act
        viewModel = CharacterViewModel(getCharactersUseCase) // Re-initialize to trigger init block

        // Assert
        assertTrue(viewModel.uiEvent.value is Event.Error)
        val successEvent = viewModel.uiEvent.value as Event.Error
        assertEquals("getCharactersUseCase returned null", successEvent.message)
    }

    @Test
    fun `WHEN getCharactersUseCase throws an exception THEN show relevant error message`() = runTest {
        // Arrange
        val errorMessage = "Test exception"
        coEvery { getCharactersUseCase.getCharacters() } throws RuntimeException(errorMessage)

        // Act
        viewModel = CharacterViewModel(getCharactersUseCase) // Re-initialize to trigger init block

        // Assert
        assertTrue(viewModel.uiEvent.value is Event.Error)
        val errorEvent = viewModel.uiEvent.value as Event.Error
        assertEquals(errorMessage, errorEvent.message)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}