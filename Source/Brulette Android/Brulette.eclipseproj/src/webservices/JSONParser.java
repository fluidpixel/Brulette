package webservices;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


enum Method_Type {
        POST, GET, PUT, DELETE
};

public class JSONParser {
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromHttpResponse(HttpResponse response, Method_Type type) throws JSONException {         

            try {
	            	HttpEntity httpEntity = response.getEntity();
	            	InputStream is = httpEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                                    is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();
            } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            Log.i("JSON Parser", json.toString());

            if (!type.equals(Method_Type.PUT)) {
                            jObj = new JSONObject(json);
            }
            return jObj;
    }

}
