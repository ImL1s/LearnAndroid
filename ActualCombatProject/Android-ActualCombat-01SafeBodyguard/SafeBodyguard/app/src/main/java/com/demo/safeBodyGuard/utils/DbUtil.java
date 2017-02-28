package com.demo.safeBodyGuard.utils;

import android.content.Context;

import com.demo.safeBodyGuard.db.dao.AddressDAO;
import com.demo.safeBodyGuard.define.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by aa223 on 2017/2/28.
 */

public class DbUtil
{
    /**
     * 將assets下的DB複製到Database目錄下.
     * @param context 上下文
     * @param dbFileName Db文件名稱
     * @return 如果回傳true,代表Copy成功.
     */
    public static boolean copyDbToDbFolder(Context context, File dbFile, String dbFileName)
    {
        if (dbFile.exists())
            return false;

        boolean copySucc = true;

        InputStream is = null;
        OutputStream os = null;

        try
        {
            is = context.getAssets().open(dbFileName);
            os = new FileOutputStream(dbFile);

            byte[] buffer = new byte[Config.IO_BUFFER_SIZE];
            int haveRead;

            while ((haveRead = is.read(buffer)) != -1)
            {
                os.write(buffer, 0, haveRead);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            copySucc = false;
        }
        finally
        {
            try
            {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return copySucc;
    }
}
