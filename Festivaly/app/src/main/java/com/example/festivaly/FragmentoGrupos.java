package com.example.festivaly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentoGrupos extends Fragment implements View.OnClickListener {

    private Button button1, button2, button3, button4, button5, button6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.festivales, null);


        button1 = (Button) view.findViewById(R.id.botonFestival1);
        button1.setOnClickListener(this);

        button2 = (Button) view.findViewById(R.id.botonFestival2);
        button2.setOnClickListener(this);

        button3 = (Button) view.findViewById(R.id.botonFestival3);
        button3.setOnClickListener(this);

        button4 = (Button) view.findViewById(R.id.botonFestival4);
        button4.setOnClickListener(this);

        button5 = (Button) view.findViewById(R.id.botonFestival5);
        button5.setOnClickListener(this);

        button6 = (Button) view.findViewById(R.id.botonFestival6);
        button6.setOnClickListener(this);


        return view;

    }


    @Override
    public void onClick(View v) {

        Fragment nuevo = new FragmentoFestival();
        Bundle b = new Bundle();


        switch (v.getId()){
            case R.id.botonFestival1:
                b.putInt("num_festival",1);
                break;

            case R.id.botonFestival2:
                b.putInt("num_festival",2);
                break;

            case R.id.botonFestival3:
                b.putInt("num_festival",3);
                break;

            case R.id.botonFestival4:
                b.putInt("num_festival",4);
                break;

            case R.id.botonFestival5:
                b.putInt("num_festival",5);
                break;

            case R.id.botonFestival6:
                b.putInt("num_festival",6);
                break;
        }

        nuevo.setArguments(b);
        leerFestival(nuevo);


    }

    public void leerFestival(Fragment fragment){
        if (fragment != null){
            getFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_fragmentos, fragment)
                    .addToBackStack("festival")
                    .commit();
        }else{

        }
    }

}
