package id.dwichan.coreandroidapp.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import id.dwichan.coreandroidapp.R
import id.dwichan.coreandroidapp.ui.config.UiPreferences
import id.dwichan.coreandroidapp.ui.config.UiViewModel
import id.dwichan.coreandroidapp.ui.config.UiViewModelFactory
import id.dwichan.coreandroidapp.util.showBelowCutout

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = UiPreferences.PREFERENCE_NAME
)
/**
 * Extends the class as Android Activity. This will avoid the memory leak caused by a binder class.
 *
 * Example usage of this class:
 * ```
 * class ProfileActivity: AndroidActivity<ActivityProfileBinding>() {
 *     override val bindingInflater: (LayoutInflater) -> ViewBinding =
 *         ActivityProfileBinding::inflate
 *
 *     override fun onSetup(){
 *          // do stuff with binding variable
 *          binding.textExample.text = "OK"
 *     }
 * }
 * ```
 * @param VB represent the ViewBinding
 * @see AppCompatActivity
 */
abstract class AndroidActivity<VB : ViewBinding>: AppCompatActivity() {

    private var _binding: ViewBinding? = null
    private lateinit var uiViewModel: UiViewModel
    /**
     * Define if the activity is at last stack. Set to TRUE if no more activity and empty fragment
     * stack, otherwise FALSE if there still an activity or fragment stack remaining.
     */
    abstract val isLastActivity: Boolean

    /**
     * Represent how the ViewBinding inflated.
     */
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB?
        get() = _binding as VB?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBelowCutout()
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding!!.root)
        Log.i("AndroidActivity", "ViewBinding ${binding!!.javaClass.name} are displayed as activity.")

        val pref = UiPreferences.getInstance(dataStore)
        uiViewModel = ViewModelProvider(this, UiViewModelFactory(pref))[
                UiViewModel::class.java
        ]
        uiViewModel.getTheme().observe(this) { mode ->
            AppCompatDelegate.setDefaultNightMode(mode)
            val modeString = when (mode) {
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    "MODE_NIGHT_YES"
                }
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    "MODE_NIGHT_NO"
                }
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                    "MODE_NIGHT_FOLLOW_SYSTEM"
                }
                else -> {
                    "MODE_UNKNOWN ($mode)"
                }
            }
            Log.i("AndroidActivity", "Theme successfully applied to $modeString")
        }
        onSetup(savedInstanceState)
        Log.i("AndroidActivity", "onSetup() triggered successfully")
    }

    /**
     * Will be invoked after event [onCreate] was invoked and view was inflated. You can use
     * [binding] variable to modify what view must do!
     */
    abstract fun onSetup(savedInstanceState: Bundle?)

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isLastActivity) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        val className = binding?.javaClass?.name
        _binding = null
        Log.i("AndroidActivity", "ViewBinding $className are nullified.")
        onTearDown()
        Log.i("AndroidActivity", "onTearDown() triggered successfully")
        super.onDestroy()
    }

    /**
     * If you need to nullify variables/components (e.g. adapter), write to this function. This will
     * called on [onDestroy] life cycle after nullifying a binder.
     */
    protected open fun onTearDown() {}

    /**
     * Sets the Night Mode to the app. Please note that the [theme] value
     * must be MODE_NIGHT_YES, MODE_NIGHT_NO, or MODE_NIGHT_FOLLOW_SYSTEM.
     * @param theme Theme value from AppCompatDelegate
     * @see AppCompatDelegate.NightMode
     */
    protected fun setUiTheme(@AppCompatDelegate.NightMode theme: Int) {
        uiViewModel.setTheme(baseContext, theme)
    }

    /**
     * Check whether the app is running on the tablet devices or not.
     */
    fun isTabletMode(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}