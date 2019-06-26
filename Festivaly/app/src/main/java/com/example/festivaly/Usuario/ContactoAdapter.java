package com.example.festivaly.Usuario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.festivaly.Festivales.Festival;
import com.example.festivaly.Festivales.FestivalAdapter;
import com.example.festivaly.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>{

    private OnItemClickListener mlistener;

    private Context context;
    private ArrayList<String> usersAmigos;
    private DatabaseReference mDataBase;
    private Usuario contacto;

    public String getName() {
        return contacto.getNombre();
    }

    public String getPhone() {
        return contacto.getDesripcion();
    }

    public String getEmail() {
        return contacto.getCorreo();
    }

    public Usuario getUsuario(){
        return contacto;
    }

    public interface OnItemClickListener {
        void onClickPerfil(int pos);
        void onClickBorrar(int pos);
    }

    public ContactoAdapter(Context context, ArrayList<String> usersAmigos) {
        this.context = context;
        this.usersAmigos = usersAmigos;
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Log.d("adapter","Creado");
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    @Override
    public ContactoAdapter.ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactoAdapter.ContactoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false), mlistener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ContactoAdapter.ContactoViewHolder holder, final int position) {
        Log.d("onBindViewHolder", usersAmigos.get(position));
        mDataBase
                .child("users").
                child(usersAmigos.get(position))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        contacto = dataSnapshot.getValue(Usuario.class);
                        holder.nombreContacto.setText(contacto.getNombre());
                        holder.userContato.setText(contacto.getUsuario());
                        Picasso.get()
                                .load(contacto.getImagenPerfil())
                                .transform(new CropCircleTransformation())
                                .into(holder.imgPerfilContacto);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return usersAmigos.size();
    }

    class ContactoViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgPerfilContacto;
        public TextView nombreContacto,userContato;
        public Button ver_perfil, eliminar_amigo;

        public ContactoViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgPerfilContacto = itemView.findViewById(R.id.imgPerfilContacto);
            nombreContacto = itemView.findViewById(R.id.nombreContacto);
            userContato = itemView.findViewById(R.id.usuarioContacto);
            ver_perfil = itemView.findViewById(R.id.botonVerUsuario);
            eliminar_amigo = itemView.findViewById(R.id.botonEliminarContacto);

            ver_perfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickPerfil(getAdapterPosition());
                }
            });

            eliminar_amigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickBorrar(getAdapterPosition());
                }
            });


        }
    }
}
