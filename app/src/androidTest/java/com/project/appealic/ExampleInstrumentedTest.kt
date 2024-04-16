package com.project.appealic

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.appealic.data.dao.PlayListDao
import com.project.appealic.data.database.AppDatabase
import com.project.appealic.data.model.PlayListEntity
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @RunWith(androidx.test.runner.AndroidJUnit4::class)
    class SimpleEntityReadWriteTest {
        private lateinit var playListDao: PlayListDao
        private lateinit var db: AppDatabase

        @Before
        fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()
            playListDao = db.platListDao()
        }

        @After
        @Throws(IOException::class)
        fun closeDb() {
            db.close()
        }

        @Test
        @Throws(Exception::class)
          fun writeUserAndReadInList() {
            val data = PlayListEntity("","1","MyPlayList", R.drawable.song1)
            playListDao.insert(data)
            val retrievedData = playListDao.getUserPlayLists("1")[0]
            assertEquals(data, retrievedData)
        }
    }
}