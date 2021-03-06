package com.optic.projectofinal.UI.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.fragments.ChatsFragment;
import com.optic.projectofinal.UI.activities.fragments.JobsFragment;
import com.optic.projectofinal.UI.activities.fragments.ProfileFragment;
import com.optic.projectofinal.UI.activities.fragments.WorkersFragment;
import com.optic.projectofinal.UI.activities.options_profile.settings.EditSettingsWorkerActivity;
import com.optic.projectofinal.databinding.ActivityMainBinding;
import com.optic.projectofinal.providers.TokenProvider;

import java.util.Objects;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WorkersFragment workersFragment;
    private JobsFragment jobsFragment;
    private ChatsFragment chatsFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ///In order to have an previous activity,, we receive an id and from here we redirected to new activity,so when the user press back button dont close de app and come to the main activity
        if (getIntent().getStringExtra("idUserToSee") != null) {
            Intent intent = new Intent(this, ProfileDetailsActivity.class);
            intent.putExtra("idUserToSee", getIntent().getStringExtra("idUserToSee"));
            startActivity(intent);
        }
        if(getIntent().getBooleanExtra("isProfessional",false)){
            startActivity(new Intent(this, EditSettingsWorkerActivity.class));
        }

        //set binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //toolbar
        setSupportActionBar(binding.toolbar.ownToolbar);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);



        BadgeDrawable bg = binding.bottomNavigation.getOrCreateBadge(R.id.btnMenuChats);
        bg.setBackgroundColor(Color.RED);
        bg.setVisible(true);
        bg.setNumber(13);
        ///open first fragment
        openFragment((workersFragment == null) ? workersFragment = new WorkersFragment() : workersFragment);
        getSupportActionBar().setTitle(getString(R.string.menuNameWorkers));

        createToken();

