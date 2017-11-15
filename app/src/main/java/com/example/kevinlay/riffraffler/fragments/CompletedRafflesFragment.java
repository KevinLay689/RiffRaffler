package com.example.kevinlay.riffraffler.fragments;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private List<String> raffleIds = new ArrayList<>();

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
        insertDataToRecyclerView();

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_raffle_layout);
        dialog.setTitle("Create Raffle");

        final EditText editText = dialog.findViewById(R.id.editText2);
        Button button = dialog.findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToDatabase(editText.getText().toString());
                dialog.dismiss();
            }
        });
        recyclerView = view.findViewById(R.id.activeRaffles);
        adapter = new MyRafflesAdapter(completedRaffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        FloatingActionButton b = ((FloatingActionButton) view.findViewById(R.id.button12));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertDataToDatabase();
                dialog.show();
            }
        });
//        Button b2 = ((Button) view.findViewById(R.id.button123));
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                insertDataToDatabase2();
//            }
//        });

        recyclerView2 = view.findViewById(R.id.completedRaffles);
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
                activeRaffles.clear();
                completedRaffles.clear();
                raffleIds.clear();
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.child("user").getChildren()) {
//                    User user = dataSnapshot1.getValue(User.class);
//                    if(user.getUserId().equals(mAuth.getUid())) {
//                        for (int i = 0; i < user.getRaffleTickets().size(); i++) {
//                            activeRaffles.add(user.getRaffleTickets().get(i));
//                        }
//                    }
//                }

                for(DataSnapshot dataSnapshot2 : dataSnapshot.child("raffles").getChildren()) {
                    RaffleTicketModel raffleTicketModel = dataSnapshot2.getValue(RaffleTicketModel.class);
                    String idKey = raffleTicketModel.getRaffleId();
                    raffleIds.add(idKey);
                }

                Log.i(TAG, "onDataChange: "+ raffleIds.toString());

                for(DataSnapshot dataSnapshot1 : dataSnapshot.child("user").getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    if(user.getUserId().equals(mAuth.getUid())) {
                        for (int i = 0; i < user.getRaffleTickets().size(); i++) {
                            if(user.getRaffleTickets().get(i).isActive()) {
                                activeRaffles.add(user.getRaffleTickets().get(i));
                            }
                        }
//                        for (int i = 0; i < user.getRaffleTickets().size(); i++) {
//                            if(!user.getRaffleTickets().get(i).isActive()) {
//                                completedRaffles.add(user.getRaffleTickets().get(i));
//                            }
//                        }
                    }
                }

                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void insertDataToDatabase(String id) {
        if(raffleIds.contains(id)) {
            activeRaffles.add(new RaffleTicketModel(id, "someone", new ArrayList<String>()));
            Log.i(TAG, "insertDataToDatbase: users " + databaseReference.child("user" + "")
                    .child(mAuth.getUid()).child("raffleTickets").setValue(activeRaffles));
        } else {
            Toast.makeText(getActivity(),"Could not find id: "+ id, Toast.LENGTH_LONG).show();
        }

    }
    private void insertDataToDatabase2() {
        RaffleTicketModel raffleTicketModel = new RaffleTicketModel("me", "someone", new ArrayList<String>());
        raffleTicketModel.setActive(false);
        activeRaffles.add(raffleTicketModel);
        Log.i(TAG, "insertDataToDatbase2: users " + databaseReference.child("user" + "")
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
