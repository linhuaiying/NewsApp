package com.example.newsapp.LocalUtils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SaveAccount {
    public static boolean saveUserInfo(Context context,String username,String password)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("userName",username);
        editor.putString("pwd",password);
        editor.commit();
        return  true;

    }
    public static Map<String,String> getUserInfo(Context context)
    {
        SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        String account=sp.getString("userName",null);
        String password=sp.getString("pwd",null);
        Map<String,String > userMap=new HashMap<String, String>();
        userMap.put("userName",account);
        userMap.put("password",password);
        return userMap;
    }
     public static void deleteUserInfo(Context context) {
         SharedPreferences sp=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
         SharedPreferences.Editor editor=sp.edit();
         editor.clear();
     }
}
