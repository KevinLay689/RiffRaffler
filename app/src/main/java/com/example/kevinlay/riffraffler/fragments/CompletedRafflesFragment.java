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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<String, String> map = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        //Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
                editText.getText().clear();
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

                for(DataSnapshot dataSnapshot2 : dataSnapshot.child("raffles").getChildren()) {
                    RaffleTicketModel raffleTicketModel = dataSnapshot2.getValue(RaffleTicketModel.class);
                    String idKey = raffleTicketModel.getRaffleId();
                    raffleIds.add(idKey);

                    String raffleName = raffleTicketModel.getRaffleName();

                    map.put(idKey, raffleName);

                }

                //Log.i(TAG, "onDataChange: "+ raffleIds.toString());

                for(DataSnapshot dataSnapshot1 : dataSnapshot.child("user").getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    if(user.getUserId().equals(mAuth.getUid())) {
                        for (int i = 0; i < user.getRaffleTickets().size(); i++) {
                            if(user.getRaffleTickets().get(i).isActive()) {
                                if(user.getRaffleTickets().get(i).getRaffleId().equals("0")){
                                    //Log.i(TAG, "onDataChange: found a 0");
                                } else {
                                    activeRaffles.add(user.getRaffleTickets().get(i));
                                }
                            }
                        }
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

            String name = map.get(id);
            //Log.i(TAG, "insertDataToDatabase: name " + name);
            activeRaffles.add(new RaffleTicketModel(id, "", new ArrayList<String>(), name));
            databaseReference.child("user" + "")
                    .child(mAuth.getUid())
                    .child("raffleTickets")
                    .setValue(activeRaffles);

            Toast.makeText(getActivity(),"Added Raffle: "+ id, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getActivity(),"Invalid Raffle Id: "+ id, Toast.LENGTH_LONG).show();
        }

    }

}
