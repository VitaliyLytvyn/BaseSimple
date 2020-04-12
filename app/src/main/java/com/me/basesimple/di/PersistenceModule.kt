package com.me.basesimple.di

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.me.basesimple.domain.room.AppDatabase
import com.me.basesimple.domain.room.RepoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

  @Provides
  @Singleton
  fun provideDatabase(@NonNull application: Application): AppDatabase {
    return Room
      .databaseBuilder(application, AppDatabase::class.java, "Repos.db")
      .allowMainThreadQueries()
      .build()
  }

  @Provides
  @Singleton
  fun provideRepoDao(@NonNull database: AppDatabase): RepoDao {
    return database.repoDao()
  }

//  @Provides
//  @Singleton
//  fun providePeopleDao(@NonNull database: AppDatabase): PeopleDao {
//    return database.peopleDao()
//  }
}
