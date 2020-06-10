package com.example.pocketbirdie;

import com.example.pocketbirdie.fragments.*;
import com.example.pocketbirdie.model.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
                        implements GameListAdapter.OnClickListener {

    static final String DEBUG_TAG = "ACTIVITY_MAIN";
    public static final String Pref_CurrentHole = "PREFKEY_CURRENTHOLE";
    public static final String Pref_GameInProgress = "PREFKEY_GAMEINPROGRESS";
    public static final String Pref_CurrentGame = "PREFKEY_CURRENTGAME";

        Toolbar toolbar;
        DrawerLayout drawer;
        RecyclerView gameList;
        List<Game> listOfGames;
        GameListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setContents();
        setDatabase();
        setGame();
    }

    private void setToolbar()
    {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    private void setContents()
    {
        drawer = findViewById(R.id.games_drawer);
        gameList = findViewById(R.id.games_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        gameList.setHasFixedSize(true);
        gameList.setLayoutManager(layoutManager);
    }

    private void setDatabase()
    {
        DBInteract.InitDB(this);

//        DBInteract.DeleteDB();
//        getPreferences(Context.MODE_PRIVATE)
//                .edit()
//                .putBoolean(Pref_GameInProgress, false)
//                .putInt(Pref_CurrentHole, 0)
//                .apply();

        adapter = new GameListAdapter(DBInteract.getAllGames(),
                this);
        gameList.setAdapter(adapter);
    }

    private void setGame()
    {
        Boolean gameInProgress = getPreferences(Context.MODE_PRIVATE)
                .getBoolean(Pref_GameInProgress, false);

        if (gameInProgress)
        {
            LoadCurrentGame();
        }
        else {
            LoadNewGameFragment();
        }
    }

    public void updateGameList()
    {
        adapter.setGameList(DBInteract.getAllGames());
    }

    private void LoadCurrentGame()
    {
        Long gameId = getPreferences(Context.MODE_PRIVATE)
                .getLong(Pref_CurrentGame, -1);

        if (gameId == -1)
        {
            LoadNewGameFragment();
        }
        else
        {
            Game currentGame = DBInteract.getGame(gameId);

            GameFragment frag = new GameFragment(currentGame);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_main_content, frag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
        }
    }

    private void LoadNewGameFragment()
    {
        //temporary
//        getPreferences(Context.MODE_PRIVATE)
//                .edit()
//                .putInt(MainActivity.Pref_CurrentHole, 0)
//                .apply();

        NewGameFragment frag = new NewGameFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main_content, frag, NewGameFragment.Fragment_Tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_appbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_games_list:
                if (!drawer.isDrawerOpen(Gravity.RIGHT))
                    drawer.openDrawer(Gravity.RIGHT);
                else
                    drawer.closeDrawer(Gravity.RIGHT);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT))
        {
            drawer.closeDrawer(Gravity.RIGHT);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(Integer position) {
        Game game = listOfGames.get(position);

        Toast.makeText(this,
                "You clicked on " + game.getParkName() + "!",
                Toast.LENGTH_SHORT).show();
    }
}