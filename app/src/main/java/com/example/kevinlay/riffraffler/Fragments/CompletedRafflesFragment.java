package com.example.kevinlay.riffraffler.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevinlay.riffraffler.adapter.MyRafflesAdapter;
import com.example.kevinlay.riffraffler.MyRafflesModel;
import com.example.kevinlay.riffraffler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinlay on 11/8/17.
 */

public class CompletedRafflesFragment extends Fragment {

    private List<MyRafflesModel> raffles = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRafflesAdapter adapter;

    private List<MyRafflesModel> raffles2 = new ArrayList<>();
    private RecyclerView recyclerView2;
    private MyRafflesAdapter adapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_raffles_layout, container, false);
        prepareTestData();
        recyclerView = view.findViewById(R.id.completedRafflesWin);
        adapter = new MyRafflesAdapter(raffles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView2 = view.findViewById(R.id.completedRafflesLose);
        adapter2 = new MyRafflesAdapter(raffles2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(adapter2);

        return view;
    }

    private void prepareTestData() {
        for(int i = 0; i < 5; i++) {
            MyRafflesModel myRafflesModel1 = new MyRafflesModel("Kevins raffle "+ i, 0);
            raffles.add(myRafflesModel1);
        }
        for(int i = 0; i < 5; i++) {
            MyRafflesModel myRafflesModel1 = new MyRafflesModel("Kevins raffle "+ i, 0);
            raffles2.add(myRafflesModel1);
        }
        if(raffles.size() <= 0 ) {
            MyRafflesModel myRafflesModel1 = new MyRafflesModel("No Raffles ", 0);
            raffles.add(myRafflesModel1);
        }
        if(raffles2.size() < 0 ) {
            MyRafflesModel myRafflesModel1 = new MyRafflesModel("No Raffles ", 0);
            raffles2.add(myRafflesModel1);
        }

    }
}
