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

//There is already a user which was stored in firebase
//email:testforrider@gmail.com
//password:123456
//name:Rider Test1
//phone:5879888888
//rider

/**
 *Test class for MainActivity (login)
 */

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


    /**
     * Test if the user does not enter email and password
     */
    @Test
    public void checkAE() {
        //Asserts that the current activity is the MainActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
    }

    /**
     * Test if the user only enters email
     */
    @Test
    public void checkPE() {
        solo.enterText((EditText) solo.getView(R.id.login_email), "UItest1@gmail.com");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
    }

    /**
     * Test if the user only enters password
     */
    @Test
    public void checkEE() {
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
    }

    /**
     * Test if there is no such user
     */
    @Test
    public void checkNoUser() {
        solo.enterText((EditText) solo.getView(R.id.login_email), "Nousersxxx@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("LOGIN");
        solo.sleep(2000);
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
    }

    /**
     * Test if the password is wrong
     */
    @Test
    public void checkWrongP(){
        solo.enterText((EditText) solo.getView(R.id.login_email), "testforrider@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "abcdef");
        solo.clickOnButton("LOGIN");
        solo.sleep(2000);
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
    }


    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

        solo.finishOpenedActivities();
    }
}


