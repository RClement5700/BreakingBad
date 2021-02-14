package com.example.breakingbad.character

import android.content.Context
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray

@Database(entities = [BreakingBadCharacter::class], version = 1, exportSchema = false)
abstract class CharacterDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: CharacterDatabase? = null
        private const val URL: String            = "https://breakingbadapi.com/api/characters"

        fun getDatabaseInstance(context: Context, scope: CoroutineScope): CharacterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    CharacterDatabase::class.java, "character_database")
                    .addCallback(CharacterDatabaseCallback(scope, URL, context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class CharacterDatabaseCallback(private val scope: CoroutineScope, private val URL: String,
                                        private val context: Context): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val characterDao = database.characterDao()
                    characterDao.deleteAll()
                    val queue = Volley.newRequestQueue(context)
                    val stringRequest = StringRequest(Request.Method.GET, URL, { response ->
                            val characters = JSONArray(response)
                            var index = 0
                            while(index < characters.length()) {
                                val currentCharacter = characters.getJSONObject(index)
                                val id               = currentCharacter.get("char_id") as Int
                                val name             = currentCharacter.get("name") as String
                                val nickname         = currentCharacter.get("nickname") as String
                                val birthday         = currentCharacter.get("birthday") as String
                                val occupation       = (currentCharacter.get("occupation") as JSONArray)
                                    .toString()
                                val img              = currentCharacter.get("img") as String
                                val portrayed        = currentCharacter.get("portrayed") as String
                                val status           = currentCharacter.get("status") as String
                                val appearances      = (currentCharacter.get("appearance") as JSONArray)
                                    .toString()
                                val character        = BreakingBadCharacter(id, name, nickname,
                                    birthday, occupation, img, portrayed, status, appearances)
                                scope.launch {
                                    characterDao.insert(character)
                                }
                                index++
                            }
                        },
                        { error ->
                            Toast.makeText(context, "Unable to retrieve movies: ${error.message}",
                                Toast.LENGTH_SHORT).show()
                        })
                    queue.add(stringRequest)
                }
            }
        }
    }
}