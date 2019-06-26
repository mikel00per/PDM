package com.example.remoty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private Button escuchar;

    String textoEscuchado;

    ClientHandler clientHandler;
    ClientThread clientThread;


    EditText editTextAddress, editTextPort, editTextMsg;
    Button buttonConnect, buttonDisconnect, buttonSend;
    TextView textViewState, textViewRx;

    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();

        setContentView(R.layout.activity_main);
        editTextAddress = (EditText) findViewById(R.id.ip);
        editTextPort = (EditText) findViewById(R.id.port);
        editTextMsg = (EditText) findViewById(R.id.msgtosend);
        buttonConnect = (Button) findViewById(R.id.conectar);
        buttonDisconnect = (Button) findViewById(R.id.desconectar);
        buttonSend = (Button)findViewById(R.id.send);
        textViewState = (TextView)findViewById(R.id.state);
        textViewRx = (TextView)findViewById(R.id.received);


        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
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
                if (matches != null) {
                    editTextMsg.setText(matches.get(0));
                    textoEscuchado = matches.get(0);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });




        findViewById(R.id.audio).setOnTouchListener(new View.OnTouchListener() {
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
        });

        buttonDisconnect.setEnabled(false);
        buttonSend.setEnabled(false);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonDisconnect.setOnClickListener(buttonDisConnectOnClickListener);
        buttonSend.setOnClickListener(buttonSendOnClickListener);

        clientHandler = new ClientHandler(this);

    }

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
                    buttonSend.setEnabled(true);

                }
            };

    View.OnClickListener buttonDisConnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(clientThread != null){
                clientThread.setRunning(false);
            }

        }
    };

    View.OnClickListener buttonSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(clientThread != null){
                String msgToSend = editTextMsg.getText().toString();
                clientThread.txMsg(textoEscuchado);
            }
        }
    };


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            textoEscuchado = results.get(0);
            editTextMsg.setText(results.get(0));
            Log.d("MainActivity", textoEscuchado);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void updateState(String state){
        textViewState.setText(state);
    }

    private void updateRxMsg(String rxmsg){
        textViewRx.append(rxmsg + "\n");
    }

    private void clientEnd(){
        clientThread = null;
        textViewState.setText("clientEnd()");
        buttonConnect.setEnabled(true);
        buttonDisconnect.setEnabled(false);
        buttonSend.setEnabled(false);

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

            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String)msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }

    }


    private void checkPermission() {
        boolean recordAudioPermissionGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED;

        if (recordAudioPermissionGranted) {

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_PERMISSION);
        }

    }

}
