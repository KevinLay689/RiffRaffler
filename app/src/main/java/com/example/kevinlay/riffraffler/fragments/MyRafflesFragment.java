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
    private static final String RAFFLE_DATABASE_KEY = "raffles";
    private static final String USER_DATABASE_KEY = "user";
    private static final String RAFFLE_TICKET_DATABASE_KEY = "raffleTicketsOwned";

    private List<RaffleTicketModel> myInitialRaffles = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<RaffleTicketModel> myFinalRaffles = new ArrayList<>();

    private RecyclerView recyclerView;
    private MyRafflesAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private Button button, button2;
    private EditText createRaffle, endRaffle;
    private EditText  raffleElig, rafflePrize;
    private Dialog dialog, dialog2;
    private String winnerId;
    private boolean isUpdatingRecord = false;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private String raffleEndedId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

        View view = inflater.inflate(R.layout.fragment_my_raffles_layout, container, false);
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.create_raffle_layout);
        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.remove_raffle_layout);
        createRaffle = (EditText) dialog.findViewById(R.id.createRaffleName);
        button = (Button) dialog.findViewById(R.id.createRaffleSubmit);
        raffleElig = (EditText) dialog.findViewById(R.id.createRaffleElig);
        rafflePrize = (EditText) dialog.findViewById(R.id.createRafflePrize);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToDatbase(createRaffle.getText().toString(), raffleElig.getText().toString(), rafflePrize.getText().toString());
                createRaffle.getText().clear();
                dialog.dismiss();
            }
        });

        recyclerView = view.findViewById(R.id.myRafflesRecycler);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        endRaffle = (EditText) dialog2.findViewById(R.id.editText3);
        button2 = (Button) dialog2.findViewById(R.id.button3);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raffleEndedId = endRaffle.getText().toString();
                endRaffle(endRaffle.getText().toString());
                endRaffle.getText().clear();
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

        adapter = new MyRafflesAdapter(myFinalRaffles);
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

                    myInitialRaffles.clear();
                    myFinalRaffles.clear();

                    for (DataSnapshot snapshot : dataSnapshot.child(RAFFLE_DATABASE_KEY).getChildren()) {
                        RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);

                        if (raffle.getOwner().equals(mAuth.getUid())) {

                            myInitialRaffles.add(raffle);
                        }
                    }

                    for (DataSnapshot snapshot : dataSnapshot.child(RAFFLE_DATABASE_KEY).getChildren()) {
                        RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);

                        if (raffle.getOwner().equals(mAuth.getUid()) && raffle.getRaffleId().equals(raffleEndedId)) {

                            raffle.setActive(false);
                            raffle.setWinner(winnerId);
                            databaseReference.child(RAFFLE_DATABASE_KEY)
                                    .child(snapshot.getKey())
                                    .setValue(raffle);
                        }
                    }

                    for (DataSnapshot snapshot : dataSnapshot.child(USER_DATABASE_KEY).getChildren()) {
                        User user = snapshot.getValue(User.class);

                        if (user.getUserId().equals(mAuth.getUid())) {

                            databaseReference.child(USER_DATABASE_KEY)
                                    .child(snapshot.getKey() )
                                    .child(RAFFLE_TICKET_DATABASE_KEY)
                                    .setValue(myInitialRaffles);
                        }
                    }

                    for(int i = 0; i < myInitialRaffles.size(); i++) {

                        if(myInitialRaffles.get(i).isActive()) {
                            myFinalRaffles.add(myInitialRaffles.get(i));
                        }
                    }

                } else {

                    myInitialRaffles.clear();
                    users.clear();
                    myFinalRaffles.clear();

                    for (DataSnapshot snapshot : dataSnapshot.child(RAFFLE_DATABASE_KEY).getChildren()) {
                        RaffleTicketModel raffle = snapshot.getValue(RaffleTicketModel.class);

                        if (raffle.getOwner().equals(mAuth.getUid())) {
                            myInitialRaffles.add(raffle);
                        }
                    }

                    for (DataSnapshot snapshot : dataSnapshot.child(USER_DATABASE_KEY).getChildren()) {
                        User user = snapshot.getValue(User.class);
                        users.add(user);

                        if (user.getUserId().equals(mAuth.getUid())) {
                            databaseReference.child(USER_DATABASE_KEY)
                                    .child(snapshot.getKey())
                                    .child(RAFFLE_TICKET_DATABASE_KEY)
                                    .setValue(myInitialRaffles);
                        }
                    }

                    for(int i = 0; i < myInitialRaffles.size(); i++) {

                        if(myInitialRaffles.get(i).isActive()) {

                            myFinalRaffles.add(myInitialRaffles.get(i));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void insertDataToDatbase(String name, String elig, String prize) {
        isUpdatingRecord = false;
        String randomNum = (int) (Math.random() * 999) + "" +((int) (Math.random() * 999) % (int) (Math.random() * 999));
        List<String> emptyList = new ArrayList<>();
        RaffleTicketModel raffleTicketModel = new RaffleTicketModel(randomNum, mAuth.getUid(), emptyList, name, elig, prize);
        databaseReference.child(RAFFLE_DATABASE_KEY).push().setValue(raffleTicketModel);
    }

    private void endRaffle(String s2) {
        isUpdatingRecord = true;

        for(int i = 0; i < myInitialRaffles.size(); i++) {

            if(myInitialRaffles.get(i).getRaffleId().equals(s2)) {

                myInitialRaffles.get(i).setActive(false);
                myInitialRaffles.remove(myInitialRaffles.get(i));
                    databaseReference.child(USER_DATABASE_KEY)
                            .child(mAuth.getUid())
                            .child(RAFFLE_TICKET_DATABASE_KEY)
                            .setValue(myInitialRaffles);
                    adapter.notifyDataSetChanged();
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
            }
            Toast.makeText(getActivity(), this.getString(R.string.select_winner_toast) + usersInRaffle.get(winner).getUserId(), Toast.LENGTH_SHORT).show();
            winnerId = usersInRaffle.get(winner).getUserId();

        } else {

            Toast.makeText(getActivity(), this.getString(R.string.empty_winner_toast), Toast.LENGTH_LONG).show();
            winnerId = 0+"";
        }
    }


}
