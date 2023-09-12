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

import com.example.tfgviravidam.features.app.models.Usuario;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.features.app.ui.fragments.ProfileFragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {


    private List<Usuario> users;
    private OnChatClickListener listener;

    public FollowersAdapter(List<Usuario> user) {
        this.users = user;
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
        Usuario u = users.get(position);
        holder.bind(u, listener);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir otro fragment
                Fragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                args.putSerializable("User", (Serializable) users);
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
        return users.size();
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

        public void bind(Usuario u, OnChatClickListener listener) {
            message.setText(u.getNombreUsuario());
            Picasso.get().load(u.getFotoPerfil()).resize(300, 200).centerCrop().into(profileFoto);
            itemView.setOnClickListener(v -> listener.onChatClick(u));
        }
    }

    public interface OnChatClickListener {
        void onChatClick(Usuario chat);
    }
}
