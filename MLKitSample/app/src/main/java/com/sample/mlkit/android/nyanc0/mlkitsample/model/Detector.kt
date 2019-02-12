package com.sample.mlkit.android.nyanc0.mlkitsample.model

enum class Detector(val title: String) {
    TEXT_DETECTION("テキスト認証"),
    CLOUD_TEXT_DETECTION("クラウドテキスト認証"),
    FACE_DETECTION("顔認証"),
    BARCODE_DETECTION("バーコード認証"),
    LABELING("ラベリング"),
    CLOUD_LABELING("クラウドラベリング"),
    CLOUD_LANDMARK("ランドマーク認証");

    companion object {
        fun createTitleList(): List<String> {
            val titleList = mutableListOf<String>()
            for (detector in Detector.values()) {
                titleList.add(detector.title)
            }
            return titleList
        }
    }
}