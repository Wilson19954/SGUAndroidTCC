package com.example.sgu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgu.R;
import com.example.sgu.classes.Projetos;
import com.example.sgu.telas.TelaEditarProjeto;

import java.util.List;

public class ProjetosAdapterEdit extends  RecyclerView.Adapter<ProjetosEditViewHolder> {

    private List<Projetos> listaProjetos;
    private Context context;
    AlertDialog alert;

    public ProjetosAdapterEdit(List<Projetos> listaProjetos, Context context){
        this.listaProjetos = listaProjetos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProjetosEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tela_projetos_edit, parent, false);
        return new ProjetosEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjetosEditViewHolder holder, int position) {

        ProjetosEditViewHolder viewHolder  = (ProjetosEditViewHolder) holder;
        viewHolder.nomeProj.setText(listaProjetos.get(position).getNome());
        viewHolder.descProj.setText(listaProjetos.get(position).getDesc());
        viewHolder.custoProj.setText(listaProjetos.get(position).getCusto());

        byte[] converteBase64 = Base64.decode(listaProjetos.get(position).getImg(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);
        viewHolder.imageProj.setImageBitmap(bitmap);

        viewHolder.icEdit.setOnClickListener(view -> {
            //Ao clicar no botão para atualizar, será aberta uma nova tela
            Intent it = new Intent(context, TelaEditarProjeto.class);
            it.putExtra("docuser", listaProjetos.get(position).getDoc_user());
            it.putExtra("nomeproj", listaProjetos.get(position).getNome());
            it.putExtra("descproj", listaProjetos.get(position).getDesc());
            it.putExtra("custoproj", listaProjetos.get(position).getCusto());
            it.putExtra("fotoprincipalproj", listaProjetos.get(position).getImg());
            it.putExtra("dataproj", listaProjetos.get(position).getData());
            it.putExtra("codproj",listaProjetos.get(position).getCod());
            it.putExtra("tagproj", listaProjetos.get(position).getTag());
            context.startActivity(it);
        });

    }

    @Override
    public int getItemCount() {
        return listaProjetos.size();
    }
}


class ProjetosEditViewHolder extends RecyclerView.ViewHolder{

    ImageView imageProj, icEdit;
    TextView nomeProj, custoProj, descProj;

    public ProjetosEditViewHolder(@NonNull View itemView) {
        super(itemView);

        imageProj = itemView.findViewById(R.id.imagePub);
        nomeProj = itemView.findViewById(R.id.nomePub);
        custoProj = itemView.findViewById(R.id.dataPub);
        descProj = itemView.findViewById(R.id.descProj);
        icEdit = itemView.findViewById(R.id.iconEdit);
    }
}
