package com.example.jetcab;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class ScannerTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Activity_Scanner> rule = new ActivityTestRule<>(Activity_Scanner.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        //initialize the ShowActivity environment before testing
        solo.assertCurrentActivity("Wrong Activity", Activity_Scanner.class);
    }

    /**
     * Check whether the right Activity opens by clicking on ARRIVED button.
     */

    @Test
    public void checkActivityChange() {
        solo.clickOnButton("SCAN QR CODE");
        solo.assertCurrentActivity("Wrong Activity", Activity_scanCode.class);
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
