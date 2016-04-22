package in.srain.cube.iface;

import in.srain.cube.image.ImageTask;
import in.srain.cube.image.iface.ProgressUpdateHandler;

import java.io.OutputStream;

public interface ImageDownloader {

    public boolean downloadToStream(ImageTask imageTask,
                                    String url,
                                    OutputStream outputStream,
                                    ProgressUpdateHandler progressUpdateHandler);
}