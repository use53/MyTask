package uz.myapigrid.taskmy.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.myapigrid.taskmy.data.DataBaseProvider
import uz.myapigrid.taskmy.data.IconModel
import uz.myapigrid.taskmy.Status
import uz.myapigrid.taskmy.R



class BoardVM(app: Application) : AndroidViewModel(app){
    val liveIconsStatus = MutableLiveData<Status>()
    val liveUpdateIconStatus = MutableLiveData<Status>()
    val liveIcons = MutableLiveData<List<IconModel>>()

    private val iconDao = DataBaseProvider.getInstance(app.applicationContext).iconDao()


   fun loadIcons() {
        viewModelScope.launch {
            try {
                liveIconsStatus.postValue(Status.LOADING)
                var list = iconDao.getAll()
                if (list.isNullOrEmpty()) {
                    val mItemArray = arrayListOf<IconModel>()
                    val addItems = 15
                    var id = 1L
                    for (i in 0 until 5) {
                        val columnId: Long = i.toLong()
                        for (j in 0 until addItems) {
                            val rowId: Long = j.toLong()
                            mItemArray.add(
                                IconModel(
                                    id = id,
                                    rowId = rowId,
                                    columnId = columnId,
                                    name = "Item $columnId - $rowId"
                                )
                            )
                            id++
                        }
                    }
                    iconDao.`saveAll(tem`(mItemArray)
                    list = iconDao.getAll()
                    liveIcons.postValue(list)
                } else
                    liveIcons.postValue(list)
                liveIconsStatus.postValue(Status.SUCCESS)
            } catch (e: Exception) {
                liveIconsStatus.postValue(Status.ERROR(R.string.error))
            }
        }
    }

  fun updateIcon(fromColumn: Long, fromRow: Long, toColumn: Long, toRow: Long) {
        viewModelScope.launch {
            try {
                liveUpdateIconStatus.postValue(Status.LOADING)
                val icon = iconDao.getIcon(fromColumn, fromRow)
                iconDao.deleteIcon(fromColumn, fromRow)
                Log.d("TAGROW", "updateIcon: $fromRow $toRow")

                if (toColumn == fromColumn) {
                    if (toRow > fromRow) {
                        (fromRow until toRow).forEach { row ->
                            iconDao.updateIcon(fromColumn, row + 1, toColumn, row)
                        }
                    } else if (toRow < fromRow) {
                        (fromRow - 1 downTo toRow).forEach { row ->
                            iconDao.updateIcon(fromColumn, row, toColumn, row + 1)
                        }
                    }
                } else {
                    (fromRow until 14).forEach { row ->
                        iconDao.updateIcon(fromColumn, row + 1, fromColumn, row)
                    }
                    (14 downTo toRow).forEach { row ->
                        iconDao.updateIcon(toColumn, row, toColumn, row + 1)
                    }
                }

                iconDao.saveItem(IconModel(icon.id, toRow, toColumn, icon.name))
                liveUpdateIconStatus.postValue(Status.SUCCESS)
            } catch (e: Exception) {
                liveUpdateIconStatus.postValue(Status.ERROR(R.string.error))
            }
        }
    }

}