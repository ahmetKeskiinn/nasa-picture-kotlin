package example.com.nasapictureproject.Adapters

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import example.com.nasapictureproject.Models.Photo
import example.com.nasapictureproject.R
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class CuriosityRecyclerAdapter(val context: Context, val activity: Activity) :View.OnClickListener, ListAdapter<Photo, CuriosityRecyclerAdapter.CuriosityHolder>(
        diffCallback
) {
    private lateinit var customLayout:View
    private val TAG = "CURIOSITYADAPTER"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuriosityHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.curiosity_item, parent,
                false
        )
        return CuriosityHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CuriosityHolder, position: Int) {
      //  val media = /*getItem(position).img_src*/ "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-"
        val media = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg"
        Log.d(TAG, "onBindViewHolder: " + getItem(position).img_src)
        if (media !== null) {
            Glide.with(holder.avatar)
                    .load(getItem(position).img_src)
                    .into(holder.avatar)
        }       //  Picasso.get().load(getItem(position).img_src.toString()).into(holder.avatar)
        holder.download.isClickable=true
        holder.download.setOnClickListener(this)
        holder.download.setOnClickListener {
            askDownload(position)
        }
        holder.avatar.isClickable = true
        holder.avatar.setOnClickListener {
            showDetails(position)
        }
    }
    private fun askDownload(position: Int): View.OnClickListener? {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.downloadTitle)
        builder.setMessage(R.string.wantToDownload)
        builder.setPositiveButton(R.string.yes) { dialog, which ->
            Toast.makeText(
                    context,
                    R.string.mediaDownloading, Toast.LENGTH_SHORT
            ).show()
            askPermissions(getItem(position).img_src)
            dialog.cancel()
        }
        builder.setNegativeButton(R.string.no) { dialog, which ->

            dialog.cancel()

        }
        builder.show()
        return null
    }


    @SuppressLint("ResourceType")
    private fun showDetails(position: Int){
        val item = getItem(position)
        Log.d(TAG, "showDetails: " + item.img_src)
        Log.d(TAG, "showDetails: " + item.camera)
        Log.d(TAG, "showDetails: " + item.earth_date)
        Log.d(TAG, "showDetails: " + item.id)
        Log.d(TAG, "showDetails: " + item.rover)
        var builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val v: View = activity.layoutInflater.inflate(R.layout.detail_dialog_content, null)
        val detailImage:ImageView = v.findViewById(R.id.dialogContentImage)
        val cameraFullName:TextView = v.findViewById(R.id.CamerafullNameTw)
        val camerName:TextView = v.findViewById(R.id.cameraNameTw)
        val cameraID:TextView = v.findViewById(R.id.cameraIDTw)
        val earthDate:TextView = v.findViewById(R.id.earthDateTw)
        val ID:TextView = v.findViewById(R.id.idTw)
        val roverID:TextView = v.findViewById(R.id.roverIDTw)
        val roverLanding:TextView = v.findViewById(R.id.roverLandDateTw)
        val roverLaunch:TextView = v.findViewById(R.id.roverLaunchDateTw)
        val roverName:TextView = v.findViewById(R.id.roverNameTw)
        val roverStatus:TextView = v.findViewById(R.id.roverStatusTw)
        Glide.with(v).load(getItem(position).img_src).into(detailImage)
        cameraFullName.setText(getItem(position).camera.full_name)
        camerName.setText(getItem(position).camera.name)
        cameraID.setText(getItem(position).camera.id.toString())
        earthDate.setText(getItem(position).earth_date)
        ID.setText(getItem(position).id.toString())
        roverID.setText(getItem(position).rover.id.toString())
        roverLanding.setText(getItem(position).rover.landing_date)
        roverLaunch.setText(getItem(position).rover.launch_date)
        roverName.setText(getItem(position).rover.name)
        roverStatus.setText(getItem(position).rover.status)
        builder.setView(v)

        builder.setPositiveButton(R.string.exit) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    fun askPermissions(imgURL: String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )) {
                AlertDialog.Builder(context)
                        .setTitle("Permission required")
                        .setMessage("Permission required to save photos from the Web.")
                        .setPositiveButton("Allow") { dialog, id ->
                            ActivityCompat.requestPermissions(
                                    activity,
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                            )
                            dialog.cancel()

                        }
                        .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                        .show()
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )

            }
        } else {
            DownloadImageFromPath(imgURL)
        }
    }


    fun DownloadImageFromPath(path: String?) {
        var `in`: InputStream? = null
        var bmp: Bitmap? = null
        //val iv = findViewById(R.id.img1) as ImageView
        var responseCode = -1
        try {
            val url = URL(path) //"http://192.xx.xx.xx/mypath/img1.jpg
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.setDoInput(true)
            con.connect()
            responseCode = con.getResponseCode()
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //download
                `in` = con.getInputStream()
                bmp = BitmapFactory.decodeStream(`in`)
                `in`.close()
               // iv.setImageBitmap(bmp)
            }
        } catch (ex: Exception) {
            Log.e("Exception", ex.toString())
        }
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                    url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

    fun getDeveloperAt(position: Int) = getItem(position)


    inner class CuriosityHolder(iv: View) : RecyclerView.ViewHolder(iv) {

        val avatar : ImageView = itemView.findViewById(R.id.curisityItemImage)
        val download: ImageView = itemView.findViewById(R.id.downloadButton)

    }

    override fun onClick(v: View?) {
      //  askDownload(get)
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
            oldItem: Photo,
            newItem: Photo
    ): Boolean {
        TODO("Not yet implemented")
    }
}