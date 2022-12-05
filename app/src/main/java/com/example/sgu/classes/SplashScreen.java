package com.example.sgu.classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sgu.R;
import com.example.sgu.telas.MainActivity;
import com.example.sgu.telas.TelaLogin;

public class SplashScreen extends Activity implements Runnable {

    ImageView gif;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_splash);

        gif = (ImageView) findViewById(R.id.gif2);

        Glide.with(this)
                .load(R.drawable.splash3)
                .asGif()
                .into(gif);

        Handler handler = new Handler();
        // define o tempo de execução em 5 segundos
        handler.postDelayed(this, 5000);
    }

    public void run(){
        // inicia outra activity após o termino do tempo
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
