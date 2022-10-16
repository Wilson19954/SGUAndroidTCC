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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
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
        //LocalTime hora_postagem = Instant.ofEpochMilli(data).atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDate hoje = LocalDate.now();

        Period diferenca = Period.between(data_postagem, hoje);
        int anos = diferenca.getYears();
        int meses = diferenca.getMonths();
        int dias = diferenca.getDays();

        LocalTime horaAtual = LocalTime.now();
        LocalTime hora2 = LocalTime.of(21, 22, 00);

        long horas = ChronoUnit.HOURS.between(horaAtual, hora2);
        long minutos = ChronoUnit.MINUTES.between(horaAtual, hora2) % 60;
        long segundos = ChronoUnit.SECONDS.between(horaAtual, hora2) % 60;

        String d = dias == 1 ?"Publicado á " + dias + " dia " : "Publicado á " + dias + " dias. ";
        String m = meses == 1 ?"Publicado á " +  meses + " mês " :"Publicado á " +  meses + " meses. ";
        String a = anos == 1 ?"Publicado á " +  anos + " ano " :"Publicado á " +  anos + " anos. ";
        String hour = horas == 1 ? " Publicado á " + horas + " hora" : "Publicado á " + horas + " horas. ";
        String min = minutos == 1 ? " Publicado á " + minutos + " minuto" : "Publicado á " + horas + " minutos.";
        String seg = segundos == 1 ? " Publicado á " + segundos + " segundo " : " Publicado á " + segundos + " segundos. ";

        if(anos == 0){
            if(meses == 0){
                if(dias == 0){
                    if(horas < 24){
                        if(minutos == 0){
                            return seg;
                        }else{
                            return min;
                        }
                    }else{
                        return hour;
                    }
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

