package br.com.valenstech.letraviva.ui.planos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.valenstech.letraviva.databinding.FragmentPlanosBinding
import br.com.valenstech.letraviva.model.ReadingPlan

class PlanosFragment : Fragment() {

    private var _binding: FragmentPlanosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanosBinding.inflate(inflater, container, false)
        val plans = listOf(
            ReadingPlan("Plano 1"),
            ReadingPlan("Plano 2"),
            ReadingPlan("Plano 3")
        )
        binding.recyclerPlans.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPlans.adapter = PlansAdapter(plans)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
