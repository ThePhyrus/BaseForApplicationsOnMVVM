package roman.bannikov.foundation.utils

typealias ResourceAction<T> = (T) -> Unit

/**
 * Actions queue, where actions are executed only if resource exists. If it doesn't then
 * action is added to queue and waits until resource becomes available.
 */
class ResourceActions<T> {

    /**
     * Переменная для ресурса. В данном случае ресурсом будет выступать MainActivity.
     * Когда этот ресурс у нам сдесь будет то все действия будут выполняться. Если ресурс
     * существует (активити активна), значит над ним можно выполнить определённые действия. Активна
     * ли активити в данный момент определяет сама активити в методах свого жизненного цикла
     * (см. MainActivity.onResume() и MainActivity.onPause()).
     *
     * Для этого поля "resource" определён свой сеттер, чтобы проверить, есть ли в очереди действия,
     * которые ожидают доступность этого ресурса. И если они есть, то эти действия выполняются
     * */
    var resource: T? = null
        set(newValue) {
            field = newValue
            if (newValue != null) {
                actions.forEach { it(newValue) }
                actions.clear()
            }
        }

    //Список (очередь) действий:
    private val actions = mutableListOf<ResourceAction<T>>()

    /**
     * (Не обязательно)
     * Invoke the action only when [resource] exists (not null). Otherwise
     * the action is postponed until some non-null value is assigned to [resource]
     *
     * Перегрузка оператора. Сдесь это сделано для удобства и позволяет нам параметр
     * action: ResourceAction<T> действия записывать сразу. Если бы сделали это не через оператор
     * invoke, а просто бы определили какой-то дополнительный метод, то просто в коде было бы нужно
     * постоянно вызывать этот самый метод. Это с одной стороны улчшило бы читаемость кода, а с
     * другой - кода было бы менее кратким и лаконичным.
     *
     */
    operator fun invoke(action: ResourceAction<T>) {
        /**
         * Тут определены сама логика вызова действия.
         * Если ресурс (MainActivity в данном случае) не доступен (равен null), то мы добавляем
         * действие (action) просто в очередь, иначе выполняем действие сразу.
         * */
        val resource = this.resource
        if (resource == null) {
            actions += action
        } else {
            action(resource)
        }
    }


    /**
     * Очистит все действия, если они больше не нужны.
     * Конкретно этот метод вызывается в методе onCleared() в ActivityScopeViewModel
     * */
    fun clear() {
        actions.clear()
    }
}