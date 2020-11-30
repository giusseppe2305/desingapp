package com.optic.projectofinal.UI.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.optic.projectofinal.adapters.ChatsAdapter;
import com.optic.projectofinal.databinding.FragmentChatsBinding;
import com.optic.projectofinal.models.Chat;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.ChatsProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {
    private FragmentChatsBinding binding;
    private ChatsProvider chatProvider;
    private AuthenticationProvider mAuth;
    private ChatsAdapter chatAdapter;

    public ChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatsFragment newInstance(String param1, String param2) {
        ChatsFragment fragment = new ChatsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding= FragmentChatsBinding.inflate(inflater, container, false);
        chatProvider=new ChatsProvider();
        mAuth=new AuthenticationProvider();
        binding.listChats.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        final Query query=chatProvider.getAllChatsFromUser(mAuth.getIdCurrentUser());///comprobar no sea nulo
        if(query!=null){
            FirestoreRecyclerOptions<Chat> options= new FirestoreRecyclerOptions.Builder<Chat>().setQuery(query,Chat.class).build();
            chatAdapter=new ChatsAdapter(options,getContext());
            binding.listChats.setAdapter(chatAdapter);
            chatAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null){
            chatAdapter.stopListening();
        }
    }
}