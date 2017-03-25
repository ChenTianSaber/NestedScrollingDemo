package com.example.nesteddemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chentian on 23/03/2017.
 */

public class MyParent extends LinearLayout implements NestedScrollingParent {
    NestedScrollingParentHelper nsp;
    ImageView iv;
    TextView tv;
    MyChild nsc;
    int ivHeight;
    int tvHeight;

    public MyParent(Context context) {
        this(context,null);
    }

    public MyParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        nsp = new NestedScrollingParentHelper(this);
    }

    //拿到父容器里面的三个子View


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iv = (ImageView) getChildAt(0);
        tv = (TextView) getChildAt(1);
        nsc = (MyChild) getChildAt(2);

        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(ivHeight<=0){
                    ivHeight = iv.getMeasuredHeight();
                }
            }
        });

        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(tvHeight<=0){
                    tvHeight = tv.getMeasuredHeight();
                }
            }
        });
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if(target instanceof MyChild){
            return true;
        }

        return false;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        nsp.onNestedScrollAccepted(child,target,nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        nsp.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //dy是子View传过来的，来询问父容器是不是要消费他，要的话，就把dy放进consumed数组
        //其中consumed数组，consumed[0]表示x方向的距离，consumed[1]表示y方向的距离
        if(showImg(dy)||hideImg(dy)/*这里根据业务逻辑来判断*/){
            scrollBy(0,-dy);
            consumed[1] = dy;
        }
    }

    private boolean hideImg(int dy) {
        //上拉的时候，判断是不是要隐藏图片
        if(dy<0){
            if(getScrollY()<ivHeight){
                //判断只要上移的部分，没有超过ImageView，那么就让父容器继续滑动
                return true;
            }
        }

        return false;
    }

    private boolean showImg(int dy) {
        //下拉的时候，判断是不是要显示图片
        if(dy>0){
            if(nsc.getScrollY()==0){
                return true;
            }
        }

        return false;
    }

    //scrollBy内部调用scrollTo,我们父容器不能滑出去，也不能滑的太下面，我们要修正这些情况
    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if(y>ivHeight){
            y = ivHeight;
        }
        else if(y<0){
            y=0;
        }

        super.scrollTo(x,y);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }
}
