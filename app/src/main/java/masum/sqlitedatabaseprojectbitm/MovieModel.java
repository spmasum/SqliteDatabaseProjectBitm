package masum.sqlitedatabaseprojectbitm;

/**
 * Created by spmas on 13-May-18.
 */

public class MovieModel {
    private String movieName;
    private String movieYear;
    private int movieId;

    public MovieModel(String movieName, String movieYear) {
        this.movieName = movieName;
        this.movieYear = movieYear;
    }

    public MovieModel(String movieName, String movieYear, int movieId) {
        this.movieName = movieName;
        this.movieYear = movieYear;
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }
}
