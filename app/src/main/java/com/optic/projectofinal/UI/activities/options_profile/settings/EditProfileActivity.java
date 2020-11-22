package com.optic.projectofinal.UI.activities.options_profile.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.optic.projectofinal.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mToolbar=findViewById(R.id.ownToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Editar perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AutoCompleteTextView textfield2=findViewById(R.id.inputTextSex);
          textfield2.setAdapter(new ArrayAdapter<String>(this,R.layout.textbox_gender, Arrays.asList(new String[]{"Masculino","Femenino","Otro"})));


//        textfield2.setEnabled(false);
//        textfield.setEndIconOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(EditProfileActivity.this, "clickkk", Toast.LENGTH_SHORT).show();
//                if(!textfield2.isEnabled()){
//                    textfield2.setEnabled(true);
//                }
//            }
//        });
//
//        textfield.addOnEditTextAttachedListener(new TextInputLayout.OnEditTextAttachedListener() {
//            @Override
//            public void onEditTextAttached(@NonNull TextInputLayout textInputLayout) {
//                Toast.makeText(EditProfileActivity.this, "algoo", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        textfield.addOnEndIconChangedListener(new TextInputLayout.OnEndIconChangedListener() {
//            @Override
//            public void onEndIconChanged(@NonNull TextInputLayout textInputLayout, int previousIcon) {
//
//                Toast.makeText(EditProfileActivity.this, "cambio foto", Toast.LENGTH_SHORT).show();
//            }
//        });

        MaterialDatePicker.Builder buider= MaterialDatePicker.Builder.datePicker();
        buider.setTitleText("Selecciona una fecha");
        buider.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        MaterialDatePicker materialDatePicker=buider.build();

        TextInputEditText cuadro=findViewById(R.id.datePicker);
        cuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Date date=new Date();
                date.setTime(Long.parseLong(selection.toString()));
                String formatDate=new SimpleDateFormat("dd/MM/yyyy").format(date);
               cuadro.setText(formatDate);
               if(EditProfileActivity.this.getCurrentFocus()!=null)
                   EditProfileActivity.this.getCurrentFocus().clearFocus();
            }
        });
    }
}