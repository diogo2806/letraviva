package br.com.valenstech.letraviva.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.databinding.FragmentHomeBinding;
import br.com.valenstech.letraviva.util.ExtensoesTextView;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;
import br.com.valenstech.letraviva.viewmodel.AutenticacaoViewModel;

public class HomeFragment extends Fragment {

    @Nullable
    private FragmentHomeBinding binding;
    private AutenticacaoViewModel autenticacaoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autenticacaoViewModel = new ViewModelProvider(requireActivity()).get(AutenticacaoViewModel.class);
        autenticacaoViewModel.getUsuario().observe(getViewLifecycleOwner(), usuario -> {
            if (binding == null) {
                return;
            }
            String nomeOriginal = usuario != null ? usuario.getDisplayName() : "";
            if (nomeOriginal == null) {
                nomeOriginal = "";
            }
            String nomeSeguro = ValidacaoConteudo.aplicarEscapeSeguro(nomeOriginal);
            ExtensoesTextView.definirTextoSeguro(binding.tvWelcome, getString(R.string.welcome, nomeSeguro));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
