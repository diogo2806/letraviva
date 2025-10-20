package br.com.valenstech.letraviva.ui.planos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;
import java.util.List;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.databinding.FragmentPlanosBinding;
import br.com.valenstech.letraviva.model.PlanoLeitura;
import br.com.valenstech.letraviva.util.EstadoUi;
import br.com.valenstech.letraviva.util.ExtensoesTextView;
import br.com.valenstech.letraviva.viewmodel.PlanosViewModel;

public class PlanosFragment extends Fragment {

    @Nullable
    private FragmentPlanosBinding binding;
    private PlanosViewModel viewModel;
    private AdaptadorPlanos adaptadorPlanos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlanosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) {
            return;
        }
        adaptadorPlanos = new AdaptadorPlanos(Collections.emptyList());
        binding.recyclerPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerPlans.setAdapter(adaptadorPlanos);

        viewModel = new ViewModelProvider(this).get(PlanosViewModel.class);
        viewModel.getEstado().observe(getViewLifecycleOwner(), this::processarEstado);
        viewModel.carregarPlanos();
    }

    private void processarEstado(@Nullable EstadoUi<List<PlanoLeitura>> estado) {
        if (estado == null) {
            exibirMensagem(getString(R.string.error_loading_plans));
            return;
        }
        if (estado instanceof EstadoUi.Carregando) {
            mostrarCarregamento();
        } else if (estado instanceof EstadoUi.Sucesso) {
            EstadoUi.Sucesso<List<PlanoLeitura>> sucesso = (EstadoUi.Sucesso<List<PlanoLeitura>>) estado;
            exibirPlanos(sucesso.getDados());
        } else if (estado instanceof EstadoUi.Vazio) {
            EstadoUi.Vazio<List<PlanoLeitura>> vazio = (EstadoUi.Vazio<List<PlanoLeitura>>) estado;
            exibirMensagem(vazio.getMensagem());
        } else if (estado instanceof EstadoUi.Erro) {
            EstadoUi.Erro<List<PlanoLeitura>> erro = (EstadoUi.Erro<List<PlanoLeitura>>) estado;
            String mensagemErro = erro.getMensagem();
            if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                mensagemErro = getString(R.string.error_loading_plans);
            }
            exibirMensagem(mensagemErro);
        }
    }

    private void mostrarCarregamento() {
        if (binding == null) {
            return;
        }
        binding.progressPlans.setVisibility(View.VISIBLE);
        binding.recyclerPlans.setVisibility(View.GONE);
        ExtensoesTextView.definirTextoSeguro(binding.tvPlansMessage, getString(R.string.loading_plans));
        binding.tvPlansMessage.setVisibility(View.VISIBLE);
    }

    private void exibirPlanos(@NonNull List<PlanoLeitura> planos) {
        if (binding == null) {
            return;
        }
        binding.progressPlans.setVisibility(View.GONE);
        binding.tvPlansMessage.setVisibility(View.GONE);
        binding.recyclerPlans.setVisibility(View.VISIBLE);
        adaptadorPlanos.atualizarPlanos(planos);
    }

    private void exibirMensagem(@NonNull String mensagem) {
        if (binding == null) {
            return;
        }
        binding.progressPlans.setVisibility(View.GONE);
        binding.recyclerPlans.setVisibility(View.GONE);
        ExtensoesTextView.definirTextoSeguro(binding.tvPlansMessage, mensagem);
        binding.tvPlansMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
