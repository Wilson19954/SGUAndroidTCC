package com.example.sgu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.R;
import com.example.sgu.adapter.PublicacoesAdapter;
import com.example.sgu.classes.Publi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublicacoesFragment extends Fragment {

    RecyclerView recyclerView;
    List<Publi> listaPubli = new ArrayList<>();
    String document;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publicacoes, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        enviarDadosPubWebservice();
    }


    private void enviarDadosPubWebservice(){
        recuperarDados();
        String url = "http://10.0.2.2:5000/api/Publicacoes/search/"+document;
        RequestQueue solicitacao = Volley.newRequestQueue(getContext());
        JsonArrayRequest envio = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0 ; i<response.length() ; i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date dataFinal = null;
                                try {
                                    dataFinal = format.parse(object.getString("data_pub"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Publi publi = new Publi(
                                        object.getString("img_pub"),
                                        object.getString("desc_pub"),
                                        object.getInt("like"),
                                        object.getString("tag_pub"),
                                        dataFinal.getTime(),
                                        object.getString("nome_user"),
                                        object.getString("doc_user"),
                                        object.getString("img_user"),
                                        object.getString("tipo_user"),
                                        object.getString("cod_pub"));
                                listaPubli.add(publi);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PublicacoesAdapter adapter = new PublicacoesAdapter(listaPubli, getContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Toast.makeText(getContext(), "Erro ao conectar", Toast.LENGTH_SHORT).show();
            }
        }
        );
        solicitacao.add(envio);
    }

    private void recuperarDados(){
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        document = sharedPref.getString("doc","");
    }

}