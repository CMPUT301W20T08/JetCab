package com.example.jetcab;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static junit.framework.TestCase.assertTrue;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Joyce
 * Test class for PostActivity. All the UI tests are written here. Robotium test framework is used
 * also included the finish intent from MapDisplay
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Very Important Notice:!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Since Robotium cannot click on the Icon of TextInputLayout,!!!!!!!!!!!!!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!I used the clickOnScreen() to instead!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!However, it may not have the same position in the different emulator!!!!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!I used the Pixel 2 XL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class PostRequestLocationMapTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Activity_PostRequest> rule =
            new ActivityTestRule<>(Activity_PostRequest.class, true, true);

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
     * If the start location editText is empty when clicking the map icon, check whether
     * the Toast will be shown and cannot enter the MapDisplay activity
     */
    @Test
    public void checkEmptyStartLocation() {
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.clearEditText((EditText) solo.getView(R.id.from_editText));

        solo.clickOnScreen(1342,459); //coordinate of map icon
        assertTrue(solo.waitForText("Please Enter the Start Location"));
    }

    /**
     * If the end location editText is empty when clicking the map icon, check whether
     * the Toast will be shown and cannot enter the MapDisplay activity
     */
    @Test
    public void checkEmptyEndLocation() {
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.clearEditText((EditText) solo.getView(R.id.to_editText));

        solo.clickOnScreen(1317,713); //coordinate of map icon
        assertTrue(solo.waitForText("Please Enter the End Location"));
    }

    /**
     * check whether the MapDisplay activity is correctly switched (for start location)
     * after clicking map icon
     */
    @Test
    public void checkStartSwitchActivity() {
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.clickOnScreen(1342,459); //coordinate of map icon

        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
    }

    /**
     * check whether the MapDisplay activity is correctly switched (for end location)
     * after clicking map icon
     */
    @Test
    public void checkEndSwitchActivity() {
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.enterText((EditText) solo.getView(R.id.to_editText), "University of Alberta");

        solo.clickOnScreen(1317,713); //coordinate of map icon
        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
    }

    /**
     * click the map icon to go the MapDisplay
     * check whether the image button can back to PostRequest Activity (for the start location)
     */
    @Test
    public void checkBackStartThisActivity() {
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.clickOnScreen(1342,459); //coordinate of map icon

        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
        solo.clickOnScreen(1407,324); //coordinate of image button (R.id.back_image_button)
                                             //unable to use clickOnImageButton here, also, cannot get the
                                             //coordinates of the ImageButton, so this also may not work
                                             //on different size screen

        solo.waitForActivity("PostRequest");
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
    }

    /**
     * click the map icon to go the MapDisplay
     * check whether the image button can back to PostRequest Activity (for the end location)
     */
    @Test
    public void checkBackEndThisActivity() {
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.enterText((EditText) solo.getView(R.id.to_editText), "University of Alberta");
        solo.clickOnScreen(1342,713); //coordinate of map icon

        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
        solo.clickOnScreen(1407,324); //coordinate of image button (R.id.back_image_button)

        solo.waitForActivity("PostRequest");
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
    }

    /**
     * Checks if the fair fare is correct
     */
    @Test
    public void checkFairFare() {
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.clearEditText((EditText) solo.getView(R.id.to_editText)); //Clear the EditText
        solo.clearEditText((EditText) solo.getView(R.id.from_editText)); //Clear the EditText

        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);
        solo.enterText((EditText) solo.getView(R.id.from_editText), "West Edmonton Mall");
        solo.clickOnScreen(1342,713); //coordinate of map icon
        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
        solo.clickOnScreen(1407,324); //coordinate of image button (R.id.back_image_button)
        solo.waitForActivity("PostRequest");
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);

        solo.enterText((EditText) solo.getView(R.id.to_editText), "University of Alberta");
        solo.clickOnScreen(1342,713); //coordinate of map icon
        solo.waitForActivity("MapDisplay");
        solo.assertCurrentActivity("Wrong Activity", Activity_RiderMapDisplay.class);
        solo.clickOnScreen(1407,324); //coordinate of image button (R.id.back_image_button)
        solo.waitForActivity("PostRequest");
        solo.assertCurrentActivity("Wrong Activity", Activity_PostRequest.class);

        assertTrue(solo.waitForText("10.19", 1, 2000));
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