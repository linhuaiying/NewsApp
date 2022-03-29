package com.example.newsapp.LocalUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;

import java.util.HashMap;
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
     public static void deleteUserInfo(Context context) {
         SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
         SharedPreferences.Editor editor=sp.edit();
         editor.clear();
         editor.commit();
     }
}
