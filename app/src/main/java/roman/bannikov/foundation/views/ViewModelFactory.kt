package roman.bannikov.foundation.views

import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import roman.bannikov.foundation.BaseApplication
import roman.bannikov.foundation.views.BaseScreen.Companion.ARG_SCREEN
import java.lang.reflect.Constructor

/**
 * Этот код пишется один раз, а дальше мы им просто пользуемся.
 * Краткая инструкция:
 * Если нужно создать какой-то экран, то
 * 1 - во фрагменте мы пишем нечто вроде:
 *  override val viewModel by screenViewModel<ИмяВьюМодели>() (вызываем screenViewModel);
 * 2 - во вью-модели этого фрагмента (в её конструкторе) указывем все необходимые ей зависимости;
 *
 * Таким образом слой view становится максимально простым. Вся логика будет находится во вью-модели
 * (включая навигацию, приём и передачу аргументов, все вызовы на основе модели и плюс какие-то
 * типичные действия по UI (toast
 * */

/**
 * Use this method for getting view-models from your fragments
 *
 * Этот фаил содержит метод BaseFragment.screenViewModel(), с помощью которого мы создаём
 * вью-модели для наших фрагментов и саму фабрику вью-моделей.
 */

/**
 * Как формируется сам список зависимостей (private val dependencies: List<Any>):
 * В данном приложении пока всего три скоупа:
 * 1 - application scope (содержить список моделей (репозиториев)
 * 2 - activityScopeViewModel scope (который состоит из Navigator и UiActions)
 * 3 - screen (скоуп самого экрана (аргументы экрана))
 * */

inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val application = requireActivity().application as BaseApplication
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen

    val activityScopeViewModel = (requireActivity() as FragmentsHolder).getActivityScopeViewModel()

    // forming the list of available dependencies:
    // - singleton scope dependencies (repositories) -> from App class
    // - activity VM scope dependencies -> from ActivityScopeViewModel
    // - screen VM scope dependencies -> screen args
    val dependencies = listOf(screen, activityScopeViewModel) + application.repositories

    // creating factory
    ViewModelFactory(dependencies, this)
}


/**
 * Фабрика вью-моделей
 * Напрямую эта фабрика нигде не используется, напрямую используется BaseFragment.screenViewModel()
 * Фабрика позволяет создавать вью-модели. Так как пока у нас нет никаких dependency injection
 * framework, то мы вручную собираем зависимости для всех вью-моделей. То есть, кода мы создаём
 * какую-то вью-модель, мы в её конструкторе указываем от чего она зависит, а зависимости у неё
 * бывают из "скоупов" (
 * 1-ый скоуп - зависимоти от экрана (аргументы скрина + SavedStateHandle);
 * 2-ой скоуп - зависимости от Navigator и UiActions;
 * 3-ий скоуп - (опционально) если есть зависимости от модели;
 *
 * Заметьте, что в конструкоре вью-модели указываются только интерфейсы, но не реализации. Таким
 * образом, если мы решим поменять архитектуру приложения или поменять способ реализации
 * какого-либо из интефейсов (или всех сразу) на какой-либо другой, то, соответсятвенно, мы будем
 * менять ТОЛЬКО эти реализации, а во вью-модели ничего менять будет не нужно.
 * Также, исползование интерфейсов в конструкторе вью-модели позволит написать юнит-тесты для неё.
 * При написании юнит-тестов интефейсы заменятся специальными заглушками (мокки).
 * */

/**
 * ViewModelFactory конкретно в данном проекте очень универсальна. Мы млжем создавать вью-модели
 * практически с любыми конструкторами (ей нужен скрин - указывем в контрукторе, нужен навигатор -
 * указывем в контрукторе, нужен SavedStateHandle - указывем в контрукторе и т.д. При этом
 * ViewModelFactory корректно создаст вью-модель с конструктором и передаст только нужные аргументы.
 *
 * Ниже описывается, как это происходит:
 * Во-первых: наша ViewModelFactory наследуется от AbstractSavedStateViewModelFactory потому, что
 * мы поддерживаем (хотим поддежать) во вью-моделях SavedStateHandle. Этот SavedStateHandle придёт
 * в аргументах в методе create(), в котором и создаётся вью-модель.
 *
 * В конструкторе ViewModelFactory мы перезаём все возможные зависимости (dependencies: List<Any>),
 * а потом этот список (dependencies: List<Any>) используется для поиска нужного конструктора.
 * */


class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        //берём конструкторы (обычно у вью-модели будет только один конструктор)
        val constructors = modelClass.constructors

        //ищем конструктор с максимальным количеством параметров (для необычной ситуации)
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!

        //формируем список зависимостей на основании тех зависимостей, которые передали в фабрику
        // + handle (тот, который handle: SavedStateHandle)
        val dependenciesWithSavedState = dependencies + handle // список со всеми возможными зави...

        /**
         * Вызываем метод findDependencies(), в который передаём constructor с самым большим
         * количеством зависимостей и полный список зависимостей dependenciesWithSavedState.
         * В результате метод findDependencies() сформирует нам arguments для конструктора
         * вью-модели. То есть, если во вью-модели есть, допустим, пять аргументов, то
         * findDependencies() сформирует список из пяти аргументов, которые будут расположены в
         * том же самом порядке, в котором они указаны в конструкторе вью-модели. Поэтому можно
         * будет безопасно вызвать метод newInstance() и вью-модель нормально создастся.
         * */
        val arguments = findDependencies(constructor, dependenciesWithSavedState)

        // creating view-model
        return constructor.newInstance(*arguments.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>): List<Any> {
        /**
         * Метод берёт constructor, проходит по всем его параметрам и ищет параметр соответсвующего
         * типа в dependencies: List<Any> + handle. И когда этот параметр находится, метод просто
         * добавляет его список и возвращает этот список в arguments.
         * */
        val args = mutableListOf<Any>()
        // here we iterate through view-model's constructor arguments and for each
        // argument we search dependency that can be assigned to the argument
        constructor.parameterTypes.forEach { parameterClass -> //ищет параметр
            //записывает его в val dependency
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            args.add(dependency)
        }
        return args //возвращает список
    }

}