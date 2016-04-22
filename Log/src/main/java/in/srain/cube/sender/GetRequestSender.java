package in.srain.cube.sender;

import in.srain.cube.request.IRequest;
import in.srain.cube.request.sender.BaseRequestSender;

import java.net.HttpURLConnection;

public class GetRequestSender extends BaseRequestSender {

    public GetRequestSender(IRequest<?> request, HttpURLConnection httpURLConnection) {
        super(request, httpURLConnection);
    }

    @Override
    public void send() {

    }
}
