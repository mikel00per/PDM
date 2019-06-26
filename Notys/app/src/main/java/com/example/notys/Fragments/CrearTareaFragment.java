package com.example.notys.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.notys.NavigationDrawerConstants;
import com.example.notys.Notas.Nota;
import com.example.notys.R;
import com.example.notys.Tareas.Tarea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class CrearTareaFragment extends Fragment {

    RadioGroup grupoOpciones;
    RadioButton c1,c2,c3,c4,c5,c6,c7;
    TextView titulo, descripcion_tarea, fechaSeleccionada;
    String fechaIniSeleccionada, fechaFinSeleccionada, horaSeleccionada, colorSeleccionado;
    File path;
    Button bAudio;

    String cadena_escuchada = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_CREAR_TAREA);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_tarea, container, false);

        path = getActivity().getFilesDir();
        // gruadamos contenido, el grupo y los colores para comprobar más tarde el color.
        titulo = v.findViewById(R.id.titulo_tarea);
        descripcion_tarea = v.findViewById(R.id.descripcion_tarea);
        fechaSeleccionada = v.findViewById(R.id.f_seleccionada);

        fechaIniSeleccionada = "";
        fechaFinSeleccionada = "";
        horaSeleccionada = "";
        colorSeleccionado = "";


        v.findViewById(R.id.abrir_calendario).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
                    @Override
                    public void onCancelled() {
                        String fecha = "Sin fecha, pulse arriba!";
                        fechaSeleccionada.setText(fecha);
                    }

                    @Override
                    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                        String fecha = "Sin fecha, pulse arriba!";
                        if (firstDate != null){
                            fechaIniSeleccionada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(firstDate.getTime());
                            fecha = fechaIniSeleccionada;
                        }
                        if (secondDate != null){
                            fechaFinSeleccionada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(secondDate.getTime());
                            fecha = fecha  + " - " + fechaFinSeleccionada;
                        }

                        fechaSeleccionada.setText(fecha);

                    }
                };

                new SlyCalendarDialog()
                        .setSingle(false)
                        .setCallback(callback)
                        .setBackgroundColor(Color.parseColor("#FFFFFF"))
                        .setHeaderColor(Color.parseColor("#008577"))
                        .setSelectedTextColor(Color.parseColor("#FFFFFF"))
                        .setSelectedColor(Color.parseColor("#00574B"))
                        .show(getActivity().getSupportFragmentManager(), "TAG_SLYCALENDAR");

            }



        });

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cargo el gramento de notas
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();


                // Recupero el icono anterior
                Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
            }
        });

        // Cambiamos los botenes flotantes
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);

        FloatingActionButton fabSave = getActivity().findViewById(R.id.floatingActionButton);
        fabSave.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_download));
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardo la tarea
                String tituloEscrito, descripcionEscrita, fechaEscrita, colorSelecionado;
                tituloEscrito = String.valueOf(titulo.getText());
                descripcionEscrita = String.valueOf(descripcion_tarea.getText());

                Tarea nueva_tarea = new Tarea("1",tituloEscrito,descripcionEscrita,fechaIniSeleccionada,fechaFinSeleccionada,horaSeleccionada);

                try {
                    guardarTarea(nueva_tarea);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();

                // Recupero el icono anterior
                Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
            }


        });


        final SpeechRecognizer mSpeechRecognizer1 = SpeechRecognizer.createSpeechRecognizer(getContext());
        final Intent mSpeechRecognizerIntent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer1.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) titulo.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        v.findViewById(R.id.botonAudioNota2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //when the user removed the finger
                        mSpeechRecognizer1.stopListening();
                        titulo.setHint("Escriba o dicte algo");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        //finger is on the button
                        mSpeechRecognizer1.startListening(mSpeechRecognizerIntent1);
                        titulo.setText("");
                        titulo.setHint("Escuchando...");
                        break;
                }
                return false;
            }
        });

        final SpeechRecognizer mSpeechRecognizer2 = SpeechRecognizer.createSpeechRecognizer(getContext());
        final Intent mSpeechRecognizerIntent2= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer2.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) descripcion_tarea.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        v.findViewById(R.id.botonAudioNota3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //when the user removed the finger
                        mSpeechRecognizer2.stopListening();
                        descripcion_tarea.setHint("Escriba o dicte algo");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        //finger is on the button
                        mSpeechRecognizer2.startListening(mSpeechRecognizerIntent2);
                        descripcion_tarea.setText("");
                        descripcion_tarea.setHint("Escuchando...");
                        break;
                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    public ArrayList<Tarea> leerTareas() throws IOException, ClassNotFoundException {
        ArrayList<Tarea> tareas = new ArrayList<Tarea>();
        Nota buff = null;
        FileInputStream fis = new FileInputStream(new File(path,"tareas"));
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
        boolean exists = new File(path,"tareas").exists();
        FileOutputStream fos = new FileOutputStream(new File(path,"tareas"), true);
        ObjectOutputStream oos = exists ?
                new ObjectOutputStream(fos) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                }:new ObjectOutputStream(fos);
        oos.writeObject(t);
    }

}