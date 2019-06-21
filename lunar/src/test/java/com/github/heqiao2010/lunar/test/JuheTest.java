package com.github.heqiao2010.lunar.test;

import com.github.heqiao2010.lunar.LunarCalendar;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;

/**
 * 调用聚合数据万年历API进行测试：https://www.juhe.cn/docs/api/id/177<br>
 * 目前万年历API只支持 1901-1-1 到 2049-12-31（50年）
 * Created by heqiao on 2019/3/31.
 */
public class JuheTest {
    // 为了测试目的，申请的key，免费的API只允许调用100次每天，谁能给个不限次数的？
    private static final String API_KEY = "d8acde4f3be4a4e54c0d56c3a3b07109";
    // 测试API
    private final String uriFormat = "http://v.juhe.cn/calendar/day?date=%s&key=%s";

    private Gson gson = new Gson();

    // 聚合api不靠谱，后面再找合适的吧
    private final Integer MAX_COUNT = 0;

    @Test
    public void testByJuhe() {
//        // 1901-1-1
//        Calendar start = Calendar.getInstance();
//        start.set(Calendar.YEAR, 1901);
//        start.set(Calendar.MONTH, 0);
//        start.set(Calendar.DATE, 1);
        Calendar start = Calendar.getInstance();
        // 2049-12-31
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, 2049);
        end.set(Calendar.MONTH, 11);
        end.set(Calendar.DATE, 31);

        int i = 0;
        Calendar t = start;
        while(t.before(end) && i < MAX_COUNT){
            String tDate = getDate(t);
            String uri = String.format(uriFormat, tDate, API_KEY);
            HttpGet get = new HttpGet(uri);

            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try{
                // 发送请求
                client = HttpClients.createDefault();
                response = client.execute(get);
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                compare(t, content);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                try{
                    if(null != client){
                        client.close();
                    }
                    if(null != response){
                        response.close();
                    }
                } catch (IOException iex) {
                    //ignore
                }
            }
            t.add(Calendar.DATE, 1);
        }
    }

    /**
     * 聚合API的XX设计，居然不识别1901-01-01这种日期格式
     */
    private String getDate(Calendar c){
        return String.join("-",
                String.valueOf(c.get(Calendar.YEAR)),
                String.valueOf(c.get(Calendar.MONTH) + 1),
                String.valueOf(c.get(Calendar.DATE)));
    }

    private void compare(Calendar c, String juheContent){
        JuheCalendarData data = gson.fromJson(juheContent, JuheCalendarData.class);
        Assert.assertEquals(Integer.valueOf(0), data.getError_code());
        String date1 = getDate(c);
        String date2 = data.getResult().getData().getDate();
        Assert.assertEquals(date1, date2);
        // 转LunarCalendar
        LunarCalendar lunar = new LunarCalendar(c);
        System.out.println(date1 + " -> " + lunar.getFullLunarName());
        System.out.println("juheContent: " + juheContent);
        Assert.assertEquals(date1, date2);
        Assert.assertEquals(data.getResult().getData().getAnimalsYear(), String.valueOf(LunarCalendar.getAnimalYearName(lunar.getLyear())));
        Assert.assertEquals(data.getResult().getData().getLunarYear(), LunarCalendar.getTraditionalYearName(lunar.getLyear()));
        Assert.assertEquals(data.getResult().getData().getLunar(), lunar.getLunar(false));
    }

    /**
     * 聚合日历API返回的数据格式
     */
    public static class JuheCalendarData{
        public String reason;
        public Integer error_code;
        public Result result;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Integer getError_code() {
            return error_code;
        }

        public void setError_code(Integer error_code) {
            this.error_code = error_code;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

    public static class Result{
        public Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public static class Data{
        public String animalsYear;
        public String weekday;
        public String lunarYear;
        public String lunar;
        public String date;

        public String getAnimalsYear() {
            return animalsYear;
        }

        public void setAnimalsYear(String animalsYear) {
            this.animalsYear = animalsYear;
        }

        public String getWeekday() {
            return weekday;
        }

        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }

        public String getLunarYear() {
            return lunarYear;
        }

        public void setLunarYear(String lunarYear) {
            this.lunarYear = lunarYear;
        }

        public String getLunar() {
            return lunar;
        }

        public void setLunar(String lunar) {
            this.lunar = lunar;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
