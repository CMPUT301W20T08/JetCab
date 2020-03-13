package com.example.jetcab;

import android.app.Activity;

import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for PostActivity. All the UI tests are written here. Robotium test framework is used
 */
public class RiderMenuIntentPostRequest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainMenuR> rule =
            new ActivityTestRule<>(MainMenuR.class, true, true);

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
     * check whether the PostRequest activity is correctly switched
     */
    @Test
    public void checkSwitchActivity() {
        solo.assertCurrentActivity("Wrong Activity", MainMenuR.class);
        solo.clickOnText("Post Requests");

        solo.waitForActivity("PostRequest");
        solo.assertCurrentActivity("Wrong Activity", PostRequest.class);
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}