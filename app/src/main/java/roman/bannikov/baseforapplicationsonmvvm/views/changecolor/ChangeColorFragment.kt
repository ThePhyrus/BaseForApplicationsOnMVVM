package roman.bannikov.baseforapplicationsonmvvm.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.GridLayoutManager
import roman.bannikov.baseforapplicationsonmvvm.R
import roman.bannikov.baseforapplicationsonmvvm.databinding.FragmentChangeColorBinding
import roman.bannikov.baseforapplicationsonmvvm.views.changecolor.ChangeColorViewModel
import roman.bannikov.foundation.views.BaseFragment
import roman.bannikov.foundation.views.BaseScreen
import roman.bannikov.foundation.views.HasScreenTitle
import roman.bannikov.foundation.views.screenViewModel


/**
 * Screen for changing color.
 * 1) Displays the list of available colors
 * 2) Allows choosing the desired color
 * 3) Chosen color is saved only after pressing "Save" button
 * 4) The current choice is saved via [SavedStateHandle] (see [ChangeColorViewModel])
 */

/**
 * Запускается из стартового фрагмента, имеет аргументы
 * */
class ChangeColorFragment : BaseFragment(), HasScreenTitle {

    /**
     * This screen has 1 argument: color ID to be displayed as selected.
     * Этот скрин имеет один аргумент: id цвета, который надо отметить выбранным.
     */
    class Screen(
        //описывем аргументы (только те типы данных, которые можно сериализовать:
        val currentColorId: Long
    ) : BaseScreen


    /**
     *Вот так создайтся вью-модель для фрагмента, используя BaseFragment.screenViewModel().
     * Нужно просто указать название класса, который будет работать с фрагментом. В данном случае
     * с фрагментом ChangeColorFragment будет работать ChangeColorViewModel
     * */
    override val viewModel by screenViewModel<ChangeColorViewModel>()

    /**
     * Example of dynamic screen title
     * Пример динамической смены заголовка экрана
     */
    override fun getScreenTitle(): String? = viewModel.screenTitle.value

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentChangeColorBinding.inflate(inflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(binding, adapter)

        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }

        viewModel.colorsList.observe(viewLifecycleOwner) {
            adapter.items = it
        }
        viewModel.screenTitle.observe(viewLifecycleOwner) {
            // if screen title is changed -> need to notify activity about updates
            notifyScreenUpdates()
        }

        return binding.root
    }

    private fun setupLayoutManager(binding: FragmentChangeColorBinding, adapter: ColorsAdapter) {
        // waiting for list width
        binding.colorsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.colorsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.colorsRecyclerView.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        })
    }
}