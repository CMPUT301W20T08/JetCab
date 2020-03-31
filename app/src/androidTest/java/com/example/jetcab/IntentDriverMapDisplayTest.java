package com.example.jetcab;

import android.app.Activity;
import android.media.Image;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Joyce
 * Test class for switching from MainMenuD to DriverMapDisplay activity.
 * All the UI tests are written here. Robotium test framework is used
 * !!!!!!!!!!!!!!!!!!!!!!!!The test is valid only if the requests database is not null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class IntentDriverMapDisplayTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Activity_MainMenuD> rule =
            new ActivityTestRule<>(Activity_MainMenuD.class, true, true);

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
     * check whether the DriverCurrentRequest activity is correctly switched
     */
    @Test
    public void checkSwitchActivity() {
        solo.assertCurrentActivity("Wrong Activity", Activity_MainMenuD.class);
        solo.clickOnText("Search Requests");
        solo.waitForActivity("Accept Req");
        solo.assertCurrentActivity("Wrong Activity", Driver_Search_Request.class);
        TextView req = (TextView) solo.getView(R.id.Requestno);
        solo.clickOnView(req);
        solo.waitForActivity("Specific Req");
        solo.assertCurrentActivity("Wrong Activity", AcceptRequest.class);

        ImageButton map_icon = (ImageButton) solo.getView(R.id.map_icon_driver);
        solo.clickOnView(map_icon);
        solo.waitForActivity("Driver Map Display");
        solo.assertCurrentActivity("Wrong Activity", Activity_DriverMapDisplay.class);
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
