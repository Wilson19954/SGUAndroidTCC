package com.example.sgu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class postagemAdapter extends RecyclerView.Adapter<postagemAdapter.MyViewHolder> {

    private List<postagem> postagens;
    public postagemAdapter(List<postagem> listaPostagens){
        this.postagens = listaPostagens;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.tela_postagem, parent, false);
        return new MyViewHolder(itemLista);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        postagem postagem = postagens.get(position);
        holder.textnome.setText(postagem.getNome());
        holder.textpostagem.setText(postagem.getPost());
        holder.imagemPostagem.setImageResource(postagem.getImg());
    }
    @Override
    public int getItemCount() {
        return postagens.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textnome;
        private TextView textpostagem;
        private ImageView imagemPostagem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textnome = itemView.findViewById(R.id.textNome);
            textpostagem = itemView.findViewById(R.id.textPostagem);
            imagemPostagem = itemView.findViewById(R.id.imagePostagem);
        }
    }

}
