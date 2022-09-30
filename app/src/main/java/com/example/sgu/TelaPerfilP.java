package com.example.sgu;

import static android.widget.TabHost.TabSpec.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.google.android.material.tabs.TabLayout;

public class TelaPerfilP extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil_p);


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FotosFragment(), "Fotos");
        vpAdapter.addFragment(new ProjetosFragment(), "Projetos");
        vpAdapter.addFragment(new PublicacoesFragment(), "Pubs");

        viewPager.setAdapter(vpAdapter);
       /* TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();*/

        //tab1
        /*TabHost.TabSpec spec = tabHost.newTabSpec("PUBLICAÇÕES");
        spec.setContent(R.id.tab1);
        spec.setIndicator("PUBLICAÇÕES");
        tabHost.addTab(spec);

        //tab2
        spec = tabHost.newTabSpec("PROJETOS");
        spec.setContent(R.id.tab2);
        spec.setIndicator("PROJETOS");
        tabHost.addTab(spec);

        //tab3
        spec = tabHost.newTabSpec("FOTOS");
        spec.setContent(R.id.tab3);
        spec.setIndicator("FOTOS");
        tabHost.addTab(spec);*/


    }
}