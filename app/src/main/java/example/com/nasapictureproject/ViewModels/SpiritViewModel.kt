package example.com.nasapictureproject.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import example.com.nasapictureproject.Models.Photo
import example.com.nasapictureproject.Repositories.COPRepo

class SpiritViewModel(app: Application) : AndroidViewModel(app), COPRepo.repoListener {
    private var TAG = "SpiritViewModel"
    val list = MutableLiveData<List<Photo?>>()

    fun hook(name: String, per_page: Int, page: Int) {
        var repository: COPRepo = COPRepo()
        repository.COPRespoInstance(this)
        repository.apiTest(name, per_page, page)
    }

    override fun onPreExecute() {
        TODO("Not yet implemented")
    }

    override fun onPostExecute(isSuccess: Boolean, data: List<Photo>?) {
        if (isSuccess) {
            list.postValue(data!!)

        } else {
        }

    }
}