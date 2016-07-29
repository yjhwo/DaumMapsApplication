package com.estsoft.daummapsapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.estsoft.daummapsapplication.search.Item;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;
import java.util.List;

public class PolylineActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener{
    private static final String LOG_TAG = "PolylineActivity";

    private String url = "daummaps://route?sp=35.8241706,127.1480532&ep=35.8551190,127.1443080&by=FOOT";

    private MapView mapView;
    private MapPolyline polyline;
    private static final MapPoint MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(35.8241706, 127.1480532);    // 전주시청
    private static final MapPoint MARKER_POINT2 = MapPoint.mapPointWithGeoCoord(35.8551190, 127.1443080);    // 전주동물원

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyline);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mapView.setHDMapTileEnabled(true);      // 고해상도 지도 타일 사용
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        // ------ POIITEM
        MapPOIItem marker1 = new MapPOIItem();
        marker1.setItemName("전주시청");
        marker1.setTag(0);
        marker1.setMapPoint(MARKER_POINT1);
        marker1.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker1.setCustomImageResourceId(R.drawable.map_pin_blue); // 기본으로 제공하는 BluePin 마커 모양
        marker1.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker1.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
        marker1.setCustomImageAutoscale(false);                     // 마커 크기 큰 상태!
        marker1.setShowAnimationType(MapPOIItem.ShowAnimationType.DropFromHeaven);

        MapPOIItem marker2 = new MapPOIItem();
        marker2.setItemName("전주동물원");
        marker2.setTag(1);
        marker2.setMapPoint(MARKER_POINT2);
        marker2.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker2.setCustomImageResourceId(R.drawable.map_pin_blue); // 기본으로 제공하는 BluePin 마커 모양
        marker2.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker2.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
        marker2.setCustomImageAutoscale(false);
        marker2.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        // -------
        mapView.addPOIItem(marker1);
        mapView.addPOIItem(marker2);

        // ------POLYLINE
        polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정

        // Polyline 좌표 지정.
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.8241706, 127.1480532));  // 전주시청
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.8551190, 127.1443080));  // 전주 동물원

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

//         지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
//        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
//        int padding = 500; // px
//        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        showAll();
    }

    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.title_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem == null) return null;

            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
//            mCalloutBalloon.setX(100.0f);
//            mCalloutBalloon.setY(50.0f);
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }


    private void showAll() {
        int padding = 20;
        float minZoomLevel = 3;
        float maxZoomLevel = 7;

        // 지도뷰의 중심좌표와 줌레벨을 해당 point가 모두 나오도록 조정
        MapPointBounds bounds = new MapPointBounds(MARKER_POINT1,MARKER_POINT2);
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(bounds, padding, minZoomLevel, maxZoomLevel));
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.e("onMapViewSingleTapped",mapPoint.getMapPointGeoCoord().latitude+","+mapPoint.getMapPointGeoCoord().longitude);

        double clickedLat = mapPoint.getMapPointGeoCoord().latitude;
        double clickedLong = mapPoint.getMapPointGeoCoord().longitude;

        MapPoint[] pnt = polyline.getMapPoints();

        for(int i=0; i<pnt.length; i++){
            Log.e("polyline_getPoint"+i, pnt[i].getMapPointGeoCoord().latitude+","+pnt[i].getMapPointGeoCoord().longitude);
        }

        // 비교
        if( clickedLat >= pnt[0].getMapPointGeoCoord().latitude && clickedLat <= pnt[1].getMapPointGeoCoord().latitude &&
                clickedLong <= pnt[0].getMapPointGeoCoord().longitude && clickedLong >= pnt[1].getMapPointGeoCoord().longitude  ){
            Log.e("polyline","선택");

            // 길찾기로 넘어가게
            Intent intent = onRoute();

            if(intent!=null)
                startActivity(intent);
        }

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.e("onMapViewDoubleTapped",mapPoint.getMapPointGeoCoord().latitude+","+mapPoint.getMapPointGeoCoord().longitude);
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Log.e("onMapViewLongPressed",mapPoint.getMapPointGeoCoord().latitude+","+mapPoint.getMapPointGeoCoord().longitude);
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
       // Log.e("onMapViewDragStarted",mapPoint.getMapPointGeoCoord().latitude+","+mapPoint.getMapPointGeoCoord().longitude);
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        //Log.e("onMapViewDragEnded",mapPoint.getMapPointGeoCoord().latitude+","+mapPoint.getMapPointGeoCoord().longitude);
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


    private Intent onRoute(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        CustomSchemeURL daummap = new CustomSchemeURL(this, intent) {
            @Override
            public boolean canOpenDaummapURL() {
                return super.canOpenDaummapURL();
            }
        };

        if(daummap.existDaummapApp()){
            return intent;
        } else {
            CustomSchemeURL.openDaummapDownloadPage(this);
        }
        return null;
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Log.e(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude,mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude), 2, true);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
