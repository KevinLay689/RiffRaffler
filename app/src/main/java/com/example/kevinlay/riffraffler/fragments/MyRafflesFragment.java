package com.example.kevinlay.riffraffler.fragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.kevinlay.riffraffler.LoginActivity;
import com.example.kevinlay.riffraffler.adapter.MyRafflesAdapter;
import com.example.kevinlay.riffraffler.model.MyRafflesModel;
import com.example.kevinlay.riffraffler.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MyRafflesFragment extends Fragment {

    private static final String TAG = "MyRafflesFragment";

    private List<MyRafflesModel> raffles = new ArrayList<>();
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
                insertDataToRecyclerView();
                //handleAdd();
            }
        });
        adapter = new MyRafflesAdapter(raffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void handleAdd() {
//        Toast.makeText(getActivity(), "Clicked Button", Toast.LENGTH_SHORT).show();
//        MyRafflesModel myRafflesModel1 = new MyRafflesModel("Kevins raffle ", 0);
//        raffles.add(myRafflesModel1);
//        adapter.notifyDataSetChanged();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void insertDataToRecyclerView() {
        double random = Math.random() * 99999;
        MyRafflesModel myRafflesModel = new MyRafflesModel("name", "test", random+"" );
        databaseReference.child(mAuth.getUid()).child("raffles").push().setValue(myRafflesModel);
        raffles.add(myRafflesModel);
    }

    private void prepareTestData() {
        for(int i = 0; i < 3; i++) {
            MyRafflesModel myRafflesModel1 = new MyRafflesModel("Kevins raffle "+ i, "www.google.com", "");
            raffles.add(myRafflesModel1);
        }
    }

}
