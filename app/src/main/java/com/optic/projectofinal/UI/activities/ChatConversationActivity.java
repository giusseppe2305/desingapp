package com.optic.projectofinal.UI.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.MessageAdapterFirebase;
import com.optic.projectofinal.databinding.ActivityChatConversationBinding;
import com.optic.projectofinal.models.Chat;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.modelsNotification.NotificationMessageDTO;
import com.optic.projectofinal.modelsNotification.WrapperNotification;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.ChatsProvider;
import com.optic.projectofinal.providers.MessageProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.RelativeTime;
import com.optic.projectofinal.utils.Utils;
import com.optic.projectofinal.utils.UtilsRetrofit;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.optic.projectofinal.channel.NotificationHelper.TYPE_NOTIFICATION.MESSAGE_CHAT;
import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class ChatConversationActivity extends AppCompatActivity {

    private ActivityChatConversationBinding binding;
    private String idCurrentChat;
    private String idUserToChat;
    private AuthenticationProvider mAuth;
    private ChatsProvider mChatsProvider;
    private UserDatabaseProvider mUserProvider;
    private MessageProvider mMessageProvider;
    private MessageAdapterFirebase mAdapterMessage;
    private LinearLayoutManager linearLayoutManager;
    private ListenerRegistration listeningChangeDataToolbar;
    private Boolean nonExistChat;
    private boolean isTyping = false;
    private ListenerRegistration listeningChangeTyping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar.toolbarChat);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserProvider = new UserDatabaseProvider();
        mChatsProvider = new ChatsProvider();
        mAuth = new AuthenticationProvider();
        mMessageProvider = new MessageProvider();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.listMessages.setLayoutManager(linearLayoutManager);

        idUserToChat = getIntent().getStringExtra("idUserToChat");
        idCurrentChat = getIntent().getStringExtra("idChat");

        binding.editTextMessageChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isTyping) {
                    Log.d(TAG_LOG, "start typing");
                    mChatsProvider.updateIsWritting(idCurrentChat, mAuth.getIdCurrentUser(), true);
                    isTyping = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        RxTextView.textChanges(binding.editTextMessageChat)
                .debounce(2, TimeUnit.SECONDS)
                .subscribe(textChanged -> {
                    if (isTyping) {
                        Log.d(TAG_LOG, "typing end ");
                        mChatsProvider.updateIsWritting(idCurrentChat, mAuth.getIdCurrentUser(), false);
                        isTyping = false;
                    }
                });

        binding.btnSendMessageChat.setOnClickListener(view -> {
            if (idCurrentChat != null && !binding.editTextMessageChat.getText().toString().isEmpty()) {
                if (nonExistChat != null && nonExistChat) {
                    nonExistChat = false;
                }

                final Message msg = new Message();
                msg.setIdChat(idCurrentChat);
                msg.setIdsUserFrom(mAuth.getIdCurrentUser());
                msg.setIdUserTo(idUserToChat);
                msg.setTimestamp(new Date().getTime());
                msg.setViewed(false);
                msg.setMessage(binding.editTextMessageChat.getText().toString());
                mMessageProvider.create(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            //send notification
                            sendNotification(msg);
                        } else {
                            Log.d(TAG_LOG, "fail to send message");
                        }
                    }
                });
                ///diremos que est es el ultimo mensaje del chat
                mChatsProvider.updateLastMessageOnChat(idCurrentChat, msg.getId(), ChatConversationActivity.this);
                binding.editTextMessageChat.setText("");
                mAdapterMessage.notifyDataSetChanged();


            } else {
                Toast.makeText(ChatConversationActivity.this, R.string.chat_conversation_field_message_void, Toast.LENGTH_SHORT).show();
            }

        });

        Log.d(TAG_LOG, "onCreate: before "+idCurrentChat);
        if (idCurrentChat == null) {
            Log.d(TAG_LOG, "onCreate: in condition "+idCurrentChat);
            checkifChatExistAndCreate();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (nonExistChat != null && nonExistChat) {

            ///delete chat because dont wrote any message
            mChatsProvider.deleteChat(idCurrentChat).addOnFailureListener(v -> Log.e(TAG_LOG, "onBackPressed: " + v.getMessage()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(idCurrentChat!=null){
            getAllMessages();
            loadDataToolbar();
        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getAllMessages() {
        Query query = mMessageProvider.getMessageByChat(idCurrentChat);
        FirestoreRecyclerOptions<Message> options =
                new FirestoreRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();
        mAdapterMessage = new MessageAdapterFirebase(options, ChatConversationActivity.this);
        binding.listMessages.setAdapter(mAdapterMessage);
        mAdapterMessage.startListening();
        mAdapterMessage.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateViewed();

                int numberMessages = mAdapterMessage.getItemCount();
                int lastMessagePosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastMessagePosition == -1 || (positionStart >= (numberMessages - 1) && lastMessagePosition == (positionStart - 1))) {
                    binding.listMessages.scrollToPosition(positionStart);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapterMessage.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listeningChangeDataToolbar != null) {
            listeningChangeDataToolbar.remove();
        }
        if (listeningChangeTyping != null) {
            listeningChangeTyping.remove();
        }
        if (nonExistChat != null && nonExistChat) {

            ///delete chat because dont wrote any message
            mChatsProvider.deleteChat(idCurrentChat).addOnFailureListener(v -> Log.e(TAG_LOG, "onBackPressed: " + v.getMessage()));
        }
    }

    private void loadDataToolbar() {

        listeningChangeTyping = mChatsProvider.getChatById(idCurrentChat).addSnapshotListener((value, error) -> {
            if (value.exists()) {
                Chat it = value.toObject(Chat.class);
                if (it.getIsTyping() != null && it.getIsTyping().size() > 0) {

                    if (it.getIsTyping().contains(idUserToChat)) {
                        binding.toolbar.statusUserToToolbar.setVisibility(View.GONE);
                        binding.toolbar.isTyping.setVisibility(View.VISIBLE);
                    }
                } else {

                    binding.toolbar.isTyping.setVisibility(View.GONE);
                    binding.toolbar.statusUserToToolbar.setVisibility(View.VISIBLE);
                }
            }

        });

        mUserProvider.getUser(idUserToChat).addOnSuccessListener(documentSnapshot -> {

            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("name")) {
                    binding.toolbar.nameUserToToolbar.setText(documentSnapshot.getString("name"));
                }
                if (documentSnapshot.contains("online") && documentSnapshot.contains("lastConnection")) {
                    listeningChangeDataToolbar = mUserProvider.getIsOnlineUser(idUserToChat).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.getBoolean("online")) {
                                binding.toolbar.statusUserToToolbar.setText(getString(R.string.chat_converstation_connected));
                            } else {
                                binding.toolbar.statusUserToToolbar.setText(RelativeTime.getStringForlastConnection(ChatConversationActivity.this,
                                        value.getLong("lastConnection")));
                            }
                        }
                    });
                }
                if (documentSnapshot.contains("profileImage")) {
                    String urlImage = documentSnapshot.getString("profileImage");
                    if (urlImage != null) {
                        Glide.with(ChatConversationActivity.this).load(urlImage)
                                .apply(Utils.getOptionsGlide(false)).into(binding.toolbar.photoUserToToolbar);
                    }
                }

            } else {
                Log.d(TAG_LOG, "fail to load data toolbar");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG_LOG, "fail to get user to load  toolbar data");
            }
        });
    }

    private void checkifChatExistAndCreate() {
        mChatsProvider.getChatFromUserToAndUserFrom(idUserToChat, mAuth.getIdCurrentUser()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.size() == 0) {
                ///no existe un chat con las comibanciones de los id entonces la creamos
                String idChat = mAuth.getIdCurrentUser() + idUserToChat;
                createChat(idChat);
                nonExistChat = true;
            } else {
                //existe el chat
                idCurrentChat = queryDocumentSnapshots.getDocuments().get(0).getString("idChat");
            }
            Log.d(TAG_LOG, "onSuccess: crea si no existe");

            onStart();


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG_LOG, "fail to enter chat");
                finish();
            }
        });
    }

    public void updateViewed() {
        mMessageProvider.getMessageByChatAndSender(idCurrentChat, idUserToChat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot i : queryDocumentSnapshots.getDocuments()) {
                    String idDocumentIterated = i.getId();
                    mMessageProvider.updateViewd(idDocumentIterated);
                }
            }
        }).addOnFailureListener(e -> Log.d(TAG_LOG, "fail to set seen message "));
    }

    private void createChat(String idChat) {
        idCurrentChat = idChat;

        Chat chat = new Chat();
        chat.setIdChat(idChat);
        chat.setIdUserTo(idUserToChat);
        chat.setIdUserFrom(mAuth.getIdCurrentUser());
        chat.setTimestamp(new Date().getTime());
        String[] idChats = new String[]{chat.getIdUserFrom(), chat.getIdUserTo()};
        chat.setIdsChats(Arrays.asList(idChats));

        mChatsProvider.create(chat);
    }

    public void sendNotification(Message model) {
        if (idUserToChat == null) {
            return;
        }
        new UserDatabaseProvider().getUser(mAuth.getIdCurrentUser()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO(getString(R.string.notification_new_message), MESSAGE_CHAT, model.getMessage());
                notificationMessageDTO.setIdChat(model.getIdChat());
                notificationMessageDTO.setNameUser(documentSnapshot.getString("name") + " " + documentSnapshot.getString("lastName"));
                notificationMessageDTO.setPhotoProfile(documentSnapshot.getString("profileImage"));
                notificationMessageDTO.setIdUserToChat(idUserToChat);

                String code = model.getIdsUserFrom().substring(model.getIdsUserFrom().length() - 3);
                notificationMessageDTO.setIdNotification(UtilsRetrofit.stringToInt(code));
                WrapperNotification<NotificationMessageDTO> wrapperNotification = new WrapperNotification<>(notificationMessageDTO);
                UtilsRetrofit.sendNotificationMessage(wrapperNotification, false);
            }

        });

    }
}