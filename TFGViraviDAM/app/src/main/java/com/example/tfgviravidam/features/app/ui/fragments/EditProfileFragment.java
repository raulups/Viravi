package com.example.tfgviravidam.features.app.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgviravidam.features.app.models.Usuario;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.databinding.FragmentEditProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    Uri path;
    FirebaseStorage storage;
    Boolean fotoDone = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        storage = FirebaseStorage.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inistListeners();
        loadUser();
    }

    private void inistListeners() {

        binding.btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Fragment nuevoFragmento = new ProfileFragment();
                nuevoFragmento.setArguments(bundle);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up_in,R.anim.slide_up_out,R.anim.slide_up_in,R.anim.slide_up_out).replace(R.id.frame_layout, nuevoFragmento).commit();

            }
        });
        binding.btnFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });
        binding.btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth firebaseAuth;
                firebaseAuth = FirebaseAuth.getInstance();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseAuth.getCurrentUser().getUid());
                userRef.child("nombre").setValue(binding.txtName.getText().toString());
                userRef.child("nombreUsuario").setValue(binding.txtUserName.getText().toString());
                userRef.child("telefono").setValue(binding.txtTelefono.getText().toString());
                userRef.child("fechaNacimiento").setValue(binding.txtNacimiento.getText().toString());
                if (fotoDone){
                    userRef.child("fotoPerfil").setValue(path.toString());
                }

                Bundle bundle = new Bundle();
                Fragment nuevoFragmento = new ProfileFragment();
                nuevoFragmento.setArguments(bundle);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up_in,R.anim.slide_up_out,R.anim.slide_up_in,R.anim.slide_up_out).replace(R.id.frame_layout, nuevoFragmento).commit();

            }
        });
    }

    private void cargarImagen() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent,"Seleccione la Aplicacion"),10);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        if(resultCode==RESULT_OK){
            path=data.getData();
            binding.btnFotoPerfil.setImageURI(path);

            final StorageReference reference = storage.getReference().child("profile/"+firebaseAuth.getCurrentUser().getUid());
            reference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            fotoDone = true;
                            path = uri;

                        }
                    });
                }
            });
        }
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
                    binding.txtName.setText(nombre);
                    binding.txtUserName.setText(nombreUsuario);
                    binding.txtNacimiento.setText(fechaNacimiento);
                    binding.txtTelefono.setText(numeroTelefono);
                    Picasso.get().load(fotoPerfil).resize(300, 200).centerCrop().into(binding.btnFotoPerfil);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores aquí
            }
        });
    }
}