package com.fanhaichao.customcalendarview;

import java.util.List;

/**
 * Created by FanHaiChao on 2017/5/8.
 */
public class CalendarDataBean {


    /**
     * headerType : 0
     * listCode : [{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-01","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-02","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-03","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-04","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-05","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-06","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-07","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"earlyStatus":"0","status":"0","singleStatus":"0","workStatus":"1","approvingStatus":"0","day":"2017-05-08","timeStatus":"1","lateStatus":"0","outStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"},{"timeStatus":"0"}]
     * imagePath : /files/
     * QUERY_USER_ID : 11570
     * countCode : {"approAttenCount":0,"lateAttenCount":0,"workAttenCount":8,"outAttenCount":0,"earlyAtenCount":0,"singleAttenCount":0}
     * code : 0
     * msg : 操作成功
     * expMsg :
     */

    public String headerType;
    public String imagePath;
    public String QUERY_USER_ID;
    public CountCodeBean countCode;
    public int code;
    public String msg;
    public String expMsg;
    public List<ListCodeBean> listCode;

    public static class CountCodeBean {
        /**
         * approAttenCount : 0
         * lateAttenCount : 0
         * workAttenCount : 8
         * outAttenCount : 0
         * earlyAtenCount : 0
         * singleAttenCount : 0
         */

        public int approAttenCount;
        public int lateAttenCount;
        public int workAttenCount;
        public int outAttenCount;
        public int earlyAtenCount;
        public int singleAttenCount;
    }

    public static class ListCodeBean {
        /**
         * earlyStatus : 0
         * status : 0
         * singleStatus : 0
         * workStatus : 1
         * approvingStatus : 0
         * day : 2017-05-01
         * timeStatus : 1
         * lateStatus : 0
         * outStatus : 0
         */

        public String earlyStatus;
        public String status;
        public String singleStatus;
        public String workStatus;
        public String approvingStatus;
        public String day;
        public String timeStatus;
        public String lateStatus;
        public String outStatus;
       /*       "earlyStatus": "0",//是否早退（0：未早退1：早退）
                "status": "0",//是否签到（0：未签到1：已签到）
                "singleStatus": "0",//是否缺卡（0：未缺卡1：缺卡）
                "workStatus": "1",//是否旷工（0：未旷工1：旷工）
                "approvingStatus": "0",//是否请假（0：未请假1：请假）
                "day": "2017-05-01",//日期
                "timeStatus": "1",//时间是今天之前还是之后（0：未到1：已到）
                "lateStatus": "0",//是否迟到（0：未迟到1：迟到）
                "outStatus": "0"//是否外勤（0：未出外勤1：已出外勤）*/

    }
}
