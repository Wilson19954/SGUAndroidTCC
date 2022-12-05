package com.example.sgu.telas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelaEditarProjeto extends AppCompatActivity {

    private EditText edNomeProjeto, edCustoProjeto, edDescricaoProjeto;
    private Spinner spnCategoriaProjeto;
    private Button btAtualizarProjeto;
    private Bitmap fotoEscolhida, fotoBuscada;
    private String document;
    private ImageView imgCam, imgProjeto, imgGal, adcFotosGal;
    private AlertDialog alert;
    private String docuser, codproj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_editar_projeto);

        Intent valores = getIntent();
        docuser = valores.getStringExtra("docuser");
        codproj = valores.getStringExtra("codproj");
        String nomeproj = valores.getStringExtra("nomeproj");
        String descproj = valores.getStringExtra("descproj");
        String custoproj = valores.getStringExtra("custoproj");
        String fotoprincipalproj = valores.getStringExtra("fotoprincipalproj");

        edNomeProjeto = findViewById(R.id.edNomeProj);
        edCustoProjeto = findViewById(R.id.edCustoProj);
        edDescricaoProjeto = findViewById(R.id.edDescProj);
        spnCategoriaProjeto = findViewById(R.id.spnCatProj);
        btAtualizarProjeto = findViewById(R.id.btCadProj);
        imgCam = findViewById(R.id.imgCam);
        imgProjeto = findViewById(R.id.imgProjeto);
        imgGal = findViewById(R.id.imgGal);

        edNomeProjeto.setText(nomeproj);
        edCustoProjeto.setText(custoproj);
        edDescricaoProjeto.setText(descproj);

        byte[] converteBase64 = Base64.decode(fotoprincipalproj, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);
        imgProjeto.setImageBitmap(bitmap);


        imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertCameraPrincipal("Abrir câmera","Autoriza o aplicativo acessar sua câmera?");
            }
        });

        btAtualizarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(edNomeProjeto) || isEmpty(edCustoProjeto)  || isEmpty(edDescricaoProjeto)){
                    Toast.makeText(TelaEditarProjeto.this, "Certifique-se de preencher todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                    if(fotoEscolhida == null && fotoBuscada == null){
                        Toast.makeText(TelaEditarProjeto.this, "Escolha uma imagem principal para seu projeto", Toast.LENGTH_SHORT).show();
                    } else {
                        atualizarDadosWebservice();
                        finish();
                    }
                }
            }
        });

    }

    private void atualizarDadosWebservice(){
        String url = "http://10.0.2.2:5000/api/Projetos";

        try {
            JSONObject dadosEnvio = new JSONObject();
            dadosEnvio.put("cod", codproj);
            dadosEnvio.put("desc", edDescricaoProjeto.getText().toString());
            dadosEnvio.put("custo", edCustoProjeto.getText().toString());
            dadosEnvio.put("tag", spnCategoriaProjeto.getSelectedItem().toString());
            dadosEnvio.put("nome", edNomeProjeto.getText().toString());
            dadosEnvio.put("doc_user", docuser);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            fotoEscolhida.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imagemEmByte = stream.toByteArray();
            String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
            dadosEnvio.put("img", imagemEmString);

            //Configurar a requisição que será enviada ao webservice
            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.PUT,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Snackbar.make(findViewById(R.id.telaEditarProjeto), R.string.avisoOk, Snackbar.LENGTH_SHORT).show();
                                }else{
                                    Snackbar.make(findViewById(R.id.telaEditarProjeto), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.telaEditarProjeto), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.telaEditarProjeto), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );

            RequestQueue requisicao = Volley.newRequestQueue(TelaEditarProjeto.this);
            requisicao.add(configRequisicao);

        }catch (Exception exc){
            exc.printStackTrace();
        }
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

    private boolean isEmpty(EditText etText) {
        String text = etText.getText().toString().trim();
        if (text.length()<1)
            return true;
        return false;
    }


}