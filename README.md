# core-android-app
Repository ini saya buat sebagai bahan dasar dalam pembuat aplikasi Android saya ke depannya. Dengan menggunakan library ini sebagai dasar aplikasi, maka kemungkinan kebocoran memori yang disebabkan oleh Activity/Fragment semakin kecil (hingga 5% kemungkinan).

Untuk implementasinya, cukup jadikan ```AndroidActivity<ViewBinding>()``` atau ```AndroidFragment<ViewBinding>()``` sebagai superclass. berikut contoh implementasinya:

### Activity
```
class MainActivity: AndroidActivity<ActivityMainBinding>() {
    override val isLastActivity: Boolean
        get() = true

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
        
    override fun onSetup() {
        // ... do some stuff after successfully inflate view at onCreate lifecycle
    }
    
    override fun onTearDown() {
        // ... do some stuff after the activity has been destroyed
    }
}
```
### Fragment
```
class UserProfileFragment: AndroidFragment<FragmentUserProfileBinding>() {
    override val isLastActivity: Boolean
        get() = true

    override val bindingInflater: (LayoutInflater) -> FragmentUserProfileBinding
        get() = FragmentUserProfileBinding::inflate
        
    override fun onSetup() {
        // ... do some stuff after successfully inflate view at onViewCreated lifecycle
    }
    
    override fun onTearDown() {
        // ... do some stuff after the fragment has been destroyed
    }
}
```
