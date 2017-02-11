package com.trabajo.carlos.fireball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private Button btnEnviar;
    private EditText edtMensaje;
    private TextView txvMensaje;

    private String nombreUsuario, nombreSala, chatMensaje, chatNombreUsuario, temp_key;

    private DatabaseReference root ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        edtMensaje = (EditText)findViewById(R.id.edtMensaje);
        txvMensaje = (TextView)findViewById(R.id.txvMensaje);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);

        nombreUsuario = getIntent().getExtras().get("nombreUsuario").toString();
        nombreSala = getIntent().getExtras().get("nombreSala").toString();

        setTitle(" Room - " + nombreSala);

        root = FirebaseDatabase.getInstance().getReference().child(nombreSala);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("nombre", nombreUsuario);
                map2.put("mensaje", edtMensaje.getText().toString());

                message_root.updateChildren(map2);

                edtMensaje.setText("");

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chatMensaje = (String) ((DataSnapshot)i.next()).getValue();
            chatNombreUsuario = (String) ((DataSnapshot)i.next()).getValue();

            txvMensaje.append(chatNombreUsuario + " : " + chatMensaje + " \n");
        }


    }

}
