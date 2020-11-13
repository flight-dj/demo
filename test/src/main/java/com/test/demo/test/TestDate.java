package com.test.demo.test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @Author: dajun
 * @Date: 2020/11/11 2:04 下午
 **/
public class TestDate {

    public static void main(String[] args) throws ParseException {

//        在使用Date的比较时，一般会用after和before，但有时理解起来易混淆，
//        如果把after当作" > (大于)"号，把before当作" <(小于)"就好理解了，例如：
//        Date1.after(Date2)即（如date1为12月1日、date2为12月2日，
//          date1 > date2或理解为：date1在前，date2在后),当Date1大于Date2时，返回TRUE，
//          当小于等于时，返回false；
//        Date1.before(Date2)即 （如date1为12月2日、date2为12月1日，
//          date1 < date2或理解为：date1在后，date2在前)，当Date1小于Date2时，返回TRUE，
//          当大于等于时，返回false

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//        System.out.println(sdf.parse("2020-11-11").after(sdf.parse("2020-11-10")));  //false
//        System.out.println(sdf.parse("2020-12-21").after(new Date()));  //true
//        System.out.println(sdf.parse("2020-10-21").before(new Date()));  //true
//        System.out.println(sdf.parse("2020-12-21").before(new Date()));  //false
        System.out.println(isEffectiveDate(sdf.parse("2020-11-11 12:01:01"),
                sdf.parse("2020-11-11"), sdf.parse("2020-11-12")));
    }

    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
