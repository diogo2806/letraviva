package br.com.valenstech.letraviva.ui.audios

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.databinding.FragmentAudiosBinding
import br.com.valenstech.letraviva.model.AudioItem

class AudiosFragment : Fragment() {

    private var _binding: FragmentAudiosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudiosBinding.inflate(inflater, container, false)
        val audios = listOf(
            AudioItem("Audio 1", R.raw.sample_audio),
            AudioItem("Audio 2", R.raw.sample_audio)
        )
        binding.recyclerAudios.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAudios.adapter = AudiosAdapter(audios)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class AudiosAdapter(private val items: List<AudioItem>) : RecyclerView.Adapter<AudiosAdapter.AudioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class AudioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(android.R.id.text1)
        private var player: MediaPlayer? = null
        init {
            view.setOnClickListener {
                val item = items[bindingAdapterPosition]
                player?.release()
                player = MediaPlayer.create(view.context, item.res)
                player?.start()
            }
        }
        fun bind(item: AudioItem) {
            text.text = item.title
        }
    }
}
