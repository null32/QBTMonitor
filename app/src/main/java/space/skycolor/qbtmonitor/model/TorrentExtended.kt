package space.skycolor.qbtmonitor.model

import space.skycolor.qbtmonitor.api.model.TorrentModel
import java.text.SimpleDateFormat
import java.util.*

class TorrentExtended(val model: TorrentModel) {
    val title get() = model.name
    val progress get() = ((model.completed ?: 0) / (model.size ?: 1) * 100).toInt()
    val total get() = 100
    val addedOn get() = printDate(model.added_on ?: 0)
    val completedOn get() = printDate(model.completed ?: 0)
    val downSpeed get() = speedToHuman(model.dlspeed ?: 0)
    val upSpeed get() = speedToHuman(model.upspeed ?: 0)
    val size get() = sizeToHuman(model.size ?: 0)


    companion object {
        fun printDate(secs: Long): String {
            val c = Calendar.getInstance()
            c.timeInMillis = secs * 1000
            val f = SimpleDateFormat("yyyy.MM.dd hh:mm:ss")
            return f.format(c.time)
        }

        fun speedToHuman(sz: Long): String {
            val postfixes = listOf("b/s", "kb/s", "mb/s", "gb/s", "tb/s")
            var amount = sz.toDouble()
            var index = 0
            while (amount >= 1024) {
                amount /= 1024
                index += 1
            }
            return "%.2f".format(amount) + " ${postfixes[index]}"
        }


        fun sizeToHuman(sz: Long): String {
            val postfixes = listOf("b", "Kb", "Mb", "Gb", "Tb")
            var amount = sz.toDouble()
            var index = 0
            while (amount >= 1024) {
                amount /= 1024
                index += 1
            }
            return "%.2f".format(amount) + " ${postfixes[index]}"
        }
    }
}