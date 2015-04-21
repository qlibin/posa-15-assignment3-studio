package vandy.mooc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * @author alibin@vidimax.ru
 *         4/21/15.
 */
public abstract class GenericImageActivity extends LifecycleLoggingActivity {

    private RetainedFragmentManager mRetainedFragmentManager = new RetainedFragmentManager(this, "GenericImageActivity");

    private static String URL = "url";
    private static String IMAGEPATH = "imagePath";
    private static String ASYNCTASK = "asyncTask";

    abstract protected Uri doInBackgroundHook(Context context, Uri uri);

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        if (mRetainedFragmentManager.firstTimeIn()) {
            mRetainedFragmentManager.put(URL, getIntent().getData());
        } else {

            Uri pathToImage = mRetainedFragmentManager.get(IMAGEPATH);

            if (pathToImage != null) {
                setActivityResult(pathToImage);
                finish();
            }
            mRetainedFragmentManager.get(URL);
        }

    }

    private void setActivityResult(Uri result) {
        if (result != null) {
            setResult(RESULT_OK, new Intent("", result));
        } else {
            setResult(RESULT_CANCELED);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        AsyncTask<Uri, Void, Uri> asyncTask = mRetainedFragmentManager.get(ASYNCTASK);

        if (asyncTask == null) {

            asyncTask = new AsyncTask<Uri, Void, Uri>() {
                @Override
                protected Uri doInBackground(Uri... uris) {
                    return doInBackgroundHook(GenericImageActivity.this, uris[0]);
                }
                @Override
                protected void onPostExecute(Uri result) {
                    setActivityResult(result);
                    finish();
                }
            };

            mRetainedFragmentManager.put(ASYNCTASK, asyncTask);

            Uri uri = mRetainedFragmentManager.get(URL);

            asyncTask.execute(uri);

        }
    }

}
