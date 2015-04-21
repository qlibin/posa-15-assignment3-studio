package vandy.mooc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author alibin@vidimax.ru
 *         4/21/15.
 */
public class MainActivity extends LifecycleLoggingActivity {

    private EditText mEditText;

    private Uri mDefaultUrl =
            Uri.parse("http://www.dre.vanderbilt.edu/~schmidt/robot.png");

    private static final int DOWNLOAD_IMAGE_REQUEST = 1;
    private static final int FILTER_IMAGE_REQUEST = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mEditText = (EditText) findViewById(R.id.url);

        final Button loadButton = (Button) findViewById(R.id.button1);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });
    }

    public void downloadImage() {
        try {
            // Hide the keyboard.
            hideKeyboard(this, mEditText.getWindowToken());

            Uri uri = getUrl();

            if (uri != null) {
                // Call the makeDownloadImageIntent() factory method to
                // create a new Intent to an Activity that can download an
                // image from the URL given by the user.  In this case
                // it's an Intent that's implemented by the
                // DownloadImageActivity.
                Intent downloadImageIntent = makeDownloadImageIntent(uri);

                // Start the Activity associated with the Intent, which
                // will download the image and then return the Uri for the
                // downloaded image file via the onActivityResult() hook
                // method.
                startActivityForResult(downloadImageIntent, DOWNLOAD_IMAGE_REQUEST);
            } else {
                Utils.showToast(this, "Invalid URL");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterImage(Uri uri) {
        try {
            if (uri != null) {
                Intent filterImageIntent = makeFilterImageIntent(uri);
                startActivityForResult(filterImageIntent, FILTER_IMAGE_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showImage(Uri uri) {
        try {
            if (uri != null) {
                Intent intent = makeGalleryIntent(uri.toString());

                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        // Check if the started Activity completed successfully.
        if (requestCode == DOWNLOAD_IMAGE_REQUEST) {
            // Check if the request code is what we're expecting.
            if (resultCode == RESULT_OK) {
                Uri imageFileUri = data.getData();
                filterImage(imageFileUri);
            }
            // Check if the started Activity did not complete successfully
            // and inform the user a problem occurred when trying to
            // download contents at the given URL.
            else if (resultCode == RESULT_CANCELED) {
                Utils.showToast(this,
                        "Couldn't download an image");
            } else {
                Utils.showToast(this,
                        "Unknown error");
            }
        } else
        if (requestCode == FILTER_IMAGE_REQUEST) {
            // Check if the request code is what we're expecting.
            if (resultCode == RESULT_OK) {
                // Call the makeGalleryIntent() factory method to
                // create an Intent that will launch the "Gallery" app
                // by passing in the path to the downloaded image
                // file.
                Uri imageFileUri = data.getData();
                showImage(imageFileUri);
            }
            // Check if the started Activity did not complete successfully
            // and inform the user a problem occurred when trying to
            // download contents at the given URL.
            else if (resultCode == RESULT_CANCELED) {
                Utils.showToast(this,
                        "Couldn't filter an image");
            } else {
                Utils.showToast(this,
                        "Unknown error");
            }
        }
        else {
            Utils.showToast(this,
                    "Unexpected error");
        }
    }

    private Intent makeDownloadImageIntent(Uri url) {
        // Create an intent that will download the image from the web.
        return new Intent(DownloadImageActivity.ACTION_DOWNLOAD_IMAGE, url);
    }

    private Intent makeFilterImageIntent(Uri url) {
        // Create an intent that will download the image from the web.
        Intent intent = new Intent(FilterImageActivity.ACTION_FILTER_IMAGE);
        intent.setDataAndType(url, "image/*");
        return intent;
    }

    private Intent makeGalleryIntent(String pathToImageFile) {
        // Create an intent that will start the Gallery app to view
        // the image.
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_VIEW);
        galleryIntent.setDataAndType(Uri.parse("file://" + pathToImageFile), "image/*");
        return galleryIntent;
    }

    protected Uri getUrl() {
        Uri url = Uri.parse(mEditText.getText().toString());

        String uri = url.toString();
        if (uri == null || uri.equals("")) {
            url = mDefaultUrl;
        }

        return URLUtil.isValidUrl(url.toString()) ? url : null;
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public void hideKeyboard(Activity activity,
                             IBinder windowToken) {
        InputMethodManager mgr =
                (InputMethodManager) activity.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                0);
    }
}
