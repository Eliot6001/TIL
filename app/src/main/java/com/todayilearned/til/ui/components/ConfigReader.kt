package com.todayilearned.til.ui.components


import android.content.Context
import org.xmlpull.v1.XmlPullParser

object ConfigReader {
    fun shouldPreloadScreens(context: Context): Boolean {
        val id = context.resources.getIdentifier("config", "xml", context.packageName)
        val parser = context.resources.getXml(id)
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "boolean") {
                val name = parser.getAttributeValue(null, "name")
                if (name == "preload_screens") {
                    return parser.nextText().toBoolean()
                }
            }
            parser.next()
        }
        return false
    }
}
