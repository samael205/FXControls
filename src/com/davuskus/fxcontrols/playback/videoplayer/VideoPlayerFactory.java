package com.davuskus.fxcontrols.playback.videoplayer;

import com.davuskus.utils.javafx.FXMLUtils;
import com.davuskus.utils.tuple.Tuple;
import javafx.scene.Node;
import javafx.scene.media.Media;

import java.net.URL;

public final class VideoPlayerFactory {

    public static Tuple<Node, VideoPlayer> createVideoPlayer() {
        return FXMLUtils.loadFXML(VideoPlayer.class.getResource("video_player.fxml"));
    }

    public static Tuple<Node, VideoPlayer> createVideoPlayer(Media media) {
        Tuple<Node, VideoPlayer> videoPlayerTuple = createVideoPlayer();
        videoPlayerTuple.getSecond().setMedia(media);
        return videoPlayerTuple;
    }

    public static Tuple<Node, VideoPlayer> createVideoPlayer(URL mediaURL) {
        return createVideoPlayer(new Media(mediaURL.toExternalForm()));
    }

}
