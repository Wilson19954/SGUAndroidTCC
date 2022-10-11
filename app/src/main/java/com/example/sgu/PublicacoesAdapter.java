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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.util.Calendar;
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
        viewHolder.txtData.setText(formataData(listaPublicacoes.get(position).getData()));

        byte[] converteBase64 = Base64.decode(listaPublicacoes.get(position).getImg(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);

        viewHolder.imgPostagem.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() { return listaPublicacoes.size(); }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formataData(long data){
        LocalDate data_postagem = Instant.ofEpochMilli(data).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hoje = LocalDate.now();

        Period diferenca = Period.between(data_postagem, hoje);
        int anos = diferenca.getYears();
        int meses = diferenca.getMonths();
        int dias = diferenca.getDays();



        /*LocalTime hora_postagem = Instant.ofEpochMilli(data).atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime hora_atual = LocalTime.now();

        int horas = hora_atual.getHour();
        int minutos = hora_atual.getMinute();
        int segundos = hora_atual.getSecond();
        */

        String d = dias == 1 ?"Publicado á " + dias + " dia " : "Publicado á " + dias + " dias ";
        String m = meses == 1 ?"Publicado á " +  meses + " mês " :"Publicado á " +  meses + " meses ";
        String a = anos == 1 ?"Publicado á " +  anos + " ano " :"Publicado á " +  anos + " anos ";

        if(anos == 0){
            if(meses == 0){
                return d;
            }else{
                return m + d;
            }
        }else{
            return a + m + d;
        }
    }

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

