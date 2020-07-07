package com.example.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView logout,passwordchange,account;
    View view;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_profile);
//
//
//        logout = findViewById(R.id.logout);
//
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (v.getId() == R.id.logout) {
//                    mFirebaseAuth.getInstance().signOut();
//                    Intent i = new Intent(Profile.this,MainActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//                }
//            }
//        });
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile,container,false);
        logout = view.findViewById(R.id.logout);
        passwordchange = view.findViewById(R.id.passwordchange);
        account = view.findViewById(R.id.Account);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(Profile.this.getActivity(),MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
            }
        });
        passwordchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment();
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentAccount();
            }
        });
        return view;
    }
    public void changeFragment(){
        Fragment fg = new ChangePassword();
        FragmentChange fc = (FragmentChange)getActivity();
        fc.replacefragment(fg);
    }
    public void changeFragmentAccount(){
        Fragment fg = new AccountInfo();
        FragmentChange fc = (FragmentChange)getActivity();
        fc.replacefragment(fg);
    }
}
