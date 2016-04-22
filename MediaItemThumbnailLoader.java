
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.ref.WeakReference;


/**
 * Created by zekigu on 21.04.2016.
 */
public class MediaItemThumbnailLoader extends Service implements IMediaItemThumbnailLoader {

    private final IBinder mLocalBinder = new LocalThumbnailBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    @Override
    public void loadMediaItemByPath(Context pContext, IMItem pMediaItem, WeakReference<ImageView> pThumbnailView) {
        String publicFileUri = CommonUtils
                .getRelevantUriFromFileProvider(pMediaItem.getId());

        Glide.with(pContext)
                .load(publicFileUri)
                .into(pThumbnailView.get());
    }

    @Override
    public void loadMediaItemByFile(Context pContext, File pFile, WeakReference<ImageView> pThumbnailView) {
        if(pFile != null &&
                pFile.exists()) {

            Glide.with(pContext)
                    .load(pFile.getPath())
                    .into(pThumbnailView.get());
        }
    }

    public class LocalThumbnailBinder extends Binder {

        public MediaItemThumbnailLoader getMediaItemThumbnailLoaderService() {
            return MediaItemThumbnailLoader.this;
        }
    }
}
