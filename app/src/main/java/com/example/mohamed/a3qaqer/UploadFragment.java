package com.example.mohamed.a3qaqer;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by mohamed on 15/05/2017.
 */

public class UploadFragment extends DialogFragment {
     private EditText drug_name,drugType,tarzez;
     private ImageView upload,take;
     private Button uploadButton;
    private String mUri=null;
    private AlertDialog.Builder mBuilder;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private static FirebaseUser mUser;

    public static UploadFragment newFragment(FirebaseUser user){
        mUser=user;
        return  new UploadFragment();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.add,null);
        drug_name= (EditText) view.findViewById(R.id.input_name);
        drugType= (EditText) view.findViewById(R.id.input_type);
        tarzez= (EditText) view.findViewById(R.id.input_tarkeez);
        upload= (ImageView) view.findViewById(R.id.upload);
        take= (ImageView) view.findViewById(R.id.take_photo);
        uploadButton= (Button) view.findViewById(R.id.upload_button);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("drugs");
        mStorageReference= FirebaseStorage.getInstance().getReference();
        UploadDrug();
        UploadPhoto();
        mBuilder=new AlertDialog.Builder(getActivity());
        mBuilder.setView(view).create();
        return mBuilder.create();

    }

    private void UploadPhoto(){
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0 && resultCode==getActivity().RESULT_OK){
            Uri uri=data.getData();
            StorageReference mStorageReference1=mStorageReference.child("blogs_images").child(uri.getLastPathSegment());
                   mStorageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           mUri= String.valueOf(taskSnapshot.getDownloadUrl());

                       }
                   });

        }


    }

    public void UploadDrug(){
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=drug_name.getText().toString();
                String type=drugType.getText().toString();
                String Tarkez=tarzez.getText().toString();
                if (TextUtils.isEmpty(name)){

                }else if(TextUtils.isEmpty(type)){

                }else if (TextUtils.isEmpty(Tarkez)){

                }else {
                    DatabaseReference mReference=mDatabaseReference.child(name);
                    mReference.child("name").setValue(name);
                    mReference.child("type").setValue(type);
                    mReference.child("photo_uri").setValue(String.valueOf(mUri));
                    mReference.child("tarkez").setValue(Tarkez);
                    DatabaseReference reference=mReference.child("pharmacy").push();
                    reference.setValue(mUser.getUid());
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("pharmacies").child(mUser.getUid()).child("Drugs");
                    reference1.child(name).setValue(name);
                }

            }
        });
    }
}
