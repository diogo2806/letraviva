package br.com.valenstech.letraviva.ui.planos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.databinding.FragmentPlanosBinding
import br.com.valenstech.letraviva.model.ReadingPlan
import br.com.valenstech.letraviva.util.UiState
import br.com.valenstech.letraviva.viewmodel.PlanosViewModel
import br.com.valenstech.letraviva.util.definirTextoSeguro

class PlanosFragment : Fragment() {

    private var _binding: FragmentPlanosBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlanosViewModel
    private lateinit var adapter: PlansAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlanosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PlansAdapter(emptyList())
        binding.recyclerPlans.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPlans.adapter = adapter

        viewModel = ViewModelProvider(this)[PlanosViewModel::class.java]
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoadingState()
                is UiState.Success -> showPlans(state.data)
                is UiState.Empty -> showMessage(state.message)
                is UiState.Error -> showMessage(state.message)
            }
        }
        viewModel.loadPlans()
    }

    private fun showLoadingState() {
        binding.progressPlans.isVisible = true
        binding.recyclerPlans.isVisible = false
        binding.tvPlansMessage.apply {
            definirTextoSeguro(getString(R.string.loading_plans))
            isVisible = true
        }
    }

    private fun showPlans(plans: List<ReadingPlan>) {
        binding.progressPlans.isVisible = false
        binding.tvPlansMessage.isVisible = false
        binding.recyclerPlans.isVisible = true
        adapter.updatePlans(plans)
    }

    private fun showMessage(message: String) {
        binding.progressPlans.isVisible = false
        binding.recyclerPlans.isVisible = false
        binding.tvPlansMessage.isVisible = true
        binding.tvPlansMessage.definirTextoSeguro(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
