package com.example.jetcab;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Mazhar
 * Test class for switching from MainMenR to CurrentRequest (for rider) activity.
 * All the UI tests are written here. Robotium test framework is used
 */

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

    @Test
    public void checkNoCurrentRequestToast() throws Exception {
        solo.clickOnText("My Current Requests");

    }

    /**
     * check whether the DriverCurrentRequest activity is correctly switched
     */
    @Test
    public void checkSwitchActivity() {
        solo.assertCurrentActivity("Wrong Activity", Activity_MainMenuR.class);
        solo.clickOnText("My Current Requests");

        solo.waitForActivity("DriverCurrReq");
        solo.assertCurrentActivity("Wrong Activity", CurrentRequest.class);
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

//
///**
//package com.example.jetcab;
//
//        import android.app.Activity;
//
//        import androidx.test.platform.app.InstrumentationRegistry;
//        import androidx.test.rule.ActivityTestRule;
//
//        import com.robotium.solo.Solo;
//
//        import org.junit.After;
//        import org.junit.Before;
//        import org.junit.Rule;
//        import org.junit.Test;
//
///**
// * @author Joyce
// * Test class for switching from MainMenD to DriverCurrentRequest activity.
// * All the UI tests are written here. Robotium test framework is used
// */
//
//
//public class IntentDriverCurrReqTest {
//    private Solo solo;
//
//    @Rule
//    public ActivityTestRule<Activity_MainMenuD> rule =
//            new ActivityTestRule<>(Activity_MainMenuD.class, true, true);
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
//     * check whether the DriverCurrentRequest activity is correctly switched
//     */
//    @Test
//    public void checkSwitchActivity() {
//        solo.assertCurrentActivity("Wrong Activity", Activity_MainMenuD.class);
//        solo.clickOnText("My Current Requests");
//
//        solo.waitForActivity("DriverCurrReq");
//        solo.assertCurrentActivity("Wrong Activity", Activity_DriverCurrentRequest.class);
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
