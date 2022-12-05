package com.example.sgu.telas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TelaAdicionarProjeto extends AppCompatActivity {

    private EditText edNomeProjeto, edCustoProjeto, edDescricaoProjeto;
    private Spinner spnCategoriaProjeto;
    private Button btCadastrarProjeto;
    private Bitmap fotoEscolhida, fotoBuscada;
    private String document;
    private ImageView imgCam, imgProjeto, imgGal, adcFotosGal;
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
        imgGal = findViewById(R.id.imgGal);

        adcFotosGal = findViewById(R.id.adicionarFotosGaleria);

        adcFotosGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaAdicionarProjeto.this, AdicionarFotosGaleria.class));
            }
        });

        imgGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertGaleriaPrincipal("Abrir Galeria", "Autoriza o aplicativo acessar sua galeria de fotos?");
            }
        });

        imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertCameraPrincipal("Abrir câmera","Autoriza o aplicativo acessar sua câmera?");
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
                       adcFotosGal.setVisibility(View.VISIBLE);
                   }
               }
            }
        });

    }

    ActivityResultLauncher<Intent> cameraImgPrincipal = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                fotoEscolhida = (Bitmap) extras.get("data");
                imgProjeto.setImageBitmap(fotoEscolhida);
                imgProjeto.setVisibility(View.VISIBLE);
            }
        }
    });

    //evento para abrir galeria e escolher foto unica
    public void onActivityResult(int requestCode, int resultCode, Intent dados) {
        super.onActivityResult(requestCode, resultCode, dados);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri imageuri = dados.getData();
                fotoBuscada = null;
                try {
                    fotoBuscada = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgProjeto.setImageBitmap(fotoBuscada);
                imgProjeto.setVisibility(View.VISIBLE);
            }
        }
    }


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

            if(fotoEscolhida != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                fotoEscolhida.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imagemEmByte = stream.toByteArray();
                String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);

                dadosEnvio.put("img", imagemEmString);
            } else if(fotoBuscada != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                fotoBuscada.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imagemEmByte = stream.toByteArray();
                String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
                dadosEnvio.put("img", imagemEmString);
            }

            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.POST,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Toast.makeText(TelaAdicionarProjeto.this, "Projeto Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
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

    private void AlertCameraPrincipal(String titulo, String mensagem){
        AlertDialog.Builder configAlert = new AlertDialog.Builder(this);
        configAlert.setTitle(titulo);
        configAlert.setMessage(mensagem);

        configAlert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraImgPrincipal.launch(cameraIntent);
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

    private void AlertGaleriaPrincipal(String titulo, String mensagem){
        AlertDialog.Builder configAlert = new AlertDialog.Builder(this);
        configAlert.setTitle(titulo);
        configAlert.setMessage(mensagem);

        configAlert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent galeriaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent, 1);
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