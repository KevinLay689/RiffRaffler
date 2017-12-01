package com.example.kevinlay.riffraffler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevinlay.riffraffler.model.MessagesModel;
import com.example.kevinlay.riffraffler.R;
import com.example.kevinlay.riffraffler.model.RaffleTicketModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    private List<RaffleTicketModel> messages;

    public MessagesAdapter(List<RaffleTicketModel> messageModel) {
        messages = messageModel;
    }

    @Override
    public MessagesAdapter.MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        MessagesViewHolder myViewHolder = new MessagesViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.MessagesViewHolder holder, int position) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        TextView textViewH = holder.textViewH;

        textViewName.setText("Raffle Owner Id: " + messages.get(position).getOwner());
        textViewVersion.setText(messages.get(position).getRaffleName());
        textViewH.setText("Raffle ID: " + messages.get(position).getRaffleId());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        TextView textViewH;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.textViewH = (TextView) itemView.findViewById(R.id.textViewH);
        }
    }
}
