package com.example.jetcab;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class PostRequestActivityTest {

    @Rule
    public ActivityTestRule<PostRequest> rule = new ActivityTestRule<>(PostRequest.class);
    @Test
    public void checkSwitchActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.from_textField))
                .perform();
    }
}