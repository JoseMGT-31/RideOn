package com.example.rideon.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import at.favre.lib.crypto.bcrypt.BCrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.rideon.model.UserEntity

@Database(entities = [UserEntity::class /*, otras*/], version = 2, exportSchema = false)
abstract class RideOnDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: RideOnDatabase? = null

        fun getDatabase(context: Context): RideOnDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, RideOnDatabase::class.java, "rideon.db")
                        .fallbackToDestructiveMigration() // o addMigrations(MIGRATION_1_2)
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                // Seed admin
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dao = getDatabase(context).userDao()
                                    val adminPassHash = BCrypt.withDefaults()
                                        .hashToString(12, "admin123".toCharArray())
                                    // Evitar duplicar por si acaso
                                    val existing = dao.getUserByEmail("admin@rideon.app")
                                    if (existing == null) {
                                        dao.insertUser(
                                            UserEntity(
                                                name = "Administrador",
                                                email = "admin@rideon.app",
                                                password = adminPassHash,
                                                role = "ADMIN"
                                            )
                                        )
                                    }
                                }
                            }
                        })
                        .build()
                INSTANCE = instance
                instance
            }
    }
}