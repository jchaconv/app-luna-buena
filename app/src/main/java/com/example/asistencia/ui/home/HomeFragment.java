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

import com.example.asistencia.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeFragment extends Fragment {

    TextView txtCodigo;
    Button btnCodigo;

    public HomeFragment(){
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtCodigo = view.findViewById(R.id.txtCodigo);
        btnCodigo = view.findViewById(R.id.btncodigo);

        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearQR();
            }
        });
        return view;
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
                txtCodigo.setText(result.getContents().toString());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}