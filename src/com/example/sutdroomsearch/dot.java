package com.example.sutdroomsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class dot extends View{
	private final float x;
	private final float y;
	private final int r;
	private final Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public dot(Context context, float x, float y, int r){
		super(context);
		mPaint.setColor(0xFFFF0000);
		this.x=x;
		this.y=y;
		this.r=r;
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawCircle(x, y, r, mPaint);
	}

}
