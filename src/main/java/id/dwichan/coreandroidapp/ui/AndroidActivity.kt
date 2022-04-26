package id.dwichan.coreandroidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import timber.log.Timber

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

    /**
     * Represent how the ViewBinding inflated.
     */
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        Timber.i("ViewBinding ${binding.javaClass.name} are displayed as activity.")
        onSetup(savedInstanceState)
        Timber.i("onSetup() triggered successfully")
    }

    /**
     * Will be invoked after event [onCreate] was invoked and view was inflated. You can use
     * [binding] variable to modify what view must do!
     */
    abstract fun onSetup(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Timber.i("ViewBinding ${binding.javaClass.name} are nullified.")
    }

}