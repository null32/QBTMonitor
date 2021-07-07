package space.skycolor.qbtmonitor.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connections")
data class ConnectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "address") val address: String,
    @NonNull @ColumnInfo(name = "username") val username: String,
    @NonNull @ColumnInfo(name = "password") val password: String
)