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
import java.util.Random;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MyRafflesFragment extends Fragment {

    private static final String TAG = "MyRafflesFragment";

    private List<RaffleTicketModel> myRaffles = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<RaffleTicketModel> removed = new ArrayList<>();
    private List<User> totalUsers = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRafflesAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private Button button, button2;
    private EditText editText, editText2;
    private Dialog dialog, dialog2;
    private String idKey;
    private String winnerId;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private String raffleEndedId;

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
        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.remove_raffle_layout);
        editText = (EditText) dialog.findViewById(R.id.createRaffleName);
        button = (Button) dialog.findViewById(R.id.createRaffleSubmit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToDatbase(editText.getText().toString());
                editText.getText().clear();
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

        editText2 = (EditText) dialog2.findViewById(R.id.editText3);
        button2 = (Button) dialog2.findViewById(R.id.button3);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raffleEndedId = editText2.getText().toString();
                endRaffle(editText2.getText().toString());
                editText2.getText().clear();
                selectWinner(raffleEndedId);
                dialog2.dismiss();
            }
        });

        floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog2.show();
                return true;
            }
        });

        adapter = new MyRafflesAdapter(removed);
        //adapter = new MyRafflesAdapter(myRaffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void insertDataToRecyclerView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(isUpdatingRecord) {

                    //removed.clear();
                    totalUsers.clear();

                    myRaffles.clear();
                    for (DataSnapshot snapshot : dataSnapshot.child("raffles").getChildren()) {
                        RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);
                        if (raffle.getOwner().equals(mAuth.getUid())) {
                            myRaffles.add(raffle);
                            Log.i(TAG, "onDataChange: raffleids" + raffle.getRaffleId());
                        }
                    }

                    for (DataSnapshot snapshot : dataSnapshot.child("raffles").getChildren()) {
                        RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);
                        if (raffle.getOwner().equals(mAuth.getUid()) && raffle.getRaffleId().equals(raffleEndedId)) {
                            raffle.setActive(false);
                            raffle.setWinner(winnerId);
                            databaseReference.child("raffles").child(snapshot.getKey()).setValue(raffle);
                        }
                    }


                    for (DataSnapshot snapshot : dataSnapshot.child("user").getChildren()) {
                        User user = snapshot.getValue(User.class);
                        totalUsers.add(user);
                        if (user.getUserId().equals(mAuth.getUid())) {
                            databaseReference.child("user").child(snapshot.getKey() )
                                    .child("raffleTicketsOwned").setValue(myRaffles);

                        }
                    }

                    Log.i(TAG, "onDataChange: my raffles size inside end raffle " + myRaffles.size());
                    Log.i(TAG, "onDataChange: my removed size inside end raffle " + removed.size());

                    removed.clear();
                    for(int i = 0; i < myRaffles.size(); i++) {
                        if(myRaffles.get(i).isActive()) {
                            removed.add(myRaffles.get(i));
                            Log.i(TAG, "onDataChange: adding active raffle ");
                        }
                    }

                } else {

                    myRaffles.clear();
                    users.clear();

                    for (DataSnapshot snapshot : dataSnapshot.child("raffles").getChildren()) {
                        RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);
                        if (raffle.getOwner().equals(mAuth.getUid())) {
                            myRaffles.add(raffle);
                            Log.i(TAG, "onDataChange: raffleids" + raffle.getRaffleId());
                        }
                    }

                    for (DataSnapshot snapshot : dataSnapshot.child("user").getChildren()) {
                        User user = snapshot.getValue(User.class);
                        users.add(user);
                        if (user.getUserId().equals(mAuth.getUid())) {
                            databaseReference.child("user").child(snapshot.getKey())
                                    .child("raffleTicketsOwned").setValue(myRaffles);
                            //Log.i(TAG, "onDataChange: inside user");
                        }
                    }

                    removed.clear();
                    for(int i = 0; i < myRaffles.size(); i++) {
                        if(myRaffles.get(i).isActive()) {
                            removed.add(myRaffles.get(i));
                            Log.i(TAG, "onDataChange: adding active raffle ");
                        }
                    }
                    Log.i(TAG, "onDataChange: my raffles size inside end raffle " + myRaffles.size());
                    Log.i(TAG, "onDataChange: my removed size inside end raffle " + removed.size());

                    //Log.i(TAG, "onDataChange: there is a winner");
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    boolean isUpdatingRecord = false;

    private void insertDataToDatbase(String s) {
        isUpdatingRecord = false;
        String randomNum = (int) (Math.random() * 999) + "" +((int) (Math.random() * 999) % (int) (Math.random() * 999));
        List<String> emptyList = new ArrayList<>();
        RaffleTicketModel raffleTicketModel = new RaffleTicketModel(randomNum, mAuth.getUid(), emptyList, s);
        databaseReference.child("raffles").push().setValue(raffleTicketModel);

        Log.i(TAG, "calling from: removed ");
    }

    private void endRaffle(String s2) {

        isUpdatingRecord = true;

        for(int i = 0; i < myRaffles.size(); i++) {
            if(myRaffles.get(i).getRaffleId().equals(s2)) {
                //if(myRaffles.get(i).getOwner().equals(mAuth.getUid())) {
                    Log.i(TAG, "endRaffle: id changing is" + myRaffles.get(i).getRaffleId());
                    myRaffles.get(i).setActive(false);
                    myRaffles.remove(myRaffles.get(i));
                    databaseReference.child("user")
                            .child(mAuth.getUid())
                            .child("raffleTicketsOwned")
                            .setValue(myRaffles);
                    adapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getActivity(), "You are not the creator of this raffle", Toast.LENGTH_LONG).show();
//                }
            }
        }


    }

    private void selectWinner(String s) {

        List<User> usersInRaffle = new ArrayList<>();
        usersInRaffle.clear();

        for (int i = 0; i < users.size(); i++) {

            for (int j = 0; j < users.get(i).getRaffleTickets().size(); j++) {

                if(users.get(i).getRaffleTickets().get(j).getRaffleId().equals(s)) {

                    usersInRaffle.add(users.get(i));
                }
            }
        }

        if(usersInRaffle.size() > 0) {

            Random rand = new Random();
            int min = 0;
            int max = usersInRaffle.size();
            int winner = rand.nextInt((max - min) + 1) + min - 1;

            while(winner >= usersInRaffle.size()) {

                winner--;
            }

            if(winner < 0) {

                winner = 0;
                Log.i(TAG, "selectWinner: winner number " + winner);
            }

            Toast.makeText(getActivity(),"Winner is " + usersInRaffle.get(winner).getUserId(), Toast.LENGTH_SHORT).show();

            winnerId = usersInRaffle.get(winner).getUserId();
        } else {

            Toast.makeText(getActivity(),"No one in raffle ", Toast.LENGTH_LONG).show();
            winnerId = 0+"";
        }
    }


}
