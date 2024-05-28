package com.example.encryptionmemo.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MemoData::class], version = 2)
abstract class MemoDatabase: RoomDatabase() {
    abstract fun getDB(): MemoDao


    companion object {
        private val DB_NAME = "my_memo_db_v01"
        private var instance: MemoDatabase? = null

        fun getInstance(context: Context): MemoDatabase {
            return instance?: synchronized(this){
                instance?: buildDatabase((context))
            }
        }

        private fun buildDatabase(context: Context): MemoDatabase{
            return Room.databaseBuilder(context.applicationContext,
                MemoDatabase::class.java,
                DB_NAME
                ).addMigrations(migration_1_2).build()
        }
    }
}
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Memodata Add COLUMN isChecked INTEGER NOT NULL DEFAULT 0"
        )
    }
}