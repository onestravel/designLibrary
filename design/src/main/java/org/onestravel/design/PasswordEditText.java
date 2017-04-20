package org.onestravel.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.onestravel.design.R;


/**
 * @author wanghu
 * @version 1.0.0
 * @projectName MyApplication
 * @desctrion
 * @createTime 2017/4/19.
 */

public class PasswordEditText extends EditText {
	private Context mContext;
	//输入框背景色
	private int pwdBackgroundColor = Color.TRANSPARENT;
	//密码（字体或小黑点）颜色
	private int passwordColor = Color.BLACK;
	//输入框边框颜色
	private int borderColor = Color.GRAY;
	//输入框字体大小
	private int passwordSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
	//输入框密码小黑点大小
	private int passwordWidth = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
	//输入框密码边框宽度
	private int borderWidth = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
	//设置密码是否可见
	private boolean pwdIsVisible = false;
	private Paint rectBorderPaint;//矩形边框画笔
	private Paint linePaint;//分割线画笔
	private Paint pwdCirclePaint;//密码小黑点画笔
	private Paint pwdTextPaint;//密码文本画笔
	private Paint backgroundPaint;//输入框背景

	private float endX = 0;//矩形结束的x坐标
	private float endY = 0;//矩形结束的y坐标
	private int width;//view的宽度
	private int height;//view的高度
	private int pwdLength = 6;//密码长度
	private int textLength = 0;//当前输入密码的长度
	//密码输入完成的监听事件
	private OnPasswordInputFinishListener listener;


	public PasswordEditText(Context context) {
		super(context);
		getAttributrs(context, null, 0);
		initPaint();
	}

