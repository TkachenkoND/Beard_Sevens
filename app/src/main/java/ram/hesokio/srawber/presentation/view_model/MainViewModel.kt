package ram.hesokio.srawber.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ram.hesokio.srawber.data.database.dao.DataDao

class MainViewModel(
    private val dataBaseDao: DataDao
) : ViewModel() {

    private val _fullLink = MutableLiveData<String>()
    val fullLink: LiveData<String> = _fullLink

    private val _isLoadingInDb = MutableLiveData<Boolean>()
    val isLoadingInDb: LiveData<Boolean> = _isLoadingInDb

    fun saveFullLinkInDataBase(newFullLink: String) {
        GlobalScope.launch {
            dataBaseDao.saveFullLinkInDataBase(newFullLink)
            _fullLink.postValue(newFullLink)
        }
    }

    fun getFullLinkFromDataBase() {
        viewModelScope.launch {
            try {
                _fullLink.postValue(dataBaseDao.getFullLinkFromDataBase())
                _isLoadingInDb.value = true
            } catch (e: Exception) {
                _fullLink.postValue("null")
                _isLoadingInDb.value = false
                Log.e("DataBaseError", e.toString())
            }
        }
    }
}