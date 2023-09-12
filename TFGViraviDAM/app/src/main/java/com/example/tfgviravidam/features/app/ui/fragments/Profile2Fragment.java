package com.example.tfgviravidam.features.app.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgviravidam.features.app.ui.adapter.OtherUserEventsAdapter;
import com.example.tfgviravidam.AppActivity;
import com.example.tfgviravidam.features.app.models.Evento;
import com.example.tfgviravidam.features.app.models.Usuario;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.databinding.FragmentProfile2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile2Fragment extends Fragment {
    private FragmentProfile2Binding binding;
    private ArrayList<String> lista = new ArrayList<String>();
    FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        binding = FragmentProfile2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        loadUser();
        loadUserEvents();

        firebaseAuth = FirebaseAuth.getInstance();
        initListeners();

        return  view;

    }

    private void initListeners() {
        binding.btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Profile2Fragment.this.getActivity(), AppActivity.class));
                finishF();
            }
        });
        binding.txtCreadoEspacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
        binding.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditProfileFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_down_in,R.anim.slide_down_out,R.anim.slide_down_in,R.anim.slide_down_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
        binding.txtNumSeguidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SeguidoresFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out,R.anim.zoom_in,R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        binding.txtNumSeguidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SeguidosFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out,R.anim.zoom_in,R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }

    private void finishF() {
        Activity activity = (Activity)getContext();
        activity.finish();
    }

    private void loadUserEvents(){

    }

    private void loadUser(){
        DatabaseReference mRootreference;
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseAuth.getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Aquí se obtienen los datos del usuario y se crea el objeto Usuario correspondiente
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
                    lista.add(nombreUsuario);
                    binding.txtnumEventos.setText(eventosCreados.size()+"");
                    binding.txtNumSeguidores.setText(seguidores.size()+"");
                    binding.txtNumSeguidos.setText(seguidos.size()+"");
                    binding.tvNombre.setText(nombreUsuario);
                    Picasso.get().load(fotoPerfil).resize(300, 200).centerCrop().into(binding.foto);

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

                                for (String nombreEvento: eventosApuntados) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores aquí
            }
        });
    }
}