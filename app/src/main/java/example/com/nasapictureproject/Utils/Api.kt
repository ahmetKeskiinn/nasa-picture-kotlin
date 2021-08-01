package example.com.nasapictureproject.Utils

import com.google.gson.GsonBuilder
import example.com.nasapictureproject.Models.AllPhotos
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("mars-photos/api/v1/rovers/{name}/photos?sol=1000&api_key=TukSIbms13lxHiJLgzzXxbLjulL6AIzTCFVnzPFo")
    suspend fun getPhotos(@Path("name") name : String,
                          @Query("per_page")per_page:Int?,
                          @Query("page") page: Int):

            Response<AllPhotos>


    @GET("mars-photos/api/v1/rovers/{name}/photos?sol=1000&api_key=TukSIbms13lxHiJLgzzXxbLjulL6AIzTCFVnzPFo")
    suspend fun getFilterdFotos(@Path("name") name : String,
                                @Query("per_page")per_page:Int?,
                                @Query("page") page: Int, @Query("camera") camera:String):

            Response<AllPhotos>



    companion object {

        var retrofitService: Api? = null

        fun getInstance() : Api {
            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                        .baseUrl("https://api.nasa.gov/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                retrofitService = retrofit.create(Api::class.java)
            }
            return retrofitService!!
        }
    }



}