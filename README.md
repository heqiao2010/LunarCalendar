# 中国农历
中国农历支持公历日期和农历日期之间的相互转化，支持获取年份的生肖以及天干地支表示；支持公历范围为：1850-02-12到2150-12-31。

本着简单，逻辑清晰的原则，核心类只实现基本的日期转化功能。

## 关于数据的验证
目前从公历范围：1901-01-01到2100-12-31 的数据是经过验证的，验证的依据是香港天文台提供的
[公历农历对照表](https://www.hko.gov.hk/tc/gts/time/conversion1_text.htm)。

超出此范围的数据验证可以对比其他数据，比如手机APP日历或者第三方API等。

# 语言支持

支持的语言列表如下：
- [java](https://github.com/heqiao2010/LunarCalendar/tree/master/java)
- golang
- python
- c++

## Source Code

 - [LunarCalendar](https://github.com/heqiao2010/LunarCalendar)


## Credits

  - [heqiao2010](https://github.com/heqiao2010)

## 受conis用JS实现的lunar启发
[conis用JS实现的lunar](http://github.com/conis/lunar)
