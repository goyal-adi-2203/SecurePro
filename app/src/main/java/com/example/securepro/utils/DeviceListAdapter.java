package com.example.securepro.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securepro.R;
import com.example.securepro.domain.model.Device;
import com.example.securepro.utils.DeviceListAdapter.DeviceViewHolder;

import java.util.List;
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    class DeviceViewHolder extends RecyclerView.ViewHolder{

        private final ImageView deviceIcon;
        private final TextView deviceNameTextView;
        private final TextView deviceStatusTextView;
        private final Button enterPasswordButton;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceIcon = (ImageView) itemView.findViewById(R.id.device_icon);
            deviceNameTextView = (TextView) itemView.findViewById(R.id.device_name);
            deviceStatusTextView = (TextView) itemView.findViewById(R.id.device_status);
            enterPasswordButton = (Button) itemView.findViewById(R.id.enter_password_button);
        }
    }
    private LayoutInflater layoutInflater;
    private List<Device> data;
    private Context context;

    public DeviceListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
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
        if (data != null) {
            Device device = data.get(position);

            holder.deviceNameTextView.setText(device.getName());

            switch (device.getDeviceType().toLowerCase()){
                case "door":
                    holder.deviceIcon.setImageResource(R.drawable.ic_door);
                    break;
                case "window":
                    holder.deviceIcon.setImageResource(R.drawable.ic_window);
                    break;
                default:
                    holder.deviceIcon.setImageResource(R.drawable.ic_shield);
                    break;
            }

            holder.deviceStatusTextView.setText("Locked");
            holder.enterPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEnterPasswordDialog(device);
                }
            });
        } else {
            Toast t = Toast.makeText(context, "No data to display", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        return 0;
    }

    private void openEnterPasswordDialog(Device device){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Password");

        // Set up an EditText to input password
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredPassword = input.getText().toString();
            // Validate or use the entered password as needed

            Toast t = new Toast(context);
            t.setDuration(Toast.LENGTH_SHORT);

            if (enteredPassword.equals(device.getPassword())) {
                // Do something if password is correct
                t.setText("Correct password");
            } else {
                // Handle incorrect password
                t.setText("Incorrect password");
            }
            t.show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}

