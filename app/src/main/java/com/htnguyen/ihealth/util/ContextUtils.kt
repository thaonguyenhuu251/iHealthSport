package com.htnguyen.ihealth.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import java.util.*

class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {

        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = c
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                configuration.setLocale(localeToSwitchTo)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return ContextUtils(context)
        }

        fun updateLanguage(context: Context) {
            val change = when (PreferencesUtil.language) {
                Constant.LG_VIETNAMESE -> {
                    "vi"
                }
                Constant.LG_ENGLISH -> {
                    "en"
                }
                Constant.LG_RUSSIAN -> {
                    "sah"
                }
                Constant.LG_LAOS -> {
                    "lo"
                }
                Constant.LG_THAI -> {
                    "th"
                }
                Constant.LG_KOREAN -> {
                    "ko"
                }
                Constant.LG_CHINESE -> {
                    "zh"
                }
                Constant.LG_JAPANESE -> {
                    "ja"
                }
                Constant.LG_INDONESIAN -> {
                    "in"
                }
                Constant.LG_SPANISH -> {
                    "es"
                }
                Constant.LG_FRENCH -> {
                    "fr"
                }
                Constant.LG_INDIAN -> {
                    "kn"
                }
                Constant.LG_GERMAN -> {
                    "de"
                }
                Constant.LG_ITALIAN -> {
                    "it"
                }
                else -> {
                    "en"
                }
            }
            if (PreferencesUtil.language != null) {
                val resources: Resources = context.resources
                val dm: DisplayMetrics = resources.displayMetrics
                val config: Configuration = resources.configuration
                config.setLocale(Locale(change.toLowerCase(Locale.ROOT)))
                resources.updateConfiguration(config, dm)
            }
        }
    }
}