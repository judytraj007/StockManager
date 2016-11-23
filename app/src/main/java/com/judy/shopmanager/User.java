package com.judy.shopmanager;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class User extends Fragment {

    EditText mEditText;
    EditText text;
    Context con;
    public User() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        con=getActivity();
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProgressBar mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar2);
        mEditText = (EditText) getView().findViewById(R.id.newemail);
        text = (EditText) getView().findViewById(R.id.newPass);
        Button add = (Button) getView().findViewById(R.id.add);
        final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(con,R.style.MyAlertDialogStyle)
                        .setTitle("Add User")
                        .setMessage("Are you sure you want to add this user?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String emailid = mEditText.getText().toString().trim();
                                String password = text.getText().toString().trim();
                                if((!TextUtils.isEmpty(emailid))&&(!TextUtils.isEmpty(password))) {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    mFirebaseAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            mProgressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                mEditText.setText(" ");
                                                text.setText(" ");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                                builder.setMessage("New User Added Succesfully.")
                                                        .setTitle("Success!")
                                                        .setPositiveButton(android.R.string.ok, null);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                                builder.setMessage("User Addition Failed.")
                                                        .setTitle("Error!")
                                                        .setPositiveButton(android.R.string.ok, null);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();

                                            }

                                        }
                                    });
                                }
                                else {
                                    mEditText.setError("Error!");
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
