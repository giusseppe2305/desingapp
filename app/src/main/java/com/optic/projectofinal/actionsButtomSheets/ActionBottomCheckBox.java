package com.optic.projectofinal.actionsButtomSheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.optic.projectofinal.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionBottomCheckBox extends BottomSheetDialogFragment
   {

    public static final String TAG = "ActionBottomCheckBox";

    private ItemClickListener mListener;

    private View vista;
    private Integer[] idsChecked;
    private CheckBox opctionCheck1,opctionCheck2,opctionCheck3,opctionCheck4;
    private CheckBox[] allBox;
    private Button buttonDone;

    public static ActionBottomCheckBox newInstance() {

        return new ActionBottomCheckBox();
    }

    public void setChecked(Integer[] idCheck) {
        if (idCheck == null) {
            return;
        }
        this.idsChecked = idCheck;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista= inflater.inflate(R.layout.bottom_sheet_2, container, false);

        if(idsChecked!=null){
            for(int i :idsChecked){
                CheckBox rr= vista.findViewById(i);
                rr.setChecked(true);

            }
        }
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vista = view;

        super.onViewCreated(view, savedInstanceState);
        opctionCheck1 = view.findViewById(R.id.opctionCheck1);
        opctionCheck2 = view.findViewById(R.id.opctionCheck2);
        opctionCheck3 = view.findViewById(R.id.opctionCheck3);
        opctionCheck4 = view.findViewById(R.id.opctionCheck4);
        buttonDone=view.findViewById(R.id.done);
        allBox=new CheckBox[]{opctionCheck1,opctionCheck2,opctionCheck3,opctionCheck4};
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> checkeds=new ArrayList<>();
                for(CheckBox i:allBox){
                    if(i.isChecked()){
                        checkeds.add(i.getId());
                    }
                }
                idsChecked=checkeds.toArray(new Integer[]{});
                mListener.onItemClick(idsChecked);
                dismiss();
            }
        });
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





    public interface ItemClickListener {
        void onItemClick(Integer[] item);
    }

}
