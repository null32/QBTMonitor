package space.skycolor.qbtmonitor.api

import android.util.Log
import com.beust.klaxon.Klaxon
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import space.skycolor.qbtmonitor.api.model.MainDataModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class QbtApi(
    private var baseUrl: String
) {
    private lateinit var retrofit: Retrofit
    private var _isLoggedIn = false
    public val isLoggedIn get() = _isLoggedIn
    private var rid = 0

    private val cacheTime = HashMap<String, Calendar>()
    private val cacheValue = HashMap<String, String>()
    private val cacheT = 60
    private val cacheL = 60 * 60

    init {
        if (!baseUrl.startsWith("http")) {
            baseUrl = "http://$baseUrl"
        }
        retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(baseUrl)
            .client(
                OkHttpClient().newBuilder()
                    .cookieJar(SessionCookieJar())
                    .build()
            )
            .build()
    }

    private val retrofitService: QbtApiService by lazy {
        retrofit.create(QbtApiService::class.java)
    }

    private fun isFresh(old: Calendar, new: Calendar, limit: Int): Boolean {
        new.add(Calendar.SECOND, -limit)
        return new.before(old)
    }

    suspend fun Login(user: String, pass: String) {
        val name = "login"
        if (cacheTime.containsKey(name) && isFresh(cacheTime[name]!!, Calendar.getInstance(), cacheL)) {
            return
        }
        try {
            retrofitService.login(baseUrl, user, pass)
            _isLoggedIn = true
            cacheTime[name] = Calendar.getInstance()
            Log.d("QBT", "Login(${baseUrl})")
        }
        catch (ex: Exception) {
            Log.d("QBT", "Login() fail ${ex}")
        }
    }

    suspend fun GetVersion(): String {
        val name = "version"
        if (cacheTime.containsKey(name) && isFresh(cacheTime[name]!!, Calendar.getInstance(), cacheT)) {
            return cacheValue[name]!!
        }
        var res: String
        try {
            res = retrofitService.getVersion()
            Log.d("QBT", "GetVersion(${baseUrl}) -> ${res}")
            cacheTime[name] = Calendar.getInstance()
            cacheValue[name] = res
        }
        catch (ex: Exception) {
            Log.d("QBT", "GetVersion() fail ${ex}")
            res = ex.toString()
        }
        return res
    }

    suspend fun GetMainData(): MainDataModel? {
        val name = "main_data"
        if (cacheTime.containsKey(name) && isFresh(cacheTime[name]!!, Calendar.getInstance(), cacheT)) {
            return Klaxon().parse<MainDataModel>(cacheValue[name]!!)
        }
        var res: String
        try {
            res = retrofitService.getMainData(rid)
            Log.d("QBT", "GetMainData() -> ok")
            cacheTime[name] = Calendar.getInstance()
            cacheValue[name] = res
            return Klaxon().parse<MainDataModel>(res)
        }
        catch (ex: Exception) {
            Log.d("QBT", "GetMainData() fail ${ex}")
            return null
        }
    }
}

interface QbtApiService {
    @FormUrlEncoded
    @POST("/api/v2/auth/login")
    suspend fun login(@Header("Referer") referer: String, @Field("username") username: String, @Field("password") password: String)
    @GET("/api/v2/app/version")
    suspend fun getVersion(): String
    @GET("/api/v2/sync/maindata")
    suspend fun getMainData(@Query("rid") rid: Int): String
}

private class SessionCookieJar : CookieJar {
    private var cookies: List<Cookie>? = null
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (url.encodedPath().endsWith("login")) {
            this.cookies = ArrayList(cookies)
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return if (!url.encodedPath().endsWith("login") && cookies != null) {
            cookies as List<Cookie>
        } else Collections.emptyList()
    }
}
