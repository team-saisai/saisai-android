package com.choius323.saisai.data.course.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "course_prefs")

class CourseDataStore(private val context: Context) {
    companion object {
        private val LAST_SHOW_DATE_COURSE_CAUTION =
            stringPreferencesKey("last_show_date_course_caution")
    }

    val lastShowDate: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LAST_SHOW_DATE_COURSE_CAUTION]
    }

    suspend fun saveDate(lastShowDate: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SHOW_DATE_COURSE_CAUTION] = lastShowDate
            println(lastShowDate)
        }
    }
}