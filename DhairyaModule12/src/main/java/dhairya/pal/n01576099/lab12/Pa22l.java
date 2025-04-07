// Dhairya Pal N01576099

package dhairya.pal.n01576099.lab12;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.Permission;
import java.util.List;

public class Pa22l extends Fragment {
    private MapView mapView;
    private GoogleMap gMap;
    private TextView textView;
    ActivityResultLauncher<String> permissionLauncher;
    StringBuilder fullAddress;
    NotificationManagerCompat managerCompat;

    private static final LatLng SYDNEY = new LatLng(-34, 151);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pa22l, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.dhaTextView);
        mapView = view.findViewById(R.id.dhaMapView);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(googleMap -> {
                gMap = googleMap;
                gMap.getUiSettings().setZoomControlsEnabled(true);
                gMap.addMarker(new MarkerOptions().position(SYDNEY).title(getString(R.string.sydneydhairya)));
                Marker marker = gMap.addMarker(new MarkerOptions().position(SYDNEY).title(getString(R.string.sydneydhairya)));
                marker.showInfoWindow();
                gMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
                gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        Geocoder geocoder = new Geocoder(getContext());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                fullAddress = new StringBuilder();
                                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                    fullAddress.append(address.getAddressLine(i)).append(getString(R.string.newline));
                                }

                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                                Marker marker = gMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality() + getString(R.string.dhairya)));
                                marker.showInfoWindow();

                                //Changing the textview
                                textView.setText(fullAddress);

                                //Creating a snackbar
                                Snackbar snackbar = Snackbar.make(view, fullAddress, Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction(getString(R.string.dismiss), view1 -> snackbar.dismiss());
                                snackbar.show();

                                //Notification
                                showNotification();
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
            });
        }

        //Creating notification channel
        NotificationChannelCompat notificationChannelCompat = new NotificationChannelCompat.Builder("Location Notification Channel", NotificationManagerCompat.IMPORTANCE_HIGH)
                .setName("Location notification")
                .setDescription("Notifications for the selected location on Google Map")
                .setVibrationEnabled(true)
                .build();
        managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.createNotificationChannel(notificationChannelCompat);

        //Setting up notifications permissions launcher
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {
                if (o) {
                    showNotification();
                } else {
                    Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PermissionChecker.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "Location Notification Channel")
                    .setSmallIcon(R.drawable.location_pin)
                    .setContentTitle("Address change")
                    .setContentText(fullAddress);
            managerCompat.notify(1, builder.build());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }
}