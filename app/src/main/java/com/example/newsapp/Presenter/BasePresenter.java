package com.example.newsapp.Presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.newsapp.View.IBaseView;

import java.lang.ref.WeakReference;
import java.util.List;

public class BasePresenter<T extends IBaseView> implements LifecycleObserver { //生命周期观察者
    //1.持有View接口的弱引用
   protected WeakReference<T> iView;
    //绑定
    public void attachView(T view) {
     iView = new WeakReference<>(view);
    }
    //解绑
    public void distachView() { //解决内存泄漏的问题。
        if(iView != null) {
           iView = null;
        }

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)  //监听生命周期
    public void onCreate(LifecycleOwner lifecycleOwner) {

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)  //监听生命周期
    public void onStart(LifecycleOwner lifecycleOwner) {

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)  //监听生命周期
    public void onResume(LifecycleOwner lifecycleOwner) {

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)  //监听生命周期
    public void onPause(LifecycleOwner lifecycleOwner) {

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)  //监听生命周期
    public void onStop(LifecycleOwner lifecycleOwner) {

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)  //监听生命周期
    public void onDestory(LifecycleOwner lifecycleOwner) {

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)  //监听生命周期
    public void onAny(LifecycleOwner lifecycleOwner) {

    }

   public void fetch() throws InterruptedException {
   }

   public void fetch(String a, String b, String c, String d) throws InterruptedException {

   }

    public void fetch(String username, String password, String repassword) throws InterruptedException {
    }

    public void fetch(String username, String password) throws InterruptedException {
   }

    public void fetch(String uniquekey) throws InterruptedException {

    }

    public void fetch(List<String> imagPaths) throws InterruptedException {
    }
}
