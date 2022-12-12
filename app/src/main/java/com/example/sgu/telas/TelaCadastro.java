package com.example.sgu.telas;

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
import com.santalu.maskara.widget.MaskEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TelaCadastro extends AppCompatActivity {

    private Button btCadastrar;
    private ImageView imgcamera, imggaleria, imgFoto;
    private MaskEditText eddocumento, edtelefone;
    private EditText  ednome, edendereco, edemail, edsenha, edbio;
    private Spinner edtipouser;
    private Bitmap fotoEscolhida, fotoBuscada;

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

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

        btCadastrar.setOnClickListener(view -> {
            Boolean preencherdoc = eddocumento.isDone();
            Boolean preenchertel = edtelefone.isDone();

            if(isEmpty(ednome) || isEmpty(edendereco) || isEmpty(edemail) || isEmpty(edbio) || isEmpty(edsenha) ){
                Toast.makeText(this, "Verifique campos vazios", Toast.LENGTH_SHORT).show();
            } else {
                if(!preencherdoc || !preenchertel){
                    Toast.makeText(this, "Complete todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    if(fotoEscolhida == null && fotoBuscada == null){
                        Toast.makeText(this, "Escolha uma foto para seu perfil", Toast.LENGTH_SHORT).show();
                    } else {
                        enviarDadosWebservice();
                    }
                }
            }
        });

        imgcamera.setOnClickListener(view -> {
            AlertCamera("Abrir camera", "Autoriza o aplicativo acessar sua câmera?");
        });

        imggaleria.setOnClickListener(view -> {
            AlertGaleria("Abrir Galeria", "Autoriza o aplicativo acessar sua galeria de fotos?");
        });
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

    private void enviarDadosWebservice(){
        String url = "http://10.0.2.2:5000/api/Usuario";
        try {
            JSONObject dadosEnvio = new JSONObject();

            dadosEnvio.put("nome", ednome.getText().toString());
            dadosEnvio.put("email", edemail.getText().toString());
            dadosEnvio.put("senha", edsenha.getText().toString());
            dadosEnvio.put("endereco", edendereco.getText().toString());
            dadosEnvio.put("telefone", edtelefone.getText().toString());
            dadosEnvio.put("desc", edbio.getText().toString());
            dadosEnvio.put("doc", eddocumento.getText().toString());
            dadosEnvio.put("tipo",edtipouser.getSelectedItem().toString());

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
                                }else if(response.getInt("status") == 204){
                                    Snackbar.make(findViewById(R.id.telaCadastro), "CPF ou email já cadastrado", Snackbar.LENGTH_SHORT).show();
                                } else {
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
            RequestQueue requisicao = Volley.newRequestQueue(TelaCadastro.this);
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

    private boolean isEmpty(EditText etText) {
        String text = etText.getText().toString().trim();
        if (text.length()<1)
            return true;
        return false;
    }

    private void limparCampos(){
        ConstraintLayout telaComponentes = findViewById(R.id.telaCadastro);
        for (int i = 0; i < telaComponentes.getChildCount(); i++) {
            View view = telaComponentes.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }
}