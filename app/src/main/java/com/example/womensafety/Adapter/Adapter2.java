package com.example.womensafety.Adapter;

import android.util.Log;
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

public class Adapter2 extends RecyclerView.Adapter {
    private ArrayList<Contact> savedList;
    private int type;

    public Adapter2(ArrayList<Contact> tempList, int type) {
        int listSize = tempList.size();

        for (int i = 0; i < listSize; i++) {
            Log.i("On Constructor: ", String.valueOf(tempList.get(i)));
        }
        this.savedList = tempList;
        this.type = type;
    }

    public void updateData(ArrayList<Contact> contacts) {
        this.savedList = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (type == 1) {
            view = layoutInflater.inflate(R.layout.example_item, parent, false);
            return new ViewHolder1(view);
        } else {
            view = layoutInflater.inflate(R.layout.example_item, parent, false);
            return new ViewHolder2(view);
        }
    }

    public ArrayList<Contact> check() {
        ArrayList<Contact> checkedList = new ArrayList<>();
        for (int i = 0; i < savedList.size(); i++) {
            boolean temp = savedList.get(i).getChecked();
            if (temp) {
                checkedList.add(savedList.get(i));
            }

        }
        return checkedList;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (type == 1) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            Contact currentContact = savedList.get(position);
            viewHolder1.contactName.setText(currentContact.getName());
            viewHolder1.phoneNumber.setText(currentContact.getNumber());
            viewHolder1.checkBox.setChecked(currentContact.getChecked());
            viewHolder1.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedList.get(holder.getAdapterPosition()).setChecked(!savedList.get(holder.getAdapterPosition()).getChecked());
                    notifyItemChanged(holder.getAdapterPosition());
                }
            });
        } else {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            Contact currentContact = savedList.get(position);
            ((ViewHolder2) holder).contactName.setText(currentContact.getName());
            ((ViewHolder2) holder).phoneNumber.setText(currentContact.getNumber());

        }

    }

    @Override
    public int getItemCount() {

        return savedList.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView phoneNumber;
        public ImageView img;
        public CheckBox checkBox;
        public CardView parentView;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.number);
            img = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkbox);
            parentView = itemView.findViewById(R.id.root_view);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView phoneNumber;
        public ImageView img;
        public CardView parentView;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.number);
            img = itemView.findViewById(R.id.imageView);
            itemView.findViewById(R.id.checkbox).setVisibility(View.INVISIBLE);
            parentView = itemView.findViewById(R.id.root_view);
        }
    }

}
