package example.com.nasapictureproject.Utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import example.com.nasapictureproject.R
import java.io.File

class ImageDownloadTask(val context: Context, val url: String, val activity: Activity) : AsyncTask<Void, Void, String>() {
    private var TAG="TAG"
    override fun doInBackground(vararg params: Void?): String? {
        start(url)
        return null
    }

    override fun onPreExecute() {
        Toast.makeText(context, context.getString(R.string.downloadingImage), Toast.LENGTH_SHORT).show()
        super.onPreExecute()
    }

    override fun onPostExecute(result: String?) {
            Toast.makeText(context, context.getString(R.string.downloadedImage), Toast.LENGTH_SHORT).show()
        super.onPostExecute(result)
        // ...
    }

    private fun start(url: String){
        Log.d(TAG, "start:")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermissions()
        } else {
            downloadImage(url)
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            ) {
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
            downloadImage(url)
        }
    }


    private var msg: String? = ""
    private var lastMsg = ""

    private fun downloadImage(url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

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
                    activity.runOnUiThread {
                  //      filePath = msg.toString()

                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }
    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        var x : String = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> context.getString(R.string.downloadFail)
            DownloadManager.STATUS_PAUSED -> context.getString(R.string.downloadPause)
            DownloadManager.STATUS_PENDING -> context.getString(R.string.downloadPending)
            DownloadManager.STATUS_RUNNING -> context.getString(R.string.downloadRunning)
            DownloadManager.STATUS_SUCCESSFUL -> directory.toString() + File.separator + url.substring(
                    url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }
}
