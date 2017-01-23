package com.demo.safeBodyGuard;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <QUERY_PHONE_ADDRESS_COMPLETED href="http://d.android.com/tools/testing">Testing documentation</QUERY_PHONE_ADDRESS_COMPLETED>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    @Test
    public void useAppContext() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("volley.test.com.a01_safebodyguard", appContext.getPackageName());
    }
}
