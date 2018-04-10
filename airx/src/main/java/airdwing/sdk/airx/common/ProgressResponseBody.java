package airdwing.sdk.airx.common;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by caojin on 2017/3/9.
 */

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody rawResponseBody;
    private OnProgressUpdateListener progressUpdateListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, OnProgressUpdateListener listener){
        this.rawResponseBody = responseBody;
        this.progressUpdateListener = listener;
    }

    @Override
    public MediaType contentType() {
        return rawResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return rawResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(rawResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if(progressUpdateListener != null){
                    progressUpdateListener.onProgressUpdate((float) totalBytesRead / rawResponseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }
}
