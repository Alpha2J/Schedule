package cn.alpha2j.schedule;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import cn.alpha2j.schedule.entity.Task;
import cn.alpha2j.schedule.repository.ScheduleDatabaseHelper;
import cn.alpha2j.schedule.repository.impl.TaskRepositoryImpl;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("cn.alpha2j.schedule", appContext.getPackageName());
    }
}
