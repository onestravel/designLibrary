package org.onestravel.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.onestravel.design.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanghu
 * @version 1.0.0
 * @projectName MyApplication
 * @desctrion
 * @createTime 2017/3/23.
 */

public class CustomerSignInView extends View {
	private Context mContext;
	private int circleBgColor = Color.WHITE;//
	private int circleUnSignInColor = Color.GRAY;
	private int circleSignInColor = Color.GREEN;
	private int lineColor = Color.WHITE;
	private int lineHeight = 5;
	private int circleRadius = 20;
	private int signPadding = 20;
	private Paint linePaint;
	private Paint circleBgPaint;
	private Paint signOutPaint;
	private Paint signInPaint;
	private int signTextColor = Color.DKGRAY;
	private int signTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
	private Paint signInTextPaint;
	private List<String> signList;
	private List<Integer> checkedList;
	private int lineHeightInView;
	private int textHeightInView;
	private Paint signInCheckPaint;
	private Paint lineSignPaint;
	private int signTextMarginTop = 20;
	private int currentPosition = 0;

	public CustomerSignInView(Context context) {
		super(context);
		this.mContext = context;
		this.signList = new ArrayList<>();
		this.checkedList = new ArrayList<>();
	}

	public CustomerSignInView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.signList = new ArrayList<>();
		this.checkedList = new ArrayList<>();
		getAttributrs(context, attrs, 0);
		initPaint();
	}

	public CustomerSignInView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		this.signList = new ArrayList<>();
		this.checkedList = new ArrayList<>();
		getAttributrs(context, attrs, defStyleAttr);
		initPaint();
	}

	private void getAttributrs(Context context, AttributeSet attrs, int defStyleAttr) {
		if (attrs == null) {
			return;
		}
		/**
		 * 获得我们所定义的自定义样式属性
		 */
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomerSignInView, defStyleAttr, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			if (attr == R.styleable.CustomerSignInView_circleBgColor) {
				circleBgColor = a.getColor(attr, Color.WHITE);

			} else if (attr == R.styleable.CustomerSignInView_circleUnSignInColor) {// 默认颜色设置为黑色
				circleUnSignInColor = a.getColor(attr, Color.GRAY);

			} else if (attr == R.styleable.CustomerSignInView_circleSignInColor) {
				circleSignInColor = a.getColor(attr, Color.GREEN);

			} else if (attr == R.styleable.CustomerSignInView_lineColor) {
				lineColor = a.getColor(attr, Color.GREEN);

			} else if (attr == R.styleable.CustomerSignInView_lineHeight) {
				lineHeight = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.CustomerSignInView_circleRadius) {// 默认设置为16sp，TypeValue也可以把sp转化为px
				circleRadius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.CustomerSignInView_signPadding) {// 默认设置为16sp，TypeValue也可以把sp转化为px
				signPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.CustomerSignInView_signTextColor) {// 默认设置为16sp，TypeValue也可以把sp转化为px
				signTextColor = a.getColor(attr, Color.DKGRAY);

			} else if (attr == R.styleable.CustomerSignInView_signTextSize) {// 默认设置为16sp，TypeValue也可以把sp转化为px
				signTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.CustomerSignInView_signTextMarginTop) {// 默认设置为16sp，TypeValue也可以把sp转化为px
				signTextMarginTop = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

			}

		}
		a.recycle();


	}

	private void initPaint() {
		linePaint = creatPaint(lineColor, 0, Paint.Style.FILL, lineHeight);
		lineSignPaint = creatPaint(circleSignInColor, 0, Paint.Style.FILL, lineHeight);
		circleBgPaint = creatPaint(circleBgColor, 0, Paint.Style.FILL, circleRadius);
		signOutPaint = creatPaint(circleUnSignInColor, 0, Paint.Style.FILL, circleRadius);
		signInPaint = creatPaint(circleSignInColor, 0, Paint.Style.FILL, circleRadius);
		signInTextPaint = creatPaint(signTextColor, signTextSize, Paint.Style.FILL, circleRadius);
		signInCheckPaint = creatPaint(Color.WHITE, circleRadius * 2, Paint.Style.FILL, circleRadius);
	}

	private Paint creatPaint(int paintColor, int textSize, Paint.Style style, int lineWidth) {
		Paint paint = new Paint();
		paint.setColor(paintColor);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(lineWidth);
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
//		int height = (getHeight() - signPadding * 2 - lineHeight) / 2;
//		int y = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
		int height = (signPadding * 2 + circleRadius * 2 + signTextSize + signTextMarginTop);
		if (height > h) {
			h = height;
			setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h));
		}
		lineHeightInView = (h - height) / 2 + circleRadius + signPadding;
		textHeightInView = lineHeightInView + circleRadius + signTextSize + signTextMarginTop;
	}

	private void drawLine(Canvas canvas) {
		canvas.drawLine(signPadding + circleRadius, lineHeightInView, getWidth() - signPadding - circleRadius, lineHeightInView, linePaint);
	}

	private void drawSignLine(Canvas canvas) {
		int size = (getWidth() - signPadding * 2 - circleRadius * 2) / 6;
		if (checkedList.contains(currentPosition)) {
			canvas.drawLine(signPadding + circleRadius, lineHeightInView, currentPosition * size + circleRadius + signPadding, lineHeightInView, lineSignPaint);
		} else {
			canvas.drawLine(signPadding + circleRadius, lineHeightInView, (currentPosition - 1) * size + circleRadius + signPadding, lineHeightInView, lineSignPaint);
		}
	}

	private void drawCircleBg(Canvas canvas) {
		int size = (getWidth() - signPadding * 2 - circleRadius * 2) / 6;
		for (int i = 0; i < signList.size(); i++) {
			canvas.drawCircle(size * i + signPadding + circleRadius, lineHeightInView, circleRadius, circleBgPaint);
		}
	}

	private void drawCircleCheck(int position, Canvas canvas) {
		int length = (getWidth() - signPadding * 2 - circleRadius * 2) / 6;
		canvas.drawText("√", length * position + signPadding + circleRadius, lineHeightInView + circleRadius / 2, signInCheckPaint);
	}

	private void drawCircleSignIn(Canvas canvas) {
		int size = (getWidth() - signPadding * 2 - circleRadius * 2) / 6;
		for (int i = 0; i <= currentPosition; i++) {
			if (checkedList.contains(i)) {
				canvas.drawCircle(size * i + signPadding + circleRadius, lineHeightInView, circleRadius, signInPaint);
				drawCircleCheck(i, canvas);
			} else {
				canvas.drawCircle(size * i + signPadding + circleRadius, lineHeightInView, circleRadius, signOutPaint);
			}
		}
	}

	private void drawSignInText(Canvas canvas) {
		int size = (getWidth() - signPadding * 2 - circleRadius * 2) / 6;
		for (int i = 0; i < signList.size(); i++) {
			canvas.drawText(signList.get(i), size * i + signPadding + circleRadius, textHeightInView, signInTextPaint);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLine(canvas);
		drawSignLine(canvas);
		drawCircleBg(canvas);
		drawCircleSignIn(canvas);
		drawSignInText(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setSignInData(List<String> data) {
		if (!data.isEmpty()) {
			signList.addAll(data);
			invalidate();
		}
	}

	public void setEventSignIn(int current, List<Integer> checkList) {
		if (current < 0) {
			this.currentPosition = 0;
		} else if (current >= signList.size()) {
			this.currentPosition = signList.size() - 1;
		} else {
			this.currentPosition = current;
		}
		if (!checkList.isEmpty()) {
			this.checkedList.addAll(checkList);
		}
		invalidate();
	}

	public int getCircleBgColor() {
		return circleBgColor;
	}

	public void setCircleBgColor(int circleBgColor) {
		this.circleBgColor = circleBgColor;
		invalidate();
	}

	public int getCircleUnSignInColor() {
		return circleUnSignInColor;
	}

	public void setCircleUnSignInColor(int circleUnSignInColor) {
		this.circleUnSignInColor = circleUnSignInColor;
		invalidate();
	}

	public int getCircleSignInColor() {
		return circleSignInColor;
	}

	public void setCircleSignInColor(int circleSignInColor) {
		this.circleSignInColor = circleSignInColor;
		invalidate();
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
		invalidate();
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
		invalidate();
	}

	public int getCircleRadius() {
		return circleRadius;
	}

	public void setCircleRadius(int circleRadius) {
		this.circleRadius = circleRadius;
		invalidate();
	}

	public int getSignPadding() {
		return signPadding;
	}

	public void setSignPadding(int signPadding) {
		this.signPadding = signPadding;
		invalidate();
	}

	public int getSignTextColor() {
		return signTextColor;
	}

	public void setSignTextColor(int signTextColor) {
		this.signTextColor = signTextColor;
		invalidate();
	}

	public int getSignTextSize() {
		return signTextSize;
	}

	public void setSignTextSize(int signTextSize) {
		this.signTextSize = signTextSize;
		invalidate();
	}

	public int getSignTextMarginTop() {
		return signTextMarginTop;
	}

	public void setSignTextMarginTop(int signTextMarginTop) {
		this.signTextMarginTop = signTextMarginTop;
		invalidate();
	}
}
