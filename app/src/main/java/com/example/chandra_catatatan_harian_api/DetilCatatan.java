package com.example.chandra_catatatan_harian_api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DetilCatatan extends AppCompatActivity {
    ProgressBar pb;
    EditText etID,etTanggal, etCatat;
    Button btHapus, btUbah;
    String id,tanggal,catat;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_catatan);

        pb      = (ProgressBar) findViewById(R.id.pb);
        etID    = (EditText) findViewById(R.id.et_id);
        etTanggal  = (EditText) findViewById(R.id.et_tanggal);
        etCatat  = (EditText) findViewById(R.id.et_catat);
        btUbah  = (Button) findViewById(R.id.bt_ubah);
        btHapus = (Button) findViewById(R.id.bt_hapus);

        //tangkap bundle
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();

        //letakan isi bundle
        id   = bundle.getString("b_id");
        tanggal = bundle.getString("b_tanggal");
        catat = bundle.getString("b_catat");

        //letakan pada textview
        etID.setText(id);
        etTanggal.setText(tanggal);
        etCatat.setText(catat);

        //operasi ubah data
        btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggal = etTanggal.getText().toString();
                catat   = etCatat.getText().toString();

                pb.setVisibility(ProgressBar.VISIBLE);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://chandrarestapi.000webhostapp.com/catatan.php?action=ubah&id=" + id + "&tanggal=" + tanggal + "&catat=" + catat + "";
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                        Request.Method.POST, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String id, tanggal,catat;
                                if (response.optString("result").equals("true")) {
                                    Toast.makeText(getApplicationContext(), "Data Berubah !", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Coba Lagi, Anda Belum Beruntung !", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(ProgressBar.GONE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Events :", error.toString());
                        Toast.makeText(getApplicationContext(),"Masalah Internet Boss !",Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsObjRequest);
            }
        });

        //operasi ubah data
        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggal = etTanggal.getText().toString();
                catat = etCatat.getText().toString();

                pb.setVisibility(ProgressBar.VISIBLE);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://chandrarestapi.000webhostapp.com/catatan.php?action=hapus&id=" + id + "";
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                        Request.Method.POST, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String id, tanggal, catat;
                                if (response.optString("result").equals("true")) {
                                    Toast.makeText(getApplicationContext(), "Data Berubah !", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Coba Lagi  !", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(ProgressBar.GONE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Events :", error.toString());
                        Toast.makeText(getApplicationContext(),"Masalah Internet Mungkin !",Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsObjRequest);
            }
        });
    }
}







