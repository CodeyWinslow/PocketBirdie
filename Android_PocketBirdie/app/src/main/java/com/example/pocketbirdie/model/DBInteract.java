package com.example.pocketbirdie.model;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
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
    private static String DBaseName = "PocketBirdieDB.db";
    private static int DBaseVersion = 1;
    private static Context DBaseContext;
    private static SQLiteDatabase DBase;
    private static DBInteract _instance;

    public static Game getGame(Long id)
    {
        Game game;
        Cursor cur;
        String park;
        String date;
        Integer holes;

        Integer holeNum;
        Integer holeScore;
        Integer holePar;
        Long holeId;

        cur = DBase.rawQuery("SELECT PARK, DATE, HOLES " +
                "FROM GAMES WHERE _id = ?;", new String[] {id.toString()});

        park = cur.getString(0);
        date = cur.getString(1);
        holes = cur.getInt(2);

        game = new Game(park, date, holes);

        cur = DBase.rawQuery("SELECT _id, HOLE_NUM, HOLE_PAR, HOLE_SCORE "+
                "FROM GAMES_TO_HOLES " +
                "WHERE GAME_ID = ?;", new String[] {id.toString()});

        while (!cur.isAfterLast())
        {
            holeId = new Long(cur.getInt(0));
            holeNum = cur.getInt(1);
            holePar = cur.getInt(2);
            holeScore = cur.getInt(3);

            game.setHole(holeNum, holePar, holeScore);
            game.setHoleId(holeNum, holeId);
        }

        return game;
    }

    public static void saveNewGame(Game game)
    {
        SQLiteDatabase db = _instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PARK", game.getParkName());
        values.put("DATE", game.getDate());
        values.put("HOLES", game.getNumHoles());
        Long gameId = db.insert("GAMES", null, values);
        for (int i = 0; i < game.getNumHoles(); ++i) {
            values = new ContentValues();
            values.put("GAME_ID", gameId);
            values.put("HOLE_NUM", i);
            values.put("HOLE_SCORE", game.getHoleScore(i));
            values.put("HOLE_PAR", game.getHolePar(i));
            game.setHoleId(
                    i,
                    db.insert("GAMES_TO_HOLES", null, values));
        }
    }

    public static void updateGame(Long id, Game game)
    {
        ContentValues values = new ContentValues();
        values.put("PARK", game.getParkName());
        values.put("DATE", game.getDate());
        values.put("HOLES", game.getNumHoles());
        DBase.update("GAMES", values, "_id = ?", new String[] { id.toString()});

        for (int i = 0; i < game.getNumHoles(); ++i) {
            values = new ContentValues();
            values.put("GAME_ID", id);
            values.put("HOLE_NUM", i);
            values.put("HOLE_SCORE", game.getHoleScore(i));
            values.put("HOLE_PAR", game.getHolePar(i));
            if (game.getHoleId(i) < 0) {
                game.setHoleId(
                        i,
                        DBase.insert("GAMES_TO_HOLES", null, values));
            }
            else
            {
                DBase.update("GAMES_TO_HOLES",
                        values,
                        "_id = ?",
                        new String[] {game.getHoleId(i).toString()});
            }
        }
    }

    public static List<Game> getAllGames()
    {
        SQLiteDatabase db = _instance.getReadableDatabase();
        Cursor cur;
        Cursor holeCursor;
        List<Game> gameList = new ArrayList<>();
        Game game;

        Integer gameId;
        String date;
        String park;
        Integer holes;

        Integer holeNum;
        Integer holePar;
        Integer holeScore;

        cur = DBase.rawQuery("SELECT _id, PARK, DATE, HOLES FROM GAMES;", null);
        if (cur.moveToFirst() && cur.getCount() > 0) {
            while (!cur.isAfterLast()) {
                gameId = cur.getInt(0);
                park = cur.getString(1);
                date = cur.getString(2);
                holes = cur.getInt(3);

                game = new Game(park, date, holes);

                holeCursor = DBase.rawQuery("SELECT HOLE_NUM, HOLE_PAR, HOLE_SCORE " +
                        "FROM GAMES_TO_HOLES " +
                        "WHERE GAME_ID = ?;", new String[]{gameId.toString()});

                if (holeCursor.moveToFirst() && holeCursor.getCount() > 0) {
                    while (!holeCursor.isAfterLast()) {
                        holeNum = holeCursor.getInt(0);
                        holePar = holeCursor.getInt(1);
                        holeScore = holeCursor.getInt(2);

                        game.setHole(holeNum, holePar, holeScore);

                        holeCursor.moveToNext();
                    }
                    holeCursor.close();
                }

                gameList.add(game);

                cur.moveToNext();
            }
            cur.close();
        }

        return gameList;
    }

    public static void InitDB(Context context)
    {
        //initialize Games table
        _instance = new DBInteract(context);
        DBase = _instance.getWritableDatabase();
        DBaseContext = context;
    }

    public static void DeleteDB()
    {
        DBaseContext.deleteDatabase(DBaseName);
    }

    public DBInteract(Context context)
    {
        super(context, DBaseName, null, DBaseVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        DBase = db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE GAMES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PARK TEXT, DATE TEXT, HOLES INTEGER);");
        db.execSQL("CREATE TABLE GAMES_TO_HOLES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "GAME_ID INTEGER, HOLE_NUM INTEGER, HOLE_PAR INTEGER, HOLE_SCORE INTEGER);");
        DBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing
    }
}
