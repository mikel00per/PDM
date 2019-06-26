package com.example.festivaly.Comentarios;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.festivaly.Peticion.Peticion;
import com.example.festivaly.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashMap;
import java.util.Map;

public class ComentarioHolder extends RecyclerView.ViewHolder {

    public LinearLayout root;
    public Context context;
    public ImageView imgPerfil, imgContenido;
    public Button botonFavorito, botonPeticionAmistad;
    public TextView contenido, fechaComentario, nombreComentario, userComentario;
    private FirebaseFunctions mFunctions;


    public String idComentarioPeticion;

    private DatabaseReference mDataBase;

    public ComentarioHolder(View itemView, Context context) {
        super(itemView);
        root = itemView.findViewById(R.id.comentario);
        imgPerfil = itemView.findViewById(R.id.imgPerfilComentario);
        botonFavorito = itemView.findViewById(R.id.botonFavorito);
        botonPeticionAmistad = itemView.findViewById(R.id.botonPetecionAmistad);
        imgContenido = itemView.findViewById(R.id.imagenComentario);
        contenido = itemView.findViewById(R.id.contenidoComentario);
        fechaComentario = itemView.findViewById(R.id.fechaComentario);
        nombreComentario = itemView.findViewById(R.id.nombreComentario);
        userComentario = itemView.findViewById(R.id.usuarioComentario);
        this.context = context;
    }

    public void setIdComentarioPeticion(String id){ this.idComentarioPeticion = id;}

    public void setNombe(String name){
        nombreComentario.setText(name);
    }

    public void setUser(String username){
        userComentario.setText(username);
    }

    public void setFecha(String fecha){
        fechaComentario.setText(fecha);
    }


    public void setImgPerfil(String img){
        Picasso
                .get()
                .load(img)
                .transform(new CropCircleTransformation())
                .into(imgPerfil);

    }

    public void setContenido(String contenido){
        this.contenido.setText(contenido);
    }

    public void setImgContenido(String url){
        if (!url.isEmpty()){
            imgContenido.setVisibility(View.VISIBLE);
            Picasso
                    .get()
                    .load(url)
                    .into(imgContenido);
        }
    }

    public String getFechaComentario(){
        if (!fechaComentario.getText().toString().isEmpty())
            return fechaComentario.getText().toString();
        else
            return "error";
    }

    public void setBotonPeticionAmistad(boolean esPeticion, final String idComentario, final String idUsuario){
        if (esPeticion){
            botonPeticionAmistad.setVisibility(View.VISIBLE);
            botonPeticionAmistad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Peticion p = new Peticion(
                            idComentario,
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            idUsuario,
                            "pendiente",
                            idComentarioPeticion
                    );

                    mDataBase = FirebaseDatabase.getInstance().getReference();
                    mDataBase
                            .child("peticiones")
                            .child(idUsuario)
                            .child("nuevas")
                            .child(p.getId())
                            .setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //onAddMessageClicked();
                        }
                    });
                    mDataBase
                            .child("peticiones")
                            .child(idUsuario)
                            .child("to_me")
                            .child(p.getId())
                            .setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //onAddMessageClicked();
                        }
                    });
                    mDataBase
                            .child("peticiones")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("from_me")
                            .child(p.getId())
                            .setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(itemView.getContext(),"Peticion enviada",Toast.LENGTH_SHORT).show();
                            //onAddMessageClicked();
                        }
                    });
                }
            });
        }else{
            botonPeticionAmistad.setVisibility(View.GONE);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "prueba name";
            String description = "pruebaa";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }else{
            Toast.makeText(context,"fallo",Toast.LENGTH_SHORT).show();
        }
        */
    }

    private Task<String> enviarNotificacion(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("sendNotification")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

    private void onAddMessageClicked() {
        String inputMessage = "prueba input";

        // [START call_add_message]
        enviarNotificacion(inputMessage)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }

                            // [START_EXCLUDE]
                            Log.w("prueba", "addMessage:onFailure", e);
                            return;
                            // [END_EXCLUDE]
                        }

                        // [START_EXCLUDE]
                        String result = task.getResult();
                        Log.d("res task:",result);
                        // [END_EXCLUDE]
                    }
                });
        // [END call_add_message]
    }

}
