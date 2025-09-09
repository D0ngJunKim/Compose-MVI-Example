package com.yeogibook.network.util

import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class CookieHandler : CookieJar {
    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        val cookieList = arrayListOf<Cookie>()
        val hostCookies = CookieManager.getInstance().getCookie(url.host).orEmpty()
        if (hostCookies.isNotEmpty()) {
            hostCookies.split(";").mapNotNullTo(cookieList) { cookie ->
                Cookie.parse(url, cookie)
            }
        }
        return cookieList
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        CookieManager.getInstance().run {
            for (cookie in cookies) {
                setCookie(url.toString(), cookie.toString())
            }
            flush()
        }
    }
}