package de.stuttgart.syzl3000.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stuttgart.syzl3000.presentation.ui.BaseApplication
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideRandomString(): String {
        return "Hilt Dependency Injectionworks"
    }

//    @Singleton
//    @Provides
//    fun provideAuthService(): AuthService {
//        authService = AuthService(encryptedPreferences)
//        return
//    }
}