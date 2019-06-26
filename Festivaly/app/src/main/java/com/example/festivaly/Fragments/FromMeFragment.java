package com.example.festivaly.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.festivaly.Constantes;
import com.example.festivaly.Peticion.Peticion;
import com.example.festivaly.Peticion.PeticionHolder;
import com.example.festivaly.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class FromMeFragment extends Fragment {


    private FirebaseAuth firebaseAuth;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference mDataBase;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;

        v = inflater.inflate(R.layout.fragment_list_peticiones, container, false);
        getActivity().setTitle(Constantes.TAG_NOTIFICACIONES);

        mostrarListaPeticionesToMe(v);


        // Inflate the layout for this fragment
        return v;
    }


    @SuppressLint("RestrictedApi")
    void mostrarListaPeticionesToMe(View v){
        mDataBase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("peticiones")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("from_me")
                .limitToLast(10);

        FirebaseRecyclerOptions<Peticion> options = new FirebaseRecyclerOptions.Builder<Peticion>()
                .setQuery(query,Peticion.class)
                .build();


        crearAdaptadorPeticiones(options);


        // TODO: Boton de respuesta:
        // para escuachar los botones de los comentarios
        // https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
        recyclerView = v.findViewById(R.id.list_peticiones);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter( adapter );

    }

    private void crearAdaptadorPeticiones(FirebaseRecyclerOptions<Peticion> options) {
        adapter = new FirebaseRecyclerAdapter<Peticion, PeticionHolder>(options) {
            @Override
            public PeticionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peticion, parent, false);

                return new PeticionHolder(view, getContext());
            }

            @Override
            protected void onBindViewHolder(final PeticionHolder holder, int position, final Peticion model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setTo_me(false,model.getTo());
                holder.setIdComentrioPeticion(model.getId());
                holder.setElementosUsuario(model.getTo());
                holder.setContenidoPeticion(model.getIdComentarioPeticion());

                Log.d("onBindViewHolder","entro");
            }
        };
        Log.d("adaptador", "creado");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}
