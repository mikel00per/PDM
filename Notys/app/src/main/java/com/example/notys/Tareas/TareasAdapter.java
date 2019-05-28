package com.example.notys.Tareas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.notys.R;

import java.io.IOException;
import java.util.ArrayList;


public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder>  {

    private ArrayList<Tarea> data;
    private TareasAdapter.OnItemClickListener mlistener;
    private Context context;
    private boolean archivada;

    public interface OnItemClickListener{
        void onItemBorrar(int pos) throws IOException;
        void onItemAplazar(int pos) throws IOException;
    }

    public void setOnItemClickListener(TareasAdapter.OnItemClickListener listener){
        mlistener = listener;
    }

    public TareasAdapter(Context c, ArrayList<Tarea> data) {
        this.context = c;
        this.data = data;
    }

    @Override
    public TareasAdapter.TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TareasAdapter.TareaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false), mlistener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(TareaViewHolder holder, final int position) {
        holder.titulo.setText(data.get(position).getTitulo());
        holder.subtitulo.setText(data.get(position).getFechaInicio());
        holder.descripcion.setText(data.get(position).getDescripcion());

    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        else
            return 0;
    }

    class TareaViewHolder extends RecyclerView.ViewHolder{

        TextView titulo, subtitulo, descripcion;
        ImageButton expandir_contraer;
        Button editar, borrar;

        Boolean oculto;

        final Drawable  retraer = ContextCompat.getDrawable(context, R.drawable.ic_expand_less_black_36dp).mutate(),
                        expandir = ContextCompat.getDrawable(context, R.drawable.ic_expand_more_black_36dp).mutate();

        public TareaViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            titulo = itemView.findViewById(R.id.primary_text);
            subtitulo = itemView.findViewById(R.id.sub_text);
            descripcion = itemView.findViewById(R.id.descripcion_tarea);
            descripcion.setVisibility(View.GONE);

            oculto = true;

            editar = itemView.findViewById(R.id.tarea_aplazar);
            borrar = itemView.findViewById(R.id.tarea_realizada);

            expandir_contraer = itemView.findViewById(R.id.expandir_contraer);
            expandir_contraer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (oculto){
                        expandir_contraer.setImageDrawable(retraer);
                        descripcion.setVisibility(View.VISIBLE);
                        oculto = false;
                    }else{
                        expandir_contraer.setImageDrawable(expandir);
                        descripcion.setVisibility(View.GONE);
                        oculto = true;
                    }
                }
            });

            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            try {
                                listener.onItemAplazar(pos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            try {
                                listener.onItemBorrar(pos);
                                notifyItemRemoved(pos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        }
    }

    public void recargarVista(ArrayList<Tarea> tareas){
        data = tareas;
        notifyDataSetChanged();
        Log.d("recargarVista","entro en la funcion");
    }
}
