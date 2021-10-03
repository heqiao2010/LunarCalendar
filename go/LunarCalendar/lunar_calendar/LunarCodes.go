package lunar_calendar

// GetMonthCodes 农历年的代码数组
func GetMonthCodes(year int32) []uint32 {
	return lunarInfo[year-MiniYear]
}

// GetCodeYear 从代码中获取年份，大于12时表示下一年
func GetCodeYear(code uint32) uint32 {
	if code/100 > 12 {
		return 1
	}
	return 0
}

// GetCodeMonth 从代码中获取农历月初一在公历中的月份
func GetCodeMonth(code uint32) uint32 {
	month := code / 100
	if month > 12 {
		month -= 12
	}
	return month
}

// GetCodeDay 从代码中获取农历日在公历中的日 (day of month)
func GetCodeDay(code uint32) uint32 {
	return code % 100
}
