package br.com.valenstech.letraviva.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.databinding.ActivityLoginBinding;
import br.com.valenstech.letraviva.ui.home.MainActivity;
import br.com.valenstech.letraviva.viewmodel.AutenticacaoViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AutenticacaoViewModel autenticacaoViewModel;
    private GoogleSignInClient clienteGoogle;

    private final ActivityResultLauncher<Intent> resultadoLogin =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
                if (resultado.getResultCode() != Activity.RESULT_OK) {
                    return;
                }
                Intent dados = resultado.getData();
                if (dados == null) {
                    return;
                }
                try {
                    GoogleSignInAccount conta = GoogleSignIn.getSignedInAccountFromIntent(dados)
                            .getResult(ApiException.class);
                    autenticarComGoogle(conta);
                } catch (ApiException excecao) {
                    // falha de autenticação ignorada para manter experiência
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        autenticacaoViewModel = new ViewModelProvider(this).get(AutenticacaoViewModel.class);
        autenticacaoViewModel.getUsuario().observe(this, usuario -> {
            if (usuario != null) {
                iniciarPrincipal();
            }
        });
        autenticacaoViewModel.verificarUsuario();

        GoogleSignInOptions opcoesGoogle = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        clienteGoogle = GoogleSignIn.getClient(this, opcoesGoogle);

        binding.btnGoogle.setOnClickListener(view -> {
            Intent intencaoLogin = clienteGoogle.getSignInIntent();
            resultadoLogin.launch(intencaoLogin);
        });
    }

    private void autenticarComGoogle(@Nullable GoogleSignInAccount conta) {
        if (conta == null) {
            return;
        }
        String token = conta.getIdToken();
        if (token == null) {
            return;
        }
        autenticacaoViewModel.autenticarComGoogle(token);
    }

    private void iniciarPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
