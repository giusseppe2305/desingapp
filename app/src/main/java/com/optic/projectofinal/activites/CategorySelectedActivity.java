package com.optic.projectofinal.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.optic.projectofinal.R;
import com.optic.projectofinal.utils.Utils;
import com.optic.projectofinal.actionsButtomSheets.ActionBottomCheckBox;
import com.optic.projectofinal.actionsButtomSheets.ActionBottomDialogFragment;
import com.optic.projectofinal.models.Category;

public class CategorySelectedActivity extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener,ActionBottomCheckBox.ItemClickListener{
    Chip chip1,chip2;
    int idCheckedChip;
    Integer[] idsCheckedCheckBox;
    Category categorySelected;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selected);

        categorySelected= Utils.getCategoryByIdJson(this,getIntent().getIntExtra("category",0));
        mToolbar=findViewById(R.id.TOOLBAR);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(categorySelected.getTitle());


        idCheckedChip=-1;
        chip1 = findViewById(R.id.chip1);
        chip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
        chip2=findViewById(R.id.chip2);
        chip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActionBottomCheckBox checkBox = ActionBottomCheckBox.newInstance();
                checkBox.setChecked(idsCheckedCheckBox);
                checkBox.show(getSupportFragmentManager(),
                        ActionBottomCheckBox.TAG);
            }
        });
    }

    public void showBottomSheet() {

        ActionBottomDialogFragment addPhotoBottomDialogFragment = ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.setChecked(idCheckedChip);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ActionBottomDialogFragment.TAG);

    }

    @Override
    public void onItemClick(int item) {
        idCheckedChip = item;

    }

    @Override
    public void onItemClick(Integer[] item) {
        idsCheckedCheckBox=item;
    }
}