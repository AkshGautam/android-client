package com.mifos.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.mifos.mifosxdroid.R;

/**
 * Created by aksh on 3/5/18.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver mContentResolver;
    private final AccountManager mAccountManager;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }
    Account mainAccount;
    public static final int SYNC_INTERVAL = 60 * 1;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.v("Sync class me","sync adapter on perform sync");
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = myAccount;
        String authority = "com.mifos.provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }
    static Account myAccount;
    public static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        myAccount = newAccount;
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, "com.mifos.provider", true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }
    public static Account getSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), "com.mifos");
        return newAccount;
    }
    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                "com.mifos.provider", bundle);
    }
}
