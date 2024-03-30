package com.best.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.best.entity.FilmAndCollectionF
import com.best.entity.SearchText

@Database(
    entities = [FilmAndCollectionF::class, SearchText::class],
    version = 1
)
abstract class DatBase:  RoomDatabase() {
    abstract fun dao(): DAOInterface
}