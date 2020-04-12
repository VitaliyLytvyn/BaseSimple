package com.me.basesimple.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.me.basesimple.domain.entity.Repo
import com.me.basesimple.domain.entity.UserAuthent
import com.me.basesimple.domain.interactor.GetRepoesUC
import com.me.basesimple.domain.interactor.BaseUseCase
import com.me.basesimple.domain.interactor.DownloadLargeFileUC
import com.me.basesimple.domain.interactor.GetAllUsersUC
import com.me.basesimple.global.BaseViewModel
import com.me.basesimple.presentation.entity.RepoView
import com.me.basesimple.presentation.entity.UserEntity
import javax.inject.Inject

class RepoesViewModel
@Inject constructor(
    private val getRepoesUseCase: GetRepoesUC,
    private val getAllUsersUC: GetAllUsersUC,
    private val downloadLargeFileUC: DownloadLargeFileUC
) : BaseViewModel() {

    private var _repoes: MutableLiveData<List<RepoView>> = MutableLiveData()
    var repoes: LiveData<List<RepoView>> = _repoes

    private var _allUsers: MutableLiveData<List<UserEntity>> = MutableLiveData()
    var allUsers: LiveData<List<UserEntity>> = _allUsers

    private var _fileLoaded: MutableLiveData<String> = MutableLiveData()
    var fileLoaded: LiveData<String> = _fileLoaded


    fun loadRepoes() = getRepoesUseCase(viewModelScope, BaseUseCase.None()) { it.fold(::handleFailure, ::handlerepoList) }

    private fun handlerepoList(repoes: List<Repo>) {
        this._repoes.value = repoes.map { RepoView(it.id, it.name, it.fullName, it.url) }
    }


    fun loadAllUsers() = getAllUsersUC(viewModelScope, BaseUseCase.None()) { it.fold(::handleFailure, ::handleAllUsersList) }


    private fun handleAllUsersList(repoes: List<UserAuthent>) {
        this._allUsers.value = repoes.map { UserEntity(it.uid, it.email, it.name,  it.phoneNumber, it.photoUrl) }
    }


    fun loadLargeFile() = downloadLargeFileUC(viewModelScope, "") { it.fold(::handleFailure, ::handleDownloadFile) }

    private fun handleDownloadFile(file: LiveData<String>) {
        this.fileLoaded = file
    }
}