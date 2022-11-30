package com.example.sgu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgu.R;
import com.example.sgu.classes.Galeria;

import java.util.List;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaViewHolder> {

    private List<Galeria> listaGaleria;
    private Context context;

    public GaleriaAdapter(List<Galeria> listaGaleria, Context context) {
        this.listaGaleria = listaGaleria;
        this.context = context;
    }

    @NonNull
    @Override
    public GaleriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_galeria, parent, false);
        return new GaleriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GaleriaViewHolder holder, int position) {
        byte[] converteBase64 = Base64.decode(listaGaleria.get(position).getimagemGaleria(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);
        holder.imgGaleria.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return listaGaleria.size();
    }

}

class GaleriaViewHolder extends RecyclerView.ViewHolder {

    ImageView imgGaleria;

    public GaleriaViewHolder(@NonNull View itemView) {
        super(itemView);
        imgGaleria = itemView.findViewById(R.id.imgGaleriaVH);
    }
}
