package masum.sqlitedatabaseprojectbitm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MovieDetailsActivity extends AppCompatActivity {
    private TextView movieName;
    private TextView movieYear;
    private int rowId;
    private String name,year;
    private MovieDatabaseOperations movieDatabaseOperations ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieName=findViewById(R.id.name);
        movieYear=findViewById(R.id.year);
        rowId=getIntent().getIntExtra("id",0);
        name=getIntent().getStringExtra("name");
        year=getIntent().getStringExtra("year");

        movieName.setText(name);
        movieYear.setText(year);
        movieDatabaseOperations=new MovieDatabaseOperations(this);
    }

    public void updateInfo(View view) {

        Intent intent =new Intent(MovieDetailsActivity.this,MainActivity.class);
        intent.putExtra("id",rowId);
        intent.putExtra("name",name);
        intent.putExtra("year",year);
        startActivity(intent);
        finish();
    }

    public void deleteInfo(View view) {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Deleted Item");
        alert.setMessage("Are you sure to delete this item?");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean status=movieDatabaseOperations.deleteMovie(rowId);
                if (status){
                    Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MovieDetailsActivity.this,MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed To Delete",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel",null);
        alert.show();
    }
}
