package com.example.pocketbirdie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pocketbirdie.R;
import com.example.pocketbirdie.model.Game;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameDetailsFragment extends Fragment
                            implements View.OnClickListener {

    public static String Fragment_Tag = "FRAG_TAG_DETAILS";
    static String BUNDLE_GAME = "BUNDLEKEY_GAME";

    Game currentGame;

    TextView parkTitle;
    TextView totalScore;
    TextView holes;
    TextView totalThrows;
    TextView gamePar;
    FloatingActionButton shareButton;
    FloatingActionButton okButton;

    public GameDetailsFragment() {
        // Required empty public constructor
    }

    public GameDetailsFragment(Game game)
    {
        currentGame = game;
    }

    void LoadNewGameFragment()
    {
        FragmentTransaction ft;
        NewGameFragment frag = new NewGameFragment();
        ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main_content, frag, NewGameFragment.Fragment_Tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();

        Fragment oldFrag = requireActivity().getSupportFragmentManager().findFragmentByTag(Fragment_Tag);
        if (oldFrag != null)
        {
            ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(oldFrag);
            ft.commit();
        }
    }

    void ShareGame()
    {
        String message = "";

        message += "My disc golf game at " + currentGame.getParkName() + ":\n";
        message += "Final score: " + currentGame.getFinalScore().toString() + "\n";
        message += "Number of holes: " + currentGame.getNumHoles().toString() + "\n";
        message += "Course par: " + currentGame.getGamePar().toString() + "\n";
        message += "Throws: " + currentGame.getNumThrows().toString() + "\n";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "How would you like to send?");
        startActivity(shareIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Game savedGame = savedInstanceState.getParcelable(BUNDLE_GAME);
            if (savedGame != null) {
                currentGame = savedGame;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_GAME, currentGame);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_details, container, false);

        parkTitle = view.findViewById(R.id.details_park);
        totalScore = view.findViewById(R.id.details_totalscore);
        holes = view.findViewById(R.id.details_holes);
        totalThrows = view.findViewById(R.id.details_throws);
        gamePar = view.findViewById(R.id.details_gamepar);
        shareButton = view.findViewById(R.id.details_share);
        shareButton.setOnClickListener(this);
        okButton = view.findViewById(R.id.details_ok);
        okButton.setOnClickListener(this);

        parkTitle.setText(currentGame.getParkName());
        totalScore.setText(currentGame.getFinalScore().toString());
        holes.setText(currentGame.getNumHoles().toString());
        totalThrows.setText(currentGame.getNumThrows().toString());
        gamePar.setText(currentGame.getGamePar().toString());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.details_ok:
                LoadNewGameFragment();
                break;
            case R.id.details_share:
                ShareGame();
                break;
        }
    }
}