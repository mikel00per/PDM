package com.example.notys.Fragments;

import android.content.Context;
import android.content.Intent;
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
import java.util.Date;
import java.util.Locale;

public class CrearNotaFragment extends Fragment {

    Button bAudio;
    RadioGroup grupoOpciones;
    RadioButton c1,c2,c3,c4,c5,c6,c7;
    TextView contenido_escrito;
    File path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_CREAR_NOTA);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_nota, container, false);

        // gruadamos contenido, el grupo y los colores para comprobar más tarde el color.
        contenido_escrito = v.findViewById(R.id.contenido_nota);
        grupoOpciones = v.findViewById(R.id.opciones_color);
        c1 = v.findViewById(R.id.radio1);
        c2 = v.findViewById(R.id.radio2);
        c3 = v.findViewById(R.id.radio3);
        c4 = v.findViewById(R.id.radio4);
        c5 = v.findViewById(R.id.radio5);
        c6 = v.findViewById(R.id.radio6);
        c7 = v.findViewById(R.id.radio7);

        path = getContext().getFilesDir();


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Elimino el contenido de la nota anterior.
                contenido_escrito.setText("");
                grupoOpciones.clearCheck();
                // cargo el gramento de inicio
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
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardo la nota
                // Miro los coles:
                String contenido = contenido_escrito.getText().toString();
                String color = "";
                int selectedId = grupoOpciones.getCheckedRadioButtonId();

                if (selectedId == c1.getId()){
                    color = "morado";
                }else if (selectedId == c2.getId()){
                    color = "azul";
                }else if (selectedId == c3.getId()){
                    color = "verde";
                }else if (selectedId == c4.getId()){
                    color = "amarillo";
                }else if (selectedId == c5.getId()){
                    color = "naranaja";
                }else if (selectedId == c6.getId()){
                    color = "gris";
                }else if (selectedId == c7.getId()){
                    color = "marino";
                }

                String id;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                id = simpleDateFormat.format(new Date());

                Nota n = new Nota (id,contenido,color,false);

                try {
                    guardarNota(n);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                contenido_escrito.setText("");
                grupoOpciones.clearCheck();

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

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                         RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
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
                if (matches != null) contenido_escrito.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        v.findViewById(R.id.botonAudioNota).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //when the user removed the finger
                        mSpeechRecognizer.stopListening();
                        contenido_escrito.setHint("Escriba o dicte la nota");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        //finger is on the button
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        contenido_escrito.setText("");
                        contenido_escrito.setHint("Escuchando...");
                        break;
                }
                return false;
            }
        });


        // Inflate the layout for this fragment
        return v;
    }


    public ArrayList<Nota> leerNotas() throws IOException, ClassNotFoundException {
        ArrayList<Nota> notas = new ArrayList<Nota>();
        Nota buff = null;
        FileInputStream fis = new FileInputStream(new File(path,"notas_generales"));
        ObjectInputStream ois = new ObjectInputStream(fis);
        while(fis.available() > 0) {
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

    public void guardarNota(Nota n) throws IOException {
        boolean exists = new File(path,"notas_generales").exists();
        FileOutputStream fos = new FileOutputStream(new File(path,"notas_generales"), true);
        ObjectOutputStream oos = exists ?
                new ObjectOutputStream(fos) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                }:new ObjectOutputStream(fos);
        oos.writeObject(n);
    }

}