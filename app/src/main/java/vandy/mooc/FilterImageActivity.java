package vandy.mooc;

import android.content.Context;
import android.net.Uri;

/**
 * @author alibin@vidimax.ru
 *         4/21/15.
 */
public class FilterImageActivity extends GenericImageActivity {

    public static final String ACTION_FILTER_IMAGE = "vandy.mooc.action.FILTER_IMAGE";

    @Override
    protected Uri doInBackgroundHook(Context context, Uri uri) {

        return Utils.grayScaleFilter(this, uri);

    }
}
