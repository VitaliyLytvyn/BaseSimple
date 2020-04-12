package com.me.basesimple.domain.interactor

import com.me.basesimple.domain.ReposRepository
import com.me.basesimple.domain.entity.UserAuthent
import com.me.basesimple.presentation.entity.UserEntity
import javax.inject.Inject

class SaveUserUC

@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<Boolean, UserEntity>() {

    override suspend fun run(params: UserEntity) = reposRepository.saveNewUser(UserAuthent(params))
}