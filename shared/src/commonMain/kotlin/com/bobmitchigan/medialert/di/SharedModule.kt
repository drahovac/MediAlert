package com.bobmitchigan.medialert.di

import com.bobmitchigan.medialert.data.MockMedicineRepository
import com.bobmitchigan.medialert.domain.MedicineRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module

val sharedModule = module{
    single<MedicineRepository> {
        MockMedicineRepository()
    }
}

fun initKoin(){
    startKoin {
        modules(sharedModule)
    }
}
