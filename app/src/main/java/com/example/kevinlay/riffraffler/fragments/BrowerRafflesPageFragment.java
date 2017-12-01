package com.example.kevinlay.riffraffler.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevinlay.riffraffler.R;
import com.example.kevinlay.riffraffler.adapter.BrowseRafflesAdapter;
import com.example.kevinlay.riffraffler.model.RaffleTicketModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class BrowerRafflesPageFragment extends Fragment {

    private static final String RAFFLE_DATABASE_KEY = "raffles";

    private List<RaffleTicketModel> totalRaffles = new ArrayList<>();
    private RecyclerView recyclerView;
    private BrowseRafflesAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_page, container, false);
        insertDataToRecyclerView();
        recyclerView = view.findViewById(R.id.my_recycler_view);
        adapter = new BrowseRafflesAdapter(totalRaffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }


    private void insertDataToRecyclerView() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalRaffles.clear();

                for(DataSnapshot dataSnapshot2 : dataSnapshot.child(RAFFLE_DATABASE_KEY).getChildren()) {
                    RaffleTicketModel raffleTicketModel = dataSnapshot2.getValue(RaffleTicketModel.class);
                    if(raffleTicketModel.isActive()) {
                        totalRaffles.add(raffleTicketModel);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}