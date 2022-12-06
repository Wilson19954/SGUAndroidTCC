package com.example.sgu.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgu.R;
import com.example.sgu.adapter.ProjetosAdapter;
import com.example.sgu.classes.Projetos;
import com.example.sgu.telas.TelaAdicionarProjeto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjetosFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjetosFragment2 extends Fragment {

    String documentPubli;
    ImageView adicionarProjetos;
    RecyclerView recyclerView;
    List<Projetos> listaProjetos = new ArrayList<>();
    String codproj;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjetosFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjetosFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjetosFragment2 newInstance(String param1, String param2) {
        ProjetosFragment2 fragment = new ProjetosFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projetos2, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BuscarDadosWebService();
    }

    private void recuperarDados(){
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        documentPubli = sharedPref.getString("docPubli","");
    }

    private void BuscarDadosWebService(){
        recuperarDados();
        String url = "http://10.0.2.2:5000/api/Projetos/search/" + documentPubli;
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
                                    dataFinal = format.parse(object.getString("data"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Projetos proj = new Projetos(object.getString("cod"),
                                        object.getString("desc"),
                                        object.getString("custo"),
                                        object.getString("tag"),
                                        object.getString("nome"),
                                        object.getString("doc_user"),
                                        dataFinal.getTime(),
                                        object.getString("img"));
                                listaProjetos.add(proj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ProjetosAdapter adapter = new ProjetosAdapter(listaProjetos, getContext());
                            recyclerView.setAdapter(adapter);

                            codproj = listaProjetos.get(i).getCod();
                            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("codprojperfil", codproj);
                            editor.apply();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Toast.makeText(getActivity(), "Sem Projetos Cadastrados", Toast.LENGTH_SHORT).show();
            }
        }
        );
        solicitacao.add(envio);
    }
}