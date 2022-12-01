package roman.bannikov.foundation.views

import androidx.fragment.app.Fragment

/**
 * Base class for all fragments
 * Нкаких зависимостей от MainActivity! )))
 */
abstract class BaseFragment : Fragment() {

    /**
     * abstract class BaseFragment мы будем использовать для того, чтобы реализовывать все наши
     * экраны. Все фрагменты будут от него наследоваться.
     * */

    /**
     * View-model that manages this fragment
     * Это поле будет определять вью-модель, которая будет упровлять фрагментом
     */
    abstract val viewModel: BaseViewModel

    /**
     * Call this method when activity controls (e.g. toolbar) should be re-rendered
     */
    fun notifyScreenUpdates() {
        /**
         * В идеале должен быть создан и всё-таки был создан отдельный интерфейс FragmentsHolder и
         * на нём вызывается notifyScreenUpdates().
         * Тогда, если нам вдруг понадобиться создавать ещё одну активити, то она будет наследовать
         * этот интерфейс. Всё остальное не поломается.
         * */
        (requireActivity() as FragmentsHolder).notifyScreenUpdates()


    }
}