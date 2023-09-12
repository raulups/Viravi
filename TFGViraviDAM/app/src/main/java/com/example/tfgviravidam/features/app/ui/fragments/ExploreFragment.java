package com.example.tfgviravidam.features.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgviravidam.features.app.ui.adapter.OtherUserEventsAdapter;
import com.example.tfgviravidam.features.app.ui.adapter.PopularAdapter;
import com.example.tfgviravidam.features.app.ui.adapter.UserAdapter;
import com.example.tfgviravidam.features.app.models.Evento;
import com.example.tfgviravidam.features.app.models.Usuario;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.databinding.FragmentExploreBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;

    private  String user;

    /* DECLARACIÓN DE LOS IMAGEBUTTONS */
    private RecyclerView recyclerViewPopularplans;
    private List<Evento> eventos = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerViewPopularplans = view.findViewById(R.id.viewPopuPlans);
        recyclerViewPopular(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
        recogerEventos();

    }

    private void initListeners() {
        binding.cvParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryEventsFragment();
                Bundle args = new Bundle();
                args.putString("category","Fiesta");
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out,R.anim.zoom_in, R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

            }
        });
        binding.cvMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryEventsFragment();
                Bundle args = new Bundle();
                args.putString("category","Turismo");
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out,R.anim.zoom_in, R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
        binding.cvActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryEventsFragment();
                Bundle args = new Bundle();
                args.putString("category","Actividades");
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out,R.anim.zoom_in, R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
        binding.cvTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryEventsFragment();
                Bundle args = new Bundle();
                args.putString("category","Viajes");
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out,R.anim.zoom_in, R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
        binding.cvFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryEventsFragment();
                Bundle args = new Bundle();
                args.putString("category","Gastronomia");
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out,R.anim.zoom_in, R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
        binding.cvSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryEventsFragment();
                Bundle args = new Bundle();
                args.putString("category","Deportes");
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out,R.anim.zoom_in, R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase= FirebaseDatabase.getInstance().getReference("Usuarios");
        loadUser();
        recogerDatosUser();

    }

    private void recogerDatosUser() {

        firebaseDatabase.child(user).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String telefono = snapshot.child("telefono").getValue(String.class);
                    String fechaNacimiento = snapshot.child("fechaNacimiento").getValue(String.class);
                    String correo = snapshot.child("correo").getValue(String.class);
                    String contrasenya = snapshot.child("contrasenya").getValue(String.class);
                    String foto= snapshot.child("fotoPerfil").getValue(String.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recyclerViewPopular(View view) {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPopularplans.setLayoutManager(linearLayoutManager);

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

                    Evento evento = new Evento(nombre, descripcion,fechaInicio,fechaFin, usuarioCreador, ciudad, categoria, imagen, usuariosApuntados);

                    eventos.add(evento);
                    PopularAdapter adapter1=new PopularAdapter(eventos, new PopularAdapter.OnItemClickListener(){

                        @Override
                        public void onItemClick(Evento evento) {
                            Fragment fragment = new EventDetaillFragment();
                            Bundle args = new Bundle();
                            args.putParcelable("evento",evento);
                            fragment.setArguments(args);

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, fragment)
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                        }
                    });
                    binding.viewPopuPlans.setAdapter(adapter1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void loadUser(){
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        ArrayList<Usuario> listaUser = new ArrayList<Usuario>();
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    String userKey = usuarioSnapshot.getKey();
                    String contrasenya = usuarioSnapshot.child("contrasenya").getValue(String.class);
                    String correo = usuarioSnapshot.child("correo").getValue(String.class);
                    ArrayList<String> eventosApuntados = new ArrayList<>();
                    for (DataSnapshot snapshot : usuarioSnapshot.child("eventosApuntados").getChildren()) {
                        String eventoApuntado = snapshot.getValue(String.class);
                        eventosApuntados.add(eventoApuntado);
                    }
                    ArrayList<String> eventosCreados = new ArrayList<>();
                    for (DataSnapshot snapshot : usuarioSnapshot.child("eventosCreados").getChildren()) {
                        String eventoCreado = snapshot.getValue(String.class);
                        eventosCreados.add(eventoCreado);
                    }
                    String fechaNacimiento = usuarioSnapshot.child("fechaNacimiento").getValue(String.class);
                    String fotoPerfil = usuarioSnapshot.child("fotoPerfil").getValue(String.class);
                    String nombre = usuarioSnapshot.child("nombre").getValue(String.class);
                    String nombreUsuario = usuarioSnapshot.child("nombreUsuario").getValue(String.class);
                    ArrayList<String> seguidores = new ArrayList<>();
                    for (DataSnapshot snapshot : usuarioSnapshot.child("seguidores").getChildren()) {
                        String seguidor = snapshot.getValue(String.class);
                        seguidores.add(seguidor);
                    }
                    ArrayList<String> seguidos = new ArrayList<>();
                    for (DataSnapshot snapshot : usuarioSnapshot.child("seguidos").getChildren()) {
                        String seguido = snapshot.getValue(String.class);
                        seguidos.add(seguido);
                    }
                    String numeroTelefono = usuarioSnapshot.child("telefono").getValue(String.class);

                    Usuario usuario = new Usuario(nombreUsuario, nombre, numeroTelefono, fechaNacimiento,
                            correo, contrasenya,fotoPerfil, userKey, eventosApuntados,
                            eventosCreados, seguidores, seguidos);
                    listaUser.add(usuario);

                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                    binding.viewUsers.setLayoutManager(linearLayoutManager);
                    UserAdapter adapterUser =new UserAdapter(listaUser);
                    binding.viewUsers.setAdapter(adapterUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores si es necesario
                // ...
            }
        });
    }

    private void recogerEventos(){
        binding.viewPlans.setLayoutManager(new GridLayoutManager(getContext(),2));


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

                    Evento evento = new Evento(nombre, descripcion,fechaInicio,fechaFin, usuarioCreador, ciudad, categoria, imagen, usuariosApuntados);
                    eventos.add(evento);
                    OtherUserEventsAdapter adapterUserEvento = new OtherUserEventsAdapter(eventos);
                    binding.viewPlans.setAdapter(adapterUserEvento);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}