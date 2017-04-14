package top.leekm.android.dynamiclib;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Created by lkm on 2017/4/3.
 */

public interface ActivityLifeCircle {

    void onCreate(Bundle savedInstance);

    void onSaveInstanceState(Bundle outState);

    void onSaveInstanceState(Bundle outState,
                             PersistableBundle outPersistentState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
