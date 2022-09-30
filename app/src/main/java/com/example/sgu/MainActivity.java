package com.example.sgu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ImageView adicionarPub;
    ImageView iconPerfil;
    TextView txtnomeperfil;

    private List<postagem> postagem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        //Define Layout
        this.prepararPostagens();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Define Adapter
        postagemAdapter postagemAdapter = new postagemAdapter(postagem);

        recyclerView.setAdapter(postagemAdapter);

        adicionarPub = findViewById(R.id.adicionarPub2);
        adicionarPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AdicionarPublicacao.class));
            }
        });

        iconPerfil = findViewById(R.id.iconperfil);
        iconPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TelaPerfilP.class));
            }
        });

        txtnomeperfil = findViewById(R.id.txtnomeperfil);
        txtnomeperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TelaPerfilP.class));
            }
        });

    }
    public void prepararPostagens(){
        postagem p = new postagem("Wilson","Nova Pub", R.drawable.r);
        this.postagem.add(p);

        postagem p2 = new postagem("Victor","New Pub", R.drawable.h);
        this.postagem.add(p2);

        postagem p3 = new postagem("Andrielly","Pub", R.drawable.d);
        this.postagem.add(p3);

        postagem p4 = new postagem("Danna","Shit", R.drawable.a);
        this.postagem.add(p4);
    }
}