package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.R;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.HisView.HisActivity;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyConcernUserAdapter extends RecyclerView.Adapter<MyConcernUserAdapter.ConcernUserViewHolder> {
    List<MyUser> myUserList;
    Context context;
    public MyConcernUserAdapter(List<MyUser> myUserList) {
        this.myUserList = myUserList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public MyConcernUserAdapter.ConcernUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_item_layout, parent, false);
        MyConcernUserAdapter.ConcernUserViewHolder concernUserViewHolder = new MyConcernUserAdapter.ConcernUserViewHolder(view);
        concernUserViewHolder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = concernUserViewHolder.getAdapterPosition();
                HisActivity.actionStart(context, myUserList.get(position));
            }
        });
        return concernUserViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyConcernUserAdapter.ConcernUserViewHolder holder, int position) {
        List<MyUser> concernUsers = SaveAccount.getConcernUsers(context) == null ? new ArrayList<>() : SaveAccount.getConcernUsers(context);
        MyUser myUser = myUserList.get(position);
        if(myUser.getNickName() != null) holder.nickName.setText(myUser.getNickName());
        else holder.nickName.setText(myUser.getUserName());
        if(myUser.getSign() != null && !myUser.getSign().equals("")) holder.sign.setText(myUser.getSign());
        if(myUser.getUserIcon() != null) Glide.with(context).load(myUser.getUserIcon()).into(holder.circleImageView);
        holder.concernBtn.setText("取消关注");
        holder.concernBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                        .build();
                UserService userService = retrofit.create(UserService.class);
                Call<ResponseBody> call;
                if(holder.concernBtn.getText().toString().equals("关注")) {
                    call = userService.concernUser(SaveAccount.getUserInfo(context).get("userName"), myUserList.get(position).getUserName());
                } else {
                    call = userService.noconcernUser(SaveAccount.getUserInfo(context).get("userName"), myUserList.get(position).getUserName());
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.body().string().equals("success")) {
                                if(holder.concernBtn.getText().toString().equals("关注")) {
                                    MyToast.toast("关注成功！");
                                    holder.concernBtn.setText("取消关注");
                                    MyUser myUser = new MyUser();
                                    myUser.setUserName(myUserList.get(position).getUserName());
                                    concernUsers.add(myUser);
                                    SaveAccount.saveConcernUsers(context, concernUsers);
                                } else {
                                    MyToast.toast("取消关注成功！");
                                    holder.concernBtn.setText("关注");
                                    for(int i = 0; i < concernUsers.size(); i++) {
                                        if(concernUsers.get(i).getUserName().equals(myUserList.get(position).getUserName())) {
                                            concernUsers.remove(i);
                                            SaveAccount.saveConcernUsers(context, concernUsers);
                                            break;
                                        }
                                    }
                                }
                            }
                            else MyToast.toast("关注失败！");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        MyToast.toast("关注失败！");
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return myUserList.size();
    }

    static class ConcernUserViewHolder extends RecyclerView.ViewHolder{
        View searchView;
        TextView nickName;
        CircleImageView circleImageView;
        TextView sign;
        Button concernBtn;
        public ConcernUserViewHolder(@NonNull View itemView) {
            super(itemView);
            searchView = itemView;
            nickName = itemView.findViewById(R.id.nick_name);
            circleImageView = itemView.findViewById(R.id.user_icon);
            sign = itemView.findViewById(R.id.sign);
            concernBtn = itemView.findViewById(R.id.concern_btn);
        }
    }
}
