package com.example.chandra_catatatan_harian_api;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etId, etTanggal, etCatat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                // tarik layout
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.form_input_catatan_layout, null);
                dialog.setView(view);
                dialog.setCancelable(true);

                // definisi objek
                etId = (EditText) view.findViewById(R.id.et_id);
                etTanggal = (EditText) view.findViewById(R.id.et_tanggal);
                etCatat = (EditText) view.findViewById(R.id.et_catat);

                dialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String id, tanggal, catat;

                        id = etId.getText().toString();
                        tanggal = etTanggal.getText().toString();
                        catat = etCatat.getText().toString();

                        // simpan catatan
                        simpanCatatan(id, tanggal, catat);
                    }
                });

                dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


    }

    private void simpanCatatan(String id, String tanggal, String catat) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://chandrarestapi.000webhostapp.com/catatan.php?action=simpan&id=" + id + "&tanggal=" + tanggal + "&catat=" + catat + "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String id, tanggal, catat;

                if (response.optString("result").equals("true")) {
                    Toast.makeText(getApplicationContext(), "Yeay, data bertambah!", Toast.LENGTH_SHORT).show();

                    // panggil fungsi load pada fragment
                    loadFragment(new FirstFragment());
                } else {
                    Toast.makeText(getApplicationContext(), "O ow, sepertinya harus dicoba lagi!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Events: ", error.toString());

                Toast.makeText(getApplicationContext(), "Hmm, masalah internet mungkin kuota anda habis atau data yang dimasukkan salah, sepertinya harus dicoba lagi!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}