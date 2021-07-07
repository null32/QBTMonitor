package space.skycolor.qbtmonitor.api.model

data class TorrentModel(
    val added_on: Long?,
    val completion_on: Long?,
    val name: String?,
    val progress: Double?,
    val content_path: String?,
    val size: Long?,
    val completed: Long?,
    val dlspeed: Long?,
    val upspeed: Long?
)