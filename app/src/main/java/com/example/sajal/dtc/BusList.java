package com.example.sajal.dtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajal.dtc.adapterClass.BusRowDetails;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class BusList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    TextView arrival, stopage;
    private ImageView back;
    ConstraintLayout click;
    TextView bus_number;
    TextView RootCode;
    ProgressDialog progressDialog;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        arrival = findViewById(R.id.arrival);
        stopage = findViewById(R.id.stopage);
        back = findViewById(R.id.back);
        click = findViewById(R.id.click);
        bus_number = findViewById(R.id.bus_number);
        RootCode = findViewById(R.id.RootCode);

        recyclerView.setItemAnimator(null);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Bus Data...");
        progressDialog.show();


        SharedPreferences sharedPreferences = getSharedPreferences("demo", MODE_PRIVATE);
        String from = sharedPreferences.getString("keystart", "");
        String to = sharedPreferences.getString("keystop", "");

        stopage.setText(to);
        arrival.setText(from);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        // Query to retrieve data from firestore database

        Query query = firebaseFirestore.collection(from + to).orderBy("busName"); // accending order bu busName field

        firebaseFirestore.collection(from + to)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (DocumentSnapshot document : task.getResult()) {
//                                    Log.d(FTAG, "Room already exists, start the chat");
//


                                }
                            } else {
//
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "No data found!🥺 please search another Stoppage😄", Toast.LENGTH_LONG).show();


                            }
                        } else {
//                            Log.d(FTAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getApplicationContext(), "Error gettin documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // RecyclerOptions
        FirestoreRecyclerOptions<BusRowDetails> options = new FirestoreRecyclerOptions.Builder<BusRowDetails>()
                .setQuery(query, BusRowDetails.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<BusRowDetails, BusRowDetailsViewHolder>(options) {
            @NonNull
            @Override
            public BusRowDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().from(parent.getContext()).inflate(R.layout.bus_row, parent, false);
                return new BusRowDetailsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BusRowDetailsViewHolder holder, int position, @NonNull BusRowDetails model) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String A = model.getRootCode().trim();
                        SharedPreferences sharedPref = getSharedPreferences("mode1", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("X", A);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), BusRoadMap.class);
                        startActivity(intent);
                    }
                });
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                holder.bus_number.setText(model.getBusNumber());
                holder.starting_time.setText(model.getStartingTime());
                holder.ending_time.setText(model.getEndingTime());
                holder.availability.setText(model.getAvailability());
                holder.bus_name.setText(model.getBusName());
                holder.rootCode.setText(model.getRootCode());
                holder.bus_fares.setText(model.getBusFares());


            }
        };


        // View Holder


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private static class BusRowDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView bus_number;
        private final TextView starting_time;
        private final TextView ending_time;
        private final TextView availability;
        private final TextView bus_name;
        private final TextView rootCode;
        private final TextView bus_fares;


        public BusRowDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            bus_number = itemView.findViewById(R.id.bus_number);
            starting_time = itemView.findViewById(R.id.starting_time);
            ending_time = itemView.findViewById(R.id.ending_time);
            availability = itemView.findViewById(R.id.availability);
            bus_name = itemView.findViewById(R.id.bus_time);
            rootCode = itemView.findViewById(R.id.RootCode);
            bus_fares = itemView.findViewById(R.id.bus_fare);


        }

        @Override
        public void onClick(View v) {

        }


        public interface OnNoteListener {
            void onNoteClick(View view, int position);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
