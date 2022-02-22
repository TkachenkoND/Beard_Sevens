package ram.hesokio.srawber.data.database.dao

interface DataDao {
    suspend fun saveFullLinkInDataBase(newFullLink: String, newFlag: String)

    suspend fun getFullLinkFromDataBase() : String
    suspend fun getFlagFromDataBase() : String
}