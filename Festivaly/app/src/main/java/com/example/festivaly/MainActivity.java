package com.example.festivaly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email,password, emailLogin, passwordLogin;
    Button botonRegistrar,botonInicioSesion, botonLogin, botonVolver;
    private FirebaseAuth firebaseAuth;

    private String nombre, apellidos, correo, contrasenia, id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        FirebaseApp.initializeApp(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        botonRegistrar = (Button) findViewById(R.id.botonRegistro);
        botonRegistrar.setOnClickListener(this);

        botonInicioSesion = (Button) findViewById(R.id.botonInicioSesion);
        botonInicioSesion.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        /*registerButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(TextUtils.isEmpty(emailText)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordText)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                }

                if(passwordText.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }

                Log.d("Email:", emailText);
                Log.d("Pass:", passwordText);

                firebaseAuth.createUserWithEmailAndPassword(emailText,passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"E-mail or password is wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(firebaseAuth.getCurrentUser() != null){
            //TODO: Cargar la actividad inicial de la app ya que se había iniciado sesión
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonRegistro:startActivity(new Intent(getApplicationContext(),MainActivity.class));
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(TextUtils.isEmpty(emailText)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordText)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                }

                if(passwordText.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }

                Log.d("Email:", emailText);
                Log.d("Pass:", passwordText);

                firebaseAuth.createUserWithEmailAndPassword(emailText,passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"E-mail or password is wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                if(firebaseAuth.getCurrentUser()!=null){
                    finish();
                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            break;

            case R.id.botonInicioSesion:
                setContentView(R.layout.login);

                botonLogin = (Button) findViewById(R.id.botonLogin);
                botonLogin.setOnClickListener(this);

                botonVolver = (Button) findViewById(R.id.botonVolver);
                botonVolver.setOnClickListener(this);

                emailLogin = (EditText) findViewById(R.id.emailLogin);
                passwordLogin = (EditText) findViewById(R.id.passwordLogin);

            break;

            case R.id.botonLogin:
                String emailTLogin = emailLogin.getText().toString();
                String passwordTLogin = passwordLogin.getText().toString();

                if(TextUtils.isEmpty(emailTLogin)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordTLogin)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                }

                if(passwordTLogin.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }

                Log.d("Email:", emailTLogin);
                Log.d("Pass:", passwordTLogin);

                firebaseAuth.signInWithEmailAndPassword(emailTLogin, passwordTLogin)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("ERROR: ", "signInWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    // TODO: CARGAR ACTIVIDAD INIO SESION
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("ERROR: ", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            break;

            case R.id.botonVolver:
                setContentView(R.layout.register);
                email = (EditText) findViewById(R.id.email);
                password = (EditText) findViewById(R.id.password);

                botonRegistrar = (Button) findViewById(R.id.botonRegistro);
                botonRegistrar.setOnClickListener(this);

                botonInicioSesion = (Button) findViewById(R.id.botonInicioSesion);
                botonInicioSesion.setOnClickListener(this);
            break;
        }

    }

}