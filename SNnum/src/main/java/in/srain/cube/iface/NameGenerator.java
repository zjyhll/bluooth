package in.srain.cube.iface;

import in.srain.cube.image.ImageLoadRequest;

public interface NameGenerator {

    /**
     * @param request
     * @return
     */
    public String generateIdentityUrlFor(ImageLoadRequest request);
}
