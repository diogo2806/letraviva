package br.com.valenstech.letraviva.ui.planos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.valenstech.letraviva.databinding.FragmentPlanosBinding
import br.com.valenstech.letraviva.viewmodel.PlanosViewModel

class PlanosFragment : Fragment() {

    private var _binding: FragmentPlanosBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlanosViewModel
    private lateinit var adapter: PlansAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanosBinding.inflate(inflater, container, false)
        adapter = PlansAdapter(emptyList())
        binding.recyclerPlans.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPlans.adapter = adapter

        viewModel = ViewModelProvider(this)[PlanosViewModel::class.java]
        viewModel.plans.observe(viewLifecycleOwner) { plans ->
            adapter.updatePlans(plans)
        }
        viewModel.loadPlans()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
