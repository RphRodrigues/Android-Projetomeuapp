package com.rtstudio.projetomeuapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.fragment.CadastrarFragment;
import com.rtstudio.projetomeuapp.fragment.TelaInicialFragment;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

public class TelaInicialActivity extends AppCompatActivity {

    public static final String TEMA_PADRAO = "temaPadrao";
    public static final String TEMA_NOTURNO = "temaNoturno";

    private Utilitaria util;
    private RelativeLayout relativeLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Repositorio mRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        setNavigationDrawer();

        mRepositorio = new Repositorio(this);

        util = new Utilitaria(this);

        relativeLayout = findViewById(R.id.telaInicial_relative_layout);

        //Recupera as O.S salvas em arquivo e carrega no recyclerView

        //adjustViewBound

        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.activity_tela_inicial_fragment_area, new TelaInicialFragment())
                .commit();

    }

    public void setNavigationDrawer() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white));

        final NavigationView navigationView = findViewById(R.id.telaInicial_navigationView);

        mDrawerLayout = findViewById(R.id.telaInicial_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.isDrawerIndicatorEnabled();

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(navigationView);
            }
        });
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                mDrawerLayout.closeDrawer(navigationView);
                if (id == R.id.drawer_ajuda) {
                    util.menuItemAjuda();
                } else if (id == R.id.drawer_sync) {
//                    getSupportFragmentManager().findFragmentByTag("");
//                    if (mRepositorio.sicronizar(ordemServicoList)) {
//                        atualizaRecyclerView(ordemServicoList);
//                    }
                } else if (id == R.id.drawer_sair) {
                    onBackPressed();
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(TelaInicialActivity.this)
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