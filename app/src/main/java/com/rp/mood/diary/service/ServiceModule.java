package com.rp.mood.diary.service;

import com.rp.mood.diary.service.impl.AstronomyServiceImpl;
import com.rp.mood.diary.service.impl.IdentityServiceImpl;
import com.rp.mood.diary.service.impl.MoodAppServiceImpl;
import com.rp.mood.diary.service.impl.NotificationServiceImpl;
import com.rp.mood.diary.service.impl.PlanetoryPositionCalculatorServiceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ServiceModule {

    @Singleton
    @Binds
    public abstract MoodAppService bindMoodAppService(MoodAppServiceImpl moodAppService);

    @Singleton
    @Binds
    public abstract AstronomyService bindAstronomyService(AstronomyServiceImpl astronomyService);

    @Singleton
    @Binds
    public abstract IdentityService bindIdentityService(IdentityServiceImpl identityService);

    @Singleton
    @Binds
    public abstract PlanetoryPositionCalculatorService bindPlanetoryPositionCalcService(PlanetoryPositionCalculatorServiceImpl positionCalculatorService);

    @Singleton
    @Binds
    public abstract NotificationService bindNotificationervice(NotificationServiceImpl notificationService);
}
