package com.example.tfgviravidam.features.app.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tfgviravidam.databinding.FragmentPhotoBinding;
import com.example.tfgviravidam.features.app.models.Usuario;
import com.example.tfgviravidam.features.app.ui.fragments.ViraviActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class PhotoFragment extends Fragment {
    Uri path;
    String nombre, fecha, phone, mail, contra, user, photo;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRootreference = database.getReference("Usuarios");
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;

    private FragmentPhotoBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();

            }
        });

        return view;
    }
    private void cargarImagen() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent,"Seleccione la Aplicacion"),10);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            path=data.getData();

            Bundle datosRecuperados = getArguments();
            nombre = datosRecuperados.getString("nombre");
            fecha = datosRecuperados.getString("fecha");
            phone = datosRecuperados.getString("phone");
            mail = datosRecuperados.getString("mail");
            contra = datosRecuperados.getString("pass");
            user = datosRecuperados.getString("user");

            binding.btnFoto.setImageURI(path);
            binding.progressBar.setVisibility(View.VISIBLE);

            final StorageReference reference = storage.getReference().child("profile/"+user);
            reference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ArrayList<String> eventosApuntado = null;
                            ArrayList<String> eventosCreado = null;
                            ArrayList<String> seguidores = null;
                            ArrayList<String> seguidos = null;

                            binding.progressBar.setVisibility(View.GONE);
                            registrarUsuarioFirebase(user, nombre, phone, fecha, mail, contra, String.valueOf(uri), seguidores, seguidos,eventosApuntado,eventosCreado);

                        }
                    });
                }
            });
        }
    }

    private void registrarUsuarioFirebase(String nombreUsuario, String nombre, String telefono, String fechaNacimiento, String correo, String contrasenya, String fotoPerfil,  ArrayList<String> seguidores,  ArrayList<String> seguidos, ArrayList<String> eventosApuntado, ArrayList<String> eventosCreados) {
        firebaseAuth.createUserWithEmailAndPassword(correo,contrasenya).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                firebaseAuth.signInWithEmailAndPassword(correo,contrasenya).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Usuario usuario = new Usuario(contrasenya, correo, eventosApuntado, eventosCreados, fechaNacimiento, fotoPerfil, nombre, nombreUsuario, seguidores, seguidos, telefono);
                            mRootreference.child(firebaseAuth.getCurrentUser().getUid()).setValue(usuario);
                            startActivity(new Intent(getActivity(),ViraviActivity.class));
                        }else{
                            String errorcode = String.valueOf(((FirebaseAuthException) task.getException()));
                            Toast.makeText(getContext(),errorcode,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}