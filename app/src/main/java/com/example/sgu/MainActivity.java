package com.example.sgu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<Publicacoes> listaPublicacoes = new ArrayList<>();
    ImageView adicionarPub;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adicionarPub = findViewById(R.id.adicionarPub2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        swipe = findViewById(R.id.swipe);


        adicionarPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TelaAdicionarPublicacao.class));
            }
        });

        String url = "http://10.0.2.2:5000/api/Publicacoes";
        RequestQueue solicitacao = Volley.newRequestQueue(this);
        JsonArrayRequest envio = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0 ; i<response.length() ; i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date dataFinal = null;
                                try {
                                    dataFinal = format.parse(object.getString("data"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Publicacoes pub = new Publicacoes(object.getString("desc"),
                                        object.getInt("cod"),
                                        object.getString("doc_user"),
                                        object.getString("tag"),
                                        object.getString("like"),
                                        object.getString("img"),
                                        dataFinal.getTime());
                                listaPublicacoes.add(pub);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PublicacoesAdapter adapter = new PublicacoesAdapter(listaPublicacoes, MainActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            }
        }
        );
        solicitacao.add(envio);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                RearrangeItems();
            }
        });
    }
    public void RearrangeItems() {
        // Shuffling the data of ArrayList using system time
        Collections.shuffle(listaPublicacoes, new Random(System.currentTimeMillis()));
        PublicacoesAdapter adapter = new PublicacoesAdapter(listaPublicacoes, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }
}