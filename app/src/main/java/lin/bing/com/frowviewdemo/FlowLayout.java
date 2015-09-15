package lin.bing.com.frowviewdemo;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bing.lin on 15-9-14.
 */
public class FlowLayout extends ViewGroup implements TagAdapter.OnDataChangeListener {
    private ArrayList<List<View>> mAllViews=new ArrayList<List<View>>();
    private List<Integer> mLineHeights=new ArrayList<Integer>();
    private TagAdapter mAdapter;
    private MotionEvent mMotionEvent;
    private OnSelectListener mListener;
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left=0;
        int top=0;
        int lineWidth=0;
        int lineHeigh=0;
        mAllViews.clear();
        mLineHeights.clear();
        int width=getWidth();
        List<View> lineViews=new ArrayList<View>();
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            ViewGroup.MarginLayoutParams childLp=(ViewGroup.MarginLayoutParams)childView.getLayoutParams();
            int childWidth= childView.getMeasuredWidth();
            int childHeight=childView.getMeasuredHeight();
            if(lineWidth+childLp.leftMargin+childLp.rightMargin+childWidth>width){
                mLineHeights.add(lineHeigh);
                mAllViews.add(lineViews);
                lineWidth=0;
                lineViews=new ArrayList<>();
            }
            lineWidth+=childWidth+childLp.leftMargin+childLp.rightMargin;
            lineHeigh=Math.max(lineHeigh,childHeight+childLp.topMargin+childLp.bottomMargin);
            lineViews.add(childView);
        }
        mLineHeights.add(lineHeigh);
        mAllViews.add(lineViews);
        int lineNum=mAllViews.size();
        for(int i=0;i<lineNum;i++){
            lineViews=mAllViews.get(i);
            lineHeigh=mLineHeights.get(i);
            for(int j=0;j<lineViews.size();j++){
                View child=lineViews.get(j);
                ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams)child.getLayoutParams();
                if(child.getVisibility()==View.GONE){
                    continue;
                }
                int lc=left+lp.leftMargin;
                int tc=top+lp.topMargin;
                int rc=lc+child.getMeasuredWidth();
                int bc=tc+child.getMeasuredHeight();
                child.layout(lc,tc,rc,bc);
                left+=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            }
            left=0;
            top+=lineHeigh;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得容器测量的大小和模式
        int sizeWidth=MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight=MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth=MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight=MeasureSpec.getMode(heightMeasureSpec);

        int width=0;
        int height=0;
        int lineWidth=0;
        int lineHeight=0;
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View child=getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
            if(lineWidth+childWidth>sizeWidth){
                width=Math.max(lineWidth,childWidth);
                lineWidth=childWidth;
                height+=childHeight;
                lineHeight=childHeight;
            }else{
                lineWidth+=childWidth;
                lineHeight=Math.max(lineHeight,childHeight);
            }
            if(i==childCount-1){
                width=Math.max(lineWidth,width);
                height+=lineHeight;
            }
        }
        Log.d("LINBING", "width" + width);
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
    public void setAdapter(TagAdapter adapter){
        mAdapter=adapter;
        mAdapter.setOnDataChangeListener(this);
        changeAdpater();

    }

    @Override
    public void change() {
        changeAdpater();
    }
    private void changeAdpater(){
        removeAllViews();
        TagAdapter adapter = mAdapter;
        TagView tagViewContainer=null;
        for(int i=0;i<adapter.getCount();i++){
            Log.d("LINBING","ADDvIEW");
            View tagView=adapter.getView(this,i,adapter.getItem(i));
            tagView.setDuplicateParentStateEnabled(true); //允许父布局的状态传递到子布局
            tagViewContainer=new TagView(getContext());
            tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==event.ACTION_UP){
            Log.d("LINBING","ACTION UP");
            mMotionEvent=MotionEvent.obtain(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        Log.d("LINBING","performClick");
        if(mMotionEvent==null){
            return super.performClick();
        }
        int x=(int)mMotionEvent.getX();
        int y=(int)mMotionEvent.getY();
        Log.d("LINBING","X ,Y "+x+","+y);
        TagView view = findChild(x, y);
        if(view!=null){
            if(mListener!=null){
                mListener.onSelect(view,findViewPostion(view));
            }
        }
        return super.performClick();
    }
    private int findViewPostion(View view){
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            if(view==getChildAt(i)){
                return i;
            }
        }
        return -1;
    }
    private TagView findChild(int x,int y){
        final int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            TagView view=(TagView)getChildAt(i);
            if(view.getVisibility()==View.GONE){
                continue;
            }
             Rect rect=new Rect();
            view.getHitRect(rect);
            if(rect.contains(x,y)){
                return view;
            }

        }
        return null;
    }
    interface  OnSelectListener{
        void onSelect(View view,int postion);
    }
    public void setOnSelectListener(OnSelectListener listener){
        mListener=listener;
    }
}
