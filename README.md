# 中国农历
中国农历的Java实现，本着简洁的原则，用一个Java类不到1000行,不依赖任何第三方库实现。

支持公历范围为：1850-02-12到2150-12-31范围内，农历日期和公历日期的转换。支持获取年份的生肖以及天干地支表示。

## 用法
直接下载源码或者导入依赖。maven:

latest-version=1.2-SNAPSHOT

```
<dependency>
  <groupId>com.github.heqiao2010</groupId>
  <artifactId>lunar</artifactId>
  <version>{latest-version}</version>
</dependency>
```

gradle:

```
compile("com.github.heqiao2010:lunar:{latest-version}")
```

示例：
公历转农历：

```
Calendar today = Calendar.getInstance();
LunarCalendar lunar = LunarCalendar.solar2Lunar(today);
System.out.println(today.getTime() + " <====> " + lunar.getFullLunarName());
```

农历转公历：

```
LunarCalendar lunar = new LunarCalendar();
Calendar today = LunarCalendar.lunar2Solar(lunar.getLyear(), lunar.getLmonth(), lunar.getLdate(), lunar.isLeapMonth());
System.out.println(lunar.getFullLunarName() + " <====> " + today.getTime());
```

## 实现思路
通过记录1850年-2150年间的农历信息到二维数组中，经过查询和相关计算就能实现公历日期和农历日期之间的转化。
例如： <br>
{ 8, 131, 301, 331, 429, 528, 627, 726, 825, 924, 1023, 1122, 1222, 1320 }, // 1900 <br>
{ 0, 219, 320, 419, 518, 616, 716, 814, 913, 1012, 1111, 1211, 1310 }, // 1901 <br>
...
* 每个数组的第一个数表示该年闰月月份，为0表示不闰月.
* 数组中其他数表示该月初一对应的公历日期.


## 测试结果
见solar2lunar.txt

* Solar：1900-01-31 <====> Lunar：一九〇〇年正月初一 庚子年 鼠
* Solar：1900-02-01 <====> Lunar：一九〇〇年正月初二 庚子年 鼠
* Solar：1900-02-02 <====> Lunar：一九〇〇年正月初三 庚子年 鼠
* Solar：1900-02-03 <====> Lunar：一九〇〇年正月初四 庚子年 鼠
* Solar：1900-02-04 <====> Lunar：一九〇〇年正月初五 庚子年 鼠
* Solar：1900-02-05 <====> Lunar：一九〇〇年正月初六 庚子年 鼠
* Solar：1900-02-06 <====> Lunar：一九〇〇年正月初七 庚子年 鼠
* Solar：1900-02-07 <====> Lunar：一九〇〇年正月初八 庚子年 鼠
* Solar：1900-02-08 <====> Lunar：一九〇〇年正月初九 庚子年 鼠
* Solar：1900-02-09 <====> Lunar：一九〇〇年正月初十 庚子年 鼠
* Solar：1900-02-10 <====> Lunar：一九〇〇年正月十一 庚子年 鼠
* Solar：1900-02-11 <====> Lunar：一九〇〇年正月十二 庚子年 鼠
* Solar：1900-02-12 <====> Lunar：一九〇〇年正月十三 庚子年 鼠
* Solar：1900-02-13 <====> Lunar：一九〇〇年正月十四 庚子年 鼠
* Solar：1900-02-14 <====> Lunar：一九〇〇年正月十五 庚子年 鼠
* Solar：1900-02-15 <====> Lunar：一九〇〇年正月十六 庚子年 鼠
* Solar：1900-02-16 <====> Lunar：一九〇〇年正月十七 庚子年 鼠
* Solar：1900-02-17 <====> Lunar：一九〇〇年正月十八 庚子年 鼠
* Solar：1900-02-18 <====> Lunar：一九〇〇年正月十九 庚子年 鼠
* Solar：1900-02-19 <====> Lunar：一九〇〇年正月二十 庚子年 鼠
<br>...

## Source Code

 - [LunarCalendar](https://github.com/heqiao2010/LunarCalendar)


## Credits

  - [heqiao2010](https://github.com/heqiao2010)

## 受conis用JS实现的lunar启发
[conis用JS实现的lunar](http://github.com/conis/lunar)
