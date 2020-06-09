package com.example.pocketbirdie.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Game implements Parcelable {

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

    protected Game(Parcel in) {
        m_date = in.readString();
        m_park = in.readString();
        if (in.readByte() == 0) {
            m_final_score = null;
        } else {
            m_final_score = in.readInt();
        }
        if (in.readByte() == 0) {
            m_game_par = null;
        } else {
            m_game_par = in.readInt();
        }
        if (in.readByte() == 0) {
            m_holes = null;
        } else {
            m_holes = in.readInt();
        }
        m_hole_scores = (Integer[]) in.readArray(Object[].class.getClassLoader());
        m_hole_pars = (Integer[]) in.readArray(Object[].class.getClassLoader());
        m_hole_ids = (Long[]) in.readArray(Object[].class.getClassLoader());
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(m_date);
        dest.writeString(m_park);
        if (m_final_score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(m_final_score);
        }
        if (m_game_par == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(m_game_par);
        }
        if (m_holes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(m_holes);
        }

        dest.writeArray(m_hole_scores);
        dest.writeArray(m_hole_pars);
        dest.writeArray(m_hole_ids);
    }
}
