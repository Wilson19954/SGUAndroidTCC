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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<Usuario> listaUsuario;
    private Context context;
    AlertDialog alert;

    public void setFilteredList(List<Publi> filteredList){
        this.listaPubli = filteredList;
        notifyDataSetChanged();
    }

    public PublicacoesAdapter(List<Publi> listaPubli,  Context context){
        this.listaPubli = listaPubli;
        this.context = context;
        notifyDataSetChanged();
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

        viewHolder.imgBtlike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "like!", Toast.LENGTH_SHORT).show();
                viewHolder.txtCurtida.setText("2");
            }
        });

        viewHolder.txtNomePerfil.setText(listaPubli.get(position).getNome_user());
        viewHolder.categoriaPub.setText(listaPubli.get(position).getTag_pub());
        viewHolder.descPostagem.setText(listaPubli.get(position).getDesc_pub());
        viewHolder.txtData.setText(formataData(listaPubli.get(position).getData_pub()));
        //viewHolder.txtCurtida.setText(listaPubli.get(position).getLike_pub());

        byte[] converteBase64_2 = Base64.decode(listaPubli.get(position).getImg_user(), Base64.DEFAULT);
        byte[] converteBase64 = Base64.decode(listaPubli.get(position).getImg_pub(), Base64.DEFAULT);

        Bitmap bitmap2 = BitmapFactory.decodeByteArray(converteBase64_2, 0, converteBase64_2.length);
        Bitmap bitmap = BitmapFactory.decodeByteArray(converteBase64, 0, converteBase64.length);

        viewHolder.imgPostagem.setImageBitmap(bitmap);
        viewHolder.imgFotoPerfil.setImageBitmap(bitmap2);

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
        String hour = horas == 1 ? " Publicado á " + horas + " hora" : "Publicado á " + horas + " horas. ";
        String min = minutos == 1 ? " Publicado á " + minutos + " minuto" : "Publicado á " + minutos + " minutos.";
        String seg = segundos == 1 ? " Publicado á " + segundos + " segundo " : " Publicado á " + segundos + " segundos. ";

        if(anos == 0){
            if(meses == 0){
                if(dias == 0){
                    if(horas < 24){
                        if(minutos < 1){
                            return hour + minutos2 + seg ;
                        }else{
                            return hour + minutos2 + seg;
                        }
                    }else{
                        return hour + minutos2 + seg ;
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
    TextView descPostagem, txtData, txtNomePerfil, txtCurtida, categoriaPub;
    ImageButton imgBtRemover, imgBtlike;

    public PublicacoesViewHolder(@NonNull View itemView) {
        super(itemView);
        imgFotoPerfil = itemView.findViewById(R.id.imageProj);
        txtNomePerfil = itemView.findViewById(R.id.nomeProj);
        descPostagem = itemView.findViewById(R.id.descPostagem);
        txtData = itemView.findViewById(R.id.custoProj);
        txtCurtida = itemView.findViewById(R.id.txtCurtida);
        imgPostagem = itemView.findViewById(R.id.imgProj);
        imgBtRemover = itemView.findViewById(R.id.like2);
        imgBtlike = itemView.findViewById(R.id.like1);
        categoriaPub = itemView.findViewById(R.id.categoriaPub);

    }
}

