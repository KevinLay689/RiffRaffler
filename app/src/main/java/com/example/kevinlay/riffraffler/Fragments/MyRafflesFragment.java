package com.example.kevinlay.riffraffler.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kevinlay.riffraffler.adapter.MyRafflesAdapter;
import com.example.kevinlay.riffraffler.MyRafflesModel;
import com.example.kevinlay.riffraffler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MyRafflesFragment extends Fragment {

    private List<MyRafflesModel> raffles = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRafflesAdapter adapter;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareTestData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_raffles_layout, container, false);
        recyclerView = view.findViewById(R.id.myRafflesRecycler);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAdd();
            }
        });
        adapter = new MyRafflesAdapter(raffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void handleAdd() {
        Toast.makeText(getActivity(), "Clicked Button", Toast.LENGTH_SHORT).show();
        MyRafflesModel myRafflesModel1 = new MyRafflesModel("Kevins raffle ", 0);
        raffles.add(myRafflesModel1);
        adapter.notifyDataSetChanged();
    }

    private void prepareTestData() {
        for(int i = 0; i < 3; i++) {
            MyRafflesModel myRafflesModel1 = new MyRafflesModel("Kevins raffle "+ i, 0);
            raffles.add(myRafflesModel1);
        }
    }

}
