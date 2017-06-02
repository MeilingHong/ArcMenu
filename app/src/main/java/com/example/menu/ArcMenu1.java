package com.example.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class ArcMenu1 extends ViewGroup implements OnClickListener{
    final String TAG = "ArcMenuTag";
    /**
     * 菜单按钮
     */
    private View mCBMenu;

    /**
     * 菜单的位置，为枚举类型
     *
     * @author fuly1314
     */
    private enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, MIDDLE_TOP, MIDDLE_BOTTOM,CENTER_TOP,CENTER_BOTTOM
    }

    /**
     * 菜单的状态
     *
     * @author fuly1314
     */
    private enum Status {
        OPEN, CLOSE
    }

    /**
     * 菜单为当前位置，默认为RIGHT_BOTTOM，在后面我们可以获取到
     */
    private Position mPosition = Position.RIGHT_BOTTOM;
    /**
     * 菜单的当前状态,默认为关闭
     */
    private Status mCurStatus = Status.CLOSE;

    /**
     * 菜单的半径，默认为120dp
     */

    /**
     * 提供一个回调接口，用来处理菜单的点击事件，点击后需要处理的事情
     */
    public interface ArcMenuListener {
        void dealMenuClick(View v);
    }

    public void setOnArcMenuListener(ArcMenuListener listener) {
        mListener = listener;
    }

    private ArcMenuListener mListener;

    //TODO 默认半径100DP
    private int mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
            getResources().getDisplayMetrics());


    public ArcMenu1(Context context) {
        this(context, null);
    }

    public ArcMenu1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyle, 0);
        //获取到菜单设置的位置
        int position = ta.getInt(R.styleable.ArcMenu_position, 3);

        switch (position) {
            case 0:
                mPosition = Position.LEFT_TOP;
                break;
            case 1:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case 2:
                mPosition = Position.RIGHT_TOP;
                break;
            case 3:
                mPosition = Position.RIGHT_BOTTOM;
                break;
            case 4:
                mPosition = Position.MIDDLE_TOP;
                break;
            case 5:
                mPosition = Position.MIDDLE_BOTTOM;
                break;
            case 6:
                mPosition = Position.CENTER_TOP;
                break;
            case 7:
                mPosition = Position.CENTER_BOTTOM;
                break;
        }

        //获取到菜单的半径
        mRadius = (int) ta.getDimension(R.styleable.ArcMenu_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics()));
        ta.recycle();

    }


    /**
     * 测量各个子View的大小
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();//获取子view的数量

        for (int i = 0; i < count; i++) {
            //测量子view的大小
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 摆放各个子view的位置
     */
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed)//如果发生了改变，就重新布局
        {
            Log.e(TAG,"\n\n*********************");
            layoutMainMenu();//菜单按钮的布局
            /**
             * 下面的代码为菜单的布局
             */
            int count = getChildCount();

            for (int i = 0; i < count - 1; i++) {
                View childView = getChildAt(i + 1);//注意这里过滤掉菜单按钮，只要菜单选项view

                childView.setVisibility(GONE);//先让菜单消失

                int left = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
                int top = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));


                switch (mPosition) {
                    case LEFT_TOP:
                        break;
                    case LEFT_BOTTOM:
                        top = getMeasuredHeight() - top - childView.getMeasuredHeight();
                        break;
                    case RIGHT_TOP:
                        left = getMeasuredWidth() - left - childView.getMeasuredWidth();
                        break;
                    case RIGHT_BOTTOM:
                        left = getMeasuredWidth() - left - childView.getMeasuredWidth();
                        top = getMeasuredHeight() - top - childView.getMeasuredHeight();
                        break;
                    case MIDDLE_TOP:
                        //TODO 计算
                        left = (int) (mRadius * Math.cos(Math.PI / 6 + Math.PI* (2*i/3/count)));
                        top = (int) (mRadius * Math.sin(Math.PI / 6 + Math.PI* (2*i/3/count)));
                        Log.e(TAG,"Position = "+mPosition);//i>=((count+1)/2)
                        Log.e(TAG,"i>((count-1)/2)? = "+(i>((count-1)/2)));
                        Log.e(TAG,"(count-1)/2) = "+(count-1)/2);
                        Log.e(TAG,"I = "+i+" --- Left(X方向) = "+left+" --- Top(Y方向) = "+top+"\n------");
                        if(i>=((count-1)/2)){
                            left = getMeasuredWidth()/2 - childView.getMeasuredWidth()/2 + left;
                        }else{
                            left = getMeasuredWidth()/2 + childView.getMeasuredWidth()/2 - left;
                        }
                        break;
                    case MIDDLE_BOTTOM:
                        left = (int) (mRadius * Math.cos(Math.PI / 6 + Math.PI* (2*i/3/count)));
                        top = (int) (mRadius * Math.sin(Math.PI / 6 + Math.PI* (2*i/3/count)));
                        Log.e(TAG,"Position = "+mPosition);//i>=((count+1)/2)
                        Log.e(TAG,"i>((count-1)/2)? = "+(i>((count-1)/2)));
                        Log.e(TAG,"(count-1)/2) = "+(count-1)/2);
                        Log.e(TAG,"I = "+i+" --- Left(X方向) = "+left+" --- Top(Y方向) = "+top+"\n------");
                        if(i>=((count-1)/2)){
                            left = getMeasuredWidth()/2 - childView.getMeasuredWidth()/2 + left;
                        }else{
                            left = getMeasuredWidth()/2 + childView.getMeasuredWidth()/2 - left;
                        }
                        top = getMeasuredHeight() - top;
                        break;
                    case CENTER_TOP:
                        if(count==1){
                            left = (int) (mRadius * Math.cos(Math.PI / 6 + Math.PI* (2*i/3/count)));
                            top = (int) (mRadius * Math.sin(Math.PI / 6 + Math.PI* (2*i/3/count)));
                        }else{
                            left = (int) (mRadius * Math.cos(Math.PI / 6 + Math.PI* (2*i/3/count)));
                            top = (int) (mRadius * Math.sin(Math.PI / 6 + Math.PI* (2*i/3/count)));
                        }
                        Log.e(TAG,"Position = "+mPosition);//i>=((count+1)/2)
                        Log.e(TAG,"i>((count-1)/2)? = "+(i>((count-1)/2)));
                        Log.e(TAG,"(count-1)/2) = "+(count-1)/2);
                        Log.e(TAG,"I = "+i+" --- Left(X方向) = "+left+" --- Top(Y方向) = "+top+"\n------");
                        if(i>=((count-1)/2)){
                            left = getMeasuredWidth()/2 - childView.getMeasuredWidth()/2 + left;
                        }else{
                            left = getMeasuredWidth()/2 + childView.getMeasuredWidth()/2 - left;
                        }
                        top = getMeasuredHeight()/2 - top;
                        break;
                    case CENTER_BOTTOM:
                        if(count==1){
                            left = (int) (mRadius * Math.cos(Math.PI / 6 + Math.PI* (2f*i/3f)));
                            top = (int) (mRadius * Math.sin(Math.PI / 6 + Math.PI* (2f*i/3f)));
                        }else{
                            left = (int) (mRadius * Math.cos(Math.PI / 6 + Math.PI* (2*i/3f/(count-1))));
                            top = (int) (mRadius * Math.sin(Math.PI / 6 + Math.PI* (2*i/3f/(count-1))));
                        }
                        if(i>=((count-1)/2)){
                            left = getMeasuredWidth()/2 - childView.getMeasuredWidth()/2 + Math.abs(left);
                        }else{
                            left = getMeasuredWidth()/2 + childView.getMeasuredWidth()/2 - Math.abs(left);
                        }
                        top = getMeasuredHeight()/2 + Math.abs(top);

                        Log.e(TAG,"Position = "+mPosition);//
                        Log.e(TAG,"i>=((count-1)/2)? = "+(i>=((count-1)/2)));
                        Log.e(TAG,"tempI = "+i+" --- Left(X方向) = "+Math.abs(left)+" --- Top(Y方向) = "+top+"\n------");
                        break;
                }

                childView.layout(left, top, left + childView.getMeasuredWidth(),
                        top + childView.getMeasuredHeight());
            }
        }


    }

    /**
     * 菜单按钮的布局
     */
    private void layoutMainMenu() {

        mCBMenu = getChildAt(0);//获得主菜单按钮

        mCBMenu.setOnClickListener(this);

        int left = 0;
        int top = 0;

        switch (mPosition) {
            case LEFT_TOP:
                left = 0;
                top = 0;
                break;
            case LEFT_BOTTOM:
                left = 0;
                top = getMeasuredHeight() - mCBMenu.getMeasuredHeight();
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - mCBMenu.getMeasuredWidth();
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = getMeasuredWidth() - mCBMenu.getMeasuredWidth();
                top = getMeasuredHeight() - mCBMenu.getMeasuredHeight();
                break;
            case MIDDLE_TOP:
                left = getMeasuredWidth()/2 - mCBMenu.getMeasuredWidth()/2;//中间位置
                top = 0;
                break;
            case MIDDLE_BOTTOM:
                left = getMeasuredWidth()/2 - mCBMenu.getMeasuredWidth()/2;//中间位置
                top = getMeasuredHeight() - mCBMenu.getMeasuredHeight();//底部
                break;
            case CENTER_TOP:
                left = getMeasuredWidth()/2 - mCBMenu.getMeasuredWidth()/2;//中间位置
                top = getMeasuredHeight()/2 - mCBMenu.getMeasuredHeight()/2;//中间位置
                break;
            case CENTER_BOTTOM:
                left = getMeasuredWidth()/2 - mCBMenu.getMeasuredWidth()/2;//中间位置
                top = getMeasuredHeight()/2 - mCBMenu.getMeasuredHeight()/2;//中间位置
                break;
        }
        mCBMenu.layout(left, top, left + mCBMenu.getMeasuredWidth(), top + mCBMenu.getMeasuredHeight());
    }

    /**
     * 菜单按钮的点击事件
     *
     * @param v
     */
    public void onClick(View v) {
        //为菜单按钮设置点击动画
        RotateAnimation rAnimation = new RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rAnimation.setDuration(300);

        rAnimation.setFillAfter(true);

        v.startAnimation(rAnimation);

        dealChildMenu(300);//处理菜单选项，比如折叠菜单或者展开菜单
    }

    /**
     * 处理菜单选项，比如折叠菜单或者展开菜单
     *
     * @param duration 菜单选项的动画时间
     */
    private void dealChildMenu(int duration) {

        //下面的代码为菜单选项设置动画

        int count = getChildCount();

        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);

            AnimationSet set = new AnimationSet(true);

            if(mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM ||
                    mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP ){
//1.首先是平移动画
                TranslateAnimation tAnimation = null;

                //平移的x方向和y方向的距离
                int x = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
                int y = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));


                //平移的标志，是平移一个正数还以一个负数
                int xflag = 1;
                int yflag = 1;
                if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
                    xflag = -1;
                }
                if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                    yflag = -1;
                }
