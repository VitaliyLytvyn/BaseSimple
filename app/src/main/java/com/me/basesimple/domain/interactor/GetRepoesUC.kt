package com.me.basesimple.domain.interactor

import com.me.basesimple.domain.ReposRepository
import com.me.basesimple.domain.entity.Repo
import javax.inject.Inject

class GetRepoesUC
@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<List<Repo>, BaseUseCase.None>() {

    override suspend fun run(params: None) = reposRepository.repoes()
}
