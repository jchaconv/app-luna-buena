package com.example.asistencia;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class AprobadasFragment extends Fragment {

    private static RequestQueue queue;
    private static final String ApiURL = "http://b3rs3rk3r-002-site2.htempurl.com/api/";

    private ListView listView;
    private ProgressBar progressBar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AprobadasFragment() {
        //Empty Constructor
    }


    public static AprobadasFragment newInstance(String param1, String param2) {
        AprobadasFragment fragment = new AprobadasFragment();
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

        View view = inflater.inflate(R.layout.fragment_aprobadas, container, false);

        listView = (ListView) view.findViewById(R.id.listAprobadas);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ApiURL + "Justificacion/SelectByTrabajador?idTrabajador="+Integer.parseInt(getFromSharedPreferences("idusuario")), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    
                    String tituloJustificacion = response.getJSONObject(0).getString("jusC_JUSTIFICACION").toString();
                    String motivoJustificacion = response.getJSONObject(0).getString("jusC_MOTIVO").toString();
                    String fechaJustificacion = response.getJSONObject(0).getString("jusD_FECHA_JUSTIFICACION").toString();
                    String estadoJustificacion = response.getJSONObject(0).getString("jusN_ESTADO_JUSTIFICACION").toString();

                    String descEstadoJustificacion = null;

                    if(estadoJustificacion!=null && !estadoJustificacion.isEmpty()){
                        if(estadoJustificacion.equalsIgnoreCase("1")){
                            descEstadoJustificacion = "Solicitud Aprobada";
                        }
                    }

                    Toast.makeText(getContext(), "tituloJustificacion: " + tituloJustificacion, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);


        return view;
    }

    private String getFromSharedPreferences(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

}