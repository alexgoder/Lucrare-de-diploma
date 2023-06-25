package com.example.Control_WIFI_Electronice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.trial1_1.R;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<WifiAddrItem> wifiAddrItems;
    public String messageFail="Nu s-a gasit nici o retea WiFi!";

    public void setWifiAddrItems(ArrayList<WifiAddrItem> wifiAddrItems) {
        this.wifiAddrItems.clear();
        this.wifiAddrItems = wifiAddrItems;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;


        public ViewHolder(final View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView =  view.findViewById(R.id.name);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView
     */
    public CustomAdapter(ArrayList<WifiAddrItem> dataSet) {
        wifiAddrItems = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wifi_addr_item, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.getTextView().setText(localDataSet[position]);
        String name=wifiAddrItems.get(position).getName();
        viewHolder.textView.setText(name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return wifiAddrItems.size();
    }
}
