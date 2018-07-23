package masum.sqlitedatabaseprojectbitm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by spmas on 13-May-18.
 */

public class MovieDatabaseOpenHelper extends SQLiteOpenHelper {

    //--create database
    public static final String DATABASE_NAME = "movie_database";
    public static final int DATABASE_VERSION = 1;

    public MovieDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //--create tables this format
    public static final String DATABASE_TABLE_NAME = "tbl_movie";
    public static final String COL_ID = "_id";
    public static final String COL_MOVIE_NAME = "movie_name";
    public static final String COL_MOVIE_YEAR = "movie_year";
    public static final String CREAT_TABLE_MOVIE = "create table " + DATABASE_TABLE_NAME + "( " +
            COL_ID + " integer primary key," +
            COL_MOVIE_NAME + " text," +
            COL_MOVIE_YEAR + " text);";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //--create table execute method
        sqLiteDatabase.execSQL(CREAT_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+DATABASE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
