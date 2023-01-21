package id.dwichan.coreandroidapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import id.dwichan.coreandroidapp.ui.config.UiPreferences
import id.dwichan.coreandroidapp.ui.config.UiViewModel
import id.dwichan.coreandroidapp.ui.config.UiViewModelFactory
import id.dwichan.coreandroidapp.util.showBelowCutout

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = UiPreferences.PREFERENCE_NAME
)

/**
 * Extends the class as Android Activity (Jetpack Compose). This will avoid the memory leak caused
 * by a binder class.
 *
 * Example usage of this class:
 * ```
 * class MainActivity: AndroidComposeActivity() {
 *
 *     @Composable
 *     override fun OnSetContent(savedInstanceState: Bundle?, darkMode: Boolean) {
 *          // do stuff with Composable Functions
 *          ExampleTheme(darkTheme = darkMode) {
 *          Surface(
 *              modifier = Modifier.fillMaxSize(),
 *              color = MaterialTheme.colorScheme.background
 *          ) {
 *              Greeting("Android")
 *          }
 *     }
 * }
 *
 * @Composable
 * fun Greeting(text: String) {
 *     ...
 * }
 * ```
 *
 * Don't forget to add ```@Composable``` annotation on overriding ```OnSetContent()``` composable function!
 *
 * @see ComponentActivity
 * @see Composable
 */
abstract class AndroidComposeActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBelowCutout()

        val pref = UiPreferences.getInstance(dataStore)
        val uiViewModel = ViewModelProvider(this, UiViewModelFactory(pref))[
                UiViewModel::class.java
        ]

        onSetup(savedInstanceState)
        Log.i("AndroidActivity", "onSetup() triggered successfully")

        setContent {
            val uiMode = uiViewModel.getTheme().observeAsState()
            val darkMode = when (uiMode.value) {
                AppCompatDelegate.MODE_NIGHT_YES -> true
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> isSystemInDarkTheme()
                else -> false
            }
            val modeString = when (uiMode.value) {
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
                    "MODE_UNKNOWN (${uiMode.value})"
                }
            }
            Log.i("AndroidComposeActivity", "Theme successfully applied to $modeString")
            OnSetContent(savedInstanceState, darkMode)
            Log.i("AndroidComposeActivity", "OnSetContent() successfully triggered.")
        }
    }

    override fun onDestroy() {
        onTearDown()
        Log.i("AndroidActivity", "onTearDown() triggered successfully")
        super.onDestroy()
    }

    /**
     * Will be invoked after event [onCreate] was invoked and view was inflated. Override this
     * function if you need to invoke syntax before setContent {}
     */
    @Suppress("UNUSED_PARAMETER")
    protected open fun onSetup(savedInstanceState: Bundle?) {}

    /**
     * Will be invoked after event [onCreate] was invoked and view was inflated. You don't need to
     * call setContent {} here because it's already called here.
     */
    @Composable
    abstract fun OnSetContent(savedInstanceState: Bundle?, darkMode: Boolean)

    /**
     * If you need to nullify variables/components (e.g. adapter), write to this function. This will
     * called on [onDestroy] life cycle after nullifying a binder.
     */
    protected open fun onTearDown() {}
}