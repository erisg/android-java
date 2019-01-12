package bog.skate.eris.goskate;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private Toolbar toolback;
    private GoogleMap mMap;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }


        // Add a marker in Sydney and move the camera
        LatLng bogota = new LatLng(4.668117, -74.100935);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, 11));


        // SkatePark

        // SanCris

        LatLng sancris = new LatLng(4.572621, -74.082905);
        mMap.addMarker(new MarkerOptions().position(sancris).title("San Cris").icon(BitmapDescriptorFactory.fromResource(R.drawable.skatepark)));

        // Alpes

        LatLng Alpes = new LatLng(4.551314, -74.084214);
        mMap.addMarker(new MarkerOptions().position(Alpes).title("Alpes SkatePark").icon(BitmapDescriptorFactory.fromResource(R.drawable.skatepark)));

        // Tunal

        LatLng Tunal = new LatLng(4.571334, -74.136492);
        mMap.addMarker(new MarkerOptions().position(Tunal).title("Tunal SkatePark").icon(BitmapDescriptorFactory.fromResource(R.drawable.skatepark)));

        // Palermo

        LatLng Palermo = new LatLng(4.541783, -74.110053);
        mMap.addMarker(new MarkerOptions().position(Palermo).title("PalermoSurf").icon(BitmapDescriptorFactory.fromResource(R.drawable.skatepark)));

        // La Francia

        LatLng Francia = new LatLng(4.625043, -74.108268);
        mMap.addMarker(new MarkerOptions().position(Francia).title("La Francia SkatePark").icon(BitmapDescriptorFactory.fromResource(R.drawable.skatepark)));

        // Guavio

        LatLng Guavio = new LatLng(4.589332, -74.071510);
        mMap.addMarker(new MarkerOptions().position(Guavio).title("Guavio").icon(BitmapDescriptorFactory.fromResource(R.drawable.skatepark)));


        // Villas de granada

        LatLng Villas = new LatLng(4.715945, -74.122811);
        mMap.addMarker(new MarkerOptions().position(Villas).title("Villas").icon(BitmapDescriptorFactory.fromResource(R.drawable.skatepark)));


        //PISO WHITE

        //japon

        LatLng Japon = new LatLng(4.620252, -74.154743);
        mMap.addMarker(new MarkerOptions().position(Japon).title("Japon").icon(BitmapDescriptorFactory.fromResource(R.drawable.street)));

        //Ricaurte

        LatLng Ricaurte = new LatLng(4.613343, -74.154743);
        mMap.addMarker(new MarkerOptions().position(Ricaurte).title("Ricaurte").icon(BitmapDescriptorFactory.fromResource(R.drawable.street)));

        //Vertical

        LatLng Chorro = new LatLng(4.597288, -74.068543);
        mMap.addMarker(new MarkerOptions().position(Chorro).title("Chorro").icon(BitmapDescriptorFactory.fromResource(R.drawable.vertical)));


        // Cedritos
        LatLng Cedritos = new LatLng(4.723793, -74.033032);
        mMap.addMarker(new MarkerOptions().position(Cedritos).title("Cedritos").icon(BitmapDescriptorFactory.fromResource(R.drawable.vertical)));

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}