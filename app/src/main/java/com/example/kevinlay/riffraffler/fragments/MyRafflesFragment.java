package com.example.kevinlay.riffraffler.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by kevinlay on 11/8/17.
 */

public class MyRafflesFragment extends Fragment {

    private static final String TAG = "MyRafflesFragment";

    private List<RaffleTicketModel> raffles = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRafflesAdapter adapter;
    private FloatingActionButton floatingActionButton;

    private String idKey;

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
    public void onStart() {
        super.onStart();
        insertDataToRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        idKey = bundle.getString("key");

        Log.i(TAG, "onCreateView: " + idKey);

        View view = inflater.inflate(R.layout.fragment_my_raffles_layout, container, false);

        recyclerView = view.findViewById(R.id.myRafflesRecycler);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToDatbase();
                //handleAdd();

            }
        });
        adapter = new MyRafflesAdapter(raffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void insertDataToRecyclerView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                raffles.clear();

//                for (DataSnapshot snapshot : dataSnapshot.child("raffles").getChildren()) {
//                    RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);
//                    //Log.i(TAG, "onDataChange: Raffles " + raffle.getRaffleId());
//
//                    raffles.add(raffle);
//                }

                for (DataSnapshot snapshot : dataSnapshot.child("raffles").getChildren()) {
                    RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);
                    //Log.i(TAG, "onDataChange: Raffles " + raffle.getRaffleId());

//                    Log.i(TAG, "onDataChange: ownerKey" + raffle.getOwner());
//                    Log.i(TAG, "onDataChange: authkey" + mAuth.getUid());
                    if(raffle.getOwner().equals(mAuth.getUid())) {
                        raffles.add(raffle);
                        //Log.i(TAG, "onDataChange: inside adding raffle");
                    }

                }



                for (DataSnapshot snapshot : dataSnapshot.child("user").getChildren()) {
                    User user = snapshot.getValue(User.class);
                    //Log.i(TAG, "onDataChange: userIdkey is  " + user.getUserId());
                    //Log.i(TAG, "onDataChange: mauthkey is  " + mAuth.getUid());
                    if(user.getUserId().equals(mAuth.getUid())) {

                        databaseReference.child("user").child(snapshot.getKey())
                                .child("raffleTicketsOwned").setValue(raffles);
                    }
                    //Log.i(TAG, "onDataChange: User ID " + user.getUserId());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void insertDataToDatbase() {
        String randomNum = (int) (Math.random() * 999) + "" +((int) (Math.random() * 999) % (int) (Math.random() * 999)); ;
        List<String> emptyList = new ArrayList<>();

        RaffleTicketModel raffleTicketModel = new RaffleTicketModel(randomNum, mAuth.getUid(), emptyList);

        databaseReference.child("raffles").push().setValue(raffleTicketModel);
    }

//    private void insertDataToDatbase2() {
//        MyRafflesModel myRafflesModel = new MyRafflesModel("name", "test", "0" );
//        databaseReference.child(mAuth.getUid()).child("myRaffles").push().setValue(myRafflesModel);
//        raffles.add(myRafflesModel);
//    }

    private void prepareTestData() {
//        for(int i = 0; i < 3; i++) {
//            RaffleTicketModel myRafflesModel1 = new RaffleTicketModel("Kevins raffle "+ i, "www.google.com", new ArrayList<String>());
//            raffles.add(myRafflesModel1);
//        }
    }

}
