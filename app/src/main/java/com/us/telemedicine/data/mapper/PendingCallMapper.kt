package com.us.telemedicine.data.mapper

import com.us.telemedicine.data.entity.response.PendingCall
import com.us.telemedicine.domain.entity.PendingCallEntity
import com.us.telemedicine.global.extention.asDate

class PendingCallMapper {

    companion object {
        fun toPendingCallEntity(input: PendingCall?): PendingCallEntity? {
            input?.sessionId ?: return null

            return PendingCallEntity(
                sessionId = input.sessionId,
                callerName = input.callerName ?: "",
                pendingSince = input.pendingSince?.asDate(),
                isAutoAnswerEnabled = input.isAutoAnswerEnabled ?: false,
                isMeeting = input.isMeeting ?: false
            )
        }

        fun toPendingCallEntityList(input: List<PendingCall>?): List<PendingCallEntity> {
            input ?: return emptyList()
            return input.mapNotNull { call -> toPendingCallEntity(call) }
        }
    }
}