package br.com.valenstech.letraviva.ui.audios

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.databinding.FragmentAudiosBinding
import br.com.valenstech.letraviva.model.AudioItem
import br.com.valenstech.letraviva.util.UiState
import br.com.valenstech.letraviva.viewmodel.AudiosViewModel
import java.io.IOException

class AudiosFragment : Fragment() {

    private var _binding: FragmentAudiosBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AudiosViewModel
    private lateinit var adapter: AudiosAdapter
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAudiosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AudiosAdapter(emptyList()) { item -> playAudio(item) }
        binding.recyclerAudios.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAudios.adapter = adapter

        viewModel = ViewModelProvider(this)[AudiosViewModel::class.java]
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoadingState()
                is UiState.Success -> showAudios(state.data)
                is UiState.Empty -> showMessage(state.message)
                is UiState.Error -> showMessage(state.message)
            }
        }
        viewModel.loadAudios()
    }

    private fun showLoadingState() {
        binding.progressAudios.isVisible = true
        binding.recyclerAudios.isVisible = false
        binding.tvAudiosMessage.apply {
            text = getString(R.string.loading_audios)
            isVisible = true
        }
        binding.tvNowPlaying.isVisible = false
    }

    private fun showAudios(audios: List<AudioItem>) {
        binding.progressAudios.isVisible = false
        binding.tvAudiosMessage.isVisible = false
        binding.recyclerAudios.isVisible = true
        adapter.updateItems(audios)
        binding.tvNowPlaying.isVisible = mediaPlayer != null
    }

    private fun showMessage(message: String) {
        binding.progressAudios.isVisible = false
        binding.recyclerAudios.isVisible = false
        binding.tvAudiosMessage.apply {
            text = message
            isVisible = true
        }
        binding.tvNowPlaying.isVisible = false
    }

    private fun playAudio(item: AudioItem) {
        binding.tvNowPlaying.apply {
            isVisible = true
            text = getString(R.string.preparing_audio, item.title)
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
            player.setDataSource(item.streamUrl)
            player.setOnPreparedListener {
                binding.tvNowPlaying.text = getString(R.string.now_playing, item.title)
                it.start()
            }
            player.setOnCompletionListener {
                binding.tvNowPlaying.text = getString(R.string.audio_finished, item.title)
            }
            player.setOnErrorListener { mp, _, _ ->
                binding.tvNowPlaying.text = getString(R.string.error_play_audio)
                mp.reset()
                mp.release()
                mediaPlayer = null
                true
            }
            player.prepareAsync()
        } catch (exception: IOException) {
            binding.tvNowPlaying.text = getString(R.string.error_play_audio)
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
        if (_binding != null) {
            binding.tvNowPlaying.isVisible = false
        }
    }
}

class AudiosAdapter(
    private var items: List<AudioItem>,
    private val onItemClick: (AudioItem) -> Unit,
) : RecyclerView.Adapter<AudiosAdapter.AudioViewHolder>() {

    fun updateItems(newItems: List<AudioItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class AudioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(android.R.id.text1)

        fun bind(item: AudioItem) {
            text.text = item.title
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}
