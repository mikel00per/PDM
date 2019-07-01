package com.example.remotymobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppComponentFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private static final int REQUEST_INTERNET_PERMISSION = 101;
    private static final int REQUEST_NETWORK_PERMISSION = 102;

    int SDK_INT = android.os.Build.VERSION.SDK_INT;


    String textoEscuchado;
    ClientHandler clientHandler;
    ClientThread clientThread;
    EditText editTextAddress, editTextPort, editTextMsg;
    Button buttonConnect, buttonDisconnect, buttonSend, buttonNex, buttonAudio;
    TextView textViewState, textViewRx;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    View.OnTouchListener buttonAudioOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    //when the user removed the finger
                    mSpeechRecognizer.stopListening();
                    editTextMsg.setHint("Escriba o dicte la nota");
                    break;

                case MotionEvent.ACTION_DOWN:
                    //finger is on the button
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    editTextMsg.setText("");
                    editTextMsg.setHint("Escuchando...");
                    break;
            }
            return false;
        }
    };
    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    clientThread = new ClientThread(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()),
                            clientHandler);
                    clientThread.start();

                    buttonConnect.setEnabled(false);
                    buttonDisconnect.setEnabled(true);
                }
            };
    View.OnClickListener buttonDisConnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (clientThread != null) {
                clientThread.setRunning(false);
            }

        }
    };
    View.OnClickListener buttonSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (clientThread != null) {
                String msgToSend = editTextMsg.getText().toString();
                clientThread.txMsg(textoEscuchado);
            }
        }
    };
    View.OnClickListener buttonNextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.escuchardor_audio);
            buttonSend = (Button) findViewById(R.id.send);
            editTextMsg = (EditText) findViewById(R.id.msgtosend);
            textViewRx = (TextView) findViewById(R.id.received);

            buttonSend.setOnClickListener(buttonSendOnClickListener);

            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);


            buttonAudio = findViewById(R.id.audio);

            mSpeechRecognizerIntent
                    .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            mSpeechRecognizerIntent
                    .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
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
                    if (matches != null) {
                        editTextMsg.setText(matches.get(0));
                        textoEscuchado = matches.get(0);
                        buttonSend.setEnabled(true);
                    } else {
                        buttonSend.setEnabled(false);
                    }
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });

            buttonAudio.setOnTouchListener(buttonAudioOnTouchListener);
        }
    };
    private Button escuchar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        checkPermission();

        setContentView(R.layout.activity_main);
        editTextAddress = (EditText) findViewById(R.id.ip);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.conectar);
        buttonDisconnect = (Button) findViewById(R.id.desconectar);
        buttonNex = findViewById(R.id.next);
        textViewState = (TextView) findViewById(R.id.state);

        buttonDisconnect.setEnabled(false);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonDisconnect.setOnClickListener(buttonDisConnectOnClickListener);
        buttonNex.setOnClickListener(buttonNextOnClickListener);

        clientHandler = new ClientHandler(this);

    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.



    private void updateState(String state) {
        textViewState.setText(state);
    }

    private void updateRxMsg(String rxmsg) {
        textViewRx.append(rxmsg + "\n");
    }

    private void clientEnd() {
        clientThread = null;
        textViewState.setText("clientEnd()");
        buttonConnect.setEnabled(true);
        buttonDisconnect.setEnabled(false);

    }

    private void checkPermission() {
        boolean recordAudioPermissionGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED;

        if (recordAudioPermissionGranted) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_PERMISSION);
        }

        boolean internetPermissionGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                        == PackageManager.PERMISSION_GRANTED;

        if (internetPermissionGranted) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    REQUEST_INTERNET_PERMISSION);
        }

        boolean networkPermissionGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                        == PackageManager.PERMISSION_GRANTED;

        if (networkPermissionGranted) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_NETWORK_PERMISSION);
        }

    }

    public static class ClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MainActivity parent;

        public ClientHandler(MainActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_STATE:
                    parent.updateState((String) msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String) msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }

    }

}
