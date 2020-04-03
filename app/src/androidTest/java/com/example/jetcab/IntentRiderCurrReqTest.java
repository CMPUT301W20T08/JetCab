package com.example.jetcab;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Mazhar
 * Test class for switching from MainMenR to CurrentRequest (for rider) activity.
 * All the UI tests are written here. Robotium test framework is used
 */

//public class IntentRiderCurrReqTest {
//    private Solo solo;
//
//    @Rule
//    public ActivityTestRule<CurrentRequest> rule = new ActivityTestRule<>(CurrentRequest.class, true, true);
//
//    /**
//     * Runs before all tests and creates solo instance.
//     * @throws Exception
//     */
//    @Before
//    public void setUp() throws Exception {
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//    }
//
//    /**
//     * Gets the Activity
//     * @throws Exception
//     */
//    @Test
//    public void start() throws Exception {
//        Activity activity = rule.getActivity();
//    }
//
//    /**
//     * check whether clicking on the driver image button opens up driver dialog
//     */
//    @Test
//    public void checkDriverInfoTest() {
//        solo.assertCurrentActivity("Wrong Activity", CurrentRequest.class);
//        solo.clickOnText("My Current Requests");
//        ImageButton br = (ImageButton) solo.getView(R.id.driver_profile_image_button);
//        solo.clickOnView(br);
//        solo.waitForActivity("CurrReq");
//        assertTrue("View driver info dialog not working", solo.waitForText("DRIVER:"));
//        assertTrue("View driver info dialog not working", solo.waitForText("PHONE:"));
//        assertTrue("View driver info dialog not working", solo.waitForText("EMAIL:"));
//        solo.assertCurrentActivity("Wrong Activity", CurrentRequest.class);
//    }
//
//    /**
//     * Closes the activity after each test
//     * @throws Exception
//     */
//    @After
//    public void tearDown() throws Exception {
//        solo.finishOpenedActivities();
//    }
//}
//
////*************************************************************************************************//*************************************************************************************************

public class IntentRiderCurrReqTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Activity_MainMenuR> rule = new ActivityTestRule<>(Activity_MainMenuR.class, true, true);

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
     * checks if warning toast appears when clicking on my current request when no request is active
     * @throws Exception
     */
    @Test
    public void checkNoCurrentRequestToast() throws Exception {
        solo.assertCurrentActivity("Wrong Activity", Activity_MainMenuR.class);
        solo.clickOnText("My Current Requests");
        assertTrue("", solo.waitForText("No Current Active Requests"));
    }

    /**
     * check whether the CurrentRequest activity is correctly displayed after posting a request
     * checks current status, driver details, and cancel button
     */
    @Test
    public void checkSwitchActivity() {
        solo.assertCurrentActivity("Wrong Activity", Activity_MainMenuR.class);
        solo.clickOnText("Post Requests");
        solo.waitForActivity("PostReq");

        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.clearEditText((EditText) solo.getView(R.id.from_editText)); //Clear the EditText
        solo.enterText((EditText) solo.getView(R.id.from_editText), "University of Alberta");
        solo.clickOnScreen(999,374); //coordinate of map icon, edit so that it matched your screen
        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
        solo.goBack();

        solo.enterText((EditText) solo.getView(R.id.to_editText), "University of Calgary");
        solo.clickOnScreen(999,571); //coordinate of map icon, edit so that it matched your screen
        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
        solo.goBack();

        solo.clickOnText("POST");
        solo.waitForActivity("CurrentReq");
        solo.assertCurrentActivity("Wrong activity", CurrentRequest.class);
        assertTrue("", solo.waitForText("Open"));

        ImageButton br = (ImageButton) solo.getView(R.id.driver_profile_image_button);
        solo.clickOnView(br);
        assertTrue("", solo.waitForText("Driver's Profile"));
        ImageButton ok = (ImageButton) solo.getView(R.id.ok_image);
        solo.clickOnView(ok);
//        solo.goBack();

        solo.clickOnText("Cancel");
        assertTrue("", solo.waitForText("CANCEL THE REQUEST"));
        solo.clickOnText("Yes");
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