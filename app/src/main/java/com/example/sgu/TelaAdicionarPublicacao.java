package com.example.sgu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TelaAdicionarPublicacao extends AppCompatActivity {

    ImageView imgPub;
    EditText edDesc, edDoc;
    Spinner spnCategoria;
    Bitmap fotoEscolhida;
    ImageButton btEscolherFoto;
    Button btPublicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_publicacao);

        imgPub = findViewById(R.id.imgPublicacoes);
        edDesc = findViewById(R.id.edDesc);
        edDoc = findViewById(R.id.edDoc);
        spnCategoria = findViewById(R.id.spnCategoria);
        btEscolherFoto = findViewById(R.id.btEscolherFoto);
        btPublicar = findViewById(R.id.btPublicar);

        btEscolherFoto.setOnClickListener(view -> {
            //Abrir a câmera principal (traseira, por padrão) e esperar uma foto tirada dela (capturada)
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //O "resultadoCamera" será o objeto que fará a configuração do que irá
            //acontecer quando a foto for tirada
            resultadoCamera.launch(cameraIntent);
        });

        btPublicar.setOnClickListener(view -> {
            //Verificar se algum campo ficou sem preencher
            if(camposVazios()){
                Toast.makeText(TelaAdicionarPublicacao.this, "Verifique se ficou algum campo vazio", Toast.LENGTH_SHORT).show();
            }else{
                    //Enviar dados para o webservice
                    enviarDadosWebservice();
            }
        });
    }
    private void enviarDadosWebservice(){
        //Indicando que irá utilizar o webservice rodando no localhost do computador
        String url = "http://10.0.2.2:5000/api/Publicacoes";

        try {
            JSONObject dadosEnvio = new JSONObject();
            dadosEnvio.put("desc", edDesc.getText().toString());
            dadosEnvio.put("doc_user", edDoc.getText().toString());
            dadosEnvio.put("tag",spnCategoria.getSelectedItem().toString());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            fotoEscolhida.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
                                    imgPub.setImageResource(R.drawable.ic_enviar);
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
            //Testar se há mesmo algo retornando da tela da câmera
            if(result.getResultCode() == RESULT_OK){
                //Pegamos o conteúdo enviado pela câmera, o qual está no objeto "result" do método acima
                Bundle extras = result.getData().getExtras();
                //Desse conteúdo inteiro, precisamos pegar um extra chamado "data"
                //que é onde a foto está armazenada e converter para Bitmap
                fotoEscolhida = (Bitmap) extras.get("data");

                //Colocar a imagem no ImageView da tela
                imgPub.setImageBitmap(fotoEscolhida);
                imgPub.setRotation(90);
            }
        }
    });

    private boolean camposVazios(){
        //Criar um objeto do ConstraintLayout que é aonde todos os EditTexts estão
        ConstraintLayout telaComponentes = findViewById(R.id.telaPublicacoes);

        //Laço para percorrer todos os componentes dentro do ConstraintLayout
        for (int i = 0; i < telaComponentes.getChildCount(); i++) {
            //Recupera o primeiro componente encontrado
            View view = telaComponentes.getChildAt(i);
            //Verifica se esse componente é um EditText
            if (view instanceof EditText) {
                if(((EditText) view).getText().toString().equals("")){
                    return true;
                }
            }
        }
        return false;
    }

    private void limparCampos(){
        //Criar um objeto do ConstraintLayout que é aonde todos os EditTexts estão
        ConstraintLayout telaComponentes = findViewById(R.id.telaPublicacoes);

        //Laço para percorrer todos os componentes dentro do ConstraintLayout
        for (int i = 0; i < telaComponentes.getChildCount(); i++) {
            //Recupera o primeiro componente encontrado
            View view = telaComponentes.getChildAt(i);
            //Verifica se esse componente é um EditText
            if (view instanceof EditText) {
                //Limpa o texto
                ((EditText) view).setText("");
            }
        }
    }

}