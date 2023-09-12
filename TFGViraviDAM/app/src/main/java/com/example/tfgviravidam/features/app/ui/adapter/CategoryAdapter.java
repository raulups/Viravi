package com.example.tfgviravidam.features.app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfgviravidam.features.app.models.Categorias;
import com.example.tfgviravidam.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<Categorias>categoriasLista;

    public CategoryAdapter(ArrayList<Categorias> categoria) {
        this.categoriasLista=categoria;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.nombre.setText(categoriasLista.get(position).getNombre());
        String picUrl="";

        switch (position){
            case 0:{
                picUrl="party";
                break;

            }
            case 1:{
                picUrl="museum";
                break;

            }
            case 2:{
                picUrl="park";
                break;

            }

        }
        int drawableResource= holder.itemView.getContext().getResources().getIdentifier(picUrl,"drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResource).
                into(holder.categoriaFoto);

    }

    @Override
    public int getItemCount() {
        return categoriasLista.size() ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoriaFoto;

        TextView nombre;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.Nombre);
            categoriaFoto = itemView.findViewById(R.id.pic);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
