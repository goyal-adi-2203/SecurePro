package com.example.securepro.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securepro.R;
import com.example.securepro.callbacks.PasswordValidationCallback;
import com.example.securepro.controllers.DeviceController.DeviceController;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.model.User;
import com.example.securepro.presentation.login.UserViewModel;
import com.example.securepro.utils.DeviceListAdapter.DeviceViewHolder;

import java.util.List;
import java.util.Map;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    private User user;

    class DeviceViewHolder extends RecyclerView.ViewHolder{

        private final ImageView deviceIcon;
        private final TextView deviceIdTextView;
        private final TextView deviceNameTextView;
        private final TextView deviceStatusTextView;
        private final Button enterPasswordButton;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceIcon = (ImageView) itemView.findViewById(R.id.device_icon);
            deviceIdTextView = (TextView) itemView.findViewById(R.id.device_id);
            deviceNameTextView = (TextView) itemView.findViewById(R.id.device_name);
            deviceStatusTextView = (TextView) itemView.findViewById(R.id.device_status);
            enterPasswordButton = (Button) itemView.findViewById(R.id.enter_password_button);
        }
    }
    private LayoutInflater layoutInflater;
    private List<Device> data;
    private Context context;
    private String TAG = "adapterClass";

    public DeviceListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public DeviceListAdapter(Context context, User user){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.user = user;
    }

    public void setUser(User user){
        this.user = user;
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
            Log.d(TAG, "onBindViewHolder: data " + data.get(position).toString());
            Device device = data.get(position);

            holder.deviceIdTextView.setText(device.getId());
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

            holder.deviceStatusTextView.setText(device.getStatus());
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

//        Log.d(TAG, "openEnterPasswordDialog: " + user.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Password");

        // Set up an EditText to input password
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredPassword = input.getText().toString();

            new DeviceController().validatePassword(enteredPassword, device.getId(), user.getUserId(), context, new PasswordValidationCallback() {
                @Override
                public void onSuccess(Map<String, Object> responseData) {
                    Log.d(TAG, "openEnterPasswordDialog: " + responseData.toString());

                    boolean isValid = (boolean) responseData.get("check");
                    String message = (String) responseData.get("message");

                    Toast t = new Toast(context);
                    t.setDuration(Toast.LENGTH_SHORT);

                    if (isValid) {
                        t.setText("Correct password");
                    } else {
                        t.setText("Incorrect password");
                    }
                    t.show();
                }

                @Override
                public void onFailure(Map<String, Object> errorData) {
                    Log.e(TAG, "Password validation failed");
                    Toast.makeText(context, "Failed to validate password. Try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}

