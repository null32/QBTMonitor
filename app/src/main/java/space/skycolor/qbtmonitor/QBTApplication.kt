package space.skycolor.qbtmonitor

import android.support.multidex.MultiDexApplication
import space.skycolor.qbtmonitor.entity.AppDatabase

class QBTApplication : MultiDexApplication() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}