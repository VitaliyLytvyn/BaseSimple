package com.me.basesimple.data


import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import android.app.DownloadManager
import android.net.Uri
import android.content.Context.DOWNLOAD_SERVICE
import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.me.basesimple.domain.FileDownLoader
import timber.log.Timber
import timber.log.Timber.e
import java.util.*


@Singleton
class FileDownLoaderImplLD
@Inject constructor(val context: Context) : FileDownLoader, LiveData<String>() {
    var downloadID: Long? = 0
     internal class Lob(val context: Context):LiveData<String>(){


//        override fun onActive() {
//            super.onActive()
//            Timber.d("FileDownLoaderImplLD onActive()")
//            context.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
//
//        }
//
//        override fun onInactive() {
//            super.onInactive()
//            Timber.d("FileDownLoaderImplLD onInactive()")
//            context.unregisterReceiver(onDownloadComplete)
//        }
    }

    companion object {
        const val TEST_SOURCE_DOWNLOAD_FROM = "http://speedtest.ftp.otenet.gr/files/test10Mb.db"
        const val TITLE = "File Loading"
        const val DESCRIPTION = "Downloading"
    }


    var file: File? = null

    override fun onActive() {
        super.onActive()
        Timber.d("FileDownLoaderImplLD onActive()")
        context.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    override fun onInactive() {
        super.onInactive()
        Timber.d("FileDownLoaderImplLD onInactive()")
        context.unregisterReceiver(onDownloadComplete)
    }

    override suspend fun downloadFrom(path: String, to: String): LiveData<String> {

        context.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        file = File(context.getExternalFilesDir(null), to)
        /*
       Create a DownloadManager.Request with all the information necessary to start the download
        */


        val request = DownloadManager.Request(Uri.parse(TEST_SOURCE_DOWNLOAD_FROM))
            .setTitle(TITLE)// Title of the Download Notification
            .setDescription(DESCRIPTION)// Description of the Download Notification
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)// Visibility of the download Notification
            .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
            .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
            //.setVisibleInDownloadsUi(true)


        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
        downloadID = downloadManager!!.enqueue(request)// enqueue puts the download request in the queue.

        //downloadManager.remove()
        // downloadManager.


        //return this

        return MutableLiveData<String>().apply { value = "not really file returned" }
    }


    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {

                if (file != null && file!!.exists() && file!!.isFile()) {


                    // Get the last modification information.
                    val lastModified = file!!.lastModified()

                    // Create a new date object and pass last modified
                    // information to the date object.
                    val date = Date(lastModified)
                    e("getFileSizeMegaBytes: ${file!!.length() / (1024 * 1024)} mb  lastModified: $lastModified date: $date")

                    file!!.setLastModified(System.currentTimeMillis())// todo check

                    this@FileDownLoaderImplLD.setValue(file!!.absolutePath) //= file!!.absolutePath
                }

                e("onDownloadComplete started id:$downloadID actual id: $id")
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show()

                context.unregisterReceiver(this)// todo check
            }
        }
    }

}


