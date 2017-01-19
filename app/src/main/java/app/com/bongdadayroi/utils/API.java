package app.com.bongdadayroi.utils;

import app.com.bongdadayroi.myapp.Config;

public class API {

    public static final String HOST_API_ALL = "http://content.amobi.vn/api/apiall/?app_id=";

    public static final String LIST_CATEGORY_URL = HOST_API_ALL + Config.APP_ID + "&type=list-category";
    public static final String HOME_URL = HOST_API_ALL + Config.APP_ID + "&type=home";
    public static final String MOST_URL = HOST_API_ALL + Config.APP_ID + "&type=topview";
    public static final String NEW_URL = HOST_API_ALL + Config.APP_ID + "&type=latest";
    public static final String SEARCH_URL = HOST_API_ALL + Config.APP_ID + "&type=search&keyword=";

    public static final String DETAIL_CATEGORY_URL = HOST_API_ALL + Config.APP_ID + "&type=detail-category&category_id=";

    public static final String LOAD_MORE_TOPVIEW_URL = HOST_API_ALL + Config.APP_ID + "&type=topview&last_id=";

    public static final String LOAD_MORE_LATEST_URL = HOST_API_ALL + Config.APP_ID + "&type=latest&last_id=";

    public static final String LOAD_MORE_CATEGORY_URL = HOST_API_ALL + Config.APP_ID + "&type=detail-category&category_id=";

    public static final String VIDEO_RELATE_URL = HOST_API_ALL + Config.APP_ID + "&type=relate-post&post_id=";

    public static final String GET_TOKEN_URL = "http://content.amobi.vn/api/comment/facebook/?app_id="+ Config.APP_ID + "&fa_access_token=";

    public static final String LIST_COMMENT = "http://content.amobi.vn/api/comment/listcomment/?app_id="+ Config.APP_ID + "&post_id=";

    public static final String VIDEO_DETAIL = HOST_API_ALL + Config.APP_ID + "&type=detail&post_id=";

    public static final String CHECK_ACTION = "http://content.amobi.vn/api/apiall/check-action";

    public static final String LOGOUT = "http://content.amobi.vn/api/comment/logout";
}
