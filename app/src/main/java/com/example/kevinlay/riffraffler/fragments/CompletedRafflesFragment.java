package com.example.kevinlay.riffraffler.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kevinlay.riffraffler.adapter.MyRafflesAdapter;
import com.example.kevinlay.riffraffler.R;
import com.example.kevinlay.riffraffler.model.RaffleTicketModel;
import com.example.kevinlay.riffraffler.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by kevinlay on 11/8/17.
 */

public class CompletedRafflesFragment extends Fragment {

    private List<RaffleTicketModel> completedRaffles = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRafflesAdapter adapter;

    private List<RaffleTicketModel> activeRaffles = new ArrayList<>();
    private RecyclerView recyclerView2;
    private MyRafflesAdapter adapter2;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        //Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        prepareTestData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_raffles_layout, container, false);
        recyclerView = view.findViewById(R.id.completedRaffles);
        adapter = new MyRafflesAdapter(completedRaffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Button b = ((Button) view.findViewById(R.id.button12));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToDatabase();
            }
        });

        recyclerView2 = view.findViewById(R.id.activeRaffles);
        adapter2 = new MyRafflesAdapter(activeRaffles);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(adapter2);

        return view;
    }


    private void insertDataToRecyclerView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                completedRaffles.clear();

                for (DataSnapshot snapshot : dataSnapshot.child("raffles").getChildren()) {
                    RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);
                    if(raffle.getOwner().equals(mAuth.getUid())) {
                        completedRaffles.add(raffle);
                        //Log.i(TAG, "onDataChange: inside adding raffle");
                    }
                }
                for (DataSnapshot snapshot : dataSnapshot.child("user").getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if(user.getUserId().equals(mAuth.getUid())) {

                        databaseReference.child("user").child(snapshot.getKey())
                                .child("raffleTicketsOwned").setValue(completedRaffles);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void insertDataToDatabase() {
        activeRaffles.add(new RaffleTicketModel("me", "someone", new ArrayList<String>()));
        Log.i(TAG, "insertDataToDatbase: users " + databaseReference.child("user" +
                "")
                .child(mAuth.getUid()).child("raffleTickets").setValue(activeRaffles));

    }


    private void prepareTestData() {
//        for(int i = 0; i < 20; i++) {
//            RaffleTicketModel myRafflesModel1 = new RaffleTicketModel("Kevins raffle "+ i, "www.google.com", new ArrayList<String>());
//            raffles.add(myRafflesModel1);
//        }
//        for(int i = 0; i < 20; i++) {
//            RaffleTicketModel myRafflesModel1 = new RaffleTicketModel("Kevins raffle "+ i, "www.google.com", new ArrayList<String>());
//            raffles2.add(myRafflesModel1);
//        }
//        if(raffles.size() <= 0 ) {
//            RaffleTicketModel myRafflesModel1 = new RaffleTicketModel("no raffle ", "www.google.com", new ArrayList<String>());
//            raffles.add(myRafflesModel1);
//        }
//        if(raffles2.size() < 0 ) {
//            RaffleTicketModel myRafflesModel1 = new RaffleTicketModel("no raffle ", "www.google.com", new ArrayList<String>());
//            raffles2.add(myRafflesModel1);
//        }

    }
}
