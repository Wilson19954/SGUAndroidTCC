package com.example.sgu.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.R;
import com.example.sgu.classes.Publi;
import com.example.sgu.classes.Publicacoes;
import com.example.sgu.classes.Usuario;
import com.example.sgu.telas.TelaPerfilV;

import org.json.JSONArray;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PublicacoesAdapter extends RecyclerView.Adapter<PublicacoesViewHolder> {

    private List<Publicacoes> listaPublicacoes;
    private List<Publi> listaPubli;
    private List<Publi> listaAtualizada;
    private List<Usuario> listaUsuario;
    private Context context;
    AlertDialog alert;
    String codPub;

    public void setFilteredList(List<Publi> filteredList){
        this.listaPubli = filteredList;
    }

    public PublicacoesAdapter(List<Publi> listaPubli, Context context){
        this.listaPubli = listaPubli;
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
    public void onBindViewHolder(@NonNull PublicacoesViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PublicacoesViewHolder viewHolder  = (PublicacoesViewHolder) holder;
        String like = String.valueOf(listaPubli.get(position).getLike_pub());

        viewHolder.txtNomePerfil.setText(listaPubli.get(position).getNome_user());
        viewHolder.categoriaPub.setText(listaPubli.get(position).getTag_pub());
        viewHolder.descPostagem.setText(listaPubli.get(position).getDesc_pub());
        viewHolder.txtData.setText(formataData(listaPubli.get(position).getData_pub()));
        viewHolder.txtCurtida.setText(like);
        viewHolder.tipoConta.setText(listaPubli.get(position).getTipo_user());

        byte[] converteBase64_2 = Base64.decode(listaPubli.get(position).getImg_user(), Base64.DEFAULT);
        byte[] converteBase64 = Base64.decode(listaPubli.get(position).getImg_pub(), Base64.DEFAULT);

        Bitmap bitmap2 = BitmapFactory.decodeByteArray(converteBase64_2, 0, converteBase64_2.length);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);

        viewHolder.imgPostagem.setImageBitmap(bitmap);
        viewHolder.imgFotoPerfil.setImageBitmap(bitmap2);

        viewHolder.imgBtLike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                codPub = listaPubli.get(position).getCod_pub();
                likePubWebService(position);
                viewHolder.imgBtLike.setVisibility(View.INVISIBLE);
                viewHolder.imgBtdeslike.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.imgBtdeslike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codPub = listaPubli.get(position).getCod_pub();
                deslikePubWebService(position);
                viewHolder.imgBtdeslike.setVisibility(View.INVISIBLE);
                viewHolder.imgBtLike.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.txtNomePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TelaPerfilV.class);
                context.startActivity(intent);
                String documentPubli = listaPubli.get(position).getDoc_user();

                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("docPubli", documentPubli);
                editor.commit();
            }
        });
        viewHolder.imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TelaPerfilV.class);
                context.startActivity(intent);
                String documentPubli = listaPubli.get(position).getDoc_user();

                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("docPubli", documentPubli);
                editor.commit();
            }
        });

    }

    @Override
    public int getItemCount() { return listaPubli.size(); }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formataData(long data){
        LocalDate data_postagem = Instant.ofEpochMilli(data).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime hora_postagem = Instant.ofEpochMilli(data).atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDate hoje = LocalDate.now();

        Period diferenca = Period.between(data_postagem, hoje);
        int anos = diferenca.getYears();
        int meses = diferenca.getMonths();
        int dias = diferenca.getDays();

        LocalTime horaAtual = LocalTime.now();
        LocalTime hora2 = LocalTime.of(21, 11, 00);

        int minutos2 = horaAtual.getMinute() - hora_postagem.getMinute();

        long horas = ChronoUnit.HOURS.between(horaAtual, hora2);
        long minutos = ChronoUnit.MINUTES.between(horaAtual, hora2) % 60;
        long segundos = ChronoUnit.SECONDS.between(horaAtual, hora2) % 60;

        String d = dias == 1 ?"Publicado á " + dias + " dia " : "Publicado á " + dias + " dias. ";
        String m = meses == 1 ?"Publicado á " +  meses + " mês " :"Publicado á " +  meses + " meses. ";
        String a = anos == 1 ?"Publicado á " +  anos + " ano " :"Publicado á " +  anos + " anos. ";
        //String hour = horas == 1 ? " Publicado á " + horas + " hora" : "Publicado á " + horas + " horas. ";
        //String min = minutos == 1 ? " Publicado á " + minutos + " minuto" : "Publicado á " + minutos + " minutos.";
        String seg =  "Publicado a menos de um dia";

        if(anos == 0){
            if(meses == 0){
                if(dias == 0){
                    return seg;
                }else{
                    return d;
                }
            }else{
                return m + d;
            }
        }else{
            return a + m + d;
        }
    }


    private void likePubWebService(int position){
        String url = "http://10.0.2.2:5000/api/Publicacoes/like/" + codPub;
        RequestQueue solicitacao = Volley.newRequestQueue(context);
        JsonArrayRequest envio = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Publi aux = listaPubli.get(position);
                        aux.setLike_pub(aux.getLike_pub() + 1);
                        listaPubli.remove(position);
                        listaPubli.add(position, aux);
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            }
        });
        solicitacao.add(envio);
    }
    private void deslikePubWebService(int position){
        String url = "http://10.0.2.2:5000/api/Publicacoes/deslike/" + codPub;
        RequestQueue solicitacao = Volley.newRequestQueue(context);
        JsonArrayRequest envio = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Publi aux = listaPubli.get(position);
                        aux.setLike_pub(aux.getLike_pub() - 1);
                        listaPubli.remove(position);
                        listaPubli.add(position, aux);
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            }
        });
        solicitacao.add(envio);
    }

}

class PublicacoesViewHolder extends RecyclerView.ViewHolder{

    ImageView imgPostagem, imgFotoPerfil, imgBtLike;
    TextView descPostagem, txtData, txtNomePerfil, txtCurtida, categoriaPub, tipoConta;
    ImageButton imgBtdeslike;

    public PublicacoesViewHolder(@NonNull View itemView) {
        super(itemView);

        imgFotoPerfil = itemView.findViewById(R.id.imagePub);
        txtNomePerfil = itemView.findViewById(R.id.nomePub);
        descPostagem = itemView.findViewById(R.id.descPostagem);
        txtData = itemView.findViewById(R.id.dataPub);
        txtCurtida = itemView.findViewById(R.id.txtCurtida);
        imgPostagem = itemView.findViewById(R.id.imgPub);
        imgBtdeslike = itemView.findViewById(R.id.likeCheio);
        imgBtLike = itemView.findViewById(R.id.likeVazio);
        categoriaPub = itemView.findViewById(R.id.categoriaPub);
        tipoConta = itemView.findViewById(R.id.tipoConta);

    }
}

