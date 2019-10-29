package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, timeUnits: TimeUnits = TimeUnits.SECOND): Date {
    var currentTime = this.time

    currentTime += when (timeUnits) {
        TimeUnits.SECOND -> value * timeUnits.value
        TimeUnits.MINUTE -> value * timeUnits.value
        TimeUnits.HOUR -> value * timeUnits.value
        TimeUnits.DAY -> value * timeUnits.value
    }
    
    this.time = currentTime
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val currentTime = date.time
    val messageTime = this.time
    val isInFuture = currentTime < messageTime

    return when (val timeUnitDiff = Math.abs(currentTime - messageTime)) {
        in 0..TimeUnits.SECOND.value -> "только что"
        in TimeUnits.SECOND.value..45 * TimeUnits.SECOND.value -> if (isInFuture) "через несколько секунд" else "несколько секунд назад"
        in (45 * TimeUnits.SECOND.value)..(75 * TimeUnits.SECOND.value )-> if (isInFuture) "через минуту" else "минуту назад"
        in (75 * TimeUnits.SECOND.value)..(45 * TimeUnits.MINUTE.value )-> if (isInFuture) "через ${TimeUnits.MINUTE.convertValue(timeUnitDiff)}" else "${TimeUnits.MINUTE.convertValue(timeUnitDiff)} назад"
        in (45 * TimeUnits.MINUTE.value)..(75 * TimeUnits.MINUTE.value )-> if (isInFuture) "через час" else "час назад"
        in (75 * TimeUnits.MINUTE.value)..(22 * TimeUnits.HOUR.value )-> if (isInFuture) "через ${TimeUnits.HOUR.convertValue(timeUnitDiff)}" else "${TimeUnits.HOUR.convertValue(timeUnitDiff)} назад"
        in (22 * TimeUnits.HOUR.value)..(26 * TimeUnits.HOUR.value )-> if (isInFuture) "через день" else "день назад"
        in (26 * TimeUnits.HOUR.value)..(360 * TimeUnits.DAY.value )-> if (isInFuture) "через ${TimeUnits.DAY.convertValue(timeUnitDiff)}" else "${TimeUnits.DAY.convertValue(timeUnitDiff)} назад"
        else -> if (isInFuture) "более чем через год" else "более года назад"
    }
}

enum class TimeUnits(val value: Long) {
    SECOND(1000L),
    MINUTE(SECOND.value * 60),
    HOUR(MINUTE.value * 60),
    DAY(HOUR.value * 24);

    fun convertValue(timeUnitDiff: Long): String{
        val result = (timeUnitDiff/this.value).toInt()
        return this.plural(result)
    }

    fun plural(value: Int): String {
        val plurals = mapOf(
            SECOND to Triple("секунду", "секунды", "секунд"),
            MINUTE to Triple("минуту", "минуты", "минут"),
            HOUR to Triple("час", "часа", "часов"),
            DAY to Triple("день", "дня", "дней")
        )

        val mark10 = value % 10
        val mark100 = value % 100

        return when {
            mark100 in 10..20 -> "$value ${plurals[this]?.third}"
            mark10 == 1 -> "$value ${plurals[this]?.first}"
            mark10 in 2..4 -> "$value ${plurals[this]?.second}"
            else -> "$value ${plurals[this]?.third}"
        }
    }
}
