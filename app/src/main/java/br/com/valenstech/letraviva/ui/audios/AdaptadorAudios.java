package br.com.valenstech.letraviva.ui.audios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.valenstech.letraviva.model.ItemAudio;
import br.com.valenstech.letraviva.util.ExtensoesTextView;

public class AdaptadorAudios extends RecyclerView.Adapter<AdaptadorAudios.AudiosViewHolder> {

    public interface AoSelecionarAudio {
        void selecionar(@NonNull ItemAudio itemAudio);
    }

    private final List<ItemAudio> itens;
    private final AoSelecionarAudio callbackSelecao;

    public AdaptadorAudios(@NonNull AoSelecionarAudio callbackSelecao) {
        this.itens = new ArrayList<>();
        this.callbackSelecao = callbackSelecao;
    }

    public void atualizarItens(@NonNull List<ItemAudio> novosItens) {
        itens.clear();
        itens.addAll(novosItens);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AudiosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new AudiosViewHolder(view, callbackSelecao);
    }

    @Override
    public void onBindViewHolder(@NonNull AudiosViewHolder holder, int position) {
        holder.vincular(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class AudiosViewHolder extends RecyclerView.ViewHolder {
        private final TextView textoAudio;
        private ItemAudio itemAtual;

        AudiosViewHolder(@NonNull View itemView, @NonNull AoSelecionarAudio callbackSelecao) {
            super(itemView);
            textoAudio = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(v -> {
                if (itemAtual != null) {
                    callbackSelecao.selecionar(itemAtual);
                }
            });
        }

        void vincular(@NonNull ItemAudio itemAudio) {
            itemAtual = itemAudio;
            ExtensoesTextView.definirTextoSeguro(textoAudio, itemAudio.getTitulo());
        }
    }
}
