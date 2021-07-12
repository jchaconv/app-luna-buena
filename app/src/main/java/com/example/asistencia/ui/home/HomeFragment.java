package com.example.asistencia.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistencia.LoginActivity;
import com.example.asistencia.MainActivity;
import com.example.asistencia.R;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Models.AsistenciaViewModel;
import Models.HorarioViewModel;
import Models.LoginViewModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    TextView txtCodigo;
    Button btnCodigo;
    private static RequestQueue queue;
    private static final String ApiURL = "http://b3rs3rk3r-002-site2.htempurl.com/api/";
    HorarioViewModel[] horariosArray;

    String HoraInicio = "";
    String HoraFin = "";
    String Sigla = "";
    String Tipo = "";
    double lat = 0;
    double lng = 0;

    public HomeFragment(){
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtCodigo = view.findViewById(R.id.txtCodigo);
        btnCodigo = view.findViewById(R.id.btncodigo);

        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = "";
                String lng = "";
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                } else {
                    locationStart();
                }
                escanearQR();
            }
        });
        return view;
    }

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
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    calendar.setMinimalDaysInFirstWeek(4);
                    calendar.setTime(new Date());
                    int numeroSemana = calendar.get(Calendar.DAY_OF_WEEK);
                    //numeroSemana = numeroSemana == 1 ? 7 : numeroSemana - 2;
                    numeroSemana = numeroSemana == 0 ? 7 : numeroSemana - 1;

                    queue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiURL + "Horario/ObtenerHorario?diaActual="+numeroSemana+"&idTrabajador="+Integer.parseInt(getFromSharedPreferences("idusuario")), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            try {
                                long ahora = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));

                                String anio = new SimpleDateFormat("yyyy").format(new java.util.Date());
                                String mes = new SimpleDateFormat("MM").format(new java.util.Date());
                                String dia = new SimpleDateFormat("dd").format(new java.util.Date());

                                horariosArray = gson.fromJson(response, HorarioViewModel[].class);
                                if (horariosArray.length == 0) {
                                    HoraInicio = "";
                                    HoraFin = "";
                                    Sigla = "";
                                    Tipo = "";
                                }
                                Boolean encontrado = false;
                                for (int x = 0; x < horariosArray.length; x++) {
                                    long hi = Long.parseLong(anio + mes + dia + (horariosArray[x].getHodC_INICIO()).replace(":", ""));
                                    long hf = Long.parseLong(anio + mes + dia + (horariosArray[x].getHodC_FIN()).replace(":", ""));
                                    if (ahora >= hi && ahora <= hf) {
                                        HoraInicio = horariosArray[x].getHodC_INICIO();
                                        HoraFin = horariosArray[x].getHodC_FIN();
                                        Sigla = horariosArray[x].getHodC_SIGLA();
                                        Tipo = horariosArray[x].getHodC_NOMBRE();
                                        encontrado = true;
                                        //OBTENIENDO LA DIRECCIÓN IP
                                        String ip = "";
                                        try {
                                            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                                            for (NetworkInterface intf : interfaces) {
                                                if (intf.getName().contains("wlan")) {
                                                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                                                    for (InetAddress addr : addrs) {
                                                        if (!addr.isLoopbackAddress()) {
                                                            String sAddr = addr.getHostAddress();
                                                            if (addr instanceof Inet4Address) {
                                                                ip = sAddr;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText(e.getMessage()).show();
                                        }

                                        //OBTENIENDO COORDENADAS
                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                                        }else {
                                            queue = Volley.newRequestQueue(getActivity());
                                            AsistenciaViewModel modelo = new AsistenciaViewModel();
                                            modelo.setAsiC_DIRECCION("direccion");
                                            modelo.setAsiC_EQUIPO("equipo");
                                            modelo.setAsiC_HORA(new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));
                                            modelo.setAsiC_IP(ip);
                                            modelo.setAsiC_LATITUD(lat+"");
                                            modelo.setAsiC_LONGITUD(lng+"");
                                            modelo.setAsiC_TIPO(Tipo);
                                            modelo.setAsiC_USU_REG((getFromSharedPreferences("idusuario")));
                                            modelo.setAsiD_FECHA(new SimpleDateFormat("YYYY-MM-dd").format(new java.util.Date()));
                                            modelo.setTraN_ID_TRABAJADOR(Integer.parseInt(getFromSharedPreferences("idusuario")));

                                            Gson _gson = new Gson();
                                            String json = _gson.toJson(modelo);

                                            JSONObject jsonObject = new JSONObject(json);
                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiURL + "Asistencia/AddAsistenciaPrueba", jsonObject, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String respect = response.getString("Valido");
                                                        if (respect == "true") {
                                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡MARCACIÓN DE " + Tipo + " REGISTRADA CORRECTAMENTE: " + new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) + "!").show();
                                                        } else {
                                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE).setTitleText("¡YA SE REGISTRÓ SU MARCACIÓN DE " + Tipo).show();
                                                        }
                                                    } catch (Exception e) {
                                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText(e.getMessage()).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    if (error instanceof ServerError) {
                                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Error interno del servidor").show();

                                                    } else if (error instanceof NoConnectionError) {
                                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE).setTitleText("No hay conexión a internet").show();
                                                    } else {
                                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText(error.getMessage()).show();
                                                    }
                                                }
                                            });
                                            queue.add(jsonObjectRequest);
                                        }
                                    }
                                }
                                if(!encontrado){
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Fuera de horario permitido").show();
                                }
                            } catch (Exception e) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText(e.getMessage()).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error instanceof ServerError) {
                                Toast.makeText(getActivity(), "Error interno del servidor", Toast.LENGTH_SHORT).show();
                            }
                            else if(error instanceof NoConnectionError) {
                                Toast.makeText(getActivity(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    queue.add(stringRequest);
                } catch (Exception e) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText(e.getMessage()).show();
                }
                txtCodigo.setText(result.getContents().toString());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity((MainActivity) getActivity());
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            lat = loc.getLatitude();
            lng = loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            txtCodigo.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
        }
    }
}