package com.example.tcc;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CadastroReceitas extends AppCompatActivity  {

    private TextInputEditText editTextCategoria;
    private TextInputEditText editTextDescricao;
    private TextInputEditText editTextValor;
    private TextInputEditText editTextData;
    private RadioGroup radioGroupStatus;
    private Button buttonCadastrar;
    private Button buttonCancelar;

    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_receitas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupDatePicker();
        setupButtons();
        setCurrentDate();
    }

    private void initializeViews() {
        editTextCategoria = findViewById(R.id.editTextCategoria);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextValor = findViewById(R.id.editTextValor);
        editTextData = findViewById(R.id.editTextData);
        radioGroupStatus = findViewById(R.id.radioGroupStatus);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        buttonCancelar = findViewById(R.id.buttonCancelar);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar = Calendar.getInstance();
    }

    private void setupDatePicker() {
        editTextData.setOnClickListener(v -> showDatePicker());

        // Também configurar o ícone do TextInputLayout
        TextInputLayout layoutData = findViewById(R.id.layoutData);
        layoutData.setEndIconOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    editTextData.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setCurrentDate() {
        editTextData.setText(dateFormat.format(new Date()));
    }

    private void setupButtons() {
        buttonCadastrar.setOnClickListener(v -> cadastrarReceita());
        buttonCancelar.setOnClickListener(v -> finish());
    }

    private void cadastrarReceita() {
        if (!validarCampos()) {
            return;
        }

        String categoria = editTextCategoria.getText().toString().trim();
        String descricao = editTextDescricao.getText().toString().trim();
        String valorStr = editTextValor.getText().toString().trim();
        String data = editTextData.getText().toString().trim();

        int selectedRadioButtonId = radioGroupStatus.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String status = selectedRadioButton.getText().toString();

        // Converter e validar valor
        double valor;
        try {
            valor = Double.parseDouble(valorStr.replace(",", "."));
            if (valor <= 0) {
                showError("Por favor, insira um valor válido maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Por favor, insira um valor numérico válido.");
            return;
        }

        // Validar data
        try {
            Date dataReceita = dateFormat.parse(data);
            if (dataReceita == null) {
                showError("Por favor, selecione uma data válida.");
                return;
            }
        } catch (ParseException e) {
            showError("Formato de data inválido.");
            return;
        }

        // Aqui você implementaria a lógica para salvar a receita
        // Por exemplo, salvar em banco de dados, SharedPreferences, etc.
        salvarReceita(categoria, descricao, valor, data, status);
    }

    private boolean validarCampos() {
        boolean isValid = true;

        // Validar categoria
        if (TextUtils.isEmpty(editTextCategoria.getText().toString().trim())) {
            TextInputLayout layoutCategoria = findViewById(R.id.layoutCategoria);
            layoutCategoria.setError("Campo obrigatório");
            isValid = false;
        } else {
            TextInputLayout layoutCategoria = findViewById(R.id.layoutCategoria);
            layoutCategoria.setError(null);
        }

        // Validar descrição
        if (TextUtils.isEmpty(editTextDescricao.getText().toString().trim())) {
            TextInputLayout layoutDescricao = findViewById(R.id.layoutDescricao);
            layoutDescricao.setError("Campo obrigatório");
            isValid = false;
        } else {
            TextInputLayout layoutDescricao = findViewById(R.id.layoutDescricao);
            layoutDescricao.setError(null);
        }

        // Validar valor
        if (TextUtils.isEmpty(editTextValor.getText().toString().trim())) {
            TextInputLayout layoutValor = findViewById(R.id.layoutValor);
            layoutValor.setError("Campo obrigatório");
            isValid = false;
        } else {
            TextInputLayout layoutValor = findViewById(R.id.layoutValor);
            layoutValor.setError(null);
        }

        // Validar data
        if (TextUtils.isEmpty(editTextData.getText().toString().trim())) {
            TextInputLayout layoutData = findViewById(R.id.layoutData);
            layoutData.setError("Campo obrigatório");
            isValid = false;
        } else {
            TextInputLayout layoutData = findViewById(R.id.layoutData);
            layoutData.setError(null);
        }

        return isValid;
    }

    private void salvarReceita(String categoria, String descricao, double valor, String data, String status) {
        // Implementar a lógica de salvamento aqui
        // Por exemplo:
        // - Salvar em banco de dados SQLite
        // - Salvar em SharedPreferences
        // - Enviar para servidor

        // Por enquanto, apenas exibir mensagem de sucesso
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = currencyFormat.format(valor);

        String mensagem = String.format(
                "Receita cadastrada com sucesso!\n\n" +
                        "Categoria: %s\n" +
                        "Descrição: %s\n" +
                        "Valor: %s\n" +
                        "Data: %s\n" +
                        "Status: %s",
                categoria, descricao, valorFormatado, data, status
        );

        showSuccess(mensagem);
        limparCampos();
    }

    private void limparCampos() {
        editTextCategoria.setText("");
        editTextDescricao.setText("");
        editTextValor.setText("");
        setCurrentDate();
        radioGroupStatus.check(R.id.radioButtonPendente);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
