package com.example.kevinlay.riffraffler.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

    private static final String RAFFLE_DATABASE_KEY = "raffles";
    private static final String USER_DATABASE_KEY = "user";
    private static final String RAFFLE_TICKET_DATABASE_KEY = "raffleTickets";

    private List<RaffleTicketModel> completedRaffles = new ArrayList<>();
    private List<RaffleTicketModel> allRaffles = new ArrayList<>();
    private List<RaffleTicketModel> rafflesAddedToDatabase = new ArrayList<>();
    private List<RaffleTicketModel> totalRaffleTickets = new ArrayList<>();
    private List<RaffleTicketModel> usersRaffleTickets = new ArrayList<>();
    private List<RaffleTicketModel> activeRaffles = new ArrayList<>();

    private RecyclerView recyclerView;
    private MyRafflesAdapter adapter;

    private RecyclerView recyclerView2;
    private MyRafflesAdapter adapter2;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private List<String> raffleIds = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_raffles_layout, container, false);
        insertDataToRecyclerView();

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_raffle_layout);
        dialog.setTitle(R.string.create_raffle_dialog);

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
                allRaffles.clear();
                rafflesAddedToDatabase.clear();
                totalRaffleTickets.clear();
                usersRaffleTickets.clear();

                for(DataSnapshot dataSnapshot2 : dataSnapshot.child(RAFFLE_DATABASE_KEY).getChildren()) {
                    RaffleTicketModel raffleTicketModel = dataSnapshot2.getValue(RaffleTicketModel.class);
                    String idKey = raffleTicketModel.getRaffleId();
                    raffleIds.add(idKey);
                    String raffleName = raffleTicketModel.getRaffleName();
                    map.put(idKey, raffleName);
                    totalRaffleTickets.add(raffleTicketModel);
                }


                for(DataSnapshot dataSnapshot2 : dataSnapshot.child(RAFFLE_DATABASE_KEY).getChildren()) {
                    RaffleTicketModel raffleTicketModel = dataSnapshot2.getValue(RaffleTicketModel.class);
                    if(raffleTicketModel.isActive()){
                        allRaffles.add(raffleTicketModel);
                    }
                }

                for(DataSnapshot dataSnapshot2 : dataSnapshot.child(USER_DATABASE_KEY).getChildren()) {
                    User user = dataSnapshot2.getValue(User.class);

                    if (user.getUserId().equals(mAuth.getUid())) {

                        for (int k = 0; k < user.getRaffleTickets().size(); k++) {
                            usersRaffleTickets.add(user.getRaffleTickets().get(k));
                        }
                    }
                }

                for (int k = 0; k < usersRaffleTickets.size(); k++) {

                    for(int i = 0; i < totalRaffleTickets.size(); i++) {

                        if(totalRaffleTickets.get(i).getRaffleId().equals(usersRaffleTickets.get(k).getRaffleId())) {
                            usersRaffleTickets.get(k).setActive(totalRaffleTickets.get(i).isActive());

                            if(totalRaffleTickets.get(i).getWinner().equals(mAuth.getUid())) {
                                usersRaffleTickets.get(k).setRaffleId("WINNER: " + usersRaffleTickets.get(k).getRaffleId()) ;
                            }
                        }
                    }
                }

                for (int k = 0; k < usersRaffleTickets.size(); k++) {

                    if(!usersRaffleTickets.get(k).isActive()) {

                        completedRaffles.add(usersRaffleTickets.get(k));
                    }
                }

                for(DataSnapshot dataSnapshot2 : dataSnapshot.child(USER_DATABASE_KEY).getChildren()) {
                    User user = dataSnapshot2.getValue(User.class);

                    if(user.getUserId().equals(mAuth.getUid())) {
                        rafflesAddedToDatabase.addAll(user.getRaffleTickets());

                        for (int k = 0; k < allRaffles.size(); k++) {

                            for (int i = 0; i < user.getRaffleTickets().size(); i++) {

                                if (rafflesAddedToDatabase.get(i).getRaffleId().equals(allRaffles.get(k).getRaffleId())) {
                                    rafflesAddedToDatabase.get(i).setActive(allRaffles.get(k).isActive());
                                    activeRaffles.add(rafflesAddedToDatabase.get(i));
                                }
                            }
                        }
                        databaseReference.child(USER_DATABASE_KEY)
                                .child(mAuth.getUid())
                                .child(RAFFLE_TICKET_DATABASE_KEY)
                                .setValue(usersRaffleTickets);
                    }

                    user.setRaffleTickets(rafflesAddedToDatabase);
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
            rafflesAddedToDatabase.add(new RaffleTicketModel(id, "", new ArrayList<String>(), name, "", ""));
            databaseReference.child(USER_DATABASE_KEY + "")
                    .child(mAuth.getUid())
                    .child(RAFFLE_TICKET_DATABASE_KEY)
                    .setValue(rafflesAddedToDatabase);

            Toast.makeText(getActivity(),this.getString(R.string.raffle_added_toast) + id, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getActivity(),this.getString(R.string.raffle_invalid_toast) + id, Toast.LENGTH_LONG).show();
        }
    }

}
