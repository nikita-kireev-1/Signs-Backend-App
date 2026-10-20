package di

import config.AppConfig
import handler.AuthHandler
import handler.SignHandler
import handler.SignReactionHandler
import handler.SignReportHandler
import handler.UserHandler
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module
import repository.Implementation.SignReactionRepositoryImplementation
import repository.Implementation.SignReportRepositoryImplementation
import repository.Implementation.SignRepositoryImplementation
import repository.Implementation.UserRepositoryImplementation
import repository.SignReactionRepositoryInterface
import repository.SignReportRepositoryInterface
import repository.SignRepositoryInterface
import repository.UserRepositoryInterface
import service.AuthService
import service.JWTService
import service.SignReactionService
import service.SignReportService
import service.SignService
import service.UserService
import service.interfaces.AuthServiceInterface
import service.interfaces.JWTServiceInterface
import service.interfaces.SignReactionServiceInterface
import service.interfaces.SignReportServiceInterface
import service.interfaces.SignServiceInterface
import service.interfaces.UserServiceInterface

fun appModule(appConfig : AppConfig) = module {
    single<AppConfig>{
        appConfig
    }

    single<SignRepositoryInterface> {
        SignRepositoryImplementation(get())
    }

    single<UserRepositoryInterface> {
        UserRepositoryImplementation(get())
    }
    single<SignReportRepositoryInterface>{
        SignReportRepositoryImplementation(get())
    }
    single<SignReactionRepositoryInterface>{
        SignReactionRepositoryImplementation(get())
    }

    single<SignServiceInterface> {
        SignService(
            signRepository = get(),
        )
    }

    single<AuthServiceInterface> {
        AuthService(
            userRepository = get(),
            userService = get(),
            jwtService = get()
        )
    }

    single<SignReportServiceInterface> {
        SignReportService(
            signReportRepository = get()
        )
    }

    single<SignReactionServiceInterface> {
        SignReactionService(
            signReactionRepository = get()
        )
    }
    single<JWTServiceInterface>{
        JWTService(
            config = get<AppConfig>().jwt
        )
    }
    single<UserServiceInterface>{
        UserService(
            userRepository = get()
        )
    }
    single<Database> {
        Database.connect(
            url = get<AppConfig>().database.url,
            driver = get<AppConfig>().database.driver,
            user = get<AppConfig>().database.user,
            password = get<AppConfig>().database.password
        )
    }
    single{
        AuthHandler(get())
    }
    single{
        SignHandler(get())
    }
    single{
        UserHandler(get())
    }
    single{
        SignReportHandler(get())
    }
    single{
        SignReactionHandler(get())
    }
}