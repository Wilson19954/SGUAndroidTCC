package com.example.sgu.telas;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.R;
import com.example.sgu.adapter.ImagensAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TelaAdicionarProjeto extends AppCompatActivity {

    EditText edNomeProjeto, edCustoProjeto, edDescricaoProjeto, edDocUser, edCod;
    Spinner spnCategoriaProjeto;
    Button btCadastrarProjeto;
    Bitmap fotoEscolhida, fotoBuscada;
    String document;
    ImageView imgCam, imgProjeto, imgGaleria;
    ListView listaView;
    ArrayList<Uri> mArrayUri;
    //int PICK_IMAGE_MULTIPLE = 1;
    List<Uri> listaImagens = new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_projeto);

        edNomeProjeto = findViewById(R.id.edNomeProj);
        edCustoProjeto = findViewById(R.id.edCustoProj);
        edDescricaoProjeto = findViewById(R.id.edDescProj);
        spnCategoriaProjeto = findViewById(R.id.spnCatProj);
        btCadastrarProjeto = findViewById(R.id.btCadProj);
        imgCam = findViewById(R.id.imgCam);
        imgProjeto = findViewById(R.id.imgProjeto);
        imgGaleria = findViewById(R.id.imgGal);
        //listaView = findViewById(R.id.listaFotos);

        imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent galeriaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent, 1);*/

                //Abrir a câmera principal (traseira, por padrão) e esperar uma foto tirada dela (capturada)
                Intent intentGaleria = new Intent();

                //Comando para permitir que seja selecionada mais de uma foto por vez
                intentGaleria.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intentGaleria.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intentGaleria.setType("image/*");

                //O "resultadoCamera" será o objeto que fará a configuração do que irá
                //acontecer quando a foto for tirada
                resultadoGaleria.launch(intentGaleria);

            }
        });

        imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultadoCamera.launch(cameraIntent);
            }
        });

        btCadastrarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDadosWebservice();
            }
        });

        /*List<String> listaFotos = new ArrayList<String>();

        String nome = "wilson";
        String nome2 = "jose";

        listaFotos.add(nome);
        listaFotos.add(nome2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaFotos);
        listaView.setAdapter(adapter);*/


    }

    ActivityResultLauncher<Intent> resultadoCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                fotoEscolhida = (Bitmap) extras.get("data");
                imgProjeto.setImageBitmap(fotoEscolhida);
                imgProjeto.setRotation(90);
                imgProjeto.setVisibility(View.VISIBLE);
            }
        }
    });

    ActivityResultLauncher<Intent> resultadoGaleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                if(result.getData().getClipData() != null){
                    //Cria um objeto do ClipData para recuperar todas as imagens
                    ClipData clipData = result.getData().getClipData();
                    //Verifica a quantidade de imagens selecionadas
                    int totalImagens = clipData.getItemCount();

                    for (int i=0 ; i<totalImagens ; i++){
                        //Recuperar cada imagem selecionada
                        Uri imageurl = clipData.getItemAt(i).getUri();
                        listaImagens.add(imageurl);
                    }
              } else {
                    //Se houver somente uma imagem
                    Uri imageurl = result.getData().getData();
                    listaImagens.add(imageurl);
                }
                //Inicia o RecylerView
                RecyclerView recyclerView = findViewById(R.id.recyclerImagens);
                //Atribui o layout do tipo Horizontal ao recyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),  LinearLayoutManager.HORIZONTAL, false));

                //Inicia o Adapter das imagens com a lista de imagens
                ImagensAdapter imgAdapter = new ImagensAdapter(listaImagens);

                recyclerView.setAdapter(imgAdapter);
            }
        }
    });

    private void recuperarDados(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        document = sharedPref.getString("doc","");
    }

    private void enviarDadosWebservice(){
        recuperarDados();
        String url = "http://10.0.2.2:5000/api/Projetos";
        try {

            JSONObject dadosEnvio = new JSONObject();
            dadosEnvio.put("cod", "");
            dadosEnvio.put("desc", edDescricaoProjeto.getText().toString());
            dadosEnvio.put("custo", edCustoProjeto.getText().toString());
            dadosEnvio.put("tag",spnCategoriaProjeto.getSelectedItem().toString());
            dadosEnvio.put("nome", edNomeProjeto.getText().toString());
            dadosEnvio.put("doc_user", document);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            fotoEscolhida.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imagemEmByte = stream.toByteArray();
            String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
            dadosEnvio.put("img", imagemEmString);



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