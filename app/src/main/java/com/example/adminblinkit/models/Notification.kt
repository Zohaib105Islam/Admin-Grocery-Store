package com.example.adminblinkit.models

import com.example.adminblinkit.models.NotificationData

data class Notification(
    val to : String? = null,
    val data: NotificationData
)
