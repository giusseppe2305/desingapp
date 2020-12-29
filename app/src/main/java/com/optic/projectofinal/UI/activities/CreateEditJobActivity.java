package com.optic.projectofinal.UI.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.ImagePickerAdapter;
import com.optic.projectofinal.databinding.ActivityCreateJobBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.models.SubCategory;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.providers.StorageProvider;
import com.optic.projectofinal.providers.SubcategoriesDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class CreateEditJobActivity extends AppCompatActivity {

    private ActivityCreateJobBinding binding;
    private int PICKER_IMAGE_CAMERA = 1;
    private int PICKER_IMAGE_GALLERY = 2;
    private MaterialAlertDialogBuilder mDialogSelectFromPicture;
    private ImagePickerAdapter adapterImage;
    private ArrayList<Uri> listUris;
    private SubcategoriesDatabaseProvider subcategoriesDatabaseProvider;
    private AuthenticationProvider authenticationProvider;
    private StorageProvider storageProvider;
    private JobsDatabaseProvider jobsDatabaseProvider;
    private AlertDialog mDialogCreateJob;
    private String idJobEdit;
    private ArrayList<Category> listCategories;
    private ArrayList<SubCategory> listSubcategories;
    private Integer positionCategorySelected;
    private Integer positionSubcategorySelected;
    private ArrayList<String> listImagesBeginingEdit;
    private LinearLayoutManager linerLayoutImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ///instance objects
        authenticationProvider = new AuthenticationProvider();
        subcategoriesDatabaseProvider = new SubcategoriesDatabaseProvider();
        jobsDatabaseProvider = new JobsDatabaseProvider();
        storageProvider = new StorageProvider(this);

        ////toolbar
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(getString(R.string.create_job_activity_title));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //load spinner categoryloadSubCategories(position)
        listCategories = Utils.getListCategoriesJson(this);
        binding.category.setAdapter(new ArrayAdapter<Category>(this, R.layout.textbox_gender, listCategories));
        binding.category.setOnItemClickListener((adapterView, view, i, l) -> {
            loadSubCategories(i, null);
            positionCategorySelected = i;
        });
        //spiner subcategories
        listSubcategories = new ArrayList<>();
        binding.subCategory.setOnItemClickListener((adapterView, view, i, l) -> positionSubcategorySelected = i);


        listUris = new ArrayList<>();
        adapterImage = new ImagePickerAdapter(this, listUris);
        //config recycler
        binding.rvImagesSelected.setAdapter(adapterImage);
        linerLayoutImages=new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.rvImagesSelected.setLayoutManager(linerLayoutImages);
        ///
        mDialogCreateJob = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.create_edit_job_uploading_photos)
                .setCancelable(false).build();

        //build dialog
        mDialogSelectFromPicture = new MaterialAlertDialogBuilder(this).
                setTitle(R.string.create_edit_job_dialog_photo_title)
                .setItems(new String[]{getString(R.string.create_edit_job_dialog_photo_option_1), getString(R.string.create_edit_job_dialog_photo_option_2)}, (dialogInterface, i) -> {
                    ImagePicker.Builder show = ImagePicker.Companion.with(CreateEditJobActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .galleryMimeTypes(new String[]{"image/png", "image/jpeg", "image/jpg"});

                    if (i == 0) {
                        show.galleryOnly()
                                .start(PICKER_IMAGE_CAMERA);
                    } else {
                        show.cameraOnly()
                                .start(PICKER_IMAGE_GALLERY);
                    }
                })
                .setNegativeButton(R.string.create_edit_job_dialog_photo_negative_button, null).setCancelable(false);


        binding.addPhoto.setOnClickListener(v -> {
            mDialogSelectFromPicture.show();
        });


//check if edit
        idJobEdit = getIntent().getStringExtra("idJob");
        if (idJobEdit != null) {
            //edit
            jobsDatabaseProvider.getJobById(idJobEdit).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Job job = documentSnapshot.toObject(Job.class);
                    binding.title.getEditText().setText(job.getTitle());
                    binding.description.getEditText().setText(job.getDescription());
                    binding.description.getEditText().setText(job.getDescription());

                    positionCategorySelected = listCategories.indexOf(new Category(job.getCategory()));
                    Category category = listCategories.get(positionCategorySelected);
                    binding.category.setText(category.getTitleString(), false);
                    new SubcategoriesDatabaseProvider().getSubCategoryById(job.getSubcategory()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            loadSubCategories(job.getCategory(), new SubCategory(documentSnapshot.getString("id")));
                            binding.subCategory.setText(documentSnapshot.getString("name"), false);

                        }
                    }).addOnFailureListener(v -> Log.e(TAG_LOG, " SubcategoriesDatabaseProvider CreateJobActivity onSuccess: " + v.getMessage()));


                    if (job.getImages().size() > 0) {
                        for (String i : job.getImages()) {

                            listUris.add(Uri.parse(i));
                        }
                        listImagesBeginingEdit = job.getImages();
                        adapterImage.notifyDataSetChanged();
                        binding.rvImagesSelected.setVisibility(View.VISIBLE);
                        binding.addPhoto.setVisibility(View.GONE);
                    }

                }
            });
        }

    }

    private void loadSubCategories(int position, SubCategory isEditing) {
        listSubcategories.clear();
        positionSubcategorySelected = null;
        binding.subCategory.setText("");
        binding.subCategory.setEnabled(true);
        Category categorySelected = listCategories.get(position);
        subcategoriesDatabaseProvider.getAllByCategory(categorySelected.getId()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    List<SubCategory> result = queryDocumentSnapshots.toObjects(SubCategory.class);
                    listSubcategories.clear();
                    listSubcategories.addAll(result);
                    if (isEditing != null) {
                        positionSubcategorySelected = listSubcategories.indexOf(isEditing);

                    }
                    binding.subCategory.setAdapter(new ArrayAdapter<SubCategory>(CreateEditJobActivity.this, R.layout.textbox_gender, listSubcategories));
                } else {
                    Log.e(TAG_LOG, "onSuccess:CreateJobActivity->loadSubCategories ");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG_LOG, "onFailure: CreateJobActivity->loadSubCategories");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER_IMAGE_CAMERA || requestCode == PICKER_IMAGE_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {

                //Image Uri will not be null for RESULT_OK
                Uri fileUri = data.getData();

                listUris.add(fileUri);
                int position = listUris.size();
                adapterImage.notifyItemInserted(position);
                adapterImage.notifyItemRangeChanged(position, listUris.size());
                //binding.iv.setImageURI(fileUri);
                //You can get File object from intent
                File a = ImagePicker.Companion.getFile(data);
                //You can also get File Path from intent
                System.out.println("ruta " + ImagePicker.Companion.getFilePath(data));
                checkCountImagesSelected();
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_job_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnMenuCreateJob) {
            if (checFieldsAreValid()) {

                createUpdateJob();
            }
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checFieldsAreValid() {

        boolean ret = false;
        boolean ret2 = false;
        boolean ret3 = false;
        boolean ret4 = false;
        boolean ret5 = false;
        if (binding.title.getEditText().getText().length() == 0) {
            binding.title.setError(getString(R.string.pattern_title_void_field));
        } else if (binding.title.getEditText().getText().length() > 40) {
            binding.title.setError(getString(R.string.pattern_title_correct_length));

        } else {
            ret = true;
            binding.title.setErrorEnabled(false);
        }
        if (binding.description.getEditText().getText().length() == 0) {
            binding.description.setError(getString(R.string.pattern_description_void_field));

        } else if (binding.description.getEditText().getText().length() > 240) {
            binding.description.setError(getString(R.string.pattern_description_correct_length));

        } else {
            binding.description.setErrorEnabled(false);
            ret2 = true;
        }
//        if(listSubcategories!=null && listSubcategories.size()>0){
//            SubCategory subCategory = listSubcategories.get(positionSubcategorySelected);
//            if (subCategory != null && !binding.subCategory.getText().equals(getString(R.string.spinner_subcategories_hint))) {
//                ret3 = true;
//            } else {
//                Toast.makeText(this, R.string.patter_spinner_subcategories, Toast.LENGTH_SHORT).show();
//            }
//        }
        if (positionCategorySelected != null) {
            ret3 = true;
            binding.categoryInput.setError(null);
        } else {
            binding.categoryInput.setError(getString(R.string.spinner_category_hint));
        }
        if (positionSubcategorySelected != null) {
            ret5 = true;
            binding.subCategoryInput.setError(null);

        } else {
            binding.subCategoryInput.setError(getString(R.string.spinner_subcategories_hint));
        }

        if (listUris.size() > 0) {
            ret4 = true;
        } else {
            Toast.makeText(this, R.string.patter_select_almost_one_image, Toast.LENGTH_SHORT).show();

        }
        return ret && ret2 && ret3 && ret4 && ret5;
    }

    private void createUpdateJob() {

        Job myJob = new Job();
        myJob.setId(idJobEdit==null?jobsDatabaseProvider.getIdDocument():idJobEdit);
        Tasks.whenAllComplete(getListTaskUploadPhotos(myJob.getId())).addOnSuccessListener(tasks -> {
            ///change dialog
            mDialogCreateJob.setMessage(getString(R.string.create_edit_job_dialog_message_finishing));
            ///create object
            Category category = listCategories.get(positionCategorySelected);
            SubCategory subCategory = listSubcategories.get(positionSubcategorySelected);

            myJob.setTitle(binding.title.getEditText().getText().toString());
            myJob.setState(Job.State.PUBLISHED);
            myJob.setIdUserOffer(authenticationProvider.getIdCurrentUser());
            myJob.setDescription(binding.description.getEditText().getText().toString());
            myJob.setCategory(category.getId());
            myJob.setSubcategory(subCategory.getId());
            /////create list
            List<Task<Uri>> tasksGetUrl = new ArrayList<>();
            for (Task<?> _tasks : tasks) {
                UploadTask.TaskSnapshot result = (UploadTask.TaskSnapshot) _tasks.getResult();
                tasksGetUrl.add(result.getStorage().getDownloadUrl());
            }
            Tasks.whenAllComplete(tasksGetUrl).addOnSuccessListener(tasks1 -> {

                ArrayList<String> urlImages = new ArrayList<>();

                if (idJobEdit != null) {
                    for (Uri it : listUris) {
                        for (String it2 : listImagesBeginingEdit) {
                            if (it.toString().equals(it2)) {

                                urlImages.add(it2);
                                break;
                            }
                        }

                    }
                }

                for (Task<?> uri : tasks1) {
                    Uri result = (Uri) uri.getResult();
                    urlImages.add(result.toString());
                }

                myJob.setImages(urlImages);


                if (idJobEdit == null) {
                    myJob.setTimestamp(new Date().getTime());
                    jobsDatabaseProvider.createJob(myJob).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //we create a thumbnail
                            createThumbnail(urlImages.get(0),myJob.getId());

                            mDialogCreateJob.dismiss();
                            Log.d(TAG_LOG, "onSuccess: "+myJob.getId());
                            Toast.makeText(CreateEditJobActivity.this, R.string.create_edit_job_message_create, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.e(TAG_LOG, "onFailure: create CreateJobActivity->createJob");
                            Toast.makeText(CreateEditJobActivity.this, R.string.create_edit_job_message_fail_created, Toast.LENGTH_SHORT).show();
                            mDialogCreateJob.dismiss();
                        }
                    });
                } else {
                    jobsDatabaseProvider.updateJob(myJob).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //we create a thumbnail
                            createThumbnail(urlImages.get(0),myJob.getId());

                            mDialogCreateJob.dismiss();
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            Toast.makeText(CreateEditJobActivity.this, R.string.create_edit_job_update, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG_LOG, "onFailure: update CreateJobActivity->createJob");
                            Toast.makeText(CreateEditJobActivity.this, R.string.create_edit_job_update_fail, Toast.LENGTH_SHORT).show();
                            mDialogCreateJob.dismiss();
                        }
                    });


                }


            });


        });


    }

    private void createThumbnail(String image,String idJob) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(image);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    storageProvider.createThumbnail(idJob,imageBitmap,"all_jobs_thumbnail",idJob).addOnSuccessListener(taskSnapshot ->
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        Job update=new Job();
                        update.setId(idJob);
                        update.setThumbnail(uri.toString());
                        jobsDatabaseProvider.updateJob(update).addOnFailureListener(e-> Log.e(TAG_LOG, "fail to save thumbnail job "+e.getMessage() ));
                    }).addOnFailureListener(e -> Log.d(TAG_LOG, "fail get url thumbnail  "+e.getMessage())));

                } catch (MalformedURLException e) {
                    Log.e(TAG_LOG, "createThumbnail: "+e.getMessage() );
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG_LOG, "createThumbnail: "+e.getMessage() );
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<StorageTask<UploadTask.TaskSnapshot>> getListTaskUploadPhotos(String idDocument) {
        List<StorageTask<UploadTask.TaskSnapshot>> list = new ArrayList<>();
        mDialogCreateJob.show();
        for (Uri _uri : listUris) {
            if (!_uri.toString().contains("https://")) {
                StorageTask<UploadTask.TaskSnapshot> task = storageProvider.uploadImageNewJob(_uri, idDocument, CreateEditJobActivity.this).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot != null) {
                            Log.i(TAG_LOG, "onSuccess:if CreateJobActivity-> getListTaskUploadPhotos");
                        } else {
                            Log.e(TAG_LOG, "onSuccess:else CreateJobActivity-> getListTaskUploadPhotos");
                        }
                    }
                }).addOnFailureListener(v -> {
                    Log.e(TAG_LOG, "onFailure: CreateJobActivity-> getListTaskUploadPhotos: ");
                });
                list.add(task);
            }

        }
        return list;
    }

    public void checkCountImagesSelected() {
        if (listUris.size() > 0) {
            binding.rvImagesSelected.setVisibility(View.VISIBLE);
            binding.addPhoto.setVisibility(View.GONE);
        } else {
            binding.rvImagesSelected.setVisibility(View.GONE);
            binding.addPhoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_confirm_close_create_job_activity_title)
                .setMessage(R.string.dialog_confirm_close_create_job_activity_message)
                .setNegativeButton(R.string.dialog_confirm_close_create_job_activity_cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.dialog_confirm_close_create_job_activity_ok, (dialogInterface, i) -> super.onBackPressed())
                .show();

    }

    public MaterialAlertDialogBuilder getDialogPhoto() {
        return mDialogSelectFromPicture;
    }


//    save state bundle

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("description", binding.description.getEditText().getText().toString());
        outState.putString("title", binding.title.getEditText().getText().toString());
        outState.putParcelableArrayList("list_uris", listUris);
        if (listCategories.size() > 0 && positionCategorySelected != null)
            outState.putString("idCategory", listCategories.get(positionCategorySelected).getTitleString());
        if (listSubcategories.size() > 0 && positionSubcategorySelected != null)
            outState.putString("idSubcategory", listSubcategories.get(positionSubcategorySelected).getName());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        binding.title.getEditText().setText(savedInstanceState.getString("title"));
        binding.description.getEditText().setText(savedInstanceState.getString("description"));
        listUris = savedInstanceState.getParcelableArrayList("list_uris");
        adapterImage.notifyDataSetChanged();

        binding.category.setText(savedInstanceState.getString("idCategory"), false);
        binding.subCategory.setText(savedInstanceState.getString("idSubcategory"), false);
    }
}