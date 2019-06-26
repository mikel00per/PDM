package com.example.notys.Notas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.notys.R;

import java.io.IOException;
import java.util.ArrayList;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> implements View.OnTouchListener {

    private ArrayList<Nota> data;
    private OnItemClickListener mlistener;
    private Context context;
    private boolean archivada;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    public interface OnItemClickListener{
            void onItemArchivar(int pos) throws IOException;
            void onItemBorrar(int pos) throws IOException;
            void onItemDesarchivar(int pos) throws IOException;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public NotaAdapter(Context c, ArrayList<Nota> data) {
        this(c, data, false);
    }

    public NotaAdapter(Context c, ArrayList<Nota> data, boolean archivada) {
        this.context = c;
        this.data = data;
        this.archivada = archivada;
    }

    @Override
    public NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (archivada) return new NotaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_archivado, parent, false), mlistener);
        else return new NotaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false), mlistener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(NotaViewHolder holder, final int position) {
        Nota nota = data.get(position);
        holder.contenido.setText(nota.getContenido());
        holder.contenido.setOnTouchListener(this);
        holder.contenido.setMovementMethod(new ScrollingMovementMethod());
        String color = nota.getColor();

        switch (color){
            case "morado":   holder.archivar.setImageDrawable(holder.d1); break;
            case "azul":     holder.archivar.setImageDrawable(holder.d2); break;
            case "verde":    holder.archivar.setImageDrawable(holder.d3); break;
            case "amarillo": holder.archivar.setImageDrawable(holder.d4); break;
            case "naranaja": holder.archivar.setImageDrawable(holder.d5); break;
            case "gris":     holder.archivar.setImageDrawable(holder.d6); break;
            case "marino":   holder.archivar.setImageDrawable(holder.d7); break;
            default:                                                   break;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NotaViewHolder extends RecyclerView.ViewHolder{

        TextView contenido;
        ImageButton archivar,borrar,adesarchivar;

        final Drawable  d1 = ContextCompat.getDrawable(context,R.drawable.archivado_color1).mutate(),
                        d2 = ContextCompat.getDrawable(context, R.drawable.archivado_color2).mutate(),
                        d3 = ContextCompat.getDrawable(context, R.drawable.archivado_color3).mutate(),
                        d4 = ContextCompat.getDrawable(context, R.drawable.archivado_color4).mutate(),
                        d5 = ContextCompat.getDrawable(context, R.drawable.archivado_color5).mutate(),
                        d6 = ContextCompat.getDrawable(context, R.drawable.archivado_color6).mutate(),
                        d7 = ContextCompat.getDrawable(context, R.drawable.archivado_color7).mutate();

        public NotaViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            contenido = itemView.findViewById(R.id.test_note);


            if (!archivada){
                archivar = itemView.findViewById(R.id.archivar);
                archivar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null){
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION){
                                try {
                                    listener.onItemArchivar(pos);
                                    notifyItemRemoved(pos);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                borrar = itemView.findViewById(R.id.borrar);
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
            }else{
                archivar = itemView.findViewById(R.id.adesarchivar);
                archivar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null){
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION){
                                try {
                                    listener.onItemDesarchivar(pos);
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
    }
}