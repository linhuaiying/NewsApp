package com.example.newsapp.LocalUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;

import com.example.newsapp.bean.MyUserbean.MyUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveAccount {
    public static boolean saveUserInfo(Context context, String username, String password)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("userName",username);
        editor.putString("pwd",password);
        editor.commit();
        return  true;
    }
    public static boolean saveExtraUserInfo(Context context, String nickName, String sex, String sign, String userIcon)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("nickName", nickName);
        editor.putString("sex", sex);
        editor.putString("sign", sign);
        if(userIcon == null || !userIcon.equals("")) editor.putString("userIcon", userIcon);
        editor.commit();
        return  true;
    }
    public static boolean saveConcernUsers(Context context, List<MyUser> myUserList)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Gson gson = new Gson();
        String concernUserList = gson.toJson(myUserList);
        editor.putString("concernUserList", concernUserList);
        editor.commit();
        return  true;
    }
    public static boolean saveFans(Context context, List<MyUser> myUserList)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Gson gson = new Gson();
        String fans = gson.toJson(myUserList);
        editor.putString("fans", fans);
        editor.commit();
        return  true;
    }
    public static boolean saveTitles(Context context, List<String> titles)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(titles);
        editor.putString("titles", json);
        editor.commit();
        return  true;
    }
    public static boolean saveIsFirstStart(Context context, boolean isFirstStart)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("isFirstStart", isFirstStart);
        editor.commit();
        return  true;
    }
    public static Map<String,String> getUserInfo(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        String account = sp.getString("userName",null);
        String password = sp.getString("pwd",null);
        String nickName = sp.getString("nickName", null);
        String sex = sp.getString("sex", null);
        String sign = sp.getString("sign", null);
        String userIcon = sp.getString("userIcon", null);
        Map<String,String > userMap=new HashMap<String, String>();
        userMap.put("userName",account);
        userMap.put("password",password);
        userMap.put("nickName", nickName);
        userMap.put("sex", sex);
        userMap.put("sign", sign);
        userMap.put("userIcon", userIcon);
        return userMap;
    }
    public static boolean isFistStart(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        Boolean isFirstStart = sp.getBoolean("isFirstStart", true);
        return isFirstStart;
    }
    public static List<MyUser> getConcernUsers(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        String concernUsers = sp.getString("concernUserList",null);
        Gson gson = new Gson();
        List<MyUser> myUserList = gson.fromJson(concernUsers, new TypeToken<List<MyUser>>(){}.getType());
        return myUserList;
    }
    public static List<MyUser> getFans(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        String fans = sp.getString("fans",null);
        Gson gson = new Gson();
        List<MyUser> myUserList = gson.fromJson(fans, new TypeToken<List<MyUser>>(){}.getType());
        return myUserList;
    }
    public static List<String> getTitles(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        String json = sp.getString("titles",null);
        Gson gson = new Gson();
        List<String> titles = gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
        return titles;
    }
     public static void deleteUserInfo(Context context) {
         SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
         SharedPreferences.Editor editor=sp.edit();
         editor.clear();
         editor.commit();
     }
}
