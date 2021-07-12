package com.example.asistencia;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.JustificacionesResponse;
import Models.ListJustificacionesAdapter;

public class AprobadasFragment extends Fragment {

    private static RequestQueue queue;
    private static final String ApiURL = "http://b3rs3rk3r-002-site2.htempurl.com/api/";

    private ListView listView;

    private ArrayAdapter<JustificacionesResponse> adapter;
    private ArrayList<JustificacionesResponse> listJustificaciones;

    private ArrayAdapter<String> textAdapter;
    private ArrayList<String> listText;

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

        queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ApiURL + "Justificacion/SelectByTrabajador?idTrabajador=" + Integer.parseInt(getFromSharedPreferences("idusuario")), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //SimpleDateFormat objDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //Date dtFechaJustificacion = null;
                String descEstadoJustificacion = null;

                if (response.length() > 0) {
                    listText = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String estadoJustificacion = response.getJSONObject(i).getString("jusN_ESTADO_JUSTIFICACION").toString();
                            if (estadoJustificacion != null && !estadoJustificacion.isEmpty() && estadoJustificacion.equalsIgnoreCase("1")) {
                                descEstadoJustificacion = "Solicitud Aprobada";
                                if (descEstadoJustificacion.equalsIgnoreCase("Solicitud Aprobada")) {
                                    String tituloJustificacion = response.getJSONObject(i).getString("jusC_JUSTIFICACION").toString();
                                    String fechaJustificacion = response.getJSONObject(i).getString("jusD_FECHA_JUSTIFICACION").toString();
                                    String text = tituloJustificacion + "|| " + fechaJustificacion.substring(0, 10);
                                    listText.add(text);
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    textAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, listText);
                    listView.setAdapter(textAdapter);
                }


                //try {

                    /*String descEstadoJustificacion = null;
                    listJustificaciones = new ArrayList<>();
                    JustificacionesResponse justificacionesResponse = new JustificacionesResponse();

                    for (int i = 0; i < response.length(); i++) {

                        String estadoJustificacion = response.getJSONObject(i).getString("jusN_ESTADO_JUSTIFICACION").toString();

                        if (estadoJustificacion != null && !estadoJustificacion.isEmpty() && estadoJustificacion.equalsIgnoreCase("1")) {
                            descEstadoJustificacion = "Solicitud Aprobada";
                        } else{
                            descEstadoJustificacion = "Solicitud Rechazada";
                        }

                        if (descEstadoJustificacion.equalsIgnoreCase("Solicitud Aprobada")) {

                            String tituloJustificacion = response.getJSONObject(i).getString("jusC_JUSTIFICACION").toString();
                            String motivoJustificacion = response.getJSONObject(i).getString("jusC_MOTIVO").toString();
                            String fechaJustificacion = response.getJSONObject(i).getString("jusD_FECHA_JUSTIFICACION").toString();

                            justificacionesResponse.setTituloJustificacion(tituloJustificacion);
                            justificacionesResponse.setMotivoJustificacion(motivoJustificacion);
                            justificacionesResponse.setFechaJustificacion(fechaJustificacion);
                            justificacionesResponse.setDescEstadoJustificacion(descEstadoJustificacion);

                            listJustificaciones.add(justificacionesResponse);
                        }

                    }

                    List<String> listString = new ArrayList<>();
                    for(JustificacionesResponse obj : listJustificaciones){

                    }


                    ListJustificacionesAdapter adapterList = new ListJustificacionesAdapter(getContext(), R.layout.adapter_view_layout, listJustificaciones);
                    listView.setAdapter(adapterList);*/
                //}


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