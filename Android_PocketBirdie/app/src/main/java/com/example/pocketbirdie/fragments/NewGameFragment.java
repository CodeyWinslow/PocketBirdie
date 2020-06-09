package com.example.pocketbirdie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pocketbirdie.R;
import com.example.pocketbirdie.model.Game;

public class NewGameFragment extends Fragment implements View.OnClickListener {


    public static String Fragment_Tag = "FRAG_TAG_NEWGAME";


    ImageButton AddButton;
    EditText parkName;
    EditText numHoles;

    public NewGameFragment() {
        // Required empty public constructor
    }

    private void LoadGameFragment()
    {
        FragmentTransaction ft;
        Game newGame;

        String park = parkName.getText().toString();
        Integer holes = Integer.parseInt(numHoles.getText().toString());

        newGame = new Game(park, "TODAY", holes);

        GameFragment frag = new GameFragment(newGame);
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main_content, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();

        Fragment oldFrag = getActivity().getSupportFragmentManager().findFragmentByTag(Fragment_Tag);
        if (oldFrag != null)
        {
            ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(oldFrag);
            ft.commit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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