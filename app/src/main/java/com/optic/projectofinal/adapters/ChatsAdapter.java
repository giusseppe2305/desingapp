package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.ListenerRegistration;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ChatConversationActivity;
import com.optic.projectofinal.databinding.CardviewChatPreviewBinding;
import com.optic.projectofinal.models.Chat;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.ChatsProvider;
import com.optic.projectofinal.providers.MessageProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.RelativeTime;
import com.optic.projectofinal.utils.Utils;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;


public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {
    private Context context;
    private UserDatabaseProvider mUserProvider;
    private ChatsProvider mChatProvider;
    private AuthenticationProvider mAuth;
    private MessageProvider messageProvider;
    private ListenerRegistration listener;

    public ChatsAdapter(@NonNull FirestoreRecyclerOptions<Chat> options, Context c) {
        super(options);
        context = c;
        mChatProvider=new ChatsProvider();
        mUserProvider = new UserDatabaseProvider();
        mAuth = new AuthenticationProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Chat model) {
        messageProvider=new MessageProvider();
        final String idUserToChat;
        if (mAuth.getIdCurrentUser().equals(model.getIdUserFrom())) {
            idUserToChat = model.getIdUserTo();
        } else {
            idUserToChat = model.getIdUserFrom();
        }

        mUserProvider.getUser(idUserToChat).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("name")) {
                    holder.binding.nameUserTo.setText(documentSnapshot.get("name").toString());
                }
                if (documentSnapshot.contains("profileImage")) {
                    String urlImage = documentSnapshot.getString("profileImage");
                    Glide.with(context).load(urlImage).apply(Utils.getOptionsGlide(true)).into(holder.binding.imageUserTo);

                }

            } else {
                Log.d(TAG_LOG, "onSuccess: not exist chats");
            }
        });

        holder.binding.getRoot().setOnClickListener(view -> {
            Intent i = new Intent(context, ChatConversationActivity.class);
            i.putExtra("idUserToChat", idUserToChat);
            i.putExtra("idChat",model.getIdChat());
            context.startActivity(i);
        });
        listener=mChatProvider.getChatFromUserToAndUserFrom(model.getIdUserTo(),model.getIdUserFrom()).addSnapshotListener((value, error) -> {
            if(value!=null && !value.isEmpty()){
                messageProvider.getMessage(value.getDocuments().get(0).getString("idLastMessage")).get().addOnSuccessListener(lastMessage -> {
                        if(lastMessage!=null && lastMessage.exists()){
                            String ultimoMensaje=lastMessage.getString("message");
                            holder.binding.lastMessage.setText(ultimoMensaje);
                            if(lastMessage.get("idsUserFrom").equals(mAuth.getIdCurrentUser())){
                                ///check if the message is viewed
                                if (lastMessage.getBoolean("viewed")){
                                    holder.binding.viewed.setColorFilter(ContextCompat.getColor(context,R.color.checkMessage));
                                }else{
                                    holder.binding.viewed.setColorFilter(ContextCompat.getColor(context,R.color.unCheckMessage));
                                }
                                holder.binding.viewed.setVisibility(View.VISIBLE);
                            }else{
                                setCountMessagesNoSee(model, holder);
                                holder.binding.viewed.setVisibility(View.GONE);
                            }
                            holder.binding.timestampLastMessage.setText(RelativeTime.timeFormatAMPM(lastMessage.getLong("timestamp")));
                        }

                });
            }
        });
    }

    private void setCountMessagesNoSee(@NonNull final Chat model, @NonNull final ViewHolder holder) {
        String oppositeId= model.getIdUserTo().equals(mAuth.getIdCurrentUser()) ?
                model.getIdUserFrom() :
                model.getIdUserTo();

        messageProvider.getMessageByChatAndSender(model.getIdChat(),oppositeId).get().addOnSuccessListener(queryDocumentSnapshots -> {

            if(queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots.size()>0){
                holder.binding.countNoSeeMessages.setVisibility(View.VISIBLE);
                holder.binding.countNoSeeMessages.setText(String.valueOf(queryDocumentSnapshots.size()));
            }else if(queryDocumentSnapshots.size()==0){
                holder.binding.countNoSeeMessages.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> Log.e(TAG_LOG, "failed to count number of not see messages  "+e.getMessage() ));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chat_preview, parent, false);
        return new ViewHolder(v);
    }

    public ListenerRegistration getListener() {
        return listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewChatPreviewBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CardviewChatPreviewBinding.bind(itemView);
        }

    }

}
