package com.github.heqiao2010.lunar.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * 通过香港天文台提供的农历对照表数据生成1901-2100年的数据
 * see https://www.hko.gov.hk/tc/gts/time/conversion1_text.htm
 */
public class DateInfoCollector {
    private static final Map<String, Integer> MONTH_NUMBER_CONVERT_MAP = new HashMap<String, Integer>();

    static {
        MONTH_NUMBER_CONVERT_MAP.put("一月", 1);
        MONTH_NUMBER_CONVERT_MAP.put("二月", 2);
        MONTH_NUMBER_CONVERT_MAP.put("三月", 3);
        MONTH_NUMBER_CONVERT_MAP.put("四月", 4);
        MONTH_NUMBER_CONVERT_MAP.put("五月", 5);
        MONTH_NUMBER_CONVERT_MAP.put("六月", 6);
        MONTH_NUMBER_CONVERT_MAP.put("七月", 7);
        MONTH_NUMBER_CONVERT_MAP.put("八月", 8);
        MONTH_NUMBER_CONVERT_MAP.put("九月", 9);
        MONTH_NUMBER_CONVERT_MAP.put("十月", 10);
        MONTH_NUMBER_CONVERT_MAP.put("十一月", 11);
        MONTH_NUMBER_CONVERT_MAP.put("十二月", 12);
    }

    @Test
    public void collector() {
        final int startYear = 1901;
        final int endYear = 2100;

        Map<Integer, Integer> lunarLeapMonthMap = new LinkedHashMap<Integer, Integer>();
        List<Pair<String, String>> parsedDateList = new ArrayList<Pair<String, String>>();

        for (int year = startYear; year <= endYear; year++){
            String uri = "http://www.hko.gov.hk/tc/gts/time/calendar/text/files/T" + year + "c.txt";
            HttpGet get = new HttpGet(uri);
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                client = HttpClients.createDefault();
                // 发送请求
                response = client.execute(get);
                String content = EntityUtils.toString(response.getEntity(), "Big5");
                doCollect(parsedDateList, lunarLeapMonthMap, content, year);
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
        }

        System.out.println("lunar month map:");
        for (Pair<String, String> pair : parsedDateList) {
            System.out.println(pair.getKey() + "->" + pair.getValue());
        }

        System.out.println("leap month info：");
        for (Map.Entry<Integer, Integer> entry : lunarLeapMonthMap.entrySet()) {
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }

        System.out.println("lunar codes：");
        for (Pair<String, String> pair : parsedDateList){
            if (pair.getKey().endsWith("十二月")) {
                if (pair.getValue().startsWith("01")){
                    System.out.print("13" + pair.getValue().substring(2));
                    System.out.print("},");
                } else {
                    System.out.print(removeStart0(pair.getValue()));
                    System.out.print("},");
                }
                System.out.println();
            } else {
                System.out.print(removeStart0(pair.getValue()));
                System.out.print(",");
            }
        }
        System.out.println();
    }

    private void doCollect(List<Pair<String, String>> parsedDateList, Map<Integer, Integer> lunarLeapMonthMap,
                           String content, int year) {
        Map<String, String> dateMap = new LinkedHashMap<String, String>();
        String[] lines = content.split("\n");

        for (int i=3; i<lines.length; i++){
            String solarDate = parseSolarDate(lines[i]);
            String lunarDate = parseLunarDate(lines[i]);
            if (null!=solarDate && !solarDate.isEmpty()) {
                dateMap.put(solarDate, lunarDate);
            }
        }

        // 获取闰月信息
        String lunarDateLeapMonth = null;
        for (String lunarDateItem : dateMap.values()) {
            if (lunarDateItem.startsWith("閏")){
                lunarDateLeapMonth = lunarDateItem;
                break;
            }
        }
        if (lunarDateLeapMonth != null) {
            Integer monthNumber = MONTH_NUMBER_CONVERT_MAP.get(lunarDateLeapMonth.substring(1));
            if (null != monthNumber) {
                lunarLeapMonthMap.put(year, monthNumber);
            } else {
                System.out.print("wrong lunarDateLeapMonth: " + lunarDateLeapMonth);
            }
        } else {
            lunarLeapMonthMap.put(year, 0);
        }

        // 获取对照信息
        for (Map.Entry<String, String> entry : dateMap.entrySet()) {
            if (entry.getValue().endsWith("月")) {
                String lunarCode = entry.getKey().substring(5, 7) + entry.getKey().substring(8, 10);
                Pair<String, String> pair = new Pair<String, String>(year + entry.getValue(), lunarCode);
                parsedDateList.add(pair);
            }
        }
    }

    private static String parseLunarDate(String line){
        if (null == line || line.length() < 19){
            System.out.println("unparsed line: " + line);
            return null;
        }
        return line.substring(15, 25).trim();
    }

    private static String parseSolarDate(String line){
        if (line.length() < 14) {
            System.out.println("unparsed line: " + line);
            return null;
        }
        String solarDate = line.substring(0, 14).trim();
        if (solarDate.length() < 9 || solarDate.length() > 11 || solarDate.charAt(solarDate.length()-1) != '日') {
            System.out.println("unparsed line: " + line);
            return null;
        }
        if (solarDate.length() < 11) {
            String year = solarDate.substring(0, 4);
            String month;
            String day;
            if (isNumber(solarDate.charAt(6))) {
                month = solarDate.substring(5, 7);
            } else {
                month = "0" + solarDate.substring(5, 6);
            }
            if (isNumber(solarDate.charAt(solarDate.length() -3))) {
                day = solarDate.substring(solarDate.length() - 3, solarDate.length() - 1);
            } else {
                day = "0" + solarDate.substring(solarDate.length() - 2, solarDate.length() - 1);
            }
            return year + "年" + month + "月" + day + "日";
        }
        return solarDate;
    }

    private static boolean isNumber(char c){
        return c >= '0' && c <= '9';
    }

    private static String removeStart0(String code){
        return code == null || code.charAt(0) != '0' ? code : code.substring(1);
    }

    private static class Pair<K,V> {
        private K key;
        private V value;
        Pair(K key, V value){
            this.key = key;
            this.value = value;
        }
        K getKey(){
            return key;
        }
        V getValue(){
            return value;
        }
    }
}
