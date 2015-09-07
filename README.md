# 中国农历
中国农历的Java 实现。
支持公历范围为：1900-01-31到2099-12-31范围内，农历日期和公历日期的转换。

## 实现思路
通过记录1900年-2099年间的农历信息到二维数组中，经过查询和相关计算就能实现公历日期和农历日期之间的转化。
例如： <br>
{ 8, 131, 301, 331, 429, 528, 627, 726, 825, 924, 1023, 1122, 1222, 1320 }, // 1900 <br>
{ 0, 219, 320, 419, 518, 616, 716, 814, 913, 1012, 1111, 1211, 1310 }, // 1901 <br>
...
* 每个数组的第一个数表示该年闰月月份，为0表示不闰月.
* 数组中其他数表示该月初一对应的公历日期.

## 方法概要														 
* void add(int field, int amount) 日期增加                                        
* static int binSearch(int[] array, int n) 一个简单的二分查找，返回查找到的元素坐标，用于查找农历二维数组信息  
* static char getAnimalYearName(int y) 获取生肖名                                           
* int getDate()                                                                 
* java.lang.String getDayName(int lunarDay) 获取天                                   
* java.lang.String getFullLunarName() 返回中国农历的全名                              
* int getLeapMonth()                                                                 
* int getMonth()                                                                          
* java.lang.String getMonthName(int lunarMonth) 获取农历月份                                    
* java.util.GregorianCalendar getSolar()                                                            
* static java.lang.String getTraditionalYearName(int y) 返回传统天干地支年名称                                  
* int getYear()                                                                                              
* java.lang.String getYearName(int lunarYear) 获取年                                                              
* boolean isLeapMonth()                                                                                          
* static long luanrDiff(LunarCalendar lc1, LunarCalendar lc2, int field) 计算两个农历日期之差                          
* static java.util.Calendar lunar2Solar(int lunarYear, int lunarMonth, int LunarDate, boolean isLeapMonth) 农历转公历                                             
* void setDate(int date)                                                                                       
* void setLeapMonth(boolean isLeapMonth)                                                                 
* void setLeapMonth(int leapMonth)                                                                               
* void setMonth(int month)                                                                                       
* void setSolar(java.util.GregorianCalendar solar)                                                            
* void setYear(int year)                                                                                    
* static LunarCalendar solar2Lunar(java.util.Calendar solar) 公历转农历                                             
* void solarAdd(int field, int n)                                                                               
* static long solarDateCodesDiff(int solarCode1, int solarCode2, int field) 判断两个整数所代表公历日期的差值 一年按365天计算，一个月按30天计算  
* static long solarDiff(java.util.Calendar solar1, java.util.Calendar solar2, int field) 求两个公历日期之差，field可以为年月日，时分秒 一年按365天计算，一个月按30天计算  
* java.lang.String toString()  

## Credits

  - [heqiao2010](https://github.com/heqiao2010)

## License

[The MIT License](http://opensource.org/licenses/MIT)

## 参考
[conis用JS实现的lunar](http://github.com/conis/lunar)
