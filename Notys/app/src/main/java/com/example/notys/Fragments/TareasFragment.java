package com.example.notys.Fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notys.MainActivity;
import com.example.notys.NavigationDrawerConstants;
import com.example.notys.Notas.Nota;
import com.example.notys.R;
import com.example.notys.Tareas.Tarea;
import com.example.notys.Tareas.TareasAdapter;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TareasFragment extends Fragment {

    private ArrayList<Tarea> data;
    private ArrayList<Tarea> data_permanente;
    private TareasAdapter adapter;
    private GridLayoutManager glm;
    private RecyclerView rvTareas;
    CollapsibleCalendar collapsibleCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle( NavigationDrawerConstants.TAG_TAREA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tareas, container, false);

        collapsibleCalendar = v.findViewById(R.id.collapsibleCalendarView);

        // Leo los datos guardados
        try {
            data = leerTareas();
            data_permanente = data;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Marco en el calendario los dias con eventos
        try {
            marcarTareas(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        rvTareas = v.findViewById(R.id.lista_tareas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvTareas.setLayoutManager(glm);

        adapter = new TareasAdapter(getContext(), data);

        adapter.setOnItemClickListener(new TareasAdapter.OnItemClickListener() {
            @Override
            public void onItemBorrar(int pos) throws IOException {
                Toast.makeText(getContext(), "Borrado: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
                borrarTarea(pos);

            }

            @Override
            public void onItemAplazar(int pos) throws IOException {
                Toast.makeText(getContext(), "Aplazar: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
                aplazarTarea(pos);
            }
        });

        rvTareas.setAdapter(adapter);

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Log.d("onDaySelect","entro en la funccion");
                Day day = collapsibleCalendar.getSelectedDay();

                String fecha_seleccionada = day.getDay() + "/" + (day.getMonth() + 1) + "/" + day.getYear();
                Log.d("onDaySelect","Dia seleccionado: "+ fecha_seleccionada);

                ArrayList<Tarea> data_a_mostrar = new ArrayList<Tarea>();

                int anio=0, mes=0, dia=0;
                String fecha = "", fecha_parse = "", color = "", parse[];

                Log.d("onDaySelect", String.valueOf(data.size()));

                for (int i = 0; i < data.size(); i++){
                    fecha = data.get(i).getFechaInicio();
                    parse = fecha.split("/");
                    anio = Integer.parseInt(parse[2]);
                    mes = Integer.parseInt(parse[1]);
                    dia = Integer.parseInt(parse[0]);
                    fecha_parse = dia + "/"+mes+"/"+anio;

                    Log.d("onDaySelect","es "+fecha_parse+"=="+fecha_seleccionada);
                    if (fecha_parse.contains(fecha_seleccionada)){
                        data_a_mostrar.add(data.get(i));
                        Log.d("onDaySelect", "AÑADO A DATA A MOSTRAR");
                    }
                }

                adapter.recargarVista(data_a_mostrar);
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    private void marcarTareas(ArrayList<Tarea> data) throws ParseException {
        int anio=0, mes=0, dia=0;
        String fecha = "", color = "";

        for (int i = 0; i < data.size(); i++){
            fecha = data.get(i).getFechaInicio();
            color = "#000000";
            String parse[] = fecha.split("/");

            anio = Integer.parseInt(parse[2]);
            mes = Integer.parseInt(parse[1]);
            dia = Integer.parseInt(parse[0]);

            collapsibleCalendar.addEventTag(anio,mes-1,dia);
        }
    }

    private void aplazarTarea(int pos) {

    }

    private void borrarTarea(int pos) throws IOException {

        data.remove(pos);
        // Borro el archivo anterior para abrir uno nuevo y guardar los datos
        File anterior = new File(getContext().getFilesDir(),"tareas");
        anterior.delete();
        // Ahora borramos en el archivo.
        for (int i = 0; i < data.size(); i++){
            guardarTarea(data.get(i));
        }
    }

    public ArrayList<Tarea> leerTareas() throws IOException, ClassNotFoundException {
        ArrayList<Tarea> tareas = new ArrayList<Tarea>();
        Nota buff = null;
        FileInputStream fis = new FileInputStream(new File(getActivity().getFilesDir(),"tareas"));
        ObjectInputStream ois = new ObjectInputStream(fis);
        while(fis.available() > 0) {
            try {
                Tarea tarea = (Tarea) ois.readObject();
                tareas.add(tarea);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        ois.close();
        return tareas;
    }

    public void guardarTarea(Tarea t) throws IOException {
        boolean exists = new File(getActivity().getFilesDir(),"tareas").exists();
        FileOutputStream fos = new FileOutputStream(new File(getActivity().getFilesDir(),"tareas"), true);
        ObjectOutputStream oos = exists ?
                new ObjectOutputStream(fos) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                }:new ObjectOutputStream(fos);
        oos.writeObject(t);
    }
}