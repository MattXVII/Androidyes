package it.univaq.speedcamerafinder.data.remote

import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import javax.inject.Inject

// Dati FITTIZI per provare la UI prima di collegare Overpass (Step 2)
class FakeRemoteRepository @Inject constructor(): RemoteRepository {

    override suspend fun downloadData(): List<SpeedCamera> = listOf(
        SpeedCamera(1, 42.3500, 13.4000, 50, "90", "Autovelox di prova 1"),
        SpeedCamera(2, 42.3600, 13.3800, 70, null, "Autovelox di prova 2"),
        SpeedCamera(3, 42.3400, 13.4200, null, null, null)
    )
}
