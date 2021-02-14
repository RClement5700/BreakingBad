package com.example.breakingbad.character

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class CharacterViewModel(private val characterRepository: CharacterRepository): ViewModel() {

    val allCharacters: LiveData<List<BreakingBadCharacter>> = characterRepository.allCharacters.asLiveData()

    fun getCharacter(id: Int): LiveData<BreakingBadCharacter> {
        return characterRepository.getCharacter(id).asLiveData()
    }

    fun insert(breakingBadCharacter: BreakingBadCharacter) {
        viewModelScope.launch {
            characterRepository.insert(breakingBadCharacter)
        }
    }

    class CharacterViewModelFactory(private val repository: CharacterRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
                @Suppress("UNCHECKED_CASTS")
                return CharacterViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown View Model Class")
        }
    }
}