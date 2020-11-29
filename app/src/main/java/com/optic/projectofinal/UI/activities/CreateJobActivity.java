package com.optic.projectofinal.UI.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CreateJobActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ////toolbar
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(getString(R.string.create_job_activity_title));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //load spinner category

        ArrayList<Category> myCategoriesList = Utils.getListCategoriesJson(this);

        binding.category.setItems(myCategoriesList);
        binding.category.setOnClickListener(v ->{
            if (CreateJobActivity.this.getCurrentFocus() != null) {
                CreateJobActivity.this.getCurrentFocus().clearFocus();
            }
        });
        binding.category.setOnItemSelectedListener((view, position, id, item) -> loadSubCategories(position));
        //spiner subcategories
        binding.subCategory.setOnClickListener(v ->{
            if (CreateJobActivity.this.getCurrentFocus() != null) {
                CreateJobActivity.this.getCurrentFocus().clearFocus();
            }
        });
        binding.subCategory.setItems(new SubCategory[]{});
        ///instance objects
        authenticationProvider = new AuthenticationProvider();
        subcategoriesDatabaseProvider = new SubcategoriesDatabaseProvider();
        jobsDatabaseProvider = new JobsDatabaseProvider();
        storageProvider = new StorageProvider(this);
        listUris = new ArrayList<>();
        adapterImage = new ImagePickerAdapter(this, listUris);
        //config recycler
        binding.rvImagesSelected.setAdapter(adapterImage);
        binding.rvImagesSelected.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ///
        mDialogCreateJob = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Subiendo imagen.")
                .setCancelable(false).build();

        //build dialog
        mDialogSelectFromPicture = new MaterialAlertDialogBuilder(this).
                setTitle("Elige una opcion")
                .setItems(new String[]{"Elegir de la galeria", "Tomar FOTO AHORA"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ImagePicker.Builder show = ImagePicker.Companion.with(CreateJobActivity.this)
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
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CreateJobActivity.this, "Cancelaste la opcion de elegir donde sacar imagen", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(false);


        binding.addPhoto.setOnClickListener(v -> {
            mDialogSelectFromPicture.show();
        });



    }

    private void loadSubCategories(int position) {
        binding.subCategory.setText(getString(R.string.spinner_subcategories_hint));
        binding.subCategory.getItems().clear();
        binding.subCategory.setEnabled(true);
        Category categorySelected = (Category) binding.category.getItems().get(position);
        subcategoriesDatabaseProvider.getAllByCategory(categorySelected.getId()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    List<SubCategory> result = queryDocumentSnapshots.toObjects(SubCategory.class);
                    binding.subCategory.setItems(result);
                } else {
                    Log.e("own", "onSuccess:CreateJobActivity->loadSubCategories ");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("own", "onFailure: CreateJobActivity->loadSubCategories");
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
                createJob();
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
        SubCategory subCategory = binding.subCategory.getItems().size() == 0 ? null : (SubCategory) binding.subCategory.getItems().get(binding.subCategory.getSelectedIndex());
        if (subCategory != null && !binding.subCategory.getText().equals(getString(R.string.spinner_subcategories_hint))) {
            ret3 = true;
        } else {
            Toast.makeText(this, R.string.patter_spinner_subcategories, Toast.LENGTH_SHORT).show();
        }
        if (listUris.size() > 0) {
            ret4 = true;
        } else {
            Toast.makeText(this, R.string.patter_select_almost_one_image, Toast.LENGTH_SHORT).show();

        }
        return ret && ret2 && ret3 && ret4;
    }

    private void createJob() {
        String idDocument = jobsDatabaseProvider.getIdDocument();
        Tasks.whenAllComplete(getListTaskUploadPhotos(idDocument)).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
            @Override
            public void onSuccess(List<Task<?>> tasks) {
                ///change dialog
                mDialogCreateJob.setMessage("Ultimando detalles");
                ///create object
                Category category = (Category) binding.category.getItems().get(binding.category.getSelectedIndex());
                SubCategory subCategory = (SubCategory) binding.subCategory.getItems().get(binding.subCategory.getSelectedIndex());
                Job myJob = new Job();
                myJob.setId(idDocument);
                myJob.setTitle(binding.title.getEditText().getText().toString());
                myJob.setState(Job.State.PUBLISHED);
                myJob.setIdUserOffer(authenticationProvider.getIdCurrentUser());
                myJob.setDescription(binding.description.getEditText().getText().toString());
                myJob.setCategory(category.getId());
                myJob.setSubcategory(subCategory.getId());
                /////create list
                ArrayList<String> urlImages = new ArrayList<>();
                for (Task<?> _tasks : tasks) {
                    UploadTask.TaskSnapshot result = (UploadTask.TaskSnapshot) _tasks.getResult();
                    urlImages.add(result.getMetadata().getPath());
                }
                ///
                myJob.setImages(urlImages);
                jobsDatabaseProvider.createJob(myJob).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("own", "onSuccess: CreateJobActivity->createJob");
                        mDialogCreateJob.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("own", "onFailure: CreateJobActivity->createJob");
                        mDialogCreateJob.dismiss();
                    }
                });


            }
        });


    }

    private List<StorageTask<UploadTask.TaskSnapshot>> getListTaskUploadPhotos(String idDocument) {
        List<StorageTask<UploadTask.TaskSnapshot>> list = new ArrayList<>();
        mDialogCreateJob.show();
        for (Uri _uri : listUris) {
            StorageTask<UploadTask.TaskSnapshot> task = storageProvider.uploadImageNewJob(_uri, idDocument).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot != null) {
                        Log.i("own", "onSuccess:if CreateJobActivity-> getListTaskUploadPhotos");
                    } else {
                        Log.e("own", "onSuccess:else CreateJobActivity-> getListTaskUploadPhotos");
                    }

                }
            }).addOnFailureListener(v -> {
                Log.e("own", "onFailure: CreateJobActivity-> getListTaskUploadPhotos: ");
            });
            list.add(task);
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
                .setNegativeButton(R.string.dialog_confirm_close_create_job_activity_cancel,(dialogInterface, i) -> dialogInterface.dismiss())
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
        outState.putInt("idCategory", binding.category.getSelectedIndex());
        outState.putInt("idSubcategory",binding.subCategory.getSelectedIndex());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        binding.title.getEditText().setText(savedInstanceState.getString("title"));
        binding.description.getEditText().setText(savedInstanceState.getString("description"));
        listUris=savedInstanceState.getParcelableArrayList("list_uris");
        adapterImage.notifyDataSetChanged();
        binding.category.setSelectedIndex(savedInstanceState.getInt("idCategory"));
        binding.subCategory.setSelectedIndex(savedInstanceState.getInt("idSubcategory"));
    }
}