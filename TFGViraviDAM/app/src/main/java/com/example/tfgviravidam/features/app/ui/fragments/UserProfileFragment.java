package com.example.tfgviravidam.features.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgviravidam.features.app.ui.adapter.OtherUserEventsAdapter;
import com.example.tfgviravidam.features.app.models.Evento;
import com.example.tfgviravidam.features.app.models.Usuario;
import com.example.tfgviravidam.databinding.FragmentUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;

    private Boolean seguido = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        seguido = false;
        isFollowed();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUser();
        loadEventUser();
        initListeners();

    }

    private void initListeners() {
        Usuario user = getArguments().getParcelable("user");





    }

    private void loadUser(){
        Usuario user = getArguments().getParcelable("user");
        binding.txtnumEventos.setText(user.getEventosCreados().size()+"");
        binding.txtNumSeguidores.setText(user.getSeguidores().size()+"");
        binding.txtNumSeguidos.setText(user.getSeguidos().size()+"");
        binding.tvNombre.setText(user.getNombreUsuario());
        Picasso.get().load(user.getFotoPerfil()).resize(300, 200).centerCrop().into(binding.foto);
    }

    private void loadEventUser(){

        ArrayList<Evento> listaEvento = new ArrayList<Evento>();

        binding.userEvents.setLayoutManager(new GridLayoutManager(getContext(),2));

        DatabaseReference eventosRef = FirebaseDatabase.getInstance().getReference().child("Events");
        eventosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eventoSnapshot : dataSnapshot.getChildren()) {
                    String nombre = eventoSnapshot.child("nombre").getValue(String.class);
                    String descripcion = eventoSnapshot.child("descripcion").getValue(String.class);
                    String usuarioCreador = eventoSnapshot.child("usuarioCreador").getValue(String.class);
                    String imagen = eventoSnapshot.child("imagen").getValue(String.class);
                    String ciudad = eventoSnapshot.child("ciudad").getValue(String.class);
                    String categoria = eventoSnapshot.child("categoria").getValue(String.class);
                    String fechaInicio = eventoSnapshot.child("fechaInicio").getValue(String.class);
                    String fechaFin = eventoSnapshot.child("fechaFin").getValue(String.class);

                    ArrayList<String> usuariosApuntados = new ArrayList<>();
                    for (DataSnapshot usuarioSnapshot : eventoSnapshot.child("usuariosApuntados").getChildren()) {
                        usuariosApuntados.add(usuarioSnapshot.getKey());
                    }

                    Evento evento = new Evento(nombre, descripcion, fechaInicio, fechaFin, usuarioCreador, ciudad, categoria, imagen, usuariosApuntados);
                    Usuario user = getArguments().getParcelable("user");
                    ArrayList<String> eventosCreados = user.getEventosCreados();

                    for (String nombreEvento: eventosCreados) {
                        if (nombre.equals(nombreEvento)){
                            listaEvento.add(evento);

                        }
                    }
                    OtherUserEventsAdapter adapterUserEvento = new OtherUserEventsAdapter(listaEvento);
                    binding.userEvents.setAdapter(adapterUserEvento);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores si es necesario
                // ...
            }
        });
    }

    private void isFollowed(){
        Usuario user = getArguments().getParcelable("user");

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseAuth.getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    ArrayList<String> seguidos = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("seguidos").getChildren()) {
                        String seguido = snapshot.getValue(String.class);
                        seguidos.add(seguido);
                    }
                    for (String name : seguidos) {
                        if (user.getNombreUsuario().equals(name)){
                            seguido = true;
                        }
                    }
                    if(seguido){
                        binding.btnDejarSeguir.setText("Siguiendo");
                        binding.btnDejarSeguir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                binding.btnDejarSeguir.setText("Seguir");
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUserKey()).child("seguidores");
                                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).removeValue();

                                databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("seguidos");
                                databaseReference.child(user.getUserKey()).removeValue();
                            }
                        });
                    }else{
                        binding.btnDejarSeguir.setText("Seguir");

                        binding.btnDejarSeguir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                binding.btnDejarSeguir.setText("Siguiendo");
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUserKey()).child("seguidores");
                                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(firebaseAuth.getCurrentUser().getEmail());

                                databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("seguidos");
                                databaseReference.child(user.getUserKey()).setValue(user.getNombreUsuario());
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores aquí
            }
        });
    }
}