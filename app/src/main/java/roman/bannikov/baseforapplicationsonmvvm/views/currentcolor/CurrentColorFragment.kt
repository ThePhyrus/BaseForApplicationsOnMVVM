package roman.bannikov.baseforapplicationsonmvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import roman.bannikov.baseforapplicationsonmvvm.databinding.FragmentCurrentColorBinding
import roman.bannikov.foundation.views.BaseFragment
import roman.bannikov.foundation.views.BaseScreen
import roman.bannikov.foundation.views.screenViewModel

class CurrentColorFragment : BaseFragment() {

    /**
     * Стартовый фрагмент
     * */


    // no arguments for this screen
    class Screen : BaseScreen // определяется скрин для этого фрагмента

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)

        viewModel.currentColor.observe(viewLifecycleOwner) {
            binding.colorView.setBackgroundColor(it.value)
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        return binding.root
    }


}