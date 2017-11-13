package com.example.kevinlay.riffraffler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kevinlay.riffraffler.model.MessagesModel;
import com.example.kevinlay.riffraffler.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    private List<MessagesModel> messages;

    public MessagesAdapter(List<MessagesModel> messageModel) {
        messages = messageModel;
    }

    @Override
    public MessagesAdapter.MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_item, parent, false);

        return new MessagesAdapter.MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.MessagesViewHolder holder, int position) {
        holder.textView.setText(messages.get(position).getMessageName());
        //holder.imageView.setImageResource(raffles.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;
        private TextView textView;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.messagesTextView);
            imageView = itemView.findViewById(R.id.message_profile_image);
        }
    }
}
