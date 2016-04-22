package in.srain.cube.iface;

import in.srain.cube.image.ImageTask;
import in.srain.cube.image.ImageTaskStatistics;

public interface ImageLoadProfiler {
    public void onImageLoaded(ImageTask imageTask, ImageTaskStatistics stat);
}
