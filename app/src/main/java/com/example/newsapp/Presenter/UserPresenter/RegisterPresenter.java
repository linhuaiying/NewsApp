package com.example.newsapp.Presenter.UserPresenter;

import com.example.newsapp.Model.UserModels.RegisterModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.Userbean.User;

public class RegisterPresenter<T extends IBaseView> extends BasePresenter {
    //2.持有Model接口
    RegisterModel registerModel;

    @Override
    //3.执行业务逻辑
    public void fetch(String username, String password, String repassword) throws InterruptedException {
        registerModel = new RegisterModel(username, password, repassword);
        if(iView.get() != null && registerModel != null) {
            registerModel.loadRegisterData(new RegisterModel.OnLoadListener() {

                @Override
                public void onComplete(User user) {
                   //不做任何操作
                }

                @Override
                public void onError(String msg) {
                    ((IBaseView)iView.get()).showErrorMessage(msg); //给view传递错误信息，比如用户名少于6位
                }
            });
        }
    }
}
