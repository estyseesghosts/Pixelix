package foxtails.taeda.domain.service.platform

expect object PlatformFeatures {
    val notificationWidgets: Boolean
    val inAppBrowser: Boolean
    val downloadToGallery: Boolean
    val customAppIcon: Boolean
    val addCollection: Boolean
    val supportsDynamicColors: Boolean
}