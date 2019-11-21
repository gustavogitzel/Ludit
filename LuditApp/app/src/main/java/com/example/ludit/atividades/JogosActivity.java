package com.example.ludit.atividades;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ludit.R;
import com.example.ludit.adapters.JogosAdapter;
import com.example.ludit.bluetooth.ConnectionThread;
import com.example.ludit.games.FormasActivity;
import com.example.ludit.games.GeniusActivity;
import com.example.ludit.games.MatematicaActivity;
import com.example.ludit.games.PinguimActivity;
import com.example.ludit.games.ReciclagemActivity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class JogosActivity extends AppCompatActivity {

    static Context context;
    ListView lvLista;
    List<Integer> imagens = new LinkedList<Integer>();
    ConnectionThread connect;

    String[] titulos ={"Jogo das Formas", "Genius", "Jogo da Reciclagem", "Bola de Neve", "Jogo dos Números"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_selecionada);

        context = this;
        iniciarBluetooth();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        ((TextView)findViewById(R.id.tvTitulo)).setText("Jogos");
        lvLista = findViewById(R.id.lista);

        imagens.add(R.drawable.capa_formas); //Formas
        imagens.add( R.drawable.capa_genius); //Genius
        imagens.add(R.drawable.capa_reciclagem); //Reciclagem
        imagens.add( R.drawable.capa_pinguim); //Pinguim
        imagens.add(R.drawable.capa_matematica); //Matemática

        JogosAdapter listaImagensAdapter = new JogosAdapter(getApplicationContext(), imagens, titulos);
        lvLista.setAdapter(listaImagensAdapter);

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Class<?> qual = null;
                switch (position)
                {
                    case 0: qual = FormasActivity.class; break;
                    case 1: qual = GeniusActivity.class; break;
                    case 2: qual = ReciclagemActivity.class; break;
                    case 3: qual = PinguimActivity.class; break;
                    case 4: qual = MatematicaActivity.class; break;
                }

                Intent i = new Intent(JogosActivity.this, qual);
                startActivity(i);
            }});

        ((Button)findViewById(R.id.btnSair)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void iniciarBluetooth()
    {
        /*  Teste rápido. O hardware Bluetooth do dispositivo Android
            está funcionando ou está bugado de forma misteriosa?
            Será que existe, pelo menos? Provavelmente existe.
         */
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(this,"Que pena! Hardware Bluetooth não está funcionando", Toast.LENGTH_SHORT).show();
        } else {

         /* A chamada do seguinte método liga o Bluetooth no dispositivo Android
            sem pedido de autorização do usuário. É altamente não recomendado no
            Android Developers, mas, para simplificar este app, que é um demo,
            faremos isso. Na prática, em um app que vai ser usado por outras
            pessoas, não faça isso.
         */
            if(!btAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, 3);
            }
        }
    }
}
