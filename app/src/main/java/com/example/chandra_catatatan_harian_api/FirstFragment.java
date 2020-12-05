package com.example.chandra_catatatan_harian_api;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    View fragment_view;
    ArrayList<Catatan> catatans;
    ProgressBar pb;
    SwipeRefreshLayout srl;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        fragment_view = rootView;

        pb = (ProgressBar) rootView.findViewById(R.id.progress_horizontal);

        //lookup
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        srl.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        load();
        return rootView;
    }

    public void load(){
        pb.setVisibility(ProgressBar.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.getCache().clear();
        String url = "https://chandrarestapi.000webhostapp.com/catatan.php";
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String id, tanggal, catat;
                        catatans = new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("result");
                            catatans.clear();
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    id = data.getString("id").toString().trim();
                                    tanggal = data.getString("tanggal").toString().trim();
                                    catat = data.getString("catat").toString().trim();

                                    catatans.add(new Catatan(id, tanggal, catat));
                                }
                                showRecyclerGrid();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pb.setVisibility(ProgressBar.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Event :", error.toString());
                pb.setVisibility(ProgressBar.GONE);
                Toast.makeText(getContext(), "Please check connection", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjectRequest);
    }


    private void showRecyclerGrid() {
        RecyclerView recyclerView = (RecyclerView) fragment_view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CatatanAdapter mAdapter = new CatatanAdapter(getContext(), catatans);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }



//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
//    }
}