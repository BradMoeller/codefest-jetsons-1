package com.codefest_jetsons.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.codefest_jetsons.R;
import com.codefest_jetsons.activity.TicketInfoActivity;
import com.codefest_jetsons.service.TicketService;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParkingNotifications {
    public static final int NOTIFICATION_TEN_MIN = 1;
    public static final int NOTIFICATION_EXPIRED = 2;

    public static void startNotifications(Context ctx, long timeRemaining) {
        Intent intent = new Intent(ctx, TicketService.class);
        intent.putExtra(TicketService.TIME_REMAINING, timeRemaining);
        ctx.startService(intent);
    }
    public static void newParkingNotification(Context ctx, int notificationID) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setContentTitle("Notice");

        switch(notificationID) {
            case NOTIFICATION_TEN_MIN:
                mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                mBuilder.setContentText("Your parking meter will expire in 10 minutes! Add time to your meter now.");
                break;
            case NOTIFICATION_EXPIRED:
                mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                mBuilder.setContentText("Your parking meter is expired! Pay for a meter now!");
                break;
        }


        Intent resultIntent = new Intent(ctx, TicketInfoActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(TicketInfoActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(notificationID, notification);
    }
}
