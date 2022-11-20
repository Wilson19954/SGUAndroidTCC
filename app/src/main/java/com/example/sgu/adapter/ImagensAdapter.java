package com.example.sgu.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgu.R;

import java.util.List;

public class ImagensAdapter extends RecyclerView.Adapter<ImagensAdapter.ImagensViewHolder> {
    //Lista de imagens da classe
    List<Uri> listaImagens;

    //Construtor
    public ImagensAdapter(List<Uri> listaImagens) {
        this.listaImagens = listaImagens;
    }

    @NonNull
    @Override
    public ImagensViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Indicar o layout a ser utilizado
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);

        //retorna o ViewHoler
        return new ImagensViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagensViewHolder holder, int position) {
        //Atribui as imagens que serão populadas na lista
        holder.imgItem.setImageURI(listaImagens.get(position));
    }

    @Override
    public int getItemCount() {
        return listaImagens.size();
    }

    public class ImagensViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;

        public ImagensViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgViewItem);
        }
    }
}
