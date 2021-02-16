package com.example.breakingbad.character

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val characterDao: CharacterDao) {

    val allCharacters: Flow<List<BreakingBadCharacter>> = characterDao.getSortedCharacters()

    fun getCharacter(id: Int): Flow<BreakingBadCharacter> {
        return characterDao.getCharacter(id)
    }

    fun getCharacter(name: String): Flow<BreakingBadCharacter> {
        return characterDao.getCharacter(name)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(breakingBadCharacter: BreakingBadCharacter) {
        characterDao.insert(breakingBadCharacter)
    }
}