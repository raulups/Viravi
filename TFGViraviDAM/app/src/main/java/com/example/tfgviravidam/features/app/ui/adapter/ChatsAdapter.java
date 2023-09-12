package com.example.tfgviravidam.features.app.ui.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgviravidam.features.app.models.Chat;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.features.app.ui.fragments.ChatFragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {


    private List<Chat> chats;
    private OnChatClickListener listener;

    public ChatsAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.bind(chat, listener);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir otro fragment
                Fragment fragment = new ChatFragment();
                Bundle args = new Bundle();
                args.putSerializable("Chat", (Serializable) chat);
                fragment.setArguments(args);

                FragmentManager fragmentManager = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_left_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_left_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileFoto;
        private TextView message,time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileFoto = itemView.findViewById(R.id.ivProfileImage);
            message = itemView.findViewById(R.id.tvName);
            time = itemView.findViewById(R.id.tvLastMessage);
        }

        public void bind(Chat chat, OnChatClickListener listener) {
            message.setText(chat.getName());
            time.setText(chat.getNameEvent());
            Picasso.get().load(chat.getFoto()).resize(300, 200).centerCrop().into(profileFoto);
            itemView.setOnClickListener(v -> listener.onChatClick(chat));
        }
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }
}
