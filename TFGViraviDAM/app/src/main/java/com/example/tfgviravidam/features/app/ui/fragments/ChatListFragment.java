package com.example.tfgviravidam.features.app.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgviravidam.features.app.ui.adapter.ChatsAdapter;
import com.example.tfgviravidam.features.app.models.Chat;
import com.example.tfgviravidam.features.app.models.Message;
import com.example.tfgviravidam.databinding.FragmentChatListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {

    private FragmentChatListBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        DatabaseReference mRootreference;
        mRootreference = FirebaseDatabase.getInstance().getReference("Usuarios");
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Chat> chatList = new ArrayList<>();
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
                        Message message = new Message(sender, text, time,null);
                        messageList.add(message);
                    }
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    mRootreference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                                if (userList.contains(nombreUsuario)) {
                                    for (String n : userList) {
                                        if (!n.equals(nombreUsuario)) {
                                            String name = n;
                                            Chat chat = new Chat(chatKey,nameEvent,Foto,Event, userList, messageList);
                                            chatList.add(chat);
                                            ChatsAdapter adapter = new ChatsAdapter(chatList);
                                            RecyclerView recyclerView = binding.chatList;
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            recyclerView.setAdapter(adapter);
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }

        });


        /*ChatsAdapter adapter = new ChatsAdapter(chats);
        RecyclerView recyclerView = binding.chatList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}