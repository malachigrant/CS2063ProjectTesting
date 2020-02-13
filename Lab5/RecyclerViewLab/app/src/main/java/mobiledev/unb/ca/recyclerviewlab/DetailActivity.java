package mobiledev.unb.ca.recyclerviewlab;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // TODO 1
       //  Get the intent that started this activity, and get the extras from it
       //  corresponding to the title and description of the course
        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        String description = extras.getString("description");

       // TODO 2
       //  Set the description TextView to be the course description
        TextView desc = (TextView) findViewById(R.id.description_textview);
        desc.setText(description);


       // TODO 3
       //  Make the TextView scrollable
        desc.setMovementMethod(new ScrollingMovementMethod());

       // TODO 4
       //  Set the title of the action bar to be the course title
        ActionBar act = getSupportActionBar();
        act.setTitle(title);
    }
}
