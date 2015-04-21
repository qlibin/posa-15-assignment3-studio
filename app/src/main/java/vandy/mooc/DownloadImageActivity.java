package vandy.mooc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends GenericImageActivity {

    public static final String ACTION_DOWNLOAD_IMAGE = "vandy.mooc.action.DOWNLOAD_IMAGE";

    @Override
    protected Uri doInBackgroundHook(Context context, Uri uri) {
        return Utils.downloadImage(DownloadImageActivity.this, uri);
    }

}
