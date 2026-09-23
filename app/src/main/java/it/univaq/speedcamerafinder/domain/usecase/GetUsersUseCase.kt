package it.univaq.speedcamerafinder.domain.usecase

import it.univaq.speedcamerafinder.common.Result
import it.univaq.speedcamerafinder.domain.model.User
import it.univaq.speedcamerafinder.domain.repositories.LocalRepository
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    operator fun invoke(): Flow<Result<List<User>>> = flow {

        emit(Result.Loading("Loading..."))

        runCatching {
            var localData = localRepository.getAll()
            if (localData.isEmpty()) {
                val remoteData = remoteRepository.downloadData()
                localRepository.save(remoteData)

                localData = localRepository.getAll()
            }
            emit(Result.Success(localData))
        }.onFailure {
            emit(Result.Error(it.message ?: "Unknown error"))
        }
    }
}