package id.dwichan.coreandroidapp.ui.config

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import id.dwichan.coreandroidapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UiPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val themePreference = intPreferencesKey("theme")

    fun getTheme(): Flow<Int> {
        return dataStore.data.map { preference ->
            preference[themePreference] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }
    
    @Suppress("KotlinConstantConditions")
    suspend fun setTheme(context: Context, theme: Int) {
        val themeApplied = if (
                theme != AppCompatDelegate.MODE_NIGHT_YES ||
                theme != AppCompatDelegate.MODE_NIGHT_NO ||
                theme != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ) {
                Log.e("UiPreferences", context.getString(R.string.message_theme_not_found))
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                theme
            }

        dataStore.edit { preferences ->
            preferences[themePreference] = themeApplied
        }
    }
    
    companion object {
        const val PREFERENCE_NAME = "ui_preference"

        @Volatile
        private var INSTANCE: UiPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UiPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UiPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}