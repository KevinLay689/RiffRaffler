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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    private Button button;
    private EditText editText;
    private Dialog dialog;
    private String idKey;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        //Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
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

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.create_raffle_layout);

        editText = (EditText) dialog.findViewById(R.id.createRaffleName);
        button = (Button) dialog.findViewById(R.id.createRaffleSubmit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToDatbase(editText.getText().toString());
                dialog.dismiss();
            }
        });

        recyclerView = view.findViewById(R.id.myRafflesRecycler);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
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

                for (DataSnapshot snapshot : dataSnapshot.child("raffles").getChildren()) {
                    RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);
                    if(raffle.getOwner().equals(mAuth.getUid())) {
                        raffles.add(raffle);
                    }
                }

                for (DataSnapshot snapshot : dataSnapshot.child("user").getChildren()) {
                    User user = snapshot.getValue(User.class);
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

    private void insertDataToDatbase(String s) {
        String randomNum = (int) (Math.random() * 999) + "" +((int) (Math.random() * 999) % (int) (Math.random() * 999)); ;
        List<String> emptyList = new ArrayList<>();

        RaffleTicketModel raffleTicketModel = new RaffleTicketModel(randomNum, mAuth.getUid(), emptyList, s);

        databaseReference.child("raffles").push().setValue(raffleTicketModel);
    }

}
