package com.optic.projectofinal.UI.actionsButtomSheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.optic.projectofinal.R;

public class ActionBottomDialogFragment extends BottomSheetDialogFragment
        implements  RadioGroup.OnCheckedChangeListener {

    public static final String TAG = "ActionBottomDialog";

    private ItemClickListener mListener;
    private RadioGroup radioGroup;
    private View vista;
    private Integer idRadio;

    public static ActionBottomDialogFragment newInstance() {

        return new ActionBottomDialogFragment();
    }

    public void setChecked(Integer idRadio) {
        if (idRadio == 0) {
            return;
        }
        this.idRadio = idRadio;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista= inflater.inflate(R.layout.bottom_sheet_1, container, false);

        if(idRadio!=-1){
            RadioButton rr= vista.findViewById(idRadio);
            rr.setChecked(true);
        }
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vista = view;

        super.onViewCreated(view, savedInstanceState);
        radioGroup = view.findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener(this);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton rb = (RadioButton) vista.findViewById(i);
        mListener.onItemClick(rb.getId());
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(int item);
    }

}
