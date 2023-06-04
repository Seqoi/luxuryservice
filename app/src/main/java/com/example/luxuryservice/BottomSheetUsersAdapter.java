package com.example.luxuryservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxuryservice.Activities.OrderActivity;
import com.example.luxuryservice.Responses.OrderUser;

import java.util.List;

public class BottomSheetUsersAdapter extends RecyclerView.Adapter<BottomSheetUsersAdapter.BottomSheetUserHolder> {

    private final List<OrderUser> usersList;
    private final OrderActivity orderActivity;
    private UserSelectedListener userSelectedListener;

    private int selectedIndex;

    public BottomSheetUsersAdapter(List<OrderUser> usersList, OrderActivity orderActivity) {
        this.usersList = usersList;
        this.orderActivity = orderActivity;
        this.userSelectedListener = null;
        selectedIndex = 0;
    }

    @NonNull
    @Override
    public BottomSheetUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_user_item, parent, false);
        return new BottomSheetUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetUserHolder holder, int position) {
        holder.userName.setText(usersList.get(position).getName());

        switch (usersList.get(position).getGender()) {
            case "Мужской":
                holder.image.setText("\uD83D\uDC66\uD83C\uDFFC");
                break;
            case "Женский":
                holder.image.setText("\uD83D\uDC67\uD83C\uDFFC");
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSelectedListener != null) {
                    userSelectedListener.onUserSelected(usersList.get(holder.getAdapterPosition()));
                }
                selectedIndex = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        if (selectedIndex == holder.getAdapterPosition()) {
            holder.itemView.setBackgroundResource(R.drawable.button_bg_available);
            holder.userName.setTextColor(orderActivity.getColor(R.color.white));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.text_input_bg);
            holder.userName.setTextColor(orderActivity.getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setOnUserSelectedListener(UserSelectedListener userSelectedListener) {
        this.userSelectedListener = userSelectedListener;
    }

    public interface UserSelectedListener {
        void onUserSelected(OrderUser user);
    }

    public class BottomSheetUserHolder extends RecyclerView.ViewHolder {

        private final TextView image;
        private final TextView userName;

        public BottomSheetUserHolder(@NonNull View itemView) {
            super(itemView);

            this.userName = itemView.findViewById(R.id.username);
            this.image = itemView.findViewById(R.id.image);
        }
    }
}
