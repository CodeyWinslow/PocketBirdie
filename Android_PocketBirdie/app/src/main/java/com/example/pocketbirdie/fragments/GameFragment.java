package com.example.pocketbirdie.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.BaseBundle;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pocketbirdie.MainActivity;
import com.example.pocketbirdie.R;
import com.example.pocketbirdie.model.DBInteract;
import com.example.pocketbirdie.model.Game;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.Objects;

public class GameFragment extends Fragment implements View.OnClickListener {

    static String BUNDLE_GAME = "BUNDLEKEY_GAME";

    //vals
    Integer score;
    Integer par;
    Integer hole;
    Game currentGame;

    //xml
    TextView scoreView;
    TextView parView;
    TextView parkTitle;
    TextView holeView;
    ImageButton reducePar;
    ImageButton increasePar;
    FloatingActionButton increaseScore;
    FloatingActionButton reduceScore;
    FloatingActionButton previousHole;
    FloatingActionButton nextHole;

    public GameFragment() {
        // Required empty public constructor
    }

    public GameFragment(Game existingGame)
    {
        currentGame = existingGame;
    }

    void DisplayHoleDetails()
    {
        par = currentGame.getHolePar(hole);
        score = currentGame.getHoleScore(hole);

        String holeString = (new Integer(hole+1)).toString();
        holeView.setText("Hole " + holeString);
        parView.setText("Par " + par.toString());
        String scoreString = score.toString();
        if (score > 0)
            scoreString = "+" + scoreString;
        scoreView.setText(scoreString);
    }

    void AdjustHole(Integer amount)
    {
        //prepare
        Integer newHole = hole + amount;
        if (newHole < currentGame.getNumHoles()
                && newHole >= 0) {
            hole = newHole;

            //save to db
            DBInteract.updateGame(currentGame);

            //save prefs
            requireActivity().getPreferences(Context.MODE_PRIVATE)
                    .edit()
                    .putInt(MainActivity.Pref_CurrentHole, hole)
                    .apply();

            //display
            DisplayHoleDetails();
        }
    }

    void AdjustScore(Integer amount)
    {
        //prepare
        score += amount;
        String scoreString = score.toString();
        if (score > 0)
            scoreString = "+" + scoreString;

        //save to game object
        currentGame.setHole(hole, par, score);

        //display
        scoreView.setText(scoreString);
    }

    void AdjustPar(Integer amount)
    {
        //prepare
        par += amount;
        String parString = "Par " + par.toString();

        //save to game object
        currentGame.setHole(hole, par, score);

        //display
        parView.setText(parString);
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
        else
        {
            //set prefs
            requireActivity().getPreferences(Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean(MainActivity.Pref_GameInProgress, true)
                    .putLong(MainActivity.Pref_CurrentGame, currentGame.getDbId())
                    .apply();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        parkTitle = view.findViewById(R.id.game_park_title);
        holeView = view.findViewById(R.id.game_hole_num);
        parView = view.findViewById(R.id.game_par);
        scoreView = view.findViewById(R.id.game_score);
        reducePar = view.findViewById(R.id.game_par_reduce);
        reducePar.setOnClickListener(this);
        increasePar = view.findViewById(R.id.game_par_increase);
        increasePar.setOnClickListener(this);
        reduceScore = view.findViewById(R.id.game_score_reduce);
        reduceScore.setOnClickListener(this);
        increaseScore = view.findViewById(R.id.game_score_increase);
        increaseScore.setOnClickListener(this);
        previousHole = view.findViewById(R.id.game_previous_hole);
        previousHole.setOnClickListener(this);
        nextHole = view.findViewById(R.id.game_next_hole);
        nextHole.setOnClickListener(this);

        if (currentGame == null) {
            getActivity().finish();
        }

        parkTitle.setText(currentGame.getParkName());
        hole = requireActivity().getPreferences(Context.MODE_PRIVATE)
                .getInt(MainActivity.Pref_CurrentHole, 0);
        requireActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putInt(MainActivity.Pref_CurrentHole, hole)
                .apply();

        DisplayHoleDetails();

        return view;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_GAME, currentGame);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.game_par_reduce:
                if (par > 1)
                    AdjustPar(-1);
                break;
            case R.id.game_par_increase:
                AdjustPar(1);
                break;
            case R.id.game_score_reduce:
                AdjustScore(-1);
                break;
            case R.id.game_score_increase:
                AdjustScore(1);
                break;
            case R.id.game_previous_hole:
                AdjustHole(-1);
                break;
            case R.id.game_next_hole:
                AdjustHole(1);
                break;
        }
    }
}