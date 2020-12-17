package com.optic.projectofinal.providers;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.optic.projectofinal.utils.Utils;

import java.util.Date;

public class StorageProvider {

    private StorageReference mStorage;
    private Context context;
    private AuthenticationProvider authenticationProvider;
    public StorageProvider(Context c) {
        authenticationProvider=new AuthenticationProvider();
        mStorage= FirebaseStorage.getInstance().getReference();
        context=c;
    }
    public UploadTask uploadImageNewJob(Uri mPhoto, String id){
        return save(mPhoto,"jobs_photos",id);
    }
    private UploadTask save( Uri mPhoto,String root,String id){

        StorageReference storage=mStorage.child("jobsPhotos").child(id).child(new Date()+Utils.getFileName(mPhoto,context));

        return storage.putFile(mPhoto);
    }

    public StorageReference getStorage(){
        return mStorage;
    }

//    public Task<Uri> getUrlImage(String path, IDo funtion) {
//        return mStorage.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//               funtion.run(uri.toString());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG_LOG, "onFailure: StorageProvider->getUrlImage");
//            }
//        });
//    }

    public UploadTask uploadImageUser(Uri uri, TYPE_IMAGE type) {
        StorageReference route = mStorage.child("imagesUsers").child(authenticationProvider.getIdCurrentUser() + "_" + type );
        return route.putFile(uri);
    }

    public enum TYPE_IMAGE{PROFILE_IMAGE,COVER_IMAGE}
}
