package com.example.jetcab;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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
 * Test class for Signup Activity
 */
public class SignupTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * The user clicks on signup textview
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Not in Main Activity", MainActivity.class);
        TextView signup = (TextView) solo.getView(R.id.signup);
        solo.clickOnView(signup);
    }

    /**
     * Check whether activity correctly switched
     */
    @Test
    public void checkActivity(){
        solo.assertCurrentActivity("Not in Signup Activity", Activity_Signup.class);
    }

    /**
     * Test if the user enters nothing
     */
    @Test
    public void checkEmpty0() {
        solo.clickOnButton("SIGN UP");
        solo.assertCurrentActivity("Not in Signup Activity", Activity_Signup.class);
    }

    /**
     * Test if the user only enters email
     */
    @Test
    public void checkEmpty1(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "UItest2@gmail.com");
        solo.clickOnButton("SIGN UP");
        solo.assertCurrentActivity("Not in Signup Activity", Activity_Signup.class);
    }

    /**
     * Test if the user only enter email and password
     */
    @Test
    public void checkEmpty2(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "UItest2@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_password), "123456");
        solo.clickOnButton("SIGN UP");
        solo.assertCurrentActivity("Not in Signup Activity", Activity_Signup.class);
    }

    /**
     * Test if the user only enter email, password and username
     */
    @Test
    public void checkEmpty3(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "UItest2@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_password), "123456");
        solo.enterText((EditText) solo.getView(R.id.signup_username), "UI TEST2");
        solo.clickOnButton("SIGN UP");
        solo.assertCurrentActivity("Not in Signup Activity", Activity_Signup.class);
    }

    /**
     * Test if the user does not choose a role
     */
    @Test
    public void checkEmpty4(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "UItest2@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_password), "123456");
        solo.enterText((EditText) solo.getView(R.id.signup_username), "UI TEST2");
        solo.enterText((EditText) solo.getView(R.id.signup_phone), "5879999999");
        solo.clickOnButton("SIGN UP");
        solo.assertCurrentActivity("Not in Signup Activity", Activity_Signup.class);
    }

    /**
     * Test if the user enter a password whose characters is less than 6
     */
    @Test
    public void checkLength(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "UItest2@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_password), "12345");
        solo.clickOnButton("SIGN UP");
        solo.assertCurrentActivity("Not in Signup Activity", Activity_Signup.class);
    }

    /**
     * Test if the user enter email with wrong format
     */
    @Test
    public void checkFormatE(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "com");
        solo.enterText((EditText) solo.getView(R.id.signup_password), "123456");
        solo.enterText((EditText) solo.getView(R.id.signup_username), "UI TESTR");
        solo.enterText((EditText) solo.getView(R.id.signup_phone), "5879999999");
        RadioButton r = (RadioButton) solo.getView(R.id.signup_rider);
        solo.clickOnView(r);
        solo.clickOnButton("SIGN UP");
        solo.sleep(2000);
        solo.assertCurrentActivity("Not in right Activity", Activity_Signup.class);
    }

    /**
     * Test if the user click on signin textview
     */
    @Test
    public void checkLogin(){
        TextView login = (TextView) solo.getView(R.id.login);
        solo.clickOnView(login);
        solo.assertCurrentActivity("Not in right Activity", MainActivity.class);
    }

    /**
     * Email is already used
     */
    @Test
    public void checkSameEmail(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "testforrider@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_password), "123456");
        solo.enterText((EditText) solo.getView(R.id.signup_username), "UI TESTTTT");
        solo.enterText((EditText) solo.getView(R.id.signup_phone), "5879230442");
        RadioButton d = (RadioButton) solo.getView(R.id.signup_driver);
        solo.clickOnView(d);
        solo.clickOnButton("SIGN UP");
        solo.sleep(2000);
        solo.assertCurrentActivity("Not in right Activity", Activity_Signup.class);
    }

    /**
     * Name is already used
     */
    @Test
    public void checkSameName(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), "UItestttt@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_password), "123456");
        solo.enterText((EditText) solo.getView(R.id.signup_username), "Rider Test1");
        solo.enterText((EditText) solo.getView(R.id.signup_phone), "58792303482");
        RadioButton d = (RadioButton) solo.getView(R.id.signup_driver);
        solo.clickOnView(d);
        solo.clickOnButton("SIGN UP");
        solo.sleep(3000);
        solo.assertCurrentActivity("Not in right Activity", Activity_Signup.class);
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
