package com.mirea.shelmichas.mireaproject.ui.places;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.mirea.shelmichas.mireaproject.databinding.FragmentPlacesBinding;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.ImageProvider;

// Фрагмент с картой Яндекс MapKit.
// 5 заведений Москвы маркерами + геолокация пользователя.
public class PlacesFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork = false;

    private FragmentPlacesBinding binding;
    private MapView mapView;
    private UserLocationLayer userLocationLayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlacesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = binding.mapview;
        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().move(new CameraPosition(
                new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f));

        addMarkers();

        int locationPermissionStatus = ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
            loadUserLocationLayer();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (isWork) {
                loadUserLocationLayer();
            } else {
                Toast.makeText(requireContext(), "Нет разрешения на геолокацию",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadUserLocationLayer() {
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
    }

    // Маркеры 5 заведений в отдельном слое + слушатель на слой
    private void addMarkers() {
        MapObjectCollection markerLayer = mapView.getMap().getMapObjects().addCollection();

        addPlacemark(markerLayer, new Point(55.741111, 37.620556), "Третьяковская галерея",
                "Адрес: Лаврушинский пер., 10\nГлавный музей русского искусства");
        addPlacemark(markerLayer, new Point(55.760556, 37.618611), "Большой театр",
                "Адрес: Театральная пл., 1\nЛегендарный театр оперы и балета");
        addPlacemark(markerLayer, new Point(55.821111, 37.639444), "ВДНХ",
                "Адрес: пр-т Мира, 119\nВыставка достижений народного хозяйства");
        addPlacemark(markerLayer, new Point(55.728889, 37.609167), "Парк Горького",
                "Адрес: ул. Крымский Вал, 9\nГлавный парк Москвы");
        addPlacemark(markerLayer, new Point(55.736389, 37.606389), "Музеон",
                "Адрес: ул. Крымский Вал, вл. 2\nПарк искусств под открытым небом");

        // Один слушатель на слой — получает конкретный маркер через mapObject
        markerLayer.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                if (mapObject instanceof PlacemarkMapObject) {
                    Object data = mapObject.getUserData();
                    if (data instanceof String) {
                        String[] parts = ((String) data).split("\\|");
                        if (parts.length >= 2) {
                            Toast.makeText(requireContext(),
                                    parts[0] + "\n" + parts[1],
                                    Toast.LENGTH_LONG).show();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    // Создаёт PlacemarkMapObject с иконкой и сохраняет title|description в userData
    private void addPlacemark(MapObjectCollection layer, Point point,
                              String title, String description) {
        PlacemarkMapObject marker = layer.addPlacemark(
                point,
                ImageProvider.fromResource(requireContext(), android.R.drawable.ic_menu_mylocation));
        marker.setUserData(title + "|" + description);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onStop() {
        if (mapView != null) {
            mapView.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mapView = null;
    }
}
