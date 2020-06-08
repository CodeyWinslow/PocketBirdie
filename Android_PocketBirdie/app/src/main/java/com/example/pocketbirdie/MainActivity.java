package com.example.pocketbirdie;

import com.example.pocketbirdie.fragments.*;
import com.example.pocketbirdie.model.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String DEBUG_TAG = "ACTIVITY_MAIN";

        Toolbar toolbar;
        DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setContents();
        setDatabase();
        LoadNewGameFragment();
    }

    private void setToolbar()
    {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    private void setContents()
    {
        drawer = findViewById(R.id.games_drawer);
    }

    private void setDatabase()
    {
        DBInteract.InitDB(this);
        Game game = new Game("Whistler's bend", "TODAY", 2);
        game.setHole(0, 3,0);
        game.setHole(1,3,-1);
        DBInteract.saveNewGame(game);
        List<Game> gameList = DBInteract.getAllGames();
    }

    private void LoadNewGameFragment()
    {
        NewGameFragment frag = new NewGameFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main_content, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

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
}