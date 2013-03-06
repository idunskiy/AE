package com.customitems;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class Triangle extends View {

    private int vertexA, vertexB, vertexC;

    public Triangle(Context ctx){
        this(ctx,null);
    }

    public Triangle(Context ctx, AttributeSet attrs){
        this(ctx,attrs,0);
    }
//
//
    public Triangle(Context ctx, AttributeSet attrs, int defStyle){
        super(ctx,attrs,defStyle);
    }
//
//
//    public void setSides(int a, int b, int c){
//        this.vertexA = a;
//        this.vertexB = b;
//        this.vertexC = c;
//        this.invalidate();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        // Try for a width based on our minimum
//        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
//
//        // Whatever the width ends up being, ask for a height that would let the triangle
//       // get as big as it can
//        int minh = MeasureSpec.getSize(w) - (int)mTextWidth + getPaddingBottom() + getPaddingTop();
//        int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)mTextWidth, heightMeasureSpec, 0);
//
//        setMeasuredDimension(w, h);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        super.onDraw(canvas);
//
//        Paint paint = new Paint();
//        Path path = new Path();
//
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.TRANSPARENT);
//
//        canvas.drawPaint(paint);
//
//        // start the path at the "origin"
//        path.moveTo(10,10); // origin
//        // add a line for side A
//        path.lineTo(10,this.vertexA);
//        // add a line for side B
//        path.lineTo(this.vertexB,10);
//        // close the path to draw the hypotenuse
//        path.close();
//
//        paint.setStrokeWidth(3);
//        paint.setPathEffect(null);
//        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.STROKE);
//
//        canvas.drawPath(path, paint);
//
//   } 

}