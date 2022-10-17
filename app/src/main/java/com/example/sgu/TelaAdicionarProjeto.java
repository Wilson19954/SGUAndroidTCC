package com.example.sgu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class TelaAdicionarProjeto extends AppCompatActivity {

    EditText edNomeProjeto, edCustoProjeto, edDescricaoProjeto, edDocUser, edCod;
    Spinner spnCategoriaProjeto;
    Button btCadastrarProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_projeto);

        edCod = findViewById(R.id.edCod);
        edNomeProjeto = findViewById(R.id.edNomeProj);
        edCustoProjeto = findViewById(R.id.edCustoProj);
        edDescricaoProjeto = findViewById(R.id.edDescProj);
        spnCategoriaProjeto = findViewById(R.id.spnCatProj);
        btCadastrarProjeto = findViewById(R.id.btCadProj);
        edDocUser = findViewById(R.id.edDocUsu);

        btCadastrarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDadosWebservice();
            }
        });
    }
    private void enviarDadosWebservice(){
        String url = "http://10.0.2.2:5000/api/Projetos";
        try {

            JSONObject dadosEnvio = new JSONObject();
            dadosEnvio.put("cod", edCod.getText().toString());
            dadosEnvio.put("desc", edDescricaoProjeto.getText().toString());
            dadosEnvio.put("custo", edCustoProjeto.getText().toString());
            dadosEnvio.put("tag",spnCategoriaProjeto.getSelectedItem().toString());
            dadosEnvio.put("nome", edNomeProjeto.getText().toString());
            dadosEnvio.put("doc_user", edDocUser.getText().toString());

            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.POST,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Snackbar.make(findViewById(R.id.telaProjetos), R.string.avisoOk, Snackbar.LENGTH_SHORT).show();
                                }else{
                                    Snackbar.make(findViewById(R.id.telaProjetos), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.telaProjetos), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.telaProjetos), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue requisicao = Volley.newRequestQueue(TelaAdicionarProjeto.this);
            requisicao.add(configRequisicao);

        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

}