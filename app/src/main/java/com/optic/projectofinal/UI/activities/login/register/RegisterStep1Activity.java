package com.optic.projectofinal.UI.activities.login.register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.MainActivity;
import com.optic.projectofinal.databinding.ActivityRegisterStep1Binding;
import com.optic.projectofinal.models.BasicInformationUser;
import com.optic.projectofinal.models.Sex;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.StorageProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.optic.projectofinal.utils.Utils.REGISTER.FACEBOOK;
import static com.optic.projectofinal.utils.Utils.TAG_LOG;
import static com.optic.projectofinal.utils.Utils.updateImageProfileBasicUserInformation;

public class RegisterStep1Activity extends AppCompatActivity {
    private String urlImageFacebook;
    private String urlImageGoogle;

    private enum TYPE_IMAGE {COVER_IMAGE, PROFILE_IMAGE}

    private final int PICKER_IMAGE_PROFILE_IMAGE = 1;
    private final int PICKER_IMAGE_COVER_IMAGE = 2;
    private Integer sexSelected;
    private Uri uriCoverImage;
    private Uri uriImageProfile;
    private ArrayList<Sex> listSex;
    private String idUser;
    private Utils.REGISTER optionRegister;
    private ActivityRegisterStep1Binding binding;
    private UserDatabaseProvider userDatabaseProvider;
    private AuthenticationProvider authenticationProvider;
    private StorageProvider storageProvider;
    private String permissionsFacebookRequest = "id,birthday,gender,first_name,middle_name,last_name,email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterStep1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authenticationProvider = new AuthenticationProvider();
        userDatabaseProvider = new UserDatabaseProvider();
        storageProvider = new StorageProvider(this);
        idUser = getIntent().getStringExtra("idUser");
        optionRegister = (Utils.REGISTER) getIntent().getSerializableExtra("optionRegister");
        Log.d(TAG_LOG, "onCreate: " + optionRegister);
        sexSelected = null;


        MaterialDatePicker.Builder buider = MaterialDatePicker.Builder.datePicker();
        buider.setTitleText(R.string.edit_profile_select_date);
        buider.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        MaterialDatePicker materialDatePicker = buider.build();
        binding.datePicker.setOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER"));
        ///

