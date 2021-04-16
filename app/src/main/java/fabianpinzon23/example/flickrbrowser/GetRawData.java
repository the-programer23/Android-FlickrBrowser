package fabianpinzon23.example.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private final OnDownloadComplete mCallback;

    private DownloadStatus mDownloadStatus;

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callback) {
        this.mCallback = callback;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    void runInSameThread (String s){
        Log.d(TAG, "runInSameThread: start");
        //onPostExecute(doInBackground(s));
        if(mCallback != null){
            mCallback.onDownloadComplete(doInBackground(s), mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
        //Log.d(TAG, "onPostExecute: parameter is = " + s);
        if(this.mCallback != null){
            this.mCallback.onDownloadComplete(s, this.mDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }
        
        try{
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while (null != (line = reader.readLine())){
                result.append(line).append("\n");
            }
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();
        }catch (MalformedURLException e){
            //log.e appear after deployment but log.d do not.
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "doInBackground: IO exception reading data" + e.getMessage());
        }catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security exception. Needs Permission?" + e.getMessage());
        }finally {
            Log.e(TAG, "doInBackground: finally block executed");
            if (connection != null){
                connection.disconnect();
            }
            if (reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage());
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    
}
