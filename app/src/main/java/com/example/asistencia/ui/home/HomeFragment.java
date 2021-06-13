package com.example.asistencia.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistencia.R;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.HorarioViewModel;

public class HomeFragment extends Fragment {

    TextView txtCodigo; //comentario de prueba
    Button btnCodigo;
    private static RequestQueue queue;
    private static final String ApiURL = "http://b3rs3rk3r-002-site2.htempurl.com/api/";
    HorarioViewModel horariosArray = new HorarioViewModel();

    String HoraInicio = "";
    String HoraFin = "";
    String Sigla = "";
    String Tipo = "";


    public HomeFragment(){
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtCodigo = view.findViewById(R.id.txtCodigo);
        btnCodigo = view.findViewById(R.id.btncodigo);

        //QUITAR
        Toast.makeText(getContext(), getFromSharedPreferences("idusuario"), Toast.LENGTH_SHORT).show();

        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearQR();
            }
        });
        return view;
    }

    //QUITAR
    private String getFromSharedPreferences(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    private void escanearQR() {
        IntentIntegrator in = IntentIntegrator.forSupportFragment(HomeFragment.this);
        in.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        in.setPrompt("ESCANEAR CODIGO");
        in.setCameraId(0);
        in.setBeepEnabled(false);
        in.setBarcodeImageEnabled(false);
        in.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(getContext(), "PROCESO CANCELADO", Toast.LENGTH_SHORT).show();
            }else{

                Boolean marcado = marcarAsistencia(Integer.parseInt(getFromSharedPreferences("idusuario")));

                txtCodigo.setText(result.getContents().toString());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private boolean marcarAsistencia(int _id) {
        Boolean status = true;
        if (!ValidarHorario(_id)) {

            status = false;
        }
        return status;
    }

    private boolean ValidarHorario(int id) {
        Boolean status = true;
        HoraInicio = ""; HoraFin = ""; Sigla = ""; Tipo = "";
        //RECUPERANDO HORARIO
        getHorarios(id);
        return status;
    }

    //Metodo que obtiene el horario de marcación del trabajador
    //para validar la hora de asistencia
    private void getHorarios(int idUser) {
        horariosArray = new HorarioViewModel();
        //OBTENIENDO DIA ACTUAL
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setTime(new Date());
        int numeroSemana = calendar.get(Calendar.DAY_OF_WEEK);
        numeroSemana = numeroSemana == 0 ? 7 : numeroSemana-1;
        queue = Volley.newRequestQueue(getActivity());
        Map<String, Integer> params = new HashMap();
        params.put("diaActual", numeroSemana);
        params.put("idTrabajador", idUser);
        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiURL + "Horario/ObtenerHorario", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //DESERIALIZANDO RESPUESTA
                Gson gson = new Gson();
                horariosArray = gson.fromJson(response.toString(), HorarioViewModel.class);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

}