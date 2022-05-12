package com.example.sajal.dtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sajal.dtc.adapterClass.BusStopDetails;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BusRoadMap extends AppCompatActivity {
    private RecyclerView stopage_recyclerview;
    private ImageView back;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter ad;
    TextView arrival;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_road_map);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        stopage_recyclerview = findViewById(R.id.stopage_recyclerview);
        back = findViewById(R.id.back);
        stopage_recyclerview.setItemAnimator(null);
        arrival = findViewById(R.id.arrival);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Root Data...");
        progressDialog.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SharedPreferences sharedPref = getSharedPreferences("mode1", MODE_PRIVATE);
        String A = sharedPref.getString("X", "");
// Query for fetching data from firestore database

        Query query = firebaseFirestore.collection(A).orderBy("km_details");

        // Recycler options
        FirestoreRecyclerOptions<BusStopDetails> options = new FirestoreRecyclerOptions.Builder<BusStopDetails>()
                .setQuery(query, BusStopDetails.class)
                .build();

        ad = new FirestoreRecyclerAdapter<BusStopDetails, BusStopViewHolder>(options) {
            @NonNull
            @Override
            public BusStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stopage_layout, parent, false);
                return new BusStopViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BusStopViewHolder holder, int position, @NonNull BusStopDetails model) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                holder.bus_time.setText(model.getBus_time());
                holder.stopage_name.setText(model.getStopage_name());
                holder.km_details.setText(model.getKm_details());
            }
        };

        stopage_recyclerview.setHasFixedSize(true);
        stopage_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        stopage_recyclerview.setAdapter(ad);



    }
    private class BusStopViewHolder extends RecyclerView.ViewHolder  {

        private TextView bus_time;
        private TextView stopage_name;
        private TextView km_details;

        public BusStopViewHolder(@NonNull View itemView) {
            super(itemView);

            bus_time = itemView.findViewById(R.id.bus_time);
            stopage_name = itemView.findViewById(R.id.stopage_name);
            km_details = itemView.findViewById(R.id.km_details);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        ad.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ad.startListening();
    }


}