package com.optic.projectofinal.UI.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.fragments.ChatsFragment;
import com.optic.projectofinal.UI.activities.fragments.JobsFragment;
import com.optic.projectofinal.UI.activities.fragments.ProfileFragment;
import com.optic.projectofinal.UI.activities.fragments.WorkersFragment;
import com.optic.projectofinal.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "own";
    private ActivityMainBinding binding;
    private WorkersFragment workersFragment;
    private JobsFragment jobsFragment;
    private ChatsFragment chatsFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setSupportActionBar(binding.toolbar.ownToolbar);


        BadgeDrawable bg = binding.bottomNavigation.getOrCreateBadge(R.id.btnMenuChats);
        bg.setBackgroundColor(Color.RED);
        bg.setVisible(true);
        bg.setNumber(13);
        ///open first fragment
        openFragment((workersFragment ==null)? workersFragment =new WorkersFragment(): workersFragment);
        getSupportActionBar().setTitle(getString(R.string.menuNameWorkers));


//        mToolbar=findViewById(R.id.TOOLBAR);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Main Principal");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        List<User> misUser= new ArrayList<User>();
//        misUser.add(new User("Frank","Rodrgiuez aguirre","mi horario","sobre mi Frank","alcobendas",0,"https://cdnb.20m.es/sites/112/2019/04/cara6-620x618.jpg"));
//        misUser.add(new User("Roberto","Guijarro Gomez","mi horario","sobre mi Roberto","Sanse",0,"https://img.freepik.com/foto-gratis/retrato-joven-sonriente-gafas_171337-4842.jpg?size=626&ext=jpg"));
//        misUser.add(new User("Adrain ","Herrera Garcia","mi horario","sobre mi Adrain","alcorocon",0,"https://www.ashoka.org/sites/default/files/styles/medium_1600x1000/public/thumbnails/images/daniela-kreimer.jpg?itok=R89tVtb4"));
//        misUser.add(new User("Iniesta","Perez","mi horario","sobre mi Iniesta","algete",0,"https://lamenteesmaravillosa.com/wp-content/uploads/2018/09/hombre-creido-pensando-que-sabe.jpg"));
//        misUser.add(new User("Pepe","Villuela","mi horario","sobre mi Pepe","sol",0,"https://www.trecebits.com/wp-content/uploads/2019/02/Persona-1-445x445.jpg"));
//        misUser.add(new User("Maria","Ferandez Ruiz","mi horario","sobre mi Maria","nuevos ministerios",1,"https://concepto.de/wp-content/uploads/2018/08/persona-e1533759204552.jpg"));
//
//
//        UserDatabaseProvider userData = new UserDatabaseProvider();
//        for(User i:misUser){
//            userData.createUser2(i);
//        }
//        new AuthenticationProvider().getFirebaseAuth().getCurrentUser().sendEmailVerification().addOnFailureListener(v-> Log.e(TAG, "onCreate: "+v.getMessage() ));
       // new AuthenticationProvider().getFirebaseAuth().getCurrentUser().updateEmail("cameracafetv98@gmail.com").addOnFailureListener(v-> Log.e(TAG, "onCreate: "+v.getMessage() ));

    }



    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.btnMenuWotkers:
                            openFragment((workersFragment ==null)? workersFragment =new WorkersFragment(): workersFragment);
                            getSupportActionBar().setTitle(getString(R.string.menuNameWorkers));
                            return true;
                            case R.id.btnMenuJobs:
                            openFragment((jobsFragment ==null)? jobsFragment =new JobsFragment(): jobsFragment);
                                getSupportActionBar().setTitle(getString(R.string.menuNameJobs));
                            return true;
                        case R.id.btnMenuChats:
                            openFragment((chatsFragment==null)?chatsFragment=new ChatsFragment():chatsFragment);
                            getSupportActionBar().setTitle(getString(R.string.menuNameChats));

                            return true;
                        case R.id.btnMenuProfile:
                            //startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
                            openFragment((profileFragment==null)?profileFragment=new ProfileFragment():profileFragment);
                            getSupportActionBar().setTitle(getString(R.string.menuNameProfile));

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