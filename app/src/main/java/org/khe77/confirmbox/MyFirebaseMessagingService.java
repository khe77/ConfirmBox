package org.khe77.confirmbox;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * 푸시 메시지 수신
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        U.getInstance().log("SH", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            U.getInstance().log("SH", "Message data payload: " + remoteMessage.getData());
            //Toast.makeText(this, remoteMessage.getData().get("data"), Toast.LENGTH_SHORT).show();
        }
        if (remoteMessage.getNotification() != null) {
            U.getInstance().log("SH", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage.getData().get("data"));
    }

    private void sendNotification(String messageBody) {
        // 알림이 와서 눌렀을때 어디로 갈것인지? 시나리오 필요
        // 앱이 켜져 있으면 확인 하고 알림이 사라진다.
        // 앱이 종료된 상태면 확인 하면 앱을 구동시켜서 해당 내용을 확인 할 수 있게 진입시킨다.

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // 노티처리를 하는 메인 코드 ==============================================
        // 상단바에 알림을 울리는 인스턴스 메시지 => 드레그 하면 내용을 볼 수 있다.
        // 클릭을 하면 해당 앱으로 진입할 수 있다.
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        // 맨앞 아이콘(통산 앱의 아이콘 사용)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        // 제목
                        .setContentTitle("신한카드 결재통 알리미")
                        // 알림 내용 (xx님 계좌 xx에 얼마가 입금되었습니다.)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        // 사운드 지정 (앱의 고유 사운드 사용 가능)
                        .setSound(defaultSoundUri);
        // 알림을 누르면 무엇을 수행할 것인지 세팅
        //.setContentIntent(pendingIntent);
        // 앱이 구동되어 있지 않다!!
        //if(U.getInstance().getListActivity() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            // 액티비티를 띄울때 데이타를 보내기
            intent.putExtra("push", messageBody);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
        //}
        // 알림 띠우기 ================================================================
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
