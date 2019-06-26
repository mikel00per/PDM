package com.example.festivaly.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.festivaly.Anuncio.Anuncio;
import com.example.festivaly.Anuncio.AnuncioHolder;
import com.example.festivaly.Constantes;
import com.example.festivaly.R;
import com.example.festivaly.Usuario.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminFestivalFragment extends Fragment implements View.OnClickListener {

    int num_festival;

    //Atributos para controlar el mensaje publico a enviar.
    EditText contenidoComentario;
    Button enviar;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    private DatabaseReference mDataBase;
    private FirebaseAuth firebaseAuth;

    private Usuario usuarioActual;
    private String idAdmin;

    private boolean activadaEscritura;

    private LinearLayout lyComentario;
    private LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        Bundle b = this.getArguments();
        getActivity().setTitle(Constantes.TAG_ANUNCIO);

        if (b != null){
            activadaEscritura = false;
            usuarioActual = new Usuario();
            v = inflater.inflate(R.layout.fragment_festival_admin,container,false);
            num_festival = b.getInt("num_festival");

            enviar = v.findViewById(R.id.enviar);
            enviar.setOnClickListener(this);

            contenidoComentario = v.findViewById(R.id.comentario);
            contenidoComentario.setOnClickListener(this);

            lyComentario = v.findViewById(R.id.LinearLayoutComentario);

            mDataBase = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();

            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("festival_"+num_festival)
                    .child("anuncios")
                    .limitToLast(10);

            FirebaseRecyclerOptions<Anuncio> options =
                    new FirebaseRecyclerOptions.Builder<Anuncio>()
                            .setQuery(query,Anuncio.class)
                            .build();


            adapter = new FirebaseRecyclerAdapter<Anuncio, AnuncioHolder>(options) {
                @Override
                public AnuncioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_anuncio_admin, parent, false);

                    return new AnuncioHolder(view);
                }

                @Override
                protected void onBindViewHolder(final AnuncioHolder holder, int position, final Anuncio model) {
                    // Bind the Chat object to the ChatHolder
                    // ...
                    holder.setTitulo(model.getTitulo());
                    holder.setFecha(model.getFecha());
                    holder.setContenido(model.getContenido());
                    holder.setImgContenido(model.getImg());
                    String idAdmin = model.getAdmin();

                    mDataBase.child("users").child(idAdmin).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            holder.setImgAdmin(dataSnapshot.getValue(Usuario.class).getImagenPerfil());
                            if (dataSnapshot.getValue(Usuario.class).getId()!= FirebaseAuth.getInstance().getCurrentUser().getUid()){
                                enviar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            };


            // para escuachar los busersotones de los comentarios
            // https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
            recyclerView = v.findViewById(R.id.lista_anuncios);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter( adapter );

        }

        // TODO: Ocultar escribir si no soy el admin

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enviar:
                Fragment f = new CrearAnuncioFragment();
                Bundle b = new Bundle();
                b.putInt("num_festival",num_festival);
                f.setArguments(b);
                leerFragment(f);
            break;
        }
    }
    public void leerFragment(Fragment fragment){
        if (fragment != null){
            getFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_fragmentos, fragment)
                    .addToBackStack("fragment_festival_users")
                    .commit();
        }else{

        }
    }
}

