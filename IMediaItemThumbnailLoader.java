

import android.content.Context;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;


/**
 * Created by zekigu on 21.04.2016.
 */
public interface IMediaItemThumbnailLoader {

    public void loadMediaItemByPath(Context pContext,
                                    IMItem pMediaItem,
                                    WeakReference<ImageView> pThumbnailView);

    public void loadMediaItemByFile(Context pContext,
                                    File pFile,
                                    WeakReference<ImageView> pThumbnailView);

}
