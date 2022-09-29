package com.parth.sqldatabas.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.parth.sqldatabas.ContactDetailsActivity;
import com.parth.sqldatabas.R;
import com.parth.sqldatabas.adapter.RoomRecyclerViewAdapter;
import com.parth.sqldatabas.data.RoomDBHandler;
import com.parth.sqldatabas.data.RoomDbDataAccessObject;
import com.parth.sqldatabas.model.RoomDBModel;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ContactFragment extends Fragment implements RoomRecyclerViewAdapter.OnRecyclerViewItemClick {
    RecyclerView recyclerView;
    RoomDbDataAccessObject roomDbDataAccessObject;
    RoomDBHandler roomDBHandler;
    RoomRecyclerViewAdapter adapter;
    List<RoomDBModel> roomDBModelList;
    SwipeRefreshLayout refreshLayout;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_contact_fragment);
        refreshLayout = view.findViewById(R.id.swipe_refresh);

        displayData();
        swipeDelete();

        //refresh layout
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            displayData();
            swipeDelete();
        });

        return view;
    }

    // display data
    private void displayData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        roomDBHandler = Room.databaseBuilder(requireContext(),
                RoomDBHandler.class, "userDetailsRoomDb").allowMainThreadQueries().build();
        roomDbDataAccessObject = roomDBHandler.roomDbDataAccessObject();

        roomDBModelList = roomDbDataAccessObject.getAllData();
        adapter = new RoomRecyclerViewAdapter(roomDBModelList, this);
        recyclerView.setAdapter(adapter);
    }

    //delete single data by swipe in recycler view
    private void swipeDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                roomDbDataAccessObject.deleteData(adapter.deleteSingle(viewHolder.getAdapterPosition()));
                final int position = viewHolder.getAdapterPosition();
                final String item = adapter.removeItem(position);

                Snackbar.make(viewHolder.itemView, "Contact Deleted ", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                        .addSwipeLeftLabel("Delete")
                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
                        .setSwipeLeftLabelColor(R.color.white)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //click on recyclerview items
    @Override
    public void onItemClick(int position) {

        //this is for getting image from database
        RoomDBModel roomDBModel = roomDBModelList.get(position);

        //pass data from database to contact details activity
        Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
        intent.putExtra("name", roomDBModelList.get(position).get_fullName());
        intent.putExtra("image", roomDBModel.get_image());
        intent.putExtra("mobileNO", roomDBModelList.get(position).get_mobileNo());
        intent.putExtra("email", roomDBModelList.get(position).get_email());
        intent.putExtra("userName", roomDBModelList.get(position).get_userName());
        intent.putExtra("id", roomDBModelList.get(position).get_id());

        startActivity(intent);
    }
}