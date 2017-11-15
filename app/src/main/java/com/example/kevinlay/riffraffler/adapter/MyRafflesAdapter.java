package com.example.kevinlay.riffraffler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevinlay.riffraffler.R;
import com.example.kevinlay.riffraffler.model.RaffleTicketModel;

import java.util.List;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MyRafflesAdapter extends RecyclerView.Adapter<MyRafflesAdapter.RaffleViewHolder> {

    private List<RaffleTicketModel> raffles;

    public MyRafflesAdapter(List<RaffleTicketModel> myRafflesModels) {
        raffles = myRafflesModels;
    }

    @Override
    public RaffleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_raffles_single_item_layout, parent, false);

        return new RaffleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RaffleViewHolder holder, int position) {
        holder.textView.setText(raffles.get(position).getRaffleId());
        //holder.imageView.setImageResource(raffles.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return raffles.size();
    }

    public class RaffleViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public RaffleViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.myRaffleText);
            imageView = itemView.findViewById(R.id.myRaffleImage);
        }
    }
}
