
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;

public class AudiosFragment extends Fragment implements TextWatcher {

    private LayoutInflater inflater;
    private MediaManagerActivity mContext;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String intentAction = intent.getAction();
        }
    };

    private void generateList() {
    }

    // newInstance constructor for creating fragment with arguments
    public AudiosFragment() {
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
        View viewLayout = inflater.inflate(R.layout.audios_fragment,
                container, false);

        createUI(viewLayout, savedInstanceState);

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

    private void createUI(View viewLayout, Bundle savedInstanceState) {

    }

    private void initReceiver() {
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