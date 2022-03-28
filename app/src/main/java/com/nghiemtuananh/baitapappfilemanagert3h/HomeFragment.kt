package com.nghiemtuananh.baitapappfilemanagert3h

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nghiemtuananh.baitapappfilemanagert3h.databinding.FragmentHomeBinding
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityAndHomeFragment
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), View.OnClickListener {
    lateinit var inter: IActivityAndHomeFragment
    lateinit var binding: FragmentHomeBinding
    var listImage: ArrayList<FileData> = arrayListOf()
    var listVideo: ArrayList<FileData> = arrayListOf()
    var listMusic: ArrayList<FileData> = arrayListOf()
    var listCompressed: ArrayList<FileData> = arrayListOf()
    var listApp: ArrayList<FileData> = arrayListOf()
    var listDocument: ArrayList<FileData> = arrayListOf()
    var listDownload: ArrayList<FileData> = arrayListOf()
    var listRecent: ArrayList<FileData> = arrayListOf()
    var listFavorite: ArrayList<FileData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        inter = context as IActivityAndHomeFragment
        rxLoadData()
        setProgressBar()
        inter.reloadToolBar()
        binding.pgbInternalStorage.setOnClickListener(this)
        binding.simApp.setOnClickListener(this)
        binding.simMusic.setOnClickListener(this)
        binding.simVideo.setOnClickListener(this)
        binding.simDocument.setOnClickListener(this)
        binding.simImage.setOnClickListener(this)
        binding.simCompressed.setOnClickListener(this)
        binding.simDownload.setOnClickListener(this)
        binding.simFavorite.setOnClickListener(this)
        binding.simRecent.setOnClickListener(this)
        return binding.root
    }

    @SuppressLint("CheckResult")
    private fun rxLoadData() {
        Observable.create<Void> {
            listImage.clear()
            listVideo.clear()
            listMusic.clear()
            listCompressed.clear()
            listApp.clear()
            listDocument.clear()
            listDownload.clear()
            listRecent.clear()
            listFavorite.clear()
            var rootPath = Environment.getExternalStorageDirectory().path
            var pathDownload =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            getData(File(rootPath))
            getDataDownLoad(pathDownload!!)
            it.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {},
                {
                    setTextCountItem()
                }
            )
    }

    private fun setTextCountItem() {
        binding.btnCountApp.setText(listApp.size.toString())
        binding.btnCountCompressed.setText(listCompressed.size.toString())
        binding.btnCountDocument.setText(listDocument.size.toString())
        binding.btnCountDownload.setText(listDownload.size.toString())
        binding.btnCountImage.setText(listImage.size.toString())
        binding.btnCountFavorite.setText(listFavorite.size.toString())
        binding.btnCountRecent.setText(listRecent.size.toString())
        binding.btnCountVideo.setText(listVideo.size.toString())
        binding.btnCountMusic.setText(listMusic.size.toString())
    }

    private fun getDataDownLoad(pathDownload: File) {
        var listFile: Array<out File> = pathDownload.listFiles() as Array<out File>
        val sortedList = listFile.sortedWith(compareBy(File::lastModified)).reversed()
        var myBitmap: Bitmap?
        var resourceId = 0
        for (f in sortedList) {
            if (f.name.lowercase().endsWith(".docs") || f.name.lowercase()
                    .endsWith(".txt")
            ) {
                myBitmap = null
                resourceId = R.drawable.baseline_insert_drive_file_yellow_500_48dp
            } else if (f.name.lowercase().endsWith(".jpg") || f.name.lowercase()
                    .endsWith(".png") || f.name.lowercase().endsWith(".jpeg")
            ) {
                myBitmap = BitmapFactory.decodeFile(f.absolutePath)
                resourceId = R.drawable.baseline_crop_original_yellow_500_48dp
            } else if (f.name.lowercase().endsWith(".apk") || f.name.lowercase()
                    .endsWith(".xapk")
            ) {
                myBitmap = null
                resourceId = R.drawable.baseline_android_yellow_500_48dp
            } else if (f.name.lowercase().endsWith(".mp3") || f.name.lowercase().endsWith(".wav")) {
                myBitmap = null
                resourceId = R.drawable.baseline_music_note_yellow_500_48dp
            } else if (f.name.lowercase().endsWith(".pdf")) {
                myBitmap = null
                resourceId = R.drawable.baseline_picture_as_pdf_yellow_500_48dp
            } else if (f.name.lowercase().endsWith(".mp4")) {
                myBitmap = null
                resourceId = R.drawable.baseline_ondemand_video_yellow_500_48dp
            } else if (f.name.lowercase().endsWith(".zip") || f.name.lowercase().endsWith(".rar")) {
                myBitmap = null
                resourceId = R.drawable.baseline_folder_zip_yellow_500_48dp
            } else {
                myBitmap = null
                resourceId = R.drawable.baseline_insert_drive_file_yellow_500_48dp
            }
            listDownload.add(FileData(
                f.name,
                Date(f.lastModified()),
                resourceId,
                f.length().toInt(),
                f.path,
                false,
                myBitmap)
            )
        }
    }

    private fun getData(file: File) {
        for (f in file.listFiles()) {
            if (f.isDirectory && !f.isHidden) {
                getData(f)
            } else {
                if (f.name.lowercase().endsWith(".jpg") || f.name.lowercase()
                        .endsWith(".png") || f.name.lowercase().endsWith(".jpeg")
                ) {
                    val myBitmap = BitmapFactory.decodeFile(f.absolutePath)
                    listImage.add(FileData(
                        f.name,
                        Date(f.lastModified()),
                        R.drawable.baseline_crop_original_yellow_500_48dp,
                        f.length().toInt(),
                        f.path,
                        false,
                        myBitmap)
                    )
                }
                if (f.name.lowercase().endsWith(".mp4")) {
                    listVideo.add(FileData(
                        f.name,
                        Date(f.lastModified()),
                        R.drawable.baseline_ondemand_video_yellow_500_48dp,
                        f.length().toInt(),
                        f.path,
                        false,
                        null)
                    )
                }
                if (f.name.lowercase().endsWith(".zip") || f.name.lowercase().endsWith(".rar")) {
                    listCompressed.add(FileData(
                        f.name,
                        Date(f.lastModified()),
                        R.drawable.baseline_folder_zip_yellow_500_48dp,
                        f.length().toInt(),
                        f.path,
                        false,
                        null)
                    )
                }
                if (f.name.lowercase().endsWith(".pdf") || f.name.lowercase()
                        .endsWith(".docs") || f.name.lowercase()
                        .endsWith(".txt")
                ) {
                    listDocument.add(FileData(
                        f.name,
                        Date(f.lastModified()),
                        R.drawable.baseline_insert_drive_file_yellow_500_48dp,
                        f.length().toInt(),
                        f.path,
                        false,
                        null)
                    )
                }
                if (f.name.lowercase().endsWith(".apk") || f.name.lowercase()
                        .endsWith(".xapk")
                ) {
                    listApp.add(FileData(
                        f.name,
                        Date(f.lastModified()),
                        R.drawable.baseline_android_yellow_500_48dp,
                        f.length().toInt(),
                        f.path,
                        false,
                        null)
                    )
                }
                if (f.name.lowercase().endsWith(".mp3") || f.name.lowercase().endsWith(".wav")) {
                    listMusic.add(FileData(
                        f.name,
                        Date(f.lastModified()),
                        R.drawable.baseline_android_yellow_500_48dp,
                        f.length().toInt(),
                        f.path,
                        false,
                        null)
                    )
                }
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.pgb_internal_storage -> {
                inter.openFolder(null, "internal")
            }
            R.id.sim_app -> {
                inter.openFolder(listApp, "app")
            }
            R.id.sim_document -> {
                inter.openFolder(listDocument, "document")
            }
            R.id.sim_compressed -> {
                inter.openFolder(listCompressed, "compressed")
            }
            R.id.sim_video -> {
                inter.openFolder(listVideo, "video")
            }
            R.id.sim_music -> {
                inter.openFolder(listMusic, "music")
            }
            R.id.sim_recent -> {
                inter.openFolder(listRecent, "recent")
            }
            R.id.sim_favorite -> {
                inter.openFolder(listFavorite, "favorite")
            }
            R.id.sim_download -> {
                inter.openFolder(listDownload, "download")
            }
            R.id.sim_image -> {
                inter.openFolder(listImage, "image")
            }
        }
    }

    private fun setProgressBar() {
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)
        ) {
            val fileRootExternal = Environment.getExternalStorageDirectory()
            val statFs = StatFs(fileRootExternal.path)
            val blockSizeCount = statFs.getBlockCountLong()
            val sizeBlocks = statFs.getBlockSizeLong()
            val availableBlocks = statFs.getAvailableBlocksLong()
            val totalExternalMemorySize = formatSize(blockSizeCount * sizeBlocks)
            val usedExternalMemorySize =
                formatSize(blockSizeCount * sizeBlocks - availableBlocks * sizeBlocks)
            binding.tvAllMemory.setText(totalExternalMemorySize)
            binding.tvMemoryUsed.setText(usedExternalMemorySize)
            binding.pgbInternalStorage.setProgress(((blockSizeCount.toDouble() * sizeBlocks.toDouble() - availableBlocks.toDouble() * sizeBlocks.toDouble()) / (blockSizeCount.toDouble() * sizeBlocks.toDouble()) * 100.0).toInt())
        }
    }

    fun formatSize(size: Long): String? {
        var size = size
        var sizeDouble = size.toDouble()
        var suffix: String? = "GB"
        sizeDouble /= (1024 * 1024 * 1024)
        sizeDouble = Math.round(sizeDouble * 100) / 100.0
//        var surplus = size % (1024 * 1024 * 1024)
        val resultBuffer = StringBuilder(java.lang.Double.toString(sizeDouble))
        if (suffix != null) resultBuffer.append(" $suffix")
        return resultBuffer.toString()
    }
}