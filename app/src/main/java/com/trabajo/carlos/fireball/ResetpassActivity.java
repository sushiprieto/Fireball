package com.trabajo.carlos.fireball;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetpassActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEmailReset;
    private Button btnReset, btnAtras;
    private FirebaseAuth autenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);

        edtEmailReset = (EditText)findViewById(R.id.edtEmailReset);
        btnReset = (Button)findViewById(R.id.btnReset);
        btnAtras = (Button)findViewById(R.id.btnAtras);

        autenti = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(this);
        btnAtras.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == btnAtras){
            finish();
        }

        if (view == btnReset){

            String email = edtEmailReset.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Por favor inserte email", Toast.LENGTH_SHORT).show();
                return;
            }

            autenti.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(ResetpassActivity.this, "Te hemos enviado las instrucciones para reiniciar tu contraseña", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetpassActivity.this, "Fallo al enviar el email", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }
}
