package com.example.sgu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Cadastro extends AppCompatActivity {

    Button criarconta;
    ImageView imgcamera, imggaleria;
    EditText cadastrartipouser, cadastrarnome, cadastrartelefone, cadastraremail, cadastrardocumento, cadastrarsenha, cadastrarbio;
    //atributo da classe.
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //criando objetos para pegar os valores das caixas de texto, botoes, imagens...
        criarconta = findViewById(R.id.btcriarconta);
        cadastrartipouser = findViewById(R.id.etTipoUser);
        cadastrarnome = findViewById(R.id.etNome);
        cadastrartelefone = findViewById(R.id.etTelefone);
        cadastraremail = findViewById(R.id.etEmail);
        cadastrardocumento = findViewById(R.id.etCpfcnpj);
        cadastrarsenha = findViewById(R.id.etSenha);
        cadastrarbio = findViewById(R.id.etBio);
        imgcamera = findViewById(R.id.imgcamera);
        imggaleria = findViewById(R.id.imggaleria);


        criarconta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iniciando activity nova
                startActivity(new Intent(Cadastro.this, Login.class));
                //recuperando a string digitada e colocando na variavel nome depois dele ter digitado e clicado no botão.
                String nome = cadastrarnome.getText().toString();
                //texto indicando o nome do user para teste
                Toast.makeText(Cadastro.this,"Nome:" + nome , Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //Falta verificar se permite ligar a camera
        //Click para abrir a camera do celular
        imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertCamera("ACESSAR CAMERA", "Autoriza o aplicativo acessar sua camera?");
            }
        });

        imggaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertGaleria("ACESSAR GALERIA","Autoriza o aplicativo acessar sua galeria?");
            }
        });
    }


    private void AlertCamera(String titulo, String mensagem){
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
    }

    private void AlertGaleria(String titulo, String mensagem){
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
    }
}