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
import br.com.valenstech.letraviva.model.DestaquePrincipal;
import br.com.valenstech.letraviva.util.EstadoUi;
import br.com.valenstech.letraviva.util.ExtensoesTextView;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;
import br.com.valenstech.letraviva.viewmodel.AutenticacaoViewModel;
import br.com.valenstech.letraviva.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    @Nullable
    private FragmentHomeBinding binding;
    private AutenticacaoViewModel autenticacaoViewModel;
    private HomeViewModel homeViewModel;

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

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getEstadoDestaque().observe(getViewLifecycleOwner(), this::processarEstadoDestaque);
        homeViewModel.carregarDestaque();
    }

    private void processarEstadoDestaque(@Nullable EstadoUi<DestaquePrincipal> estado) {
        if (binding == null) {
            return;
        }
        if (estado == null) {
            mostrarMensagemDestaque(getString(R.string.error_loading_highlight));
            return;
        }
        if (estado instanceof EstadoUi.Carregando) {
            mostrarCarregamentoDestaque();
        } else if (estado instanceof EstadoUi.Sucesso) {
            EstadoUi.Sucesso<DestaquePrincipal> sucesso = (EstadoUi.Sucesso<DestaquePrincipal>) estado;
            mostrarDestaque(sucesso.getDados());
        } else if (estado instanceof EstadoUi.Vazio) {
            EstadoUi.Vazio<DestaquePrincipal> vazio = (EstadoUi.Vazio<DestaquePrincipal>) estado;
            mostrarMensagemDestaque(vazio.getMensagem());
        } else if (estado instanceof EstadoUi.Erro) {
            EstadoUi.Erro<DestaquePrincipal> erro = (EstadoUi.Erro<DestaquePrincipal>) estado;
            String mensagemErro = erro.getMensagem();
            if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                mensagemErro = getString(R.string.error_loading_highlight);
            }
            mostrarMensagemDestaque(mensagemErro);
        }
    }

    private void mostrarCarregamentoDestaque() {
        if (binding == null) {
            return;
        }
        binding.progressHighlight.setVisibility(View.VISIBLE);
        binding.layoutHighlightConteudo.setVisibility(View.GONE);
        ExtensoesTextView.definirTextoSeguro(binding.tvHighlightMensagem, getString(R.string.loading_highlight));
        binding.tvHighlightMensagem.setVisibility(View.VISIBLE);
    }

    private void mostrarDestaque(@NonNull DestaquePrincipal destaque) {
        if (binding == null) {
            return;
        }
        binding.progressHighlight.setVisibility(View.GONE);
        binding.tvHighlightMensagem.setVisibility(View.GONE);
        binding.layoutHighlightConteudo.setVisibility(View.VISIBLE);
        boolean tituloVisivel = !destaque.getTitulo().isEmpty();
        if (tituloVisivel) {
            ExtensoesTextView.definirTextoSeguro(binding.tvHighlightTitulo, destaque.getTitulo());
        }
        binding.tvHighlightTitulo.setVisibility(tituloVisivel ? View.VISIBLE : View.GONE);
        ExtensoesTextView.definirTextoSeguro(binding.tvHighlightDescricao, destaque.getDescricao());
    }

    private void mostrarMensagemDestaque(@NonNull String mensagem) {
        if (binding == null) {
            return;
        }
        binding.progressHighlight.setVisibility(View.GONE);
        binding.layoutHighlightConteudo.setVisibility(View.GONE);
        ExtensoesTextView.definirTextoSeguro(binding.tvHighlightMensagem, mensagem);
        binding.tvHighlightMensagem.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
