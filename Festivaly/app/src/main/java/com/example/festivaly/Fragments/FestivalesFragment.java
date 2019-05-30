package com.example.festivaly.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.festivaly.Constantes;
import com.example.festivaly.Festivales.Festival;
import com.example.festivaly.Festivales.FestivalAdapter;
import com.example.festivaly.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FestivalesFragment extends Fragment implements View.OnClickListener {
    private RecyclerView rvFestivales;
    private FestivalAdapter adapter;
    private GridLayoutManager glm;
    private DatabaseReference mDataBase;

    public ArrayList<Festival> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_festivales, null);

        getActivity().setTitle(Constantes.TAG_FESTIVALES);

        rvFestivales = view.findViewById(R.id.lista_festivales);
        glm = new GridLayoutManager(getActivity(), 1);
        rvFestivales.setLayoutManager(glm);

        data = new ArrayList<Festival>();

        data.add(new Festival("Viñarock","23/02/2019",Constantes.festival1,"https://firebasestorage.googleapis.com/v0/b/festivaly-599a7.appspot.com/o/imagenes-festivales%2Fvi%C3%B1a.jpg?alt=media&token=750ab67c-1d4e-4298-ae41-c55316103ffc"));
        data.add(new Festival("Sonar","23/02/2019", Constantes.festival2, "https://firebasestorage.googleapis.com/v0/b/festivaly-599a7.appspot.com/o/imagenes-festivales%2Fsonar.jpg?alt=media&token=e0e617a6-4359-4144-85c6-990c190a17c9"));
        data.add(new Festival("DreamBeach","23/02/2019", Constantes.festival3,"https://firebasestorage.googleapis.com/v0/b/festivaly-599a7.appspot.com/o/imagenes-festivales%2Fdreambeach.jpg?alt=media&token=f031dd87-7d47-4352-b060-b88ec31f0d2f"));
        data.add(new Festival("Azkena Rock","23/02/2019", Constantes.festival4,"https://firebasestorage.googleapis.com/v0/b/festivaly-599a7.appspot.com/o/imagenes-festivales%2Fazkena.jpg?alt=media&token=197d5dd3-7c4c-4d07-90d2-f7a2f7a53d1c"));

        adapter = new FestivalAdapter(getContext(),data);

        adapter.setOnItemClickListener(new FestivalAdapter.OnItemClickListener() {
            @Override
            public void onClickTablon(int pos) {
                Fragment f = new AdminFestivalFragment();
                Bundle b = new Bundle();
                b.putInt("num_festival",pos+1);
                f.setArguments(b);
                leerFragment(f);
            }

            @Override
            public void onClickTL(int pos) {
                Fragment f = new UserFestivalFragment();
                Bundle b = new Bundle();
                b.putInt("num_festival",pos+1);
                f.setArguments(b);
                leerFragment(f);
            }
        });

        rvFestivales.setAdapter(adapter);

        return view;

    }


    @Override
    public void onClick(View v) {


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
