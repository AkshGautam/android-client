package com.mifos.api.local;

import android.net.Uri;

import com.mifos.api.model.Payload;
import com.mifos.objects.group.GroupPayload;
import com.mifos.services.data.CenterPayload;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;

/**
 * Created by Rajan Maurya on 23/06/16.
 */
@ContentProvider(authority = MifosDatabase.AUTHORITY,
        database = MifosDatabase.class,
        baseContentUri = MifosDatabase.BASE_CONTENT_URI)
@Database(name = MifosDatabase.NAME, version = MifosDatabase.VERSION, foreignKeysSupported = true)
public class MifosDatabase {

    // database name will be Mifos.db
    public static final String NAME = "Mifos";

    //Always Increase the Version Number
    public static final int VERSION = 3;

    public static final String AUTHORITY = "com.mifos";

    // The scheme is always present and it represents
    public static final String BASE_CONTENT_URI = "content://";

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = Uri.parse(MifosDatabase.BASE_CONTENT_URI + MifosDatabase.AUTHORITY).buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(name = ClientPayload.ENDPOINT,contentProvider = MifosDatabase.class)
    public static class ClientPayload {

        public static final String ENDPOINT = "ClientPayload";

        @ContentUri(path = ClientPayload.ENDPOINT,
                type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = buildUri(ENDPOINT);
    }

    @TableEndpoint(name = CenterPayload.ENDPOINT,contentProvider = MifosDatabase.class)
    public static class CenterPayload {

        public static final String ENDPOINT = "CenterPayload";

        @ContentUri(path = CenterPayload.ENDPOINT,
                type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = buildUri(ENDPOINT);
    }
}
