package com.sillybubbles.game;

public interface AdsController {

    public boolean isWifiConnected();
    public boolean canWriteExternal();

    public void showBannerAd();
    public void hideBannerAd();

    public void showInterstitialAd (Runnable then);

}