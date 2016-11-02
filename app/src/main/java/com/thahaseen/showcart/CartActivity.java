package com.thahaseen.showcart;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView listViewCart;
    TextView txtLocation;
    ArrayList<Product> lstProductsInCart = new ArrayList<>();
    ProductListAdapter productListAdapter;
    LocationManager mLocationManager;
    String provider;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            txtLocation.setText(String.format(getString(R.string.latitude_longitude),location.getLatitude(),location.getLongitude()));
        }
        @Override
        public void onProviderDisabled(String provider) {

        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listViewCart = (ListView) findViewById(R.id.listViewCart);
        txtLocation = (TextView) findViewById(R.id.txtLocationCoordinates);
        lstProductsInCart = getIntent().getParcelableArrayListExtra("cartList");
        productListAdapter = new ProductListAdapter(CartActivity.this, lstProductsInCart);
        listViewCart.setAdapter(productListAdapter);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = mLocationManager.getBestProvider(criteria, true);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission", "Permission is granted");
                getLocation();
            }else{
                Log.v("Permission","Permission is not granted");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else {
            getLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                getLocation();
        }else {
            Toast.makeText(CartActivity.this, getString(R.string.GPS_No_Permission_Error), Toast.LENGTH_LONG).show();
            txtLocation.setText(getString(R.string.No_location_detected));
        }
    }

    private void getLocation(){
        try {
            mLocationManager.requestLocationUpdates(provider, 2000, 0, mLocationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }


    class ProductListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Product> lstProducts;

        public ProductListAdapter(Context context, ArrayList<Product> items) {
            this.context = context;
            this.lstProducts = items;
        }

        @Override
        public int getCount() {
            return lstProducts.size();
        }

        @Override
        public Object getItem(int position) {
            return lstProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ProductViewHolder {
            TextView txtId, txtProdName, txtProdPrice;
            Button btnAddCart;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ProductViewHolder productViewHolder;

            if(convertView == null) {
                // inflate prod_row xml
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(android.app.Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.prod_row, null);
                // hook up UI to viewHolder
                productViewHolder = new ProductViewHolder();
                productViewHolder.txtId = (TextView) convertView.findViewById(R.id.txtId);
                productViewHolder.txtProdName = (TextView) convertView.findViewById(R.id.txtProdName);
                productViewHolder.txtProdPrice = (TextView) convertView.findViewById(R.id.txtProdPrice);
                productViewHolder.btnAddCart = (Button) convertView.findViewById(R.id.btnAddCart);
                convertView.setTag(productViewHolder);
            }else{
                productViewHolder = (ProductViewHolder) convertView.getTag();
            }

            final Product product = lstProducts.get(position);

            //set values
            productViewHolder.txtId.setText(String.valueOf(product.getId()));
            productViewHolder.txtProdName.setText(product.getName());
            productViewHolder.txtProdPrice.setText(String.format(getString(R.string.SAR_xxx), product.getPrice()));
            productViewHolder.btnAddCart.setText(getString(R.string.delete_from_cart));

            // click listener to handle delete product from cart
            productViewHolder.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lstProductsInCart.size() > 0 && lstProducts.contains(product)){
                        lstProductsInCart.remove(product);
                        productListAdapter.notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }

    }
}
