package com.optic.projectofinal.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.optic.projectofinal.R;
import com.optic.projectofinal.activites.fragments.ChatsFragment;
import com.optic.projectofinal.activites.fragments.PostsFragment;
import com.optic.projectofinal.activites.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private PostsFragment postsFragment;
    private ChatsFragment chatsFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        BadgeDrawable bg = bottomNavigation.getOrCreateBadge(R.id.btnMenuChats);
        bg.setBackgroundColor(Color.RED);
        bg.setVisible(true);
        bg.setNumber(13);



//        mToolbar=findViewById(R.id.TOOLBAR);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Main Principal");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }



    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.btnMenuPosts:
                            openFragment((postsFragment==null)?postsFragment=new PostsFragment():postsFragment);
                           
                            return true;
                        case R.id.btnMenuChats:
                            openFragment((chatsFragment==null)?chatsFragment=new ChatsFragment():chatsFragment);
                            return true;
                        case R.id.btnMenuProfile:
                            //startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
                            openFragment((profileFragment==null)?profileFragment=new ProfileFragment():profileFragment);
                            return true;
                    }
                    return false;
                }
            };
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}