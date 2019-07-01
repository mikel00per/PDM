package com.example.festivaly;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.festivaly.Fragments.ContactosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.festivaly.Fragments.FestivalesFragment;
import com.example.festivaly.Fragments.NotificacionesFragment;
import com.example.festivaly.Usuario.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ActividadPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
    private DatabaseReference dbeRef;

    private TextView titulo_nombre;
    private TextView correo_usuario;

    public Usuario usuarioActual;
    private ImageView imgPefil;

    BottomNavigationView nav;
    NavigationView navigationView;

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actividad_principal);
        setTitle(Constantes.TAG_FESTIVALES);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        controlarNavLateral();
        controlarNavInferior();

        usuarioActual = new Usuario();

        if(firebaseAuth.getCurrentUser() != null) {
            dbeRef = FirebaseDatabase.getInstance().getReference();
            cargarDatosUsuarioActual();
        }

        String accion;
        if (getIntent().getAction() != null){
            accion = getIntent().getAction();
            String idComentrioPeticion = getIntent().getStringExtra("idPeticion");
            String idUsuario = getIntent().getStringExtra("idUsuarioPeticion");
            Log.d("OnCreate", getIntent().getAction());
            Fragment f = null;

            switch (accion){
                case "aceptar":
                        aceptarPeticionNotificacion(idUsuario,idComentrioPeticion);
                        leerFragment(new ContactosFragment());
                    break;
                case "rechazar":
                        rechazarPeticionNotificacion(idUsuario,idComentrioPeticion);
                        leerFragment(new FestivalesFragment());
                    break;
                case "mostrar":
                        leerFragment(new NotificacionesFragment());
                        leerFragment(new NotificacionesFragment());
                    break;
            }
        }else{
            leerFragment(new FestivalesFragment());
        }



    }
    private void rechazarPeticionNotificacion(String idUsuario, String idComentrioPeticion) {
        Map<String,Object> b = new HashMap<String, Object>();
        b.put(idComentrioPeticion, null);

        Log.d("eliminarAntiguPeticion",idComentrioPeticion);
        mDatabase
                .child("peticiones")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("nuevas")
                .updateChildren(b);

        b = new HashMap<String, Object>();
        b.put(idComentrioPeticion, null);
        mDatabase
                .child("peticiones")
                .child(idUsuario)
                .child("from_me")
                .updateChildren(b);

        b = new HashMap<String, Object>();
        b.put(idComentrioPeticion, null);
        mDatabase
                .child("peticiones")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("to_me")
                .updateChildren(b);
    }

    private void aceptarPeticionNotificacion(String idUsuario, String idComentrioPeticion) {
        Map<String,Boolean> b = new HashMap<String, Boolean>();
        b.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
        mDatabase
                .child("users")
                .child(idUsuario)
                .child("contactos")
                .setValue(b);

        b = new HashMap<String, Boolean>();
        b.put(idUsuario,true);

        mDatabase
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("contactos")
                .setValue(b);

        rechazarPeticionNotificacion(idUsuario,idComentrioPeticion);
    }
    private void controlarNavLateral() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        titulo_nombre = navigationView.getHeaderView(0).findViewById(R.id.titulo_nombre);
        correo_usuario = navigationView.getHeaderView(0).findViewById(R.id.correo_usuario);

    }

    private void controlarNavInferior() {
        nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment f = null;
                Log.d("OnNavItemSelect", String.valueOf(menuItem.getItemId()));
                Log.d("OnNavItemSelect", String.valueOf(R.id.navigation_home));

                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        f = new FestivalesFragment();
                        break;
                    case R.id.navigation_notifications:
                        f = new NotificacionesFragment();
                        break;
                    case R.id.navigation_contactos:
                        f = new ContactosFragment();
                        break;
                }

                return leerFragment(f);
            }
        });

    }

    private void cargarDatosUsuarioActual() {
        DatabaseReference ref = dbeRef.child("users").child(firebaseAuth.getCurrentUser().getUid());

        ref.getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioActual = dataSnapshot.getValue(Usuario.class);
                titulo_nombre.setText(dataSnapshot.getValue(Usuario.class).getNombre());
                correo_usuario.setText(dataSnapshot.getValue(Usuario.class).getCorreo());

                imgPefil = navigationView.getHeaderView(0).findViewById(R.id.imgPerfil);

                Picasso
                    .get()
                    .load(dataSnapshot.getValue(Usuario.class).getImagenPerfil())
                    .transform(new CropCircleTransformation())
                    .into(imgPefil);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean leerFragment(Fragment fragment){
        if (fragment != null){
            Log.d("leerFragment", "Entro");
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragmentos, fragment).commit();
            return true;
        }else
            return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actividad_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), ActividadRegistroLogin.class));

                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
