package com.example.jetcab;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

//There are already two users which were stored in firebase(one rider and one driver)
//email:testforrider@gmail.com
//password:123456
//name:Rider Test1
//phone:5879888888
//rider

//email:testfordriver@gmail.com
//password:654321
//name:Driver Test1
//phone:5879999999
//driver

/**
 * Test class for Profile Activity
 */
public class ProfileTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * The user logs in successfully
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
    }

    /**
     * Check if switch to correct activity for rider
     * check back textview
     */
    @Test
    public void checkRiderPro(){
        solo.enterText((EditText) solo.getView(R.id.login_email), "testforrider@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        TextView Rpro = (TextView) solo.getView(R.id.profileR);
        solo.clickOnView(Rpro);
        solo.assertCurrentActivity("Not in right Activity", Activity_Profile.class);
        TextView back = (TextView) solo.getView(R.id.back);
        solo.clickOnView(back);
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        solo.clickOnButton("SIGNOUT");
        solo.assertCurrentActivity("Not in right Activity", MainActivity.class);
    }

    /**
     * Check rider data correctness
     */
    @Test
    public void checkRProC(){
        solo.enterText((EditText) solo.getView(R.id.login_email), "testforrider@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        TextView Rpro = (TextView) solo.getView(R.id.profileR);
        solo.clickOnView(Rpro);
        solo.assertCurrentActivity("Not in right Activity", Activity_Profile.class);
        assertTrue(solo.waitForText("Rider Test1", 1, 2000));
        assertTrue(solo.waitForText("testforrider@gmail.com", 1, 2000));
        assertTrue(solo.waitForText("5879888888", 1, 2000));
        TextView back = (TextView) solo.getView(R.id.back);
        solo.clickOnView(back);
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        solo.clickOnButton("SIGNOUT");
        solo.assertCurrentActivity("Not in right Activity", MainActivity.class);
    }

    /**
     * Check phone number update
     */
    @Test
    public void checkPhoneUpdate(){
        solo.enterText((EditText) solo.getView(R.id.login_email), "testforrider@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        TextView Rpro = (TextView) solo.getView(R.id.profileR);
        solo.clickOnView(Rpro);
        solo.assertCurrentActivity("Not in right Activity", Activity_Profile.class);
        TextView phone = (TextView) solo.getView(R.id.phone);
        solo.clickOnView(phone);
        solo.clickOnButton("Cancel");
        solo.clickOnView(phone);
        solo.clearEditText(0);
        solo.enterText(0, "5879777777");
        solo.clickOnButton("Save");
        assertTrue(solo.waitForText("5879777777", 1, 2000));
        solo.clickOnView(phone);
        solo.clearEditText(0);
        solo.enterText(0, "5879888888");
        solo.clickOnButton("Save");
        assertTrue(solo.waitForText("5879888888", 1, 2000));
        TextView back = (TextView) solo.getView(R.id.back);
        solo.clickOnView(back);
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        solo.clickOnButton("SIGNOUT");
        solo.assertCurrentActivity("Not in right Activity", MainActivity.class);
    }

    /**
     * Check email update
     */
    @Test
    public void checkEmailUpdate(){
        solo.enterText((EditText) solo.getView(R.id.login_email), "testforrider@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        TextView Rpro = (TextView) solo.getView(R.id.profileR);
        solo.clickOnView(Rpro);
        solo.assertCurrentActivity("Not in right Activity", Activity_Profile.class);
        TextView email = (TextView) solo.getView(R.id.email);
        solo.clickOnView(email);
        solo.clickOnButton("Cancel");
        solo.clickOnView(email);
        solo.clearEditText(0);
        solo.enterText(0, "testforrider111@gmail.com");
        solo.clickOnButton("Save");
        assertTrue(solo.waitForText("testforrider111@gmail.com", 1, 2000));
        solo.clickOnView(email);
        solo.clearEditText(0);
        solo.enterText(0, "testforrider@gmail.com");
        solo.clickOnButton("Save");
        assertTrue(solo.waitForText("testforrider@gmail.com", 1, 2000));
        TextView back = (TextView) solo.getView(R.id.back);
        solo.clickOnView(back);
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuR.class);
        solo.clickOnButton("SIGNOUT");
        solo.assertCurrentActivity("Not in right Activity", MainActivity.class);
    }

    /**
     * Check if switch to correct activity for driver
     */
    @Test
    public void checkDriverPro(){
        solo.enterText((EditText) solo.getView(R.id.login_email), "testfordriver@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "654321");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuD.class);
        TextView Dpro = (TextView) solo.getView(R.id.profileD);
        solo.clickOnView(Dpro);
        solo.assertCurrentActivity("Not in right Activity", Activity_Profile.class);
        TextView back = (TextView) solo.getView(R.id.back);
        solo.clickOnView(back);
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuD.class);
        solo.clickOnButton("SIGNOUT");
        solo.assertCurrentActivity("Not in right Activity", MainActivity.class);
    }



    /**
     * Check data correctness
     */
    @Test
    public void checkDProC(){
        solo.enterText((EditText) solo.getView(R.id.login_email), "testfordriver@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "654321");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuD.class);
        TextView Dpro = (TextView) solo.getView(R.id.profileD);
        solo.clickOnView(Dpro);
        solo.assertCurrentActivity("Not in right Activity", Activity_Profile.class);
        assertTrue(solo.waitForText("Driver Test1", 1, 2000));
        assertTrue(solo.waitForText("testfordriver@gmail.com", 1, 2000));
        assertTrue(solo.waitForText("5879999999", 1, 2000));
        TextView back = (TextView) solo.getView(R.id.back);
        solo.clickOnView(back);
        solo.assertCurrentActivity("Not in right Activity", Activity_MainMenuD.class);
        solo.clickOnButton("SIGNOUT");
        solo.assertCurrentActivity("Not in right Activity", MainActivity.class);
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
