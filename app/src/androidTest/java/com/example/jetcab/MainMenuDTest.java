package com.example.jetcab;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//There is already a user which was stored in firebase
//email:testfordriver@gmail.com
//password:654321
//name:Driver Test1
//phone:5879999999
//driver

/**
 * Test class for MainMenuD Activity
 */
public class MainMenuDTest {
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
        solo.enterText((EditText) solo.getView(R.id.login_email), "testfordriver@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "654321");
        solo.clickOnButton("LOGIN");
    }

    /**
     * Check whether activity correctly switched
     * Go to MainActivity after logout
     */
    @Test
    public void checkActivity(){
        solo.assertCurrentActivity("Not in correct Activity", Activity_MainMenuD.class);
        ImageButton bd = (ImageButton) solo.getView(R.id.logout_d_image_button);
        solo.clickOnView(bd);
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
