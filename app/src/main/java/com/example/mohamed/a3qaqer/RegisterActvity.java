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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mohamed on 15/05/2017.
 */

public class RegisterActvity extends AppCompatActivity {
    private EditText name,password,license,phone,address;
    private Button signup;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,RegisterActvity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);
        signup= (Button) findViewById(R.id.sign_up_register);
        name= (EditText) findViewById(R.id.name_edit_text);
        password= (EditText) findViewById(R.id.password_edit);
        phone= (EditText) findViewById(R.id.phone_edit_text);
        license= (EditText) findViewById(R.id.lisence_edit_text);
        address= (EditText) findViewById(R.id.address_edit_text);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("انشاء حساب ......");
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("pharmacies");
       signUp();
    }

    private void signUp(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnection.isNetworkAvailableAndConnected(RegisterActvity.this) && CheckConnection.isNetworkConnected(RegisterActvity.this)) {

                    final String mName = name.getText().toString().trim();
                    final String mPhone = phone.getText().toString().trim();
                    final String Address = address.getText().toString().trim();
                    final String Lisance = license.getText().toString().trim();
                    String Password = password.getText().toString().trim();

                    if (TextUtils.isEmpty(mName)) {
                        Toast.makeText(RegisterActvity.this, "ادخل اسم المستخدم !", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(mPhone)) {
                        Toast.makeText(RegisterActvity.this, "ادخل رقم الهاتف !", Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(Address)) {
                        Toast.makeText(RegisterActvity.this, "ادخل العنوان !", Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(Lisance)) {
                        Toast.makeText(RegisterActvity.this, "ادخل رقم الترخيص !", Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(Password)) {
                        Toast.makeText(RegisterActvity.this, "ادخل كلمه المرور ", Toast.LENGTH_SHORT).show();

                    } else {

                        mProgressDialog.show();
                        mFirebaseAuth.createUserWithEmailAndPassword(mPhone + "@3qaqer.com", Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference mReference = mDatabaseReference.child(user.getUid());
                                    mReference.child("address").setValue(Address);
                                    mReference.child("name").setValue(mName);
                                    mReference.child("phone").setValue(mPhone);
                                    mReference.child("location").setValue(Lisance);
                                    mProgressDialog.dismiss();
                                    Toast.makeText(RegisterActvity.this, "تم التسجيل بنجاح ", Toast.LENGTH_SHORT).show();

                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(RegisterActvity.this, "فشل فى انشاء الحساب الرجاء مراجعه رقم التليفون " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }else {
                    Toast.makeText(RegisterActvity.this, "مفيش نت ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
