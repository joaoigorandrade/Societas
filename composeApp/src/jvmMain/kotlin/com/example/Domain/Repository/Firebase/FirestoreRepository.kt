package com.example.Domain.Repository.Firebase

import com.example.Domain.Models.Message.FirestoreMessage
import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatModel
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.ListenerRegistration
import com.google.cloud.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreRepository(private val db: Firestore) : FirestoreRepositoryInterface {

    override fun getMessages(userId: String, agentId: String): Flow<NetworkResult<SocietasChatModel>> = callbackFlow {
        trySend(NetworkResult.Loading(true))

        val chatsRef = db.collection("users").document(userId).collection("chats")
        val chatQuery = chatsRef.whereArrayContains("participants", agentId)
        var messagesListenerRegistration: ListenerRegistration? = null

        val chatListenerRegistration = chatQuery.addSnapshotListener { chatSnapshot, error ->
            messagesListenerRegistration?.remove()

            if (error != null) {
                trySend(NetworkResult.Error(NetworkException.UnknownError(error.message ?: "Firestore chat query error")))
                close(error)
                return@addSnapshotListener
            }

            if (chatSnapshot == null || chatSnapshot.isEmpty) {
                trySend(NetworkResult.Error(NetworkException.HttpError(404, "Chat not found for agentId: $agentId")))
                return@addSnapshotListener
            }

            val chatDoc = chatSnapshot.documents[0]
            val chatId = chatDoc.id

            val messagesQuery = chatDoc.reference.collection("messages").orderBy("time", Query.Direction.ASCENDING)
            messagesListenerRegistration = messagesQuery.addSnapshotListener { messagesSnapshot, messagesError ->
                if (messagesError != null) {
                    trySend(NetworkResult.Error(NetworkException.UnknownError(messagesError.message ?: "Firestore messages query error")))
                    close(messagesError)
                    return@addSnapshotListener
                }

                if (messagesSnapshot != null) {
                    val messages = messagesSnapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(FirestoreMessage::class.java)?.let { firestoreMessage ->
                                SocietasChatModel.Message(
                                    text = firestoreMessage.content,
                                    author = firestoreMessage.sender
                                )
                            }
                        } catch (e: Exception) {
                            println("Failed to deserialize message: ${doc.id}, error: ${e.message}")
                            null
                        }
                    }
                    trySend(NetworkResult.Success(SocietasChatModel(chatId = chatId, messages = messages)))
                }
            }
        }

        awaitClose {
            chatListenerRegistration.remove()
            messagesListenerRegistration?.remove()
        }
    }
}

suspend fun <T> ApiFuture<T>.await(): T = suspendCoroutine { continuation ->
    addListener({
        try {
            continuation.resume(get())
        } catch (e: Exception) {
            continuation.resumeWithException(e.cause ?: e)
        }    }, Executors.newSingleThreadExecutor())
}
