package com.ron.keepie.server.server_callbacks;

import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.Track;

import java.util.List;

public interface TrackCallback {
    void get_tracks(List<Track> tracks);
    void track_updated();
    void track_created();
    void track_deleted();
    void failed(int status_code,String info);
}
