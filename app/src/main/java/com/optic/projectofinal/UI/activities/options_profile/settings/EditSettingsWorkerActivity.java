package com.optic.projectofinal.UI.activities.options_profile.settings;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.optic.projectofinal.R;
import com.optic.projectofinal.Swipe.SwipeRVAdapter;
import com.optic.projectofinal.Swipe.SwipeRVTouchHelper;
import com.optic.projectofinal.databinding.ActivityEditSettingsWorkerBinding;
import com.optic.projectofinal.databinding.AlertDialogCreateSkillBinding;
import com.optic.projectofinal.databinding.SwitchItemBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Resource;
import com.optic.projectofinal.models.Skill;
import com.optic.projectofinal.models.SubCategory;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.SubcategoriesDatabaseProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class EditSettingsWorkerActivity extends AppCompatActivity {

    private ActivityEditSettingsWorkerBinding binding;
    private ArrayList<Skill> listSkills;
    private SwipeRVAdapter adapterSkills;
    private ArrayList<Resource> listResources;
    private SwipeRVAdapter adapterResources;
    private UserDatabaseProvider userDatabaseProvider;
    private AuthenticationProvider authenticationProvider;
    private SubcategoriesDatabaseProvider subcategoriesDatabaseProvider;
    private String isEditingSkillId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditSettingsWorkerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //set toolbar
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.edit_settings_worker_activity_title));
        //
        userDatabaseProvider = new UserDatabaseProvider();
        authenticationProvider = new AuthenticationProvider();
        subcategoriesDatabaseProvider = new SubcategoriesDatabaseProvider();
        ////
        binding.container.setAlpha(0.3f);
        Utils.setEnabledAllViews(binding.container,false);
        //
        loadData();

        binding.addResource.setOnClickListener(v -> {
            ArrayList<Resource> temp = Utils.getListResourcesJson(this);

            for(Resource it:temp){
                Log.d(TAG_LOG, "onCreate:  Resource "+it.getTitleString());
            }
            for(Resource it2:listResources){
                Log.d(TAG_LOG, "onCreate:  listResources "+it2.getTitleString());
            }
            temp.removeAll(listResources);
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.edit_settings_worker_dialog_resource_title)
                    .setAdapter(new ArrayAdapter<>(EditSettingsWorkerActivity.this, R.layout.support_simple_spinner_dropdown_item, temp), (dialogInterface, i) -> {
                        adapterResources.addSwipeItem(listResources.size(), temp.get(i));
                    })
                    .setNegativeButton(R.string.edit_settings_worker_dialog_resource_cancel, null)
                    .show();
        });

        binding.addSkill.setOnClickListener(v -> showAlertDialogSkill(null));
    }

    private void showAlertDialogSkill(Integer itemToEdit) {
        AlertDialogCreateSkillBinding bindingAlertdialog = AlertDialogCreateSkillBinding.inflate(getLayoutInflater());
        ///load spinner category
        ArrayList<Category> myCategoriesList = Utils.getListCategoriesJson(EditSettingsWorkerActivity.this);
        bindingAlertdialog.category.setItems(myCategoriesList);
        bindingAlertdialog.category.setOnClickListener(vv -> {
            if (bindingAlertdialog.getRoot().findFocus() != null) {
                bindingAlertdialog.getRoot().findFocus().clearFocus();
            }
        });
        bindingAlertdialog.category.setOnItemSelectedListener((view, position, id, item) -> loadSubCategories(position, bindingAlertdialog));

        //load spinner subcategory
        bindingAlertdialog.subCategory.setOnClickListener(vv -> {
            if (bindingAlertdialog.getRoot().findFocus()!= null) {
                bindingAlertdialog.getRoot().findFocus().clearFocus();
            }
        });
        bindingAlertdialog.subCategory.setItems(new SubCategory[]{});
        ///load data if is edit
        if (itemToEdit != null) {
            Skill iterated = listSkills.get(itemToEdit);
            bindingAlertdialog.titleSkill.getEditText().setText(iterated.getTitle());
            bindingAlertdialog.priceSkill.getEditText().setText(String.valueOf(iterated.getPricePerHour()));
            bindingAlertdialog.category.setSelectedIndex(iterated.getIdCategory() - 1);
            loadSubCategories(iterated.getIdCategory() - 1, bindingAlertdialog);
            isEditingSkillId = iterated.getIdSubcategory();
        }


        MaterialAlertDialogBuilder d = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.edit_settings_worker_dialog_skill_title)
                .setView(bindingAlertdialog.getRoot())
                .setPositiveButton((itemToEdit != null) ? getString(R.string.edit_settings_worker_dialog_skill_edit) : getString(R.string.edit_settings_worker_dialog_skill_create), (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .setNegativeButton(R.string.edit_settings_worker_dialog_skill_cancel, null);
        AlertDialog a = d.create();
        a.setOnShowListener(dialogInterface -> a.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //not cacel when click positive button only if fields are valid
                if (checFieldsAreValid(bindingAlertdialog)) {
                    Category category = (Category) bindingAlertdialog.category.getItems().get(bindingAlertdialog.category.getSelectedIndex());
                    SubCategory subCategory = (SubCategory) bindingAlertdialog.subCategory.getItems().get(bindingAlertdialog.subCategory.getSelectedIndex());
                    Skill skill = new Skill();
                    skill.setIdCategory(category.getId());
                    skill.setIdSubcategory(subCategory.getId());
                    skill.setPricePerHour(Double.parseDouble(bindingAlertdialog.priceSkill.getEditText().getText().toString()));
                    skill.setTitle(bindingAlertdialog.titleSkill.getEditText().getText().toString());
                    //change if edit
                    if (isEditingSkillId != null) {
                        listSkills.get(itemToEdit).update(skill);
                        adapterSkills.notifyItemChanged(itemToEdit);
                        isEditingSkillId = null;
                    } else {
                        adapterSkills.addSwipeItem(listSkills.size(), skill);
                    }
                    dialogInterface.dismiss();
                }


            }
        }));
        a.show();

    }

    private boolean checFieldsAreValid(AlertDialogCreateSkillBinding bindingAlertdialog) {

        boolean ret = false;
        boolean ret2 = false;
        boolean ret3 = false;
        if (bindingAlertdialog.titleSkill.getEditText().getText().length() == 0) {
            bindingAlertdialog.titleSkill.setError(getString(R.string.pattern_title_void_field));
            bindingAlertdialog.titleSkill.setError(getString(R.string.pattern_title_void_field));
        } else if (bindingAlertdialog.titleSkill.getEditText().getText().length() > 40) {
            bindingAlertdialog.titleSkill.setError(getString(R.string.pattern_title_correct_length));

        } else {
            ret = true;
            bindingAlertdialog.titleSkill.setErrorEnabled(false);
        }

        SubCategory subCategory = bindingAlertdialog.subCategory.getItems().size() == 0 ? null : (SubCategory) bindingAlertdialog.subCategory.getItems().get(bindingAlertdialog.subCategory.getSelectedIndex());
        if (subCategory != null && !bindingAlertdialog.subCategory.getText().equals(getString(R.string.spinner_subcategories_hint))) {
            ret2 = true;
        } else {
            Toast.makeText(this, R.string.patter_spinner_subcategories, Toast.LENGTH_SHORT).show();
        }

        if (bindingAlertdialog.priceSkill.getEditText().getText().toString().length() == 0) {
            bindingAlertdialog.priceSkill.setError(getString(R.string.pattern_title_void_field));
        } else {
            ret3 = true;
        }

        return ret && ret2 && ret3;
    }

    private void loadSubCategories(int position, AlertDialogCreateSkillBinding bindingAlertdialog) {
         bindingAlertdialog.subCategory.setText(getString(R.string.spinner_subcategories_hint));
        bindingAlertdialog.subCategory.getItems().clear();
        bindingAlertdialog.subCategory.setEnabled(true);
        Category categorySelected = (Category) bindingAlertdialog.category.getItems().get(position);
        Log.e(TAG_LOG, "loadSubCategories: " + categorySelected.getId());
        subcategoriesDatabaseProvider.getAllByCategory(categorySelected.getId()).addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                List<SubCategory> result = queryDocumentSnapshots.toObjects(SubCategory.class);
                bindingAlertdialog.subCategory.setItems(result);

                if (isEditingSkillId != null) {
                    Log.e(TAG_LOG, "onSuccess: entra en is editing firebase " + bindingAlertdialog.subCategory.getItems().size());
                    for (int i = 0; i < bindingAlertdialog.subCategory.getItems().size(); i++) {
                        SubCategory sub = (SubCategory) bindingAlertdialog.subCategory.getItems().get(i);
                        Log.e(TAG_LOG, sub.getId() + "-" + isEditingSkillId + "-> " + i);
                        if (sub.getId().equals(isEditingSkillId)) {
                            Log.e(TAG_LOG, "onSuccess: postition final " + i);
                            bindingAlertdialog.subCategory.setSelectedIndex(i);
                        }
                    }


                }
            } else {
                Log.e(TAG_LOG, "onSuccess:EditSettingsWorkerActivity->loadSubCategories ");
            }
        }).addOnFailureListener(e -> Log.e(TAG_LOG, "onFailure: EditSettingsWorkerActivity->loadSubCategories"));
    }

    private void loadData() {
        userDatabaseProvider.getUser(authenticationProvider.getIdCurrentUser()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User mUser = documentSnapshot.toObject(User.class);
                //load resources
                ArrayList<Integer> listR = mUser.getResources();
                loadResources(listR);
                //load skills
                ArrayList<Skill> listS = mUser.getSkills();

                loadSkills(listS);

            } else {
                Log.e(TAG_LOG, "EditSettingsWorkerActivity->loadData -> onSuccess: ");
            }
        }).addOnFailureListener(v -> Log.e(TAG_LOG, "EditSettingsWorkerActivity->loadData->addOnFailureListener: " + v.getMessage()));
    }

    private void loadSkills(ArrayList<Skill> listS) {
        if (listS != null && listS.size() > 0) {
            listSkills = listS;
        } else {
            listSkills = new ArrayList<>();
        }
        adapterSkills = new SwipeRVAdapter(listSkills, this, Skill.class);
        setupRecyclerView(binding.rvSkilsList, adapterSkills, listSkills, false, listenerSkills);
    }

    private void loadResources(ArrayList<Integer> listR) {

        if (listR != null && listR.size() > 0) {
            listResources = Utils.createListResourcesByIds(this, listR);
        } else {
            listResources = new ArrayList<>();
        }
        adapterResources = new SwipeRVAdapter(listResources, this, Resource.class);
        setupRecyclerView(binding.rvResourcesList, adapterResources, listResources, true, listenerResources);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_edit_settings_worker, menu);
        SwitchItemBinding bindSwitch = SwitchItemBinding.bind(menu.findItem(R.id.switch_settings_worker).getActionView());
        bindSwitch.option.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
            //checked
                binding.container.setAlpha(1);
                Utils.setEnabledAllViews(binding.container,true);

                menu.findItem(R.id.btnSave).setVisible(true);
            }else{
                //unchecked
                binding.container.setAlpha(0.3f);
                Utils.setEnabledAllViews(binding.container,false);
                menu.findItem(R.id.btnSave).setVisible(false);

            }
        });

        userDatabaseProvider.getUser(authenticationProvider.getIdCurrentUser()).addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                if(documentSnapshot.getBoolean("professional")){
                    bindSwitch.option.setChecked(true);

                }
            }else{
                Log.e(TAG_LOG, "EditSettingsWorkerActivity onCreateOptionsMenu onSuccess: " );
            }
        }).addOnFailureListener(v-> Log.e(TAG_LOG, " EditSettingsWorkerActivity addOnFailureListener onCreateOptionsMenu: "+v.getMessage() ));

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.btnSave){
            updateDataFirebase();
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDataFirebase() {
        User userUpdate=new User();
        userUpdate.setId(authenticationProvider.getIdCurrentUser());
        userUpdate.setProfessional(listSkills.size()==0?false:true);
        userUpdate.setResources(Utils.createListIntResourcesByList(listResources));
        userUpdate.setSkills(listSkills);
        userDatabaseProvider.updateUser(userUpdate).addOnFailureListener(v-> Log.e(TAG_LOG, "EditSettingsWorkerActivity updateDataFirebase: addOnFailureListener"+v.getMessage() ));
    }



    private void setupRecyclerView(RecyclerView rv, SwipeRVAdapter adapter, ArrayList list, boolean onlyLeft, SwipeRVTouchHelper.SwipeRVTouchHelperListener listener) {
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        ItemTouchHelper.SimpleCallback simpleCallback = new SwipeRVTouchHelper(listener, 0,
                onlyLeft ? ItemTouchHelper.LEFT : ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv);


        rv.setAdapter(adapter);

    }

    SwipeRVTouchHelper.SwipeRVTouchHelperListener listenerSkills = new SwipeRVTouchHelper.SwipeRVTouchHelperListener() {
        @Override
        public void onSwiped(int direction, int position) {
            adapterSkills.notifyDataSetChanged();
            // Temporary store the swiped off item
            final Skill skill = listSkills.get(position);
            //Remove the item
            //adapterSkills.removeSwipeItem(position);
            // If swipe left - delete the item
            if (direction == ItemTouchHelper.LEFT) {
                new MaterialAlertDialogBuilder(EditSettingsWorkerActivity.this)
                        .setTitle(R.string.edit_settings_worker_dialog_delete_skill_title)
                        .setMessage(getString(R.string.edit_settings_worker_dialog_delete_skill_message) + skill.getTitle() + "?")
                        .setNegativeButton(R.string.edit_settings_worker_dialog_delete_skill_cancel, null)
                        .setPositiveButton(R.string.edit_settings_worker_dialog_delete_skill_delete, (dialogInterface, i) -> adapterSkills.removeSwipeItem(position))
                        .show();
            } else if (direction == ItemTouchHelper.RIGHT) {
                showAlertDialogSkill(position);
            }
        }
    };
    SwipeRVTouchHelper.SwipeRVTouchHelperListener listenerResources = new SwipeRVTouchHelper.SwipeRVTouchHelperListener() {
        @Override
        public void onSwiped(int direction, int position) {
            adapterResources.notifyDataSetChanged();
            // Temporary store the swiped off item
            final Resource resource = listResources.get(position);
            //Remove the item
            //adapterSkills.removeSwipeItem(position);
            // If swipe left - delete the item
            if (direction == ItemTouchHelper.LEFT) {
                new MaterialAlertDialogBuilder(EditSettingsWorkerActivity.this)
                        .setTitle(R.string.edit_settings_worker_dialog_delete_resource_title)
                        .setMessage(getString(R.string.edit_settings_worker_dialog_delete_resource_message) + resource.getTitleString() + "?")
                        .setNegativeButton(R.string.edit_settings_worker_dialog_delete_resource_cancel, null)
                        .setPositiveButton(R.string.edit_settings_worker_dialog_delete_resource_delete, (dialogInterface, i) -> adapterResources.removeSwipeItem(position))
                        .show();
//            Snackbar.make(binding.rvSkilsList, "Contact deleted", Snackbar.LENGTH_LONG)
//                    .setActionTextColor(ContextCompat.getColor(this, R.color.spots_dialog_color))
//                    .setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            adapterSkills.addSwipeItem(position, contact);
//                        }
//                    }).show();
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
}