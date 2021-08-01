package example.com.nasapictureproject.Adapters

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.Image
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import example.com.nasapictureproject.Models.Photo
import example.com.nasapictureproject.R
import java.io.File
import kotlin.math.log

class CuriosityRecyclerAdapter(val context:Context, val activity: Activity) :View.OnClickListener, ListAdapter<Photo, CuriosityRecyclerAdapter.CuriosityHolder>(
        diffCallback
) {
    private val TAG = "CURIOSITYADAPTER"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuriosityHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.curiosity_item, parent,
                false)
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
    }
    private fun askDownload(position: Int): View.OnClickListener? {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.downloadTitle)
        builder.setMessage("We have a message")
        builder.setPositiveButton(R.string.yes) { dialog, which ->
            Toast.makeText(context,
                    R.string.mediaDownloading, Toast.LENGTH_SHORT).show()
            dialog.cancel()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->

            dialog.cancel()

        }
        builder.show()
        return null
    }

    fun askPermissions(imgURL:String) {
        if (ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
            downloadImage(imgURL)
        }
    }


    private var msg: String? = ""
    private var lastMsg = ""

    private fun downloadImage(url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(url.substring(url.lastIndexOf("/") + 1))
                    .setDescription("")
                    .setDestinationInExternalPublicDir(
                            directory.toString(),
                            url.substring(url.lastIndexOf("/") + 1)
                    )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {

                    /*context.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }*/
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
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