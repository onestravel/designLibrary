package org.onestravel.design.roundProgress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.onestravel.design.R;



/**
 * @author wanghu
 * @version 1.0.0
 * @projectName MyApplication
 * @desctrion
 * @createTime 2017/4/19.
 */

public class RoundProgressBar extends View {
	private Context mContext;
	private int roundColor = Color.GRAY;//进度条默认颜色
	private int roundProgressColor = Color.RED;//进度条进度的颜色
	private int roundCircleColor = Color.TRANSPARENT;//圆形进度条内圆颜色
	private int progressTextColor = Color.RED;//进度字体颜色
	private int roundWidth = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());//进度条进度（圆环）的宽度
	private int progressTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());//进度字体大小
	private String text = null;//圆形进度条内字
	int centre = 0; //获取圆心的x坐标
	private int circleRadius = 0;//圆弧的半径
	/**
	 * 最大进度
	 */
	private float maxProgress = 100f;

	/**
	 * 当前进度
	 */
	private float progress = 0f;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable = true;
	/**
	 * 进度是否是实心的
	 */
	private boolean roundIsFill = false;
	private Paint defaultRoundPaint;
	private Paint progressRoundPaint;
	private Paint progressTextPaint;
	private Paint defaultCircleRoundPaint;


	public RoundProgressBar(Context context) {
		super(context);
		getAttributrs(context, null, 0);
		initPaint();
	}

	public RoundProgressBar(Context context,AttributeSet attrs) {
		super(context, attrs);
		getAttributrs(context, attrs, 0);
		initPaint();
	}

	public RoundProgressBar(Context context,  AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getAttributrs(context, attrs, defStyleAttr);
		initPaint();
	}

	private void getAttributrs(Context context, AttributeSet attrs, int defStyleAttr) {
		this.mContext = context;
		TypedArray typeArr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgress, defStyleAttr, 0);
		int n = typeArr.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = typeArr.getIndex(i);
			if (attr == R.styleable.RoundProgress_roundColor) {
				roundColor = typeArr.getColor(attr, Color.GRAY);

			} else if (attr == R.styleable.RoundProgress_roundProgressColor) {
				roundProgressColor = typeArr.getColor(attr, Color.GREEN);

			} else if (attr == R.styleable.RoundProgress_roundCircleColor) {
				roundCircleColor = typeArr.getColor(attr, Color.TRANSPARENT);

			} else if (attr == R.styleable.RoundProgress_progressTextColor) {
				progressTextColor = typeArr.getColor(attr, Color.GREEN);

			} else if (attr == R.styleable.RoundProgress_roundWidth) {
				roundWidth = typeArr.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.RoundProgress_progressTextSize) {
				progressTextSize = typeArr.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.RoundProgress_textIsDisplayable) {
				textIsDisplayable = typeArr.getBoolean(attr, true);

			} else if (attr == R.styleable.RoundProgress_roundIsFill) {
				roundIsFill = typeArr.getBoolean(attr, true);

			}
		}

	}

	private Paint creatPaint(int paintColor, int textSize, Paint.Style style, int roundWidth) {
		Paint paint = new Paint();
		paint.setColor(paintColor);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(roundWidth);
		paint.setDither(true);
		paint.setTextSize(textSize);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setStyle(style);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		return paint;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		centre = w / 2; //获取圆心的x坐标
		int padding = getPaddingLeft() > getPaddingTop() ? getPaddingLeft() : getPaddingTop();
		circleRadius = centre - roundWidth / 2 - 2 - padding; //圆弧的半径（圆心到x的距离减去园弧线宽度的1/2，再减去2作为修正）
	}

	private void initPaint() {
		defaultRoundPaint = creatPaint(roundColor, 0, Paint.Style.STROKE, roundWidth);
		defaultCircleRoundPaint = creatPaint(roundCircleColor, 0, Paint.Style.FILL, 0);
		progressRoundPaint = creatPaint(roundProgressColor, 0, Paint.Style.STROKE, roundWidth);
		progressTextPaint = creatPaint(progressTextColor, progressTextSize, Paint.Style.FILL, roundWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawDefaultRound(canvas);
		drawCircleRound(canvas);
		drawProgressRound(canvas);
		drawProgressText(canvas);
	}

	private void drawDefaultRound(Canvas canvas) {
		canvas.drawCircle(centre, centre, circleRadius, defaultRoundPaint);
	}

	private void drawCircleRound(Canvas canvas) {
		canvas.drawCircle(centre, centre, circleRadius - roundWidth / 2, defaultCircleRoundPaint);
	}


	private void drawProgressRound(Canvas canvas) {
		RectF oval = new RectF(centre - circleRadius, centre - circleRadius, centre + circleRadius, centre + circleRadius);  //用于定义的圆弧的形状和大小的界限
		if (roundIsFill) {
			progressRoundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		} else {
			progressRoundPaint.setStyle(Paint.Style.STROKE);
		}
		canvas.drawArc(oval, -90, 360 * progress / maxProgress, roundIsFill, progressRoundPaint);  //根据进度画圆弧
	}

	private void drawProgressText(Canvas canvas) {
		if (textIsDisplayable) {
			String text = String.valueOf((int) (progress * 100f / maxProgress)) + "%";
			canvas.drawText(text, centre, centre + progressTextSize / 2 - roundWidth / 2, progressTextPaint);
		}

	}

	public int getRoundColor() {
		return roundColor;
	}

	public void setRoundColor(int roundColor) {
		this.roundColor = roundColor;
		postInvalidate();
	}

	public int getRoundProgressColor() {
		return roundProgressColor;
	}

	public void setRoundProgressColor(int roundProgressColor) {
		this.roundProgressColor = roundProgressColor;
		postInvalidate();
	}

	public int getRoundCircleColor() {
		return roundCircleColor;
	}

	public void setRoundCircleColor(int roundCircleColor) {
		this.roundCircleColor = roundCircleColor;
		postInvalidate();
	}

	public int getProgressTextColor() {
		return progressTextColor;
	}

	public void setProgressTextColor(int progressTextColor) {
		this.progressTextColor = progressTextColor;
		postInvalidate();
	}

	public int getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(int roundWidth) {
		this.roundWidth = roundWidth;
		postInvalidate();
	}

	public int getProgressTextSize() {
		return progressTextSize;
	}

	public void setProgressTextSize(int progressTextSize) {
		this.progressTextSize = progressTextSize;
		postInvalidate();
	}

	public synchronized float getMaxProgress() {
		return maxProgress;
	}

	public synchronized void setMaxProgress(float maxProgress) {
		this.maxProgress = maxProgress;
	}

	public synchronized float getProgress() {
		return progress;
	}

	public synchronized void setProgress(float progress) {
		if (progress < 0) {
			progress = 0;
		}
		if (progress > maxProgress) {
			progress = maxProgress;
		}
		if (progress <= maxProgress) {
			this.progress = progress;
			postInvalidate();
		}
	}

	public boolean isTextIsDisplayable() {
		return textIsDisplayable;
	}

	public void setTextIsDisplayable(boolean textIsDisplayable) {
		this.textIsDisplayable = textIsDisplayable;
		postInvalidate();
	}

	public boolean isRoundIsFill() {
		return roundIsFill;
	}

	public void setRoundIsFill(boolean roundIsFill) {
		this.roundIsFill = roundIsFill;
		postInvalidate();
	}


	@Override
	public void invalidate() {
		initPaint();
		super.invalidate();
	}

	@Override
	public void postInvalidate() {
		initPaint();
		super.postInvalidate();
	}
}
