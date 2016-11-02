package com.thahaseen.showcart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listViewProducts;
    Button btnShowCart;
    ArrayList<Product> lstProductsInCart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewProducts = (ListView) findViewById(R.id.listViewProducts);
        btnShowCart = (Button) findViewById(R.id.btnShowCart);
        // bind the products to list view
        ProductListAdapter productListAdapter = new ProductListAdapter(MainActivity.this, getProducts());
        listViewProducts.setAdapter(productListAdapter);
        // handle show cart click event
        btnShowCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                intent.putParcelableArrayListExtra("cartList", lstProductsInCart);
                startActivity(intent);
            }
        });
    }

    // Return some manual Product data
    private ArrayList<Product> getProducts(){
        ArrayList<Product> lstProducts = new ArrayList<>();
        lstProducts.add(new Product(14,"Google Nexus 5X","2300"));
        lstProducts.add(new Product(19,"Lenevo Note 3","1900"));
        lstProducts.add(new Product(112,"iBall Mouse","150"));
        lstProducts.add(new Product(10,"Dell Inspiron Laptop","9999"));
        lstProducts.add(new Product(99,"Asus Tablet 7 Inch","1900"));
        lstProducts.add(new Product(67,"Moto G4 Plus","890"));
        return lstProducts;
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
            productViewHolder.btnAddCart.setText(getString(R.string.add_to_cart));

            // click listener to handle add product to cart
            productViewHolder.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lstProductsInCart.size() > 0 && lstProductsInCart.contains(product)){
                        Toast.makeText(context, getString(R.string.product_already_added_to_cart), Toast.LENGTH_SHORT).show();
                    }else{
                        lstProductsInCart.add(product);
                    }
                }
            });

            return convertView;
        }

    }
}
