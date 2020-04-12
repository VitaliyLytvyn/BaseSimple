package com.me.basesimple.domain.interactor


import androidx.lifecycle.LiveData
import com.me.basesimple.domain.ReposRepository
import javax.inject.Inject

class DownloadLargeFileUC
@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<LiveData<String>, String>() {

    override suspend fun run(params: String) = reposRepository.downloadFile(params)
}
