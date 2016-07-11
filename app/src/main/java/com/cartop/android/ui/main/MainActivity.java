package com.cartop.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cartop.android.R;
import com.cartop.android.core.api.ApiDelegate;
import com.cartop.android.core.api.ApiManager;
import com.cartop.android.core.models.Program;
import com.cartop.android.helpers.DeviceInfoHelper;
import com.cartop.android.helpers.PermissionHelper;
import com.cartop.android.ui.program.preview.ProgramActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_DEVICE_INFO = 1;

    private PermissionHelper permissionHelper;
    private DeviceInfoHelper deviceInfoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiManager.get().getDelegatesSet().addDelegate(TAG, apiDelegate);

        permissionHelper = new PermissionHelper(this);
        deviceInfoHelper = new DeviceInfoHelper();

//        permissionHelper.verifyPermission(PERMISSION_REQUEST_DEVICE_INFO, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionHelper.PermissionCallback() {
//            @Override
//            public void grantedAllPermissions(int requestCode, String[] permissions) {
//                deviceInfoHelper.init(MainActivity.this);
//                Log.d(TAG, "IMEI = " + deviceInfoHelper.getIMEI());
//                Log.d(TAG, "Phone number = " + deviceInfoHelper.getPhoneNumber());
//                Log.d(TAG, "Battery level = " + deviceInfoHelper.getBatteryLevel());
//            }
//
//            @Override
//            public void deniedPermissions(int requestCode, String[] permissions) {
//                Log.w(TAG, "deniedPermissions: " + Arrays.toString(permissions));
//            }
//        });

//        permissionHelper.verifyPermission(PERMISSION_REQUEST_DEVICE_INFO, new String[]{Manifest.permission.CALL_PHONE}, new PermissionHelper.PermissionCallback() {
//            @Override
//            public void grantedAllPermissions(int requestCode, String[] permissions) {
//                USSDHelper.call(MainActivity.this, "*111#");
//            }
//
//            @Override
//            public void deniedPermissions(int requestCode, String[] permissions) {
//                Log.w(TAG, "deniedPermissions: " + Arrays.toString(permissions));
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        ApiManager.get().getDelegatesSet().removeDelegate(apiDelegate);
        super.onDestroy();
    }

    public void startProgram(View view) {
        ApiManager.get().getProgram(TAG, ApiManager.API_REQUEST_DEFAULT, 1);
    }

    private ApiDelegate apiDelegate = new ApiDelegate.SimpleApiDelegate() {
        @Override
        public void onReceiveProgram(int requestCode, Program program) {
            Intent intent = new Intent(MainActivity.this, ProgramActivity.class);
            intent.putExtra(ProgramActivity.EXTRA_PROGRAM, program);
            startActivity(intent);
        }
    };
}