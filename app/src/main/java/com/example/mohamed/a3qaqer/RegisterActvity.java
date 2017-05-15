package com.example.mohamed.a3qaqer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
    private EditText name, password, license, phone, address;
    private Button signup;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    LocationManager locationManager;
    private String loc_string;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RegisterActvity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);
        signup = (Button) findViewById(R.id.sign_up_register);
        name = (EditText) findViewById(R.id.name_edit_text);
        password = (EditText) findViewById(R.id.password_edit);
        phone = (EditText) findViewById(R.id.phone_edit_text);
        license = (EditText) findViewById(R.id.lisence_edit_text);
        address = (EditText) findViewById(R.id.address_edit_text);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("انشاء حساب ......");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pharmacies");
        signUp();
    }

    private void signUp() {
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

                        try {
                            getloc();
                        }catch (NullPointerException f){
                            // if no gps data
                            Toast.makeText(RegisterActvity.this, " هناك عطل فى الgps. اعد المحاوله الان", Toast.LENGTH_SHORT).show();
                        }

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
                                    mReference.child("location_Gps").setValue(loc_string);
                                    mProgressDialog.dismiss();
                                    Toast.makeText(RegisterActvity.this, "تم التسجيل بنجاح ", Toast.LENGTH_SHORT).show();

                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(RegisterActvity.this, "فشل فى انشاء الحساب الرجاء مراجعه رقم التليفون " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                } else {
                    Toast.makeText(RegisterActvity.this, "مفيش نت ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("يفضل فتح ال GPS,هل تريد فتحه؟")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getloc() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            loc_string="none";// indicate that no gps data
            return;
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 2000, 10, new LocationListener() {
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }

            @Override
            public void onLocationChanged(final Location location) {
            }
        });
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            double longitude = myLocation.getLongitude();
            double latitude = myLocation.getLatitude();
            loc_string = latitude + "-" + longitude;

    }
}
