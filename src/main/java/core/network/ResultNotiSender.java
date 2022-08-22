package core.network;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ResultNotiSender {

    private final String notiUrl;

    public ResultNotiSender(String notiUrl) {
        this.notiUrl = notiUrl;
    }

    public void send(String content) throws Exception {
        URL url = new URL(notiUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", content);
        String data = jsonObject.toString();

        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);

        log.debug("ResponseCode:{} / Msg: {}", http.getResponseCode(), http.getResponseMessage());
        http.disconnect();
    }

}
