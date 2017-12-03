package com.example.kevinlay.riffraffler.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kevinlay.riffraffler.R;
import com.example.kevinlay.riffraffler.adapter.BrowseRafflesAdapter;
import com.example.kevinlay.riffraffler.callbacks.BrowseCallback;
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

// In this case, the fragment displays simple text based on the page
public class BrowerRafflesPageFragment extends Fragment implements BrowseCallback {

    private static final String RAFFLE_DATABASE_KEY = "raffles";
    private static final String USER_DATABASE_KEY = "user";
    private static final String RAFFLE_TICKET_DATABASE_KEY = "raffleTickets";

    private List<RaffleTicketModel> totalRaffles = new ArrayList<>();
    private List<RaffleTicketModel> usersRaffleTickets = new ArrayList<>();
    private RecyclerView recyclerView;
    private BrowseRafflesAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private Button buttonNo, buttonYes;

    private  Dialog dialog;

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
        adapter = new BrowseRafflesAdapter(totalRaffles, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.confirm_raffle_add);

        buttonNo = (Button) dialog.findViewById(R.id.bNoConfirm);
        buttonYes = (Button) dialog.findViewById(R.id.bYesConfirm);

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return view;
    }


    private void insertDataToRecyclerView() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalRaffles.clear();
                usersRaffleTickets.clear();

                // this populates the browse page with data
                for(DataSnapshot dataSnapshot2 : dataSnapshot.child(RAFFLE_DATABASE_KEY).getChildren()) {
                    RaffleTicketModel raffleTicketModel = dataSnapshot2.getValue(RaffleTicketModel.class);
                    if(raffleTicketModel.isActive()) {
                        totalRaffles.add(raffleTicketModel);
                    }
                }

                // this grabs the users raffle tickets, used to add more tickets from browse page
                for(DataSnapshot dataSnapshot2 : dataSnapshot.child(USER_DATABASE_KEY).getChildren()) {
                    User user = dataSnapshot2.getValue(User.class);

                    if (user.getUserId().equals(mAuth.getUid())) {

                        for (int k = 0; k < user.getRaffleTickets().size(); k++) {
                            usersRaffleTickets.add(user.getRaffleTickets().get(k));
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

    @Override
    public void browseAdapterCallback(final String raffleId, final String raffleName) {

        dialog.show();

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersRaffleTickets.add(new RaffleTicketModel(raffleId, "", new ArrayList<String>(), raffleName, "", "",""));
                databaseReference.child(USER_DATABASE_KEY + "")
                        .child(mAuth.getUid())
                        .child(RAFFLE_TICKET_DATABASE_KEY)
                        .setValue(usersRaffleTickets);
                dialog.dismiss();
            }
        });
    }
}