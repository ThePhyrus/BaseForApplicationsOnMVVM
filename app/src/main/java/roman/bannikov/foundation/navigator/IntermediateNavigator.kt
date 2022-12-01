package roman.bannikov.foundation.navigator

import roman.bannikov.foundation.utils.ResourceActions
import roman.bannikov.foundation.views.BaseScreen

/**
 * Mediator that holds nav actions in the queue if real navigator is not active.
 * Класс написан только для того, чтобы перенаправлять действия в реальный навигатор.
 * Всё из чего он состоит это targetNavigator и те аргументы, которые этому targetNavigator
 * передаются
 *
 * Нужен для того, чтобы решить проблему с доступностью активити.
 * Этот класс будет брать все вызовы, которые поступают по навигации (launch(), goBack()) и в
 * случае, если у нас не будет ресурса (активити), то действия будут откладываться в очередь.
 * Работать этот класс будет на стороне вью-модели. Здесь нет никаких зависимостей от активити,
 * поэтому здесь всё безопасно с этой точки зрения. Плюс, мы используем ResourceActions, а значит
 * не будет утечек памяти потому, что в случае чего мы будем вызывать setTarget() в
 * MainActivity.onResume (там мы будем передавать реальный навигатор), а в MainActivity.onPause()
 * будем ставить setTarget(null).
 * В clear() (когда вью-модель активити будет уничтожаться) мы очистим все действия (очистим
 * очередь действий) и соответсвенно никаких утечек не случится.
 */
class IntermediateNavigator : Navigator {

    private val targetNavigator = ResourceActions<Navigator>()

    override fun launch(screen: BaseScreen) = targetNavigator {
        /**
         * Когда из какого-то фрагмента мы вызываем метод launch(), то вызывается именно этот
         * метод. Если активити активна, то метод сразу передаст вызов тому навигатору, который
         * отвечает за навигацию. Если неактивна, то отложит это действие в очередь и отдаст
         * уже когда активити будет активна.
         * */
        it.launch(screen) //передаются аргументы
    }

    override fun goBack(result: Any?) = targetNavigator {
        it.goBack(result) //передаются аргументы
    }


    // для того, чтобы назначать навигатор, который реализует саму навигацию в targetNavigator
    fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }


    fun clear() {
        targetNavigator.clear()
    }

    /**
     * Если у нас добавятся ещё какие-то методы, то их прописывать будем по аналогии выше.
     * */


}