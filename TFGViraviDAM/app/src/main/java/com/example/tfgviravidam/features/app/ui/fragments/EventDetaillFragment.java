package com.example.tfgviravidam.features.app.ui.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Fade;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tfgviravidam.features.app.models.Chat;
import com.example.tfgviravidam.features.app.models.Evento;
import com.example.tfgviravidam.features.app.models.Message;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.databinding.FragmentEventDetaillBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventDetaillFragment extends Fragment {
    private FragmentEventDetaillBinding binding;
    private ArrayList<String> lista = new ArrayList<String>();
    private ArrayList<String> phone = new ArrayList<String>();

    Boolean existe = false;
    Boolean existeDef = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRootreference = database.getReference("Events");

    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUser();
        existe();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventDetaillBinding.inflate(inflater, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterTransition(new Fade());
        }
        View view = binding.getRoot();
        Evento event = getArguments().getParcelable("evento");
        if (event != null) {
            Picasso.get().load(event.getImagen()).resize(300, 200).centerCrop().into(binding.ivEventImage);
            binding.tvName.setText(event.getNombre());
            binding.tvDescription.setText(event.getDescripcion());
            binding.tvCategoria.setText(event.getCategoria());
            binding.tvCiudad.setText(event.getCiudad());
            binding.tvDateStart.setText(event.getFechaInicio());
            binding.tvDateEnd.setText(event.getFechaFin());
            binding.tvCreator.setText(event.getUsuarioCreador());
            binding.tvPersonas.setText(String.valueOf(event.getUsuariosApuntados().size()));
        }
        initListeners(event.getNombre());
        return view;

    }

    private void initListeners(String nombre) {
        binding.btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootreference = FirebaseDatabase.getInstance().getReference("Usuarios");
                mRootreference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ArrayList<String> listaEvent = new ArrayList<String>();
                            String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                            lista.add(0,nombreUsuario);
                            Log.i("Usuario", nombreUsuario);
                            Log.i("event", nombre);
                            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Events").child(nombre);
                            listaEvent.clear();
                            listaEvent.add(nombre);
                            mRootreference.child(firebaseAuth.getCurrentUser().getUid()).child("eventosApuntados").child(nombre).setValue(nombre);

                            DatabaseReference usersRef = eventRef.child("usuariosApuntados");

                            usersRef.child(nombreUsuario).setValue(nombreUsuario);
                            Evento event = getArguments().getParcelable("evento");
                            binding.btnSign.setText("Apuntado");
                            Toast.makeText(getContext(),"Te has apuntado a " + event.getNombre(),Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("EXISTIRA", existe.toString());
                if (existe && existeDef){
                    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats");
                    chatRef.orderByChild("fotoChat").equalTo(getEventFoto()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                                String chatKey = chatSnapshot.getKey();
                                String nameEvent = chatSnapshot.child("name").getValue().toString();
                                String Event = chatSnapshot.child("event").getValue().toString();
                                String Foto = chatSnapshot.child("fotoChat").getValue().toString();
                                ArrayList<String> userList = (ArrayList<String>) chatSnapshot.child("users").getValue();
                                ArrayList<Message> messageList = new ArrayList<>();
                                for (DataSnapshot messageSnapshot : chatSnapshot.child("messages").getChildren()) {
                                    String sender = messageSnapshot.child("sender").getValue(String.class);
                                    String text = messageSnapshot.child("text").getValue(String.class);
                                    String time = messageSnapshot.child("time").getValue(String.class);
                                    Message message = new Message(sender, text, time, null);
                                    messageList.add(message);
                                }
                                Chat chat = new Chat(chatKey,nameEvent,Foto,Event, userList, messageList);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Chat", (Serializable) chat);
                                Fragment nuevoFragmento = new ChatFragment();
                                nuevoFragmento.setArguments(bundle);
                                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_left_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_left_out).replace(R.id.frame_layout, nuevoFragmento).commit();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else {
                    createChat();
                }

            }
        });


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ExploreFragment();
                Bundle args = new Bundle();

                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out,R.anim.zoom_in,R.anim.zoom_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }

    private void existe(){
        ArrayList<String> listaFotos = new ArrayList<String>();
        ArrayList<Chat> listaChat = new ArrayList<Chat>();

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats");

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String chatKey = chatSnapshot.getKey();
                    String nameEvent = chatSnapshot.child("name").getValue().toString();
                    String Event = chatSnapshot.child("event").getValue().toString();
                    String Foto = chatSnapshot.child("fotoChat").getValue().toString();
                    listaFotos.add(Foto);
                    ArrayList<String> userList = (ArrayList<String>) chatSnapshot.child("users").getValue();
                    ArrayList<Message> messageList = new ArrayList<>();
                    for (DataSnapshot messageSnapshot : chatSnapshot.child("messages").getChildren()) {
                        String sender = messageSnapshot.child("sender").getValue(String.class);
                        String text = messageSnapshot.child("text").getValue(String.class);
                        String time = messageSnapshot.child("time").getValue(String.class);
                        Message message = new Message(sender, text, time, null);
                        messageList.add(message);
                    }
                    Boolean telefonoexiste=false;
                    for (String phone:userList) {
                        if (phone.equals(getUserPhone())){
                            telefonoexiste=true;
                        }
                    }
                    Log.i("CHATNAME",nameEvent+ " " + Event);
                    if (getEventFoto().equals(Foto)&& telefonoexiste){
                        existe =true;
                        existeDef=true;
                        Log.i("EXISTE",existe.toString());
                    }else {
                        existe = false;
                        Log.i("NO EXISTE",existe.toString());
                    }
                    Chat chat = new Chat(chatKey,nameEvent,Foto,Event, userList, messageList);
                    listaChat.add(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUser(){
        firebaseAuth = FirebaseAuth.getInstance();
        mRootreference = FirebaseDatabase.getInstance().getReference("Usuarios");
        mRootreference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                    String telefono = snapshot.child("telefono").getValue(String.class);

                    lista.add(0,nombreUsuario);
                    phone.add(0,telefono);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    private String getUserName(){
        String nombreUsuario = lista.get(0);
        return nombreUsuario;
    }
    private String getUserPhone(){
        String phone = lista.get(0);
        return phone;
    }
    private String getEventName(){
        Evento event = getArguments().getParcelable("evento");
        String eventName = event.getUsuarioCreador();
        return eventName;
    }

    private String getEventFoto(){
        Evento event = getArguments().getParcelable("evento");
        String eventFoto = event.getImagen();
        return eventFoto;
    }

    private String getEvent(){
        Evento event = getArguments().getParcelable("evento");
        String eventName = event.getNombre();
        return eventName;
    }

    private void createChat(){

        String userName = getUserName();
        String eventName = getEventName();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chatRef = database.getReference("Chats").push();
        String chatKey = chatRef.getKey();
        DatabaseReference messageRef = chatRef.child("messages").push();

        String horaActual = new SimpleDateFormat("HH:mm").format(new Date());

        Map<String, Object> chatData = new HashMap<>();
        chatData.put("name", getUserName() +" & " + getEventName());
        chatData.put("event", getEvent());
        chatData.put("fotoChat", getEventFoto());
        chatData.put("users", Arrays.asList(userName, eventName));

        chatRef.setValue(chatData);

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("text", "!Bienvenidos!");
        messageData.put("sender", getUserName());
        messageData.put("timestamp", horaActual);
        Message m = new Message(getUserName(),"!Bienvenidos!",horaActual,null);
        ArrayList<Message> messagesList = new ArrayList<>();
        messagesList.add(m);

        messageRef.setValue(messageData);

        Chat c = new Chat(chatKey,getUserName() +" & " + getEventName(),getEventFoto(),getEvent(),Arrays.asList(userName, eventName),messagesList);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Chat", (Serializable) c);
        Fragment nuevoFragmento = new ChatFragment();
        nuevoFragmento.setArguments(bundle);
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_left_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_left_out).replace(R.id.frame_layout, nuevoFragmento).commit();


    }
}