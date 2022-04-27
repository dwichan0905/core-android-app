package id.dwichan.coreandroidapp.ui.config

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UiViewModel(private val preferences: UiPreferences): ViewModel() {

    fun getTheme(): LiveData<Int> = preferences.getTheme().asLiveData()

    fun setTheme(context: Context, theme: Int) {
        viewModelScope.launch {
            preferences.setTheme(context, theme)
        }
    }
}