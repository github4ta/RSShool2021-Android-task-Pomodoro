package school.rs.tsarik.pomodoro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import school.rs.tsarik.pomodoro.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), StopwatchListener {

    private lateinit var binding: ActivityMainBinding

    private val stopwatchAdapter = StopwatchAdapter(this)
    private val stopwatches = mutableListOf<Stopwatch>()
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }

        binding.addNewStopwatchTimer.addTextChangedListener {
            if (binding.addNewStopwatchTimer.text.isNotEmpty()) {
                binding.addNewStopwatchButton.isEnabled = true
                binding.addNewStopwatchButton.isClickable = true
            } else {
                binding.addNewStopwatchButton.isEnabled = false
                binding.addNewStopwatchButton.isClickable = false
            }
        }

        binding.addNewStopwatchButton.setOnClickListener {
            val timerMin = binding.addNewStopwatchTimer.text.toString().toLong()
            val timerMs = TimeUnit.MINUTES.toMillis(timerMin)
            stopwatches.add(Stopwatch(nextId++, timerMs, timerMs, false))
            stopwatchAdapter.submitList(stopwatches.toList())
        }
    }

    override fun start(id: Int) {
        val stopwatchIdStarted = stopwatches.find { it.isStarted }
        if (stopwatchIdStarted != null && stopwatchIdStarted.id != id) {
            stop(stopwatchIdStarted.id, stopwatchIdStarted.currentMs)
        }
        changeStopwatch(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeStopwatch(id, currentMs, false)
    }

    override fun delete(id: Int) {
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    override fun finish(id: Int) {

        Toast.makeText(this, "Timer has finished.", Toast.LENGTH_SHORT).show()
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<Stopwatch>()
        stopwatches.forEach {
            if (it.id == id) {
                newTimers.add(
                    Stopwatch(
                        it.id,
                        it.startMs,
                        currentMs ?: it.currentMs,
                        isStarted
                    )
                )
            } else {
                newTimers.add(it)
            }
        }
        stopwatchAdapter.submitList(newTimers)
        stopwatches.clear()
        stopwatches.addAll(newTimers)
    }
}