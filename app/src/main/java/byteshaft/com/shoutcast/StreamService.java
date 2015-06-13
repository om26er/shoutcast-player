package byteshaft.com.shoutcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;

import wseemann.media.FFmpegMediaPlayer;

public class StreamService extends Service implements FFmpegMediaPlayer.OnPreparedListener {

    FFmpegMediaPlayer mMediaPlayer;
    private static StreamService sService;
    private boolean mIsPrepared;
    private boolean mPreparing;

    static StreamService getInstance() {
        return sService;
    }

    static boolean isRunning() {
        return sService != null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sService = this;
        mMediaPlayer = new FFmpegMediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        String url = getString(R.string.shoutcast_url);
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startStream() {
        if (mIsPrepared) {
            mMediaPlayer.start();
        } else if (!mPreparing){
            mMediaPlayer.prepareAsync();
            mPreparing = true;
        }
    }

    void pauseStream() {
        mMediaPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopStream();
    }

    void stopStream() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(FFmpegMediaPlayer fFmpegMediaPlayer) {
        mIsPrepared = true;
        mMediaPlayer.start();
    }
}
