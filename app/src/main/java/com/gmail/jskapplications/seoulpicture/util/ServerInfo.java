package com.gmail.jskapplications.seoulpicture.util;

/**
 * Created by for on 2017-10-15.
 */

public class ServerInfo {


    public static String UPLOAD_URL = "http://jskim421.dothome.co.kr/upload_post/upload_post.php";
    public static String EDIT_UPLOAD_URL = "http://jskim421.dothome.co.kr/upload_post/edit_upload_post.php";
    public static String GET_POSTS = "http://jskim421.dothome.co.kr/get_post/get_posts.php";

    public static String GET_BESTS= "http://jskim421.dothome.co.kr/get_post/get_bests.php";
    public static String GET_NEWS= "http://jskim421.dothome.co.kr/get_post/get_news.php";

    public static String GET_BESTS_WITH_DETAIL = "http://jskim421.dothome.co.kr/get_post/get_bests_with_detail.php";
    public static String GET_POST= "http://jskim421.dothome.co.kr/get_post/get_post.php";
    public static String GET_POST_FOR_EDIT = "http://jskim421.dothome.co.kr/get_post/get_post_for_edit.php";

    public static String GET_OR_PUT_USER = "http://jskim421.dothome.co.kr/get_user/get_or_put_user.php";
    public static String PUT_LIKE_POST = "http://jskim421.dothome.co.kr/like/put_like_post.php";
    public static String DELETE_POST = "http://jskim421.dothome.co.kr/delete/delete_post.php";

    public static String GET_USER_LIKES = "http://jskim421.dothome.co.kr/like/get_user_likes.php";

    public static String GET_USER_LIST = "http://jskim421.dothome.co.kr/get_user/get_user_list.php";


    public static String getProfilePictureUrl(String id, int width, int height) {
        return "https://graph.facebook.com/"+ id + "/picture?width="+width+"&height="+height;
    }
}
