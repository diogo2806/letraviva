package br.com.valenstech.letraviva.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.valenstech.letraviva.databinding.FragmentHomeBinding
import br.com.valenstech.letraviva.viewmodel.AuthViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        authViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvWelcome.text = getString(br.com.valenstech.letraviva.R.string.welcome, user?.displayName ?: "")
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
