package de.stuttgart.syzl3000.di

import android.content.Context
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stuttgart.syzl3000.BaseApplication
import de.stuttgart.syzl3000.services.AuthService
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