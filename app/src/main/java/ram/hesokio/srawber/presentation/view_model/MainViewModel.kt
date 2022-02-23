package ram.hesokio.srawber.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ram.hesokio.srawber.data.database.dao.DataDao

class MainViewModel(
    private val dataBaseDao: DataDao
) : ViewModel() {

    private val _fullLink = MutableLiveData<String>()
    val fullLink: LiveData<String> = _fullLink

    private val _flag = MutableLiveData<String>()
    val flag: LiveData<String> = _flag

    fun saveFullLinkInDataBase(newFullLink: String, newFlag: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseDao.saveFullLinkInDataBase(newFullLink, newFlag)
            _flag.postValue(newFlag)
            _fullLink.postValue(newFullLink)
        }
    }

    fun getFullLinkFromDataBase() {
        viewModelScope.launch(Dispatchers.IO)  {
            try {
                _fullLink.postValue(dataBaseDao.getFullLinkFromDataBase())
            } catch (e: Exception) {
                _fullLink.postValue("null")
                Log.e("DataBaseError", e.toString())
            }
        }
    }

    fun getFlagFromDataBase() {
        viewModelScope.launch(Dispatchers.IO)  {
            try {
                _flag.postValue(dataBaseDao.getFlagFromDataBase())
            } catch (e: Exception) {
                _flag.postValue("null")
                Log.e("DataBaseError", e.toString())
            }
        }
    }
}