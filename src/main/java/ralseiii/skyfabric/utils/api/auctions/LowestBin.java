package ralseiii.skyfabric.utils.api.auctions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class LowestBin {
    static AtomicReference<Map<String, Long>> priceMapReference = new AtomicReference<>();
    public static void update() {
        try {
            // use skytils' api to fetch lowest bin
            URL lowestBinApi = new URL("https://whoknew.sbe-stole-skytils.design/api/auctions/lowestbins");
            HttpURLConnection connection = (HttpURLConnection) lowestBinApi.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                Scanner s = new Scanner(connection.getInputStream());
                StringBuilder b = new StringBuilder();
                while (s.hasNextLine()) {
                    b.append(s.nextLine() + "\n");
                }
                s.close();
                JsonObject items = new Gson().fromJson(b.toString(), JsonObject.class);
                Map<String, Long> priceMap = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                    priceMap.put(entry.getKey(), entry.getValue().getAsLong());
                }
                priceMapReference.set(priceMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Long get(String id) {
        return priceMapReference.get().getOrDefault(id, 0L);
    }

    public static Boolean isAvailable(String id) {
        return priceMapReference.get().containsKey(id);
    }
}
