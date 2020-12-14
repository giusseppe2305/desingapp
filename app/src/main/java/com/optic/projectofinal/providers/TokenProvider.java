package com.optic.projectofinal.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.optic.projectofinal.models.Token;

public class TokenProvider {
    private CollectionReference mCollection;
    private AuthenticationProvider mAuth;

    public TokenProvider() {

        mAuth=new AuthenticationProvider();
        mCollection= FirebaseFirestore.getInstance().collection("Tokens");
    }
    public void create(final String token)
    {
        Token token_ = new Token(token)  ;
        mCollection.document(mAuth.getIdCurrentUser()).set(token);
    }
    public Task<DocumentSnapshot> getToken(String idUser){
        return mCollection.document(idUser).get();
    }
}
