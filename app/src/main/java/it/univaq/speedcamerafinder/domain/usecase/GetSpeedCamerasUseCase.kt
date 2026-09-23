package it.univaq.speedcamerafinder.domain.usecase

import it.univaq.speedcamerafinder.common.Result
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.repositories.LocalRepository
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// Posizione fissa (L'Aquila) finché lo Step 4-5 non passa quella dell'utente
private const val TEST_LAT = 42.35
private const val TEST_LNG = 13.40
private const val RADIUS_METERS = 20_000

class GetSpeedCamerasUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    operator fun invoke(): Flow<Result<List<SpeedCamera>>> = flow {

        emit(Result.Loading("Loading..."))

        runCatching {
            var localData = localRepository.getAll()
            if (localData.isEmpty()) {
                val remoteData = remoteRepository.downloadData(TEST_LAT, TEST_LNG, RADIUS_METERS)
                localRepository.save(remoteData)

                localData = localRepository.getAll()
            }
            emit(Result.Success(localData))
        }.onFailure {
            emit(Result.Error(it.message ?: "Unknown error"))
        }
    }
}
