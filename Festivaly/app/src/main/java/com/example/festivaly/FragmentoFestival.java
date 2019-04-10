package com.example.festivaly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = null;
            Bundle b = this.getArguments();

            if (b != null){
                v = inflater.inflate(R.layout.festivales2,container,false);
                num_festival = b.getInt("num_festival");

                enviar = v.findViewById(R.id.enviar);
                enviar.setOnClickListener(this);

                contenidoComentario = v.findViewById(R.id.comentario);
                contenidoComentario.setOnClickListener(this);

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
                    protected void onBindViewHolder(ComentarioHolder holder, int position, Comentario model) {
                        // Bind the Chat object to the ChatHolder
                        // ...
                        holder.setContenido(model.getContenido());
                    }
                };

                recyclerView = v.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setHasFixedSize(true);
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
                if (!contenidoComentario.getText().toString().isEmpty()){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                    Comentario nuevo_comentario = new Comentario(
                            UUID.randomUUID().toString(),
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


                }else{

                }
            break;

        }

    }
}

