package com.rtstudio.projetomeuapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.rtstudio.projetomeuapp.fragment.TelaInicialFragment;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.util.Utilitaria;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Utilitaria mUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNavigationDrawer();

        mUtil = new Utilitaria(this);

        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.main_activity_fragment_area, new TelaInicialFragment())
                .commit();
    }

    public void setNavigationDrawer() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white));

        mNavigationView = findViewById(R.id.main_activity_navigationView);

        mDrawerLayout = findViewById(R.id.main_activity_drawer);

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                mDrawerLayout.closeDrawer(mNavigationView);
                if (id == R.id.drawer_ajuda) {
                    mUtil.menuItemAjuda();
                } else if (id == R.id.drawer_sync) {
//                    getSupportFragmentManager().findFragmentByTag("");
//                    if (mRepositorio.sicronizar(ordemServicoList)) {
//                        atualizaRecyclerView(ordemServicoList);
//                    }
                } else if (id == R.id.drawer_sair) {
                    alerta();
                }
                return false;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if ((getSupportFragmentManager().findFragmentByTag("CADASTRAR")) != null &&
                (getSupportFragmentManager().findFragmentByTag("CADASTRAR").isVisible())) {

            getSupportFragmentManager().popBackStack();

        } else if ((getSupportFragmentManager().findFragmentByTag("EDITAR")) != null &&
                (getSupportFragmentManager().findFragmentByTag("EDITAR").isVisible())) {

            getSupportFragmentManager().popBackStack();

        } else if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
        } else if (!mDrawerLayout.isDrawerOpen(mNavigationView)) {
            alerta();
        }
    }

    public void alerta() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Sair")
                .setMessage("Deseja realmente sair do app?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }
}
