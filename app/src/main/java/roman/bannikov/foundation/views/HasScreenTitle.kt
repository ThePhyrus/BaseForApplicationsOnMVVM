package roman.bannikov.foundation.views

/**
 * If your fragment wants to show custom screen title in the toolbar, implement this
 * interface and override [getScreenTitle] method.
 *
 * Please note if screen title can be changed dynamically while fragment is active, you should
 * call [BaseFragment.notifyScreenUpdates] method to re-render activity toolbar.
 */
interface HasScreenTitle {

    /**
     * interface HasScreenTitle может быть использован любым фрагментом, если этот фрагмент хочет
     * показать в тулбаре какой-то свой заголовок отличный от стандартного.
     * В данном случае мы его используем в ChangeColorFragment.
     * */

    fun getScreenTitle(): String?

}