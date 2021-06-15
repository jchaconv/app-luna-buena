package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.LoginViewModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText edtUsuario, edtPassword;
    private Button btnLogin;
    private static RequestQueue queue;
    private static String ApiURL = "http://b3rs3rk3r-002-site2.htempurl.com/api/Cuenta/Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("Prefences", Context.MODE_PRIVATE);
        setCredentialsIfExist();
        edtUsuario = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = edtUsuario.getText().toString();
                String password = edtPassword.getText().toString();
                if (edtUsuario.getText().toString().trim().isEmpty() || edtPassword.getText().toString().trim().isEmpty()) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Ambos campos son obligatorios").show();
                    return;
                }
                //CONECTANDO CON EL API
                queue = Volley.newRequestQueue(getApplicationContext());
                Map<String, String> params = new HashMap();
                params.put("username", edtUsuario.getText().toString());
                params.put("password", edtPassword.getText().toString());
                JSONObject jsonObject = new JSONObject(params);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiURL, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {//DESERIALIZANDO RESPUESTA
                            Gson gson = new Gson();
                            try {
                                LoginViewModel respuesta = gson.fromJson(response.toString(), LoginViewModel.class);
                                if (respuesta.getValido() != null && !respuesta.getValido()) { //VALIDANDO CREDENCIALES ENVIADAS
                                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText(respuesta.getRespuesta()).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            //GUARDANDO VARIABLES DE SESION
                            String idUser = response.getJSONArray("datos").getJSONObject(0).getString("traN_ID_TRABAJADOR");
                            saveLoginSharedPreferences("idusuario", idUser);
                            //ACCEDIENDO A MAIN
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   //PARA QUE CUANDO SE PRESIONE ATRAS NO REGRESE AL LOGIN
                            startActivity(intent);


                            /*
                            Gson gson = new Gson();
                            LoginViewModel respuesta = gson.fromJson(response.toString(), LoginViewModel.class);
                            if (respuesta.getValido() != null && !respuesta.getValido()) { //VALIDANDO CREDENCIALES ENVIADAS
                                Toast.makeText(getApplicationContext(), respuesta.getRespuesta(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //ACCEDIENDO A MAIN
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   //PARA QUE CUANDO SE PRESIONE ATRAS NO REGRESE AL LOGIN
                            startActivity(intent);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("usersystem", response.getJSONArray("datos").getJSONObject(0).getString("usersystem"));
                            editor.apply();
                            */

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //quitar
                    public void saveLoginSharedPreferences(String key, String value) {
                        SharedPreferences prefs = getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(key, value);
                        editor.apply();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObjectRequest);
            }
        });
    }

    private void setCredentialsIfExist() {

    }


}