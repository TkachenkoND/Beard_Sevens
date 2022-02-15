package ram.hesokio.srawber.data.database.dao

import io.realm.Realm
import ram.hesokio.srawber.data.database.models.Data

class DataDaoImpl : DataDao {

    override suspend fun saveFullLinkInDataBase(newFullLink: String) {
        val dataBase = Realm.getDefaultInstance()

        dataBase.executeTransaction {
            val fullLink = Data().apply {
                fullLink = newFullLink
            }
            it.insert(fullLink)
        }
        dataBase.close()
    }

    override suspend fun getFullLinkFromDataBase(): String {
        val dataBase = Realm.getDefaultInstance()
        return dataBase.where(Data::class.java).findFirst()!!.fullLink
    }
}