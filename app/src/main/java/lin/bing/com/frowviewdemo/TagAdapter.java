package lin.bing.com.frowviewdemo;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bing.lin on 15-9-14.
 */
public abstract  class TagAdapter<T> {
    private List<T> mDatas;
    private OnDataChangeListener mListener;
    public TagAdapter(List<T> datas){
        mDatas=datas;
    }
    public TagAdapter(T[] datas){
        mDatas=new ArrayList<T>(Arrays.asList(datas));

    }
    public void setOnDataChangeListener(OnDataChangeListener listener){
        mListener=listener;
    }
    static interface OnDataChangeListener{
        void change();
    }
    public void notificationDataChanged(){
            if(mListener!=null){
                mListener.change();
            }
    }
    public int getCount(){
        return mDatas!=null?mDatas.size():0;
    }
    public T getItem(int postion){
        return mDatas.get(postion);
    }
    public abstract View getView(FlowLayout layout,int postion,T t);

}
