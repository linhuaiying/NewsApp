package com.example.newsapp.Presenter.EditInfoPresenter;

import com.example.newsapp.Model.EditInfoModels.EditInfoModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.EditInfoView.IEditInfoView;

public class EditInfoPresenter<T extends IEditInfoView> extends BasePresenter {
    EditInfoModel editInfoModel;

    @Override
    public void fetch(String userName, String nickName, String sex, String sign, String imagUrl) throws InterruptedException {
        editInfoModel = new EditInfoModel(userName, nickName, sex, sign, imagUrl);
        if(iView.get() != null && editInfoModel != null) {
            editInfoModel.loadUserData(new EditInfoModel.OnLoadListener() {
                @Override
                public void onComplete(String msg) {
                    ((IEditInfoView)iView.get()).showSuccessMsg(msg); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {
                    ((IEditInfoView)iView.get()).showErrorMessage(msg);
                }
            });
        }
    }
}
