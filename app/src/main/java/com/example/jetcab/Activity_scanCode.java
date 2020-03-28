package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * @author Yingxin
 * this is scanning screen
 */
public class Activity_scanCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView view;

    /**
     * uses zxing scanner view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("QR Code");
        view = new ZXingScannerView(this);
        setContentView(view);
    }

    /**
     * get the result from the qr code
     * @param result
     */
    @Override
    public void handleResult(Result result) {
        Activity_Scanner.result.setText(result.getText());
        //go back to previous page if get the result
        onBackPressed();
    }

    /**
     * stop camera
     */
    @Override
    protected void onPause() {
        super.onPause();
        view.stopCamera();
    }

    /**
     * start camera
     */
    @Override
    protected void onResume(){
        super.onResume();
        view.setResultHandler(this);
        view.startCamera();
    }
}
