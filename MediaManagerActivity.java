
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;

/**
 * Desc: Custom gallery. Only includes images for now.
 *
 * @author zekigu
 */
public class MediaManagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private View mToggleButtonLayout;
    private View mTitlebarLayout;
    private TextView mPageTitle;
    private View mCancelTextViewLayout;
    private RadioButton mPhotosButton;
    private RadioButton mVideosButton;
    private RadioButton mAudiosButton;
    private int mCurrentPage = 0;
    private boolean mBound = false;
    private String mMediaManagerChatId = null;
    private InteractionStateNotifierView mInteractionStateView;
    private int mSavedFilterType = ContactListManager.filterType;
    private LinkedHashMap<Integer, Fragment> mFragmentsHash = new LinkedHashMap<Integer, Fragment>();
    private MediaCategoriesPagerAdapter mMediaCategoriesPagerAdapter;
    private MediaItemThumbnailLoader mThumbnailService = null;
    private Object mServiceLock = new Object();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        private boolean mConnected;

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction
                    .equals(RegistrationManager.INTERACTION_STATE_CHANGE_ACTION)) {
                final InteractionState state = RegistrationManager
                        .getInteractionState();
                boolean connected = (state == InteractionState.REGISTERED_FULL);

                if (connected != mConnected) {
                    mConnected = connected;
                }

                updateInteractionStateNotification();
            } else if (intentAction.equals(BaseSipApplication.KILL_THE_APP_ACTION)) {
                finish();
            }
        }
    };

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaItemThumbnailLoader.LocalThumbnailBinder binder = (MediaItemThumbnailLoader.LocalThumbnailBinder) service;
            mThumbnailService = binder.getMediaItemThumbnailLoaderService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public MediaItemThumbnailLoader getMediaItemThumbnailLoaderService(){

        if(mBound)
            return mThumbnailService;
        else
            return null;
    }

    private void updateInteractionStateNotification() {
        final InteractionState state = RegistrationManager
                .getInteractionState();
        mInteractionStateView.updateState(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_media_manager);

        createUIViews();
        initViewPager();
        setCheckedListeners();
        bindToDownloaderService();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mViewPager.setCurrentItem(0);

    }

    private void bindToDownloaderService() {
        Intent bindServiceIntent = new Intent(this, MediaItemThumbnailLoader.class);
        bindService(bindServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindToDownloaderService() {
        if(mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private void createUIViews() {

        mTitlebarLayout = (LinearLayout) findViewById(R.id.titlebarLayout);
        if (mTitlebarLayout != null)
            mPageTitle = (TextView) mTitlebarLayout.findViewById(R.id.title);

        mCancelTextViewLayout = (LinearLayout) findViewById(R.id.lytTitleBarRightForwading);
        mCancelTextViewLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToggleButtonLayout = (LinearLayout) findViewById(R.id.toggleButtonLayout);
        mToggleButtonLayout.setVisibility(View.VISIBLE);

        mPhotosButton = (RadioButton) findViewById(R.id.radioBtnPhotos);
        mVideosButton = (RadioButton) findViewById(R.id.radioBtnVideos);
        mAudiosButton = (RadioButton) findViewById(R.id.radioBtnAudios);

        mMediaManagerChatId = getIntent().getExtras().getString(
                MediaManagerUtils.MEDIA_CHATID_KEY);

        mInteractionStateView = (InteractionStateNotifierView) this
                .findViewById(R.id.view_interaction_state);
    }

    private void initViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mMediaCategoriesPagerAdapter = new MediaCategoriesPagerAdapter(getSupportFragmentManager()
                , mMediaManagerChatId);
        mViewPager.setAdapter(mMediaCategoriesPagerAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mCurrentPage = arg0;

                if (arg0 == 0) {
                    setCheckStateOfButtons(0);
                } else if (arg0 == 1) {

                    View view = MediaManagerActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    setCheckStateOfButtons(1);
                } else if (arg0 == 2) {

                    View view = MediaManagerActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    setCheckStateOfButtons(2);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     *
     * Desc: 0 - photos, 1- videos, 2- audios
     *
     * @param pCheckState
     */
    private void setCheckStateOfButtons(int pCheckState) {

        if(pCheckState == 0) {
            mPhotosButton.setChecked(true);
            mVideosButton.setChecked(false);
            mAudiosButton.setChecked(false);
        } else if(pCheckState == 1) {
            mPhotosButton.setChecked(false);
            mVideosButton.setChecked(true);
            mAudiosButton.setChecked(false);
        } else if(pCheckState == 2) {
            mPhotosButton.setChecked(false);
            mVideosButton.setChecked(false);
            mAudiosButton.setChecked(true);
        }
    }

    private void setCheckedListeners() {

        mPhotosButton
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            mViewPager.setCurrentItem(0);
                        }
                    }
                });

        mVideosButton
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            mViewPager.setCurrentItem(1);
                        }
                    }
                });

        mAudiosButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mViewPager.setCurrentItem(2);
                }
            }
        });
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    class MediaCategoriesPagerAdapter extends FragmentStatePagerAdapter {

        private String mChatId = null;

        public MediaCategoriesPagerAdapter(FragmentManager fm, String pChatId) {

            super(fm);
            mChatId = pChatId;
        }

        @Override
        public Fragment getItem(int pos) {
            Bundle args = new Bundle();
            switch (pos) {
                case 0:
                    Fragment fragmentPhotos = new PhotosFragment();
                    args.putString(MediaManagerUtils.MEDIA_CHATID_KEY,
                            mChatId);
                    fragmentPhotos.setArguments(args);
                    mFragmentsHash.put(pos, fragmentPhotos);
                    return fragmentPhotos;
                case 1:
                    Fragment fragmentVideos = new VideosFragment();
                    args.putString(MediaManagerUtils.MEDIA_CHATID_KEY,
                            mChatId);
                    fragmentVideos.setArguments(args);
                    mFragmentsHash.put(pos, fragmentVideos);
                    return fragmentVideos;

                case 2:
                    Fragment fragmentAudios = new AudiosFragment();
                    args.putString(MediaManagerUtils.MEDIA_CHATID_KEY,
                            mChatId);
                    fragmentAudios.setArguments(args);
                    mFragmentsHash.put(pos, fragmentAudios);
                    return fragmentAudios;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            default:
                break;
        }
    }

    class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        @SuppressLint("NewApi")
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                        * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initReceiver();
        updateInteractionStateNotification();
        updateTextSizes();
    }

    private void updateTextSizes() {
        String langName = ConfigurationManager.appLanguage.toLowerCase();
        if (langName.equals("tj")) {
            float textSize = 12;
            mPageTitle.setTextSize(textSize);
            mPhotosButton.setTextSize(textSize);
            mVideosButton.setTextSize(textSize);
            mAudiosButton.setTextSize(textSize);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        ContactListManager.filterType = mSavedFilterType;
    }

    @Override
    protected void onDestroy() {

        // clear the hash prior to system-destruction to clear references on the
        // fragments
        mFragmentsHash.clear();
        ContactListManager.mSelectedContactsForForwarding.clear();

        unbindToDownloaderService();

        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initReceiver() {
        this.registerReceiver(this.mReceiver, new IntentFilter(
                RegistrationManager.UNREGISTERED_ACTION));
        this.registerReceiver(this.mReceiver, new IntentFilter(
                RegistrationManager.INTERACTION_STATE_CHANGE_ACTION));
        this.registerReceiver(this.mReceiver, new IntentFilter(
                BaseSipApplication.KILL_THE_APP_ACTION));
    }

    /**
     * Desc: Method to update contacts fragment upon selection
     */
    public void updateContactsFragment() {
        Fragment contactsFragment = mFragmentsHash.get(1);

        if (contactsFragment instanceof ForwardToContactsFragment) {
            ((ForwardToContactsFragment) contactsFragment).adjustGridViewState();
        }
    }

}
