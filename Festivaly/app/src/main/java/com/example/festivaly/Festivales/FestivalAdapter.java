package com.example.festivaly.Festivales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.festivaly.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.FestivalViewHolder> {

    private ArrayList<Festival> data;
    private OnItemClickListener mlistener;
    private Context context;


    public interface OnItemClickListener {
        void onClickTablon(int pos);
        void onClickTL(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }


    public FestivalAdapter(Context c, ArrayList<Festival> data) {
        this.context = c;
        this.data = data;
    }

    @Override
    public FestivalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FestivalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_festival, parent, false), mlistener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(FestivalViewHolder holder, final int position) {
        Festival festival = data.get(position);
        holder.titulo.setText(festival.getTitulo());
        holder.subtitulo.setText(festival.getSubtitulo());
        holder.descripcion.setText(festival.getDescripcion());
        Picasso.get().load(data.get(position).getImg()).into(holder.img_festival);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FestivalViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_festival;
        public Button boton1, boton2;
        public TextView titulo, subtitulo, descripcion;
        ImageButton expandir_contraer;
        Boolean oculto;

        final Drawable  retraer = ContextCompat.getDrawable(context, R.drawable.ic_expand_less_black_36dp).mutate(),
                expandir = ContextCompat.getDrawable(context, R.drawable.ic_expand_more_black_36dp).mutate();

        public FestivalViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            img_festival = itemView.findViewById(R.id.img_festival);
            boton1 = itemView.findViewById(R.id.tablon_boton);
            boton2 = itemView.findViewById(R.id.timeline_boton);
            titulo = itemView.findViewById(R.id.titulo_festival);
            subtitulo = itemView.findViewById(R.id.subtitulo_festival);
            descripcion = itemView.findViewById(R.id.descripcion_festival);
            descripcion.setVisibility(View.GONE);
            oculto = true;

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

            boton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickTablon(getAdapterPosition());
                }
            });

            boton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickTL(getAdapterPosition());
                }
            });
        }
    }
}
