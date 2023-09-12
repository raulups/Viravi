package com.example.tfgviravidam.features.app.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tfgviravidam.databinding.FragmentEditEventBinding;
import com.example.tfgviravidam.features.app.models.Evento;
import com.example.tfgviravidam.R;
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

public class EditEventFragment extends Fragment {

    private FragmentEditEventBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRootreference = database.getReference("Eventos");

    String user;
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String fechaInicio;
    String fechaFin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        initListeners();
        loadEvent();

    }

    private void loadEvent(){
        Evento event = getArguments().getParcelable("evento");
        binding.txtName.setText(event.getNombre());
        binding.txtDesc.setText(event.getDescripcion());
        binding.txtCategory.setText(event.getCategoria());
        binding.txtCity.setText(event.getCiudad());
        binding.txtDayInicio.setText(event.getFechaInicio());
        binding.txtDayFin.setText(event.getFechaFin());
        Picasso.get().load(event.getImagen()).resize(300, 200).centerCrop().into(binding.btnGaleria);


        binding.txtName.addTextChangedListener(textWatcherYear);
        binding.txtDesc.addTextChangedListener(textWatcherYear);
        binding.txtCategory.addTextChangedListener(textWatcherYear);
        binding.txtCity.addTextChangedListener(textWatcherYear);
    }



    private void initListeners() {

        binding.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Evento event = getArguments().getParcelable("evento");
                DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("Events").child(event.getNombre());
                chatsRef.removeValue();

                Fragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);

                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out,R.anim.zoom_in,R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

            }
        });
        binding.btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });
        binding.txtPulsar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        binding.btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);

                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out,R.anim.zoom_in,R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

            }
        });
        binding.btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!binding.txtName.getText().toString().isEmpty() &&
                        !binding.txtName.getText().toString().isEmpty() &&
                        !binding.txtDesc.getText().toString().isEmpty() &&
                        !binding.txtCity.getText().toString().isEmpty() &&
                        !binding.txtCategory.getText().toString().isEmpty() &&
                        !binding.txtDayInicio.getText().toString().isEmpty() &&
                        !binding.txtDayFin.getText().toString().isEmpty()){

                    fechaInicio = binding.txtDayInicio.getText().toString();
                    fechaFin = binding.txtDayFin.getText().toString();
                    crearEvento(binding.txtName.getText().toString(),binding.txtDesc.getText().toString(),fechaInicio,fechaFin,binding.txtCity.getText().toString(),binding.txtCategory.getText().toString());

                } else {
                    Toast.makeText(getContext(), "Rellene todos los campos", Toast.LENGTH_LONG).show();
                }

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
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            binding.btnGaleria.setImageURI(path);
            final StorageReference reference = storage.getReference().child("event/"+path.toString());
            reference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image_url=uri;
                        }
                    });
                }
            });
        }

    }
    private TextWatcher textWatcherYear = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            fechaInicio = binding.txtDayInicio.getText().toString();
            fechaFin = binding.txtDayFin.getText().toString();

            if(!binding.txtName.getText().toString().isEmpty()
                    && !binding.txtDesc.getText().toString().isEmpty()
                    && !binding.txtCity.getText().toString().isEmpty()
                    && !binding.txtCategory.getText().toString().isEmpty()
                    && !fechaInicio.isEmpty()
                    && !fechaFin.isEmpty()) {
                binding.btnCrear.setEnabled(true);
            }else{
                binding.btnCrear.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void showDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_dialog);

        LinearLayout fiestaLayout = dialog.findViewById(R.id.layoutFiesta);
        LinearLayout turismoLayout = dialog.findViewById(R.id.layoutTurismo);
        LinearLayout actividadesLayout = dialog.findViewById(R.id.layoutActividades);
        LinearLayout viajesLayout = dialog.findViewById(R.id.layoutViajes);
        LinearLayout gastronomiaLayout = dialog.findViewById(R.id.layoutGastronomia);
        LinearLayout deportesLayout = dialog.findViewById(R.id.layoutDeportes);

        fiestaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                binding.txtCategory.setText("Fiesta");

            }
        });

        turismoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                binding.txtCategory.setText("Turismo");

            }
        });

        actividadesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                binding.txtCategory.setText("Actividades");

            }
        });

        viajesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                binding.txtCategory.setText("Viajes");

            }
        });

        gastronomiaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                binding.txtCategory.setText("Gastronomia");

            }
        });

        deportesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                binding.txtCategory.setText("Deportes");

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void crearEvento(String nombre,String descripcion, String fechaInicio, String fechaFin, String ciudad, String categoria) {
        ArrayList<String> usuariosApuntados = new ArrayList<String>();
        mRootreference= FirebaseDatabase.getInstance().getReference("Usuarios");
        mRootreference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                    mRootreference= FirebaseDatabase.getInstance().getReference("Events");
                    usuariosApuntados.add(nombreUsuario);
                    usuariosApuntados.add("Juan");
                    DatabaseReference userEvent = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("eventosCreados").child(nombre);
                    userEvent.setValue(nombre);

                    Evento e = new Evento();
                    e.setNombre(nombre);
                    e.setDescripcion(descripcion);
                    e.setFechaInicio(fechaInicio);
                    e.setFechaFin(fechaFin);
                    e.setUsuarioCreador(nombreUsuario);
                    e.setCiudad(ciudad);
                    e.setCategoria(categoria);
                    e.setImagen(image_url.toString());
                    e.setUsuariosApuntados(new ArrayList<>());
                    mRootreference.child(nombre).setValue(e);

                    Fragment fragment = new ProfileFragment();
                    Bundle args = new Bundle();
                    fragment.setArguments(args);

                    FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out,R.anim.zoom_in,R.anim.zoom_out)
                            .replace(R.id.frame_layout, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}