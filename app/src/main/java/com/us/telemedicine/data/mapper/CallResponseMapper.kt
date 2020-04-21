package com.us.telemedicine.data.mapper

import com.us.telemedicine.data.entity.response.Caller
import com.us.telemedicine.data.entity.response.CallData
import com.us.telemedicine.domain.entity.CallerEntity
import com.us.telemedicine.domain.entity.CallEntity

class CallResponseMapper {
    companion object {
        fun toCallEntity(input: CallData?) =
            if (input == null) null
            else
                CallEntity(
                    sessionId = input.sessionId,
                    sessionToken = input.sessionToken,
                    userToken = input.userToken,
                    caller = callerToCallerEntity(input.caller),
                    callee = callerToCallerEntity(input.callee)
                )

        private fun callerToCallerEntity(input: Caller?) =
            if (input == null) null
            else
                CallerEntity(
                    id = input.id,
                    fullName = input.fullName
                )
    }
}