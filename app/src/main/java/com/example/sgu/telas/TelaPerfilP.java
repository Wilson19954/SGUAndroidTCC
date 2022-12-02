package com.example.sgu.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.sgu.EditarCadastro;
import com.example.sgu.R;
import com.example.sgu.adapter.ProjetosAdapter;
import com.example.sgu.adapter.VPAdapter;
import com.example.sgu.classes.Projetos;
import com.example.sgu.classes.Usuario;
import com.example.sgu.fragments.GaleriaFragment;
import com.example.sgu.fragments.ProjetosFragment;
import com.example.sgu.fragments.PublicacoesFragment;
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

    TextView nomePerfil, descPerfil,endPerfil, telPerfil, emPerfil;
    ImageView imgPerfil, imgEditarPerfil;
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
        endPerfil = findViewById(R.id.enderecoPerfil);
        telPerfil = findViewById(R.id.telefonePerfil);
        emPerfil = findViewById(R.id.emailPerfil);
        imgEditarPerfil = findViewById(R.id.imgEditarPerfil);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new PublicacoesFragment(), "PUBS");
        vpAdapter.addFragment(new ProjetosFragment(), "PROJETOS");
        vpAdapter.addFragment(new GaleriaFragment(), "GALERIA");

        viewPager.setAdapter(vpAdapter);
        buscarUsuario();

        imgEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaPerfilP.this, EditarCadastro.class));
            }
        });

    }

    private void recuperarDados(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        document = sharedPref.getString("doc","");
    }

    private void buscarUsuario(){
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
                                        object.getString("tipo"),
                                        object.getString("senha"),
                                        object.getString("cod_ver"));
                                listaUsuario.add(u);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            byte[] converteBase64 = Base64.decode(listaUsuario.get(i).getImg(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);

                            imgPerfil.setImageBitmap(bitmap);
                            descPerfil.setText(listaUsuario.get(i).getDesc());
                            nomePerfil.setText(listaUsuario.get(i).getNome());
                            telPerfil.setText(listaUsuario.get(i).getTelefone());
                            endPerfil.setText(listaUsuario.get(i).getEndereco());
                            emPerfil.setText(listaUsuario.get(i).getEmail());

                            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString("emailuser", listaUsuario.get(i).getEmail());
                            editor.putString("nomeuser", listaUsuario.get(i).getNome());
                            editor.putString("senhauser", listaUsuario.get(i).getSenha());
                            editor.putString("telefoneuser", listaUsuario.get(i).getTelefone());
                            editor.putString("enderecouser", listaUsuario.get(i).getEndereco());
                            editor.putString("descricaouser", listaUsuario.get(i).getDesc());
                            editor.putString("cpfuser", listaUsuario.get(i).getDoc());

                            editor.apply();



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

    }

}