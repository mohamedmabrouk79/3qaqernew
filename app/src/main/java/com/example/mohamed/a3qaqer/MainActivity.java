package com.example.mohamed.a3qaqer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.SearchManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button Login;
    Context context=MainActivity.this;
    private Button signUp;
    private SearchView mSearchView;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference  mDatabaseReference;
    private ProgressDialog mProgressDialog;
    LocationManager locationManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        mSearchView= (SearchView) findViewById(R.id.search);
        Login= (Button) findViewById(R.id.login);
        signUp= (Button) findViewById(R.id.singup);
        mFirebaseAuth=FirebaseAuth.getInstance();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                       buildAlertMessageNoGps();
                  }
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("تسجيل الدخول ....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("pharmacies");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (CheckConnection.isNetworkAvailableAndConnected(context)&& CheckConnection.isNetworkConnected(context)) {
                    startActivity(ResultSeachActivity.newIntent(context, query));
                }else {
                    Toast.makeText(context, "مفيش نت ", Toast.LENGTH_SHORT).show();
                }
              //  Toast.makeText(MainActivity.this, query+"", Toast.LENGTH_SHORT).show();
                mSearchView.onActionViewCollapsed();
                mSearchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
     Login();
     Singup();
    }

    private void Login(){
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(LoginActivity.newIntent(MainActivity.this));
            }
        });
    }

    private void Singup(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(RegisterActvity.newIntent(MainActivity.this));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser user=mFirebaseAuth.getCurrentUser();
        if (CheckConnection.isNetworkConnected(MainActivity.this) && CheckConnection.isNetworkAvailableAndConnected(MainActivity.this)) {
            if (user != null) {
                mProgressDialog.show();
                DatabaseReference mReference = mDatabaseReference.child(user.getUid());
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        startActivity(PharmacyProfileActivity.newIntent(context,user));
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }else {
            Toast.makeText(MainActivity.this, "مفيش نت ", Toast.LENGTH_SHORT).show();
        }
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
}
