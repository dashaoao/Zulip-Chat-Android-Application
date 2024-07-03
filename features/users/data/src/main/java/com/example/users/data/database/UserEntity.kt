package com.example.users.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.users.domain.Presence
import com.example.users.domain.User

@Entity(tableName = "user")
@TypeConverters(PresenceConverters::class)
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "presence") val presence: Presence
)

fun UserEntity.toDomain() = User(
    id = id.toString(),
    username = name,
    icon = avatarUrl,
    mail = email,
    status = presence,
)
