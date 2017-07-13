package com.pooja.carepack.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.pooja.carepack.fragments.BaseFragment;
import com.pooja.carepack.utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PostFileAsync extends AsyncTask<String, Void, Object> {
    private final HashMap<String, String> fileparam;
    private final Object obj;
    private final int requestCode;
    private HashMap<String, String> params;
    private ProgressDialog progress;
    private Context context;
    private PostFileListener listener;

    public PostFileAsync(Context context, Object obj, HashMap<String, String> params, HashMap<String, String> fileparam, int requestCode, PostFileListener listener) {
        this.context = context;
        this.obj = obj;
        this.params = params;
        this.requestCode = requestCode;
        this.listener = listener;
        this.fileparam = fileparam;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait..");
        progress.setCancelable(false);
        progress.show();
    }


    @Override
    protected Object doInBackground(String... param) {
        try {
            BaseFragment.Log.d("execute >> " + param[0]);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(param[0]);
            MultipartEntity reqEntity = new MultipartEntity();
            FileBody bin1 = null;
            if (fileparam != null) {
                BaseFragment.Log.d("images >>> " + fileparam.size());
                for (String key : fileparam.keySet()) {
                    BaseFragment.Log.d(key + " : " + fileparam.get(key));
                    bin1 = new FileBody(new File(fileparam.get(key)));
                    reqEntity.addPart(key, bin1);
                }
            }

            for (String key : params.keySet()) {
                BaseFragment.Log.d(" param >>> " + key + "->" + params.get(key));
                reqEntity.addPart(key, new StringBody(params.get(key), ContentType.TEXT_PLAIN));
            }

            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            String strRes = EntityUtils.toString(response.getEntity());

            if (strRes != null) {
                BaseFragment.Log.d("response : " + strRes);
                Gson gson = new Gson();
                try {
                    return (Object) gson.fromJson(strRes, obj.getClass());
                } catch (Exception e) {
                    Utility.e(e.getMessage());
                }
                return strRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (progress != null)
            progress.cancel();
        if (result != null && listener != null)
            listener.onPostFileSuccess(result, requestCode);
        else if (listener != null)
            listener.onPostFileError(requestCode);

    }

    public interface PostFileListener {
        public void onPostFileSuccess(Object clsGson, int requestCode);

        public void onPostFileError(int requestCode);
    }

}