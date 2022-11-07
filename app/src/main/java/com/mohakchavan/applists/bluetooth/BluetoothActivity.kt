package com.mohakchavan.applists.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.coroutineScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.mohakchavan.applists.databinding.ActivityBluetoothBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BluetoothActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBluetoothBinding
    private var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
    }
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var bluetoothManager: BluetoothManager
    private var bluetoothAdapter: BluetoothAdapter? = null

    private val enableResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult?) {
                Log.e(TAG, "enableResult -> onActivityResult: ${result?.resultCode}")
                if (result?.resultCode == Activity.RESULT_OK) {
                    scanBluetoothDevices()
                }
            }
        }
    )

    private val locationResult: ActivityResultLauncher<IntentSenderRequest> = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
        object : ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult?) {
                Log.e(TAG, "locationResult -> onActivityResult: ${result?.resultCode}")
                if (result?.resultCode == Activity.RESULT_OK) {
                    scanBluetoothDevices()
                }
            }
        }
    )

    private val discoverableResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult?) {
                Log.e(TAG, "discoverableResult -> onActivityResult: ${result?.resultCode}")
                if (result?.resultCode == Activity.RESULT_OK) {
                    scanBluetoothDevices()
                }
            }
        }
    )

    private fun discoverable() {
        discoverableResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300))
    }

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let { intent ->
                Log.e(TAG, "onReceive: action: ${intent.action}")
                intent.action?.let { action ->
                    if (action == BluetoothAdapter.ACTION_DISCOVERY_STARTED) {

                    } else if (action == BluetoothDevice.ACTION_FOUND) {
                        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                                this@BluetoothActivity,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return
                        }
                        Log.e(TAG, "deviceName: ${device?.name} , deviceMAC: ${device?.address}")
                    } else if (action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {

                    } else if (action == BluetoothDevice.ACTION_ACL_CONNECTED) {

                    } else if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                        Log.e(TAG, "onReceive: ${intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)}")
                    } else if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                        Log.e(TAG, "onReceive: ${intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1)}")
                        if (intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1) == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                            scanBluetoothDevices()
                        } else {
                        }
                    } else {

                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hasPermissions(this@BluetoothActivity, *permissions)

        val filters = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filters.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filters.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filters.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        filters.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filters.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        registerReceiver(bluetoothReceiver, filters)
        binding.blScan.setOnClickListener {
            if (hasPermissions(this@BluetoothActivity, *permissions)) {
                enableBluetooth()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothReceiver)
        Log.e(TAG, "onDestroy: Receiver Unregistered")
    }

    private fun scanBluetoothDevices() {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "scanBluetoothDevices: Location not Enabled.")
            enableGPS(locationManager)
            return
        }

        Log.e(TAG, "scanBluetoothDevices: Bluetooth Enabled. Scanning for devices...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        lifecycle.coroutineScope.launch(Dispatchers.Default) {
            if (bluetoothAdapter?.isDiscovering == true) {
                bluetoothAdapter?.cancelDiscovery()
            }
            val disStarted = bluetoothAdapter?.startDiscovery()
            Log.e(TAG, "scanBluetoothDevices: Discovery Started: $disStarted")

            val devices = bluetoothAdapter?.bondedDevices
            devices?.forEach {
                Log.e(TAG, "scanBluetoothDevices: Bonded Device Name: ${it?.name} , Bonded Device MAC: ${it?.address}")
            }
        }
    }

    private fun enableGPS(locationManager: LocationManager) {
        val settingsClient = LocationServices.getSettingsClient(this@BluetoothActivity)
        val locReq = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10 * 1000
            fastestInterval = 2 * 1000
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locReq)
        val locSetReq = builder.build()
        builder.setAlwaysShow(true)
        settingsClient.checkLocationSettings(locSetReq)
            .addOnSuccessListener {
                Log.e(TAG, "enableGPS: OnSuccess")
//                scanBluetoothDevices()
            }
            .addOnFailureListener {
                Log.e(TAG, "enableGPS: OnFailure")
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
//                            (it as ResolvableApiException).startResolutionForResult(this@BluetoothActivity, 1234)
//                            locationResult.launch(IntentSenderRequest.Builder((it as ResolvableApiException).resolution).build())
                            it.status.resolution?.let { it1 -> locationResult.launch(IntentSenderRequest.Builder(it1).build()) } ?: throw Exception()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    else -> {
                        it.printStackTrace()
                    }
                }
            }
            .addOnCompleteListener {
                Log.e(TAG, "enableGPS: OnComplete")
//                scanBluetoothDevices()
            }
    }

    private fun enableBluetooth() {
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothAdapter?.let {
            if (!it.isEnabled)
                Log.e(TAG, "enableBluetooth: Enabling Bluetooth...")
            enableResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } ?: run {
            Log.e(TAG, "enableBluetooth: Does not supports Bluetooth")
            Toast.makeText(this@BluetoothActivity, "Does not supports Bluetooth", Toast.LENGTH_SHORT).show()
        }

    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        val listPermissionNeeded: MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                val permissionResults = HashMap<String, Int>()
                var deniedcount = 0
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionResults[permissions[i]] = grantResults[i]
                        deniedcount++
                    }
                    i++
                }
                if (deniedcount > 0) {
                    Toast.makeText(this@BluetoothActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    /**
                     * Registering/Establishing Posiflex usb device connection
                     */
//                    registerUsbReceiver()
//                    scanDevices()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val TAG = "BluetoothActivity"
    }
}