package roman.bannikov.baseforapplicationsonmvvm


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import roman.bannikov.baseforapplicationsonmvvm.views.currentcolor.CurrentColorFragment
import roman.bannikov.foundation.ActivityScopeViewModel
import roman.bannikov.foundation.navigator.IntermediateNavigator
import roman.bannikov.foundation.navigator.StackFragmentNavigator
import roman.bannikov.foundation.uiactions.AndroidUiActions
import roman.bannikov.foundation.utils.viewModelCreator
import roman.bannikov.foundation.views.FragmentsHolder

/**
 * This application is a single-activity app. MainActivity is a container
 * for all screens.
 */


class MainActivity : AppCompatActivity(), FragmentsHolder {

    //переменная для навигатора:
    private lateinit var navigator: StackFragmentNavigator
//    private lateinit var binding: ActivityMainBinding

    /**
     * Это вью-модель именно MainActivity и из-за того, что навигация строится на базе фрагментов и
     * должна быть доступна из других вью-моделей, птм что именно ActivityScopeViewModel и реализует
     * навигатор. А также и класс UiActions.
     * */
    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            //передаём реализации:
            uiActions = AndroidUiActions(applicationContext),
            //в навигатор передаём не StackFragmentNavigator, а IntermediateNavigator, который
            // корректно работает с жизненным циклом:
            navigator = IntermediateNavigator()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        //Создаём навигатор:
        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            defaultTitle = getString(R.string.app_name),
            animations = StackFragmentNavigator.Animations(
                enterAnim = R.anim.enter,
                exitAnim = R.anim.exit,
                popEnterAnim = R.anim.pop_enter,
                popExitAnim = R.anim.pop_exit
            ),
            initialScreenCreator = { CurrentColorFragment.Screen() }
        )
        navigator.onCreate(savedInstanceState) // создали навигатор, передали в него бандл


    }


    override fun onDestroy() {
        navigator.onDestroy() //Убили навигатор
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        // обращаемся к навигатору и говорим, что целевой навигатор - это наш navigator
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.navigator.setTarget(null) // нету больше целевого навигатора (очистили)
    }

    override fun notifyScreenUpdates() {
        /**
         * Отвечает за корректное отображение toolBar,
         * частично отвечает за навигацию.
         * Вызывется каждый раз, когда какой-то экран становится активным.
         * Плюс этот метод можно вызвать из любого фрагмента, который наследует BaseFragment
         * */
        navigator.notifyScreenUpdates()
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }
}