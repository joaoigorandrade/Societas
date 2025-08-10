package com.example.societas.Configurations

import com.example.societas.Controllers.*
import com.example.societas.Services.*
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    single { UserService() }
    single { UserController(get()) }
    single { HealthService() }
    single { HealthController(get()) }
    single { ConversationService() }
    single { ConversationController(get()) }
    single { MessageService() }
    single { MessageController(get()) }
    single { ConversationParticipantService() }
    single { ConversationParticipantController(get()) }
}
