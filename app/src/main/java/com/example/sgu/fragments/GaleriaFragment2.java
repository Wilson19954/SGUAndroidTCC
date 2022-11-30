package com.example.sgu.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.sgu.adapter.GaleriaAdapter;
import com.example.sgu.classes.Galeria;
import com.example.sgu.telas.TelaAdicionarProjeto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GaleriaFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GaleriaFragment2 extends Fragment {

    List<Galeria> galeria = new ArrayList<>();
    RecyclerView recyclerView;
    String codproj;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GaleriaFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GaleriaFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static GaleriaFragment2 newInstance(String param1, String param2) {
        GaleriaFragment2 fragment = new GaleriaFragment2();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_galeria2, container, false);
    }
    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
            BuscarDadosGaleriaWebService();

    }

    private void BuscarDadosGaleriaWebService(){
        recuperarDados();
        String url = "http://10.0.2.2:5000/api/Galeria/search/"+ codproj;

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
                                Galeria  img = new Galeria(object.getString("foto"));
                                galeria.add(img);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            GaleriaAdapter adapter = new GaleriaAdapter(galeria, getContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Sem Projetos Cadastrados", Toast.LENGTH_SHORT).show();
            }
        }
        );
        solicitacao.add(envio);
    }

    private void recuperarDados(){
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        codproj = sharedPref.getString("codprojperfil","");
    }
}