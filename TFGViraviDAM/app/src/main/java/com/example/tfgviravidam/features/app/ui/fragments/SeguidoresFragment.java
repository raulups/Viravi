package com.example.tfgviravidam.features.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgviravidam.features.app.ui.adapter.FollowersAdapter;
import com.example.tfgviravidam.features.app.models.Usuario;
import com.example.tfgviravidam.databinding.FragmentSeguidoresBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeguidoresFragment extends Fragment {

    private FragmentSeguidoresBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSeguidoresBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFollowers();
    }

    private void loadFollowers() {
        ArrayList<Usuario> listaUsers = new ArrayList<Usuario>();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        FirebaseAuth userAuth = FirebaseAuth.getInstance();

        usersRef.child(userAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String contrasenya = dataSnapshot.child("contrasenya").getValue(String.class);
                    String correo = dataSnapshot.child("correo").getValue(String.class);
                    ArrayList<String> eventosApuntados = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("eventosApuntados").getChildren()) {
                        String eventoApuntado = snapshot.getValue(String.class);
                        eventosApuntados.add(eventoApuntado);
                    }
                    ArrayList<String> eventosCreados = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("eventosCreados").getChildren()) {
                        String eventoCreado = snapshot.getValue(String.class);
                        eventosCreados.add(eventoCreado);
                    }
                    String fechaNacimiento = dataSnapshot.child("fechaNacimiento").getValue(String.class);
                    String fotoPerfil = dataSnapshot.child("fotoPerfil").getValue(String.class);
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String nombreUsuario = dataSnapshot.child("nombreUsuario").getValue(String.class);
                    ArrayList<String> seguidores = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("seguidores").getChildren()) {
                        String seguidor = snapshot.getValue(String.class);
                        seguidores.add(seguidor);
                    }
                    ArrayList<String> seguidos = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("seguidos").getChildren()) {
                        String seguido = snapshot.getValue(String.class);
                        seguidos.add(seguido);
                    }
                    String numeroTelefono = dataSnapshot.child("telefono").getValue(String.class);

                    Usuario usuario = new Usuario(contrasenya, correo, eventosApuntados, eventosCreados,
                            fechaNacimiento, fotoPerfil, nombre, nombreUsuario,
                            seguidores, seguidos, numeroTelefono);

                    for (String user : seguidores) {
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                                    String userKey = usuarioSnapshot.getKey();
                                    String contrasenya = usuarioSnapshot.child("contrasenya").getValue(String.class);
                                    String correo = usuarioSnapshot.child("correo").getValue(String.class);
                                    ArrayList<String> eventosApuntados = new ArrayList<>();
                                    for (DataSnapshot snapshot1 : usuarioSnapshot.child("eventosApuntados").getChildren()) {
                                        String eventoApuntado = snapshot1.getValue(String.class);
                                        eventosApuntados.add(eventoApuntado);
                                    }
                                    ArrayList<String> eventosCreados = new ArrayList<>();
                                    for (DataSnapshot snapshot1 : usuarioSnapshot.child("eventosCreados").getChildren()) {
                                        String eventoCreado = snapshot1.getValue(String.class);
                                        eventosCreados.add(eventoCreado);
                                    }
                                    String fechaNacimiento = usuarioSnapshot.child("fechaNacimiento").getValue(String.class);
                                    String fotoPerfil = usuarioSnapshot.child("fotoPerfil").getValue(String.class);
                                    String nombre = usuarioSnapshot.child("nombre").getValue(String.class);
                                    String nombreUsuario = usuarioSnapshot.child("nombreUsuario").getValue(String.class);
                                    ArrayList<String> seguidores = new ArrayList<>();
                                    for (DataSnapshot snapshot1 : usuarioSnapshot.child("seguidores").getChildren()) {
                                        String seguidor = snapshot1.getValue(String.class);
                                        seguidores.add(seguidor);
                                    }
                                    ArrayList<String> seguidos = new ArrayList<>();
                                    for (DataSnapshot snapshot1 : usuarioSnapshot.child("seguidos").getChildren()) {
                                        String seguido = snapshot1.getValue(String.class);
                                        seguidos.add(seguido);
                                    }
                                    String numeroTelefono = usuarioSnapshot.child("telefono").getValue(String.class);

                                    Usuario usuario = new Usuario(nombreUsuario, nombre, numeroTelefono, fechaNacimiento,
                                            correo, contrasenya, fotoPerfil, userKey, eventosApuntados,
                                            eventosCreados, seguidores, seguidos);
                                    if (user.equals(correo)) {
                                        listaUsers.add(usuario);
                                        FollowersAdapter adapter = new FollowersAdapter(listaUsers);
                                        binding.seguidoresList.setLayoutManager(new LinearLayoutManager(getContext()));
                                        binding.seguidoresList.setAdapter(adapter);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}