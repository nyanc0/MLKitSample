package com.sample.mlkit.android.yuriyuri.sampleapp.presentation


//class CameraActivity : DaggerAppCompatActivity(), View.OnClickListener {
//
//    private val permissionUtil: PermissionUtil = PermissionUtil(this)
//    private var handler: Handler = Handler()
//
//    private val binding: ActivityCameraBinding by lazy {
//        DataBindingUtil.setContentView<ActivityCameraBinding>(this, R.layout.fragment_camera)
//    }
//
//    private val cameraListener = object : CameraListener() {
//        override fun onPictureTaken(jpeg: ByteArray?) {
//            super.onPictureTaken(jpeg)
//
//            getBackgroundHandler().post {
//                val timeStamp = createTimeStamp(Date().time)
//                val saveFileName = createPreCroppedFileName(timeStamp)
//                val saveFile = File(externalCacheDir, saveFileName)
//
//                val fileOutputStream = FileOutputStream(saveFile)
//                fileOutputStream.write(jpeg)
//                fileOutputStream.close()
//
//                val uri = Uri.fromFile(saveFile)
//                val intent = Intent()
//                intent.putExtra(KEY_INTENT, uri)
//                setResult(Activity.RESULT_OK, intent)
//                finish()
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding.camera.setLifecycleOwner(this)
//        binding.camera.addCameraListener(cameraListener)
//        binding.takePicture.setOnClickListener(this)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        when {
//            permissionUtil.isAuthorized(Permission.P_CAMERA) -> binding.camera.start()
//            permissionUtil.showRationale(Permission.P_CAMERA) -> {
//                // TODO:show toast or dialog
//            }
//            else -> permissionUtil.requestPermission(Permission.P_CAMERA, REQUEST_CAMERA_PERMISSION)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        binding.camera.stop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        binding.camera.destroy()
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_CAMERA_PERMISSION -> {
//                if (!permissionUtil.verifyGrantResults(grantResults)) {
//                    Toast.makeText(this, R.string.message_no_camera_permission, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.take_picture -> binding.camera.capturePicture()
//        }
//    }
//
//    private fun isLoading(): Boolean {
//        return handler.looper.thread.isAlive
//    }
//
//    private fun getBackgroundHandler(): Handler {
//        val thread = HandlerThread("transformByteArray")
//        thread.start()
//        handler = Handler(thread.looper)
//        return handler
//    }
//
//    companion object {
//
//        const val REQUEST_CD = 200
//        const val KEY_INTENT = "key_image"
//        private const val REQUEST_CAMERA_PERMISSION = 1
//
//        fun startForResult(activity: Activity) {
//            val intent = Intent(activity, CameraActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//            activity.startActivityForResult(intent, REQUEST_CD)
//        }
//    }
//}