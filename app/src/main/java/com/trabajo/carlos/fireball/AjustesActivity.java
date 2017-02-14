package com.trabajo.carlos.fireball;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AjustesActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCambiaEmail, btnCambiaPass, btnBorrarUser;
    private EditText edtCambiaEmail, edtCambiaPass;

    private FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth autenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        edtCambiaEmail = (EditText)findViewById(R.id.edtCambiaEmail);
        edtCambiaPass = (EditText)findViewById(R.id.edtCambiaPass);

        btnCambiaEmail = (Button)findViewById(R.id.btnCambiaEmail);
        btnCambiaPass = (Button)findViewById(R.id.btnCambiaPass);
        btnBorrarUser = (Button)findViewById(R.id.btnBorrarUser);

        setTitle(usuario.getEmail());

        //Cogemos la instancia de la sesión
        autenti = FirebaseAuth.getInstance();

        btnCambiaEmail.setOnClickListener(this);
        btnCambiaPass.setOnClickListener(this);
        btnBorrarUser.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        //boton cambiar email
        if (view == btnCambiaEmail){

            if (usuario != null && !edtCambiaEmail.getText().toString().trim().equals("")) {

                usuario.updateEmail(edtCambiaEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(AjustesActivity.this, "Su email se ha actualizado. Por favor inicia sesión con su nuevo email", Toast.LENGTH_LONG).show();
                            autenti.signOut();
                            startActivity(new Intent(AjustesActivity.this, LoginActivity.class));
                            finish();

                        } else {

                            Toast.makeText(AjustesActivity.this, "Fallo al cambiar su email, vuelve a iniciar sesión", Toast.LENGTH_LONG).show();

                        }

                    }

                });

            } else if (edtCambiaEmail.getText().toString().trim().equals("")) {

                edtCambiaEmail.setError("Introduce su email");

            }

        }

        //boton cambiar contraseña
        if (view == btnCambiaPass){

            //Recogemos los datos de los edittexts
            String contra = edtCambiaPass.getText().toString().trim();

            if (usuario != null) {

                if (contra.length() < 5) {

                    edtCambiaPass.setError("Mínimo 6 caracteres");

                } else {

                    usuario.updatePassword(contra).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(AjustesActivity.this, "La contraseña ha sido actualizada", Toast.LENGTH_SHORT).show();
                                autenti.signOut();
                                startActivity(new Intent(AjustesActivity.this, LoginActivity.class));
                                finish();

                            } else {

                                Toast.makeText(AjustesActivity.this, "Fallo al cambiar la contraseña", Toast.LENGTH_SHORT).show();

                            }

                        }

                    });
                }

            } else if (contra.equals("")) {

                edtCambiaPass.setError("Introduzca la contraseña");

            }

        }

        //boton borrar usuario
        if (view == btnBorrarUser){

            if (usuario != null) {

                usuario.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(AjustesActivity.this, "Tu perfil ha sido eliminado", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AjustesActivity.this, RegisterActivity.class));
                            finish();

                        } else {

                            Toast.makeText(AjustesActivity.this, "Fallo al eliminar tu usuario, vuelve a iniciar sesión", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }

        }

    }
}
