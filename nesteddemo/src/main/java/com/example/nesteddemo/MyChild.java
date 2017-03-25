package com.example.nesteddemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by chentian on 23/03/2017.
 */

public class MyChild extends LinearLayout implements NestedScrollingChild {
    NestedScrollingChildHelper nscp;
    int lastY;
    int[] consumed;
    int[] offsetWindow;
    int showHeight;

    public MyChild(Context context) {
        this(context,null);
    }

    public MyChild(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //第一次测量，因为是wrap_content,测量出来的只是父容器除了ImageView和TextView剩余的高度
        //此次测量只是为了求得剩余的高度
        //如果没有第二次测量，那么下面的文字就会显示不出来
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        showHeight = getMeasuredHeight();

        //现在我们把MeasureSpec设置为UNSPECIFIED,这样MyChild的高度就没有限制了，也就能显示全部的文字了
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getRawY();
                int dy = y-lastY;
                lastY = y;

                if(startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)){
                    dispatchNestedPreScroll(0,dy,consumed,offsetWindow);
                    scrollBy(0,-dy);
                }

                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        int maxY = getMeasuredHeight()-showHeight;
        if(y>maxY){
            y=maxY;
        }
        else if(y<0){
            y=0;
        }
        super.scrollTo(x, y);
    }

    private NestedScrollingChildHelper getNscp(){
        if(nscp == null){
            nscp = new NestedScrollingChildHelper(this);
            nscp.setNestedScrollingEnabled(true);
            return nscp;
        }else {
            return nscp;
        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getNscp().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getNscp().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getNscp().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getNscp().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getNscp().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getNscp().dispatchNestedScroll(dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed,offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getNscp().dispatchNestedPreScroll(dx,dy,consumed,offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getNscp().dispatchNestedFling(velocityX,velocityY,consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getNscp().dispatchNestedPreFling(velocityX,velocityY);
    }
}
