package com.example.sgu;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PublicacoesAdapter extends RecyclerView.Adapter<PublicacoesViewHolder> {

    private List<Publicacoes> listaPublicacoes;
    private Context context;
    AlertDialog alert;

    public PublicacoesAdapter(List<Publicacoes> listaPublicacoes, Context context){
        this.listaPublicacoes = listaPublicacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public PublicacoesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tela_postagem, parent, false);
        return new PublicacoesViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull PublicacoesViewHolder holder, int position) {
        PublicacoesViewHolder viewHolder  = (PublicacoesViewHolder) holder;
        viewHolder.descPostagem.setText(listaPublicacoes.get(position).getDesc());
        byte[] converteBase64 = Base64.decode(listaPublicacoes.get(position).getImg(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);
        viewHolder.imgPostagem.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() { return listaPublicacoes.size(); }
}

class PublicacoesViewHolder extends RecyclerView.ViewHolder{

    ImageView imgPostagem, imgFotoPerfil;
    TextView descPostagem, txtData, txtNomePerfil, txtCurtida;
    ImageButton imgBtRemover, imgBtAtualizar;

    public PublicacoesViewHolder(@NonNull View itemView) {
        super(itemView);
        imgFotoPerfil = itemView.findViewById(R.id.imgFotoPerfil);
        descPostagem = itemView.findViewById(R.id.descPostagem);
        txtData = itemView.findViewById(R.id.txtTime);
        txtNomePerfil = itemView.findViewById(R.id.txtNomePerfil);
        txtCurtida = itemView.findViewById(R.id.txtCurtida);
        imgPostagem = itemView.findViewById(R.id.imgPostagem);
        imgBtRemover = itemView.findViewById(R.id.like2);
        imgBtAtualizar = itemView.findViewById(R.id.like1);
    }
}

