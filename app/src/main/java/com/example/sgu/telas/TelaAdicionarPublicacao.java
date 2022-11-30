package com.example.sgu.telas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class TelaAdicionarPublicacao extends AppCompatActivity {

    private ImageView imgPub;
    private EditText edDesc;
    private Spinner spnCategoria;
    private Bitmap fotoEscolhida, fotoBuscada;
    private ImageView btEscolherFoto, btGaleria;
    private Button btPublicar;
    private String document;

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_publicacao);

        imgPub = findViewById(R.id.imgPublicacoes);
        edDesc = findViewById(R.id.edDesc);
        spnCategoria = findViewById(R.id.spnCategoria);
        btEscolherFoto = findViewById(R.id.btEscolherFoto);
        btPublicar = findViewById(R.id.btPublicar);
        btGaleria = findViewById(R.id.btGaleria);

        btEscolherFoto.setOnClickListener(view -> {
            AlertCamera("Abrir camera", "Autoriza o aplicativo acessar sua câmera?");
        });

        btPublicar.setOnClickListener(view -> {
            if(isEmpty(edDesc)){
                Toast.makeText(this, "Não deixe de escrever na sua publicação", Toast.LENGTH_SHORT).show();
            } else {
                if(fotoEscolhida == null && fotoBuscada == null){
                    Toast.makeText(this, "Escolha uma imagem para sua publicação", Toast.LENGTH_SHORT).show();
                } else {
                    enviarDadosWebservice();
                }
            }
        });
        btGaleria.setOnClickListener(view -> {
            AlertGaleria("Abrir Galeria", "Autoriza o aplicativo acessar sua galeria de fotos?");
        });

    }

    private void recuperarDados(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        document = sharedPref.getString("doc","");
    }

    private void enviarDadosWebservice(){
        recuperarDados();
        String url = "http://10.0.2.2:5000/api/Publicacoes";

        try {
            JSONObject dadosEnvio = new JSONObject();
            dadosEnvio.put("desc", edDesc.getText().toString());
            dadosEnvio.put("doc_user", document);
            dadosEnvio.put("tag",spnCategoria.getSelectedItem().toString());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //fotoEscolhida.compress(Bitmap.CompressFormat.PNG, 100, stream);
            fotoBuscada.compress(Bitmap.CompressFormat.PNG, 70, stream);
            byte[] imagemEmByte = stream.toByteArray();
            String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
            dadosEnvio.put("img", imagemEmString);

            //Configurar a requisição que será enviada ao webservice
            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.POST,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Snackbar.make(findViewById(R.id.telaPublicacoes), R.string.avisoOk, Snackbar.LENGTH_SHORT).show();
                                    limparCampos();
                                    imgPub.setImageBitmap(null);
                                    //imgPub.setImageResource(R.drawable.ic_enviar);
                                    startActivity(new Intent(TelaAdicionarPublicacao.this, MainActivity.class));
                                }else{
                                    Snackbar.make(findViewById(R.id.telaPublicacoes), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.telaPublicacoes), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.telaPublicacoes), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue requisicao = Volley.newRequestQueue(TelaAdicionarPublicacao.this);
            requisicao.add(configRequisicao);

        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    ActivityResultLauncher<Intent> resultadoCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                fotoEscolhida = (Bitmap) extras.get("data");
                imgPub.setImageBitmap(fotoEscolhida);
                imgPub.setRotation(90);
                imgPub.setVisibility(View.VISIBLE);
            }
        }
    });


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
                    imgPub.setImageBitmap(fotoBuscada);
                    imgPub.setVisibility(View.VISIBLE);
                }
            }
        }

    private void limparCampos(){
        ConstraintLayout telaComponentes = findViewById(R.id.telaPublicacoes);
        for (int i = 0; i < telaComponentes.getChildCount(); i++) {
            View view = telaComponentes.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
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