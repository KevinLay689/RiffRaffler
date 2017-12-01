package com.example.kevinlay.riffraffler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kevinlay.riffraffler.R;
import com.example.kevinlay.riffraffler.model.RaffleTicketModel;

import java.util.List;

/**
 * Created by kevinlay on 11/8/17.
 */

public class BrowseRafflesAdapter extends RecyclerView.Adapter<BrowseRafflesAdapter.MessagesViewHolder> {
    private List<RaffleTicketModel> messages;

    public BrowseRafflesAdapter(List<RaffleTicketModel> messageModel) {
        messages = messageModel;
    }

    @Override
    public BrowseRafflesAdapter.MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        MessagesViewHolder myViewHolder = new MessagesViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(BrowseRafflesAdapter.MessagesViewHolder holder, int position) {

        TextView raffleOwnerTextView = holder.raffleOwnerTextView;
        TextView raffleNameTextView = holder.raffleNameTextView;
        TextView raffleIdTextView = holder.raffleIdTextView;

        raffleNameTextView.setText(messages.get(position).getRaffleName());
        raffleOwnerTextView.setText("Raffle Owner ID: " + messages.get(position).getOwner());
        raffleIdTextView.setText("Raffle ID: " + messages.get(position).getRaffleId());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        TextView raffleNameTextView;
        TextView raffleIdTextView;
        TextView raffleOwnerTextView;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            this.raffleNameTextView = (TextView) itemView.findViewById(R.id.raffleNameTextView);
            this.raffleIdTextView = (TextView) itemView.findViewById(R.id.raffleIdTextView);
            this.raffleOwnerTextView = (TextView) itemView.findViewById(R.id.raffleOwnerTextView);
        }
    }
}
