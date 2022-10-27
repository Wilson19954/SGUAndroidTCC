package com.example.sgu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class TelaLogin extends AppCompatActivity {

    Button btLogin;
    TextView criarconta;
    EditText edDocLogin;
    EditText edSenhaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        criarconta = findViewById(R.id.txtCriarconta);
        edDocLogin = findViewById(R.id.edDocLogin);
        edSenhaLogin = findViewById(R.id.edSenhaLogin);
        btLogin = findViewById(R.id.btLogin);

        criarconta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaLogin.this, TelaCadastro.class));
            }
        });

        btLogin.setOnClickListener(view -> {
            salvarDados();
            startActivity(new Intent(TelaLogin.this, MainActivity.class));
            //enviarDadosWebservice();
        });
    }

    /*private void enviarDadosWebservice(){
        String url = "http://10.0.2.2:5000/api/Usuario/login";
        try {
            JSONObject dadosEnvio = new JSONObject();
            dadosEnvio.put("doc", edDocLogin.getText().toString());
            dadosEnvio.put("senha", edSenhaLogin.getText().toString());
            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.POST,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Snackbar.make(findViewById(R.id.telaLogin), R.string.avisoAcessoOk, Snackbar.LENGTH_SHORT).show();
                                    Intent it = new Intent(TelaLogin.this, MainActivity.class);
                                    startActivity(it);
                                }else{
                                    Snackbar.make(findViewById(R.id.telaLogin), R.string.avisoAcessoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.telaLogin), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.telaLogin), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue requisicao = Volley.newRequestQueue(TelaLogin.this);
            requisicao.add(configRequisicao);
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }*/

    private void salvarDados(){
        String document = edDocLogin.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("doc", document);
        editor.commit();
    }
}