package com.example.pocketbirdie.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pocketbirdie.MainActivity;
import com.example.pocketbirdie.R;
import com.example.pocketbirdie.model.DBInteract;
import com.example.pocketbirdie.model.Game;

import java.util.Calendar;
import java.util.Date;

public class NewGameFragment extends Fragment implements View.OnClickListener {


    public static String Fragment_Tag = "FRAG_TAG_NEWGAME";


    ImageButton AddButton;
    EditText parkName;
    EditText numHoles;

    public NewGameFragment() {
    }

    private void LoadGameFragment()
    {
        FragmentTransaction ft;
        Game newGame;

        String park = parkName.getText().toString();
        Integer holes = Integer.parseInt(numHoles.getText().toString());

        requireActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putInt(MainActivity.Pref_CurrentHole, 0)
                .apply();

        Date currentTime = Calendar.getInstance().getTime();

        newGame = new Game(park, currentTime.toString(), holes);

        DBInteract.saveNewGame(newGame);

        GameFragment frag = new GameFragment(newGame);
        ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main_content, frag);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //update game list
        MainActivity activity = (MainActivity)getActivity();
        if (activity != null && activity.isInitialized()) {
            activity.updateGameList();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);
        AddButton = view.findViewById(R.id.main_add_button);
        AddButton.setOnClickListener(this);
        parkName = view.findViewById(R.id.newgame_parkname);
        numHoles = view.findViewById(R.id.newgame_numholes);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.main_add_button:
                LoadGameFragment();
        }
    }
}