package aspace.trya;

import android.app.Application;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import io.intercom.android.sdk.Intercom;
import timber.log.Timber;

public class AspaceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("INTITALIZED!");
    }
}
