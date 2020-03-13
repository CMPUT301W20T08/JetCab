package com.example.jetcab;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
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


