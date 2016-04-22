
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import java.util.ArrayList;


public class PhotosFragment extends Fragment implements TextWatcher {

    private LayoutInflater inflater;
    private MediaManagerActivity mContext;
    private String mChatId;
    private GridView mPhotosGridView;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String intentAction = intent.getAction();
        }
    };

    // newInstance constructor for creating fragment with arguments
    public PhotosFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = (MediaManagerActivity) activity;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.photos_fragment,
                container, false);

        mPhotosGridView = (GridView) viewLayout.findViewById(R.id.gridviewPhotos);
        mChatId = getArguments().getString(MediaManagerUtils.MEDIA_CHATID_KEY);
        generatePhotoItemList();

        return viewLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        initReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mContext.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initReceiver() {
    }

    private void generatePhotoItemList() {

        ArrayList<IMItem> photosList = IMUtils.getMediaPhotoIMItemsSorted(mChatId);
        MediaItemsAdapter photosAdapter = new MediaItemsAdapter(mContext, photosList, 0);
        mPhotosGridView.setAdapter(photosAdapter);
    }

    private void saveScrollPosition() {
    }

    private void setScrollPosition() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
    }
}