package com.orlandofrancesco.audiotoggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class AudioTileService extends TileService {

    AudioManager audioManager;
    int current;

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        setTileToCurrentRingMode();
    }

    @Override
    public void onClick() {
        super.onClick();
        current = audioManager.getRingerMode();

        current++;
        if (current == 3) {
            current = 0;
        }

        switch (current){
            case 0:
                audioManager.setRingerMode(0);
                break;
            case 1:
                audioManager.setRingerMode(1);
                break;
            case 2:
                audioManager.setRingerMode(2);
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                setTileToCurrentRingMode();
            }
        };
        IntentFilter filter = new IntentFilter( AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(receiver,filter);
    }

    void setTileToCurrentRingMode(){
        current = audioManager.getRingerMode();

        Icon silentIcon = Icon.createWithResource(this, R.drawable.round_volume_mute_24);
        Icon vibrationIcon = Icon.createWithResource(this, R.drawable.round_vibration_24);
        Icon normalIcon = Icon.createWithResource(this, R.drawable.round_volume_up_24);

        switch (current){
            case 0:
                this.getQsTile().setIcon(silentIcon);
                this.getQsTile().setLabel(getString(R.string.silent));
                this.getQsTile().setState(Tile.STATE_INACTIVE);
                this.getQsTile().updateTile();
                break;
            case 1:
                this.getQsTile().setIcon(vibrationIcon);
                this.getQsTile().setLabel(getString(R.string.vibration));
                this.getQsTile().setState(Tile.STATE_ACTIVE);
                this.getQsTile().updateTile();
                break;
            case 2:
                this.getQsTile().setIcon(normalIcon);
                this.getQsTile().setLabel(getString(R.string.normal));
                this.getQsTile().setState(Tile.STATE_ACTIVE);
                this.getQsTile().updateTile();
                break;
        }
    }
}
