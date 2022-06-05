package id.dwichan.coreandroidapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import id.dwichan.coreandroidapp.R

/**
 * Extends the class as Android Fragment. This will avoid the memory leak caused by a binder class.
 *
 * Example usage of this class:
 * ```
 * class ProfileFragment: AndroidFragment<FragmentProfileBinding>() {
 *     override val bindingInflater: (LayoutInflater) -> ViewBinding =
 *         FragmentProfileBinding::inflate
 *
 *     override fun onSetup(){
 *          // do stuff with binding variable
 *          binding.textExample.text = "OK"
 *     }
 * }
 * ```
 * @param VB represent the ViewBinding
 * @see Fragment
 */
abstract class AndroidFragment<VB : ViewBinding>: Fragment() {

    private var _binding: ViewBinding? = null

    /**
     * Represent how the ViewBinding inflated.
     */
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB?
        get() = _binding as VB?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        Log.i("AndroidFragment", "ViewBinding ${binding?.javaClass?.name} are invoked.")
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("AndroidFragment", "ViewBinding ${binding!!.javaClass.name} are created as fragment.")
        onSetup(savedInstanceState)
        Log.i("AndroidFragment", "onSetup() triggered successfully")
    }

    /**
     * Will be invoked after event [onViewCreated] was invoked and view was inflated. You can use
     * [binding] variable to modify what view must do!
     */
    abstract fun onSetup(savedInstanceState: Bundle?)

    override fun onDestroyView() {
        val className = binding?.javaClass?.name
        _binding = null
        Log.i("AndroidActivity", "ViewBinding $className are nullified.")
        onTearDown()
        Log.i("AndroidFragment", "onTearDown() triggered successfully")
        super.onDestroyView()
    }

    /**
     * If you need to nullify variables/components (e.g. adapter), write to this function. This will
     * called on [onDestroyView] life cycle after nullifying a binder.
     */
    protected open fun onTearDown() {

    }
}