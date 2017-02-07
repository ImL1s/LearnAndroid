package com.demo.safeBodyGuard;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.demo.safeBodyGuard.db.BlackListOpenHelper;
import com.demo.safeBodyGuard.db.dao.BlackListDAO;
import com.demo.safeBodyGuard.db.dao.model.BlackRoll;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
        //   Context appContext = InstrumentationRegistry.getTargetContext();

        //        assertEquals("volley.test.com.a01_safebodyguard", appContext.getPackageName());

        BlackListOpenHelper openHelper =
                new BlackListOpenHelper(InstrumentationRegistry.getContext());
        Context context = InstrumentationRegistry.getTargetContext();

        //        BlackListDAO.getInstance(InstrumentationRegistry.getContext()).insert("110",2);

        long insertRow = BlackListDAO.getInstance(context).insert("113", 2);
        int updateRow = BlackListDAO.getInstance(context).update(1, "111", 1);
        int deleteRow = BlackListDAO.getInstance(context).delete(1);
        List<BlackRoll> blackRolls = BlackListDAO.getInstance(context).select(3);
        List<BlackRoll> allBlackRolls = BlackListDAO.getInstance(context).selectAll();
        List<BlackRoll> limitBlackRolls = BlackListDAO.getInstance(context).selectRange(0, 10);

        Log.d("debug", "----- openHelper -----");


    }
}
