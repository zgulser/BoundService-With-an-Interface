
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.PipedOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MediaItemsAdapter extends BaseAdapter {

    private MediaManagerActivity mContext;
    private ArrayList<IMItem> mMediaItems;
    private int mMediaType;

    /**
     *
     * Desc: Const
     *
     * @param pActivity
     * @param pMediaItems
     * @param pMediaType 0- photos, 1-videos, 2-audios
     */
    public MediaItemsAdapter(MediaManagerActivity pActivity, ArrayList<IMItem> pMediaItems, int pMediaType ) {
        mContext = pActivity;
        this.mMediaItems = pMediaItems;
        this.mMediaType = pMediaType;

    }

    public int getCount() {
        return mMediaItems.size();
    }

    public Object getItem(int position) {
        return mMediaItems.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder mediaItemHolder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.media_manager_item, null);
            mediaItemHolder = new ViewHolder(rowView);
            rowView.setTag(mediaItemHolder);
        } else {
            mediaItemHolder = (ViewHolder) rowView.getTag();

        }

        ImageView imageViewThumbnail = mediaItemHolder.getThumbnailView();
        ImageView imageViewMediaIcon = mediaItemHolder.getMediaIconView();
        TextView textViewDuration = mediaItemHolder.getDurationTextView();

        IMItem item = mMediaItems.get(position);
        populateAdapterThumbnailView(imageViewThumbnail, item);
        populateAdapterMediaIconView(imageViewMediaIcon, item);
        populateAdapterDurationView(textViewDuration, item);

        return rowView;
    }

    private void populateAdapterThumbnailView(ImageView pImageViewThumbnail, IMItem pItem) {

        switch (mMediaType) {
            case 0:
                mContext.getMediaItemThumbnailLoaderService().loadMediaItemByPath(mContext, pItem, new WeakReference<ImageView>(pImageViewThumbnail));
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    private void populateAdapterMediaIconView(ImageView pImageViewMediaIcon, IMItem pItem) {
        switch (mMediaType) {
            case 0:
                pImageViewMediaIcon.setVisibility(View.GONE);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    private void populateAdapterDurationView(TextView pTextViewDuration, IMItem pItem) {
        switch (mMediaType) {
            case 0:
                pTextViewDuration.setVisibility(View.GONE);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    static class ViewHolder {

        private ImageView mThumbnailView;
        private ImageView mMediaIconView;
        private TextView mDurationTextView;
        private View mBase;

        ViewHolder(View base){
            mBase = base;
        }

        public ImageView getThumbnailView() {
            if(mThumbnailView == null){
                mThumbnailView = (ImageView) mBase.findViewById(R.id.imageViewThumbnail);
            }
            return mThumbnailView;
        }

        public TextView getDurationTextView() {
            if(mDurationTextView == null){
                mDurationTextView = (TextView) mBase.findViewById(R.id.textViewDuration);
            }
            return mDurationTextView;
        }

        public ImageView getMediaIconView() {
            if(mMediaIconView == null){
                mMediaIconView = (ImageView) mBase.findViewById(R.id.imageViewMediaIcon);
            }
            return mMediaIconView;
        }

    }
}
