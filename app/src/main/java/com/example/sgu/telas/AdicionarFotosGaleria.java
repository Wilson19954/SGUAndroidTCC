package com.example.sgu.telas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.R;
import com.example.sgu.adapter.ImagensAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdicionarFotosGaleria extends AppCompatActivity {

    private Button btEnviarFotos;
    private ImageView imgGaleriaG, imgCamGaleria;
    private AlertDialog alert;
    private List<Uri> listaImagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_fotos_galeria);

        imgGaleriaG = findViewById(R.id.imgGaleriaG);
        imgCamGaleria = findViewById(R.id.imgCamGaleria);
        btEnviarFotos= findViewById(R.id.btTeste);

        imgGaleriaG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertGaleria("Abrir Galeria", "Autoriza o aplicativo acessar sua galeria de fotos?");
            }
        });

        imgCamGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertCamera("Abrir câmera", "Autoriza o aplicativo acessar sua câmera?");
            }
        });


        btEnviarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               enviarlistaGaleriaWebService();
            }
        });
    }
    private void enviarlistaGaleriaWebService(){
        if(listaImagens.size() == 0){
            Toast.makeText(AdicionarFotosGaleria.this, "Nâo há imagens para cadastrar", Toast.LENGTH_SHORT).show();
        }else{
            try{
                JSONArray arrayFotos = new JSONArray();
                JSONObject cadaFoto = new JSONObject();
                for (Uri uri : listaImagens) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] imagemEmByte = stream.toByteArray();
                    String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
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
                                Toast.makeText(AdicionarFotosGaleria.this, "Fotos cadastradas", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AdicionarFotosGaleria.this, "Erro", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AdicionarFotosGaleria.this, "Erro", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        });
                RequestQueue requisicao = Volley.newRequestQueue(AdicionarFotosGaleria.this);
                requisicao.add(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    ActivityResultLauncher<Intent> resultadoCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Uri imageurl = result.getData().getData();
                listaImagens.add(imageurl);
                RecyclerView recyclerView = findViewById(R.id.recyclerImagens);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),  LinearLayoutManager.HORIZONTAL, false));
                ImagensAdapter imgAdapter = new ImagensAdapter(listaImagens);
                recyclerView.setAdapter(imgAdapter);
            }
        }
    });

    ActivityResultLauncher<Intent> resultadoGaleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                if(result.getData().getClipData() != null){
                    ClipData clipData = result.getData().getClipData();
                    int totalImagens = clipData.getItemCount();
                    for (int i=0 ; i<totalImagens ; i++){
                        Uri imageurl = clipData.getItemAt(i).getUri();
                        listaImagens.add(imageurl);
                    }
                } else {
                    Uri imageurl = result.getData().getData();
                    listaImagens.add(imageurl);
                }
                RecyclerView recyclerView = findViewById(R.id.recyclerImagens);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),  LinearLayoutManager.HORIZONTAL, false));
                ImagensAdapter imgAdapter = new ImagensAdapter(listaImagens);
                recyclerView.setAdapter(imgAdapter);
            }
        }
    });

}