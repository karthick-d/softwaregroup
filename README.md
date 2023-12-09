# softwaregroup

# Upload and View Image
Upload and View Image is an Android Application to upload capture photos using CameraX or choose image from gallery and upload to Firebase.
Include Navigation  & View Binding [In Progress]

<br>Upload and View Image App can:

1. Capture image using Camera with Flash, Zoom and Camera Switch functions
2. Pick images from gallery to upload
3. Expand image functionality
4. Image Upload with Retrofit Library & progessbar show
   
## Libraries Used

### MVVM
MVVM stands for Model, View, ViewModel. MVVM facilitates a separation of development of the graphical user interface from development of the business logic or back-end logic (the data model). 

#### Model
Model holds the data of the application. Model represents domain specific data and business logic in MVC architecture. It maintains the data of the application. Model objects retrieve and store model state in the persistance store like a database. Model class holds data in public properties. It cannot directly talk to the View.
#### View
View represents the UI of the application devoid of any Application Logic. It observes the ViewModel.
#### ViewModel
ViewModel acts as a link between the Model and the View. It’s responsible for transforming the data from the Model. It provides data streams to the View. It also uses hooks or callbacks to update the View. It’ll ask for the data from the Model.
The following flow illustrates the core MVVM Pattern.
* CameraX
* JetpackComponents
* Material Design

  AndroidManifest.xml

* Add permissions
```xml
     <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />


## CameraX
CameraX is an Android Jetpack use case based API, it has abstracted 3 main handles which you can use to interact with the camera: Preview, Image analysis and Image capture. Choosing which use case(s) to use depends on what you intend to use the camera for
* Preview : Provides a way to get the camera preview stream.
* Image analysis : Provides a way to process camera data (frames).
* Image capture : Provides a way to capture and save a photo.

We have used `Preview` and `Image capture` use cases in `CameraActivity`. They have been implemented by : 
* Preview Use Caes : Preview.Builder provides options to set either the target aspect ratio or resolution to be used. The rotation can also be configured, which should typically match the device’s orientation. 
The Preview use case needs a Surface to display the incoming preview frames it receives from the camera. You can provide a Surface by calling `Preview.setSurfaceProvider(SurfaceProvider)`, the SurfaceProvider passes the preview Surface to be used by the camera.
* Image Capture Use Case : ImageCapture.takePicture(executor, OnImageCapturedCallback), if the image is successfully captured, onCaptureSuccess() is called with an ImageProxy that wraps the capture image. If the image capture fails, onError() is invoked with the type of the error. Both callbacks are run in the passed in Executor, if they need to run on the main thread, pass in the main thread Executor. ImageCapture.Builder allows to set the flash mode to be used when taking a photo, it can be one of the following: FLASH_MODE_ON, FLASH_MODE_OFF or FLASH_MODE_AUTO.
