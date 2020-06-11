package com.nativegames.pocketbirdie.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nativegames.pocketbirdie.MainActivity;
import com.nativegames.pocketbirdie.R;
import com.nativegames.pocketbirdie.model.DBInteract;
import com.nativegames.pocketbirdie.model.Game;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameFragment extends Fragment implements View.OnClickListener {

    static String BUNDLE_GAME = "BUNDLEKEY_GAME";
    static String Fragment_Tag = "FRAG_TAG_GAME";

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

    void setContent(View view)
    {
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
        if (hole == 0)
            previousHole.setVisibility(View.GONE);
        else
            previousHole.setVisibility(View.VISIBLE);

        if (hole == currentGame.getNumHoles() - 1)
            nextHole.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_check_24));
        else
            nextHole.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_24));
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

    void FinishGame()
    {
        DialogInterface.OnClickListener whenAnswered
                = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //save to db
                DBInteract.updateGame(currentGame);

                //save prefs
                requireActivity().getPreferences(Context.MODE_PRIVATE)
                        .edit()
                        .putInt(MainActivity.Pref_CurrentHole, hole)
                        .apply();

                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //save to db
                        DBInteract.updateGame(currentGame);

                        //fix prefs
                        requireActivity().getPreferences(Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean(MainActivity.Pref_GameInProgress, false)
                                .putInt(MainActivity.Pref_CurrentHole, 0)
                                .putLong(MainActivity.Pref_CurrentGame, -1)
                                .apply();

                        //move to stats page
                        FragmentTransaction ft;
                        GameDetailsFragment frag = new GameDetailsFragment(currentGame);
                        ft = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_main_content, frag, GameDetailsFragment.Fragment_Tag);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();

                        Fragment oldFrag = requireActivity().getSupportFragmentManager().findFragmentByTag(Fragment_Tag);
                        if (oldFrag != null)
                        {
                            ft = requireActivity().getSupportFragmentManager().beginTransaction();
                            ft.remove(oldFrag);
                            ft.commit();
                        }

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Finish game?")
                .setPositiveButton("Yeah", whenAnswered)
                .setNegativeButton("Nah", whenAnswered)
                .show();
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

        setContent(view);

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
        currentGame.setHole(hole, par, score);
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
                if (hole == currentGame.getNumHoles() - 1)
                    FinishGame();
                else
                    AdjustHole(1);
                break;
        }
    }
}