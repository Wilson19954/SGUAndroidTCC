package com.example.sgu.telas;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
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
import com.example.sgu.classes.Galeria;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TelaAdicionarProjeto extends AppCompatActivity {

    private EditText edNomeProjeto, edCustoProjeto, edDescricaoProjeto;
    private Spinner spnCategoriaProjeto;
    private Button btCadastrarProjeto, btTeste;
    private Bitmap fotoEscolhida, fotoBuscada;
    private String document;
    private ImageView imgCam, imgProjeto, imgGaleria;
    private List<Uri> listaImagens = new ArrayList<>();
    private AlertDialog alert;

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
        btTeste = findViewById(R.id.btTeste);

        imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertGaleria("Abrir Galeria", "Autoriza o aplicativo acessar sua galeria de fotos?");
            }
        });

        imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertCamera("Abrir camera", "Autoriza o aplicativo acessar sua câmera?");
            }
        });

        btCadastrarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isEmpty(edNomeProjeto) || isEmpty(edCustoProjeto)  || isEmpty(edDescricaoProjeto)){
                   Toast.makeText(TelaAdicionarProjeto.this, "Certifique-se de preencher todos os campos", Toast.LENGTH_SHORT).show();
               }else{
                   if(fotoEscolhida == null && fotoBuscada == null){
                       Toast.makeText(TelaAdicionarProjeto.this, "Escolha uma imagem principal para seu projeto", Toast.LENGTH_SHORT).show();
                   } else {
                       enviarDadosWebservice();
                   }
               }
                enviarDadosWebservice();
            }
        });

        btTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaImagens.size() == 0){
                    Toast.makeText(TelaAdicionarProjeto.this, "Nâo há imagens para cadastrar", Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        JSONArray arrayFotos = new JSONArray();
                        JSONObject cadaFoto = new JSONObject();

                        //Converter as imagens para Base64
                        for (Uri uri : listaImagens) {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , uri);

                            //Converter a imagem de Bitmap para String no formato Base64
                            //Objeto para poder converter a imagem em vetor de byte
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            //Populando o "stream" com o conteúdo da imagem
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            //Convertendo o "stream" para um vetor de byte
                            byte[] imagemEmByte = stream.toByteArray();
                            //Converter o vetor de byte para Base64
                            String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
                            //Adicionar na lista

                            stream.flush();
                            stream.reset();
                            Log.d("TESTEIMG", " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + imagemEmString);

                            cadaFoto.put("foto", imagemEmString);
                            arrayFotos.put(cadaFoto);
                        }

                        JSONObject dados = new JSONObject();
                        dados.put("Galeria", arrayFotos);

                        String url = "http://10.0.2.2:5000/api/Galeria";

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dados, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getInt("status") == 200) {
                                        Toast.makeText(TelaAdicionarProjeto.this, "Fotos cadastradas", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(TelaAdicionarProjeto.this, "Erro", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Log.d("TESTEIMG", e.getMessage());
                                    Log.d("TESTEIMG", e.getCause().getMessage());
                                    e.printStackTrace();
                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(TelaAdicionarProjeto.this, "Erro", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });

                        RequestQueue requisicao = Volley.newRequestQueue(TelaAdicionarProjeto.this);
                        requisicao.add(request);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

            /*
            //Para cada imagem na lista ele repete o processo
            for(Uri imagem : listaImagens){
                //transforma imagemURI em BITMAP
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagem);

                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                //tranforma imagem bitmap em png
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                //tranformando imagem em byte
                byte[] imagemEmByte2 = stream2.toByteArray();
                //transformando a imagem em string
                String imagemEmString2 = Base64.encodeToString(imagemEmByte2, Base64.DEFAULT);
                //adicionando a imagem em string em uma lista do tipo String
                listaImagensString.add(imagemEmString2);
            }

            dadosEnvio.put("listaimg", listaImagensString);
            //dadosEnvio.put("img", String.valueOf(listaImagensString));*/

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

    private void AlertCamera(String titulo, String mensagem){
        AlertDialog.Builder configAlert = new AlertDialog.Builder(this);
        configAlert.setTitle(titulo);
        configAlert.setMessage(mensagem);

        configAlert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultadoCamera.launch(cameraIntent);
            }
        });
        configAlert.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.cancel();
            }
        });
        alert = configAlert.create();
        alert.show();
    }

    private void AlertGaleria(String titulo, String mensagem){
        AlertDialog.Builder configAlert = new AlertDialog.Builder(this);
        configAlert.setTitle(titulo);
        configAlert.setMessage(mensagem);

        configAlert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentGaleria = new Intent();
                //Comando para permitir que seja selecionada mais de uma foto por vez
                intentGaleria.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intentGaleria.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intentGaleria.setType("image/*");
                resultadoGaleria.launch(intentGaleria);
            }
        });
        configAlert.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.cancel();
            }
        });
        alert = configAlert.create();
        alert.show();
    }

    private boolean isEmpty(EditText etText) {
        String text = etText.getText().toString().trim();
        if (text.length()<1)
            return true;
        return false;
    }

}