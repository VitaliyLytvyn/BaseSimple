package com.us.telemedicine.domain.entity

import java.util.*

class PendingCallEntity (
    val sessionId: String,
    val callerName: String,
    val pendingSince: Date?,
    val isAutoAnswerEnabled: Boolean,
    val isMeeting: Boolean
)