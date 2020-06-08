package com.example.pocketbirdie.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    String m_date;
    String m_park;
    Integer m_final_score;
    Integer m_game_par;
    Integer m_holes;
    Integer[] m_hole_scores;
    Integer[] m_hole_pars;
    Long[] m_hole_ids;

    public Game(String park, String date, Integer holes)
    {
        m_park = park;
        m_date = date;
        m_holes = holes;
        m_hole_scores = new Integer[holes];
        m_hole_pars = new Integer[holes];
        m_hole_ids = new Long[holes];
        for (int i = 0; i < holes; ++i)
        {
            m_hole_scores[i] = 0;
            m_hole_pars[i] = 3;
            m_hole_ids[i] = -1l;
        }
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
    public Integer getHoleScore(Integer hole) { return m_hole_scores[hole]; }
    public Integer getHolePar(Integer hole) { return m_hole_pars[hole]; }
    public Long getHoleId(Integer hole) { return m_hole_ids[hole]; }

    //setters
    public void setDate(String date) { m_date = date; }
    public void setParkName(String park) { m_park = park; }
    public void setHoleId(Integer hole, Long id) { m_hole_ids[hole] = id; }
    public void setHole(int hole, Integer par, Integer score)
    {
        m_hole_pars[hole] = par;
        m_hole_scores[hole] = score;
        calcFinalScore();
        calcGamePar();
    }
}
