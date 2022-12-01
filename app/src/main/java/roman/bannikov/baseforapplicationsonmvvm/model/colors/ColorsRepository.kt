package roman.bannikov.baseforapplicationsonmvvm.model.colors

import roman.bannikov.foundation.model.Repository

typealias ColorListener = (NamedColor) -> Unit

/**
 * Repository interface example.
 *
 * Provides access to the available colors and current selected color.
 *
 * Наследуется от класса Repository из пакета foundation и поределяет список действий, полей,
 * будут доступны для этого репозитория. Именно этот (ColorsRepository) интерфейс будет
 * использоваться для конструктора вью-модели, которая будет работать с ColorsRepository. А уже
 * конкретная реализация ColorsRepository (пока одна и называется InMemoryColorsRepository)
 * реализует interface ColorsRepository.
 */
interface ColorsRepository : Repository {

    var currentColor: NamedColor

    /**
     * Get the list of all available colors that may be chosen by the user.
     */
    fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color content by its ID
     */
    fun getById(id: Long): NamedColor

    /**
     * Listen for the current color changes.
     * The listener is triggered immediately with the current value when calling this method.
     */
    fun addListener(listener: ColorListener)

    /**
     * Stop listening for the current color changes
     */
    fun removeListener(listener: ColorListener)

}