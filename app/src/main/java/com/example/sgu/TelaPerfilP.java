package com.example.sgu;

import static android.widget.TabHost.TabSpec.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TelaPerfilP extends AppCompatActivity {
    List<Usuario> listaUsuario = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    TextView nomePerfil, descPerfil;
    ImageView imgPerfil;

    String document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil_p);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        imgPerfil = findViewById(R.id.imgPerfil);
        nomePerfil = findViewById(R.id.nomePerfil);
        descPerfil = findViewById(R.id.descPerfil);
        recuperarDados();

        String url = "http://10.0.2.2:5000/api/Usuario/buscar/" +document;
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
                                        object.getString("tipo"),
                                        object.getString("senha"));
                                        listaUsuario.add(u);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            byte[] converteBase64 = Base64.decode(listaUsuario.get(i).getImg(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);
                            imgPerfil.setImageBitmap(bitmap);
                            descPerfil.setText(listaUsuario.get(i).getDesc());
                            nomePerfil.setText(listaUsuario.get(i).getNome());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(TelaPerfilP.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            }
        });
        solicitacao.add(envio);
        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new PublicacoesFragment(), "Pubs");
        vpAdapter.addFragment(new ProjetosFragment(), "Projetos");

        viewPager.setAdapter(vpAdapter);

    }
        private void recuperarDados(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        document = sharedPref.getString("doc","");
    }

}