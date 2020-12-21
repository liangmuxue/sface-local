package com.ss.sdk.utils;

public class HttpConstant {

    /*云端*/
    public static String TOKEN = "/auth/login";

    public static String FACE_LIST = "/resident/issue/task";

    public static String FACE_LIST_RESULT = "/resident/issue/status";

    public static String UPLOAD_COMMON_CAPTURE_LIST = "/collect/capture/commonSnap";

    public static String UPLOAD_REMOTE_CAPTURE_LIST = "/collect/capture/remoteSnap";

    public static String DEVICE_STATE = "/camera/updateCameraState";

    /*冠林*/
    public static String LL_LOGIN = "/login";

    public static String LL_EVENT = "/event";

    public static String LL_CARD_ADD = "/card/add";

    public static String LL_TENEMENT_CLEAR = "/tenement/clear";

    public static String LL_TENEMENT_MODIFY = "/tenement/modify";

    public static String LL_TENEMENT_ADD = "/tenement/add";

    public static String LL_TENEMENT_DELETE = "/tenement/delete";

    public static String LL_TENEMENT_QUERY = "/tenement/query";

    public static String LL_FACE_ADD = "/face/add";

    public static String LL_OPEN_DOOR = "/ctrl/door_open";

    //冠品设备
    public static String GUANPIN_PERSON_CREATE = "/person/create";

    public static String GUANPIN_PERSON_DELETE = "/person/delete";

    public static String GUANPIN_FACE_CREATE = "/face/create";

    public static String GUANPIN_PULL_SET_PERSION_PERMISSION_PARAMETER = "/pull/setPersonPermissionParameter";

    public static String GUANPIN_PULL_SET_DATA_UPLOAD_PARAMETER = "/pull/setDataUploadParameter";

    public static String GUANPIN_DEVICE_OPENDOOR_CONTROL = "/device/openDoorControl";

    public static String GUANPIN_FACE_CAPTURE = "/face/capture";

    public static String GUANPIN_PERSON_PERMISSION_CREATE = "/person/permissionsCreate";

    public static String GUANPIN_PERSON_CREATE_PASSTIME = "/person/createPasstime";

    public static String YUSHI_INSERT_PEOPLE = "/LAPI/V1.0/PeopleLibraries/3/People";

    public static String YUSHI_DELETE_PEOPLE = "/LAPI/V1.0/PeopleLibraries/3/People/";

    public static String YUSHI_REMOTE_DOOR = "/LAPI/V1.0/PACS/Controller/RemoteOpened";
}
