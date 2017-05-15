package com.example.mohamed.a3qaqer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by mohamed on 15/05/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText userName,password;
    private Button login;
    private TextView singup;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;


    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userName= (EditText) findViewById(R.id.user_name);
        password= (EditText) findViewById(R.id.user_password);
        login= (Button) findViewById(R.id.button_login);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("تسجيل الدخول ....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("pharmacies");
        Login();
    }

    private void Login(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnection.isNetworkConnected(LoginActivity.this) && CheckConnection.isNetworkAvailableAndConnected(LoginActivity.this)) {

                    String mUsername = userName.getText().toString().trim();
                    String mPassword = password.getText().toString().trim();
                    if (TextUtils.isEmpty(mUsername)) {

                    } else if (TextUtils.isEmpty(mPassword)) {

                    } else {
                        mProgressDialog.show();
                        mFirebaseAuth.signInWithEmailAndPassword(mUsername + "@3qaqer.com", mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                  mProgressDialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "مفيش نت ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser!=null){
            DatabaseReference mReference=mDatabaseReference.child(currentUser.getUid());
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String,String> map= (Map<String, String>) dataSnapshot.getValue();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