	public PasswordEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttributrs(context, attrs, 0);
		initPaint();
	}

	public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getAttributrs(context, attrs, defStyleAttr);
		initPaint();
	}

	/**
	 * 获取自定义 属性
	 *
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	private void getAttributrs(Context context, AttributeSet attrs, int defStyleAttr) {
		this.mContext = context;
		TypedArray typeArr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordEditText, defStyleAttr, 0);
		int n = typeArr.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = typeArr.getIndex(i);
			if (attr == R.styleable.PasswordEditText_pwdBackgroundColor) {
				pwdBackgroundColor = typeArr.getColor(attr, Color.TRANSPARENT);

			} else if (attr == R.styleable.PasswordEditText_passwordColor) {
				passwordColor = typeArr.getColor(attr, Color.GRAY);

			} else if (attr == R.styleable.PasswordEditText_borderColor) {
				borderColor = typeArr.getColor(attr, Color.GREEN);

			} else if (attr == R.styleable.PasswordEditText_passwordSize) {
				passwordSize = typeArr.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.PasswordEditText_passwordWidth) {
				passwordWidth = typeArr.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.PasswordEditText_borderWidth) {
				borderWidth = typeArr.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));

			} else if (attr == R.styleable.PasswordEditText_pwdIsVisible) {
				pwdIsVisible = typeArr.getBoolean(attr, true);

			} else if (attr == R.styleable.PasswordEditText_pwdLength) {
				pwdLength = typeArr.getInteger(attr, 6);

			}
		}
		typeArr.recycle();
		this.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
		this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(pwdLength)}); //最大输入长度
		this.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
		this.setSingleLine();
		this.setEllipsize(TextUtils.TruncateAt.END);
		this.setTextColor(Color.TRANSPARENT);//设置字体颜色透明不显示
		this.setCursorVisible(false);//设置光标不显示
		this.setSelected(false);
		this.setHighlightColor(Color.TRANSPARENT);
	}

	/**
	 * 创建一个画笔
	 *
	 * @param paintColor 画笔颜色
	 * @param textSize   文字大小
	 * @param style      画笔样式
	 * @param roundWidth 画笔宽度
	 * @return
	 */
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

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		backgroundPaint = creatPaint(pwdBackgroundColor, 0, Paint.Style.FILL_AND_STROKE, borderWidth);
		rectBorderPaint = creatPaint(borderColor, 0, Paint.Style.STROKE, borderWidth);
		linePaint = creatPaint(borderColor, 0, Paint.Style.FILL, borderWidth);
		pwdCirclePaint = creatPaint(passwordColor, 0, Paint.Style.FILL, passwordWidth);
		pwdTextPaint = creatPaint(passwordColor, passwordSize, Paint.Style.FILL, 0);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = getWidth();
		height = getHeight();
		endX = w;
		endY = h;
	}


	/**
	 * 重写ondraw方法，进行view的重新绘画
	 *
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBackGround(canvas);
		drawRectBorder(canvas);
		drawLine(canvas);
		if (pwdIsVisible) {
			drawPwdText(canvas);
		} else {
			drawPwdCircle(canvas);
		}
	}


	/**
	 * 密码输入框背景色
	 *
	 * @param canvas
	 */
	private void drawBackGround(Canvas canvas) {
		RectF rectf = new RectF(borderWidth / 2, borderWidth / 2, endX - borderWidth / 2, endY - borderWidth / 2);
		canvas.drawRoundRect(rectf, 20, 20, backgroundPaint);
	}

	/**
	 * 密码输入框边框
	 *
	 * @param canvas
	 */
	private void drawRectBorder(Canvas canvas) {
		RectF rectf = new RectF(borderWidth / 2, borderWidth / 2, endX - borderWidth / 2, endY - borderWidth / 2);
		canvas.drawRoundRect(rectf, 20, 20, rectBorderPaint);
	}

	/**
	 * 输入框分割线
	 *
	 * @param canvas
	 */
	private void drawLine(Canvas canvas) {
		for (int i = 1; i < pwdLength; i++) {
			float x = width * i / pwdLength;
			canvas.drawLine(x, 0, x, height, linePaint);
		}
	}

	/**
	 * 密码输入框表示密码的黑点
	 *
	 * @param canvas
	 */
	private void drawPwdCircle(Canvas canvas) {
		// 密码
		float cx, cy = height / 2;
		float half = width / pwdLength / 2;
		for (int i = 0; i < textLength; i++) {
			cx = width * i / pwdLength + half;
			canvas.drawCircle(cx, cy, passwordWidth, pwdCirclePaint);
		}
	}

	/**
	 * 密码输入框密码文本显示
	 *
	 * @param canvas
	 */
	private void drawPwdText(Canvas canvas) {
		// 密码
		float cx = height / 2;
		float cy = height / 2 + (passwordSize / 2) - borderWidth * 2;
		float half = width / pwdLength / 2;
		char[] charArray = getText().toString().toCharArray();
		for (int i = 0; i < textLength; i++) {
			if (i < charArray.length) {
				cx = width * i / pwdLength + half;
				String text = String.valueOf(charArray[i]);
				canvas.drawText(text, cx, cy, pwdTextPaint);
			}
		}
	}

	/**
	 * 输入框内容变化监听事件
	 *
	 * @param text
	 * @param start
	 * @param lengthBefore
	 * @param lengthAfter
	 */
	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		textLength = text.length();
		postInvalidate();
		if (listener != null) {
			listener.onTextChanged(this, text, start, lengthBefore, lengthAfter);
		}
		if (textLength == pwdLength && listener != null) {
			listener.onInputFinished(this, text);
		}

	}

	/**
	 * 键盘点击事件的监听，设置光标一直在文本的最后
	 *
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		String text = getText().toString();
		if (!TextUtils.isEmpty(text)) {
			this.setSelection(text.length());
		}
		this.setSelected(false);
		return super.onKeyDown(keyCode, event);
	}


	public boolean isPwdIsVisible() {
		return pwdIsVisible;
	}

	public void setPwdIsVisible(boolean pwdIsVisible) {
		this.pwdIsVisible = pwdIsVisible;
		postInvalidate();
	}

	public void clear() {
		this.setText("");
		postInvalidate();
	}


	public void setOnPasswordInputFinishListener(OnPasswordInputFinishListener listener) {
		this.listener = listener;
	}

	/**
	 * 密码输入完成回调事件
	 */
	public interface OnPasswordInputFinishListener {
		public void onTextChanged(View view, CharSequence password, int start, int lengthBefore, int lengthAfter);

		public void onInputFinished(View view, CharSequence password);
	}


}
