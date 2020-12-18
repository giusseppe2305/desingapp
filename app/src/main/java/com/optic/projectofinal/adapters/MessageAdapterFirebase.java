package com.optic.projectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.CardviewMessageFromMeBinding;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.utils.RelativeTime;


public class MessageAdapterFirebase extends FirestoreRecyclerAdapter<Message, MessageAdapterFirebase.ViewHolder> {
    private Context context;
    private AuthenticationProvider mAuth;

    public MessageAdapterFirebase(@NonNull FirestoreRecyclerOptions<Message> options, Context c) {
        super(options);
        context = c;
        mAuth = new AuthenticationProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Message message) {
        DocumentSnapshot compareDocument;


        if ((position == 0 && getSnapshots().size() > 0) ||
                getSnapshots().size() == 1) {
            holder.binding.date.setVisibility(View.VISIBLE);
            holder.binding.textDate.setText(RelativeTime.getTittleDate(context, message.getTimestamp()));
        } else if (position < getSnapshots().size() ) {
            if (position == 0) {

                compareDocument = getSnapshots().getSnapshot(position + 1);

            } else {
                compareDocument = getSnapshots().getSnapshot(position - 1);

            }

            Long compare = compareDocument.getLong("timestamp");
            if (RelativeTime.compare(compare, message.getTimestamp()) == 1) {
                holder.binding.date.setVisibility(View.VISIBLE);
                holder.binding.textDate.setText(RelativeTime.getTittleDate(context, message.getTimestamp()));
            } else {
                holder.binding.date.setVisibility(View.GONE);

            }

        } else {
            holder.binding.date.setVisibility(View.GONE);

        }


        String relativeTime = RelativeTime.getHourPM(message.getTimestamp());

        if (message.getIdsUserFrom().equals(mAuth.getIdCurrentUser())) {
            holder.binding.messageFromMe.setText(message.getMessage());
            holder.binding.timestampFromMe.setText(relativeTime);

            holder.binding.layoutMessageFromMe.setVisibility(View.VISIBLE);
            holder.binding.layoutMessageToMe.setVisibility(View.GONE);
            if (message.isViewed())
                holder.binding.doubleCheck.setColorFilter(ContextCompat.getColor(context, R.color.checkMessage));
            else
                holder.binding.doubleCheck.setColorFilter(ContextCompat.getColor(context, R.color.unCheckMessage));
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
