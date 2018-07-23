package masum.sqlitedatabaseprojectbitm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

/**
 * Created by spmas on 13-May-18.
 */

public class MovieDatabaseOperations {
    private MovieDatabaseOpenHelper movieDatabaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
//    private MovieModel movieModel;

    public MovieDatabaseOperations(Context context) {
        this.movieDatabaseOpenHelper = new MovieDatabaseOpenHelper(context);
    }


    public void openDatabase() {
        sqLiteDatabase = movieDatabaseOpenHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        sqLiteDatabase.close();
    }

    public boolean addMovie(MovieModel movieModel) {
        this.openDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieDatabaseOpenHelper.COL_MOVIE_NAME, movieModel.getMovieName());
        values.put(MovieDatabaseOpenHelper.COL_MOVIE_YEAR, movieModel.getMovieYear());
        long id = sqLiteDatabase.insert(MovieDatabaseOpenHelper.DATABASE_TABLE_NAME, null, values);
        this.closeDatabase();
        if (id > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateMovie(MovieModel movieModel,int rowId){
        this.openDatabase();
        ContentValues values=new ContentValues();
        values.put(MovieDatabaseOpenHelper.COL_MOVIE_NAME,movieModel.getMovieName());
        values.put(MovieDatabaseOpenHelper.COL_MOVIE_YEAR,movieModel.getMovieYear());
        long id=sqLiteDatabase.update(MovieDatabaseOpenHelper.DATABASE_TABLE_NAME,values,MovieDatabaseOpenHelper.COL_ID+"=?",new String[]{String.valueOf(rowId)});
        this.closeDatabase();
        if (id>0){
            return  true;
        }else {
            return false;
        }
    }



    public boolean deleteMovie(int rowId) {
        this.openDatabase();
        int deletedItemId = sqLiteDatabase.delete(MovieDatabaseOpenHelper.DATABASE_TABLE_NAME, MovieDatabaseOpenHelper.COL_ID + "=?", new String[]{String.valueOf(rowId)});
        this.closeDatabase();
        if (deletedItemId > 0) {
            return true;
        } else {
            return false;
        }

    }




    public ArrayList<MovieModel> getAllMovies() {
        ArrayList<MovieModel> movieModelArrayList = new ArrayList<>();
        this.openDatabase();
        Cursor cursor = sqLiteDatabase.query(MovieDatabaseOpenHelper.DATABASE_TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            do {
                int movieId = cursor.getInt(cursor.getColumnIndex(MovieDatabaseOpenHelper.COL_ID));
                String movieName = cursor.getString(cursor.getColumnIndex(MovieDatabaseOpenHelper.COL_MOVIE_NAME));
                String movieYear = cursor.getString(cursor.getColumnIndex(MovieDatabaseOpenHelper.COL_MOVIE_YEAR));


                movieModelArrayList.add(new MovieModel(movieName, movieYear, movieId));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        this.closeDatabase();
        return movieModelArrayList;
    }
}
