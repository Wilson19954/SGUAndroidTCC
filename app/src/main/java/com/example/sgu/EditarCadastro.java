package com.example.sgu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.snackbar.Snackbar;
import com.santalu.maskara.widget.MaskEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditarCadastro extends AppCompatActivity {

    private Button btEditar;
    private ImageView imgcamera, imggaleria, imgFoto;
    private MaskEditText eddocumento, edtelefone;
    private EditText ednome, edendereco, edemail, edsenha, edbio;
    private Spinner edtipouser;
    private Bitmap fotoEscolhida, fotoBuscada;
    private String emailuser, nomeuser, senhauser, telefoneuser, enderecouser, descuser, docuser;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cadastro);

        btEditar = findViewById(R.id.btcriarconta);
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

        recuperarDados();

        edemail.setText(emailuser);
        ednome.setText(nomeuser);
        //edsenha.setText(senhauser);
        edtelefone.setText(telefoneuser);
        edendereco.setText(enderecouser);
        edbio.setText(descuser);
        eddocumento.setHint(docuser);

        imgcamera.setOnClickListener(view -> {
            AlertCamera("Abrir camera", "Autoriza o aplicativo acessar sua câmera?");
        });

        imggaleria.setOnClickListener(view -> {
            AlertGaleria("Abrir Galeria", "Autoriza o aplicativo acessar sua galeria de fotos?");
        });

        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean preenchertel = edtelefone.isDone();
                if(isEmpty(ednome) || isEmpty(edendereco) || isEmpty(edemail) || isEmpty(edbio)){
                    Toast.makeText(EditarCadastro.this, "Verifique campos vazios", Toast.LENGTH_SHORT).show();
                } else {
                    if(!preenchertel){
                        Toast.makeText(EditarCadastro.this, "Complete todos os campos", Toast.LENGTH_SHORT).show();
                    } else {
                        if(isEmpty(edsenha)){
                            Toast.makeText(EditarCadastro.this, "Confirme sua senha", Toast.LENGTH_SHORT).show();
                        } else {
                        if(fotoEscolhida == null && fotoBuscada == null){
                            Toast.makeText(EditarCadastro.this, "Escolha uma nova foto para seu perfil", Toast.LENGTH_SHORT).show();
                        } else {
                            WebService();
                            finish();
                        }
                        }
                    }
                }
            }
        });
    }

    private void recuperarDados(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        emailuser = sharedPref.getString("emailuser","");
        nomeuser = sharedPref.getString("nomeuser","");
        senhauser = sharedPref.getString("senhauser","");
        telefoneuser = sharedPref.getString("telefoneuser","");
        enderecouser = sharedPref.getString("enderecouser","");
        descuser = sharedPref.getString("descricaouser","");
        docuser = sharedPref.getString("cpfuser","");
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

    ActivityResultLauncher<Intent> resultadoCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                fotoEscolhida = (Bitmap) extras.get("data");
                imgFoto.setImageBitmap(fotoEscolhida);
                imgFoto.setRotation(90);
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
                imgFoto.setImageBitmap(fotoBuscada);
                imgFoto.setVisibility(View.VISIBLE);
            }
        }
    }

    private void WebService(){
        String url = "http://10.0.2.2:5000/api/Usuario";
        try {
            JSONObject dadosEnvio = new JSONObject();

            dadosEnvio.put("nome", ednome.getText().toString());
            dadosEnvio.put("email", edemail.getText().toString());
            dadosEnvio.put("senha", edsenha.getText().toString());
            dadosEnvio.put("endereco", edendereco.getText().toString());
            dadosEnvio.put("telefone", edtelefone.getText().toString());
            dadosEnvio.put("desc", edbio.getText().toString());
            dadosEnvio.put("tipo",edtipouser.getSelectedItem().toString());
            dadosEnvio.put("doc", docuser);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if(fotoEscolhida != null){
                fotoEscolhida.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imagemEmByte = stream.toByteArray();
                String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
                dadosEnvio.put("img", imagemEmString);
            } else {
                fotoBuscada.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imagemEmByte = stream.toByteArray();
                String imagemEmString = Base64.encodeToString(imagemEmByte, Base64.DEFAULT);
                dadosEnvio.put("img", imagemEmString);
            }
            JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.PUT,
                    url, dadosEnvio,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status") == 200){
                                    Snackbar.make(findViewById(R.id.telaEditar), "Conta Atualizada com Sucesso!", Snackbar.LENGTH_SHORT).show();
                                    imgFoto.setImageBitmap(null);
                                }else{
                                    Snackbar.make(findViewById(R.id.telaEditar), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(findViewById(R.id.telaEditar), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(findViewById(R.id.telaEditar), R.string.avisoErro, Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );

            RequestQueue requisicao = Volley.newRequestQueue(EditarCadastro.this);
            requisicao.add(configRequisicao);

        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    private boolean isEmpty(EditText etText) {
        String text = etText.getText().toString().trim();
        if (text.length()<1)
            return true;
        return false;
    }
}