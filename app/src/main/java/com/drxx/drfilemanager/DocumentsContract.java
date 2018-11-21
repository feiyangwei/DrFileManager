package com.drxx.drfilemanager;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.SearchView;

import com.drxx.drfilemanager.misc.ContentProviderClientCompat;

/**
 * 类描述：
 * 创建人：wfy
 * 创建时间：2018/11/21
 * 邮箱：cugb_feiyang@163.com
 */
public class DocumentsContract {
    private static final String TAG = "Documents";

    /**
     * {@hide}
     */
    public static final String METHOD_DELETE_DOCUMENT = "android:deleteDocument";
    /**
     * {@hide}
     */
    public static final String EXTRA_URI = "uri";

    public final static class Document {
        private Document() {
        }

        /**
         * Flag indicating that a document is deletable.
         */
        public static final int FLAG_SUPPORTS_DELETE = 1 << 2;
    }

    /**
     * Delete the given document.
     *
     * @param documentUri document with {@link Document#FLAG_SUPPORTS_DELETE}
     * @return if the document was deleted successfully.
     */
    public static boolean deleteDocument(ContentResolver resolver, Uri documentUri) {
        final ContentProviderClient client = resolver.acquireUnstableContentProviderClient(
                documentUri.getAuthority());
        try {
            deleteDocument(client, documentUri);
            Log.e(TAG, "成功删除");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete document", e);
            return false;
        } finally {
            ContentProviderClientCompat.releaseQuietly(client);
        }
    }

    public static void deleteDocument(ContentProviderClient client, Uri documentUri)
            throws RemoteException {
        final Bundle in = new Bundle();
        in.putParcelable(EXTRA_URI, documentUri);
        client.call(METHOD_DELETE_DOCUMENT, null, in);
    }
}
