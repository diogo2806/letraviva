package br.com.valenstech.letraviva.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.databinding.ActivityMainBinding;
import br.com.valenstech.letraviva.ui.login.LoginActivity;
import br.com.valenstech.letraviva.viewmodel.AutenticacaoViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration configuracaoAppBar;
    private ActivityMainBinding binding;
    private AutenticacaoViewModel autenticacaoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        autenticacaoViewModel = new ViewModelProvider(this).get(AutenticacaoViewModel.class);

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout gaveta = binding.drawerLayout;
        NavController controladorNavegacao = Navigation.findNavController(this, R.id.nav_host_fragment);
        configuracaoAppBar = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_devocional,
                R.id.nav_planos,
                R.id.nav_audios
        ).setOpenableLayout(gaveta).build();
        NavigationUI.setupActionBarWithNavController(this, controladorNavegacao, configuracaoAppBar);
        binding.navView.setupWithNavController(controladorNavegacao);
        binding.navView.setNavigationItemSelectedListener(menuItem -> tratarSelecaoMenu(menuItem, controladorNavegacao, gaveta));
    }

    private boolean tratarSelecaoMenu(@NonNull MenuItem itemMenu,
                                      @NonNull NavController controladorNavegacao,
                                      @NonNull DrawerLayout gaveta) {
        if (itemMenu.getItemId() == R.id.nav_logout) {
            autenticacaoViewModel.sair();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        boolean manipulou = NavigationUI.onNavDestinationSelected(itemMenu, controladorNavegacao);
        if (manipulou) {
            gaveta.closeDrawer(GravityCompat.START);
        }
        return manipulou;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController controladorNavegacao = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(controladorNavegacao, configuracaoAppBar) || super.onSupportNavigateUp();
    }
}
