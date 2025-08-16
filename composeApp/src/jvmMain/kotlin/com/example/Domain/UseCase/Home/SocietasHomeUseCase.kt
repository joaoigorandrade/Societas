package com.example.Domain.UseCase.Home

import com.example.UI.Screens.Home.SocietasHomeScreenModel
import com.example.UI.Screens.SocietasScreenModel
import kotlinx.coroutines.delay

class SocietasHomeUseCase {

    suspend fun execute(): SocietasScreenModel {
        delay(1500)

        return SocietasHomeScreenModel(
            "Joao Igor",
            "https://localhost:3000/photos",
            "Societas",
            arrayOf(
                SocietasHomeScreenModel.Agent(
                    "Fabiano",
                    "Technology"
                ),
                SocietasHomeScreenModel.Agent(
                    "Maria",
                    "Marketing"
                ),
                SocietasHomeScreenModel.Agent(
                    "Jose",
                    "Sales"
                )
            )
        )
    }
}