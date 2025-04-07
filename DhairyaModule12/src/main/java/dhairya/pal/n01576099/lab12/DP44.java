package dhairya.pal.n01576099.lab12;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DP44 extends Fragment {
    private ActivityResultLauncher<String> permissionLauncher;
    BroadcastReceiver deliveredReceiver;
    BroadcastReceiver sentReceiver;

    public DP44() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_d_p44, container, false);

        Button button = view.findViewById(R.id.dhaSendSmsButton);
        EditText editTextNumber = view.findViewById(R.id.dhaNumberEditText);
        EditText editTextMessage = view.findViewById(R.id.dhaSmsEditText);

        //Setting up permissions launcher for sending SMS
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), o -> {
            if (o) {
                sendSms(editTextNumber.getText().toString(), editTextMessage.getText().toString());
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        //Setting up sent and delivered receivers
        deliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), "SMS delivered", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getContext(), "SMS not delivered", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        sentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), "SMS sent", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getContext(), "Generic failure", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getContext(), "No service", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getContext(), "Null PDU", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getContext(), "Radio off", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        button.setOnClickListener(view1 -> {
            if (editTextNumber.getText().toString().isEmpty()) {
                editTextNumber.setError(getString(R.string.can_not_be_empty));
            } else if (editTextNumber.getText().toString().length() != 10) {
                editTextNumber.setError(getString(R.string.must_be_ten_digits));
            } else if (editTextMessage.getText().toString().isEmpty()) {
                editTextMessage.setError(getString(R.string.can_not_be_empty));
            } else {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendSms(editTextNumber.getText().toString(), editTextMessage.getText().toString());
                } else {
                    permissionLauncher.launch(Manifest.permission.SEND_SMS);
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void sendSms(String phoneNumber, String message) {
        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getContext(), 0, new Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE);

        requireActivity().registerReceiver(sentReceiver, new IntentFilter("SMS_SENT"), Context.RECEIVER_NOT_EXPORTED);
        requireActivity().registerReceiver(deliveredReceiver, new IntentFilter("SMS_DELIVERED"), Context.RECEIVER_NOT_EXPORTED);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}