        listSex = Utils.getListSexJson(this);
        binding.sex.setAdapter(new ArrayAdapter<Sex>(this, R.layout.textbox_gender, listSex));
        binding.sex.setOnItemClickListener((adapterView, view, i, l) -> {
            sexSelected = listSex.get(i).getId();
        });
        ///
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            Date date = new Date();
            date.setTime(Long.parseLong(selection.toString()));
            String formatDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            binding.datePicker.setText(formatDate);
            if (RegisterStep1Activity.this.getCurrentFocus() != null)
                RegisterStep1Activity.this.getCurrentFocus().clearFocus();
        });
        //////
        binding.btnChangeCoverImage.setOnClickListener(v -> showDialogSelectImage(TYPE_IMAGE.COVER_IMAGE));
        binding.btnChangeImageProfile.setOnClickListener(v -> showDialogSelectImage(TYPE_IMAGE.PROFILE_IMAGE));
        ///
        binding.next.setOnClickListener(click -> {
            Toast.makeText(this, checkFieldAreValid() + "", Toast.LENGTH_SHORT).show();
            updateDataUser();
        });

        loadData();
    }

    private void updateDataUser() {
        if (checkFieldAreValid()) {
            if (optionRegister == FACEBOOK && urlImageFacebook == null) {
                Toast.makeText(this, "Ha ocurrido un error vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                return;
            }
            User userUpdate = new User();
            userUpdate.setOnline(true);
            userUpdate.setId(idUser);
            userUpdate.setName(binding.name.getEditText().getText().toString());
            userUpdate.setLastName(binding.lastName.getEditText().getText().toString());
            userUpdate.setSex(sexSelected);
            userUpdate.setBirthdate(Utils.getTimestampFromString(binding.birthdate.getEditText().getText().toString()));

            if (uriCoverImage != null) {

                storageProvider.uploadImageUser(uriCoverImage, StorageProvider.TYPE_IMAGE.COVER_IMAGE).addOnSuccessListener(v -> {


                    v.getStorage().getDownloadUrl().addOnSuccessListener(c -> {
                        User mUser = new User();
                        mUser.setId(authenticationProvider.getIdCurrentUser());
                        mUser.setCoverPageImage(c.toString());
                        userDatabaseProvider.updateUser(mUser);
                    }).addOnFailureListener(cc -> Log.e(TAG_LOG, "updateDataUser: cover " + cc.getMessage()));


                }).addOnFailureListener(v -> Log.e(TAG_LOG, "fail update cover image prifle " + v.getMessage()));
            }
            if (uriImageProfile != null) {
                storageProvider.uploadImageUser(uriImageProfile, StorageProvider.TYPE_IMAGE.PROFILE_IMAGE)
                        .addOnSuccessListener(v -> {
                            Log.e(TAG_LOG, "updateDataUser: cambio profile image");
                            v.getStorage().getDownloadUrl().addOnSuccessListener(c -> {
                                User mUser = new User();
                                mUser.setId(authenticationProvider.getIdCurrentUser());
                                mUser.setProfileImage(c.toString());
                                userDatabaseProvider.updateUser(mUser);
                                //create thumbnail
                                createThumbnail(c.toString(), mUser.getId());
                            }).addOnFailureListener(cc -> Log.e(TAG_LOG, "updateDataUser: profile " + cc.getMessage()));

                        })
                        .addOnFailureListener(v -> Log.e(TAG_LOG, "fail update  image prifle " + v.getMessage()));
            } else if (uriImageProfile == null && (urlImageFacebook != null || urlImageGoogle != null)) {
                Utils.downloadFile(urlImageFacebook != null ? urlImageFacebook : urlImageGoogle, file ->
                        storageProvider.uploadImageUser(file, StorageProvider.TYPE_IMAGE.PROFILE_IMAGE).addOnSuccessListener(taskSnapshot -> {
                    Log.e(TAG_LOG, "updateDataUser: cambio profile image facebook");
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(c -> {
                        User mUser = new User();
                        mUser.setId(authenticationProvider.getIdCurrentUser());
                        mUser.setProfileImage(c.toString());
                        userDatabaseProvider.updateUser(mUser);
                        //create thumbnail
                        createThumbnail(c.toString(), mUser.getId());
                    }).addOnFailureListener(cc -> Log.e(TAG_LOG, "updateDataUser: profile facebook" + cc.getMessage()));
                }).addOnFailureListener(e -> Log.e(TAG_LOG, "updateDataUser: fail to upload phtoto profile from facebook")));
            }

            userDatabaseProvider.updateUser(userUpdate)
                    .addOnSuccessListener(n -> Toast.makeText(this, "Todo update ", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(v -> Log.e(TAG_LOG, "updateDataUser: " + v.getMessage()));

            saveSharePreference(userUpdate);

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("isProfessional", binding.isProfessional.isChecked());
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            Toast.makeText(this, R.string.edit_profile_fields_are_invalid, Toast.LENGTH_SHORT).show();
        }

    }

    private void createThumbnail(String image, String idUser) {
        new Thread(() -> {
            try {
                URL url = new URL(image);
                Bitmap imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                storageProvider.createThumbnail(idUser, imageBitmap, "all_jobs_thumbnail", idUser).addOnSuccessListener(taskSnapshot ->
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                            User update = new User();
                            update.setId(idUser);
                            update.setThumbnail(uri.toString());
                            userDatabaseProvider.updateUser(update).addOnFailureListener(e -> Log.e(TAG_LOG, "fail to save thumbnail user " + e.getMessage()));
                            updateImageProfileBasicUserInformation(uri.toString(), RegisterStep1Activity.this);

                        }).addOnFailureListener(e -> Log.d(TAG_LOG, "fail get url thumbnail  " + e.getMessage())));

            } catch (IOException e) {
                Log.e(TAG_LOG, "createThumbnail: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void saveSharePreference(User user) {
        BasicInformationUser basicInformationUser = new BasicInformationUser();
        basicInformationUser.setName(user.getName());
        basicInformationUser.setLastName(user.getLastName());
        basicInformationUser.setPhotoUser((uriImageProfile) == null ? "nonPhoto" : uriImageProfile.toString());


        Utils.setPersistantBasicUserInformation(basicInformationUser, this);
//        Utils.setLanguage("es-Es", this);
    }

    private void loadData() {
        if (optionRegister == Utils.REGISTER.GOOGLE) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                binding.name.getEditText().setText(acct.getGivenName());
                binding.lastName.getEditText().setText(acct.getFamilyName());
                urlImageGoogle = acct.getPhotoUrl().toString();
                Glide.with(this).load(acct.getPhotoUrl()).apply(Utils.getOptionsGlide(false)).into(binding.imageProfile);

            }
        } else if (optionRegister == FACEBOOK) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            Log.d(TAG_LOG, "onCompleted: token " + token.getToken());
            Log.d(TAG_LOG, "onCompleted: token " + token.toString());
            Log.d(TAG_LOG, "onCompleted: token " + token.getUserId());
            Log.d(TAG_LOG, "onCompleted: token " + token.getGraphDomain());
            GraphRequest request = GraphRequest.newMeRequest(
                    token,
                    (object, response) -> {

                        Log.v("LoginActivity", response.toString());


                        JSONObject json = response.getJSONObject();
                        try {
                            if (json != null) {
                                for (int i = 0; i < json.names().length(); i++) {
                                    Log.d(Utils.TAG_LOG, "key = " + json.names().getString(i) + " value = " + json.get(object.names().getString(i)));
                                }
                                binding.birthdate.getEditText().setText(Utils.convertDateFormat(json.getString("birthday")));
                                binding.name.getEditText().setText(json.getString("first_name"));
                                binding.lastName.getEditText().setText(json.getString("last_name"));
                                int typeGender;
                                switch (json.getString("gender")) {
                                    case "male":
                                        typeGender = 0;
                                        break;
                                    case "female":
                                        typeGender = 1;
                                        break;
                                    default:
                                        typeGender = 2;
                                }

                                Sex sex = Utils.getSexByIdJson(RegisterStep1Activity.this, typeGender);
                                sexSelected = sex.getId();
                                binding.sex.setText(sex.getTitleString(), false);

                                String idFacebook = json.getString("id");


                                Log.d(TAG_LOG, "onCompleted: token " + token);
                                GraphRequest request1 = GraphRequest.newGraphPathRequest(
                                        token,
                                        "/" + idFacebook + "/picture",
                                        response1 -> {
                                            if (response1.getError() == null) {
                                                JSONObject object1;
                                                try {
                                                    object1 = new JSONObject(response1.getRawResponse());
                                                    String image = object1.getJSONObject("data").getString("url");
                                                    Log.d(TAG_LOG, "onCompleted: image " + image);
                                                    Glide.with(RegisterStep1Activity.this).load(image).apply(Utils.getOptionsGlide(false)).into(binding.imageProfile);
                                                    urlImageFacebook = image;
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString("width", "500");
                                parameters.putString("redirect", "false");
                                parameters.putString("access_token", token.getToken());
                                request1.setParameters(parameters);

                                request1.executeAsync();


                            }

                        } catch (JSONException ignored) {

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", permissionsFacebookRequest);
            request.setParameters(parameters);
            request.executeAsync();
            Log.d(TAG_LOG, "loadData: " + authenticationProvider.getCurrentUser().getUid());
            Log.d(TAG_LOG, "loadData: " + authenticationProvider.getCurrentUser().getProviderId());
            // GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),"")
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER_IMAGE_COVER_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                uriCoverImage = data.getData();
                Glide.with(this).load(uriCoverImage).apply(Utils.getOptionsGlide(false)).centerCrop().into(binding.coverPageImage);
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
                Log.e(TAG_LOG, "fail on get select image " + ImagePicker.Companion.getError(data));
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialogSelectImage(TYPE_IMAGE type) {
        new MaterialAlertDialogBuilder(this).
                setTitle(R.string.edit_profile_dialog_select_image_from_tittle)
                .setItems(new String[]{getString(R.string.edit_profile_dialog_select_image_from_option1),
                        getString(R.string.edit_profile_dialog_select_image_from_option2)}, (dialogInterface, i) -> {
                            ImagePicker.Builder show = ImagePicker.Companion.with(RegisterStep1Activity.this)
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

    private boolean checkFieldAreValid() {
        boolean ret = false;
        boolean ret2 = false;
        boolean ret3 = false;
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
        if (sexSelected == null) {
            binding.sexContainer.setError("seleccione algun sexo");
        } else {
            ret3 = true;
            binding.sexContainer.setError(null);
        }

        return ret && ret2 && ret3;

    }
}