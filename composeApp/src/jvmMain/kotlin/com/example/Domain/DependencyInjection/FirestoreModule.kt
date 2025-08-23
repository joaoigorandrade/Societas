package com.example.societas.di

import com.example.Domain.Repository.Firebase.FirestoreRepository
import com.example.Domain.Repository.Firebase.FirestoreRepositoryInterface
import com.google.firebase.cloud.FirestoreClient
import org.koin.dsl.module

val firestoreModule = module {
    single { FirestoreClient.getFirestore() }
    single<FirestoreRepositoryInterface> { FirestoreRepository(get()) }
}
