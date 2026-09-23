package it.univaq.speedcamerafinder.domain.usecase

import it.univaq.speedcamerafinder.common.Result
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.repositories.LocalRepository
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val RADIUS_METERS = 20_000

class GetSpeedCamerasUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    operator fun invoke(lat: Double, lng: Double): Flow<Result<List<SpeedCamera>>> = flow {

        emit(Result.Loading("Loading..."))

        val download = runCatching {
            val remoteData = remoteRepository.downloadData(lat, lng, RADIUS_METERS)
            localRepository.save(remoteData)
        }

        // Se la rete non risponde mostro comunque gli ultimi autovelox salvati in Room
        val localData = localRepository.getAll()
        if (download.isSuccess || localData.isNotEmpty()) {
            emit(Result.Success(localData))
        }
        // ...ma segnalo anche l'errore, così la UI sa che i dati non sono aggiornati
        if (download.isFailure) {
            emit(Result.Error(download.exceptionOrNull()?.message ?: "Unknown error"))
        }
    }
}
