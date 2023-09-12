package com.example.tfgviravidam.features.app.ui.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgviravidam.features.app.models.Message;
import com.example.tfgviravidam.databinding.MessageCardBinding;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MessageCardBinding binding = MessageCardBinding.inflate(layoutInflater, parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private MessageCardBinding binding;

        public MessageViewHolder(MessageCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            if (message.getText().equals("!Bienvenidos!")) {
                binding.welcome.getRoot().setVisibility(View.VISIBLE);
                binding.sender.getRoot().setVisibility(View.GONE);
                binding.receiver.getRoot().setVisibility(View.GONE);

                binding.welcome.tvText.setText(message.getText());
            } else {
                if (message.getOwner()) {
                    binding.sender.getRoot().setVisibility(View.VISIBLE);
                    binding.receiver.getRoot().setVisibility(View.GONE);
                    binding.welcome.getRoot().setVisibility(View.GONE);

                    binding.sender.tvText.setText(message.getText());
                    binding.sender.tvTime.setText(message.getTimestamp());
                } else {
                    binding.sender.getRoot().setVisibility(View.GONE);
                    binding.receiver.getRoot().setVisibility(View.VISIBLE);
                    binding.welcome.getRoot().setVisibility(View.GONE);

                    binding.receiver.tvText.setText(message.getText());
                    binding.receiver.tvTime.setText(message.getTimestamp());
                }
            }
        }
    }
}