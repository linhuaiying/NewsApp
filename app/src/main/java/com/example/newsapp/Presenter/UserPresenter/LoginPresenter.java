package com.example.newsapp.Presenter.UserPresenter;

import com.example.newsapp.Model.UserModels.UserModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.UserView.IUserView;
import com.example.newsapp.bean.Userbean.User;

public class LoginPresenter<T extends IUserView> extends BasePresenter {
    //2.持有Model接口
    UserModel userModel;

    @Override
    //3.执行业务逻辑
    public void fetch(String username, String password) throws InterruptedException {
        userModel = new UserModel(username, password);
        if(iView.get() != null && userModel != null) {
            userModel.loadUserData(new UserModel.OnLoadListener() {

                @Override
                public void onComplete(User user) {
                    ((IUserView)iView.get()).getUser(user); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {
                    ((IUserView)iView.get()).showErrorMessage(msg); //给view传递错误信息，比如密码错误
                }
            });
        }
    }
}
