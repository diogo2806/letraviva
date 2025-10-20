package br.com.valenstech.letraviva.ui.devocional;

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

import java.io.IOException;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.databinding.FragmentDevocionalBinding;
import br.com.valenstech.letraviva.model.ConteudoDevocional;
import br.com.valenstech.letraviva.util.EstadoUi;
import br.com.valenstech.letraviva.util.ExtensoesTextView;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;
import br.com.valenstech.letraviva.viewmodel.DevocionalViewModel;

public class DevocionalFragment extends Fragment {

    @Nullable
    private FragmentDevocionalBinding binding;
    @Nullable
    private MediaPlayer reprodutor;
    @Nullable
    private ConteudoDevocional conteudoAtual;
    private DevocionalViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDevocionalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DevocionalViewModel.class);
        if (binding == null) {
            return;
        }
        binding.btnPlay.setEnabled(false);
        binding.btnPlay.setOnClickListener(botao -> {
            if (conteudoAtual != null && conteudoAtual.getUrlAudio() != null && !conteudoAtual.getUrlAudio().isEmpty()) {
                reproduzirAudio(conteudoAtual);
            }
        });
        viewModel.getEstado().observe(getViewLifecycleOwner(), estado -> {
            if (estado == null) {
                mostrarMensagem(getString(R.string.error_loading_devotional));
                return;
            }
            if (estado instanceof EstadoUi.Carregando) {
                mostrarEstadoCarregamento();
            } else if (estado instanceof EstadoUi.Sucesso) {
                EstadoUi.Sucesso<ConteudoDevocional> sucesso = (EstadoUi.Sucesso<ConteudoDevocional>) estado;
                mostrarDevocional(sucesso.getDados());
            } else if (estado instanceof EstadoUi.Vazio) {
                EstadoUi.Vazio<ConteudoDevocional> vazio = (EstadoUi.Vazio<ConteudoDevocional>) estado;
                mostrarMensagem(vazio.getMensagem());
            } else if (estado instanceof EstadoUi.Erro) {
                EstadoUi.Erro<ConteudoDevocional> erro = (EstadoUi.Erro<ConteudoDevocional>) estado;
                String mensagemErro = erro.getMensagem();
                if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                    mensagemErro = getString(R.string.error_loading_devotional);
                }
                mostrarMensagem(mensagemErro);
            }
        });
        viewModel.carregarDevocional();
    }

    private void mostrarEstadoCarregamento() {
        if (binding == null) {
            return;
        }
        binding.progressDevotional.setVisibility(View.VISIBLE);
        ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalMessage, getString(R.string.loading_devotional));
        binding.tvDevotionalMessage.setVisibility(View.VISIBLE);
        binding.contentDevotional.setVisibility(View.GONE);
        binding.btnPlay.setVisibility(View.GONE);
        binding.btnPlay.setEnabled(false);
    }

    private void mostrarDevocional(@NonNull ConteudoDevocional conteudo) {
        conteudoAtual = conteudo;
        if (binding == null) {
            return;
        }
        binding.progressDevotional.setVisibility(View.GONE);
        binding.tvDevotionalMessage.setVisibility(View.GONE);
        binding.contentDevotional.setVisibility(View.VISIBLE);
        ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalTitle, conteudo.getTitulo());
        binding.tvDevotionalTitle.setVisibility(conteudo.getTitulo().isEmpty() ? View.GONE : View.VISIBLE);
        ExtensoesTextView.definirTextoSeguro(binding.tvDevotional, conteudo.getTexto());
        boolean possuiAudio = conteudo.getUrlAudio() != null && !conteudo.getUrlAudio().isEmpty();
        binding.btnPlay.setVisibility(possuiAudio ? View.VISIBLE : View.GONE);
        binding.btnPlay.setEnabled(possuiAudio);
    }

    private void mostrarMensagem(@NonNull String mensagem) {
        conteudoAtual = null;
        if (binding == null) {
            return;
        }
        binding.progressDevotional.setVisibility(View.GONE);
        binding.contentDevotional.setVisibility(View.GONE);
        binding.btnPlay.setVisibility(View.GONE);
        binding.btnPlay.setEnabled(false);
        ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalMessage, mensagem);
        binding.tvDevotionalMessage.setVisibility(View.VISIBLE);
    }

    private void reproduzirAudio(@NonNull ConteudoDevocional conteudo) {
        if (binding == null) {
            return;
        }
        String urlAudio = conteudo.getUrlAudio();
        if (urlAudio == null) {
            return;
        }
        binding.btnPlay.setEnabled(false);
        String tituloSeguro = ValidacaoConteudo.aplicarEscapeSeguro(
                conteudo.getTitulo().isEmpty() ? getString(R.string.devotional_default_title) : conteudo.getTitulo()
        );
        ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalMessage,
                getString(R.string.preparing_audio, tituloSeguro));
        binding.tvDevotionalMessage.setVisibility(View.VISIBLE);
        liberarReprodutor();
        MediaPlayer novoReprodutor = new MediaPlayer();
        novoReprodutor.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build());
        reprodutor = novoReprodutor;
        try {
            novoReprodutor.setDataSource(urlAudio);
            novoReprodutor.setOnPreparedListener(player -> {
                String tituloPronto = ValidacaoConteudo.aplicarEscapeSeguro(
                        conteudo.getTitulo().isEmpty() ? getString(R.string.devotional_default_title) : conteudo.getTitulo()
                );
                ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalMessage,
                        getString(R.string.now_playing, tituloPronto));
                binding.btnPlay.setEnabled(true);
                player.start();
            });
            novoReprodutor.setOnCompletionListener(player -> {
                String tituloFinalizado = ValidacaoConteudo.aplicarEscapeSeguro(
                        conteudo.getTitulo().isEmpty() ? getString(R.string.devotional_default_title) : conteudo.getTitulo()
                );
                ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalMessage,
                        getString(R.string.audio_finished, tituloFinalizado));
                binding.btnPlay.setEnabled(true);
            });
            novoReprodutor.setOnErrorListener((player, what, extra) -> {
                ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalMessage,
                        getString(R.string.error_play_audio));
                binding.btnPlay.setEnabled(true);
                player.reset();
                player.release();
                reprodutor = null;
                return true;
            });
            novoReprodutor.prepareAsync();
        } catch (IOException excecao) {
            ExtensoesTextView.definirTextoSeguro(binding.tvDevotionalMessage,
                    getString(R.string.error_play_audio));
            binding.btnPlay.setEnabled(true);
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
            binding.btnPlay.setEnabled(false);
        }
    }
}
