package com.optic.projectofinal.activites;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.optic.projectofinal.R;

public class Overla extends CoordinatorLayout {
    public Overla(@NonNull Context context) {
        super(context);
        View.inflate(context,R.layout.overlay,this);
    }

    public Overla(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Overla(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
