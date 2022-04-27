package id.dwichan.coreandroidapp.ui.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UiViewModelFactory(private val preferences: UiPreferences): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UiViewModel::class.java)) {
            return UiViewModel(preferences) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}