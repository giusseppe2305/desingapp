package com.optic.projectofinal.UI.activities.options_profile.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ActivityEditProfileBinding;
import com.optic.projectofinal.models.Sex;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.StorageProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {
    private final int PICKER_IMAGE_PROFILE_IMAGE = 1;
    private final int PICKER_IMAGE_COVER_IMAGE = 2;

    private static final String TAG = "own";
    private ActivityEditProfileBinding binding;
    private UserDatabaseProvider userDatabaseProvider;
    private AuthenticationProvider authenticationProvider;
    private StorageProvider storageProvider;
    private ArrayList<Sex> listSex;
    private Integer sexSelected;
    private Uri uriCoverImage;
    private Uri uriImageProfile;
    private AlertDialog dialogUpdate;

    private enum TYPE_IMAGE {COVER_IMAGE, PROFILE_IMAGE}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle("Editar perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //instance objects
        authenticationProvider = new AuthenticationProvider();
        userDatabaseProvider = new UserDatabaseProvider();
        storageProvider = new StorageProvider(this);
        sexSelected = null;
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
        buider.setTitleText("Selecciona una fecha");
        buider.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        MaterialDatePicker materialDatePicker = buider.build();


        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        ///
        dialogUpdate = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Actualizando informacion")
                .setCancelable(false).build();
        ///
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Date date = new Date();
                date.setTime(Long.parseLong(selection.toString()));
                String formatDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                binding.datePicker.setText(formatDate);
                if (EditProfileActivity.this.getCurrentFocus() != null)
                    EditProfileActivity.this.getCurrentFocus().clearFocus();
            }
        });
        //////
        binding.btnChangeCoverImage.setOnClickListener(v -> showDialogSelectImage(TYPE_IMAGE.COVER_IMAGE));
        binding.btnChangeImageProfile.setOnClickListener(v -> showDialogSelectImage(TYPE_IMAGE.PROFILE_IMAGE));
        ///

    }

    private void loadUserData() {
        userDatabaseProvider.getUser(authenticationProvider.getIdCurrentUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User userIterated=documentSnapshot.toObject(User.class);
                binding.name.getEditText().setText(userIterated.getName());
                binding.lastName.getEditText().setText(userIterated.getLastName());
                binding.about.getEditText().setText(userIterated.getAbout());
                binding.schedule.getEditText().setText(userIterated.getSchedule());
                Sex sex=Utils.getSexByIdJson(EditProfileActivity.this,userIterated.getSex());
                sexSelected=sex.getId();
                binding.sex.setText(sex.getTitleString(),false);
                binding.birthDate.getEditText().setText(Utils.getStringFromTimestamp(userIterated.getBirthdate()));
                binding.location.getEditText().setText(userIterated.getLocation());
                //images
                Glide.with(EditProfileActivity.this).load(userIterated.getCoverPageImage()).apply(Utils.getOptionsGlide(false)).transform(Utils.getTransformSquareRound()).into(binding.coverPageImage);
                Glide.with(EditProfileActivity.this).load(userIterated.getProfileImage()).apply(Utils.getOptionsGlide(false)).into(binding.imageProfile);
            }
        }).addOnFailureListener(v-> Log.e(TAG, "loadUserData: "+v.getMessage() ));
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
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER_IMAGE_COVER_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {

                //Image Uri will not be null for RESULT_OK
                Uri fileUri = data.getData();
                uriCoverImage = fileUri;
                Glide.with(this).load(uriCoverImage).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(binding.coverPageImage);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICKER_IMAGE_PROFILE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                Uri fileUri = data.getData();
                uriImageProfile = fileUri;
                Glide.with(this).load(uriImageProfile).apply(Utils.getOptionsGlide(true)).into(binding.imageProfile);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialogSelectImage(TYPE_IMAGE type) {
        new MaterialAlertDialogBuilder(this).
                setTitle("Elige una opcion")
                .setItems(new String[]{"Elegir de la galeria", "Tomar FOTO AHORA"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditProfileActivity.this, "Cancelaste la opcion de elegir donde sacar imagen", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(false).show();
    }

    private void updateDataUser() {
        if (checkFieldAreValid()) {
            User userUpdate = new User();
            userUpdate.setName(binding.name.getEditText().getText().toString());
            userUpdate.setLastName(binding.lastName.getEditText().getText().toString());
            userUpdate.setAbout(binding.about.getEditText().getText().toString());
            userUpdate.setSchedule(binding.schedule.getEditText().getText().toString());
            if (sexSelected != null)
                userUpdate.setSex(sexSelected);
            userUpdate.setBirthdate(Utils.getTimestampFromString(binding.birthDate.getEditText().getText().toString()));
            userUpdate.setLocation(binding.location.getEditText().getText().toString());

            userDatabaseProvider.updateUser(authenticationProvider.getIdCurrentUser(), userUpdate)
                    .addOnSuccessListener(n -> Toast.makeText(this, "Todo update ", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(v -> {
                        Log.e(TAG, "updateDataUser: " + v.getMessage());
                        Toast.makeText(this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                    });
            if (uriCoverImage != null) {
                storageProvider.uploadImageUser(uriCoverImage, StorageProvider.TYPE_IMAGE.COVER_IMAGE).addOnFailureListener(v -> Log.e(TAG, "fail update cover image prifle " + v.getMessage()));
            }
            if (uriImageProfile != null) {
                storageProvider.uploadImageUser(uriImageProfile, StorageProvider.TYPE_IMAGE.PROFILE_IMAGE).addOnFailureListener(v -> Log.e(TAG, "fail update  image prifle " + v.getMessage()));
            }
            //check all right
            finish();
        } else {
            Toast.makeText(this, "Campos invalidos", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkFieldAreValid() {
        boolean ret = false;
        boolean ret2 = false;
        boolean ret3 = true;
        boolean ret4 = true;
        if (binding.name.getEditText().getText().toString().length() == 0) {
            binding.name.setError("Introduza un nombre");
        } else if (binding.name.getEditText().getText().toString().length() >= 20) {
            binding.name.setError("Nombre demasiado largo");
        } else {
            binding.name.setError(null);
            ret = true;
        }
        if (binding.lastName.getEditText().getText().toString().length() == 0) {
            binding.lastName.setError("Introduza un apellido");
        } else if (binding.lastName.getEditText().getText().toString().length() >= 40) {
            binding.lastName.setError("Apellido demasiado largo");
        } else {
            binding.lastName.setError(null);
            ret2 = true;
        }

        if (binding.about.getEditText().getText().toString().length() >= 240) {
            binding.about.setError("Apellido demasiado largo");
            ret3 = false;
        } else {
            binding.about.setError(null);
        }

        if (binding.schedule.getEditText().getText().toString().length() >= 240) {
            binding.schedule.setError("Apellido demasiado largo");
            ret4 = false;
        } else {
            binding.schedule.setError(null);
        }
        System.out.println("errores " + ret + ret2 + ret3 + ret4);
        return ret && ret2 && ret3 && ret4;

    }

}