//        String[] names={"Jan",
//                "Jose Joaquin",
//                "Martin",
//                "Luis Carlos",
//                "Paulino",
//                "Anas",
//                "Gael",
//                "Alonso",
//                "David",
//                "Jose David",
//                "Emilio",
//                "Holgado",
//                "Jose Fernando",
//                "Hipolito",
//                "Marti",
//                "Eneko",
//                "Adan",
//                "Jesus Antonio",
//                "Ali",
//                "Ismail",
//                "Nestor",
//                "Enrique",
//                "Carlos",
//                "Ramiro",
//                "Jose Daniel",
//                "Evaristo",
//                "Jan",
//                "Marco",
//                "Aleix",
//                "Eugenio",
//                "Jose Ramon",
//                "Jose Javier",
//                "Guillem",
//                "Pedro",
//                "Francisco",
//                "Josu",};
//        String[] lastnames={"Badia",
//        "Esteve",
//        "de Los Santos",
//        "Wu",
//        "Rojo",
//        "Belda",
//        "Miguel",
//        "Contreras",
//        "Quesada",
//        "Novoa",
//        "Portillo",
//        "Quesada",
//        "Aguilera",
//        "Casado",
//        "Sanchis",
//        "Gago",
//        "Aroca",
//        "Sala",
//        "Dominguez",
//        "Casal",
//        "Enrique Monzon",
//        "Galan",
//        "Puerto",
//        "Agudo",
//        "Clemente",
//        "Oliveira",
//        "Zhang",
//        "Marrero",
//        "Borja",
//        "Pulido",
//        "Vergara",
//        "del Pino",
//        "Crespo",
//        "San-Jose",
//        "Amaya",
//        "Castillo",
//        };
//
//        long[] times={1609238824000L,
//                1609152424000L,
//                1609066024000L,
//                1608979624000L,
//                1608893224000L,
//                1608806824000L,
//                1608720424000L,
//                1608634024000L,
//                1609152424000L,
//                1609152424000L,
//                1609243140000L,
//                1608897547000L,
//                1608922747000L,
//                1609095547000L,
//                1609092907000L,
//                1609179307000L,
//                1608660907000L,
//                1608632107000L,
//                1608603307000L,
//                1608171307000L,
//                1608243307000L,
//                1608149707000L,
//                1608236107000L,
//                1608379264000L,
//                1609138864000L,
//                1609243287000L,
//                1609243293000L,
//                1609243356000L,
//                1609156956000L,
//                1609070556000L,
//                1609243384000L,
//                1609243393000L,
//                1609228993000L,
//                1609243405000L,
//                1609239805000L,
//                1609243405000L
//        };
//        String[] photo={"https://concepto.de/wp-content/uploads/2018/08/persona-e1533759204552.jpg",
//                "https://www.ashoka.org/sites/default/files/styles/medium_1600x1000/public/thumbnails/images/daniela-kreimer.jpg?itok=R89tVtb4",
//                "https://lamenteesmaravillosa.com/wp-content/uploads/2018/09/hombre-creido-pensando-que-sabe.jpg"
//        ,"https://cdnb.20m.es/sites/112/2019/04/cara6-620x618.jpg"
//        ,"https://static2.abc.es/media/bienestar/2019/10/01/personas-magneticas-k02H--1200x630@abc.jpg"
//        ,"https://img.freepik.com/foto-gratis/retrato-joven-sonriente-gafas_171337-4842.jpg?size=626&ext=jpg"
//        ,"https://pymstatic.com/5028/conversions/personas-genuinas-thumb.jpg"
//        ,"https://www.contrareplica.mx/uploads/galerias/normal/d51adde6198cd2e5a32ddda82d2ee9b6.jpg"
//        ,"https://assets.entrepreneur.com/content/3x2/2000/20181012160100-atractiva.jpeg?width=700&crop=2:1"
//        ,"https://e00-elmundo.uecdn.es/assets/multimedia/imagenes/2019/11/14/15737278746181.jpg"
//        ,"https://pymstatic.com/5028/conversions/personas-genuinas-thumb.jpg"
//        ,"https://www.decideo.com/photo/art/default/42090343-35199053.jpg?v=1579807427"
//        ,"https://img.huffingtonpost.com/asset/5c8a98102400006f054c0b94.jpeg?ops=scalefit_630_noupscale"
//        ,"https://www.xlsemanal.com/wp-content/uploads/sites/3/2019/12/edward-norton-no-te-puedes-fiar-de-las-personas-que-fueron-felices-cuanto-tenian-14-anso-1366x768.jpg"
//        ,"https://estaticos.muyinteresante.es/media/cache/760x570_thumb/uploads/images/article/55365cde3787b2187a1f0fbc/impresion-cara.jpg"
//        ,"https://www.pactomundial.org/wp-content/uploads/2019/12/Tribuna-DKV.jpg"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSECZABdj8GRifBnxBh_Io5PK5T_nTcYS4yZw&usqp=CAU"
//        ,"https://pymstatic.com/30196/conversions/personas-susceptibles-social.jpg"
//        ,"https://mk0lanoticiavesdar5g.kinstacdn.com/wp-content/uploads/2020/06/12-cualidades-de-las-personas-inteligentes.jpg"
//        ,"https://selecciones.com.mx/wp-content/uploads/2019/06/que-hace-inteligentes-a-las-personas.jpg"
//        ,"https://www.cancer.net/sites/cancer.net/files/styles/blog_media/public/markham_merry-jennifer_headshot.jpg.png?itok=GnzqyjcQ"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRumXy60SJVLtfeAjroZHXcuW1ywhdWpWpzlw&usqp=CAU"
//        ,"https://aws.glamour.mx/prod/designs/v1/assets/620x788/186096.jpg"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXN1_znAMM_0_oHVZDIYEnC5gia-gX6xgihg&usqp=CAU"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSynd5Ve0wlgyxKe0ZmRxSxPuWi533Ddctsg&usqp=CAU"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdrPaLw3a8bVzFJczIhU3OBQWNJ_rrOYARwg&usqp=CAU"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-AxVBAfzfgFrnqum4s3E8ORcSAeAU4XpYdg&usqp=CAU"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR4NcbXoYdtv8VgPlKbxl6lnD5AchZlkARJ5A&usqp=CAU"
//        ,"https://www.redaccionmedica.com/images/destacados/las-personas-con-un-riesgo-genetico-bajo-de-tdah-son-mas-afortunadas--2868.jpg"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGGf-4-jszpgMNsMztOCu92ZwqjUrOCssiOw&usqp=CAU"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIhJnQbE88bpupI2Nm6rOgAF3JPbmi_bElBQ&usqp=CAU"
//        ,"https://www.trecebits.com/wp-content/uploads/2019/02/Persona-1-445x445.jpg"
//        ,"https://www.gravatar.com/avatar/24acd98e5d71a6a789488b4c049de9fe?s=200"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR0iAxGNQ5h9k2Pr1J_iUKEZz0fbK0KRQIytw&usqp=CAU"
//        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQRl5MRBjEoUriQOZffpjWCGvsCC8Yi8n0PTA&usqp=CAU"
//        ,"https://persona-app-es.herokuapp.com/assets/personas/1-e0e7223aeb2f66a659d032fb393d11d7e62e00706b1bef5f9f1a13eaa93d4b4b.png"
//        };
//        for(int i=0;i<names.length;i++)
//        {
//            DocumentReference users = FirebaseFirestore.getInstance().collection("Users").document();
//            User user1=new User();
//            user1.setId(users.getId());
//            user1.setName(names[i]);
//            user1.setLastName(lastnames[i]);
//            user1.setThumbnail(photo[i]);
//            user1.setProfileImage(photo[i]);
//            user1.setCoverPageImage("https://fundspeople-repository.s3.amazonaws.com/system/picture/file/211455/07ff9dc0f0bf87a7.jpg");
//            user1.setOnline(false);
//            user1.setAbout("Sobre mi "+user1.getName()+" "+user1.getLastName());
//            user1.setBirthdate(times[i]);
//            user1.setTimestamp(times[i]);
//            user1.setLastConnection(times[i]);
//            user1.setProfessional(true);
//            user1.setVerified(true);
//            user1.setPhoneNumber(657560689);
//            user1.setResources(new ArrayList<>(Arrays.asList(new Integer[]{1,2,4})));
//            users.set(user1);
//        }


        //new StorageProvider(this).createThumbnail("prueba", Uri.parse("https://firebasestorage.googleapis.com/v0/b/projecto-final-b5adb.appspot.com/o/jobs_photos%2FNqCsoz2vJeKAwVAHrPVv%2FSun%20Dec%2020%2015%3A27%3A14%20GMT%2B01%3A00%202020IMG_20201220_152702188.jpg?alt=media&token=b2b7c8fa-0e2a-47a7-a63a-0137891f0ddb"),"all_jobs","prueba");

       // Log.d(TAG_LOG, "onCreate: "+acct.getPhotoUrl().toString());

        //Utils.downloadFile("https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=3408049219249663&width=500&ext=1611006682&hash=AeT3HtJphqZV0K5-NL8");
        //Utils.download(this,"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=3408049219249663&width=500&ext=1611006682&hash=AeT3HtJphqZV0K5-NL8");
