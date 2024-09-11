package com.example.securepro.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securepro.R;
import com.example.securepro.domain.model.Device;
import com.example.securepro.utils.DeviceListAdapter.DeviceViewHolder;

import java.util.List;
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    class DeviceViewHolder extends RecyclerView.ViewHolder{

        private final TextView dataText;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            dataText = itemView.findViewById(R.id.text_recyclerview_item);
        }
    }
    private LayoutInflater layoutInflater;
    private List<Device> data;

    public DeviceListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.device_list_item, parent, false);
        return new DeviceViewHolder(itemView);
    }

    public void setData(List<Device> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        if(data != null){
            Device current = data.get(position);
            holder.dataText.setText(current.getId() + " " + current.getName()+ " " + current.getDeviceType() + " " + current.getPassword());
        }
        else {
            holder.dataText.setText("No Text");
        }
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        return 0;
    }


}

