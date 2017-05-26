人老了，就容易暴躁。直接上图，暴躁如我。
![这里写图片描述](http://img.blog.csdn.net/20170526150408989?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc2luYXRfMjY3MTA3MDE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

```
功能：
	1.当天会有一个蓝色半弧标志
	2.如果某一天 签到了，会显示“小爪子”图片
	3.如果点击了某一天，则会显示灰色圆形选中状态
	4.每一天的底下可以有不同的小颜色点，表示当天的出勤状态（让服务器哥们算好，别弄叉了数据）
```

这是一个简单的自定义控件，总的来说，没什么技术含量（虽然我搞了两天才弄出来），不过还是希望能分享下，要不然，搞了两天的东西不拿来强行装b，简直是浪费。

废话不多说，暴躁如我，走起。

```
先说下思路：
	第一步：准备好Paint（分割线画笔【mPaintLine】，文字画笔【mPaintTv】，小圆点画笔【mPaintPoint】，背景图画笔【mPaintBg】）
	第二步：分析需要的数据bean，然后OnDraw绘制
	第三步：处理点击事件。
	第四步：优化（布局最大高度[OnMeasure]）。
```
第一步就不说了，不懂的请从自定义view的基础看起吧。
**这里只说一点，mPaintTv这个画笔，一定要将设置绘制点为中心点`  mPaintTv.setTextAlign(Paint.Align.CENTER);`**

第二步，分析：
六种状态自然是需要的：

```
public String earlyStatus;     //是否早退（0：未早退1：早退）
public String singleStatus;           //是否缺卡（0：未缺卡1：缺卡）
public String workStatus;         //是否旷工（0：未旷工1：旷工）
public String approvingStatus;         //是否请假（0：未请假1：请假）
public String lateStatus;           //是否迟到（0：未迟到1：迟到）
public String outStatus;        //是否外勤（0：未出外勤1：已出外勤）
```
当然还有签到状态  `        public String status;           //是否签到（0：未签到1：已签到）`

这里为了方便，再加上一个天份日期  `public String day;                 //日期`

**然后就直接开始绘制了（这里是重点）：**

 - **绘制顶部横线**
```
canvasWidth = getWidth();
canvas.drawLine(0,0, canvasWidth,0,mPaintLine);
```
 - **绘制星期文字**

```
for (int i = 0; i < weekStr.length; i++) {
            canvas.drawText(weekStr[i], viewMargin + mPaintTv.measureText("11")/2 +
                    i * (canvasWidth - viewMargin*2 - mPaintTv.measureText("11")) / 6, posweekStrY, mPaintTv);
        }
        
```
**横坐标：**        
因为mPaintTv绘制点在文字的正中心，所以，“日”字的绘制的x坐标为，“日”字长度的二分之一，加上字体距离边界的viewMargin，而后其他星期的字的绘制横坐标怎么算： 
	剩下的距离平分成六份
	(控件总宽度-左右的viewMargin-（“日”字的二分之一的距离 + "六"字的二分之一的距离））/ 6
	![这里写图片描述](http://img.blog.csdn.net/20170526161618322?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc2luYXRfMjY3MTA3MDE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
**纵坐标：**
纵坐标的绘制，如果不知道textview的绘制原理的话，很容一迷糊，所以：走你，传送门----[textview的绘制坐标原理](http://blog.csdn.net/sinat_26710701/article/details/70184252)
（如图：textview的paint绘制，并不是以最底的线为基准，而是以**baseline**为基准，也就是说，
canvas.drawText("text",x,y,paintTv);中的y，指的是**baseline**所在的y轴坐标。

**同时，通过`mPaintTv.getFontMetrics()`可以获取到 leading，ascent，descent 三个值，但是这里要注意的是，leading和ascent的值是负数，因为是以baseline为横轴，baseline之上就是负值。**）
![这里写图片描述](http://img.blog.csdn.net/20170526162038048?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc2luYXRfMjY3MTA3MDE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
纵坐标就等于： 
`posweekStrY = lineTextDest - (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading);`
 
  - **绘制月份天数文字**
首先，确定当月第一天是星期几（代表第一天前有几个地方不需要绘制），总共要绘制`dayNums+ firstDayOfWeek`个，`firstDayOfweek`之前都不`drawText`

计算纵坐标:

```
posMonthDayY = poslineBottomY + lineTextDest - (mPaintTv.getFontMetrics().ascent + mPaintTv.getFontMetrics().leading)
                        + (j/7) * (mPaintTv.getTextSize() + lineTextDest + 2 * pointRadius + pointMarginTop);
```
计算横坐标：

```
posMonthDayX = viewMargin + mPaintTv.measureText("11") / 2
                        + (j%7) * (canvasWidth - viewMargin * 2 - mPaintTv.measureText("11")) / 6;
```
绘制文字：

```
         canvas.drawText(calendarBeanList.get(j - firstDayOfWeek).day,
                        posMonthDayX, posMonthDayY, mPaintTv);
```

- **绘制背景图** 
计算绘制的区域：
-6和+6 是为了让图片能比文字的绘制区域稍微大一点，显得好看（本人很注重审美的）。
```
RectF rect = new RectF((float) (posMonthDayX - mPaintTv.measureText("11") / 2)-6,
                        (float) (posMonthDayY - (-mPaintTv.getFontMetrics().ascent - mPaintTv.getFontMetrics().leading))-6,
                        (float) (posMonthDayX + mPaintTv.measureText("11") / 2)+6,
                        (float) (posMonthDayY + mPaintTv.getFontMetrics().descent)+6
                );
```
- **绘制圆点**

```
int paintCount = calendarBeanList.get(j-firstDayOfWeek).getPaintColors().size();
//绘制圆点
for (int i = 0; i < paintCount; i++) {
	mPaintPoint.setColor(Color.parseColor(calendarBeanList.get(j-firstDayOfWeek).getPaintColors().get(i)));
    if (paintCount%2 == 0){
	    canvas.drawCircle(posMonthDayX -(2*i-1)*(dotMargin/2+pointRadius)
        , posMonthDayY +pointRadius+pointMarginTop,pointRadius,mPaintPoint);
    }else {
        canvas.drawCircle(posMonthDayX -((int)Math.pow(-1,i+1)*(i+1)/2)*(dotMargin+2*pointRadius), posMonthDayY +pointRadius+pointMarginTop,pointRadius,mPaintPoint);
    }
}
```
![这里写图片描述](http://img.blog.csdn.net/20170526173723855?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc2luYXRfMjY3MTA3MDE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
分为两种：
奇数个小点：先绘制圆点坐标的小点，接着是圆点左边的第一个小点，接着是圆点右边的第一个小点，然后是，圆点左边第二个小点，再绘制圆点右边第二个小点。。。（Math.pow(-1,i+1)来控制圆点左边是负数，右边是正数）
偶数个小点：先绘制左边在绘制右边，在绘制左二，依次进行。

- 点击事件判断

```
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
```
很简单，自行体会吧

代码已经提交git 和csdn，需要的自行下载，如果有什么问题，请一定要告诉我。

[GitHub地址：https://github.com/SuperChandler/CustomCalendarView](https://github.com/SuperChandler/CustomCalendarView)


