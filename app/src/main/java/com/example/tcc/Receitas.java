package com.example.tcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Receitas extends AppCompatActivity {

    private TextView textViewPendentes;
    private TextView textViewRecebidas;
    private TextView textViewTotal;
    private TabLayout tabLayoutFiltros;
    private RecyclerView recyclerViewReceitas;
    private LinearLayout layoutEmpty;
    private FloatingActionButton fabAddReceita;

    private ReceitasAdapter adapter;
    private List<Receita> listaReceitas;
    private List<Receita> listaFiltrada;

    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receitas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupRecyclerView();
        setupTabLayout();
        setupFab();
        loadReceitas();
        updateResumo();
    }

    private void initializeViews() {
        textViewPendentes = findViewById(R.id.textViewPendentes);
        textViewRecebidas = findViewById(R.id.textViewRecebidas);
        textViewTotal = findViewById(R.id.textViewTotal);
        tabLayoutFiltros = findViewById(R.id.tabLayoutFiltros);
        recyclerViewReceitas = findViewById(R.id.recyclerViewReceitas);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAddReceita = findViewById(R.id.fabAddReceita);

        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        listaReceitas = new ArrayList<>();
        listaFiltrada = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new ReceitasAdapter((com.example.tcc.List<Receita>) listaFiltrada, this::onReceitaClick, this::onReceitaLongClick);
        recyclerViewReceitas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReceitas.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayoutFiltros.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filtrarReceitas(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupFab() {
        fabAddReceita.setOnClickListener(v -> {
            Intent intent = new Intent(Receitas.this, CadastroReceitas.class);
            startActivity(intent);
        });
    }

    private void loadReceitas() {
        // Implementar carregamento das receitas do banco de dados ou SharedPreferences
        // Por enquanto, vamos criar algumas receitas de exemplo
        criarReceitasExemplo();
    }

    private void criarReceitasExemplo() {
        // Dados de exemplo - remover quando implementar persistência real
        listaReceitas.clear();
        listaReceitas.add(new Receita("01/12/2024", "Salário", "Pagamento mensal", 5000.00, "Recebida"));
        listaReceitas.add(new Receita("15/12/2024", "Freelance", "Projeto web", 1500.00, "Pendente"));
        listaReceitas.add(new Receita("20/12/2024", "Vendas", "Produto online", 800.00, "Recebida"));
        listaReceitas.add(new Receita("25/12/2024", "Consultoria", "Assessoria técnica", 2000.00, "Pendente"));

        filtrarReceitas(0); // Mostrar todas inicialmente
    }

    private void filtrarReceitas(int filtro) {
        listaFiltrada.clear();

        switch (filtro) {
            case 0: // Todas
                listaFiltrada.addAll(listaReceitas);
                break;
            case 1: // Pendentes
                for (Receita receita : listaReceitas) {
                    if ("Pendente".equals(receita.getStatus())) {
                        listaFiltrada.add(receita);
                    }
                }
                break;
            case 2: // Recebidas
                for (Receita receita : listaReceitas) {
                    if ("Recebida".equals(receita.getStatus())) {
                        listaFiltrada.add(receita);
                    }
                }
                break;
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (listaFiltrada.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            recyclerViewReceitas.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            recyclerViewReceitas.setVisibility(View.VISIBLE);
        }
    }

    private void updateResumo() {
        double totalPendentes = 0;
        double totalRecebidas = 0;

        for (Receita receita : listaReceitas) {
            if ("Pendente".equals(receita.getStatus())) {
                totalPendentes += receita.getValor();
            } else if ("Recebida".equals(receita.getStatus())) {
                totalRecebidas += receita.getValor();
            }
        }

        double total = totalPendentes + totalRecebidas;

        textViewPendentes.setText(currencyFormat.format(totalPendentes));
        textViewRecebidas.setText(currencyFormat.format(totalRecebidas));
        textViewTotal.setText(currencyFormat.format(total));
    }

    private void onReceitaClick(Receita receita) {
        // Implementar ação ao clicar na receita (ex: editar)
        Intent intent = new Intent(this, CadastroReceitas.class);
        // Passar dados da receita para edição
        startActivity(intent);
    }

    private void onReceitaLongClick(Receita receita) {
        // Implementar ação ao pressionar longo (ex: menu de opções)
        // Pode abrir um dialog com opções: Editar, Excluir, Marcar como Recebida, etc.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar dados quando voltar para a tela
        loadReceitas();
        updateResumo();
    }

    // Classe interna para representar uma Receita
    public static class Receita {
        private String data;
        private String categoria;
        private String descricao;
        private double valor;
        private String status;

        public Receita(String data, String categoria, String descricao, double valor, String status) {
            this.data = data;
            this.categoria = categoria;
            this.descricao = descricao;
            this.valor = valor;
            this.status = status;
        }

        // Getters
        public String getData() { return data; }
        public String getCategoria() { return categoria; }
        public String getDescricao() { return descricao; }
        public double getValor() { return valor; }
        public String getStatus() { return status; }

        // Setters
        public void setData(String data) { this.data = data; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public void setValor(double valor) { this.valor = valor; }
        public void setStatus(String status) { this.status = status; }
    }
}