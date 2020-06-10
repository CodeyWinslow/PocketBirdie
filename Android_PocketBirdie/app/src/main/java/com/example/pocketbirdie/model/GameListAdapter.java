package com.example.pocketbirdie.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketbirdie.R;

import org.w3c.dom.Text;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    List<Game> gameList;
    OnClickListener onClickListener;

    public GameListAdapter(List<Game> list, OnClickListener callback)
    {
        gameList = list;
        onClickListener = callback;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res = R.layout.gameitem;
        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);

        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        holder.parkTitle.setText(gameList.get(position).getParkName());
        holder.date.setText(gameList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    class GameViewHolder extends RecyclerView.ViewHolder
                            implements View.OnClickListener
    {
        View view;
        public TextView parkTitle;
        public TextView date;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
            parkTitle = view.findViewById(R.id.gameitem_park);
            date = view.findViewById(R.id.gameitem_date);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(getAdapterPosition());
        }
    }

    public interface OnClickListener
    {
        void onClick(Integer position);
    }
}
