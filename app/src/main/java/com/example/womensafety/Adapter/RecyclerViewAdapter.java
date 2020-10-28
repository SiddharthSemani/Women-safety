package com.example.womensafety.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Contact> contactList;

    public RecyclerViewAdapter(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, int position) {
        Contact currentContact = contactList.get(position);
        holder.contactName.setText(currentContact.getName());
        holder.phoneNumber.setText(currentContact.getNumber());
        holder.checkBox.setChecked(currentContact.getChecked());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               contactList.get(holder.getAdapterPosition()).setChecked(!contactList.get(holder.getAdapterPosition()).getChecked());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }
    public ArrayList<Contact> check() {
        ArrayList<Contact> checkedList = new ArrayList<>();
        for (int i = 0; i < contactList.size(); i++) {
            boolean temp = contactList.get(i).getChecked();
            if (temp) {
                checkedList.add(contactList.get(i));
            }

        }
        return checkedList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView phoneNumber;
        public ImageView img;
        public CheckBox checkBox;
        public CardView parentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.number);
            img = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkbox);
            parentView = itemView.findViewById(R.id.root_view);
        }
    }
}
