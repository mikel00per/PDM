package com.example.notys.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.notys.NavigationDrawerConstants;
import com.example.notys.Notas.Nota;
import com.example.notys.Notas.NotaAdapter;

import com.example.notys.R;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NotasFragment extends Fragment {

    private RecyclerView rvNotas;
    private GridLayoutManager glm;
    private NotaAdapter adapter;


    public ArrayList<Nota> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_NOTAS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notas, container, false);

        rvNotas = v.findViewById(R.id.lista_notas);
        glm = new GridLayoutManager(getActivity(), 2);
        rvNotas.setLayoutManager(glm);


        data = new ArrayList<Nota>();

        try {
            data = leerNotas();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new NotaAdapter(getContext(), data);

        adapter.setOnItemClickListener(new NotaAdapter.OnItemClickListener() {
            @Override
            public void onItemArchivar(int id) throws IOException {
                Toast.makeText(getContext(), "Archivado", Toast.LENGTH_SHORT).show();
                archivarNota(id);
            }

            @Override
            public void onItemBorrar(int id) throws IOException {
                Toast.makeText(getContext(), "Borrado: " + String.valueOf(id), Toast.LENGTH_SHORT).show();
                borrarNotaGeneral(id);

                // Recarga el activi recargando la vista tmb,
                // TODO: buscar alternativa

            }

            @Override
            public void onItemDesarchivar(int pos) throws IOException {

            }
        });
        rvNotas.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

    public void archivarNota(int id) throws IOException {
        Nota aArchivar = data.get(id);
        aArchivar.setOculto(true);
        borrarNotaGeneral(id);
        guardarNotaArchivada(aArchivar);
    }

    public void borrarNotaGeneral(int id) throws IOException {
        data.remove(id);
        // Borro el archivo anterior para abrir uno nuevo y guardar los datos
        File anterior = new File(getContext().getFilesDir(),"notas_generales");
        anterior.delete();
        // Ahora borramos en el archivo.
        for (int i = 0; i < data.size(); i++){
            guardarNotaGeneral(data.get(i));
        }

    }


    public ArrayList<Nota> leerNotas() throws IOException {
        ArrayList<Nota> notas = new ArrayList<Nota>();
        Nota buff = null;
        FileInputStream fis = new FileInputStream(new File(getContext().getFilesDir(), "notas_generales"));
        ObjectInputStream ois = new ObjectInputStream(fis);
        while (fis.available() > 0) {
            try {
                Nota nota = (Nota) ois.readObject();
                notas.add(nota);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        ois.close();
        return notas;
    }

    public void guardarNotaGeneral(Nota n) throws IOException {

        boolean exists = new File(getContext().getFilesDir(),"notas_generales").exists();
        FileOutputStream fos = new FileOutputStream(new File(getContext().getFilesDir(),"notas_generales"), true);
        ObjectOutputStream oos = exists ?
                new ObjectOutputStream(fos) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                }:new ObjectOutputStream(fos);
        oos.writeObject(n);
    }

    public void guardarNotaArchivada(Nota n) throws IOException {

        boolean exists = new File(getContext().getFilesDir(),"notas_archivadas").exists();
        FileOutputStream fos = new FileOutputStream(new File(getContext().getFilesDir(),"notas_archivadas"), true);
        ObjectOutputStream oos = exists ?
                new ObjectOutputStream(fos) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                }:new ObjectOutputStream(fos);
        oos.writeObject(n);
    }

}