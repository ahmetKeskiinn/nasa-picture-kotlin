package example.com.nasapictureproject.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import example.com.nasapictureproject.Models.AllPhotos
import example.com.nasapictureproject.Models.Photo
import example.com.nasapictureproject.Utils.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class COPRepo {
    private var TAG = "COPRepo"
    private lateinit var api: Api
    private lateinit var listener: repoListener
    fun COPRespoInstance(repoListener: repoListener) {
            this.listener = repoListener
    }

    fun apiTest(deviceName: String, per_page: Int, page: Int) {
        api = Api.getInstance()
        var list: List<Photo?>? = null
        val call: Call<AllPhotos> = api.getPhotos(deviceName, per_page, page)
        lateinit var listGame: LiveData<List<AllPhotos>>
        call.enqueue(object : Callback<AllPhotos> {
            override fun onResponse(call: Call<AllPhotos>, response: Response<AllPhotos>) {
                list = response.body()!!.photos
                Log.d(TAG, "onResponse: " + (list as List<Photo>).size)
                listener.onPostExecute(true, list as List<Photo>)
            }

            override fun onFailure(call: Call<AllPhotos>, t: Throwable) {
                Log.d(TAG, "onFailure: " + t.toString())
                listener.onPostExecute(false, null)
            }

        })
    }
    interface repoListener{
        fun onPreExecute()
        fun onPostExecute(isSuccess:Boolean, data: List<Photo>?)
    }
}
