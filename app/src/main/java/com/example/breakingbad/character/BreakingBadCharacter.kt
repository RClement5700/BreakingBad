package com.example.breakingbad.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "character_table")
data class BreakingBadCharacter(
     @PrimaryKey @ColumnInfo(name = "id") val id: Int,
                 @ColumnInfo(name = "name") val name: String,
                 @ColumnInfo(name = "nickname") val nickname: String,
                 @ColumnInfo(name = "birthday") val birthday: String,
                 @ColumnInfo(name = "occupation") val occupation: String,
                 @ColumnInfo(name = "img") val img: String,
                 @ColumnInfo(name = "portayed") val portrayed: String,
                 @ColumnInfo(name = "status") val status: String,
                 @ColumnInfo(name = "appearances") val appearances: String,
                 ): Serializable