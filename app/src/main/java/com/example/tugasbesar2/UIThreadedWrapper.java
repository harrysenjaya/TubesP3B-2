package com.example.tugasbesar2;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class UIThreadedWrapper extends Handler {

    protected final static int MSG_SET_TEXTVIEW_OUTPUT=0;
    protected MainActivity mainActivity;

    public UIThreadedWrapper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleMessage(Message msg){
        if(msg.what==UIThreadedWrapper.MSG_SET_TEXTVIEW_OUTPUT){
            Circle parameter = (Circle) msg.obj;
            this.mainActivity.setTextViewOut(parameter);
        }
    }

    public void setTextViewOut(Circle circle) {
        Message msg = new Message();
        msg.what=MSG_SET_TEXTVIEW_OUTPUT;
        msg.obj = circle;
        this.sendMessage(msg);
    }


}
