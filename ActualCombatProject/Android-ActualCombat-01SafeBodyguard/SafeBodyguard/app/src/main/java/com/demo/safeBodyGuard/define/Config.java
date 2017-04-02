package com.demo.safeBodyGuard.define;

import com.demo.safeBodyGuard.R;

import java.util.HashMap;

/**
 * Created by iml1s-macpro on 2016/12/30.
 */

public class Config {
    public static final int IO_BUFFER_SIZE = 1024;

    public static final int PERMISSION_ALL_REQUEST_CODE = 0x2;

    //region activity request code
    public static final int ACTIVITY_REQUEST_CODE_CONTACT_SELECT = 0x1;
    //endregion

    //region activity result code
    public static final int ACTIVITY_RESULT_CODE_NOTHING = 0x1;
    public static final int ACTIVITY_RESULT_CODE_CONTACT_SELECTED = 0x2;


    //endregion


    // SplashActivity檢查更新的最小時間(MS)
    public static int SPLASH_MIN_INTERVAL_MS = 2500;


    //region haredPreferences key

    public static final String SP_KEY_TEST = "sp_key_test";

    public static final String SP_KEY_BOOL_UPDATE = "sp_key_update";

    public static final String SP_KEY_BOOL_SAFE_SETUP = "sp_key_safe_setup_bool";

    public static final String SP_KEY_BOOL_OPEN_SAFE_GUARD = "sp_key_bool_open_safe_guard";

    public static final String SP_KEY_STRING_PWD = "sp_key_pwd";

    public static final String SP_KEY_STRING_SIM_SERIAL_NUM = "sp_key_sim_serial_num";

    public static final String SP_KEY_STRING_ALARM_PHONE_NUMBER =
            "sp_key_string_alarm_phone_number";

    public static final String SP_KEY_INT_PHONE_ADDRESS_VIEW_BACKGROUND_INDEX =
            "SP_KEY_INT_PHONE_ADDRESS_VIEW_BACKGROUND_INDEX";


    public static final String SP_KEY_BOOL_PROCESS_MANAGER_ACT_SYSTEM_SHOW =
            "sp_key_bool_process_manager_act_system_show";

    public static final String SP_KEY_BOOL_CREATED_ICON = "sp_key_bool_created_icon";

    /**
     * 來電漂浮查詢視窗的X位置
     */
    public static final String SP_KEY_INT_FLOW_VIEW_LOCATION_X = "sp_key_int_flow_view_location_x";

    /**
     * 來電漂浮查詢視窗的Y位置
     */
    public static final String SP_KEY_INT_FLOW_VIEW_LOCATION_Y = "sp_key_int_flow_view_location_y";
    //endregion


    //region DB
    public static final String DB_URL_CONTACTS = "content://com.android.contacts/";

    public static final String DB_TABLE_CONTACT_RAW_CONTACT = "raw_contacts";

    public static final String DB_TABLE_CONTACT_DATA = "data";

    public static final String DB_TABLE_ADDRESS_DATA1 = "data1";

    public static final String DB_TABLE_ADDRESS_DATA2 = "data2";

    public static final String DB_COLUMN_RAW_CONTACT_CONTACT_ID = "contact_id";

    public static final String DB_COLUMN_DATA_CONTACT_DATA1 = "data1";

    public static final String DB_COLUMN_DATA_CONTACT_MIME_TYPE = "mimetype";

    public static final String DB_FILE_NAME_ADDRESS = "address.db";

    public static final String DB_FILE_NAME_COMMON_PHONE = "commonnum.db";
    //endregion


    //region intent key
    public static final String INTENT_DATA_KEY_PHONE = "phone";
    public static final String INTENT_KEY_PKG_NAME = "pkgName";
    //endregion

    //region sms control key
    public static final String SMS_CONTROL_KEY_ALARM = "#*alarm*#";

    public static final String SMS_CONTROL_KEY_LOCATION = "#*location*#";

    public static final String SMS_CONTROL_KEY_STOP_LOCATION = "#*stopLocation*#";
    //endregion sms control key


    //region APP
    //    public static final String APP_PKG_NAME = "com.demo.safeBodyGuard";
    //endregion

    //region drawable
    public static final String[] DRAWABLE_DISPLAY_NAME_ARRAY_QUERY_ADDRESS_VIEW_BACKGROUND =
            new String[]{"藍色", "綠色", "白色"

            };

    public static final int[] DRAWABLE_RESOURCE_ID_ARRAY_PHONE_QUERY_ADDR_VIEW_BG =
            new int[]{R.drawable.call_locate_blue, R.drawable.call_locate_green,
                    R.drawable.call_locate_white};

    public static final String[] DRAWABLE_NAME_ARRAY_PHONE_QUERY_ADDRESS_VIEW_BG =
            new String[]{"藍色", "綠色", "白色"};

    public static final HashMap<String, Integer> DRAWABLE_MAP_NAME_BG_PHONE_QUERY_ADDRESS_VIEW;
    //endregion


    static {
        DRAWABLE_MAP_NAME_BG_PHONE_QUERY_ADDRESS_VIEW = new HashMap<>();

        for (int i = 0; i < DRAWABLE_NAME_ARRAY_PHONE_QUERY_ADDRESS_VIEW_BG.length; i++) {
            DRAWABLE_MAP_NAME_BG_PHONE_QUERY_ADDRESS_VIEW
                    .put(DRAWABLE_NAME_ARRAY_PHONE_QUERY_ADDRESS_VIEW_BG[i],
                            DRAWABLE_RESOURCE_ID_ARRAY_PHONE_QUERY_ADDR_VIEW_BG[i]);
        }
    }

}
