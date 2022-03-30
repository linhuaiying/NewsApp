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
import com.example.newsapp.Model.SearchUserModels.SearchUserModel;
import com.example.newsapp.R;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.HisView.HisActivity;
import com.example.newsapp.bean.MyUserbean.MyUser;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {
    List<MyUser> myUserList;
    Context context;
    public SearchUserAdapter(List<MyUser> myUserList) {
        this.myUserList = myUserList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_item_layout, parent, false);
        SearchUserViewHolder searchUserViewHolder = new SearchUserViewHolder(view);
        searchUserViewHolder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = searchUserViewHolder.getAdapterPosition();
                HisActivity.actionStart(context, myUserList.get(position));
            }
        });
        searchUserViewHolder.concernBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = searchUserViewHolder.getAdapterPosition();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                        .build();
                UserService userService = retrofit.create(UserService.class);
                Call<ResponseBody> call = userService.concernUser(SaveAccount.getUserInfo(context).get("userName"), myUserList.get(position).getUserName());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.body().string().equals("success")) {
                                MyToast.toast("关注成功！");
                                searchUserViewHolder.concernBtn.setText("已关注");
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
        return searchUserViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder holder, int position) {
        MyUser myUser = myUserList.get(position);
        if(myUser.getNickName() != null) holder.nickName.setText(myUser.getNickName());
        else holder.nickName.setText(myUser.getUserName());
        if(myUser.getSign() != null && !myUser.getSign().equals("")) holder.sign.setText(myUser.getSign());
        if(myUser.getUserIcon() != null) Glide.with(context).load(myUser.getUserIcon()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return myUserList.size();
    }

    static class SearchUserViewHolder extends RecyclerView.ViewHolder{
        View searchView;
        TextView nickName;
        CircleImageView circleImageView;
        TextView sign;
        Button concernBtn;
        public SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);
            searchView = itemView;
            nickName = itemView.findViewById(R.id.nick_name);
            circleImageView = itemView.findViewById(R.id.user_icon);
            sign = itemView.findViewById(R.id.sign);
            concernBtn = itemView.findViewById(R.id.concern_btn);
        }
    }
}
