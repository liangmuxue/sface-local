package com.ss.sdk.utils;

public class HttpConstant {

    /*云端*/
    public static String TOKEN = "/auth/login";

    public static String FACE_LIST = "/peopleDeviceRef/getIssueData";

    public static String VISITOR_LIST = "/visitor/syncToSdk";

    public static String FACE_LIST_RESULT = "/peopleDeviceRef/issue/status";

    public static String VISITOR_RESULT = "/visitor/issueStatus";

    public static String UPLOAD_CAPTURE_LIST = "/collect/capture/syncRecord";

    public static String UPLOAD_VISITOR_CAPTURE_LIST = "/visitor/record/syncRecord";

    public static String DEVICE_STATE = "/camera/updateCameraState";

    /*冠林*/
    public static String LL_LOGIN = "/login";

    public static String LL_EVENT = "/event";

    public static String LL_TENEMENT_ADD = "/tenement/add";

    public static String LL_TENEMENT_PERMISSION = "/tenement/permission";

    public static String LL_TENEMENT_DELETE = "/tenement/delete";

    public static String LL_FACE_ADD = "/face/add";

    public static String LL_FACE_REMOVE = "/face/remove";

    public static String LL_OPEN_DOOR = "/ctrl/door_open";

    public static String VISTOR_MANA = "/visitor/add";

    public static String VISTOR_SIGNOUT = "/visitor/signout";

}
