package byteshaft.com.shoutcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button mPlaybackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlaybackButton = (Button) findViewById(R.id.button_toggle_playback);
        mPlaybackButton.setOnClickListener(this);
        startService(new Intent(getApplicationContext(), StreamService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StreamService.getInstance() != null && StreamService.getInstance().mMediaPlayer.isPlaying()) {
            mPlaybackButton.setText("Pause");
        } else {
            mPlaybackButton.setText("Play");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (StreamService.isRunning()  && !StreamService.getInstance().mMediaPlayer.isPlaying()) {
            StreamService.getInstance().stopStream();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_toggle_playback:
                if (StreamService.isRunning()  && !StreamService.getInstance().mMediaPlayer.isPlaying()) {
                    StreamService.getInstance().startStream();
                    mPlaybackButton.setText("Pause");
                } else {
                    StreamService.getInstance().pauseStream();
                    mPlaybackButton.setText("Start");
                }
        }
    }
}
