package com.optic.projectofinal.UI.activities;

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
import com.optic.projectofinal.UI.activities.fragments.ChatsFragment;
import com.optic.projectofinal.UI.activities.fragments.PostsFragment;
import com.optic.projectofinal.UI.activities.fragments.ProfileFragment;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.UserDatabaseProvider;

import java.util.ArrayList;
import java.util.List;

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

        List<User> misUser= new ArrayList<User>();
        misUser.add(new User("Frank","Rodrgiuez aguirre","mi horario","sobre mi Frank","alcobendas",0));
        misUser.add(new User("Roberto","Guijarro Gomez","mi horario","sobre mi Roberto","Sanse",0));
        misUser.add(new User("Adrain ","Herrera Garcia","mi horario","sobre mi Adrain","alcorocon",0));
        misUser.add(new User("Iniesta","Perez","mi horario","sobre mi Iniesta","algete",0));
        misUser.add(new User("Pepe","Villuela","mi horario","sobre mi Pepe","sol",0));
        misUser.add(new User("Maria","Ferandez Ruiz","mi horario","sobre mi Maria","nuevos ministerios",1));


        UserDatabaseProvider userData = new UserDatabaseProvider();
        for(User i:misUser){
            userData.createUser2(i);
        }
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