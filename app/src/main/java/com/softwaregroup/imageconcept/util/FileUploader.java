package com.softwaregroup.imageconcept.util;

import android.os.Handler;
import android.os.Looper;

import com.softwaregroup.imageconcept.model.lmageResponse.ImageOutputItem;
import com.softwaregroup.imageconcept.network.API;
import com.softwaregroup.imageconcept.network.RetrofitInstance;
import com.softwaregroup.imageconcept.util.FileUploaderCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * file upload to server whole functionality
 */
public class FileUploader {

    public FileUploaderCallback fileUploaderCallback;
    private File[] files;
    public int uploadIndex = -1;
    private String uploadURL = "";
    private long totalFileLength = 0;
    private long totalFileUploaded = 0;
    private String filekey="";
    private API uploadInterface;
    private String auth_token = "";
    private String[] responses;



    /**
     * progress bar initiate during api call
     */
    public class PRRequestBody extends RequestBody {
        private File mFile;

        private static final int DEFAULT_BUFFER_SIZE = 2048;

        public PRRequestBody(final File file) {
            mFile = file;

        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            return MediaType.parse("image/*");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }

    /**
     * interface call of api functionality
     */
    public FileUploader(){
        uploadInterface = RetrofitInstance.Companion.getImageApi();
    }

    /**
     * upload functionality from activity
     * @param url
     * @param filekey
     * @param files
     * @param fileUploaderCallback
     */
    public void uploadFiles(String url,String filekey,File[] files, FileUploaderCallback fileUploaderCallback){
        uploadFiles(url,filekey,files,fileUploaderCallback,"");
    }

    public void uploadFiles(String url,String filekey,File[] files, FileUploaderCallback fileUploaderCallback,String auth_token){
        this.fileUploaderCallback = fileUploaderCallback;
        this.files = files;
        this.uploadIndex = -1;
        this.uploadURL = url;
        this.filekey = filekey;
        this.auth_token = auth_token;
        totalFileUploaded = 0;
        totalFileLength = 0;
        uploadIndex = -1;
        responses = new String[files.length];
        for(int i=0; i<files.length; i++){
            totalFileLength = totalFileLength + files[i].length();
        }
        uploadNext();
    }

    /**
     * Multiple files check functionality
     */
    private void uploadNext(){
        if(files.length>0){
            if(uploadIndex!= -1)
                totalFileUploaded = totalFileUploaded + files[uploadIndex].length();
            uploadIndex++;
            if(uploadIndex < files.length){
                uploadSingleFile(uploadIndex);
            }else{
                fileUploaderCallback.onFinish(responses);
            }
        }else{
            fileUploaderCallback.onFinish(responses);
        }
    }

    /**
     * Each file send to server with retrofit api call
     * @param index
     */
    private void uploadSingleFile(final int index){
        PRRequestBody fileBody = new PRRequestBody(files[index]);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(filekey, files[index].getName(), fileBody);
        Call<ImageOutputItem> call;
        if(auth_token.isEmpty()){

            call  = uploadInterface.uploadFile(uploadURL, filePart);
        }else{
            call  = uploadInterface.uploadFile(uploadURL, filePart, auth_token);
        }

        call.enqueue(new Callback<ImageOutputItem>() {
            @Override
            public void onResponse(Call<ImageOutputItem> call, retrofit2.Response<ImageOutputItem> response) {
                if (response.isSuccessful()) {
                    ImageOutputItem jsonElement = response.body();
                    responses[index] = jsonElement.toString();
                }else{
                    responses[index] = "";
                }
                uploadNext();
            }

            @Override
            public void onFailure(Call<ImageOutputItem> call, Throwable t) {
                fileUploaderCallback.onError();
            }
        });


    }

    /**
     * Progress percentage update while sending to server
     */
    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int)(100 * mUploaded / mTotal);
            int total_percent = (int)(100 * (totalFileUploaded+mUploaded) / totalFileLength);
            fileUploaderCallback.onProgressUpdate(current_percent, total_percent,uploadIndex+1 );
        }
    }
}
