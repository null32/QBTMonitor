package space.skycolor.qbtmonitor.model

import space.skycolor.qbtmonitor.api.QbtApi
import space.skycolor.qbtmonitor.api.model.MainDataModel
import space.skycolor.qbtmonitor.entity.ConnectionEntity

data class ConnectionExtended(
    val base: ConnectionEntity,
    val Api: QbtApi,
    var Version: String = "loading",
    var MainData: MainDataModel? = null
)