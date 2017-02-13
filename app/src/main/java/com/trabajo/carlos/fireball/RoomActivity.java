package com.trabajo.carlos.fireball;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener{

    private Button  btnAdd;
    private EditText edtSala;
    private ListView lsvSalas;
    //private TextView txvShowAlias;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listadoSalas = new ArrayList<>();

    private String alias = "";

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private FirebaseAuth autenti;
    private FirebaseUser usuario;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Recogemos la isntancia de la autentificacion
        autenti = FirebaseAuth.getInstance();

        btnAdd = (Button)findViewById(R.id.btnAdd);
        edtSala = (EditText)findViewById(R.id.edtSala);
        lsvSalas = (ListView)findViewById(R.id.lsvSalas);
        //txvShowAlias = (TextView)findViewById(R.id.txvShowAlias);

        //Recogemos el usuario actual
        usuario = autenti.getCurrentUser();
        //txvShowAlias.setText(usuario.getEmail());

        // TODO: 13/02/2017 Al salir de la app no guarda el alias
        //Recogemos el alias
        //alias = getIntent().getExtras().get("alias").toString();

        //Establecemos el titulo de la actividad
        setTitle(usuario.getEmail());
        //setTitle(alias);

        //txvShowAlias.setText(alias);

        //Cogemos el usuario actual
        //final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(RoomActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        //Creamos la lista de salas
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listadoSalas);
        lsvSalas.setAdapter(arrayAdapter);

        btnAdd.setOnClickListener(this);

        //Rellenamos la lista
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                listadoSalas.clear();
                listadoSalas.addAll(set);

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Método onclick de los elementos de la lista
        lsvSalas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("nombreSala", ((TextView)view).getText().toString());
                //intent.putExtra("nombreUsuario", nombreUsuario);
                intent.putExtra("alias2", alias);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == btnAdd){

            //Comprobamos que el campo no esté vacio
            if (TextUtils.isEmpty(edtSala.getText())){

                Toast.makeText(this, "Por favor inserte una sala", Toast.LENGTH_SHORT).show();

            }else{

                Map<String, Object> map = new HashMap<String, Object>();
                map.put(edtSala.getText().toString(), "");
                root.updateChildren(map);

                edtSala.setText("");

                edtSala.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ajuste) {



        }else if (id == R.id.action_logout){

            autenti.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        }else if (id == R.id.action_nuevasala){

            edtSala.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        autenti.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            autenti.removeAuthStateListener(authListener);
        }
    }

}
