package com.optic.projectofinal.activites;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.viewPagerAdapter;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScrollingActivity extends AppCompatActivity {

    private AppBarLayout appbarlayout;
    private CircleImageView imageProfile;
    private Overla overlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overlay=new Overla(this);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(Color.RED);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new viewPagerAdapter(this));


        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 1) {
                            tab.setText("Tab aaa" + (position + 1));
                            BadgeDrawable bg = tab.getOrCreateBadge();
                            bg.setBackgroundColor(Color.RED);
                            bg.setVisible(true);
                            bg.setNumber(1500);
                        } else {
                            tab.setText("Tab aaa" + (position + 1));
                        }
                    }
                }).attach();

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        CoordinatorLayout coordinatorLayoutContainer = (CoordinatorLayout) findViewById(R.id.imageProfileContainer);

        getSupportActionBar().setTitle("pruena");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        appbarlayout = findViewById(R.id.app_bar);
        imageProfile = findViewById(R.id.imageProfile);
//        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange()) {
//                    toolBarLayout.setExpandedTitleColor(Color.TRANSPARENT);
//                    // Fully expanded
//                    Window window = getWindow();
//                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    window.setStatusBarColor(Color.WHITE);
//                } else if (Math.abs(verticalOffset) == (appBarLayout.getTotalScrollRange())) {
//
//                   // coordinatorLayoutContainer.setVisibility(View.INVISIBLE);
//                    Window window = getWindow();
//                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    window.setStatusBarColor(getResources().getColor(R.color.teal_200));
//                } else {
//                    //imageProfile.setVisibility(View.INVISIBLE);
////                    Window window = getWindow();
////                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////                    window.setStatusBarColor(Color.MAGENTA);
//
//                }
//            }
//        });
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.MAGENTA);
                }
                if (scrollRange + verticalOffset == 0) {
                    toolBarLayout.setTitle("Title");
                    isShow = true;
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.teal_200));
                } else if (isShow) {
                    toolBarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.WHITE);

                }
            }
        });
        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);
        ExpandableTextView expTv2 = (ExpandableTextView) findViewById(R.id.expand_text_view2);
        expTv2.setText("lunes\n" +
                "         martes   ");
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expTv1.setText("Describir es explicar, de manera detallada y ordenada, cómo son las personas, animales, lugares, objetos, etc. La descripción sirve sobre todo para ambientar la acción y crear una que haga más creíbles los hechos que se narran. Muchas veces, contribuyen a detener la acción y preparar el escenario de los hechos que siguen.\n" +
                "\n" +
                "La descripción también se puede definir como la representación verbal de los rasgos propios de un objeto. Al describir una persona, un animal, un sentimiento, etc. Se expresan aquellas características que hacen peculiar a lo descrito, y lo diferencia de otros objetos de otra o de la misma clase.");

        Button prueba2 = findViewById(R.id.btnCall);
//        Animation in= AnimationUtils.loadAnimation(this,R.anim.in);
//        Animation out= AnimationUtils.loadAnimation(this,R.anim.out);
//        out.setBackgroundColor(Color.RED);
//        in.setBackgroundColor(Color.BLUE);
//        out.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                prueba2.setText("cambioo");
//                prueba2.startAnimation(in);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//       prueba2.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               prueba2.startAnimation(out);
//           }
//       });

        findViewById(R.id.ivExampleTooltip).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (view != null) {
                    Toast.makeText(ScrollingActivity.this, "Tooltip: " + view.getTag(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.imageProfile).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                ImageView iv= (ImageView) view;
                new StfalconImageViewer.Builder<String>(ScrollingActivity.this, new String[]{iv.getTag().toString()}, new ImageLoader<String>() {
                    @Override
                    public void loadImage(ImageView imageView, String image) {
                        Picasso.get().load(R.drawable.prueba).into(imageView);

                    }


                }).withOverlayView(overlay).withHiddenStatusBar(false).show();
            }
        });


    }


}