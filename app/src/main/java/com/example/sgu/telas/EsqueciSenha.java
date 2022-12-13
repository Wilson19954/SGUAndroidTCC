package com.example.sgu.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class EsqueciSenha extends AppCompatActivity {

    Button btEnviar,btRecuperarSenha;
    EditText enviarEmail, digitarToken;
    String email, token, emailRecuperado;
    TextView txtToken, txt1, txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);


        btEnviar = findViewById(R.id.btEnviarToken);
        btRecuperarSenha = findViewById(R.id.btRecuperarSenha);
        enviarEmail = findViewById(R.id.emailToken);
        digitarToken = findViewById(R.id.edDigitarToken);
        txtToken = findViewById(R.id.txtToken);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(enviarEmail)){
                    salvarEmail();
                    enviarEmailWebService();

                    btRecuperarSenha.setEnabled(true);
                    digitarToken.setEnabled(true);
                    txtToken.setTextColor(Color.parseColor("#FFFFFF"));

                    btEnviar.setEnabled(false);
                    enviarEmail.setEnabled(false);
                    txt1.setTextColor(Color.parseColor("#676767"));
                    txt2.setTextColor(Color.parseColor("#676767"));

                } else {
                    Toast.makeText(EsqueciSenha.this, "Digite seu email", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(digitarToken)){
                    enviarTokenWebService();
                } else {
                    Toast.makeText(EsqueciSenha.this, "Não deixe o token vazio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enviarEmailWebService(){
        recuperarEmail();
        String url = "http://10.0.2.2:5000/api/Usuario/enviar/"+ emailRecuperado;
        try {
            JSONObject dadosEnvio = new JSONObject();

            dadosEnvio.put("email", emailRecuperado);

            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.PUT,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Snackbar.make(findViewById(R.id.TelaForgotPass), "Token Enviado", Snackbar.LENGTH_SHORT).show();
                                }else{
                                    Snackbar.make(findViewById(R.id.telaLogin),R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.TelaForgotPass), "Erro", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.TelaForgotPass), "Token Enviado", Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue requisicao = Volley.newRequestQueue(EsqueciSenha.this);
            requisicao.add(configRequisicao);
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    private void enviarTokenWebService(){
        recuperarEmail();
        token = digitarToken.getText().toString();
        String url = "http://10.0.2.2:5000/api/Usuario/token/"+token+"/"+emailRecuperado;
        try {
            JSONObject dadosEnvio = new JSONObject();

            dadosEnvio.put("token", digitarToken.getText().toString());

            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.POST,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    startActivity(new Intent(EsqueciSenha.this, TelaRecuperarSenha.class));
                                }else{
                                    Snackbar.make(findViewById(R.id.TelaForgotPass),R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.TelaForgotPass), "Erro1", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.TelaForgotPass), "Token Inválido", Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue requisicao = Volley.newRequestQueue(EsqueciSenha.this);
            requisicao.add(configRequisicao);
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    private void salvarEmail(){
        email = enviarEmail.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("emailToken", email);
        editor.apply();
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