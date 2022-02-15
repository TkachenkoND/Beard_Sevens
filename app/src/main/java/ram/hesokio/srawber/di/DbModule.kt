package ram.hesokio.srawber.di

import org.koin.dsl.module
import ram.hesokio.srawber.data.database.dao.DataDao
import ram.hesokio.srawber.data.database.dao.DataDaoImpl

val dbModule = module {

    fun provideDataDao(): DataDao {
        return DataDaoImpl()
    }

    factory { provideDataDao() }

}