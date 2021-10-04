# CoPilotDemo

A demo App for CoPilot with companion app on Wear OS. The phone starts the WearOS app.
They use Data Layer to communicate information that the companion app collects from WearOS.
WearOS uses Health services to collect information about Heart rate and callories burn.

To set up emulator for WearOS:
https://developer.android.com/training/wearables/get-started/creating#emulator

More about health services:
https://developer.android.com/training/wearables/health-services

Using synthetic data provides for health services on simulator:
https://developer.android.com/training/wearables/health-services/synthetic-data


Currently running into the following error, so cannot quite test it:
```
2021-10-03 18:51:58.921 5071-5096/com.nogavicka.copilotdemo E/ServiceConnection: Connection to service is not available for package 'com.google.android.wearable.healthservices' and action 'com.google.android.wearable.healthservices.ExerciseClient'.
2021-10-03 18:51:58.923 5071-5096/com.nogavicka.copilotdemo E/ServiceConnection: Connection disconnected and maximum number of retries reached.
    java.util.concurrent.CancellationException: Service not available
        at androidx.health.services.client.impl.ipc.internal.ServiceConnection.connect(ServiceConnection.java:145)
        at androidx.health.services.client.impl.ipc.internal.ServiceConnection.enqueue(ServiceConnection.java:203)
        at androidx.health.services.client.impl.ipc.internal.ConnectionManager.handleMessage(ConnectionManager.java:123)
        at android.os.Handler.dispatchMessage(Handler.java:102)
        at android.os.Looper.loop(Looper.java:193)
        at android.os.HandlerThread.run(HandlerThread.java:65)
```

Looks like a [recent release problem](https://stackoverflow.com/questions/69083442/cant-connect-to-android-wearable-health-services).
