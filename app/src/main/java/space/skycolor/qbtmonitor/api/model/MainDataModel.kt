package space.skycolor.qbtmonitor.api.model

data class MainDataModel(
    val full_update: Boolean?,
    val rid: Int,
    val server_state: ServerStateModel? = null,
    val torrents: LinkedHashMap<String, TorrentModel>? = null,
    val torrents_removed: HashMap<String, TorrentModel>? = null,
)