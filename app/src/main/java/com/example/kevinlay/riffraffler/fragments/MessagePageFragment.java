package com.example.kevinlay.riffraffler.fragments;

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

import com.example.kevinlay.riffraffler.model.MessagesModel;
import com.example.kevinlay.riffraffler.R;
import com.example.kevinlay.riffraffler.adapter.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class MessagePageFragment extends Fragment {

    private List<MessagesModel> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter adapter;

    private FloatingActionButton floatingActionButton;

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
        View view = inflater.inflate(R.layout.fragment_message_page, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAdd();
            }
        });
        recyclerView = view.findViewById(R.id.messagesRecycler);
        adapter = new MessagesAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void handleAdd() {
        Toast.makeText(getActivity(), "Clicked Button", Toast.LENGTH_SHORT).show();
        MessagesModel messagesModel = new MessagesModel("Kevins Message ", 0);
        messages.add(messagesModel);
        adapter.notifyDataSetChanged();
    }

    private void prepareTestData() {
        for(int i = 0; i < 3; i++) {
            MessagesModel messagesModel = new MessagesModel("Kevins Message "+ i, 0);
            messages.add(messagesModel);
        }
    }
}