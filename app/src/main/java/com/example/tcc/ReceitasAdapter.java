package com.example.tcc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Locale;

public class ReceitasAdapter extends RecyclerView.Adapter<ReceitasAdapter.ReceitaViewHolder> {

    private List<Receitas.Receita> receitas;
    private OnReceitaClickListener clickListener;
    private OnReceitaLongClickListener longClickListener;
    private NumberFormat currencyFormat;



    public interface OnReceitaClickListener {
        void onReceitaClick(Receitas.Receita receita);
    }

    public interface OnReceitaLongClickListener {
        void onReceitaLongClick(Receitas.Receita receita);
    }

    public ReceitasAdapter(List<Receitas.Receita> receitas,
                           OnReceitaClickListener clickListener,
                           OnReceitaLongClickListener longClickListener) {
        this.receitas = receitas;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }

    @NonNull
    @Override
    public ReceitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receita, parent, false);
        return new ReceitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceitaViewHolder holder, int position) {
        Receitas.Receita receita = receitas.get(position);
        holder.bind(receita);
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }

    public class ReceitaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewData;
        private TextView textViewCategoria;
        private TextView textViewDescricao;
        private TextView textViewValor;
        private TextView textViewStatus;
        private View statusIndicator;

        public ReceitaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewValor = itemView.findViewById(R.id.textViewValor);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onReceitaClick(receitas.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        longClickListener.onReceitaLongClick(receitas.get(position));
                        return true;
                    }
                }
                return false;
            });
        }

        public void bind(Receitas.Receita receita) {
            textViewData.setText(receita.getData());
            textViewCategoria.setText(receita.getCategoria());
            textViewDescricao.setText(receita.getDescricao());
            textViewValor.setText(currencyFormat.format(receita.getValor()));
            textViewStatus.setText(receita.getStatus());

            // Configurar cor do status
            if ("Recebida".equals(receita.getStatus())) {
                textViewStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
                statusIndicator.setBackgroundColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
            } else {
                textViewStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_orange_dark));
                statusIndicator.setBackgroundColor(itemView.getContext().getColor(android.R.color.holo_orange_dark));
            }
        }
    }
}
