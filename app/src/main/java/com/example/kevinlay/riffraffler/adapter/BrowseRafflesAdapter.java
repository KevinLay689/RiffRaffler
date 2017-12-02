package com.example.kevinlay.riffraffler.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

        TextView raffleEligTextView = holder.raffleEligTextView;
        TextView raffleNameTextView = holder.raffleNameTextView;
        TextView raffleIdTextView = holder.raffleIdTextView;
        TextView rafflePrizeTextView = holder.rafflePrizeTextView;
        ImageView raffleImage = holder.raffleImage;

        raffleNameTextView.setText(messages.get(position).getRaffleName());
        raffleEligTextView.setText("Raffle Eligibility: " + messages.get(position).getRaffleElig());
        raffleIdTextView.setText("Raffle ID: " + messages.get(position).getRaffleId());
        rafflePrizeTextView.setText("Raffle Prize: " + messages.get(position).getRafflePrize());

        byte[] decodedString = Base64.decode(messages.get(position).getPhoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Bitmap.createScaledBitmap(decodedByte, 100, 100, false);
        raffleImage.setImageBitmap(decodedByte);
        decodedByte = null;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        TextView raffleNameTextView;
        TextView raffleIdTextView;
        TextView raffleEligTextView;
        TextView rafflePrizeTextView;
        ImageView raffleImage;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            this.raffleNameTextView = (TextView) itemView.findViewById(R.id.raffleNameTextView);
            this.raffleIdTextView = (TextView) itemView.findViewById(R.id.raffleIdTextView);
            this.raffleEligTextView = (TextView) itemView.findViewById(R.id.raffleEligTextView);
            this.raffleImage = (ImageView) itemView.findViewById(R.id.browseImage);
            this.rafflePrizeTextView = (TextView) itemView.findViewById(R.id.rafflePrizeTextView);
        }
    }
}
