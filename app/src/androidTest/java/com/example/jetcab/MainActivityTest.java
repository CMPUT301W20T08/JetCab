package com.example.jetcab;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo( InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void DriverLogin()
    {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText ( (EditText)solo.getView ( R.id.login_email ),"JetCabDriver@JetCab.com" );
        solo.enterText ( (EditText)solo.getView ( R.id.login_password ),"JetCabDriver" );
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity ( "WrongActivity", MainMenuD.class);
        solo.clickOnButton ( "SIGNOUT" );
        solo.assertCurrentActivity ( "Wrong Activity",MainActivity.class );

    }

    @Test
    public void RiderLogin()
    {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText ( (EditText)solo.getView ( R.id.login_email ),"JetCabRider@JetCab.com" );
        solo.enterText ( (EditText)solo.getView ( R.id.login_password ),"JetCabRider" );
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity ( "WrongActivity", MainMenuR.class);
        solo.clickOnButton ( "SIGNOUT" );
        solo.assertCurrentActivity ( "Wrong Activity",MainActivity.class );

    }

    @Test
    public void SearchRequest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText ( (EditText)solo.getView ( R.id.login_email ),"JetCabDriver@JetCab.com" );
        solo.enterText ( (EditText)solo.getView ( R.id.login_password ),"JetCabDriver" );
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity ( "WrongActivity", MainMenuD.class);
        new Request ( new LatLng (53.518882, -113.453807 ),new LatLng (53.538882, -113.463807 ),200 );
        solo.clickOnButton ( 0);
        solo.assertCurrentActivity ( "Wrong Activity", Driver_Search_Request.class );
        solo.searchText ( "Request# 1",1 );
        solo.goBack ();
        solo.assertCurrentActivity ( "WrongActivity", MainMenuD.class);
        solo.clickOnButton ( "SIGNOUT" );
        solo.assertCurrentActivity ( "Wrong Activity",MainActivity.class );
    }

    @Test
    public void AcceptRequest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText ( (EditText)solo.getView ( R.id.login_email ),"JetCabDriver@JetCab.com" );
        solo.enterText ( (EditText)solo.getView ( R.id.login_password ),"JetCabDriver" );
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity ( "WrongActivity", MainMenuD.class);
        solo.clickOnButton ( 0);
        solo.assertCurrentActivity ( "Wrong Activity", Driver_Search_Request.class );
        solo.searchText ( "Request# 1",1 );
        solo.clickOnText ( "Request# 1" );
        solo.clickOnButton ( "ACCEPT" );
        solo.assertCurrentActivity ( "WrongActivity", MainMenuD.class);
        solo.clickOnButton ( "SIGNOUT" );
        solo.assertCurrentActivity ( "Wrong Activity",MainActivity.class );
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}


