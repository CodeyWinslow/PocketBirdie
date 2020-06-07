package com.example.pocketbirdie.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    String m_date;
    String m_park;
    Integer m_final_score;
    Integer m_game_par;
    Integer m_holes;
    ArrayList<Integer> m_hole_scores;
    ArrayList<Integer> m_hole_pars;

    public Game(String park, String date)
    {
        m_park = park;
        m_date = date;
    }

    private void calcFinalScore()
    {

    }

    private void calcGamePar()
    {

    }

    //getters
    public String getDate() { return m_date; }
    public String getParkName() { return m_park; }
    public Integer getFinalScore() { return m_final_score; }
    public Integer getGamePar() { return m_game_par; }
    public Integer getNumHoles() { return m_holes; }

    //setters
    public void setDate(String date) { m_date = date; }
    public void setParkName(String park) { m_park = park; }
    public void setHole(int hole, Integer par, Integer score)
    {
        m_hole_pars.set(hole, par);
        m_hole_scores.set(hole, score);
        calcFinalScore();
        calcGamePar();
    }
}
