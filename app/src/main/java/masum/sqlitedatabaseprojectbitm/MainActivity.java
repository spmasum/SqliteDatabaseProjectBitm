package masum.sqlitedatabaseprojectbitm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


public class MainActivity extends AbstractRuntimePermission implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private EditText movieNameET;
    private EditText movieYearET;
    private ListView movieList;
    private MovieAdapter movieAdapter;
    private ArrayList<MovieModel> movieModelArrayList;
    private MovieDatabaseOperations movieDatabaseOperations;
    private int rowId;
    private String name, year;
    private Button saveMovieInfo;

    //--timer task variable
    private Handler handler;
    private int delay = 5000;
    private Runnable runnable;

    //--Location task variable
    private TextView locationTV;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    String latitude = null;
    String longitude = null;

    //--
    private static final int MY_REQUEST_PERMISSION=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = findViewById(R.id.movieLV);
        movieNameET = findViewById(R.id.movieNameET);
        movieYearET = findViewById(R.id.movieYearET);
        saveMovieInfo = findViewById(R.id.saveMovieInfo);
        this.showList();

//--timer task
        handler = new Handler();
        runTimer();
        handler.post(runnable);
//-


        //--Location Task
        locationTV = findViewById(R.id.location);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        //--
        rowId = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        year = getIntent().getStringExtra("year");

        movieNameET.setText(name);
        movieYearET.setText(year);
        if (rowId > 0) {
            saveMovieInfo.setText("Update");
        }


        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int rowId = movieModelArrayList.get(position).getMovieId();
                String movieName = movieModelArrayList.get(position).getMovieName();
                String movieYear = movieModelArrayList.get(position).getMovieYear();
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("id", rowId);
                intent.putExtra("name", movieName);
                intent.putExtra("year", movieYear);
                startActivity(intent);
                finish();
            }
        });

        requestAppPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION},R.string.snackbarMsg,MY_REQUEST_PERMISSION);



    }

    @Override
    public void onPermissionGranted(int requestCode) {
        //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
    }

    //--timer task method
    private void runTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                saveInfo();
                handler.postDelayed(this, delay);
            }
        };
    }


    private void demoToast() {
        Toast.makeText(this, "I am demo toast", Toast.LENGTH_SHORT).show();
    }

    private void showList() {
        movieDatabaseOperations = new MovieDatabaseOperations(this);
        movieModelArrayList = movieDatabaseOperations.getAllMovies();
        movieAdapter = new MovieAdapter(this, movieModelArrayList);
        movieList.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    public void saveMovieInformation(View view) {
//        String movieName = movieNameET.getText().toString();
//        String movieYear = movieYearET.getText().toString();
        handler.removeCallbacks(runnable);
        this.saveInfo();
    }

    public void saveInfo() {

        String movieName = movieNameET.getText().toString();
        String movieYear = movieYearET.getText().toString();


        if (movieName.isEmpty()) {
            movieNameET.setError(getString(R.string.empty_error_msg));
        }
        if (movieYear.isEmpty()) {
            movieYearET.setError(getString(R.string.empty_error_msg));
        } else
            {
            if (rowId > 0) {
                MovieModel movieModel = new MovieModel(movieName, movieYear);
                boolean status = movieDatabaseOperations.updateMovie(movieModel, rowId);
                if (status) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    this.showList();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Update", Toast.LENGTH_SHORT).show();
                }


            } else {
                MovieModel movieModel = new MovieModel(movieName, movieYear);
                boolean status = movieDatabaseOperations.addMovie(movieModel);

                if (status) {
                    Toast.makeText(this, R.string.success_msg, Toast.LENGTH_SHORT).show();
                    movieNameET.setText("");
                    movieYearET.setText("");
                    this.showList();
                } else {
                    Toast.makeText(this, R.string.failed_msg, Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    //Location task method


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude= String.valueOf(location.getLatitude());
        longitude= String.valueOf(location.getLongitude());
        movieNameET.setText(latitude);
        movieYearET.setText(longitude);
        locationTV.setText(latitude+"\n"+longitude);

    }
}
