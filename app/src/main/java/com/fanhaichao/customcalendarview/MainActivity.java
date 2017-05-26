package com.fanhaichao.customcalendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private ImageView iv_date_left;
    private TextView tv_date_top;
    private TextView tv_text;
    private ImageView iv_date_right;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = (CalendarView) findViewById(R.id.calendar);
        iv_date_left = (ImageView) findViewById(R.id.iv_date_left);
        tv_date_top = (TextView) findViewById(R.id.tv_date_top);
        tv_text = (TextView) findViewById(R.id.tv_text);
        iv_date_right = (ImageView) findViewById(R.id.iv_date_right);
        tv_date_top.setText(calendarView.getChooseYear()+"-"+ calendarView.getChooseMonth()+"-"+ calendarView.getChooseDay());
        initListener();
        calendar = Calendar.getInstance();
        okhttpget("2017-05");
    }

    private void initListener() {
        iv_date_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                Date date = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String yearMonth = format.format(date);
                tv_date_top.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
                okhttpget(yearMonth);
            }
        });

        iv_date_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, +1);
                Date date = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String yearMonth = format.format(date);
                tv_date_top.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
                okhttpget(yearMonth);
            }
        });
    }

    private void okhttpget(String year_month){
        OkHttpUtils.post()
                .url("http://192.168.1.120:8080/iosAttendance/getFacAttendanceStatistics.do")
                .addParams("USER_ID","11646")
                .addParams("YEAR_MONTH",year_month)
                .addParams("QUERY_USER_ID","11646")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        CalendarDataBean calendarDataBean = new Gson().fromJson(s,CalendarDataBean.class);
                        List<CalendarView.CalendarBean> calendarBeanList = new ArrayList<CalendarView.CalendarBean>();
                        for (int i = 0; i < calendarDataBean.listCode.size(); i++) {
                            CalendarView.CalendarBean calendarBean = new CalendarView.CalendarBean();
                            calendarBean.day = (i+1) +"";
                            calendarBean.earlyStatus = calendarDataBean.listCode.get(i).earlyStatus;
                            calendarBean.status = calendarDataBean.listCode.get(i).status;
                            calendarBean.singleStatus = calendarDataBean.listCode.get(i).singleStatus;
                            calendarBean.workStatus = calendarDataBean.listCode.get(i).workStatus;
                            calendarBean.approvingStatus = calendarDataBean.listCode.get(i).approvingStatus;
                            calendarBean.timeStatus = calendarDataBean.listCode.get(i).timeStatus;
                            calendarBean.lateStatus = calendarDataBean.listCode.get(i).lateStatus;
                            calendarBean.outStatus = calendarDataBean.listCode.get(i).outStatus;
                            calendarBeanList.add(calendarBean);
                        }
                        calendarView.setDataList(calendarBeanList);
                        calendarView.setChooseYMD(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
                        calendarView.setiOnClickDateListener(new CalendarView.IOnClickDateListener() {
                            @Override
                            public void onClickDate(int year, int month, int day) {
//                                Toast.makeText(MainActivity.this,"点击了"+year+"年"+month+"月"+day+"日",Toast.LENGTH_SHORT).show();
                                tv_date_top.setText(year+"-"+month+"-"+day);
                                tv_text.setText("点击了"+year+"年"+month+"月"+day+"日");
                            }
                        });
                    }
                });
    }
}
