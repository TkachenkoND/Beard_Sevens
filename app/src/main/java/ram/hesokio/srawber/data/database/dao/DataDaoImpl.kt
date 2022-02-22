package ram.hesokio.srawber.data.database.dao

import io.realm.Realm
import ram.hesokio.srawber.data.database.models.Data

class DataDaoImpl : DataDao {

    override suspend fun saveFullLinkInDataBase(newFullLink: String, newFlag: String) {
        val dataBase = Realm.getDefaultInstance()

        dataBase.executeTransaction {
            val data = Data().apply {
                fullLink = newFullLink
                flag = newFlag
            }
            it.insertOrUpdate(data)
        }
        dataBase.close()
    }

    override suspend fun getFullLinkFromDataBase(): String {
        val dataBase = Realm.getDefaultInstance()
        return dataBase.where(Data::class.java).findFirst()!!.fullLink
    }


    override suspend fun getFlagFromDataBase(): String {
        val dataBase = Realm.getDefaultInstance()
        return dataBase.where(Data::class.java).findFirst()!!.flag
    }
}