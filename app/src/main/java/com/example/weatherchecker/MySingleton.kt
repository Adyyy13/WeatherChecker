package com.example.weatherchecker

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MySingleton private constructor(context: Context) {
    private var requestQueue: RequestQueue? = null
    private val ctx: Context = context.applicationContext

    private fun getRequestQueue(): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx)
        }
        return requestQueue!!
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        getRequestQueue().add(req)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: MySingleton? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: MySingleton(context).also { instance = it }
            }
    }
}
