package com.example.pocketbirdie.model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBInteract extends SQLiteOpenHelper {
    private static String DBaseName = "PocketBirdieDB";
    private static int DBaseVersion = 1;
    private static Context DBaseContext;
    private static SQLiteDatabase DBase;
    private static DBInteract _instance;

    public static Game getGame(int id)
    {
        //to implement
        return null;
    }

    public static void saveNewGame(Game game)
    {
        //to implement
    }

    public static void updateGame(int id, Game game)
    {
        //to implement
    }

    public static List<Game> getAllGames()
    {
        Cursor cur;
        Cursor holeCursor;
        List<Game> gameList = new ArrayList<>();
        Game game;

        Integer gameId;
        String date;
        String park;
        ArrayList<Integer> holePars = new ArrayList<>();
        ArrayList<Integer> holeScores = new ArrayList<>();

        Integer holeNum;
        Integer holePar;
        Integer holeScore;

        cur = DBase.rawQuery("SELECT _id, PARK, DATE FROM GAMES;", null);
        while (!cur.isAfterLast())
        {
            gameId = cur.getInt(0);
            park = cur.getString(1);
            date = cur.getString(2);

            game = new Game(park, date);

            holeCursor = DBase.rawQuery("SELECT HOLE_NUM, HOLE_PAR, HOLE_SCORE "+
                    "FROM GAMES_TO_HOLES JOIN GAMES ON GAMES._id = GAMES_TO_HOLES.GAME_ID " +
                    "WHERE GAMES_TO_HOLES.GAME_ID = ?;", new String[] {gameId.toString()});

            while (!holeCursor.isAfterLast())
            {
                holeNum = holeCursor.getInt(0);
                holePar = holeCursor.getInt(1);
                holeScore = holeCursor.getInt(2);

                game.setHole(holeNum, holePar, holeScore);

                holeCursor.moveToNext();
            }

            gameList.add(game);

            cur.moveToNext();
        }

        return gameList;
    }

    public static void InitDB(Activity activity)
    {
        //initialize Games table
        _instance = new DBInteract(activity.getApplicationContext());
        DBase = _instance.getWritableDatabase();
        activity.getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(DBaseName, DBase.getPath())
                .apply();
        DBaseContext = activity.getApplicationContext();
    }

    public static void LoadDB(Activity activity)
    {
        String dbPath = activity.getPreferences(Context.MODE_PRIVATE)
                .getString(DBaseName,"");
        DBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        DBaseContext = activity.getApplicationContext();
    }

    public DBInteract(Context context)
    {
        super(context, DBaseName, null, DBaseVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE GAMES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PARK TEXT, DATE TEXT);");
        db.execSQL("CREATE TABLE GAMES_TO_HOLES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "GAME_ID INTEGER, HOLE_NUM INTEGER, HOLE_PAR INTEGER, HOLE_SCORE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing
    }
}
