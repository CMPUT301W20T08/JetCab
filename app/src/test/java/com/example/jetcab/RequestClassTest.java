package com.example.jetcab;

import com.google.android.gms.maps.model.LatLng;

public class RequestClassTest {

    private Request mockRequest(){
        Request request = new Request(new LatLng(53.521967, -113.511960), new LatLng(53.534191, -113.507541) ,100);
        return request;
    }
}