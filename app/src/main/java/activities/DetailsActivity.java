package activities;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import Models.Services;
import codepath.fayberapp.R;

public class DetailsActivity extends AppCompatActivity {
    TextView tvText;
    TextView titre;
    ImageView ImageItem;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        //Display the Up button home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Call a differents contenu,not the same in details page
        Services serv = (Services) getIntent().getSerializableExtra("services");

        tvText = (TextView) findViewById(R.id.tvDetails);
        tvText.setText(serv.getDetails().toString());
        titre = (TextView) findViewById(R.id.tvtitle4detail);
        titre.setText(serv.getTitle().toString());
        ImageItem = (ImageView) findViewById(R.id.ivImage1);
<<<<<<< HEAD
       // ImageItem.setImageResource(Integer.parseInt(serv.getImage()));
=======
>>>>>>> aae36905c1ac897fb7ab1d139ba2eb82860016ca
        button = (Button) findViewById(R.id.btnRegister);

        changeImage(getIntent().getIntExtra("position",0));
    }

    public void changeImage(int position){
        switch (position) {
            case 0:
                ImageItem.setImageResource(R.drawable.fitness);
                break;
            case 1:
                ImageItem.setImageResource(R.drawable.blood);
                break;
            case 2:
                ImageItem.setImageResource(R.drawable.diab);
                break;
            case 3:
                ImageItem.setImageResource(R.drawable.diabetes);
                break;
            case 4:
                ImageItem.setImageResource(R.drawable.diete);
                break;
            case 5:
                ImageItem.setImageResource(R.drawable.ok);
                break;
            case 6:
                ImageItem.setImageResource(R.drawable.reeducationbarres);
                break;
            case 7:
                ImageItem.setImageResource(R.drawable.bandage);
                break;
            case 8:
                ImageItem.setImageResource(R.drawable.mental);
                break;
            default:
        }
    }
    //private ShareActionProvider mShareActionProvider;
   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.icon_share, menu);
       return true;
   }
    //navigate to SignIn activity
    public void onLogButton(View v) {
        Intent i = new Intent(DetailsActivity.this, SignInActivity.class);
        startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                finish();
                return true;
            case R.id.miShare:
                shareInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//
    // To share via another apps
    public void shareInfo(){
<<<<<<< HEAD
        //add the message for sharing
        String messageToShare="Pour plus de detail et de contenu, visitez notre siteWeb- http://fayberagency.com/";
=======
        String messageToShare="visitez notre siteWeb- http://fayberagency.com/";

>>>>>>> aae36905c1ac897fb7ab1d139ba2eb82860016ca

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, titre.getText().toString() + " " + messageToShare);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
