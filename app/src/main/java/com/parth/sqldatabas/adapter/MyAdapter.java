package com.parth.sqldatabas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parth.sqldatabas.EditUserInfoActivity;
import com.parth.sqldatabas.R;
import com.parth.sqldatabas.data.MyDbHandler;
import com.parth.sqldatabas.model.DetailsModel;
import com.parth.sqldatabas.parameters.Parameters;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    int singleData;
    ArrayList<DetailsModel> modelArrayList;
    SQLiteDatabase sqLiteDatabase;

    public MyAdapter(Context context, int singleData, ArrayList<DetailsModel> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.singleData = singleData;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_cards, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);

        DetailsModel detailsModel = modelArrayList.get(position);
        byte[] image = detailsModel.getByteBuffer();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        holder.avatar.setImageBitmap(bitmap);
        holder.fullName.setText(detailsModel.getFullName());
        holder.userName.setText(detailsModel.getUserName());
        holder.email.setText(detailsModel.getEmail());
        holder.mobileNo.setText(detailsModel.getMobileNo());

        holder.itemView.startAnimation(animation);

        //menu on card
        holder.flowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.flowMenu);
                popupMenu.inflate(R.menu.flow_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edit_menu:
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", detailsModel.getId());
                                //bundle.putByteArray(Parameters.KEY_AVATAR, detailsModel.getByteBuffer());
                                bundle.putByteArray("avatar", detailsModel.getByteBuffer());
                                bundle.putString("fullName", detailsModel.getFullName());
                                bundle.putString("userName", detailsModel.getUserName());
                                bundle.putString("email", detailsModel.getEmail());
                                bundle.putString("mobilNo", detailsModel.getMobileNo());
                                //bundle.putString("password", detailsModel.getPassword());

                                Intent intent = new Intent(context, EditUserInfoActivity.class);
                                intent.putExtra("userData", bundle);
                                context.startActivity(intent);
                                break;

                            case R.id.delete:
                                MyDbHandler myDbHandler = new MyDbHandler(context);
                                sqLiteDatabase = myDbHandler.getWritableDatabase();
                                long recDelete = sqLiteDatabase.delete(Parameters.TABLE_NAME,   " id= " + detailsModel.getId(), null);
                                if (recDelete != -1) {
                                    Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();

                                    // remove position after deleted
                                    modelArrayList.remove(position);

                                    //update data
                                    notifyDataSetChanged();
                                }
                                sqLiteDatabase.close();
                                break;

                            default:
                                return false;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    //getting details from card which we want to show
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView fullName, userName, email, mobileNo;
        ImageButton flowMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.profile_image_card);
            fullName = itemView.findViewById(R.id.full_name_text_card);
            userName = itemView.findViewById(R.id.user_name_text_card);
            email = itemView.findViewById(R.id.email_text_card);
            mobileNo = itemView.findViewById(R.id.mobileNo_text_card);
            flowMenu = itemView.findViewById(R.id.menu_imageButton_card);
        }
    }
}
