package com.trabajo.carlos.fireball;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogeo;
    private EditText edtEmailLog, edtPassLog, edtAlias;
    private TextView txvSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth autenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        autenti = FirebaseAuth.getInstance();
        if (autenti.getCurrentUser() != null){

            finish();
            startActivity(new Intent(getApplicationContext(), RoomActivity.class));

        }

        edtEmailLog = (EditText)findViewById(R.id.edtEmailLog);
        edtPassLog = (EditText)findViewById(R.id.edtPasswordLog);
        //edtAlias = (EditText)findViewById(R.id.edtAlias);

        txvSignUp = (TextView)findViewById(R.id.txvSignUp);

        btnLogeo = (Button)findViewById(R.id.btnLogeo);

        btnLogeo.setOnClickListener(this);
        txvSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == btnLogeo){
            userLogin();
        }

        if (view == txvSignUp){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

    }

    private void userLogin() {

        String email = edtEmailLog.getText().toString().trim();
        String password = edtPassLog.getText().toString().trim();
        //final String alias = edtAlias.getText().toString().trim();

        //Comprobamos si el correo o la contraseña esta vacio
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Por favor inserte email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Por favor inserte contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        autenti.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()){

                    finish();
                    Intent envioAlias = new Intent(getApplicationContext(), RoomActivity.class);
                    //Intent envioAlias2 = new Intent(getApplicationContext(), ChatActivity.class);
                    //envioAlias.putExtra("alias", alias);
                    //envioAlias2.putExtra("alias2", alias);
                    startActivity(envioAlias);

                }else {

                    Toast.makeText(LoginActivity.this, "No se pudo iniciar sesión...", Toast.LENGTH_SHORT).show();

                }

            }
        });

        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

    }
}
