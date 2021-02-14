package com.example.breakingbad

import android.app.Application
import com.example.breakingbad.character.CharacterDatabase
import com.example.breakingbad.character.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BreakingBadApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { CharacterDatabase.getDatabaseInstance(this, applicationScope) }
    val repository by lazy { CharacterRepository(database.characterDao()) }
}