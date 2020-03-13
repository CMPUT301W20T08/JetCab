package com.example.jetcab;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author chirag
 * This class helps to create a custom array adaptor
 */
public class CustomList extends ArrayAdapter< String[] >  {

    //attributes
    private ArrayList<String []> requestdetails;
    private Context context;

    /**
     * Initalizes class varibles
     * @param context
     * @param requestdetails
     */

    public CustomList(Context context, ArrayList<String []> requestdetails)
    {
        super(context,0, requestdetails );
        this.context=context;
        this.requestdetails=requestdetails;
    }

    /**
     * Inflate the view to content.xml
     * Fills the layout attributes with data
     *
     * geocoder helps to get the address from Latitude and Longitude
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view;
        view = convertView;

        if (view == null){
            view= LayoutInflater.from(context).inflate(R.layout.content,parent,false);
        }

        String details [] =requestdetails.get ( position );
        String address;
        String coordinates []=details[1].split ( "," );
        TextView pickUpLocation= view.findViewById(R.id.Pickup_location);
        TextView fare= view.findViewById(R.id.fare_text);
        TextView requestno=view.findViewById ( R.id.Requestno );

        requestno.setText ( "Request# "+(position+1));
        Geocoder geocoder;
        geocoder = new Geocoder(view.getContext (), Locale.getDefault());

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(Double.valueOf ( coordinates[0] ), Double.valueOf  ( coordinates[1] ), 1);
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                int count = 0;
                while ( count < addresses.size()) {
                    address=addresses.get(count).getAddressLine(0);
                    count++;
                }
            pickUpLocation.setText ("Pickup: "+address);
            }

        } catch (IOException e) {
            e.printStackTrace ( );
        }
        fare.setText ( "Fare: $"+ details[2] );


        return view;
    }

}
