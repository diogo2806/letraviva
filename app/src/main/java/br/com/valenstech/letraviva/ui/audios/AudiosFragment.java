package br.com.valenstech.letraviva.ui.audios;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.util.List;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.databinding.FragmentAudiosBinding;
import br.com.valenstech.letraviva.model.ItemAudio;
import br.com.valenstech.letraviva.util.EstadoUi;
import br.com.valenstech.letraviva.util.ExtensoesTextView;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;
import br.com.valenstech.letraviva.viewmodel.AudiosViewModel;

public class AudiosFragment extends Fragment {

    @Nullable
    private FragmentAudiosBinding binding;
    private AudiosViewModel viewModel;
    private AdaptadorAudios adaptadorAudios;
    @Nullable
    private MediaPlayer reprodutor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAudiosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) {
            return;
        }
        adaptadorAudios = new AdaptadorAudios(itemAudio -> reproduzirAudio(itemAudio));
        binding.recyclerAudios.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerAudios.setAdapter(adaptadorAudios);

        viewModel = new ViewModelProvider(this).get(AudiosViewModel.class);
        viewModel.getEstado().observe(getViewLifecycleOwner(), this::processarEstado);
        viewModel.carregarAudios();
    }

    private void processarEstado(@Nullable EstadoUi<List<ItemAudio>> estado) {
        if (estado == null) {
            exibirMensagem(getString(R.string.error_loading_audios));
            return;
        }
        if (estado instanceof EstadoUi.Carregando) {
            mostrarCarregamento();
        } else if (estado instanceof EstadoUi.Sucesso) {
            EstadoUi.Sucesso<List<ItemAudio>> sucesso = (EstadoUi.Sucesso<List<ItemAudio>>) estado;
            exibirAudios(sucesso.getDados());
        } else if (estado instanceof EstadoUi.Vazio) {
            EstadoUi.Vazio<List<ItemAudio>> vazio = (EstadoUi.Vazio<List<ItemAudio>>) estado;
            exibirMensagem(vazio.getMensagem());
        } else if (estado instanceof EstadoUi.Erro) {
            EstadoUi.Erro<List<ItemAudio>> erro = (EstadoUi.Erro<List<ItemAudio>>) estado;
            String mensagemErro = erro.getMensagem();
            if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                mensagemErro = getString(R.string.error_loading_audios);
            }
            exibirMensagem(mensagemErro);
        }
    }

    private void mostrarCarregamento() {
        if (binding == null) {
            return;
        }
        binding.progressAudios.setVisibility(View.VISIBLE);
        binding.recyclerAudios.setVisibility(View.GONE);
        ExtensoesTextView.definirTextoSeguro(binding.tvAudiosMessage, getString(R.string.loading_audios));
        binding.tvAudiosMessage.setVisibility(View.VISIBLE);
        binding.tvNowPlaying.setVisibility(View.GONE);
    }

    private void exibirAudios(@NonNull List<ItemAudio> audios) {
        if (binding == null) {
            return;
        }
        binding.progressAudios.setVisibility(View.GONE);
        binding.tvAudiosMessage.setVisibility(View.GONE);
        binding.recyclerAudios.setVisibility(View.VISIBLE);
        adaptadorAudios.atualizarItens(audios);
        binding.tvNowPlaying.setVisibility(reprodutor != null ? View.VISIBLE : View.GONE);
    }

    private void exibirMensagem(@NonNull String mensagem) {
        if (binding == null) {
            return;
        }
        binding.progressAudios.setVisibility(View.GONE);
        binding.recyclerAudios.setVisibility(View.GONE);
        ExtensoesTextView.definirTextoSeguro(binding.tvAudiosMessage, mensagem);
        binding.tvAudiosMessage.setVisibility(View.VISIBLE);
        binding.tvNowPlaying.setVisibility(View.GONE);
    }

    private void reproduzirAudio(@NonNull ItemAudio itemAudio) {
        if (binding == null) {
            return;
        }
        ExtensoesTextView.definirTextoSeguro(binding.tvNowPlaying,
                getString(R.string.preparing_audio, ValidacaoConteudo.aplicarEscapeSeguro(itemAudio.getTitulo())));
        binding.tvNowPlaying.setVisibility(View.VISIBLE);
        liberarReprodutor();
        MediaPlayer novoReprodutor = new MediaPlayer();
        novoReprodutor.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build());
        reprodutor = novoReprodutor;
        try {
            novoReprodutor.setDataSource(itemAudio.getUrlTransmissao());
            novoReprodutor.setOnPreparedListener(player -> {
                ExtensoesTextView.definirTextoSeguro(binding.tvNowPlaying,
                        getString(R.string.now_playing,
                                ValidacaoConteudo.aplicarEscapeSeguro(itemAudio.getTitulo())));
                player.start();
            });
            novoReprodutor.setOnCompletionListener(player -> {
                ExtensoesTextView.definirTextoSeguro(binding.tvNowPlaying,
                        getString(R.string.audio_finished,
                                ValidacaoConteudo.aplicarEscapeSeguro(itemAudio.getTitulo())));
            });
            novoReprodutor.setOnErrorListener((player, what, extra) -> {
                ExtensoesTextView.definirTextoSeguro(binding.tvNowPlaying,
                        getString(R.string.error_play_audio));
                player.reset();
                player.release();
                reprodutor = null;
                return true;
            });
            novoReprodutor.prepareAsync();
        } catch (IOException excecao) {
            ExtensoesTextView.definirTextoSeguro(binding.tvNowPlaying,
                    getString(R.string.error_play_audio));
            novoReprodutor.reset();
            novoReprodutor.release();
            reprodutor = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        liberarReprodutor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        liberarReprodutor();
        binding = null;
    }

    private void liberarReprodutor() {
        if (reprodutor != null) {
            reprodutor.reset();
            reprodutor.release();
            reprodutor = null;
        }
        if (binding != null) {
            binding.tvNowPlaying.setVisibility(View.GONE);
        }
    }
}
