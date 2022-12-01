package roman.bannikov.baseforapplicationsonmvvm.views.changecolor

import roman.bannikov.baseforapplicationsonmvvm.model.colors.NamedColor

/**
 * Represents list item for the color; it may be selected or not
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)