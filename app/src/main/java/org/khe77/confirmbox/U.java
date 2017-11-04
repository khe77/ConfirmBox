package org.khe77.confirmbox;

import android.util.Log;

/**
 * 싱글톤 클레스 이고, 유틸리티 기능을 모두 총괄한다.
 * 여기서는 앱이 구동중인지 체크하는 객체를 담는 그릇의 용도로 사용.
 */

public class U {
    // ========================================================
    private static final U ourInstance = new U();
    public static U getInstance() {
        return ourInstance;
    }
    private U() {
    }
    // ========================================================
    public void log(String tag, String msg) {
        // 디버그 모드일때만 노출된다.
        //if(BuildConfig.DEBUG)
        Log.i(""+tag, ""+msg);
    }
    // ========================================================
    // 푸시가 와서 알림 띄울때 WebMainActivity 객체가 존재하면 그냥 확인, 없으면 앱을 띄움
    ListActivity listActivity;

    public ListActivity getListActivity() {
        return listActivity;
    }

    public void setListActivity(ListActivity listActivity) {
        this.listActivity = listActivity;
    }
    // =========================================================
}
