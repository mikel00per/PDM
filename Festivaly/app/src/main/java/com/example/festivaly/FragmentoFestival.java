package com.example.festivaly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class FragmentoFestival extends Fragment implements View.OnClickListener {

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = null;
            Bundle b = this.getArguments();

            if (b != null){
                activadaEscritura = false;
                usuarioActual = new Usuario();
                v = inflater.inflate(R.layout.festival,container,false);
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
                        .child("festival_1")
                        .limitToLast(10);

                FirebaseRecyclerOptions<Comentario> options =
                        new FirebaseRecyclerOptions.Builder<Comentario>()
                                .setQuery(query,Comentario.class)
                                .build();


                adapter = new FirebaseRecyclerAdapter<Comentario, ComentarioHolder>(options) {
                    @Override
                    public ComentarioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.comentario, parent, false);

                        return new ComentarioHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(final ComentarioHolder holder, int position, final Comentario model) {
                        // Bind the Chat object to the ChatHolder
                        // ...
                        holder.setContenido(model.getContenido());
                        String id = model.getUsuario();


                        mDataBase.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usuarioActual = dataSnapshot.getValue(Usuario.class);
                                holder.setNombe(dataSnapshot.getValue(Usuario.class).getNombre());
                                holder.setUser("@" + dataSnapshot.getValue(Usuario.class).getUsuario());
                                holder.setFecha(model.getFecha());
                                holder.setImgPerfil(dataSnapshot.getValue(Usuario.class).getImagenPerfil());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                };

                // TODO: Boton de respuesta:
                // para escuachar los botones de los comentarios
                // https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
                recyclerView = v.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter( adapter );

            }


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
                if (!activadaEscritura){
                    contenidoComentario.setVisibility(View.VISIBLE);
                    activadaEscritura = true;
                }else{
                    activadaEscritura = false;
                    contenidoComentario.setVisibility(View.GONE);

                    if (!contenidoComentario.getText().toString().isEmpty()){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                        Comentario nuevo_comentario = new Comentario(
                                simpleDateFormat.format(new Date()).concat(" - " + UUID.randomUUID().toString()),
                                firebaseAuth.getCurrentUser().getUid(),
                                contenidoComentario.getText().toString(),
                                simpleDateFormat.format(new Date()),
                                new HashMap<String, String>()
                        );

                        mDataBase
                                .child("festival_" + num_festival)
                                .child(nuevo_comentario.getId())
                                .setValue(nuevo_comentario).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                        contenidoComentario.setText("");
                    }else{
                        Toast.makeText(getContext(),"Escribe algo!",Toast.LENGTH_SHORT).show();
                    }
                }



            break;

        }

    }
}

