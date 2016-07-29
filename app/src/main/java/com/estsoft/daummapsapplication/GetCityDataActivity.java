package com.estsoft.daummapsapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/*
    서버를 통해 데이터 가져오기(TEST)_전주
     - http://localhost:8080/TravelFriendAndroid/android/getPinData/{cityList_no}

     ex) http://222.239.250.207:8080/travelfriendandroid/android/getPinData/{1~12}
     * cityList_no : DB에 들어있는 cityList의 no(PK) 번호 임. (주의 : 지역코드 아님)
	 * cityList_no 를  1 ~ 12 까지 넣으면 각 번호에 해당하는 지역의 정보를 가져온다.
	 * return값 : no(PK), location, category
	 *
	 * ex){"atrList":[{"no":6788,"cityList_no":0,"name":null,"location":"34.7556775223,127.7476389815","picture":null,"info":null,"category":"inn","address":null},...
 */

public class GetCityDataActivity extends AppCompatActivity {
    private static final String URL = "http://222.239.250.207:8080/travelfriendandroid/android/getPinData/12";

    private static final String TAG_RESULTS="atrList";
    private static final String TAG_NO = "no";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_CATEGORY ="category";

    JSONArray datas = null;
    String myJSON;

    ArrayList<HashMap<String, String>> dataList;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_city_data);

        list = (ListView) findViewById(R.id.listView);
        dataList = new ArrayList<HashMap<String,String>>();
        getData(URL);

    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            datas = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i = 0; i< datas.length(); i++){
                JSONObject c = datas.getJSONObject(i);
                String no = c.getString(TAG_NO);
                String location = c.getString(TAG_LOCATION);
                String category = c.getString(TAG_CATEGORY);

                HashMap<String,String> datasMap = new HashMap<String,String>();

                datasMap.put(TAG_NO,no);
                datasMap.put(TAG_LOCATION,location);
                datasMap.put(TAG_CATEGORY,category);

                dataList.add(datasMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(), dataList, R.layout.list_item,
                    new String[]{TAG_NO,TAG_LOCATION,TAG_CATEGORY},
                    new int[]{R.id.no, R.id.location, R.id.category}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
