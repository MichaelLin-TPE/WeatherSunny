package com.weather.sunny.widget

import android.Manifest
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.weather.sunny.R
import java.lang.Exception
import java.util.Locale

class AppWidget : AppWidgetProvider(){

    private lateinit var views: RemoteViews
    private lateinit var googleApiClient: GoogleApiClient
    private var context : Context? = null

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        if (context == null || appWidgetManager == null || appWidgetIds == null) {
            return
        }
        for (appId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appId)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        views = RemoteViews(context.packageName, R.layout.widget_layout)
        views.setTextViewText(R.id.location, "台北市 大安區")
        views.setTextViewText(R.id.description, "多雲時晴偶陣雨")
        views.setTextViewText(R.id.pop_percent, "降雨機率 : 50%")
        this.context = context
        searchLocation(context)




        appWidgetManager.updateAppWidget(appWidgetId, views)

    }

    private fun searchLocation(context: Context) {
        Log.i("Michael","searchLocation")
        val client = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("Michael","AppWidget沒有權限")
            return
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                if (result == null){
                    Log.i("Michael","result is null")
                    return
                }
                for (location in result.locations){
                    Log.i("Michael","location is not null")


                }
            }
        }

        client.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }




}