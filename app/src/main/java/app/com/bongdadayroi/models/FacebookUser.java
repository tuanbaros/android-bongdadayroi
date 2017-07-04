package app.com.bongdadayroi.models;

import com.facebook.Profile;

import app.com.bongdadayroi.myapp.ScreenSize;

/**
 * Created by tuan on 27/03/2016.
 */
public class FacebookUser {
    private String user_id = null;
    private String user_name = null;
    private String auth_token = null;
    private String user_avatar_uri = null;
    private static volatile FacebookUser user = null;

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    private FacebookUser() {
    }

    public static FacebookUser getInstance() {
        if (user == null) {
            synchronized (FacebookUser.class) {
                if (user == null) {
                    user = new FacebookUser();
                }
            }
        }
        return user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar_uri() {
        return user_avatar_uri;
    }

    public void setUser_avatar_uri(String user_avatar_uri) {
        this.user_avatar_uri = user_avatar_uri;
    }

    public void reset() {
        this.user_id = null;
        this.user_name = null;
        this.user_avatar_uri = null;
        this.auth_token = null;
    }

    public void setInformation(Profile profile) {
        if (profile != null) {
            this.user_id = profile.getId();
            this.user_name = profile.getName();
            this.user_avatar_uri =
                profile.getProfilePictureUri(ScreenSize.WIDTH / 3, ScreenSize.WIDTH / 3).toString();
        }
    }
}
