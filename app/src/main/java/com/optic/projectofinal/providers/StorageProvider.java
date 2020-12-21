package com.optic.projectofinal.providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.optic.projectofinal.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class StorageProvider {

    private StorageReference mStorage;
    private Context context;
    private AuthenticationProvider authenticationProvider;
    public StorageProvider(Context c) {
        authenticationProvider=new AuthenticationProvider();
        mStorage= FirebaseStorage.getInstance().getReference();
        context=c;
    }
    public UploadTask uploadImageNewJob(Uri mPhoto, String id,Context context){
        return save(mPhoto,"jobs_photos",id,context);
    }
    private UploadTask save( Uri mPhoto,String root,String id,Context context){
        Date now=new Date();
        StorageReference storage=mStorage.child(root).child(id).child(now+Utils.getFileName(mPhoto,context));
        return storage.putFile(mPhoto);
    }
    public StorageTask<UploadTask.TaskSnapshot> createThumbnail(String title, Bitmap mPhoto, String... path){

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mPhoto.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            mPhoto.recycle();
            byte[] data = baos.toByteArray();

            StorageReference storageThumb = FirebaseStorage.getInstance().getReference();
            for(String it:path){
                storageThumb=storageThumb.child(it);
            }
            return storageThumb.child(title+"_thumbnail").putBytes(data).addOnFailureListener(e -> Log.d(TAG_LOG, "fail crate: thumbnail "+e.getMessage()));


    }
    public StorageReference getStorage(){
        return mStorage;
    }


    public UploadTask uploadImageUser(Uri uri, TYPE_IMAGE type) {
        StorageReference route = mStorage.child("imagesUsers").child(authenticationProvider.getIdCurrentUser()  ).child(type.toString());
        return route.putFile(uri);
    }
    public UploadTask uploadImageUser(byte[] file, TYPE_IMAGE type) {
        StorageReference route = mStorage.child("imagesUsers").child(authenticationProvider.getIdCurrentUser() ).child(type.toString());
        return route.putBytes(file);
    }
    public enum TYPE_IMAGE{PROFILE_IMAGE,COVER_IMAGE}
}
