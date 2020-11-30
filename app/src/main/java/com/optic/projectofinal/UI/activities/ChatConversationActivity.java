package com.optic.projectofinal.UI.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.adapters.MessageAdapter;
import com.optic.projectofinal.databinding.ActivityChatConversationBinding;
import com.optic.projectofinal.models.Chat;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.ChatsProvider;
import com.optic.projectofinal.providers.MessageProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;

import java.util.Arrays;
import java.util.Date;

public class ChatConversationActivity extends AppCompatActivity {
    private ActivityChatConversationBinding binding;
    private String idCurrentChat;
    private String idUserToChat;
    private AuthenticationProvider mAuth;
    private ChatsProvider mChatsProvider;
    private UserDatabaseProvider mUserProvider;
    private MessageProvider mMessageProvider;
    private MessageAdapter mAdapterMessage;
    private LinearLayoutManager linearLayoutManager;

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

        binding.btnSendMessageChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idCurrentChat != null && !binding.editTextMessageChat.getText().toString().isEmpty()) {
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
                               // sendNotification(msg);
                            } else {
                                Toast.makeText(ChatConversationActivity.this, "Fallo al subir mensaje", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    ///diremos que est es el ultimo mensaje del chat
                    mChatsProvider.updateLastMessageOnChat(idCurrentChat, msg.getId(), ChatConversationActivity.this);

                    Toast.makeText(ChatConversationActivity.this, "Se envio el mensaje", Toast.LENGTH_SHORT).show();
                    binding.editTextMessageChat.setText("");
                    mAdapterMessage.notifyDataSetChanged();
                    //enviar notifiacion


                } else {
                    Toast.makeText(ChatConversationActivity.this, "Escriba un mensaje antes de enviar o idchat", Toast.LENGTH_SHORT).show();
                }

            }
        });


        if (idCurrentChat == null) {
            checkifChatExistAndCreate();
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        getAllMessages();
    }

    private void getAllMessages() {
        Query query = mMessageProvider.getMessageByChat(idCurrentChat);
        FirestoreRecyclerOptions<Message> options =
                new FirestoreRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();
        mAdapterMessage = new MessageAdapter(options, ChatConversationActivity.this);
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
//        if (escuchaConstanteCambioStatusConnectionUser != null) {
//            escuchaConstanteCambioStatusConnectionUser.remove();
//        }
    }

    private void checkifChatExistAndCreate() {
        mChatsProvider.getChatFromUserToAndUserFrom(idUserToChat, mAuth.getIdCurrentUser()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() == 0) {
                    ///no existe un chat con las comibanciones de los id entonces la creamos
                    createChat();
                    onPause();
                    onStart();

                } else {
                    //existe el chat
                    System.out.println("este el id del chat que existe:::" + queryDocumentSnapshots.getDocuments().get(0).getString("idChat"));
                    idCurrentChat = queryDocumentSnapshots.getDocuments().get(0).getString("idChat");
                    onPause();
                    onStart();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatConversationActivity.this, "Error al entrar al chat", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void updateViewed() {
        mMessageProvider.getMessageByChatAndSender(idCurrentChat, idUserToChat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                System.out.println(idCurrentChat + "---" + idUserToChat);
                System.out.println("total mensajes " + queryDocumentSnapshots.size());
                for (DocumentSnapshot i : queryDocumentSnapshots.getDocuments()) {
                    String idDocumentIterated = i.getId();
                    System.out.println("id mensaje iterado " + idDocumentIterated);
                    mMessageProvider.updateViewd(idDocumentIterated);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatConversationActivity.this, "Fallo al marcar el visto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createChat() {
        String idChat = mAuth.getIdCurrentUser() + idUserToChat;
        idCurrentChat = idChat;

        Chat chat = new Chat();
        chat.setIdChat(idChat);
        chat.setIdUserTo(idUserToChat);
        chat.setIdUserFrom(mAuth.getIdCurrentUser());
        chat.setWritting(false);
        chat.setTimestamp(new Date().getTime());
        String[] idChats = new String[]{chat.getIdUserFrom(), chat.getIdUserTo()};
        chat.setIdsChats(Arrays.asList(idChats));

        mChatsProvider.create(chat);
    }
}