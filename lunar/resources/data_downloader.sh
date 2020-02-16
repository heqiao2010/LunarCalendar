#!/bin/sh

# description: 从香港天文台网站下载 1901-2100期间的日历对照信息
# see: https://www.hko.gov.hk/tc/gts/time/conversion1_text.htm

readonly begin_year=1901
readonly end_year=2100

set +e

echo "begin download data..."
for((year=begin_year;year<=end_year;year++));
do
    echo "download $year.txt"
    curl -O "https://www.hko.gov.hk/tc/gts/time/calendar/text/files/T${year}c.txt"
done
echo "done."
