package com.demo.safeBodyGuard.define;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class HandlerProtocol
{
    /*
     * 進入主界面 enter to mainPage
     */
    public final static int ENTER_HOME = 1;

    /*
     * 升級版本 update new apk version
     */
    public final static int UPDATE_VERSION = 2;

    /*
     *
     */
    public final static int URL_ERROR = 4;

    public final static int IO_ERROR = 8;

    public final static int JSON_ERROR = 16;

    public static final int CONTACTS_READ_OVER = 32;

    public final static int QUERY_PHONE_ADDRESS_COMPLETED = 64;

    /**
     * 黑名單相關
     */
    public static final int ON_BLACK_LIST_SELECTED_ALL = 128;           // 查詢所有黑名單資料
    public static final int ON_BLACK_LIST_ADD          = 256;           // 新增黑名單
    public static final int ON_BLACK_LIST_INIT         = 512;           // 黑名單ListView初始化
    public static final int ON_BLACK_LIST_LOADED       = 1024;          // 黑名單列表讀取完畢
    public static final int ON_APP_INFO_LOADED         = 2048;



    public static final int ON_PROCESS_INFO_LOADED = 4096;          // 運行中Process清單讀取完畢
    public static final int ON_PROCESS_INFO_SPLIT  = 8192;          // 運行中Process清單拆分完畢(分為系統和一般)
}
