package br.com.valenstech.letraviva.ui.devocional

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.databinding.FragmentDevocionalBinding

class DevocionalFragment : Fragment() {

    private var _binding: FragmentDevocionalBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDevocionalBinding.inflate(inflater, container, false)
        binding.btnPlay.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sample_audio)
            }
            mediaPlayer?.start()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
