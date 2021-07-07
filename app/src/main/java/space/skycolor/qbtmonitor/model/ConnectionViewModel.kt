package space.skycolor.qbtmonitor.model

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import space.skycolor.qbtmonitor.api.QbtApi
import space.skycolor.qbtmonitor.entity.ConnectionDao
import space.skycolor.qbtmonitor.entity.ConnectionEntity

class ConnectionViewModel(private val connectionDao: ConnectionDao)
    : ViewModel() {
    private val _connections: LiveData<List<ConnectionEntity>> = connectionDao.getItems().asLiveData()
    public val connections get() = _connections

    private val _extended = MutableLiveData<HashMap<Int, ConnectionExtended>?>()
    public val extended get() = Transformations.map(_extended) {
        it?.map { e -> e.value }
    }

    fun singleConnection(id: Int): LiveData<ConnectionExtended> {
        return Transformations.map(_extended) {
            it?.filter { e -> e.key == id }?.get(0)
        }
    }

    public fun AddConnection(item: ConnectionEntity) {
        this.viewModelScope.launch {
            connectionDao.insert(item)
        }
    }

    public fun RemoveConnection(item: ConnectionEntity) {
        this.viewModelScope.launch {
            connectionDao.delete(item)
        }
    }

    public fun UpdateVersion(item: ConnectionEntity) {
        Log.d("QBT", "Updating version for ${item.title}")
        if (_extended.value == null) {
            _extended.value = HashMap()
        }
        if (!_extended.value!!.containsKey(item.id)) {
            _extended.value!![item.id] = ConnectionExtended(item, QbtApi(item.address))
        } else {
            _extended.value!![item.id]!!.base.let { item }
        }
        val ei = _extended.value!![item.id]!!
        this.viewModelScope.launch {
            if (!ei.Api.isLoggedIn) {
                ei.Api.Login(ei.base.username, ei.base.password)
            }
            ei.Version = ei.Api.GetVersion()

            _extended.value = _extended.value
        }
    }

    public fun UpdateVersionAll(items: List<ConnectionEntity>) {
        items.forEach{
            UpdateVersion(it)
        }
    }

    public fun UpdateVersionAll() {
        UpdateVersionAll(_connections.value!!)
    }

    public fun UpdateMainData(id: Int) {
        val item = connections.value?.filter { e -> e.id == id }?.get(0)!!
        Log.d("QBT", "Updating main data for ${item.title}")
        val ei = _extended.value!![item.id]!!
        this.viewModelScope.launch {
            if (!ei.Api.isLoggedIn) {
                ei.Api.Login(ei.base.username, ei.base.password)
            }
            ei.MainData = ei.Api.GetMainData()

            _extended.value = _extended.value
        }
    }

    public fun getNewConnectionEntity(title: String, address: String, user: String, pass: String): ConnectionEntity {
        return ConnectionEntity(
            title = title,
            address = address,
            username = user,
            password = pass
        )
    }
}