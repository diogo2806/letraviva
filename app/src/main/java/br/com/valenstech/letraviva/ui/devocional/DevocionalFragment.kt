package br.com.valenstech.letraviva.ui.devocional

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.databinding.FragmentDevocionalBinding
import br.com.valenstech.letraviva.model.DevotionalContent
import br.com.valenstech.letraviva.util.UiState
import br.com.valenstech.letraviva.util.ValidacaoConteudo
import br.com.valenstech.letraviva.util.definirTextoSeguro
import br.com.valenstech.letraviva.viewmodel.DevocionalViewModel
import java.io.IOException

class DevocionalFragment : Fragment() {

    private var _binding: FragmentDevocionalBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var viewModel: DevocionalViewModel
    private var currentContent: DevotionalContent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDevocionalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DevocionalViewModel::class.java]
        binding.btnPlay.isEnabled = false
        binding.btnPlay.setOnClickListener {
            currentContent?.let { content ->
                if (!content.audioUrl.isNullOrBlank()) {
                    playAudio(content)
                }
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoadingState()
                is UiState.Success -> showDevotional(state.data)
                is UiState.Empty -> showMessage(state.message)
                is UiState.Error -> showMessage(state.message)
            }
        }
        viewModel.loadDevotional()
    }

    private fun showLoadingState() {
        binding.progressDevotional.isVisible = true
        binding.tvDevotionalMessage.apply {
            definirTextoSeguro(getString(R.string.loading_devotional))
            isVisible = true
        }
        binding.contentDevotional.isVisible = false
        binding.btnPlay.isVisible = false
        binding.btnPlay.isEnabled = false
    }

    private fun showDevotional(content: DevotionalContent) {
        currentContent = content
        binding.progressDevotional.isVisible = false
        binding.tvDevotionalMessage.isVisible = false
        binding.contentDevotional.isVisible = true
        binding.tvDevotionalTitle.definirTextoSeguro(content.title)
        binding.tvDevotionalTitle.isVisible = content.title.isNotBlank()
        binding.tvDevotional.definirTextoSeguro(content.text)
        val hasAudio = !content.audioUrl.isNullOrBlank()
        binding.btnPlay.isVisible = hasAudio
        binding.btnPlay.isEnabled = hasAudio
    }

    private fun showMessage(message: String) {
        currentContent = null
        binding.progressDevotional.isVisible = false
        binding.contentDevotional.isVisible = false
        binding.btnPlay.isVisible = false
        binding.btnPlay.isEnabled = false
        binding.tvDevotionalMessage.apply {
            definirTextoSeguro(message)
            isVisible = true
        }
    }

    private fun playAudio(content: DevotionalContent) {
        val audioUrl = content.audioUrl ?: return
        binding.btnPlay.isEnabled = false
        binding.tvDevotionalMessage.apply {
            val tituloSeguro = ValidacaoConteudo.aplicarEscapeSeguro(
                content.title.ifBlank { getString(R.string.devotional_default_title) },
            )
            definirTextoSeguro(getString(R.string.preparing_audio, tituloSeguro))
            isVisible = true
        }
        releasePlayer()
        val player = MediaPlayer()
        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build(),
        )
        mediaPlayer = player
        try {
            player.setDataSource(audioUrl)
            player.setOnPreparedListener {
                val tituloSeguro = ValidacaoConteudo.aplicarEscapeSeguro(
                    content.title.ifBlank { getString(R.string.devotional_default_title) },
                )
                binding.tvDevotionalMessage.definirTextoSeguro(
                    getString(R.string.now_playing, tituloSeguro),
                )
                binding.btnPlay.isEnabled = true
                it.start()
            }
            player.setOnCompletionListener {
                val tituloSeguro = ValidacaoConteudo.aplicarEscapeSeguro(
                    content.title.ifBlank { getString(R.string.devotional_default_title) },
                )
                binding.tvDevotionalMessage.definirTextoSeguro(
                    getString(R.string.audio_finished, tituloSeguro),
                )
                binding.btnPlay.isEnabled = true
            }
            player.setOnErrorListener { mp, _, _ ->
                binding.tvDevotionalMessage.definirTextoSeguro(getString(R.string.error_play_audio))
                binding.btnPlay.isEnabled = true
                mp.reset()
                mp.release()
                mediaPlayer = null
                true
            }
            player.prepareAsync()
        } catch (exception: IOException) {
            binding.tvDevotionalMessage.definirTextoSeguro(getString(R.string.error_play_audio))
            binding.btnPlay.isEnabled = true
            player.reset()
            player.release()
            mediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
        _binding = null
    }

    private fun releasePlayer() {
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
