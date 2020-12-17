package com.optic.projectofinal.UI.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ActivityValuationBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.models.Valuation;
import com.optic.projectofinal.providers.JobsDatabaseProvider;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class ValuationActivity extends AppCompatActivity {

    private ActivityValuationBinding binding;
    private double rating1,rating2,rating3,rating4;
    private String idJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityValuationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle("Envia tu valoracion");

        idJob=getIntent().getStringExtra("idJob");

        rating1=1;
        rating2=1;
        rating3=1;
        rating4=1;

        binding.rating1.setOnRatingChangeListener((ratingBar, rating) ->{ rating1=rating;reloadTotalRating();});
        binding.rating2.setOnRatingChangeListener((ratingBar, rating) ->{ rating2=rating;reloadTotalRating();});
        binding.rating3.setOnRatingChangeListener((ratingBar, rating) -> { rating3=rating;reloadTotalRating();});
        binding.rating4.setOnRatingChangeListener((ratingBar, rating) ->{ rating4=rating;reloadTotalRating();});
    }
    public void reloadTotalRating(){
        double total=rating1+rating2+rating3+rating4;
        binding.ratingTotal.setRating((float)total/4);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_valuation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.btnSave){
            Valuation valuation=new Valuation(rating1,rating2,rating3,rating4);
            Job job=new Job();
            job.setId(idJob);
            job.setValuation(valuation);
            if(binding.opinion.getEditText().getText().toString().length()>0){
                job.setOpinionUserOffer(binding.opinion.getEditText().getText().toString());
            }
            new JobsDatabaseProvider().updateJob(job).addOnFailureListener(v-> Log.e(TAG_LOG, "update job onOptionsItemSelected: "+v.getMessage() ));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}