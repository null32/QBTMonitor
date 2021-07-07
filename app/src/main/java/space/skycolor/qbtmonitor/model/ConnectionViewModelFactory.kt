package space.skycolor.qbtmonitor.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import space.skycolor.qbtmonitor.entity.ConnectionDao

class ConnectionViewModelFactory(
    private val connectionDao: ConnectionDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConnectionViewModel(connectionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}