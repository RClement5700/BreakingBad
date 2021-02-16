package com.example.breakingbad.character

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character_table ORDER BY name ASC")
    fun getSortedCharacters(): Flow<List<BreakingBadCharacter>>

    @Query("SELECT * FROM character_table WHERE id LIKE :id")
    fun getCharacter(id: Int): Flow<BreakingBadCharacter>

    @Query("SELECT * FROM character_table WHERE name LIKE :name")
    fun getCharacter(name: String): Flow<BreakingBadCharacter>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(breakingBadCharacter: BreakingBadCharacter)

    @Query("DELETE FROM character_table")
    suspend fun deleteAll()
}