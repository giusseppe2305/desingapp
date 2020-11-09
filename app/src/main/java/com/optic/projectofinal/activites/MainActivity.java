package com.optic.projectofinal.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.optic.projectofinal.R;
import com.optic.projectofinal.fragments.ChatsFragment;
import com.optic.projectofinal.fragments.PostsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private PostsFragment postsFragment;
    private ChatsFragment chatsFragment;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        mToolbar=findViewById(R.id.TOOLBAR);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Main Principal");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity , menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSeeProfile:{
               startActivity(new Intent(this,ScrollingActivity.class));
                break;
            }
        }
        return false;
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
                            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
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