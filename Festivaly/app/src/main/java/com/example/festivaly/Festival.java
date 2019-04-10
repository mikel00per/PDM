package com.example.festivaly;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Festival {

   private List<Comentario> listaComentarios;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
    private DatabaseReference dbeRef;

    public List<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public List<Comentario> actualizarListaComentarios(){
        dbeRef = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() != null) {
            DatabaseReference ref = dbeRef.child("comentarios");

            ref.getRef();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //HashMap<String,Comentario> listaComentarios = dataSnapshot.getValue(new HashMap());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        return null;
    }
}
