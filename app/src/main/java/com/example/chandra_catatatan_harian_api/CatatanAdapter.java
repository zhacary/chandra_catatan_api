package com.example.chandra_catatatan_harian_api;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.GridviewHolder> {

    private List<Catatan> catatans;
    private Context context;

    public CatatanAdapter(Context context, List<Catatan> catatans) {
        this.catatans = catatans;
        this.context = context;
    }

    @NonNull
    @Override
    public CatatanAdapter.GridviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        GridviewHolder viewHolder = new GridviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CatatanAdapter.GridviewHolder holder, int position) {
        final int pos = position;
        final String id = catatans.get(position).getId();
        final String tanggal = catatans.get(position).getTanggal();
        final String catat = catatans.get(position).getCatat();

        holder.tvId.setText(id);
        holder.tvTanggal.setText(tanggal);
        holder.tvCatat.setText(catat);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Operasi data");
                alertDialog.setMessage(id + " - " + tanggal);
                alertDialog.setPositiveButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("Lihat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle b = new Bundle();
                        b.putString("b_id",id);
                        b.putString("b_tanggal",tanggal);
                        b.putString("b_catat",catat);

                        Intent intent = new Intent(context, DetilCatatan.class);
                        intent.putExtras(b);

                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
                            ((Activity)context).startActivityForResult(intent,1,b);
                        }
                    }
                });
                alertDialog.setNeutralButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url = "https://chandrarestapi.000webhostapp.com/catatan.php?action=hapus&id=" + id;
                        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(
                                Request.Method.POST, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        String id, tanggal, catat;
                                        if (response.optString("result").equals("true")) {
                                            Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                            catatans.remove(pos);
                                            notifyItemRemoved(pos);
                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(context, "Data gagal dihapus, silakan coba lagi", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Events :", error.toString());
                                Toast.makeText(context, "Masalah internet atau data yang dimasukan", Toast.LENGTH_SHORT).show();
                            }
                        }); queue.add(jsObjectRequest);


                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        });

    }

    @Override
    public int getItemCount() {
        return catatans.size();
    }

    public class GridviewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView tvTanggal;
        TextView tvCatat;


        public GridviewHolder(@NonNull View itemView) {
            super(itemView);

            tvId    = (TextView) itemView.findViewById(R.id.tv_id);
            tvTanggal  = (TextView) itemView.findViewById(R.id.tv_tanggal);
            tvCatat  = (TextView) itemView.findViewById(R.id.tv_catat);
        }
    }
}
