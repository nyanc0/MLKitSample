## このアプリについて
本アプリはFirebaseが提供する機械学習SDK [MLKit](https://firebase.google.com/products/ml-kit/?hl=ja) を利用したサンプルアプリです。  
アプリを実行する場合はapp配下にgoogle-service.jsonを配置する必要があります。  
google-service.jsonはFirebaseプロジェクトからダウンロードしてください。  
また、クラウドモードで利用する場合はFirebaseプロジェクトをBrazeにする必要があります。

<img border="0" src="https://github.com/nyanc0/LT/blob/master/mlkit/images/mlkit_1.gif?raw=true" height="600">

## 利用環境

- Android Studio: 3.3
- kotlin: 1.3.11
- ライブラリ
```gradle
// LiveData,ViewModel
annotationProcessor "androidx.lifecycle:lifecycle-compiler:2.0.0"
implementation "androidx.lifecycle:lifecycle-runtime:2.0.0"
implementation "androidx.lifecycle:lifecycle-extensions:2.0.0"

// UI
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
implementation 'com.google.android.material:material:1.0.0'

// kotlin
implementation 'androidx.core:core-ktx:1.0.1'
implementation 'com.github.bumptech.glide:glide:4.8.0'

// kotlin coroutine
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1"
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1"

// firebase
implementation 'com.google.firebase:firebase-core:16.0.7'
implementation 'com.google.firebase:firebase-ml-vision:19.0.2'
implementation 'com.google.firebase:firebase-ml-vision-image-label-model:17.0.2'

// Image Crop
implementation 'com.isseiaoki:simplecropview:1.1.8'

```

## パッケージ構成
サンプルのため割と適当にきっています。  

```
 └ model // 内部で利用するデータモデル
 └ permission // パーミッション取得に必要な処理をまとめたUtil
 └ presentation
  └ bottomsheet // ボトムシートに必要なView関連クラス
  └ common // UI処理に必要なUtilクラス
  └ crop // 画像トリミング画面のViewクラス
 └ repository // Firebaseからデータ取得を行うためのレポジトリ

```

## データフロー

<img border="0" src="MLKitSample_MainActivity.png" height="300">
