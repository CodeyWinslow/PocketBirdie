package com.example.pocketbirdie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pocketbirdie.R;

public class NewGameFragment extends Fragment implements View.OnClickListener {


    public static String Fragment_Tag = "FRAG_TAG_NEWGAME";
    ImageButton AddButton;

    public NewGameFragment() {
        // Required empty public constructor
    }

    private void LoadGameFragment(int id)
    {
        FragmentTransaction ft;

        GameFragment frag = new GameFragment();
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
        AddButton = getView().findViewById(R.id.main_add_button);
        AddButton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_game, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.main_add_button:
                LoadGameFragment(0);
        }
    }
}