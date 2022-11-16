package com.example.sgu.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgu.R;
import com.example.sgu.classes.Projetos;

import java.util.ArrayList;
import java.util.List;

public class FotosAdapter extends RecyclerView.Adapter<FotosViewHolder> {
    private Context context;
    private List<Bitmap> listaFotos;

    public FotosAdapter(List<Bitmap> listaFotos, Context context){
        this.listaFotos = listaFotos;
        this.context = context;
    }

    @NonNull
    @Override
    public FotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fotos, parent, false);
        return new FotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotosViewHolder holder, int position) {
        FotosViewHolder viewHolder  = (FotosViewHolder) holder;

        byte[] converteBase64 = Base64.decode(String.valueOf(listaFotos), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);
        viewHolder.fotos.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return listaFotos.size();
    }
}

class FotosViewHolder extends RecyclerView.ViewHolder{

    ImageView fotos;

    public FotosViewHolder(@NonNull View itemView) {
        super(itemView);

        fotos = itemView.findViewById(R.id.foto);

    }
}
