package com.judy.shopmanager;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountSettings extends Fragment {


    public AccountSettings() {

    }

    EditText newPass;
   EditText newId;
    String id,passW;
    Context con;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        con=getActivity();
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProgressBar spinner = (ProgressBar) getView().findViewById(R.id.spinner);
        newId = (EditText) getView().findViewById(R.id.newId);
         newPass = (EditText) getView().findViewById(R.id.newPass);
        Button email = (Button) getView().findViewById(R.id.doneEm);
        Button pass = (Button) getView().findViewById(R.id.donePass);
        Button delete = (Button) getView().findViewById(R.id.doneDelete);
        Button logOut=(Button)getView().findViewById(R.id.logOut);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=newId.getText().toString().trim();
                if(TextUtils.isEmpty(id)){
                    newId.setError(getString(R.string.required_field));
                }
                else {

                        spinner.setVisibility(View.VISIBLE);
                        user.updateEmail(id)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        spinner.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            newId.setText(" ");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                            builder.setMessage("Email Changed Successfully.")
                                                    .setTitle("Success!")
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            SharedPreferences sp=getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor e=sp.edit();
                                            e.clear();
                                            e.commit();
                                            Intent i=new Intent(getActivity(),LoginActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);


                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                            builder.setMessage("Email Change Failed.")
                                                    .setTitle("Error!")
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();

                                        }
                                    }
                                });
                }
            }

        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passW=newPass.getText().toString().trim();
                if(TextUtils.isEmpty(passW)) {
                    newPass.setError("Invalid!");
                }
                else
                {
                    spinner.setVisibility(View.VISIBLE);
                    user.updatePassword(passW)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    spinner.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        newPass.setText(" ");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                        builder.setMessage("Password Changed Successfully.")
                                                .setTitle("Success!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        SharedPreferences sp=getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor e=sp.edit();
                                        e.clear();
                                        e.commit();
                                        Intent i=new Intent(getActivity(),LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                        builder.setMessage("Password Change Failed.")
                                                .setTitle("Error!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }

                                }
                            });
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b=new AlertDialog.Builder(con,R.style.MyAlertDialogStyle)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               spinner.setVisibility(View.VISIBLE);
                                if (user != null) {
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                       spinner.setVisibility(View.GONE);
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                                        builder.setMessage("Account Deleted.")
                                                                .setTitle("Success!")
                                                                .setPositiveButton(android.R.string.ok, null);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                        SharedPreferences sp=getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor e=sp.edit();
                                                        e.clear();
                                                        e.commit();
                                                        Intent i=new Intent(getActivity(),LoginActivity.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);


                                                    } else {
                                                        spinner.setVisibility(View.GONE);
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                                        builder.setMessage("Account Deletion Failed.")
                                                                .setTitle("Error!")
                                                                .setPositiveButton(android.R.string.ok, null);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                b.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog d=b.create();
                d.show();

            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor e=sp.edit();
                e.clear();
                e.commit();
                Intent i=new Intent(getActivity(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }
}
