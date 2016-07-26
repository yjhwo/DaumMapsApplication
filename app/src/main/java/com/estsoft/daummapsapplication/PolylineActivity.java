package com.estsoft.daummapsapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

public class PolylineActivity extends AppCompatActivity implements MapView.MapViewEventListener{
    private MapView mapView;
    private MapPolyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyline);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mapView.setHDMapTileEnabled(true);      // 고해상도 지도 타일 사용
        mapView.setMapViewEventListener(this);

        polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정

        // Polyline 좌표 지정.
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.8241706, 127.1480532));  // 전주시청
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.8551190, 127.1443080));  // 전주 동물원

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 500; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

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
}
