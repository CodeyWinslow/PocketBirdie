package com.nativegames.pocketbirdie.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nativegames.pocketbirdie.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    List<Game> gameList;
    OnClickListener onClickListener;

    public GameListAdapter(List<Game> list, OnClickListener callback)
    {
        gameList = list;
        onClickListener = callback;
    }

    public void setGameList(List<Game> list)
    {
        gameList = list;
        notifyDataSetChanged();
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

        SimpleDateFormat sdf = new SimpleDateFormat();
        Date gameDate = Calendar.getInstance().getTime();
        try {
             gameDate = sdf.parse(gameList.get(position).getDate());
        }
        catch (ParseException e)
        {
            //Log.d("GameListAdapter", "There was an issue");
        }
        Date today = Calendar.getInstance().getTime();
        sdf.applyPattern("MM/dd/yyyy");
        String gameDateString = sdf.format(gameDate);
        if (gameDateString.compareTo(sdf.format(today)) == 0)
            gameDateString = "Today";
        holder.date.setText(gameDateString);
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
