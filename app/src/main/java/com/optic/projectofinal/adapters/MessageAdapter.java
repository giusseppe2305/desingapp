package com.optic.projectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.CardviewMessageFromMeBinding;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.RelativeTime;


public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.ViewHolder> {
    private Context context;
    private UserDatabaseProvider mUserProvider;
    private AuthenticationProvider mAuth;

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Context c) {
        super(options);
        context = c;
        mUserProvider = new UserDatabaseProvider();
        mAuth = new AuthenticationProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Message message) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String messageId = document.getId();
        String relativeTime = RelativeTime.timeFormatAMPM(message.getTimestamp());


        if (message.getIdsUserFrom().equals(mAuth.getIdCurrentUser())) {
            holder.binding.messageFromMe.setText(message.getMessage());
            holder.binding.timestampFromMe.setText(relativeTime);

            holder.binding.layoutMessageFromMe.setVisibility(View.VISIBLE);
            holder.binding.layoutMessageToMe.setVisibility(View.GONE);
            if (message.isViewed()) {
                holder.binding.doubleCheck.setImageResource(R.drawable.ic_double_check);
            } else {
                holder.binding.doubleCheck.setImageResource(R.drawable.ic_add_photo);
            }
        } else {
            holder.binding.messageToMe.setText(message.getMessage());
            holder.binding.timestampToMe.setText(relativeTime);


            holder.binding.layoutMessageFromMe.setVisibility(View.GONE);
            holder.binding.layoutMessageToMe.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message_from_me, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewMessageFromMeBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = CardviewMessageFromMeBinding.bind(view);
        }

    }

}
