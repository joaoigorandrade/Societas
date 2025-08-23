package com.example.Domain.Repository.Firebase

import com.example.Domain.Models.Message.FirestoreMessage
import com.example.Domain.Repository.Firebase.FirestoreRepositoryInterface
import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatModel
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreRepository(private val db: Firestore) : FirestoreRepositoryInterface {

    override suspend fun getMessages(userId: String, agentId: String): NetworkResult<SocietasChatModel> {
        return try {
            val chatsRef = db.collection("users").document(userId).collection("chats")
            val chatQuery = chatsRef.whereArrayContains("participants", agentId)
            val chatSnapshot = chatQuery.get().await()

            if (chatSnapshot.isEmpty) {
                return NetworkResult.Error(NetworkException.HttpError(404, "Chat not found for agentId: $agentId"))
            }

            val chatDoc = chatSnapshot.documents[0]
            val chatId = chatDoc.id

            val messagesSnapshot = chatDoc.reference.collection("messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .await()

            val messages = messagesSnapshot.documents.mapNotNull { doc ->
                val firestoreMessage = doc.toObject(FirestoreMessage::class.java)
                SocietasChatModel.Message(
                    text = firestoreMessage.content,
                    author = firestoreMessage.sender
                )
            }

            NetworkResult.Success(SocietasChatModel(chatId = chatId, messages = messages))
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Firestore error"))
        }
    }
}

suspend fun <T> ApiFuture<T>.await(): T = suspendCoroutine { continuation ->
    addListener({
        try {
            continuation.resume(get())
        } catch (e: Exception) {
            continuation.resumeWithException(e.cause ?: e)
        }
    }, Executors.newSingleThreadExecutor())
}
