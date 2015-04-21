package vandy.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import java.util.HashMap;

/**
 * @author alibin@vidimax.ru
 *         4/21/15.
 */
public class RetainedFragmentManager {

    private final FragmentManager mFragmentManager;

    private final String mRetainedFragmentTag;

    private RetainedFragment mRetainedFragment;

    public RetainedFragmentManager(Activity activity, String retainedFragmentTag) {

        mFragmentManager = activity.getFragmentManager();

        this.mRetainedFragmentTag = retainedFragmentTag;

    }

    public boolean firstTimeIn() {

        mRetainedFragment = (RetainedFragment) mFragmentManager.findFragmentByTag(mRetainedFragmentTag);

        if (mRetainedFragment != null) {
            return false;
        } else {
            mRetainedFragment = new RetainedFragment();
            mFragmentManager.beginTransaction().add(mRetainedFragment, mRetainedFragmentTag)
                    .commit();
            return true;
        }

    }

    public void put(String key, Object o) {
        mRetainedFragment.put(key, o);
    }

    public <T> T get(String key) {
        return (T) mRetainedFragment.get(key);
    }

    public static class RetainedFragment extends Fragment {

        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void put(String key, Object o) {
            mData.put(key, o);
        }

        public <T> T get(String key) {
            return (T) mData.get(key);
        }

    }

}
