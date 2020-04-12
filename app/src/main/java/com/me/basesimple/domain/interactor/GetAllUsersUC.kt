package com.me.basesimple.domain.interactor

import com.me.basesimple.domain.ReposRepository
import com.me.basesimple.domain.entity.UserAuthent
import javax.inject.Inject

class GetAllUsersUC
@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<List<UserAuthent>, BaseUseCase.None>() {

    override suspend fun run(params: None) = reposRepository.getUsersFromDB()
}
