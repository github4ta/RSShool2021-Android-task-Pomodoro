package school.rs.tsarik.pomodoro

data class Stopwatch(
    val id: Int,
    val startMs: Long,
    var currentMs: Long,
    var isStarted: Boolean
)