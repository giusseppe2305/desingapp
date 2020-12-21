package com.optic.projectofinal.utils;

import android.content.Context;
import android.net.Uri;

import com.optic.projectofinal.R;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import uk.co.jemos.podam.api.PodamFactoryImpl;

public class UtilsTest {
    @Mock
    Context context;
    @Mock
    Uri uri;
    @Mock
    private Utils utils;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    PodamFactoryImpl podam=new PodamFactoryImpl();
    @Test
    public void getResIdOK() throws NoSuchFieldException, IllegalAccessException {
        int result = utils.getResId("sex_male", R.string.class);
        Assert.assertNotEquals(context.getString(result),"sex_male");
    }
    @Test
    public void getFileNameOK()  {
        Uri uri=Uri.parse("hola.jpg");
        String result = utils.getFileName(uri, podam.manufacturePojo(Context.class));
        Assert.assertNotNull(result);
    }



}
