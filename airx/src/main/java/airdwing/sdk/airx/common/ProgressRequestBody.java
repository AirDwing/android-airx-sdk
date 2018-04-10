package airdwing.sdk.airx.common;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    private File mFile;
    private MediaType mContentType;
    private OnProgressUpdateListener progressListener;

    public ProgressRequestBody(File file, MediaType contentType, OnProgressUpdateListener progressListener) {
        super();
        this.mFile = file;
        this.mContentType = contentType;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mContentType;
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    /**
     * when in log mode, will be called twice, the first call is for logging,
     * and the second call is the real uploading
     * @param sink
     * @throws IOException
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        final long fileLength = contentLength();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                uploaded += read;
                sink.write(buffer, 0, read);

                final long finalUploaded = uploaded;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressListener.onProgressUpdate((float) finalUploaded / fileLength, finalUploaded == fileLength);
                    }
                });
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}