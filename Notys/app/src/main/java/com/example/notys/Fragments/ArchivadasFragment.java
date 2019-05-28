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
import android.widget.Toast;

import com.example.notys.NavigationDrawerConstants;
import com.example.notys.Notas.Nota;
import com.example.notys.Notas.NotaAdapter;

import com.example.notys.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ArchivadasFragment extends Fragment {

    private RecyclerView rvNotas;
    private GridLayoutManager glm;
    private NotaAdapter adapter;

    public ArrayList<Nota> data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_ARCHIVADAS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_archivadas, container, false);

        rvNotas = v.findViewById(R.id.lista_notas_archivadas);
        glm = new GridLayoutManager(getActivity(), 2);
        rvNotas.setLayoutManager(glm);


        data = new ArrayList<Nota>();

        try {
            data = leerNotasArchivadas();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new NotaAdapter(getContext(), data,true);

        adapter.setOnItemClickListener(new NotaAdapter.OnItemClickListener() {
            @Override
            public void onItemArchivar(int pos) throws IOException {

            }

            @Override
            public void onItemBorrar(int pos) throws IOException {

            }

            @Override
            public void onItemDesarchivar(int pos) throws IOException {
                Toast.makeText(getContext(), "Desarchivada", Toast.LENGTH_SHORT).show();
                desarchivarNota(pos);
            }

        });
        rvNotas.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

    public void desarchivarNota(int id) throws IOException {
        Nota aDesarchivar = data.get(id);
        aDesarchivar.setOculto(false);
        borrarNotaArchivada(id);
        guardarNotaGeneral(aDesarchivar);
    }

    public void borrarNotaArchivada(int id) throws IOException {
        data.remove(id);
        // Borro el archivo anterior para abrir uno nuevo y guardar los datos
        File anterior = new File(getContext().getFilesDir(),"notas_archivadas");
        anterior.delete();
        // Ahora borramos en el archivo.
        for (int i = 0; i < data.size(); i++){
            guardarNotaArchivada(data.get(i));
        }

    }


    public ArrayList<Nota> leerNotasArchivadas() throws IOException {
        ArrayList<Nota> notas = new ArrayList<Nota>();
        Nota buff = null;
        FileInputStream fis = new FileInputStream(new File(getContext().getFilesDir(), "notas_archivadas"));
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
        oos.close();
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
        oos.close();
    }

}