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

import com.example.festivaly.Comentarios.Comentario;
import com.example.festivaly.Comentarios.ComentarioHolder;
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


public class UserFestivalFragment extends Fragment implements View.OnClickListener {

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

    private boolean activadaEscritura;

    private LinearLayout lyComentario;
    private LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        Bundle b = this.getArguments();

        getActivity().setTitle(Constantes.TAG_TIMELINE);

        if (b != null){
            activadaEscritura = false;
            usuarioActual = new Usuario();
            v = inflater.inflate(R.layout.fragment_festival_users,container,false);
            num_festival = b.getInt("num_festival");

            enviar = v.findViewById(R.id.enviar);
            enviar.setOnClickListener(this);

            contenidoComentario = v.findViewById(R.id.comentario);
            contenidoComentario.setOnClickListener(this);

            lyComentario = v.findViewById(R.id.LinearLayoutComentario);

            mostrarListaComentarios(v);
        }


        return v;
    }

    private void mostrarListaComentarios(View v) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        Query query = FirebaseDatabase.getInstance()
            .getReference()
            .child("festival_"+num_festival)
            .child("comentarios")
            .limitToLast(10);

        FirebaseRecyclerOptions<Comentario> options = new FirebaseRecyclerOptions.Builder<Comentario>()
            .setQuery(query,Comentario.class)
            .build();


        crearAdaptadorComentarios(options);

        // TODO: Boton de respuesta:
        // para escuachar los botones de los comentarios
        // https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
        recyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter( adapter );
    }

    private void crearAdaptadorComentarios(FirebaseRecyclerOptions<Comentario> options) {
        adapter = new FirebaseRecyclerAdapter<Comentario, ComentarioHolder>(options) {
            @Override
            public ComentarioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);

                return new ComentarioHolder(view, getContext());
            }

            @Override
            protected void onBindViewHolder(final ComentarioHolder holder, int position, final Comentario model) {
                // Bind the Chat object to the ChatHolder
                // ...

                String idUsuario = model.getUsuario();
                holder.setContenido(model.getContenido());
                holder.setImgContenido(model.getImg());
                holder.setBotonPeticionAmistad(model.getEs_peticion(),model.getId(),idUsuario);
                holder.setFecha(model.getFecha());
                holder.setIdComentarioPeticion(model.getId());
                mDataBase.child("users").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usuarioActual = dataSnapshot.getValue(Usuario.class);
                        holder.setNombe(dataSnapshot.getValue(Usuario.class).getNombre());
                        holder.setUser("@" + dataSnapshot.getValue(Usuario.class).getUsuario());
                        holder.setImgPerfil(dataSnapshot.getValue(Usuario.class).getImagenPerfil());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


        };
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
                Fragment f = new CrearComentarioFragment();
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

