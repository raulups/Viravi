package com.example.tfgviravidam.features.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgviravidam.features.app.ui.adapter.MessageAdapter;
import com.example.tfgviravidam.features.app.models.Chat;
import com.example.tfgviravidam.features.app.models.Message;
import com.example.tfgviravidam.R;
import com.example.tfgviravidam.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ArrayList<String> nombre = new ArrayList<String>();
    private ArrayList<String> telefono = new ArrayList<String>();
    private ArrayList<Message> messageList = new ArrayList<Message>();
    private MessageAdapter adapter;

    private Chat chat = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserName();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
        Bundle bundle = getArguments();
        if(bundle != null){
            chat = (Chat) bundle.getSerializable("Chat");
            binding.tvUserName.setText(chat.getName());
            binding.tvEventName.setText(chat.getNameEvent());
            Picasso.get().load(chat.getFoto()).resize(300, 200).centerCrop().into(binding.ivProfileImage);

        }
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Chats").child(chat.getId()).child("messages");
        messagesRef.addChildEventListener(messagesListener);
    }

    private void initListeners() {

        binding.messageList.smoothScrollToPosition(messageList.size() - 1);
        binding.messageList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.messageList.setHasFixedSize(true);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                binding.etMessage.setText("");
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ChatListFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);

                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_right_out,R.anim.slide_right_in,R.anim.slide_right_out)
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }

    private void sendMessage(){
        Bundle bundle = getArguments();

        if(bundle != null){
            chat = (Chat) bundle.getSerializable("Chat");
        }

        Chat finalChat = chat;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chatRef = database.getReference("Chats").child(finalChat.getId());
        DatabaseReference newMessageRef = chatRef.child("messages").push();
        String horaActual = new SimpleDateFormat("HH:mm").format(new Date());
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("date", horaActual);
        messageData.put("sender", nombre.get(0));
        messageData.put("text", binding.etMessage.getText());
        Message m = new Message(nombre.get(0),binding.etMessage.getText().toString(),horaActual,null);
        ArrayList<Message> messagesList = new ArrayList<>();
        messagesList.add(m);
        newMessageRef.setValue(m);
    }

    private ChildEventListener messagesListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String messageId = snapshot.getKey();
            String sender = snapshot.child("sender").getValue(String.class);
            String text = snapshot.child("text").getValue(String.class);
            String time = snapshot.child("timestamp").getValue(String.class);

            if (sender.equals(nombre.get(0))) {
                Message m = new Message(sender, text, time, true);
                messageList.add(m);
            } else {
                Message m = new Message(sender, text, time, false);
                messageList.add(m);
            }

            adapter = new MessageAdapter(messageList);

            binding.messageList.setAdapter(adapter);

            adapter.notifyItemInserted(messageList.size() - 1);
            binding.messageList.smoothScrollToPosition(messageList.size() - 1);}

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    private void getUserName(){
        DatabaseReference mRootreference = FirebaseDatabase.getInstance().getReference("Usuarios");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        mRootreference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                    String phone = snapshot.child("telefono").getValue(String.class);
                    nombre.add(0,nombreUsuario);
                    telefono.add(0,phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}