package com.example.jetcab;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Charles
 * test class to see:
 * request is deleted from firebase after clicking yes on the fragment
 * accepted request is deleted otherwise if no unaccepted request is there
 */
public class CancelRequestFragmentTest {
    private Solo solo;
    private Activity_Request mockRequest(){ // from RequestClassTest
        Activity_Request request = new Activity_Request(new LatLng(53.521967, -113.511960), new LatLng(53.534191, -113.507541) ,100);
        return request;
    }

    @Rule
    public ActivityTestRule<CurrentRequest> rule
            = new ActivityTestRule<>(CurrentRequest.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     * post a request then remove with the fragment
     */
    public void checkRequestCancel() throws Exception {
        // TO DO
        // post a request
        // check firebase for existence of the request
        // press cancel button and confirm
        // check firebase that request has been removed

    }

    public void checkAcceptedRequestCancel() throws Exception {
        // TO DO
        // post a request
        // check firebase for existence of the accepted request
        // press cancel button and confirm
        // check firebase that the accepted request has been removed

    }
}
