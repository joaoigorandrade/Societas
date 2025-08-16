package com.example.UI.Screens.Home

import com.example.UI.Screens.SocietasScreenModel
import kotlinx.serialization.Serializable

@Serializable
data class SocietasHomeScreenModel(
    val userName: String,
    val userPhoto: String,
    val enterprise: String,
    val cBoard: Array<Agent>
) : SocietasScreenModel {
    @Serializable
    data class Agent(
        val name: String,
        val department: String
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SocietasHomeScreenModel

        if (userName != other.userName) return false
        if (userPhoto != other.userPhoto) return false
        if (enterprise != other.enterprise) return false
        if (!cBoard.contentEquals(other.cBoard)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userName.hashCode()
        result = 31 * result + userPhoto.hashCode()
        result = 31 * result + enterprise.hashCode()
        result = 31 * result + cBoard.contentHashCode()
        return result
    }
}