package space.skycolor.qbtmonitor.model

import space.skycolor.qbtmonitor.api.model.ServerStateModel

class ServerStateExtended(val model: ServerStateModel?) {
    val totalUp get() = TorrentExtended.sizeToHuman(model?.alltime_ul ?: 0)
    val totalDown get() = TorrentExtended.sizeToHuman(model?.alltime_dl ?: 0)
}