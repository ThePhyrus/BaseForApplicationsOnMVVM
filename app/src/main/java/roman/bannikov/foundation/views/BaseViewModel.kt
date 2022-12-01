package roman.bannikov.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import roman.bannikov.foundation.utils.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

/**
 * Base class for all view-models.
 *
 * Базовый класс для всех вью-моделей, которые управляют экранами. То есть, для всех вью-моделей,
 * которые находятся внутри фрагментов
 */
open class BaseViewModel : ViewModel() {

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     *
     *
     */
    open fun onResult(result: Any) {
        /**
         * Та вью-модель, которая хочет слушать результаты с других экранов просто переопределяет
         * этот метод и здесь проверяет результат на соответсвие определённого типа, который
         * нужен конкретно этой вью-модели.
         * */
    }

}