//                Log.e(TAG,"mPosition =  "+mPosition);
//                Log.e(TAG,"I =  "+i+"  ----  xflag:"+xflag);
//                Log.e(TAG,"I =  "+i+"  ----  yflag:"+yflag);

                if (mCurStatus == Status.CLOSE)//如果当前状态为关闭则应该打开
                {
                    tAnimation = new TranslateAnimation(xflag * x, 0,
                            yflag * y, 0);
                    tAnimation.setDuration(duration);
                    tAnimation.setFillAfter(true);

                } else//否则为打开状态，就应该关闭
                {
                    tAnimation = new TranslateAnimation(0, xflag * x,
                            0, yflag * y);
                    tAnimation.setDuration(duration);
                    tAnimation.setFillAfter(true);

                }
                tAnimation.setStartOffset((i * 100) / count);
                tAnimation.setAnimationListener(new AnimationListener() {


                    public void onAnimationStart(Animation animation) {


                    }


                    public void onAnimationRepeat(Animation animation) {


                    }


                    public void onAnimationEnd(Animation animation) {

                        if (mCurStatus == Status.CLOSE) {
                            childView.setVisibility(GONE);
                            childView.setClickable(false);
                            childView.setFocusable(false);
                        }
                        if (mCurStatus == Status.OPEN) {
                            childView.setVisibility(VISIBLE);//设置菜单可见
                            //为打开状态，则菜单是可点击和获得焦点
                            childView.setClickable(true);
                            childView.setFocusable(true);
                        }

                    }
                });

                //2.然后是旋转动画
                RotateAnimation rAnimation = new RotateAnimation(0f, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rAnimation.setDuration(duration);
                rAnimation.setFillAfter(true);//动画结束是画面停留在此动画的最后一帧


                set.addAnimation(rAnimation);//一定要注意顺序，先旋转动画，然后再平移
                set.addAnimation(tAnimation);

                childView.startAnimation(set);

                //为菜单项设置点击事件
                final int cPos = i + 1;
                childView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        clickAnimation(cPos);//点击动画

                        if (mListener != null)//处理点击事件的逻辑
                        {
                            mListener.dealMenuClick(childView);
                        }

                        changeStatus();


                    }
                });
            }else{
                //TODO 中间位置的

                //1.首先是平移动画
                TranslateAnimation tAnimation = null;

                //平移的x方向和y方向的距离
                int x = (int) (mRadius * Math.cos(Math.PI / 6 + Math.PI* (2/3/count) * i));
                int y = (int) (mRadius * Math.sin(Math.PI / 6 + Math.PI* (2/3/count) * i));


                //平移的标志，是平移一个正数还以一个负数
                int xflag = 1;
                int yflag = 1;

                if (mPosition == Position.MIDDLE_BOTTOM || mPosition == Position.CENTER_TOP) {
                    yflag = 1;
                    if(i>=((count-1)/2)){
                        xflag = -1;
                    }else{
                        xflag = 1;
                    }
                }
                if (mPosition == Position.MIDDLE_TOP || mPosition == Position.CENTER_BOTTOM) {
                    yflag = -1;
                    if(i>=((count-1)/2)){
                        xflag = -1;
                    }else{
                        xflag = 1;
                    }
                }
//                Log.e(TAG,"mPosition =  "+mPosition);
//                Log.e(TAG,"I =  "+i+"  ----  xflag:"+xflag);
//                Log.e(TAG,"I =  "+i+"  ----  yflag:"+yflag);

                if (mCurStatus == Status.CLOSE)//如果当前状态为关闭则应该打开
                {
                    tAnimation = new TranslateAnimation(xflag * x, 0,
                            yflag * y, 0);
                    tAnimation.setDuration(duration);
                    tAnimation.setFillAfter(true);

                } else//否则为打开状态，就应该关闭
                {
                    tAnimation = new TranslateAnimation(0, xflag * x,
                            0, yflag * y);
                    tAnimation.setDuration(duration);
                    tAnimation.setFillAfter(true);

                }
                tAnimation.setStartOffset((i * 100) / count);
                tAnimation.setAnimationListener(new AnimationListener() {


                    public void onAnimationStart(Animation animation) {


                    }


                    public void onAnimationRepeat(Animation animation) {


                    }


                    public void onAnimationEnd(Animation animation) {

                        if (mCurStatus == Status.CLOSE) {
                            childView.setVisibility(GONE);
                            childView.setClickable(false);
                            childView.setFocusable(false);
                        }
                        if (mCurStatus == Status.OPEN) {
                            childView.setVisibility(VISIBLE);//设置菜单可见
                            //为打开状态，则菜单是可点击和获得焦点
                            childView.setClickable(true);
                            childView.setFocusable(true);
                        }

                    }
                });

                //2.然后是旋转动画
                RotateAnimation rAnimation = new RotateAnimation(0f, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rAnimation.setDuration(duration);
                rAnimation.setFillAfter(true);//动画结束是画面停留在此动画的最后一帧


                set.addAnimation(rAnimation);//一定要注意顺序，先旋转动画，然后再平移
                set.addAnimation(tAnimation);

                childView.startAnimation(set);

                //为菜单项设置点击事件
                final int cPos = i + 1;
                childView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        clickAnimation(cPos);//点击动画

                        if (mListener != null)//处理点击事件的逻辑
                        {
                            mListener.dealMenuClick(childView);
                        }
                        changeStatus();
                    }
                });
            }
        }

        changeStatus();//动画完成后，要改变状态

    }

    /**
     * 改变状态
     */
    private void changeStatus() {

        mCurStatus = (mCurStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);

    }

    /**
     * 菜单项的点击动画
     *
     * @param cPos 用来判断当前点击的是哪一个菜单
     */
    private void clickAnimation(int cPos) {

        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);

            if (i + 1 == cPos) {
                AnimationSet set = new AnimationSet(true);
                ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                sAnimation.setFillAfter(true);
                AlphaAnimation alAnimation = new AlphaAnimation(1.0f, 0f);
                alAnimation.setFillAfter(true);

                set.addAnimation(sAnimation);
                set.addAnimation(alAnimation);

                set.setDuration(300);
                childView.startAnimation(set);

            } else {
                AnimationSet set = new AnimationSet(true);
                ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                sAnimation.setFillAfter(true);
                AlphaAnimation alAnimation = new AlphaAnimation(1.0f, 0f);
                alAnimation.setFillAfter(true);

                set.addAnimation(sAnimation);
                set.addAnimation(alAnimation);

                set.setDuration(300);
                childView.startAnimation(set);
            }
            childView.setVisibility(GONE);
        }

    }

    /**
     06-01 15:16:39.487 30556-30556/com.example.menu E/ArcMenu: mPosition =  RIGHT_BOTTOM
     06-01 15:16:39.488 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  xflag:1
     06-01 15:16:39.488 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  yflag:1
     06-01 15:16:39.490 30556-30556/com.example.menu E/ArcMenu: mPosition =  RIGHT_BOTTOM
     06-01 15:16:39.490 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  xflag:1
     06-01 15:16:39.490 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  yflag:1
     06-01 15:16:39.490 30556-30556/com.example.menu E/ArcMenu: mPosition =  RIGHT_BOTTOM
     06-01 15:16:39.491 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  xflag:1
     06-01 15:16:39.491 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  yflag:1
     *******************************************************************************************
     06-01 15:17:58.023 30556-30556/com.example.menu E/ArcMenu: mPosition =  LEFT_TOP
     06-01 15:17:58.023 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  xflag:-1
     06-01 15:17:58.023 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  yflag:-1
     06-01 15:17:58.025 30556-30556/com.example.menu E/ArcMenu: mPosition =  LEFT_TOP
     06-01 15:17:58.025 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  xflag:-1
     06-01 15:17:58.025 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  yflag:-1
     06-01 15:17:58.026 30556-30556/com.example.menu E/ArcMenu: mPosition =  LEFT_TOP
     06-01 15:17:58.026 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  xflag:-1
     06-01 15:17:58.026 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  yflag:-1
     *******************************************************************************************
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: mPosition =  LEFT_BOTTOM
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  xflag:-1
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  yflag:1
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: mPosition =  LEFT_BOTTOM
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  xflag:-1
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  yflag:1
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: mPosition =  LEFT_BOTTOM
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  xflag:-1
     06-01 15:18:35.356 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  yflag:1
     *******************************************************************************************
     06-01 15:19:40.271 30556-30556/com.example.menu E/ArcMenu: mPosition =  RIGHT_TOP
     06-01 15:19:40.271 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  xflag:1
     06-01 15:19:40.271 30556-30556/com.example.menu E/ArcMenu: I =  0  ----  yflag:-1
     06-01 15:19:40.271 30556-30556/com.example.menu E/ArcMenu: mPosition =  RIGHT_TOP
     06-01 15:19:40.271 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  xflag:1
     06-01 15:19:40.271 30556-30556/com.example.menu E/ArcMenu: I =  1  ----  yflag:-1
     06-01 15:19:40.272 30556-30556/com.example.menu E/ArcMenu: mPosition =  RIGHT_TOP
     06-01 15:19:40.272 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  xflag:1
     06-01 15:19:40.272 30556-30556/com.example.menu E/ArcMenu: I =  2  ----  yflag:-1


     *******************************************************************************************


     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: mPosition =  MIDDLE_BOTTOM
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: I =  0  ----  xflag:1
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: I =  0  ----  yflag:1
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: mPosition =  MIDDLE_BOTTOM
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: I =  1  ----  xflag:1
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: I =  1  ----  yflag:1
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: mPosition =  MIDDLE_BOTTOM
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: I =  2  ----  xflag:-1
     06-01 15:27:17.284 18902-18902/com.example.menu E/ArcMenu: I =  2  ----  yflag:1

     *******************************************************************************************
     06-01 15:29:19.032 20306-20306/com.example.menu E/ArcMenu: mPosition =  MIDDLE_TOP
     06-01 15:29:19.032 20306-20306/com.example.menu E/ArcMenu: I =  0  ----  xflag:1
     06-01 15:29:19.033 20306-20306/com.example.menu E/ArcMenu: I =  0  ----  yflag:-1
     06-01 15:29:19.035 20306-20306/com.example.menu E/ArcMenu: mPosition =  MIDDLE_TOP
     06-01 15:29:19.035 20306-20306/com.example.menu E/ArcMenu: I =  1  ----  xflag:1
     06-01 15:29:19.035 20306-20306/com.example.menu E/ArcMenu: I =  1  ----  yflag:-1
     06-01 15:29:19.035 20306-20306/com.example.menu E/ArcMenu: mPosition =  MIDDLE_TOP
     06-01 15:29:19.035 20306-20306/com.example.menu E/ArcMenu: I =  2  ----  xflag:-1
     06-01 15:29:19.035 20306-20306/com.example.menu E/ArcMenu: I =  2  ----  yflag:-1

     */

}
