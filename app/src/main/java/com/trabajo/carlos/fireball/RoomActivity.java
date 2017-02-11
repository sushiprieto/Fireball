package com.trabajo.carlos.fireball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

public class RoomActivity extends AppCompatActivity {

    private Button  btnAdd;
    private EditText edtSala;
    private ListView lsvSalas;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listadoSalas = new ArrayList<>();

    private String nombreUsuario;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        edtSala = (EditText)findViewById(R.id.edtSala);
        lsvSalas = (ListView)findViewById(R.id.lsvSalas);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listadoSalas);
        lsvSalas.setAdapter(arrayAdapter);

        pedirNombreSusuario();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<String, Object>();
                map.put(edtSala.getText().toString(), "");
                root.updateChildren(map);

                edtSala.setText("");

            }
        });

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

        lsvSalas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("nombreSala", ((TextView)view).getText().toString());
                intent.putExtra("nombreUsuario", nombreUsuario);
                startActivity(intent);

            }
        });

    }

    private void pedirNombreSusuario() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduce nombre:");

        final EditText edtNombre = new EditText(this);

        builder.setView(edtNombre);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                nombreUsuario = edtNombre.getText().toString();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
                pedirNombreSusuario();

            }
        });

        builder.show();

    }

}