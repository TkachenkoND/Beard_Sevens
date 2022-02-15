package ram.hesokio.srawber.data.database.dao

interface DataDao {
    suspend fun saveFullLinkInDataBase(newFullLink: String)
    suspend fun getFullLinkFromDataBase() : String
}