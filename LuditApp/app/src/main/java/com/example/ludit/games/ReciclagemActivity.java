package com.example.ludit.games;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ludit.R;
import com.example.ludit.webservice.RetrofitConfig;
import com.example.ludit.webservice.UserService;
import com.example.ludit.webservice.Filho;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReciclagemActivity extends AppCompatActivity {
    ImageView img;

    int[] imagens = {R.drawable.glass_1, R.drawable.glass_2, R.drawable.glass_3, R.drawable.glass_4,
                    R.drawable.metal_1, R.drawable.metal_2, R.drawable.metal_3,
                    R.drawable.paper_1, R.drawable.paper_2, R.drawable.papel_3,
                    R.drawable.plastic_1, R.drawable.plastic_2, R.drawable.plastic_3};
    int btnCerto, pontosReciclagem, qtd;

    Button btnAzul, btnVermelho, btnAmarelo, btnVerde;
    Button[] btns = new Button[4];

    SharedPreferences preferences;
    String nomeFilho, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciclagem);

        img = (ImageView) findViewById(R.id.imgLixo);

        btnAmarelo = (Button) findViewById(R.id.btnAmarelo);
        btnAzul = (Button) findViewById(R.id.btnAzul);
        btnVerde = (Button) findViewById(R.id.btnVerde);
        btnVermelho = (Button) findViewById(R.id.btnVermelho);

        btns[0] = btnAmarelo;
        btns[1] = btnAzul;
        btns[2] = btnVermelho;
        btns[3] = btnVerde;

        preferences = getApplicationContext().getSharedPreferences("minhaShared",MODE_PRIVATE);

        email = preferences.getString("email", null);
        nomeFilho = preferences.getString("nomeFilho", null);

        email  = "sasa";
        nomeFilho = "Henrique";

        construirJogo();

        for(int i = 0; i< btns.length; i++)
        {
            final int id = i;
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verResult(btns[id].getId());
                }
            });
        }
    }

    public  void  construirJogo() {
        Random random = new Random();
        int val = random.nextInt(imagens.length);

        img.setImageResource(imagens[val]);

        if(val >= 0 && val <= 3)
            btnCerto = btnVerde.getId();
        else if(val >= 4 && val <= 6)
            btnCerto = btnAmarelo.getId();
        else  if(val >= 7 && val <= 9)
            btnCerto = btnAzul.getId();
        else btnCerto = btnVermelho.getId();
    }
    public  void  verResult(int id) {
        if(id == btnCerto)
            pontosReciclagem++;
        atualizar();
    }
    public  void  atualizar(){
        if(qtd < 9) {
            qtd++;
            construirJogo();
        }else {
            float pontoFinal = 0.0f;

            if(pontosReciclagem >= 0 && pontosReciclagem <= 2) pontoFinal = -0.05f;
            else if(pontosReciclagem == 3 || pontosReciclagem == 4)  pontoFinal = 0f;
            else if(pontosReciclagem >= 5 && pontosReciclagem <= 7) pontoFinal = 0.05f;
            else pontoFinal = 0.1f;

            UserService service =  RetrofitConfig.getClient().create(UserService.class);

            Call<List<Filho>> ponto = service.skill(email,nomeFilho,"rec", pontoFinal);

            ponto.enqueue(new Callback<List<Filho>>() {
                @Override
                public void onResponse(Call<List<Filho>> call, Response<List<Filho>> response) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReciclagemActivity.this);

                    builder.setTitle("Sua pontuação foi de "+ pontosReciclagem);

                    builder.setMessage("PARABÉNS, DEU CERTO");

                    builder.setNegativeButton("OK", null);

                    builder.create().show();
                }

                @Override
                public void onFailure(Call<List<Filho>> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReciclagemActivity.this);

                    AlertDialog alerta;

                    builder.setTitle("Erro com a pontuação");

                    builder.setMessage("Não foi possível enviar sua pontuação");

                    builder.setNegativeButton("OK", null);

                    alerta = builder.create();

                    alerta.show();
                }
            });
        }
    }
}