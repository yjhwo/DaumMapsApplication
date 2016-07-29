package com.estsoft.daummapsapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;

public class SimpleMarkerMapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    private static final MapPoint CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(33.4460238, 126.5709653);
    private static final MapPoint CUSTOM_MARKER_POINT2 = MapPoint.mapPointWithGeoCoord(37.447229, 127.015515);
    private static final MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.4020737, 127.1086766);

    private final int MENU_DEFAULT_CALLOUT_BALLOON = Menu.FIRST;
    private final int MENU_CUSTOM_CALLOUT_BALLOON = Menu.FIRST + 1;

    private MapView mMapView;
    private MapPOIItem mDefaultMarker;
    private MapPOIItem mCustomMarker;
    private MapPOIItem mCustomBmMarker;

    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            ((ImageView) mCalloutBalloon.findViewById(R.id.badge)).setImageResource(R.drawable.jeju);
            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
            ((TextView) mCalloutBalloon.findViewById(R.id.desc)).setText(((HashMap<String, String>)poiItem.getUserObject()).get("desc"));
//            ((TextView) mCalloutBalloon.findViewById(R.id.desc)).setText("Custom CalloutBalloon");
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_marker_map);

        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);

        mMapView.setMapViewEventListener (this);
        mMapView.setPOIItemEventListener(this);

        // 구현한 CalloutBalloonAdapter 등록
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
      //  createDefaultMarker(mMapView);
        createCustomMarker(mMapView);
       // createCustomBitmapMarker(mMapView);
        showAll();

    }
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_DEFAULT_CALLOUT_BALLOON, Menu.NONE, "Default CalloutBalloon");
        menu.add(0, MENU_CUSTOM_CALLOUT_BALLOON, Menu.NONE, "Custom CalloutBalloon");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DEFAULT_CALLOUT_BALLOON: {
                mMapView.removeAllPOIItems();
                mMapView.setCalloutBalloonAdapter(null);
                createDefaultMarker(mMapView);
                createCustomMarker(mMapView);
                showAll();
                return true;
            }
            case MENU_CUSTOM_CALLOUT_BALLOON: {
                mMapView.removeAllPOIItems();
                mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                createDefaultMarker(mMapView);
                createCustomMarker(mMapView);
                showAll();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void createDefaultMarker(MapView mapView) {
        mDefaultMarker = new MapPOIItem();
        String name = "Default Marker";
        mDefaultMarker.setItemName(name);
        mDefaultMarker.setTag(0);
        mDefaultMarker.setMapPoint(DEFAULT_MARKER_POINT);
        mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        HashMap<String, String> userObject = new HashMap<>();
        userObject.put("desc","제주시첨단로300");
        mDefaultMarker.setUserObject(userObject);

        mapView.addPOIItem(mDefaultMarker);
        mapView.selectPOIItem(mDefaultMarker, true);
        mapView.setMapCenterPoint(DEFAULT_MARKER_POINT, false);
    }

    private void createCustomMarker(MapView mapView) {
        mCustomMarker = new MapPOIItem();
        String name = "이스트소프트 제주R&D센터";
        mCustomMarker.setItemName(name);
        mCustomMarker.setTag(1);
        mCustomMarker.setMapPoint(CUSTOM_MARKER_POINT);

        HashMap<String, String> userObject = new HashMap<>();
        userObject.put("desc","제주시첨단로300");
        mCustomMarker.setUserObject(userObject);

        mCustomMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);

        mCustomMarker.setCustomImageResourceId(R.drawable.custom_marker_red);
        mCustomMarker.setCustomImageAutoscale(false);
        mCustomMarker.setCustomImageAnchor(0.5f, 1.0f);     // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        mapView.addPOIItem(mCustomMarker);
        mapView.selectPOIItem(mCustomMarker, true);
        mapView.setMapCenterPoint(CUSTOM_MARKER_POINT, false);

    }

    private void createCustomBitmapMarker(MapView mapView) {
        mCustomBmMarker = new MapPOIItem();
        String name = "Custom Bitmap Marker";
        mCustomBmMarker.setItemName(name);
        mCustomBmMarker.setTag(2);
        mCustomBmMarker.setMapPoint(CUSTOM_MARKER_POINT2);

        mCustomBmMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.custom_marker_star);
        mCustomBmMarker.setCustomImageBitmap(bm);
        mCustomBmMarker.setCustomImageAutoscale(false);
        mCustomBmMarker.setCustomImageAnchor(0.5f, 0.5f);

        HashMap<String, String> userObject = new HashMap<>();
        userObject.put("desc","제주시첨단로300");
        mCustomBmMarker.setUserObject(userObject);

        mapView.addPOIItem(mCustomBmMarker);
        mapView.selectPOIItem(mCustomBmMarker, true);
        mapView.setMapCenterPoint(CUSTOM_MARKER_POINT, true);
    }

    private void showAll() {
        int padding = 20;
        float minZoomLevel = 7;
        float maxZoomLevel = 10;

        // 지도뷰의 중심좌표와 줌레벨을 해당 point가 모두 나오도록 조정
//        MapPointBounds bounds = new MapPointBounds(CUSTOM_MARKER_POINT, DEFAULT_MARKER_POINT);
        MapPointBounds bounds = new MapPointBounds(CUSTOM_MARKER_POINT,CUSTOM_MARKER_POINT);
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(bounds, padding, minZoomLevel, maxZoomLevel));
    }
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    // -----
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Toast.makeText(this, "Clicked " + mapPOIItem.getItemName() + " Callout Balloon", Toast.LENGTH_SHORT).show();

        Log.e("getItemName",mapPOIItem.getItemName()+"");
        Log.e("getLeftSideButton",mapPOIItem.getLeftSideButtonResourceIdOnCalloutBalloon()+"");
        Log.e("getRightSideButton",mapPOIItem.getRightSideButtonResourceIdOnCalloutBalloon()+"");
        Log.e("getSelectedMarkerType",mapPOIItem.getCustomSelectedImageResourceId()+"");

        // 자세히보기 액티비티로 넘어가기
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {


    }


}

