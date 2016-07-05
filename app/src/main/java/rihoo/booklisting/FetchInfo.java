package rihoo.booklisting;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FetchInfo extends AsyncTask<String, Void, String> {

    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> author = new ArrayList<>();

    public AsyncResponse delegate = null;

    public FetchInfo(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected String doInBackground(String... URLs) {
        StringBuilder strBuilder = new StringBuilder();

        for (String searchURL : URLs) {
            // search URLs
            HttpClient client = new DefaultHttpClient();

            try {
                // get the data
                HttpGet get = new HttpGet(searchURL);

                HttpResponse response = client.execute(get);

                StatusLine searchStatus = response.getStatusLine();

                if (searchStatus.getStatusCode() == 200) {
                    String lineIn;

                    // result
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    InputStreamReader input = new InputStreamReader(content);
                    BufferedReader reader = new BufferedReader(input);

                    while ((lineIn = reader.readLine()) != null) {
                        strBuilder.append(lineIn);
                    }
                } else {
                    strBuilder.append("Error: received HTTP Status: " +
                            toString().valueOf(searchStatus.getStatusCode()));
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (strBuilder.toString() == "") {
                    strBuilder.append("Error: there has been a connection problem.");
                }
            }
        }

        return strBuilder.toString();
    }

    // Parse search results
    protected void onPostExecute(String result) {
        if (result.contains("Error: received HTTP Status: ") ||
                result.contains("Error: there has been a connection problem.")) {
            title.add(result);
            author.add("");
            delegate.processFinish(title, author);
        } else {
            try {
                JSONObject resultObject = new JSONObject(result);

                JSONArray bookArray = resultObject.getJSONArray("items");

                //ArrayList<JSONObject> bookObject = new ArrayList<>();

                // JSONObject bookObject = bookArray.getJSONObject(0);
                // JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");

                JSONObject bookObject;
                JSONObject volumeObject;

                for (int i = 0; i < bookArray.length(); i++) {
                    bookObject = bookArray.getJSONObject(i);
                    volumeObject = bookObject.getJSONObject("volumeInfo");

                    // Parse for the book title
                    try {
                        title.add("Title: " + volumeObject.getString("title"));
                    } catch (JSONException jse) {
                        title.add("N/A");
                        jse.printStackTrace();
                    }

                    // Parse for the author(s)
                    StringBuilder authorBuild = new StringBuilder("");

                    try {
                        JSONArray authorArray = volumeObject.getJSONArray("authors");
                        for (int a = 0; a < authorArray.length(); a++) {
                            if (a > 0) authorBuild.append(", ");
                            authorBuild.append(authorArray.getString(a));
                        }
                        author.add("Author(s): " + authorBuild.toString());
                    } catch (JSONException jse) {
                        author.add("N/A");
                        jse.printStackTrace();
                    }
                }
                delegate.processFinish(title, author);
            } catch (Exception e) {
                e.printStackTrace();
                title.add("No results found.");
                author.add("");
                delegate.processFinish(title, author);
            }
        }
    }

}