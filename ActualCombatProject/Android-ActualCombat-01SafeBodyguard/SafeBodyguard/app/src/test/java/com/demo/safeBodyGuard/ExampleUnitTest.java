package com.demo.safeBodyGuard;

import android.util.Log;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <QUERY_PHONE_ADDRESS_COMPLETED href="http://d.android.com/tools/testing">Testing documentation</QUERY_PHONE_ADDRESS_COMPLETED>
 */
public class ExampleUnitTest
{
    @Test
    public void addition_isCorrect() throws Exception
    {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dbTest()
    {
//        BlackListOpenHelper
//                openHelper = new BlackListOpenHelper();

        Log.d("debug", "----- openHelper -----");
    }
}