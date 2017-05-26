package com.fanhaichao.customcalendarview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by FanHaiChao on 2017/5/4.
 * 日历控件
 */
public class CalendarView extends View {
    /**
     * 分割线 画笔
     */
    private Paint mPaintLine;
    /**
     * 文字画笔
     */
    private Paint mPaintTv;

    private int widthSize;
    private String[] weekStr = {"日","一","二","三","四","五","六"};

    private float lineTextDest = 20;//线与文字上、下的间距

    private int currentYear;
    private int currentMonth;
    private int currentDay;

    private int chooseYear;
    private int chooseMonth;
    private int chooseDay;

    private int firstDayOfWeek;//该月第一天是星期几 ------------------日：0	一：1  二：2		三：3		四：4		五：5		六：6

    private int dayNums;//这个月有多少天

    private int viewMargin = 30;//绘制日历文字的距离日历的左右间隔

    private float posweekStrY ;//绘制日期文字的画笔所在的y轴坐标
    private float poslineBottomY;//绘制第二根线的画笔所在的y轴坐标
    private float pointRadius  = 5;//小点半径
    private float pointMarginTop = 20;//小球顶部距离文字的距离

    private float dotMargin  = 0;//圆点之间的距离

    private Paint mPaintPoint;
    private Paint mPaintBg;
    private int canvasWidth;
    private int heightSize;
    private float posMonthDayY;
    private float posMonthDayX;

