package com.optic.projectofinal.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.SliderAdapterExample;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class JobDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_done);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapterExample(this));

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setAutoCycle(false);


        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view_desc_buyer);
        ExpandableTextView expTv2 = (ExpandableTextView) findViewById(R.id.expand_text_view_desc_worker);
        expTv1.setText("linea1 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2  ");
        expTv2.setText("linea1 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2 \r\n linea2  ");
    }
}