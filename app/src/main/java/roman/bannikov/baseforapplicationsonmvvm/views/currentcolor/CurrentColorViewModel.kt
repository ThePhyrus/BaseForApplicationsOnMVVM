package roman.bannikov.baseforapplicationsonmvvm.views.currentcolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import roman.bannikov.baseforapplicationsonmvvm.R
import roman.bannikov.baseforapplicationsonmvvm.model.colors.ColorListener
import roman.bannikov.baseforapplicationsonmvvm.model.colors.ColorsRepository
import roman.bannikov.baseforapplicationsonmvvm.model.colors.NamedColor
import roman.bannikov.baseforapplicationsonmvvm.views.changecolor.ChangeColorFragment
import roman.bannikov.foundation.navigator.Navigator
import roman.bannikov.foundation.uiactions.UiActions
import roman.bannikov.foundation.views.BaseViewModel


class CurrentColorViewModel(
    //перечисляем зависимости этой вью-модели
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveData<NamedColor>()
    val currentColor: LiveData<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    // --- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        /**
         * Пример слушанья результата полученного во вором экране.
         * То есть, когда во втором экране выбрали нужный цвет и нажали на кнопку save, второй
         * экран посылает результат, а этот экран его получает.
         * */
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}