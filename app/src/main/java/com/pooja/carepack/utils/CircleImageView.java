/*
 * 
 */
package com.pooja.carepack.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * The Class CirculImageView.
 */
public class CircleImageView extends ImageView {

	/**
	 * Instantiates a new circul image view.
	 *
	 * @param context the context
	 */
	public CircleImageView(Context context) {
		super(context);
	}

	/**
	 * Instantiates a new circul image view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Instantiates a new circul image view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/* (non-Javadoc)
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null)
			return;
		if (getWidth() == 0 || getHeight() == 0)
			return;
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
		Bitmap roundBitmap = getCircleBitmap(bitmap, getWidth());

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#450faf"));
		paint.setStrokeWidth(1.0f);

//		canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() /2, paint);
		canvas.drawBitmap(roundBitmap, 0, 0, null);
	}

	/**
	 * Gets the circle bitmap.
	 *
	 * @param bmp the bmp
	 * @param radius the radius
	 * @return the circle bitmap
	 */
	public static Bitmap getCircleBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;

		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas c = new Canvas(output);
		c.drawARGB(0, 0, 0, 0);
		c.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2 - 6, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
		c.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

}