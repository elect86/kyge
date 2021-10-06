package kyge.triggerEvents

import kyge.builder
import kyge.indent
import kyge.plusAssign

interface Schedule {

    /** crontab.guru */
    fun schedule(block: Cron.() -> Unit) {
        builder += "schedule:"
        indent {
            Cron.apply(block)
        }
    }

    var schedule: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = schedule { cron = value }

    object Cron {
        var cron: String
            @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
            set(value) {
                builder += "- cron:  '$value'"
            }

        fun cron(block: Builder.() -> Unit) {
            val cron = Builder().apply(block)
            builder += "- cron:  '$cron'"
        }

        class Builder {
            private var min = "*"
            private var h = "*"
            private var dM = "*"
            private var mon = "*"
            private var dW = "*"


            var minute: Any
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    min = when (value) {
                        is Int -> {
                            check(value in 0..59)
                            value.toString()
                        }
                        is IntProgression -> value.joinToString(",")
                        is String -> value
                        else -> error("invalid")
                    }
                }

            fun minutes(vararg minutes: Int) {
                min = minutes.joinToString(",")
            }

            var hour: Any
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    h = when (value) {
                        is Int -> {
                            check(value in 0..23)
                            value.toString()
                        }
                        is IntProgression -> value.joinToString(",")
                        is String -> value
                        else -> error("invalid")
                    }
                }

            fun hours(vararg hours: Int) {
                h = hours.joinToString(",")
            }

            var dayOfMonth: Any
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    dM = when (value) {
                        is Int -> {
                            check(value in 0..31)
                            value.toString()
                        }
                        is IntProgression -> value.joinToString(",")
                        is String -> value
                        is ClosedRange<*> -> (value as ClosedRange<Day>).run { "$start-$endInclusive" }
                        else -> error("invalid")
                    }
                }

            fun daysOfMonth(vararg days: Int) {
                dM = days.joinToString(",")
            }

            fun daysOfMonth(vararg days: Day) {
                dM = days.joinToString(",")
            }

            var month: Any
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    mon = when (value) {
                        is Month -> value.toString()
                        is Int -> {
                            check(value in 0..11)
                            value.toString()
                        }
                        is IntProgression -> value.joinToString(",")
                        is String -> value
                        is ClosedRange<*> -> (value as ClosedRange<Month>).run { "$start-$endInclusive" }
                        else -> error("invalid")
                    }
                }

            fun months(vararg days: Int) {
                mon = days.joinToString(",")
            }

            fun months(vararg months: Month) {
                mon = months.joinToString(",")
            }

            var dayOfWeek: Any
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    dW = when (value) {
                        is Day -> value.toString()
                        is Int -> {
                            check(value in 0..11)
                            value.toString()
                        }
                        is IntProgression -> value.joinToString(",")
                        is String -> value
                        is ClosedRange<*> -> (value as ClosedRange<Day>).run { "$start-$endInclusive" }
                        else -> error("invalid")
                    }
                }

            fun daysOfWeek(vararg days: Int) {
                dW = days.joinToString(",")
            }

            fun daysOfWeek(vararg days: Day) {
                dW = days.joinToString(",")
            }

            fun daysOfWeek(daysOfWeek: String) {
                dW = daysOfWeek
            }

            enum class Month { JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC }
            enum class Day { MON, TUE, WED, THU, FRI, SAT, SUN }

            override fun toString() = "$min $h $dM $mon $dW"
        }
    }
}