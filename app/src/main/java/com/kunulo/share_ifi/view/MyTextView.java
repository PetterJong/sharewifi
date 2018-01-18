package com.kunulo.share_ifi.view;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyTextView extends TextView {

	private boolean isBold = false;

	private final int TOUCH_ADDITION = 0;
    private int mTouchAdditionBottom = 0;
    private int mTouchAdditionLeft = 0;
    private int mTouchAdditionRight = 0;
    private int mTouchAdditionTop = 0;
    private int mPreviousLeft = -1;
    private int mPreviousRight = -1;
    private int mPreviousBottom = -1;
    private int mPreviousTop = -1;

	public MyTextView(Context context) {
		super(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
		init(context,attrs);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context,attrs);
	}

    private void init(Context context, AttributeSet attrs) {  
//        TypedArray a = context.obtainStyledAttributes(attrs,
//                R.styleable.LargeTouchableAreaView);
//        isBold = a.getBoolean(R.styleable.textAtt_isBold, false);
//    	setTypeface(TypeFaceManager.getSharedInstance(getContext()).getTypeFace(isBold));
//		getPaint().setFakeBoldText(isBold);
//
//        int addition = (int) a.getDimension(
//                R.styleable.LargeTouchableAreaView_addition, TOUCH_ADDITION);
//        mTouchAdditionBottom = addition;  
//        mTouchAdditionLeft = addition;
//        mTouchAdditionRight = addition;
//        mTouchAdditionTop = addition;
//        mTouchAdditionBottom = (int) a.getDimension(
//                R.styleable.LargeTouchableAreaView_additionBottom,
//                mTouchAdditionBottom);
//        mTouchAdditionLeft = (int) a.getDimension(
//                R.styleable.LargeTouchableAreaView_additionLeft,
//                mTouchAdditionLeft);
//        mTouchAdditionRight = (int) a.getDimension(
//                R.styleable.LargeTouchableAreaView_additionRight,
//                mTouchAdditionRight);
//        mTouchAdditionTop = (int) a.getDimension(
//                R.styleable.LargeTouchableAreaView_additionTop,
//                mTouchAdditionTop);
//        a.recycle();
    }  
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    	// TODO Auto-generated method stub
    	super.onLayout(changed, left, top, right, bottom);
        if (left != mPreviousLeft || top != mPreviousTop  
                || right != mPreviousRight || bottom != mPreviousBottom) {  
            mPreviousLeft = left;  
            mPreviousTop = top;  
            mPreviousRight = right;  
            mPreviousBottom = bottom;  
            final View parent = (View) this.getParent();  
            parent.setTouchDelegate(new TouchDelegate(new Rect(left  
                    - mTouchAdditionLeft, top - mTouchAdditionTop, right  
                    + mTouchAdditionRight, bottom + mTouchAdditionBottom), this));  
        }  
    }
		
}
