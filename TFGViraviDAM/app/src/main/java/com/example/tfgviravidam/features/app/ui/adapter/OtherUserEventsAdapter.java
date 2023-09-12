package com.example.tfgviravidam.features.app.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgviravidam.databinding.ViewholderEventosProfileBinding;
import com.example.tfgviravidam.features.app.models.Evento;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.features.app.ui.fragments.EventDetaillFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OtherUserEventsAdapter extends RecyclerView.Adapter<OtherUserEventsAdapter.OtherUserEventViewHolder> {
    private List<Evento> listaEvent;

    public OtherUserEventsAdapter(List<Evento> listaEvent) {
        this.listaEvent = listaEvent;

    }

    @NonNull
    @Override
    public OtherUserEventsAdapter.OtherUserEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.otheruser_eventholder, parent, false);
        return new OtherUserEventsAdapter.OtherUserEventViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull OtherUserEventsAdapter.OtherUserEventViewHolder holder, int position) {
        Evento e = listaEvent.get(position);

        holder.binding.Nombre.setText(e.getNombre());
        holder.binding.NumeroUsers.setText(e.getUsuariosApuntados().size() + " Usuarios apuntados");
        Picasso.get().load(e.getImagen()).resize(300, 200).centerCrop().into(holder.binding.FotoCard);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new EventDetaillFragment();
                Bundle args = new Bundle();
                args.putParcelable("evento",e);
                fragment.setArguments(args);

                FragmentManager fragmentManager = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out,R.anim.zoom_in,R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEvent.size();
    }

    public class OtherUserEventViewHolder extends RecyclerView.ViewHolder {

        ViewholderEventosProfileBinding binding;

        public OtherUserEventViewHolder(View itemView) {
            super(itemView);

            // Inicializar el binding
            binding = ViewholderEventosProfileBinding.bind(itemView);
        }
    }
}