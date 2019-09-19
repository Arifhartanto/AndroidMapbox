package com.app.pesona;

import android.app.ProgressDialog;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Saran extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextName;
    private EditText editTextDesg;

    private Button buttonAdd;
    String tanggal_sekarang = getCurrentDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDesg = (EditText) findViewById(R.id.editTextDesg);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(this);
    }

    private void addEmployee(){

        final String nama = editTextName.getText().toString().trim();
        final String saran = editTextDesg.getText().toString().trim();
        final String tgl = tanggal_sekarang.trim();
        final String status = "PENDING".trim();

        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Saran.this,"Mengirim...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Saran.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_EMP_NAMA,nama);
                params.put(Konfigurasi.KEY_EMP_SARAN,saran);
                params.put(Konfigurasi.KEY_EMP_TANGGAL,tgl);
                params.put(Konfigurasi.KEY_EMP_STATUS,status);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Konfigurasi.URL_ADD, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    public String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "/" + (month+1) + "/" + year;
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addEmployee();
        }
    }
}
