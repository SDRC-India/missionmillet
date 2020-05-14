package org.sdrcwassan.missionmillet.collect.android.externalintents;

import android.support.test.filters.Suppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdrcwassan.missionmillet.collect.android.activities.FormEntryActivity;

import java.io.IOException;

import static org.sdrcwassan.missionmillet.collect.android.externalintents.ExportedActivitiesUtils.testDirectories;

@Suppress
// Frequent failures: https://github.com/opendatakit/collect/issues/796
@RunWith(AndroidJUnit4.class)
public class FormEntryActivityTest {

    @Rule
    public ActivityTestRule<FormEntryActivity> formEntryActivityRule =
            new ExportedActivityTestRule<>(FormEntryActivity.class);

    @Test
    public void formEntryActivityMakesDirsTest() throws IOException {
        testDirectories();
    }

}
