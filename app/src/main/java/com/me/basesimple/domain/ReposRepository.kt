package com.me.basesimple.domain


import androidx.lifecycle.LiveData
import com.me.basesimple.domain.entity.Repo
import com.me.basesimple.domain.entity.UserAuthent
import com.me.basesimple.domain.interactor.Either
import com.me.basesimple.global.extention.Failure
import com.me.basesimple.domain.room.entity.RepoEntity

interface ReposRepository {
    suspend fun repoes(): Either<Failure, List<Repo>>

    suspend fun saveNewRepo(repo: RepoEntity): Either<Failure, Boolean>
    suspend fun getUsersFromDB(): Either<Failure, List<UserAuthent>>
    suspend fun getOneUserFromDB(key: String): Either<Failure, UserAuthent?>
    suspend fun downloadFile(pathFrom: String): Either<Failure, LiveData<String>>
    suspend fun saveNewUser(userAuthent: UserAuthent): Either<Failure, Boolean>
}
