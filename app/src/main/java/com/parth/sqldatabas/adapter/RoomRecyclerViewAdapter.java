package com.parth.sqldatabas.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parth.sqldatabas.R;
import com.parth.sqldatabas.model.RoomDBModel;

import java.util.List;

public class RoomRecyclerViewAdapter extends RecyclerView.Adapter<RoomRecyclerViewAdapter.ViewHolderRoom> {
    List<RoomDBModel> roomDBModelList;
    OnRecyclerViewItemClick onRecyclerViewItemClick;

    public RoomRecyclerViewAdapter(List<RoomDBModel> roomDBModelList, OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.roomDBModelList = roomDBModelList;
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public RoomRecyclerViewAdapter.ViewHolderRoom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.home_contacts_cards, parent, false);
        return new ViewHolderRoom(view, onRecyclerViewItemClick);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoomRecyclerViewAdapter.ViewHolderRoom holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);

        RoomDBModel roomDBModel = roomDBModelList.get(position);
        byte[] image = roomDBModel.get_image();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.avatar.setImageBitmap(bitmap);

        try {
            holder.name.setText(roomDBModelList.get(position).get_fullName());

            if (roomDBModelList.get(position).get_mobileNo().isEmpty()) {
                holder.mobileNo.setText("+91 " + roomDBModelList.get(position).get_userName());
            } else {
                holder.mobileNo.setText("+91 " + roomDBModelList.get(position).get_mobileNo());
            }

        } catch (Exception e) {
            Log.d("PARTH_DATA", "MOBILE NO error - " + e);
        }

        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return roomDBModelList.size();
    }

    public String removeItem(int position) {
        String item = null;
        try {
            item = String.valueOf(roomDBModelList.get(position));
            roomDBModelList.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
        return item;
    }

    public RoomDBModel deleteSingle(int position) {
        return roomDBModelList.get(position);
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(int position);
    }

    public static class ViewHolderRoom extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView avatar;
        TextView name, mobileNo;
        OnRecyclerViewItemClick onRecyclerViewItemClick;

        public ViewHolderRoom(@NonNull View itemView, OnRecyclerViewItemClick onRecyclerViewItemClick) {
            super(itemView);
            avatar = itemView.findViewById(R.id.contact_card_imageview);
            name = itemView.findViewById(R.id.name_tv_contact_card);
            mobileNo = itemView.findViewById(R.id.moNumber_tv_contact_card);
            this.onRecyclerViewItemClick = onRecyclerViewItemClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecyclerViewItemClick.onItemClick(getAdapterPosition());
        }
    }
}
