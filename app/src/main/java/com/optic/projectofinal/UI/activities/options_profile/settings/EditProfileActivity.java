package com.optic.projectofinal.UI.activities.options_profile.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ActivityEditProfileBinding;
import com.optic.projectofinal.models.BasicInformationUser;
import com.optic.projectofinal.models.Sex;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.StorageProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class EditProfileActivity extends AppCompatActivity {
    private final int PICKER_IMAGE_PROFILE_IMAGE = 1;
    private final int PICKER_IMAGE_COVER_IMAGE = 2;


    private ActivityEditProfileBinding binding;
    private UserDatabaseProvider userDatabaseProvider;
    private AuthenticationProvider authenticationProvider;
    private StorageProvider storageProvider;
    private ArrayList<Sex> listSex;
    private Integer sexSelected;
    private Uri uriCoverImage;
    private Uri uriImageProfile;
    private AlertDialog dialogUpdate;
    private boolean hasProfileImage;
    private boolean hasCoverImage;
    private String urlImageProfileThumbnail;

    private enum TYPE_IMAGE {COVER_IMAGE, PROFILE_IMAGE}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(R.string.edit_profile_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //instance objects
        authenticationProvider = new AuthenticationProvider();
        userDatabaseProvider = new UserDatabaseProvider();
        storageProvider = new StorageProvider(this);
        sexSelected = null;
        hasCoverImage=false;
        hasProfileImage=false;
        ///
        listSex = Utils.getListSexJson(this);
        binding.sex.setAdapter(new ArrayAdapter<Sex>(this, R.layout.textbox_gender, listSex));
        binding.sex.setOnItemClickListener((adapterView, view, i, l) -> {
            System.out.println("sex selected " + i);
            sexSelected = listSex.get(i).getId();
        });
        ///load data
        loadUserData();


        MaterialDatePicker.Builder buider = MaterialDatePicker.Builder.datePicker();
        buider.setTitleText(R.string.edit_profile_select_date);
        buider.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        MaterialDatePicker materialDatePicker = buider.build();


        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        ///

        ///
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            Date date = new Date();
            date.setTime(Long.parseLong(selection.toString()));
            String formatDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            binding.datePicker.setText(formatDate);
            if (EditProfileActivity.this.getCurrentFocus() != null)
                EditProfileActivity.this.getCurrentFocus().clearFocus();
        });
        //////
        binding.btnChangeCoverImage.setOnClickListener(v -> showDialogSelectImage(TYPE_IMAGE.COVER_IMAGE));
        binding.btnChangeImageProfile.setOnClickListener(v -> showDialogSelectImage(TYPE_IMAGE.PROFILE_IMAGE));
        ///

    }

    private void loadUserData() {
        userDatabaseProvider.getUser(authenticationProvider.getIdCurrentUser()).addOnSuccessListener(documentSnapshot -> {
            User userIterated=documentSnapshot.toObject(User.class);
            binding.name.getEditText().setText(userIterated.getName());
            binding.lastName.getEditText().setText(userIterated.getLastName());
            binding.about.getEditText().setText(userIterated.getAbout());
            ///save thumbnail
            urlImageProfileThumbnail =userIterated.getProfileImage();

            Sex sex=Utils.getSexByIdJson(EditProfileActivity.this,userIterated.getSex());
            sexSelected=sex.getId();
            binding.sex.setText(sex.getTitleString(),false);
            if(userIterated.getBirthdate()!=null)
            binding.birthDate.getEditText().setText(Utils.getStringFromTimestamp(userIterated.getBirthdate()));
            binding.location.getEditText().setText(userIterated.getLocation());
            //images
            if(userIterated.getCoverPageImage()!=null&&!userIterated.getCoverPageImage().isEmpty()){
                hasCoverImage=true;
            }
            if(userIterated.getProfileImage()!=null&&!userIterated.getProfileImage().isEmpty()){
                hasProfileImage=true;
            }
            Glide.with(EditProfileActivity.this).load(userIterated.getCoverPageImage()).apply(Utils.getOptionsGlide(false)).transform(Utils.getTransformSquareRound()).into(binding.coverPageImage);
            Glide.with(EditProfileActivity.this).load(userIterated.getProfileImage()).apply(Utils.getOptionsGlide(false)).into(binding.imageProfile);
        }).addOnFailureListener(v-> Log.e(TAG_LOG, "loadUserData: "+v.getMessage() ));
    }
    private void createThumbnail(String image,String idUser) {
        new Thread(() -> {
            try {
                URL url = new URL(image);
                Bitmap imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                storageProvider.createThumbnail(idUser,imageBitmap,"imagesUsers",idUser).addOnSuccessListener(taskSnapshot ->
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                            User update=new User();
                            update.setId(idUser);
                            update.setThumbnail(uri.toString());
                            userDatabaseProvider.updateUser(update).addOnFailureListener(e-> Log.e(TAG_LOG, "fail to save thumbnail job "+e.getMessage() ));
                        }).addOnFailureListener(e -> Log.d(TAG_LOG, "fail get url thumbnail  "+e.getMessage())));

            } catch (MalformedURLException e) {
                Log.e(TAG_LOG, "createThumbnail: "+e.getMessage() );
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG_LOG, "createThumbnail: "+e.getMessage() );
                e.printStackTrace();
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnSave) {
            System.out.println(binding.birthDate.getEditText().getText().toString());
            updateDataUser();
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER_IMAGE_COVER_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                uriCoverImage = data.getData();
                Glide.with(this).load(uriCoverImage).apply(Utils.getOptionsGlide(false)).transform(Utils.getTransformSquareRound()).into(binding.coverPageImage);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICKER_IMAGE_PROFILE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                uriImageProfile = data.getData();
                Glide.with(this).load(uriImageProfile).apply(Utils.getOptionsGlide(false)).into(binding.imageProfile);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Log.e(TAG_LOG, "fail on get select image "+ ImagePicker.Companion.getError(data) );
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateSharePreference(User user) {
        BasicInformationUser basicInformationUser = new BasicInformationUser();
        basicInformationUser.setName(user.getName());
        basicInformationUser.setLastName(user.getLastName());
        if(uriImageProfile!=null)
            basicInformationUser.setPhotoUser(uriImageProfile.toString());
        else
            basicInformationUser.setPhotoUser("nonPhoto");
        Utils.setPersistantBasicUserInformation(basicInformationUser, this);
//        Utils.setLanguage("es-Es", this);
    }

    private void showDialogSelectImage(TYPE_IMAGE type) {
        new MaterialAlertDialogBuilder(this).
                setTitle(R.string.edit_profile_dialog_select_image_from_tittle)
                .setItems(new String[]{getString(R.string.edit_profile_dialog_select_image_from_option1),
                        getString(R.string.edit_profile_dialog_select_image_from_option2)}, (dialogInterface, i) -> {
                            ImagePicker.Builder show = ImagePicker.Companion.with(EditProfileActivity.this)
                                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                    .galleryMimeTypes(new String[]{"image/png", "image/jpeg", "image/jpg"});
                            if (i == 0) {
                                show.galleryOnly();

                            } else {
                                show.cameraOnly();
                            }
                            if (type == TYPE_IMAGE.COVER_IMAGE) {
                                show.crop(16f, 9f);
                                show.start(PICKER_IMAGE_COVER_IMAGE);
                            } else {
                                show.crop();
                                show.start(PICKER_IMAGE_PROFILE_IMAGE);
                            }
                        })
                .setNegativeButton(R.string.edit_profile_dialog_select_image_from_negative_button, (dialogInterface, i) -> Log.d(TAG_LOG, "the user cancel dialog where you selec option gallery or camera")).setCancelable(false).show();
    }

    private void updateDataUser() {
        if (checkFieldAreValid()) {
            User userUpdate = new User();
            userUpdate.setId(authenticationProvider.getIdCurrentUser());
            userUpdate.setName(binding.name.getEditText().getText().toString());
            userUpdate.setLastName(binding.lastName.getEditText().getText().toString());
            userUpdate.setAbout(binding.about.getEditText().getText().toString());
            if (sexSelected != null)
                userUpdate.setSex(sexSelected);
            userUpdate.setBirthdate(Utils.getTimestampFromString(binding.birthDate.getEditText().getText().toString()));
            userUpdate.setLocation(binding.location.getEditText().getText().toString());

            updateSharePreference(userUpdate);

            if (uriCoverImage != null) {
                Log.d(TAG_LOG, "updateDataUser: has uriCoverImage");
                storageProvider.uploadImageUser(uriCoverImage, StorageProvider.TYPE_IMAGE.COVER_IMAGE).addOnSuccessListener(v->{
                    Log.d(TAG_LOG, "updateDataUser: change cover image "+hasCoverImage);
                    if(!hasCoverImage){
                        v.getStorage().getDownloadUrl().addOnSuccessListener(c->{
                            User mUser=new User();
                            mUser.setId(authenticationProvider.getIdCurrentUser());
                            mUser.setCoverPageImage(c.toString());
                            userDatabaseProvider.updateUser(mUser);
                        }).addOnFailureListener(cc-> Log.e(TAG_LOG, "updateDataUser: cover "+cc.getMessage() ));

                    }
                }).addOnFailureListener(v -> Log.e(TAG_LOG, "fail update cover image prifle " + v.getMessage()));
            }
            if (uriImageProfile != null) {
                Log.d(TAG_LOG, "updateDataUser: has image profile");
                storageProvider.uploadImageUser(uriImageProfile, StorageProvider.TYPE_IMAGE.PROFILE_IMAGE)
                        .addOnSuccessListener(v-> {
                            Log.d(TAG_LOG, "updateDataUser: cambio profile image");
                            if (!hasProfileImage) {
                                v.getStorage().getDownloadUrl().addOnSuccessListener(c -> {
                                    User mUser = new User();
                                    mUser.setId(authenticationProvider.getIdCurrentUser());
                                    mUser.setProfileImage(c.toString());
                                    userDatabaseProvider.updateUser(mUser);


                                }).addOnFailureListener(cc -> Log.e(TAG_LOG, "updateDataUser: profile " + cc.getMessage()));

                            }
                            //create thumbnail
                            Log.d(TAG_LOG, "updateDataUser: create thumbnail "+urlImageProfileThumbnail+"- "+authenticationProvider.getIdCurrentUser());
                            createThumbnail(urlImageProfileThumbnail,authenticationProvider.getIdCurrentUser());

                        })
                        .addOnFailureListener(v -> Log.e(TAG_LOG, "fail update  image prifle " + v.getMessage()));
            }

            userDatabaseProvider.updateUser(userUpdate)
                    .addOnSuccessListener(n -> Toast.makeText(this, "Todo update ", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(v -> Log.e(TAG_LOG, "updateDataUser: " + v.getMessage()));
            //check all right
            finish();
        } else {
            Toast.makeText(this, R.string.edit_profile_fields_are_invalid, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkFieldAreValid() {
        boolean ret = false;
        boolean ret2 = false;
        boolean ret3 = true;
        boolean ret4 = true;
        if (binding.name.getEditText().getText().toString().length() == 0) {
            binding.name.setError(getString(R.string.pattern_name_void_field));
        } else if (binding.name.getEditText().getText().toString().length() >= 20) {
            binding.name.setError(getString(R.string.pattern_name_correct_length));
        } else {
            binding.name.setError(null);
            ret = true;
        }
        if (binding.lastName.getEditText().getText().toString().length() == 0) {
            binding.lastName.setError(getString(R.string.pattern_last_name_void_field));
        } else if (binding.lastName.getEditText().getText().toString().length() >= 40) {
            binding.lastName.setError(getString(R.string.pattern_last_name_correct_length));
        } else {
            binding.lastName.setError(null);
            ret2 = true;
        }

        if (binding.about.getEditText().getText().toString().length() >= 240) {
            binding.about.setError(getString(R.string.pattern_about_correct_length));
            ret3 = false;
        } else {
            binding.about.setError(null);
        }

        if (binding.schedule.getEditText().getText().toString().length() >= 240) {
            binding.schedule.setError(getString(R.string.pattern_schedule_correct_length));
            ret4 = false;
        } else {
            binding.schedule.setError(null);
        }
        return ret && ret2 && ret3 && ret4;

    }

}