package com.example.sgu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.telas.TelaLogin;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class TelaRecuperarSenha extends AppCompatActivity {

    String emailRecuperado, novasenha;
    Button btMudarSenha;
    EditText edNovaSenha, edNovaSenha2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_recuperar_senha);

        btMudarSenha = findViewById(R.id.btMudarSenha);
        edNovaSenha = findViewById(R.id.edDigitarNovaSenha);
        edNovaSenha2 = findViewById(R.id.edDigitarNovaSenha2);

        btMudarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(edNovaSenha) && !isEmpty(edNovaSenha2)){
                    if(!edNovaSenha.getText().toString().equals(edNovaSenha2.getText().toString())){
                        Toast.makeText(TelaRecuperarSenha.this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
                    }else{
                        enviarNovaSenhaWebService();
                    }
                }
            }
        });

    }

    private void enviarNovaSenhaWebService(){
        recuperarEmail();
        novasenha = edNovaSenha.getText().toString();

        String url = "http://10.0.2.2:5000/api/Usuario/recuperar/"+novasenha+"/"+emailRecuperado;
        try {
            JSONObject dadosEnvio = new JSONObject();

            dadosEnvio.put("novasenha", novasenha);
            dadosEnvio.put("email", emailRecuperado);

            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.PUT,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Toast.makeText(TelaRecuperarSenha.this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(TelaRecuperarSenha.this, TelaLogin.class));
                                }else{
                                    Snackbar.make(findViewById(R.id.telaRecuperarSenha),R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.telaRecuperarSenha), "Erro1", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.telaRecuperarSenha), "Erro2", Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue requisicao = Volley.newRequestQueue(TelaRecuperarSenha.this);
            requisicao.add(configRequisicao);
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    private void recuperarEmail(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        emailRecuperado = sharedPref.getString("emailToken","");
    }

    private boolean isEmpty(EditText etText) {
        String text = etText.getText().toString().trim();
        if (text.length()<1)
            return true;
        return false;
    }
}