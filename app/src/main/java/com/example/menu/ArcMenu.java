package com.example.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;

public class ArcMenu extends ViewGroup implements OnClickListener{
    final String TAG = "ArcMenuTag";
    /**
     * 菜单按钮
     */
    private View mCBMenu;

    double div;
    double radius;
    int countLeft;
    int countRight;
    /**
     * 菜单的位置，为枚举类型
     * @author fuly1314
     *
     */
    private enum Position
    {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, MIDDLE_TOP, MIDDLE_BOTTOM,CENTER_TOP,CENTER_BOTTOM
    }
    /**
     * 菜单的状态
     * @author fuly1314
     *
     */
    private enum Status
    {
        OPEN,CLOSE
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
    public interface ArcMenuListener
    {
        void dealMenuClick(View v);
    }
    public void setOnArcMenuListener(ArcMenuListener listener){

        mListener = listener;
    }
    private ArcMenuListener mListener;



    private int mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
            getResources().getDisplayMetrics());



    public ArcMenu(Context context) {
        this(context,null);
    }
    public ArcMenu(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyle, 0);
        //获取到菜单设置的位置
        int position = ta.getInt(R.styleable.ArcMenu_position, 3);

        switch(position){
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
                        getResources().getDisplayMetrics()));//TODO 默认100DP 的半径
        ta.recycle();

    }



    /**
     * 测量各个子View的大小
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int count = getChildCount();//获取子view的数量

        for(int i=0;i<count;i++)
        {
            //测量子view的大小
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 摆放各个子view的位置
     */
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if(changed)//如果发生了改变，就重新布局
        {
//            Log.e(TAG,"Width:"+getMeasuredWidth()+"   ---   Height:"+getMeasuredHeight());
            layoutMainMenu();//菜单按钮的布局
            /**
             * 下面的代码为菜单的布局
             */
            int count = getChildCount();

            for(int i=0;i<count-1;i++)
            {
                View childView = getChildAt(i+1);//注意这里过滤掉菜单按钮，只要菜单选项view

                //TODO 原来应该是GONE
                childView.setVisibility(GONE);//先让菜单消失
                //TODO X方向上的位置（距离）
                int left = (int) (mRadius*Math.cos(Math.PI/2f/(count-2f)*i));
                //TODO Y方向上的位置（距离）
                int top = (int) (mRadius*Math.sin(Math.PI/2f/(count-2f)*i));

//                Log.e(TAG,"*****\nFirst value left:"+left+"   ---   top:"+top);
                //TODO 计算展开菜单后，对应子菜单的位置
                switch(mPosition)
                {
                    case LEFT_TOP:
                        break;
                    case LEFT_BOTTOM:
                        top = getMeasuredHeight() - top-childView.getMeasuredHeight();
                        break;
                    case RIGHT_TOP:
                        left = getMeasuredWidth() - left-childView.getMeasuredWidth();
                        break;
                    case RIGHT_BOTTOM:
                        left = getMeasuredWidth() - left-childView.getMeasuredWidth();
                        top = getMeasuredHeight() - top-childView.getMeasuredHeight();
//                        Log.e(TAG,"Init value left:"+left+"   ---   top:"+top +"  ---  Child Width:"+childView.getMeasuredWidth()+"  ---  Child Height:"+childView.getMeasuredHeight());
                        break;
                    case MIDDLE_TOP:
                        div = Math.PI/2;
                        radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                        top = (int) (mRadius*Math.sin(radius));
                        countLeft = 2 * (i+1);
                        countRight = count;
                        if(countLeft==countRight){
                            top = mRadius;
                            left = 0;
                        }else{
                            if(div>radius){
                                left = (int) (mRadius*Math.cos(radius));
                            }else{
                                left = -1*((int) (mRadius*Math.cos(radius)));
                            }
                        }

                        if(div>radius){//TODO 没过一半的位置
                            left = getMeasuredWidth()/2 - left - childView.getMeasuredWidth()/2;
                        }else{//TODO 超过一半位置
                            left = getMeasuredWidth()/2 + left - childView.getMeasuredWidth()/2;
                        }
                        break;
                    case MIDDLE_BOTTOM:
                        div = Math.PI/2;
                        radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                        top = (int) (mRadius*Math.sin(radius));
                        countLeft = 2 * (i+1);
                        countRight = count;
                        if(countLeft==countRight){
                            top = mRadius;
                            left = 0;
                        }else{
                            if(div>radius){
                                left = (int) (mRadius*Math.cos(radius));
                            }else{
                                left = -1*((int) (mRadius*Math.cos(radius)));
                            }
                        }

                        if(div>radius){//TODO 没过一半的位置
                            left = getMeasuredWidth()/2 - left - childView.getMeasuredWidth()/2;
                        }else{//TODO 超过一半位置
                            left = getMeasuredWidth()/2 + left - childView.getMeasuredWidth()/2;
                        }
                        top = getMeasuredHeight() - top - childView.getMeasuredHeight();
                        break;
                    case CENTER_TOP:
                        //TODO 需要注意的几点信息，Menu包含了子菜单与主菜单按钮
                        //TODO count 计算的值包含了主按钮
                        //TODO 子按钮序号从1开始，0号为主按钮
                        //TODO 获取子View时，若i从0开始，则调用getChild时需要在i的基础上+1
                        //TODO 弧度的计算与子View有关，需要保证子View对应的位置的正确与否
                        //
                        div = Math.PI/2;
                        radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                        top = (int) (mRadius*Math.sin(radius));
                        countLeft = 2 * (i+1);
                        countRight = count;
                        if(countLeft==countRight){
                            top = mRadius;
                            left = 0;
                        }else{
                            if(div>radius){
                                left = (int) (mRadius*Math.cos(radius));
                            }else{
                                left = -1*((int) (mRadius*Math.cos(radius)));
                            }
                        }
                        if(div>radius){//TODO 没过一半的位置
                            left = getMeasuredWidth()/2 - left - childView.getMeasuredWidth()/2;
                        }else{//TODO 超过一半位置
                            left = getMeasuredWidth()/2 + left - childView.getMeasuredWidth()/2;
                        }
                        top = getMeasuredHeight()/2 - top - childView.getMeasuredHeight()/2;
                        break;
                    case CENTER_BOTTOM:
                        div = Math.PI/2;
                        radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                        top = (int) (mRadius*Math.sin(radius));
                        countLeft = 2 * (i+1);
                        countRight = count;
                        if(countLeft==countRight){
                            top = mRadius;
                            left = 0;
                        }else{
                            if(div>radius){
                                left = (int) (mRadius*Math.cos(radius));
                            }else{
                                left = -1*((int) (mRadius*Math.cos(radius)));
                            }
                        }

                        if(div>radius){//TODO 没过一半的位置
                            left = getMeasuredWidth()/2 - left - childView.getMeasuredWidth()/2;
                        }else{//TODO 超过一半位置
                            left = getMeasuredWidth()/2 + left - childView.getMeasuredWidth()/2;
                        }
                        top = getMeasuredHeight()/2 + top - childView.getMeasuredHeight()/2;
                        break;
                }
                childView.layout(left, top, left+childView.getMeasuredWidth(),
                        top+childView.getMeasuredHeight());
            }
        }


    }
    /**
     * TODO 主菜单位置一般不会移动
     * 菜单按钮的布局
     */
    private void layoutMainMenu() {

        mCBMenu = getChildAt(0);//获得主菜单按钮

        mCBMenu.setOnClickListener(this);

        int left=0;
        int top=0;

        switch(mPosition)
        {
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

        mCBMenu.layout(left, top, left+mCBMenu.getMeasuredWidth(), top+mCBMenu.getMeasuredHeight());
    }
    /**
     * 菜单按钮的点击事件
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
     * @param duration 菜单选项的动画时间
     */
    private void dealChildMenu(int duration)
    {

        //下面的代码为菜单选项设置动画

        int count = getChildCount();
        for(int i=0;i<count-1;i++)
        {
            final View childView = getChildAt(i+1);

            AnimationSet set = new AnimationSet(true);

            //1.首先是平移动画
            TranslateAnimation tAnimation = null;

            //平移的x方向和y方向的距离
            int x = (int) (mRadius*Math.cos(Math.PI/2/(count-2)*i));
            int y = (int) (mRadius*Math.sin(Math.PI/2/(count-2)*i));




            //平移的标志，是平移一个正数还以一个负数
            int xflag =1;
            int yflag =1;

            if(mPosition == Position.LEFT_TOP||mPosition == Position.LEFT_BOTTOM)
            {
                xflag = -1;
            }
            if(mPosition == Position.LEFT_TOP||mPosition == Position.RIGHT_TOP)
            {
                yflag = -1;
            }
            /**
             * 左上
             xflag =-1;
             yflag =-1;
             * 右上
             xflag =1;
             yflag =-1;
             * 左下
             xflag =-1;
             yflag =1;
             * 右下
             xflag =1;
             yflag =1;
             */
            //TODO 设置子菜单移动的关键部分
            if(mCurStatus == Status.CLOSE)//如果当前状态为关闭则应该打开
            {
                if(mPosition == Position.RIGHT_BOTTOM||mPosition == Position.LEFT_BOTTOM ||
                        mPosition == Position.LEFT_TOP||mPosition == Position.RIGHT_TOP){
                    tAnimation = new TranslateAnimation(xflag*x, 0,
                            yflag*y, 0);
                }else if(mPosition == Position.MIDDLE_BOTTOM || mPosition == Position.CENTER_TOP){
                    div = Math.PI/2;
                    radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                    y = (int) (mRadius*Math.sin(radius));
                    countLeft = 2 * (i+1);
                    countRight = count;
                    if(countLeft==countRight){
                        y = mRadius;
                        x = 0;
                    }else{
                        if(div>radius){
                            x = (int) (mRadius*Math.cos(radius));
                        }else{
                            x = -1*((int) (mRadius*Math.cos(radius)));
                        }
                    }
                    if(div>radius){//与右下一致
                        xflag =1;
                        yflag =1;
                    }else{//与左下一致
                        xflag =-1;
                        yflag =1;
                    }
                    tAnimation = new TranslateAnimation(xflag*x, 0,
                            yflag*y, 0);
                }else if(mPosition == Position.MIDDLE_TOP || mPosition == Position.CENTER_BOTTOM){
                    div = Math.PI/2;
                    radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                    y = (int) (mRadius*Math.sin(radius));
                    countLeft = 2 * (i+1);
                    countRight = count;
                    if(countLeft==countRight){
                        y = mRadius;
                        x = 0;
                    }else{
                        if(div>radius){
                            x = (int) (mRadius*Math.cos(radius));
                        }else{
                            x = -1*((int) (mRadius*Math.cos(radius)));
                        }
                    }
                    if(div>radius){//与右下一致
                        xflag =1;
                        yflag =-1;
                    }else{//与左下一致
                        xflag =-1;
                        yflag =-1;
                    }
                    tAnimation = new TranslateAnimation(xflag*x, 0,
                            yflag*y, 0);
                }else{
                    tAnimation = new TranslateAnimation(xflag*x, 0,
                            yflag*y, 0);
                }

                tAnimation.setDuration(duration);
                tAnimation.setFillAfter(true);
//                Log.e(TAG,"CLOSE  ---   FromX:"+(xflag*x)+" --- ToX:0 \nCLOSE  ---   FromY:"+(yflag*y)+" --- ToY:0"+" --- which:"+i);
            }else//否则为打开状态，就应该关闭
            {
                if(mPosition == Position.RIGHT_BOTTOM||mPosition == Position.LEFT_BOTTOM ||
                        mPosition == Position.LEFT_TOP||mPosition == Position.RIGHT_TOP) {
                    tAnimation = new TranslateAnimation(0, xflag * x,
                            0, yflag * y);
                }else if(mPosition == Position.MIDDLE_BOTTOM || mPosition == Position.CENTER_TOP){
                    div = Math.PI/2;
                    radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                    y = (int) (mRadius*Math.sin(radius));
                    countLeft = 2 * (i+1);
                    countRight = count;
                    if(countLeft==countRight){
                        y = mRadius;
                        x = 0;
                    }else{
                        if(div>radius){
                            x = (int) (mRadius*Math.cos(radius));
                        }else{
                            x = -1*((int) (mRadius*Math.cos(radius)));
                        }
                    }
                    if(div>radius){//与右下一致
                        xflag =1;
                        yflag =1;
                    }else{//与左下一致
                        xflag =-1;
                        yflag =1;
                    }
                    tAnimation = new TranslateAnimation(0, xflag * x,
                            0, yflag * y);
                }else if(mPosition == Position.MIDDLE_TOP || mPosition == Position.CENTER_BOTTOM){
                    div = Math.PI/2;
                    radius = Math.PI/6.0 + Math.PI*i*2.0/((count-2)*3.0);
                    y = (int) (mRadius*Math.sin(radius));
                    countLeft = 2 * (i+1);
                    countRight = count;
                    if(countLeft==countRight){
                        y = mRadius;
                        x = 0;
                    }else{
                        if(div>radius){
                            x = (int) (mRadius*Math.cos(radius));
                        }else{
                            x = -1*((int) (mRadius*Math.cos(radius)));
                        }
                    }
                    if(div>radius){//与右下一致
                        xflag =1;
                        yflag =-1;
                    }else{//与左下一致
                        xflag =-1;
                        yflag =-1;
                    }
                    tAnimation = new TranslateAnimation(0, xflag * x,
                            0, yflag * y);
                }else{
                    tAnimation = new TranslateAnimation(0, xflag * x,
                            0, yflag * y);
                }

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

                    if(mCurStatus == Status.CLOSE)
                    {
                        childView.setVisibility(GONE);
                        childView.setClickable(false);
                        childView.setFocusable(false);
                    }
                    if(mCurStatus == Status.OPEN)
                    {
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
            final int cPos = i+1;
            childView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    clickAnimation(cPos);//点击动画

                    if(mListener != null)//处理点击事件的逻辑
                    {
                        mListener.dealMenuClick(childView);
                    }

                    changeStatus();


                }
            });


        }

        changeStatus();//动画完成后，要改变状态

    }
    /**
     * 改变状态
     */
    private void changeStatus() {

        mCurStatus = (mCurStatus == Status.CLOSE?Status.OPEN:Status.CLOSE);

    }
    /**
     * 菜单项的点击动画
     * @param cPos  用来判断当前点击的是哪一个菜单
     */
    private void clickAnimation(int cPos) {

        for(int i=0;i<getChildCount()-1;i++)
        {
            View childView = getChildAt(i+1);

            if(i+1== cPos)
            {
                AnimationSet set = new AnimationSet(true);
                ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
                sAnimation.setFillAfter(true);
                AlphaAnimation alAnimation = new AlphaAnimation(1.0f, 0f);
                alAnimation.setFillAfter(true);

                set.addAnimation(sAnimation);
                set.addAnimation(alAnimation);

                set.setDuration(300);
                childView.startAnimation(set);

            }else
            {
                AnimationSet set = new AnimationSet(true);
                ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
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

}