    public CalendarView(Context context) {
        this(context,null);
    }
    private List<CalendarBean> calendarBeanList;
    public void setDataList(List<CalendarBean> calendarBeanList){
        this.calendarBeanList = calendarBeanList;
        invalidate();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        calendarBeanList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DATE); i++) {
            CalendarBean calendarBean = new CalendarBean();
            calendarBean.day = i+"";
            calendarBeanList.add(calendarBean);
        }
        chooseYear = currentYear = calendar.get(Calendar.YEAR);
        chooseMonth = currentMonth = calendar.get(Calendar.MONTH)+1;
        chooseDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        setChooseYMD(chooseYear,chooseMonth,chooseDay);
    }

    private void initView(){
        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setColor(Color.GRAY);
        mPaintLine.setStrokeWidth(1);

        mPaintTv = new Paint();
        mPaintTv.setAntiAlias(true);
        mPaintTv.setStyle(Paint.Style.FILL);
        mPaintTv.setTextSize(22);
        mPaintTv.setColor(Color.BLACK);
        mPaintTv.setTextAlign(Paint.Align.CENTER);

        mPaintPoint = new Paint();
        mPaintPoint.setAntiAlias(true);
        mPaintPoint.setStyle(Paint.Style.FILL);
        mPaintPoint.setStrokeWidth(0.1f);

        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStyle(Paint.Style.FILL);
        mPaintBg.setColor(Color.parseColor("#E5E5E5"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制顶部横线
        canvasWidth = getWidth();
        canvas.drawLine(0,0, canvasWidth,0,mPaintLine);
        posweekStrY = lineTextDest - (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading);
        //绘制日期
        for (int i = 0; i < weekStr.length; i++) {
            canvas.drawText(weekStr[i], viewMargin + mPaintTv.measureText("11")/2 +
                    i * (canvasWidth - viewMargin*2 - mPaintTv.measureText("11")) / 6, posweekStrY, mPaintTv);
        }


        poslineBottomY = mPaintTv.getTextSize() + 2*lineTextDest;
        //绘制星期的 底部横线
        canvas.drawLine(0 ,poslineBottomY, canvasWidth,poslineBottomY,mPaintLine);

        //绘制日历内容
        for (int j = 0; j < dayNums+ firstDayOfWeek; j++) {
            if (j >= firstDayOfWeek){
                posMonthDayY = poslineBottomY + lineTextDest - (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading)
                        + (j/7) * (mPaintTv.getTextSize() + lineTextDest + 2 * pointRadius + pointMarginTop);
                posMonthDayX = viewMargin + mPaintTv.measureText("11") / 2
                        + (j%7) * (canvasWidth - viewMargin * 2 - mPaintTv.measureText("11")) / 6;
                float textCenterY = posMonthDayY + (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading) + mPaintTv.getTextSize()/2;

                RectF rect = new RectF((float) (posMonthDayX - mPaintTv.measureText("11") / 2)-6,
                        (float) (posMonthDayY - (-mPaintTv.getFontMetrics().ascent - mPaintTv.getFontMetrics().leading))-6,
                        (float) (posMonthDayX + mPaintTv.measureText("11") / 2)+6,
                        (float) (posMonthDayY + mPaintTv.getFontMetrics().descent)+6
                );
                //绘制 "签到" 背景
                if ("1".equals(calendarBeanList.get(j - firstDayOfWeek).status)){
                    Bitmap bitmapSign = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.bg_sign);
                    canvas.drawBitmap(bitmapSign,null,rect,mPaintBg);
                }

                if (calendarBeanList.get(j - firstDayOfWeek).day.equals(chooseDay+"")){
                    //绘制 "选中" 背景
                    canvas.drawCircle(posMonthDayX, textCenterY, mPaintTv.measureText("11") / 2 + 6, mPaintBg);
                    mPaintTv.setColor(Color.BLUE);
                }else {
                    mPaintTv.setColor(Color.BLACK);
                }

                //绘制 "当天" 的背景
                if (currentYear == chooseYear && currentMonth == chooseMonth && calendarBeanList.get(j - firstDayOfWeek).day.equals(currentDay+"")) {
                    Bitmap bitmapCurrentDay = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.circle_day);
                    canvas.drawBitmap(bitmapCurrentDay,null,rect,mPaintBg);
                }

                //绘制日期文字12345
                canvas.drawText(calendarBeanList.get(j - firstDayOfWeek).day,
                        posMonthDayX, posMonthDayY, mPaintTv);

                int paintCount = calendarBeanList.get(j-firstDayOfWeek).getPaintColors().size();
                //绘制圆点
                for (int i = 0; i < paintCount; i++) {
                    mPaintPoint.setColor(Color.parseColor(calendarBeanList.get(j-firstDayOfWeek).getPaintColors().get(i)));
                    if (paintCount%2 == 0){
                        canvas.drawCircle(posMonthDayX -(2*i-1)*(dotMargin/2+pointRadius)
                                , posMonthDayY +pointRadius+pointMarginTop,pointRadius,mPaintPoint);
                    }else {
                        canvas.drawCircle(posMonthDayX -((int)Math.pow(-1,i+1)*(i+1)/2)*(dotMargin+2*pointRadius)
                                , posMonthDayY +pointRadius+pointMarginTop,pointRadius,mPaintPoint);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //判断 点击的是哪一天
                for (int j = 0; j < dayNums+ firstDayOfWeek; j++) {
                    if (j >= firstDayOfWeek){
                        float posMonthDayY = poslineBottomY + lineTextDest - (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading)
                                + (j/7) * (mPaintTv.getTextSize() + lineTextDest + 2 * pointRadius + pointMarginTop);
                        float posMonthDayX = viewMargin + mPaintTv.measureText("11") / 2
                                + (j%7) * (canvasWidth - viewMargin * 2 - mPaintTv.measureText("11")) / 6;
                        if (x > posMonthDayX - mPaintTv.measureText("11") / 2-6 &&
                                x < posMonthDayX + mPaintTv.measureText("11") / 2+6 &&
                                y > posMonthDayY - (-mPaintTv.getFontMetrics().ascent - mPaintTv.getFontMetrics().leading)-6 &&
                                y < posMonthDayY + mPaintTv.getFontMetrics().descent+6){
                            //只有小于当前日期的才能点击

                            if (currentYear>chooseYear ||
                                    (currentYear == chooseYear && currentMonth>chooseMonth) ||
                                    (currentYear == chooseYear && currentMonth == chooseMonth && currentDay >= j - firstDayOfWeek + 1)) {

                                setChooseYMD(chooseYear, chooseMonth, j - firstDayOfWeek + 1);
                                if (iOnClickDateListener != null){
                                    iOnClickDateListener.onClickDate(chooseYear,chooseMonth,j - firstDayOfWeek + 1);
                                }else {
                                    new RuntimeException("iOnClickDateListener == null");
                                }
                            }else {
                                System.out.print("点击");
                            }

                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;

    }

    /**
     * 这只选中的哪年那月那日
     * @param chooseYear
     * @param chooseMonth
     * @param chooseDay
     */
    public void setChooseYMD(int chooseYear,int chooseMonth,int chooseDay){
        this.chooseYear = chooseYear;
        this.chooseMonth = chooseMonth;
        this.chooseDay = chooseDay;
        firstDayOfWeek = DateUtils.getFirstDayWeek(chooseYear, chooseMonth, 1);
        dayNums = DateUtils.getMonthDays(chooseYear, chooseMonth);

        invalidate();
    }

    public int getChooseYear() {
        return chooseYear;
    }

    public int getChooseMonth() {
        return chooseMonth;
    }

    public int getChooseDay() {
        return chooseDay;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        posweekStrY = lineTextDest - (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading);
        poslineBottomY = mPaintTv.getTextSize() + 2*lineTextDest;
        //按最多的 六行布局高度计算
        if (MeasureSpec.AT_MOST == heightMode){
            heightSize = (int)( poslineBottomY + lineTextDest - (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading)
                    + 6*((mPaintTv.getTextSize() + lineTextDest + 2 * pointRadius + pointMarginTop)));
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    public static class CalendarBean {
        public String earlyStatus;     //是否早退（0：未早退1：早退）
        public String singleStatus;           //是否缺卡（0：未缺卡1：缺卡）
        public String workStatus;         //是否旷工（0：未旷工1：旷工）
        public String approvingStatus;         //是否请假（0：未请假1：请假）
        public String lateStatus;           //是否迟到（0：未迟到1：迟到）
        public String outStatus;        //是否外勤（0：未出外勤1：已出外勤）
        public String status;           //是否签到（0：未签到1：已签到）
        public String day;                 //日期
        public String timeStatus;           //时间是今天之前还是之后（0：未到1：已到）


        private String Color_Late = "#32c37d";
        private String Color_LeaveEarly = "#3A96EC";
        private String Color_Single = "#FD9529";
        private String Color_DayOff = "#FFC335";
        private String Color_Work = "#FF3435";
        private String Color_OutWork = "#A7CF4E";
        private List<String> paintColors;

        public List<String> getPaintColors(){
            paintColors = new ArrayList<>();
            if ("1".equals(earlyStatus)){
                paintColors.add(Color_LeaveEarly);
            }
            if ("1".equals(singleStatus)){
                paintColors.add(Color_Single);
            }
            if ("1".equals(workStatus)){
                paintColors.add(Color_Work);
            }
            if ("1".equals(approvingStatus)){
                paintColors.add(Color_DayOff);
            }
            if ("1".equals(lateStatus)){
                paintColors.add(Color_Late);
            }
            if ("1".equals(outStatus)){
                paintColors.add(Color_OutWork);
            }
            return paintColors;
        }
    }

    /**
     * 点击日期的监听
     */
    public interface IOnClickDateListener{
        /**
         *
         * @param year 年
         * @param month 月
         * @param day 点击的哪一天
         */
        void onClickDate(int year, int month, int day);
    }
    private IOnClickDateListener iOnClickDateListener;

    public void setiOnClickDateListener(IOnClickDateListener iOnClickDateListener) {
        this.iOnClickDateListener = iOnClickDateListener;
    }
}
