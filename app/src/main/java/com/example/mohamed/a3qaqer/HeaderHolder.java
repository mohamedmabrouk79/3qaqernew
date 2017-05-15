package com.example.mohamed.a3qaqer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by mohamed on 4/24/17.
 */

public class HeaderHolder extends RecyclerView.ViewHolder {
    private KenBurnsView mPharmacyImage;
    private TextView mPharmacyName;
    private TextView mPhamarcyAddress;
    private TextView mPharmacyPhone;
    public HeaderHolder(View itemView) {
        super(itemView);
        mPharmacyImage= (KenBurnsView) itemView.findViewById(R.id.phamacy_image_view);
        mPharmacyName= (TextView) itemView.findViewById(R.id.pharmacy_name_header);
        mPhamarcyAddress= (TextView) itemView.findViewById(R.id.pharmacy_address_profile);
        mPharmacyPhone= (TextView) itemView.findViewById(R.id.phamacy_phone_profile);
    }

    public void bindData(Pharmacy pharmacy){
        mPharmacyName.setText(pharmacy.getName());
        mPharmacyPhone.setText(pharmacy.getPhone());
        mPhamarcyAddress.setText(pharmacy.getLoction());
    }
}
