package com.example.sgu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
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
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<Publicacoes> listaPublicacoes = new ArrayList<>();
    ImageView adicionarPub, iconPerfil, imgLog;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    TextView nomePerfil;
    ImageButton like;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        imgLog = findViewById(R.id.imgLogo);
        nomePerfil = findViewById(R.id.txtnomeperfil);
        iconPerfil = findViewById(R.id.iconperfil);
        adicionarPub = findViewById(R.id.adicionarPub2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        swipe = findViewById(R.id.swipe);

        imgLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lManager.scrollToPositionWithOffset(0, 0);
            }
        });

        nomePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TelaPerfilP.class));
            }
        });

        iconPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TelaPerfilP.class));
            }
        });

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

    private void filterList(String text) {
        List<Publicacoes> filteredList = new ArrayList<>();
        for(Publicacoes pub : listaPublicacoes){
            if(pub.getTag().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(pub);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "no data Found", Toast.LENGTH_SHORT).show();
        }else{
            PublicacoesAdapter adapter = new PublicacoesAdapter(listaPublicacoes, MainActivity.this);
            adapter.setFilteredList(filteredList);
            recyclerView.setAdapter(adapter);
        }
    }
}