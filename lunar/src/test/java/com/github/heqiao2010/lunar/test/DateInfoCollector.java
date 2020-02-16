package com.github.heqiao2010.lunar.test;

import javafx.util.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.*;

/**
 * 通过香港天文台提供的农历对照表数据生成1901-2100年的数据
 * see https://www.hko.gov.hk/tc/gts/time/conversion1_text.htm
 */
public class DateInfoCollector {
    private static final Map<String, Integer> MONTH_NUMBER_CONVERT_MAP = new HashMap<>();

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

        Map<Integer, Integer> lunarLeapMonthMap = new LinkedHashMap<>();
        List<Pair<String, String>> parsedDateList = new ArrayList<>();

        for (int year = startYear; year <= endYear; year++){
            String uri = "http://www.hko.gov.hk/tc/gts/time/calendar/text/files/T" + year + "c.txt";
            HttpGet get = new HttpGet(uri);

            try(CloseableHttpClient client = HttpClients.createDefault()) {
                // 发送请求
                CloseableHttpResponse response = client.execute(get);
                String content = EntityUtils.toString(response.getEntity(), "Big5");
                doCollect(parsedDateList, lunarLeapMonthMap, content, year);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        System.out.println("lunar month map:");
        parsedDateList.forEach(pair -> System.out.println(pair.getKey() + "->" + pair.getValue()));

        System.out.println("leap month info：");
        lunarLeapMonthMap.forEach((key, value) -> System.out.println(key + "->" + value));

        System.out.println("lunar codes：");
        parsedDateList.forEach(pair -> {
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
        });
        System.out.println();
    }

    private void doCollect(List<Pair<String, String>> parsedDateList, Map<Integer, Integer> lunarLeapMonthMap,
                           String content, int year) {
        Map<String, String> dateMap = new LinkedHashMap<>();
        String[] lines = content.split("\n");

        for (int i=3; i<lines.length; i++){
            String solarDate = parseSolarDate(lines[i]);
            String lunarDate = parseLunarDate(lines[i]);
            if (null!=solarDate && !solarDate.isEmpty()) {
                dateMap.put(solarDate, lunarDate);
            }
        }

        // 获取闰月信息
        String lunarDateLeapMonth = dateMap.values().stream()
                .filter(value -> value.startsWith("閏"))
                .findFirst()
                .orElse(null);
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
        dateMap.forEach((key, value) ->{
            if (value.endsWith("月")) {
                String lunarCode = key.substring(5, 7) + key.substring(8, 10);
                Pair<String, String> pair = new Pair<>(year + value, lunarCode);
                parsedDateList.add(pair);
            }
        });
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
}
