package com.example.festivaly.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.festivaly.Constantes;
import com.example.festivaly.R;

public class NotificacionesFragment extends Fragment implements View.OnClickListener {

    Button toMeButton, fromMeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;
        v = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        getActivity().setTitle(Constantes.TAG_NOTIFICACIONES);

        toMeButton = v.findViewById(R.id.tome);
        toMeButton.setOnClickListener(this);
        fromMeButton = v.findViewById(R.id.fromme);
        fromMeButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment f = new ToMeFragment();
        leerSubFragment(f);
    }

    boolean leerSubFragment(Fragment f){
        if (f != null){
            getChildFragmentManager().beginTransaction().replace(R.id.subFragment,f).commit();
            return true;
        }else
            return false;
    }

    @Override
    public void onClick(View v) {
        Fragment f = null;
        switch (v.getId()){
            case R.id.tome:
                f = new ToMeFragment();
                toMeButton.setBackgroundColor(getResources().getColor(R.color.colorPestaniaClick));
                fromMeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.fromme:
                f = new FromMeFragment();
                toMeButton.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                fromMeButton.setBackgroundColor(getResources().getColor(R.color.colorPestaniaClick));
                break;
        }

        leerSubFragment(f);
    }
}
