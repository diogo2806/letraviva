package br.com.valenstech.letraviva.ui.planos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.valenstech.letraviva.model.PlanoLeitura;
import br.com.valenstech.letraviva.util.ExtensoesTextView;

public class AdaptadorPlanos extends RecyclerView.Adapter<AdaptadorPlanos.PlanosViewHolder> {

    private final List<PlanoLeitura> planos;

    public AdaptadorPlanos(@NonNull List<PlanoLeitura> planosIniciais) {
        this.planos = new ArrayList<>(planosIniciais);
    }

    @NonNull
    @Override
    public PlanosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PlanosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanosViewHolder holder, int position) {
        holder.vincular(planos.get(position));
    }

    @Override
    public int getItemCount() {
        return planos.size();
    }

    public void atualizarPlanos(@NonNull List<PlanoLeitura> novosPlanos) {
        planos.clear();
        planos.addAll(novosPlanos);
        notifyDataSetChanged();
    }

    static class PlanosViewHolder extends RecyclerView.ViewHolder {
        private final TextView textoPlano;

        PlanosViewHolder(@NonNull View itemView) {
            super(itemView);
            textoPlano = itemView.findViewById(android.R.id.text1);
        }

        void vincular(@NonNull PlanoLeitura plano) {
            ExtensoesTextView.definirTextoSeguro(textoPlano, plano.getTitulo());
        }
    }
}
