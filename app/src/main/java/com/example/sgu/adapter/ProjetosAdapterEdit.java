package com.example.sgu.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.R;
import com.example.sgu.classes.Projetos;
import com.example.sgu.telas.TelaEditarProjeto;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        viewHolder.icDeletar.setOnClickListener(view -> {
            AlertDialog.Builder confirmacao = new AlertDialog.Builder(holder.itemView.getContext());
            confirmacao.setTitle(R.string.txtTituloAlert);
            confirmacao.setPositiveButton(R.string.txtApagarSim, (dialog, which) -> {
                removerWebservice(listaProjetos.get(position).getCod(), position);
            });
            confirmacao.setNegativeButton(R.string.txtApagarNao, (dialog, which) -> {
                alert.cancel();
            });
            alert = confirmacao.create();
            alert.show();
        });


    }

    @Override
    public int getItemCount() {
        return listaProjetos.size();
    }



    private void removerWebservice(String cod, int posicao){
        //Indicando que irá utilizar o webservice rodando no localhost do computador
        String url = "http://10.0.2.2:5000/api/Projetos/" + cod;

        //Configurar a requisição que será enviada ao webservice
        JsonObjectRequest configRequisicao = new JsonObjectRequest(Request.Method.DELETE,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("status") == 200){
                                listaProjetos.remove(posicao);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Removido com sucesso", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Erro1", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Erro2", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, "Erro3", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requisicao = Volley.newRequestQueue(context);
        requisicao.add(configRequisicao);
    }

}


class ProjetosEditViewHolder extends RecyclerView.ViewHolder{

    ImageView imageProj, icEdit, icDeletar;
    TextView nomeProj, custoProj, descProj;

    public ProjetosEditViewHolder(@NonNull View itemView) {
        super(itemView);

        imageProj = itemView.findViewById(R.id.imagePub);
        nomeProj = itemView.findViewById(R.id.nomePub);
        custoProj = itemView.findViewById(R.id.dataPub);
        descProj = itemView.findViewById(R.id.descProj);
        icEdit = itemView.findViewById(R.id.iconEdit);
        icDeletar = itemView.findViewById(R.id.deletarProj);
    }
}
