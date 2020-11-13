package utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BaseUtil {

///////////////////数学运算函数集合//////////////////////

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double mathAdd(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double mathAdd(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }


    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double mathSub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mathSub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mathMul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double mathMul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double mathDiv(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double mathDiv(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    ///////////////////Bean函数集合//////////////////////

    /**
     * 获取当前类声明的private/protected变量
     *
     * @param object       将被执行的对象
     * @param propertyName 属性名
     * @return 属性对应的object value
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static Object getPrivateProperty(Object object, String propertyName)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        return field.get(object);
    }


    /**
     * 设置当前类声明的private/protected变量
     *
     * @param object       将被执行的对象
     * @param propertyName 属性名
     * @param newValue     属性对应的object value
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static void setPrivateProperty(Object object, String propertyName,
                                          Object newValue) throws IllegalAccessException,
            NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        field.set(object, newValue);
    }

    /**
     * 调用当前类声明的private/protected函数
     *
     * @param object     将被执行的对象
     * @param methodName 方法名
     * @param params     方法参数对应值数组
     * @return 返回返回值
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokePrivateMethod(Object object, String methodName,
                                             Object[] params) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Class[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }
        Method method = object.getClass().getDeclaredMethod(methodName, types);
        method.setAccessible(true);
        return method.invoke(object, params);
    }

    /**
     * 调用当前类声明的private/protected函数
     *
     * @param object     将被执行的对象
     * @param methodName 方法名
     * @param param      方法参数一个值时，方法参数对应值。
     * @return 返回返回值
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokePrivateMethod(Object object, String methodName,
                                             Object param) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        return invokePrivateMethod(object, methodName, new Object[]{param});
    }


    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException)) ||
                    ((e instanceof NoSuchMethodException)))
                return new IllegalArgumentException("Reflection Exception.", e);
            if ((e instanceof InvocationTargetException))
                return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
            if ((e instanceof RuntimeException)) {
                return (RuntimeException) e;
            }
            return new RuntimeException("Unexpected Checked Exception.", e);
        }

    }

    public static Method getAccessibleMethod(Object obj, String methodName, Class<?>[] parameterTypes) {
        for (Class superClass = obj.getClass(); superClass != Object.class; ) {
            try {
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

                method.setAccessible(true);

                return method;
            } catch (NoSuchMethodException localNoSuchMethodException) {
                superClass = superClass.getSuperclass();
            }

        }

        return null;
    }


    private static boolean searchInArray(String key, String[] arry) {
        if (arry != null && arry.length > 0) {
            for (String s : arry) {
                if (key.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String getName(String name, String split) {
        if (StringUtils.isEmpty(split)) {
            return name;
        }
        String[] names = name.split(split);
        String result = name;
        if (names.length > 1) {
            result = names[0];
            for (int i = 1; i < names.length; i++) {
                if (result.length() == 1 && i == 1) {
                    result += names[i];
                } else {
                    result += names[i].substring(0, 1).toUpperCase() + names[i].substring(1, names[i].length());
                }
            }
        }
        return result;
    }


    ////////byte数据操作////////////

    /**
     * int转化为byte[]数组
     *
     * @param num
     * @return
     */
    public static byte[] int2bytes(int num) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }

    /**
     * byte[]数字转化为int型
     *
     * @param b
     * @return
     */
    public static int bytes2int(byte[] b) {
        int mask = 0xff;
        int temp = 0;
        int res = 0;
        for (int i = 0; i < 4; i++) {
            res <<= 8;
            temp = b[i] & mask;
            res |= temp;
        }
        return res;
    }

    /**
     * java字节码转十六进制字符串
     *
     * @param source
     * @return
     */
    public static String byte2hex(byte[] source) {
        StringBuffer result = new StringBuffer();
        String item = "";
        for (int i = 0; i < source.length; i++) {
            // 整数转成十六进制表示
            item = (Integer.toHexString(source[i] & 0XFF));
            if (item.length() == 1) {
                result.append("0");
                result.append(item);
            } else {
                result.append(item);
            }
        }
        return result.toString().toUpperCase(); // 转成大写
    }

    /**
     * 十六进制字符串转换字节数组
     *
     * @param hex String 十六进制
     * @return String 转换后的字符串
     */
    public static byte[] hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return bytes;
    }


    /////////////////////////////////////////String数组组合//////////////////////////////


    /**
     * 对字符串进行UTF-8编码;
     *
     * @param source 原始输入
     * @return 返回编码后的字符串;
     */
    public static String encodeSourceByUTF8(String source) {
        if (source == null || source.length() <= 0) {
            return "";
        }
        try {
            return URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 截取字符串的前targetCount个字符,格式化字符串长度，区分汉字跟字母。 注意，该方法前提认为：汉字2个字节，字母、数字一个字节。
     *
     * @param source      被处理字符串
     * @param targetCount 截取长度
     * @param more        后缀字符串
     * @return String
     */
    public static String StringSubContent(String source, int targetCount, String more) {
        String result = "";
        try {
            // 不能使用getBytes(),因为它会使用平台默认的字符集来判断长度，比如utf-8会认为汉字是三个字节
            if (source.getBytes("GBK").length <= targetCount) {// 如果长度比需要的长度n小,返回原字符串
                return source;
            } else {
                int t = 0;
                char[] tempChar = source.toCharArray();
                for (int i = 0; i < tempChar.length && t < targetCount; i++) {
                    // if ((int) tempChar[i] >= 0x4E00
                    // && (int) tempChar[i] <= 0x9FA5)// 是否汉字
                    if ((int) tempChar[i] > 256) { // 不是英文字母以及数字
                        result += tempChar[i];
                        t += 2;
                    } else {
                        result += tempChar[i];
                        t++;
                    }
                }
                result += more;
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据传入的数组转化成以逗号分隔的字符串
     *
     * @param arrays string数组
     * @return String 例如"1,2,3,4,5,6"
     */
    public static String array2String(String[] arrays, String splitChar) {
        String resultString = "";
        if (arrays != null && arrays.length != 0) {
            StringBuffer tmpstring = new StringBuffer();
            boolean flag = false;
            for (String tmps : arrays) {
                if (flag)
                    tmpstring.append(splitChar);
                tmpstring.append(tmps.trim());
                flag = true;
            }
            resultString = tmpstring.toString();
        }
        return resultString;
    }


    /**
     * 根据以分割的字符串参数转化为数组 默认使用逗号
     *
     * @param string
     * @return
     */
    public static String[] string2Array(String string) {
        return string2Array(string, ",");
    }

    /**
     * 根据以分割的字符串参数转化为数组
     *
     * @param string
     * @param splitChar
     * @return
     */
    public static String[] string2Array(String string, String splitChar) {
        String[] tmpArray = null;
        if (string != null && !"".equals(string.trim())) {
            tmpArray = StringUtils.split(string, splitChar);
        }
        return tmpArray;
    }

    /**
     * 根据以分割的字符串参数转化为list 默认使用逗号
     *
     * @param string
     * @return
     */
    public static List<String> string2List(String string) {
        return string2List(string, ",");
    }

    /**
     * 根据以分割的字符串参数转化为list
     *
     * @param string
     * @return
     */
    public static List<String> string2List(String string, String splitChar) {
        List<String> tmpList = null;
        if (string != null && !"".equals(string.trim())) {
            tmpList = Arrays.asList(StringUtils.split(string,
                    splitChar));
        }
        return tmpList;
    }


    public static boolean isNotBlank(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isEmpty(str);
    }


    /////////////////////////////////Date函数集合////////////////////////////////

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    public static long getSecond(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime());
        } catch (Exception e) {
            return 0;
        }
        return day;
    }

    /**
     * 获取给定月有多少天
     */
    public static String getMonthDays(String str) {
        String nm = formatDate(addMonth(strToDate(str)), "yyyy-MM-dd");
        return getTwoDay(nm, str);
    }


    public static String formatDate(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        if (format == null || "".equalsIgnoreCase(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }


    /**
     * 根据�?个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范�? 1~7
        // 1=星期�? 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 根据当前时间，返回是1,2等字符串
     *
     * @return
     */
    public static String getWeekNow() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("星期一", "1");
        map.put("星期二", "2");
        map.put("星期三", "3");
        map.put("星期四", "4");
        map.put("星期五", "5");
        map.put("星期六", "6");
        map.put("星期日", "7");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(new Date());
        return map.get(week);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static Date strToDate(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    public static Date addMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.MONTH, 1);
        return cd.getTime();
    }

    /**
     * 两个时间之间的天�?
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时�?
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    //获取加上i天后的日期
    public static String getAddDay(String date, int i) {
        Date tempDate = strToDate(date);
        Calendar cd = Calendar.getInstance();
        cd.setTime(tempDate);
        cd.add(Calendar.DAY_OF_MONTH, i);
        Date tempDate1 = cd.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(tempDate1);
    }


    // 计算当月�?后一�?,返回字符�?
    public static String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1�?
        lastDate.add(Calendar.MONTH, 1);//加一个月，变为下月的1�?
        lastDate.add(Calendar.DATE, -1);//减去�?天，变为当月�?后一�?

        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 上月第一�?
    public String getPreviousMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1�?
        lastDate.add(Calendar.MONTH, -1);//减一个月，变为下月的1�?
        //lastDate.add(Calendar.DATE,-1);//减去�?天，变为当月�?后一�?

        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获取当月第一�?
    public String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1�?
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得本周星期日的日期
    public String getCurrentWeekday() {
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得本周星期一的日期
    public static String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        //mondayPlus=mondayPlus-7;
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得下周星期一的日期
    public static String getNextWeekday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6 + 1);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = df1.format(monday);
        return preMonday;
    }


    //获取当天时间
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);//可以方便地修改日期格�?
        String hehe = dateFormat.format(now);
        return hehe;
    }

    // 获得当前日期与本周日相差的天�?
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二�?......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;         //因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    //获得本周�?的日�?
    public static String getMondayOFWeek() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    // 获得下周星期�?的日�?
    public static String getNextMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得下周星期日的日期
    public static String getNextSunday() {

        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    //获得上月�?后一天的日期
    public static String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一�?
        lastDate.roll(Calendar.DATE, -1);//日期回滚�?天，也就是本月最后一�?
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得下个月第�?天的日期
    public static String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一�?
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得下个月最后一天的日期
    public static String getNextMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//加一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一�?
        lastDate.roll(Calendar.DATE, -1);//日期回滚�?天，也就是本月最后一�?
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得明年�?后一天的日期
    public static String getNextYearEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得明年第一天的日期
    public static String getNextYearFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    //获得本年有多少天
    private static int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第�?�?
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天�??
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第�?�?
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天�??
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    //获得本年第一天的日期
    public static String getCurrentYearFirst() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }


    //获得本年�?后一天的日期 *
    public static String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格�?
        String years = dateFormat.format(date);
        return years + "-12-31";
    }


    //获得上年第一天的日期 *
    public static String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格�?
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }


    //获得本季�?
    public static String getThisSeasonTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格�?
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }

    /**
     * 获取某年某月的最后一�?
     *
     * @param year  �?
     * @param month �?
     * @return �?后一�?
     */
    private static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year �?
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /*
     * 比较两个月日期大小
     * */
    public static boolean compareMonth(int year1, int month1, int year, int month) {
        if (year1 > year) {
            return true;
        } else if (year1 == year) {
            if (month1 >= month) {
                return true;
            }
        }
        return false;
    }

    public static boolean compareDate(String d1, String d2) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1;

        date1 = df.parse(d1);
        Date date2 = df.parse(d2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        return c2.before(c1);
    }

    public static String decreaseMonth(String d1) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1;
        date1 = df.parse(d1);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        c1.add(Calendar.MONTH, -1);
        return df.format(c1.getTime());
    }

    public static String addDay(String d1, int day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1;
        try {
            date1 = df.parse(d1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            c1.add(Calendar.DATE, day);
            return df.format(c1.getTime());
        } catch (ParseException e) {
            return "";
        }
    }

    public static String addHour(String d1, int hour) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1;
        try {
            date1 = df.parse(d1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            c1.add(Calendar.HOUR_OF_DAY, hour);
            return df.format(c1.getTime());
        } catch (ParseException e) {
            return "";
        }
    }

    public static String addMin(String d1, int min) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1;
        date1 = df.parse(d1);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        c1.add(Calendar.MINUTE, min);
        return df.format(c1.getTime());
    }

    /*
     * 获取上月25日到当前系统时间的日期列表
     * */
    public static List getDateList(int year, int month) throws Exception {
        List list = new ArrayList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String strDate = df.format(date);
        String thisDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(26);
        String thisDate1 = decreaseMonth(thisDate);
        String thisDate2 = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(25);
        if (compareDate(thisDate1, strDate)) {
            return null;
        } else if (compareDate(strDate, thisDate2)) {
            long days = getDays(thisDate2, thisDate1);
            for (int i = 0; i < days - 1; i++) {
                list.add(addDay(thisDate1, i));
            }
        } else if (compareDate(strDate, thisDate1)) {
            long days = getDays(strDate, thisDate1);
            for (int i = 0; i < days; i++) {
                list.add(addDay(thisDate1, i));
            }
        }
        return list;
    }


    /**
     * 从给定日期序列中获取连续系列
     **/
    public static List getDateListInDate(List list, String sdate) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List upList = new ArrayList();
        List downList = new ArrayList();
        List aList = new ArrayList();
        String tstr = sdate;
        while (true) {
            tstr = getSubDateUp(list, tstr);
            if ("".equalsIgnoreCase(tstr)) {
                break;
            }
            upList.add(tstr);
        }
        tstr = sdate;
        while (true) {
            tstr = getSubDateDown(list, tstr);
            if ("".equalsIgnoreCase(tstr)) {
                break;
            }
            downList.add(tstr);
        }

        for (int i = upList.size() - 1; i >= 0; i--) {
            String tempStr = (String) upList.get(i);
            aList.add(tempStr);
        }
        aList.add(sdate);
        for (int i = 0; i < downList.size(); i++) {
            String tempStr = (String) downList.get(i);
            aList.add(tempStr);
        }

        return aList;
    }

    public static String getSubDateDown(List list, String sdate) {
        if (list == null || list.size() == 0) {
            return "";
        }
        String sdate1 = addDay(sdate, 1);
        for (int i = 0; i < list.size(); i++) {
            String tempStr = (String) list.get(i);
            if (sdate1.equalsIgnoreCase(tempStr)) {
                return tempStr;
            }
        }
        return "";
    }

    public static String getSubDateUp(List list, String sdate) {
        if (list == null || list.size() == 0) {
            return "";
        }
        String sdate1 = addDay(sdate, -1);
        for (int i = 0; i < list.size(); i++) {
            String tempStr = (String) list.get(i);
            if (sdate1.equalsIgnoreCase(tempStr)) {
                return tempStr;
            }
        }
        return "";
    }

    //获取本年度所有周末数据
    public static List getWeekInYear(String year) {
        List list = new ArrayList();
        int iyear = Integer.parseInt(year);
        int[] days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (iyear % 4 == 0 && iyear % 100 != 0 || iyear % 400 == 0) {
            days[2] = 29;
        }
        for (int i = 1; i < days.length; i++) {
            for (int j = 1; j <= days[i]; j++) {
                Date date = new Date(iyear - 1900, i - 1, j);
                int week = date.getDay();
                if (week == 0 || week == 6) {
                    list.add(formatDate(date, "yyyy-MM-dd"));
                }
            }
        }
        return list;
    }

    //
    public static int getMaxDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year); //指定年
        cal.set(Calendar.MONTH, month - 1);//指定月，应该是指定月-1
        int maxDate = cal.getActualMaximum(Calendar.DATE);
        return maxDate;
    }


    ///////////////////////验证函数集合//////////////////////////////

    /**
     * 验证手机号码是否合法,包括移动，联通，小灵通
     *
     * @param mobileNo
     */
    public static boolean isMobileNo(String mobileNo) {

        String reg = "(^13[0-9]\\d{8}$)|(^17[0,1,6,7,8]\\d{8}$)|(^15[0,1,2，5，6,7,8,9]\\d{8}$)|(^18[0-9]\\d{8}$)|(^14[5,7]\\d{8}$)|(^[1-9]{2}\\d{6}$)";

        Pattern p = Pattern.compile(reg);

        Matcher m = p.matcher(mobileNo);

        return m.find();
    }

    public static String getHashStr(String str, String type) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] input = str.getBytes();
            byte[] buff = md.digest(input);
            return bytesToHex(buff);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }

    public static Timestamp getSysDate() {//获得系统当前时间
        return new Timestamp((new GregorianCalendar()).getTimeInMillis());
    }


    public static List<Map<String, Object>> checknull(List<Map<String, Object>> topic, List<Map<String, Object>> details) throws Exception {

        for (Map<String, Object> tmap : topic) {
            for (Map<String, Object> dmap : details) {

                if (dmap.get(tmap.get("Ename").toString()) == null) {
                    dmap.put(tmap.get("Ename").toString(), "");
                }
            }
        }
        return details;
    }


    public static List<Map<String, Object>> changeTime(List<Map<String, Object>> list) throws Exception {
        for (Map<String, Object> map : list) {
            if (map.get("jc_conn") == null) {
                map.put("jc_conn", "");
            }
            for (String key : map.keySet()) {
                if (map.get(key) instanceof Date) {
                    Date date = (Date) map.get(key);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String str = sdf.format(date);
                    map.put(key, str);
                }
            }
        }
        return list;
    }

    public static List<HashMap<String, Object>> changeHashTime(List<HashMap<String, Object>> list) throws Exception {
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                if (map.get(key) == null) {
                    map.put(key, "");
                }

                if (map.get(key) instanceof Date) {
                    Date date = (Date) map.get(key);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String str = sdf.format(date);
                    map.put(key, str);
                }
            }
        }
        return list;
    }


    /***
     * 队列相关工具
     */
    public static class ListUtils {


        public static void main(String[] args) {
            String str = BaseUtil.getHashStr("234", "SHA-256");
            System.out.println(str);
//            logger.info(String.format("用户"));
//            List<CourseWareDO> list = new ArrayList<>();
//            List<CourseWareDO> list2 = new ArrayList<>();

//            CourseWareDO courseWareDO1 = new CourseWareDO();
//            courseWareDO1.setId(1);
//            courseWareDO1.setName("demo1");
//            CourseWareDO courseWareDO2 = new CourseWareDO();
//            courseWareDO2.setId(2);
//            courseWareDO2.setName("demo2");
//            CourseWareDO courseWareDO3 = new CourseWareDO();
//            courseWareDO3.setId(1);
//            courseWareDO3.setName("demo3");

//            list.add(courseWareDO1);
//            list.add(courseWareDO2);
//            list.add(courseWareDO3);

//            List<CourseWareDO> repeatCoursewareList = utils.BaseUtils.ListUtils.getRepeatElements(list, "id");
//            list.removeAll(repeatCoursewareList);
//            System.out.println(list);
        }

        /**
         * 将list根据fieldName将元素装载到map中
         *
         * @param list            数据源
         * @param map             数据容器
         * @param keyFieldName    map中的key在list中元素的字段的名称,如果为空则使用uuid
         * @param prefixFieldName 字段前缀名称
         */
        public static void listToMap(List list, Map<String, Object> map, String keyFieldName, String prefixFieldName) {
            try {
                Object key;
                Object prefix = null;
                String getT = "get";
                for (Object object : list) {
                    if (keyFieldName == null) {
                        key = UUID.randomUUID().toString();
                    } else {
                        Class c = object.getClass();
                        Method keyMethod = c.getMethod(getT + StringUtilsSon.firstLetterUpperCase(keyFieldName));
                        key = keyMethod.invoke(object);
                        if (prefixFieldName != null) {
                            Method prefixMethod = c.getMethod(getT + StringUtilsSon.firstLetterUpperCase(prefixFieldName));
                            prefix = prefixMethod.invoke(object);
                        }
                    }
                    map.put(prefix == null ? JSONObject.toJSONString(key).replaceAll("\"", "") : (JSONObject.toJSONString(prefix) + JSONObject.toJSONString(key)).replaceAll("\"", ""), object);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        /**
         * 将list根据fieldName将元素装载到map中
         *
         * @param list            数据源
         * @param map             数据容器
         * @param keyFieldName    map中的key在list中元素的字段的名称,如果为空则使用uuid
         * @param prefixFieldName 字段前缀名称
         */
        public static void listToMapAndPrefix(List list, Map<String, Object> map, String keyFieldName, String prefixFieldName) {
            try {
                Object key;
                String getT = "get";
                for (Object object : list) {
                    if (keyFieldName == null) {
                        key = UUID.randomUUID().toString();
                    } else {
                        Class c = object.getClass();
                        Method keyMethod = c.getMethod(getT + StringUtilsSon.firstLetterUpperCase(keyFieldName));
                        key = keyMethod.invoke(object);
                    }
                    map.put(prefixFieldName == null ? key + "" : prefixFieldName + key, object);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


        /**
         * 从一个list中获取某个元素的某个值等于key的元素实例对象,(默认返回list中第一个符合条件的对象实例)，元素实例的变量必须为object类型，比如说整型的话Integer可以，int就不行。
         *
         * @param list  数据源
         * @param key   元素中的变量名称
         * @param value 要查询的具体值
         * @param <T>
         * @return
         */
        public static <T> T getListToValue(List<T> list, String key, Object value) {
            String get = "get";
            String methodName = get + StringUtilsSon.firstLetterUpperCase(key);
            for (T t : list) {
                Object tValue = ObjectUtils.invokeMethod(t, methodName);
                if (value == null) {
                    if (value == tValue) {
                        return t;
                    }
                } else {
                    if (value.equals(tValue)) {
                        return t;
                    }
                }
            }
            return null;
        }

        /**
         * 自动生成uuid为map的key,然后装载到map中
         *
         * @param list 数据源
         * @param map  数据容器
         */
        public static void listToMap(List list, Map<String, Object> map) {
            ListUtils.listToMap(list, map, null, null);
        }


    }

    /**
     * String工具类
     */
    public static class StringUtilsSon extends StringUtils {
        /**
         * 将一个字符串的首字母变成大写
         *
         * @param str
         * @return
         */
        public static String firstLetterUpperCase(String str) {
            return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
        }

        /***
         * 根据一个标记转换为驼峰
         * @param str
         * @return
         */
        public static String markToHump(String str, String mark, Integer start) {
            String[] strs = str.split(mark);
            String returnStr = "";
            if (start == null) {
                start = 0;
            }
            for (int i = 0; i < strs.length; i++) {
                returnStr += start == null ? StringUtilsSon.firstLetterUpperCase(strs[i]) : start == i ? strs[i] : StringUtilsSon.firstLetterUpperCase(strs[i]);
            }
            return returnStr;
        }

        /***
         * 将一个标识符在驼峰的字符串中加入进去，并将大写字母转为小写字母
         * @param str 原始数据
         * @param mark 标识
         * @return
         */
        public static String addMarkToString(String str, String mark) {
            char[] chs = str.toCharArray();
            int start = 65;
            int end = 90;
            str = "";
            int i = -1;
            for (char ch : chs) {
                i++;
                int chInt = (int) ch;
                if (start <= chInt && end >= chInt) {
                    if (i != 0) {
                        str += (mark + (ch + "").toLowerCase());
                    } else {
                        str += (ch + "").toLowerCase();
                    }
                    continue;
                }
                str += ch;
            }
            return str;
        }


        private static String replaceRange(String source, String oldChar, String newChar, String start, String end, int index) {
            int startInt = source.indexOf(start, index);
            int endInt = source.indexOf(end, index);
            if (endInt == -1)
                return source;
            return StringUtilsSon.replaceRange(
                    source.substring(0, startInt) + source.substring(startInt, endInt).replace(oldChar, newChar) + source.substring(endInt, source.length()),
                    oldChar,
                    newChar,
                    start,
                    end,
                    endInt + 1
            );
        }

        /***
         * 讲一个字符串中两个字符之间的值替换为其他值
         * @param source 数据源
         * @param oldChar 要替换的字符串原来的值
         * @param newChar 要替换的字符串最新的值
         * @param start    范围开始的值
         * @param end 范围结束的值
         * @return
         */
        public static String replaceRange(String source, String oldChar, String newChar, String start, String end) {
            return StringUtilsSon.replaceRange(source, oldChar, newChar, start, end, 0);
        }
    }

    /***
     * 枚举相关工具
     */
    public static class EnumUtils {


//        public static <T> T getValue(Object[] enmuObjs, String keyName, String valueName, String byKey, Class<T> type){
//            String methodKeyName = "";
//            String methodValueName = "";
//            for(Object enmuObj : enmuObjs){
//                Field[] fields = enmuObj.getClass().getDeclaredFields();
//                String get = "get";
//                //取到key name 和 value name
//                if(methodKeyName.equals("") || methodKeyName.equals(null)){
//                    for(Field field : fields){
//                        field.setAccessible(true);
//                        String fieldName = field.getName();
//                        //判断是否为key name
//                        if(keyName.equals(fieldName))
//                            methodKeyName = get + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());//如果为key name或者value name则存起来
//                        if(valueName.equals(fieldName))
//                            methodValueName = get + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
//                    }
//                }
//                try{
//                    Method methodValueMth = enmuObj.getClass().getMethod(methodValueName);
//                    Method methodKeyMth = enmuObj.getClass().getMethod(methodKeyName);
//                    T value = (T) methodValueMth.invoke(enmuObj);
//                    String key = (String) methodKeyMth.invoke(enmuObj);
//                    if(key.equals(byKey)){
//                        return value;
//                    }
//                }catch(NoSuchMethodException e){
//                    String msg = e.getMessage();
//                    String msgStr = "() 此方法不存在";
//                    if(msg.contains(methodValueName)){
//                        try{
//                            throw new Exception(methodValueName + msgStr);
//                        }catch(Exception e1){
//                            e1.printStackTrace();
//                        }
//                    }else if(msg.contains(methodKeyName)){
//                        try{
//                            throw new Exception(methodKeyName + msgStr);
//                        }catch(Exception e1){
//                            e1.printStackTrace();
//                        }
//                    }else{
//                        e.printStackTrace();
//                    }
//                }catch(IllegalAccessException e){
//                    e.printStackTrace();
//                }catch(InvocationTargetException e){
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }

        /***
         * 根据keyName得值获取对应valueName的值
         * @param enums 要查的枚举类数组对象
         * @param keyName 相对key的名称（此名称也可指定枚举中的value或别的字段的变量名）
         * @param valueName 相对value的名称（keyName多对应的value的值）（此名称也可指定枚举中的value或别的字段的变量名）
         * @param byKey keyName对应的key值
         * @return
         */
        public static <T> T getValue(Enum[] enums, String keyName, String valueName, String byKey, Class<T> type) {
            try {
                for (Enum enumObj : enums) {
                    Field keyField = enumObj.getClass().getDeclaredField(keyName);
                    keyField.setAccessible(true);
                    Object o = keyField.get(enumObj);
                    if (o.equals(byKey)) {
                        Field valueField = enumObj.getClass().getDeclaredField(valueName);
                        valueField.setAccessible(true);
                        return (T) valueField.get(enumObj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class ObjectUtils {
        /**
         * 执行一个对象中的某个方法
         *
         * @param obj        对象
         * @param methodName 方法名称
         * @param parameters 将要执行的方法的参数
         * @return 方法的返回值
         */
        public static Object invokeMethod(Object obj, String methodName, Object... parameters) {
            try {
                Class c = obj.getClass();
                Method[] mds = c.getMethods();
                for (Method method : mds) {
                    if (method.getName().equals(methodName)) {
                        return method.invoke(obj, parameters);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }


    }

}