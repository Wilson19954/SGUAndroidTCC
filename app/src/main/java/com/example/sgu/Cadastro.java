package com.example.sgu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Date;

public class Cadastro extends AppCompatActivity {

    Button btCadastrar;
    ImageView imgcamera, imggaleria, imgFoto;
    EditText edtipouser, ednome, edendereco, edemail, eddocumento, edsenha, edbio, edtelefone;
    Bitmap fotoEscolhida;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //criando objetos para pegar os valores das caixas de texto, botoes, imagens...
        btCadastrar = findViewById(R.id.btcriarconta);
        edtipouser = findViewById(R.id.etTipoUser);
        ednome = findViewById(R.id.etNome);
        edendereco = findViewById(R.id.etEndereco);
        edemail = findViewById(R.id.etEmail);
        eddocumento = findViewById(R.id.etCpfcnpj);
        edsenha = findViewById(R.id.etSenha);
        edbio = findViewById(R.id.etBio);
        edtelefone = findViewById(R.id.etTelefone);
        imgcamera = findViewById(R.id.imgcamera);
        imggaleria = findViewById(R.id.imggaleria);
        imgFoto = findViewById(R.id.imgFoto);


        //Evento do botão cadastrar
        //Será necessário recuperar e validar os dados digitados em cada campo e enviá-los ao webservice
        btCadastrar.setOnClickListener(view -> {
            //Verificar se algum campo ficou sem preencher
            if(camposVazios()){
                Toast.makeText(Cadastro.this, "Verifique se ficou algum campo vazio", Toast.LENGTH_SHORT).show();
            }else{
               enviarDadosWebservice();
            }
        });

        //Falta verificar se permite ligar a camera
        //Click para abrir a camera do celular
        imgcamera.setOnClickListener(view -> {
            //Abrir a câmera principal (traseira, por padrão) e esperar uma foto tirada dela (capturada)
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //O "resultadoCamera" será o objeto que fará a configuração do que irá
            //acontecer quando a foto for tirada
            resultadoCamera.launch(cameraIntent);
        });
        imggaleria.setOnClickListener(view -> {
           Intent galeriaIntent = new Intent(Intent.ACTION_GET_CONTENT);
           galeriaIntent.setType("image/*");
            resultadoCamera.launch(galeriaIntent);
        });

    }


    /*private void AlertCamera(String titulo, String mensagem){
        //Novo objeto da classe AlertDialog porém com a opção de configurar (Builder)
        AlertDialog.Builder configAlert = new AlertDialog.Builder(this);
        configAlert.setTitle(titulo); //Configura o título
        configAlert.setMessage(mensagem); //Configura a mensagem interna

        //Botões do AlertDialog
        configAlert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(it);
            }
        });
        configAlert.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.cancel(); //Remove o AlertDialog da tela
            }
        });
        //Iniciar o AlertDialog com as configurações acima
        alert = configAlert.create();
        alert.show(); //Exibir o AlertDialog
    }*/

   /* private void AlertGaleria(String titulo, String mensagem){
        //Novo objeto da classe AlertDialog porém com a opção de configurar (Builder)
        AlertDialog.Builder configAlert = new AlertDialog.Builder(this);
        configAlert.setTitle(titulo); //Configura o título
        configAlert.setMessage(mensagem); //Configura a mensagem interna

        //Botões do AlertDialog
        configAlert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                it.setType("image/*");
                startActivity(it);
            }
        });
        configAlert.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.cancel(); //Remove o AlertDialog da tela
            }
        });
        //Iniciar o AlertDialog com as configurações acima
        alert = configAlert.create();
        alert.show(); //Exibir o AlertDialog
    }*/

    private void enviarDadosWebservice(){
        //Indicando que irá utilizar o webservice rodando no localhost do computador
        String url = "http://10.0.2.2:5000/api/Usuario";

        try {
            //Criar um objeto que irá transformar os dados preenchidos na tela em JSON
            JSONObject dadosEnvio = new JSONObject();
            //O nome dos parâmetros precisam ser iguais ao que o webservice espera receber
            //no nosso caso são "nome", "email", "senha", "fotoPerfil" e "dataNascimento"
            dadosEnvio.put("nome", ednome.getText().toString());
            dadosEnvio.put("email", edemail.getText().toString());
            dadosEnvio.put("senha", edsenha.getText().toString());
            dadosEnvio.put("endereco", edendereco.getText().toString());
            dadosEnvio.put("telefone", edtelefone.getText().toString());
            dadosEnvio.put("desc", edbio.getText().toString());
            dadosEnvio.put("doc", eddocumento.getText().toString());
            dadosEnvio.put("tipo", edtipouser.getText().toString());

            //Converter a imagem de Bitmap para String no formato Base64
            //Objeto para poder converter a imagem em vetor de byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //Populando o "stream" com o conteúdo da imagem
            fotoEscolhida.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //Convertendo o "stream" para um vetor de byte
            byte[] imagemEmByte = stream.toByteArray();
            //Converter o vetor de byte para Base64
            String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
            dadosEnvio.put("img", imagemEmString);


            //Converter a data do formato brasileiro (dia-mês-ano) para o formato
            //inglês (ano-mês-dia) que o banco de dados trabalha
            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //Converter um objeto do tipo Calendar para Date
            //Date dataFormatada = dataNascimento.getTime();
            //Converte a data
            //String dataEnvio = df.format(dataFormatada);
            //dadosEnvio.put("dataNascimento", dataEnvio);

            //Configurar a requisição que será enviada ao webservice
            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.POST,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Snackbar.make(findViewById(R.id.telaCadastro), R.string.avisoOk, Snackbar.LENGTH_SHORT).show();
                                    limparCampos();
                                    imgFoto.setImageBitmap(null);
                                    imgFoto.setImageResource(R.drawable.ic_enviar);
                                }else{
                                    Snackbar.make(findViewById(R.id.telaCadastro), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.telaCadastro), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.telaCadastro), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue requisicao = Volley.newRequestQueue(Cadastro.this);
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
                imgFoto.setImageBitmap(fotoEscolhida);
                imgFoto.setRotation(90);
            }
        }
    });
    private boolean camposVazios(){
        //Criar um objeto do ConstraintLayout que é aonde todos os EditTexts estão
        ConstraintLayout telaComponentes = findViewById(R.id.telaCadastro);

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
        ConstraintLayout telaComponentes = findViewById(R.id.telaCadastro);

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