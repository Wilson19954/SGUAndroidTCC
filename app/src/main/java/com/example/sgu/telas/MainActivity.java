package com.example.sgu.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.example.sgu.R;
import com.example.sgu.adapter.PublicacoesAdapter;
import com.example.sgu.classes.Publi;
import com.example.sgu.classes.Usuario;

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

    List<Publi> listaPubli = new ArrayList<>();
    List<Usuario> listaUsuario = new ArrayList<>();
    ImageView adicionarPub, iconPerfil, imgLog;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    TextView nomePerfil, tipoUser;
    String document;

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        enviarDadosPubWebservice();
        dadosSessaoWebservice();
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
        tipoUser = findViewById(R.id.tipoUser);
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

        /*swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               swipe.setRefreshing(false);
               RearrangeItems();
            }
        });*/

    }
    private void enviarDadosPubWebservice(){
        String url = "http://10.0.2.2:5000/api/Publicacoes/";
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
                                    dataFinal = format.parse(object.getString("data_pub"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Publi publi = new Publi(
                                        object.getString("img_pub"),
                                        object.getString("desc_pub"),
                                        object.getInt("like"),
                                        object.getString("tag_pub"),
                                        dataFinal.getTime(),
                                        object.getString("nome_user"),
                                        object.getString("doc_user"),
                                        object.getString("img_user"),
                                        object.getString("tipo_user"),
                                        object.getString("cod_pub"));
                                listaPubli.add(publi);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PublicacoesAdapter adapter = new PublicacoesAdapter(listaPubli, MainActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            }
        });
        solicitacao.add(envio);
    }

    private void dadosSessaoWebservice(){
        recuperarDados();
        String url = "http://10.0.2.2:5000/api/Usuario/buscar/" + document;
        RequestQueue solicitacao = Volley.newRequestQueue(this);
        JsonArrayRequest envio = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0 ; i< response.length() ; i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Usuario u = new Usuario(object.getString("endereco"),
                                        object.getString("nome"),
                                        object.getString("desc"),
                                        object.getString("doc"),
                                        object.getString("telefone"),
                                        object.getString("email"),
                                        object.getString("img"),
                                        object.getString("senha"),
                                        object.getString("tipo"));
                                listaUsuario.add(u);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            nomePerfil.setText(listaUsuario.get(i).getNome());
                            tipoUser.setText(listaUsuario.get(i).getTipo());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            }
        });
        solicitacao.add(envio);
    }

    private void recuperarDados(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        document = sharedPref.getString("doc","");
    }

    public void RearrangeItems() {
        Collections.shuffle(listaPubli, new Random(System.currentTimeMillis()));
        PublicacoesAdapter adapter = new PublicacoesAdapter(listaPubli, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void filterList(String text) {
        List<Publi> filteredList = new ArrayList<>();
        for(Publi pub : listaPubli){
            if(pub.getTag_pub().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(pub);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "Pesquisa não encontrada!", Toast.LENGTH_SHORT).show();
        }else{
            PublicacoesAdapter adapter = new PublicacoesAdapter(listaPubli, MainActivity.this);
            adapter.setFilteredList(filteredList);
            recyclerView.setAdapter(adapter);
        }
    }
}