//        NotificationMessageDTO notificationDTO=new NotificationMessageDTO("titulo", NotificationHelper.TYPE_NOTIFICATION.MESSAGE_CHAT.toString(),"cuerpo", idChat);
//        PodamFactoryImpl podamFactory=new PodamFactoryImpl();
//        Message[] messages=new Message[3];
//        for(int i=0;i<3;i++){
//            messages[i]=podamFactory.manufacturePojo(Message.class);
//        }
//
//
//        notificationDTO.setMessages(messages);
//        notificationDTO.setNameUser("Frank");
//        notificationDTO.setPhotoProfile("https://image.shutterstock.com/image-photo/bright-spring-view-cameo-island-260nw-1048185397.jpg");
//        String result=new Gson().toJson(notificationDTO);
//        Log.d(TAG_LOG, "onCreate:aaa "+result);
//        Log.d(TAG_LOG, "onCreate:aaa "+ Utils.getOptionNotificationFromJSON(result));
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
//        new AuthenticationProvider().getFirebaseAuth().getCurrentUser().sendEmailVerification().addOnFailureListener(v-> Log.e(TAG_LOG, "onCreate: "+v.getMessage() ));
        // new AuthenticationProvider().getFirebaseAuth().getCurrentUser().updateEmail("cameracafetv98@gmail.com").addOnFailureListener(v-> Log.e(TAG_LOG, "onCreate: "+v.getMessage() ));

//        for(int i=0;i<12;i++){
//            Opinion opinion=new Opinion();
//            opinion.setMessage("mesaje "+i);
//            opinion.setTitleJob("titulo "+i);
//            opinion.setAverageValuation(i);
//            opinion.setImageJob("data:image/jpeg;base64,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uzzJMQLgu21j91OeI95YfEVQilWIYyyifKKIpFdpVYcikV2lByKRXaUHIpFdpQcikV2lApSlApSlApSlApSlApSlApSlArlXuKaNbfKKk+baS6VYyylpBkwBBjId9mE71SoL3G9Etm+9tSYXEwx61yQMUcwAWWYMCNqpKxBkEg/IMGuE/dKDmI+PqkV2lBLptS9s5W3e2xESlwo0bbSsbbD/AEKgxHx+/b3+a9UoOAV2lKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKBSlKD6RuD2hpxdg5MrmC57pftpkI/SQ5EEncH9qzvCp8fZq5YclbgJOyADfsOahgftJmoK0I206nciT+7GdhA+hXPCp8fZqWlBF4VPj7NPCp8fZqWlUReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQReFT4+zTwqfH2alpQf/Z");
//            opinion.setTimestamp(new Date().getTime());
//            new UserDatabaseProvider().putOpinion(new AuthenticationProvider().getIdCurrentUser(),opinion).addOnFailureListener(v-> Log.e(TAG_LOG, "onCreate: "+v.getMessage() ));
//        }

//        FirebaseFirestore.getInstance().collection("Users").document("0cXIRZ8eYrIrjcLG4p9h").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                documentSnapshot.getDocumentReference("ref").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        System.out.println("nmbre "+documentSnapshot.getString("message"));
//                    }
//                });
//            }
//        });


    }
    static class categ{
        String name;
        int idCategory;
        String id;
        public categ(String name, int idCategory, String id) {
            this.name = name;
            this.idCategory = idCategory;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIdCategory() {
            return idCategory;
        }

        public void setIdCategory(int idCategory) {
            this.idCategory = idCategory;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    private void createToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        //Could not get FirebaseMessagingToken
                        return;
                    }
                    if (null != task.getResult()) {
                        //Got FirebaseMessagingToken
                        String firebaseMessagingToken = Objects.requireNonNull(task.getResult());
                        Log.d(TAG_LOG, "onCreate: " + firebaseMessagingToken);
                        new TokenProvider().create(firebaseMessagingToken);
                        //Use firebaseMessagingToken further
                    }
                });
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