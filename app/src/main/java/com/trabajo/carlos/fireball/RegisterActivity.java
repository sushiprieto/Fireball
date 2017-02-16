package com.trabajo.carlos.fireball;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.trabajo.carlos.fireball.utils.ProgressGenerator;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, ProgressGenerator.OnCompleteListener{

   // private Button btnRegistro;
    private EditText edtEmail, edtEmailConfi, edtPass;
    private TextView txvSigIn;

    private ActionProcessButton btnRegistroGuay;
    private ProgressGenerator progressGenerator;
    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";

    //private ProgressDialog progressDialog;

    private FirebaseAuth autenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //progressDialog = new ProgressDialog(this);

        //Cogemos la instancia de la autentificacion
        autenti = FirebaseAuth.getInstance();
        /**if (autenti.getCurrentUser() != null){

            finish();
            startActivity(new Intent(getApplicationContext(), RoomActivity.class));

        }**/

        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtEmailConfi = (EditText)findViewById(R.id.edtEmailConfirm);
        edtPass = (EditText)findViewById(R.id.edtPassword);

        txvSigIn = (TextView)findViewById(R.id.txvSignIn);

        //btnRegistro = (Button)findViewById(R.id.btnRegistro);
        btnRegistroGuay = (ActionProcessButton)findViewById(R.id.btnRegistro);

        //btnRegistro.setOnClickListener(this);
        btnRegistroGuay.setOnClickListener(this);
        txvSigIn.setOnClickListener(this);

        progressGenerator = new ProgressGenerator(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
            btnRegistroGuay.setMode(ActionProcessButton.Mode.ENDLESS);
        } else {
            btnRegistroGuay.setMode(ActionProcessButton.Mode.PROGRESS);
        }

        //btnRegistroGuay.setMode(ActionProcessButton.Mode.PROGRESS);

        // no progress
        //btnRegistroGuay.setProgress(0);
        // progressDrawable cover 50% of button width, progressText is shown
        //btnRegistroGuay.setProgress(50);
        // progressDrawable cover 75% of button width, progressText is shown
        //btnRegistroGuay.setProgress(75);
        // completeColor & completeText is shown
        //btnRegistroGuay.setProgress(100);

        // you can display endless google like progress indicator
        //btnRegistroGuay.setMode(ActionProcessButton.Mode.ENDLESS);
        // set progress > 0 to start progress indicator animation
        //btnRegistroGuay.setProgress(1);

    }

        @Override
    public void onClick(View view) {

        if (view == btnRegistroGuay){

            progressGenerator.start(btnRegistroGuay);
            //btnRegistroGuay.setEnabled(false);
            //edtEmail.setEnabled(false);
            //edtEmailConfi.setEnabled(false);
            //edtPass.setEnabled(false);

        }

        if (view == txvSigIn){
            //startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }

    /**
     * Metodo que registra un usuario mediante el email y la contraseña
     */
    private void registrarUsuario() {

        //Recogemos los datos de los edittexts
        String email = edtEmail.getText().toString().trim();
        String emailConfirm = edtEmailConfi.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        //Comprobamos si el correo o la contraseña estan vacio
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(emailConfirm)){

            Toast.makeText(this, "Por favor inserte email", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(password)){

            Toast.makeText(this, "Por favor inserte contraseña", Toast.LENGTH_SHORT).show();
            return;

        }

        //Comprobamos que el email sea correcto
        if (email.equals(emailConfirm)){

            //Creamos un usuario
            autenti.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();

                    }else {

                        Toast.makeText(RegisterActivity.this, "No se pudo registrar...", Toast.LENGTH_SHORT).show();
                        //progressDialog.dismiss();

                    }

                }
            });

            //Le indicamos un mensaje de que se esta registrando
            //progressDialog.setMessage("Registrando usuario...");
            //progressDialog.show();

        }else{

            Toast.makeText(this, "El email no coincide", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Metodo para cuando se acabe la animacion del botón
     */
    @Override
    public void onComplete() {

        registrarUsuario();

    }
}
