package com.mvochoa.PrintBluetooth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.YailList;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

@DesignerComponent(version = 1,
        description = "Imprimir texto plano con impresora bluetooth",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.BLUETOOTH")
public class PrinterBluetooth extends AndroidNonvisibleComponent {
    private BluetoothDevice[] bluetoothDevices;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    private final Activity activity;

    private OutputStream outputStream;
    private InputStream inputStream;

    private byte[] readBuffer;
    private int readBufferPosition;

    public PrinterBluetooth(ComponentContainer container) {
        super(container.$form());
        activity = container.$context();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @SimpleFunction(description = "Ejecuta una busqueda de dispositivos bluetooth y llama al evento ListPrinter")
    public void SearchPrinter() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(activity, "Adapter bluetooth is shutdown", Toast.LENGTH_SHORT).show();
            }

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                form.startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            int i = 0;
            bluetoothDevices = new BluetoothDevice[pairedDevices.size()];
            Object[] namesDevices = new Object[pairedDevices.size()];


            for (BluetoothDevice device : pairedDevices) {
                namesDevices[i] = device.getName();
                bluetoothDevices[i] = device;
                i++;
            }

            ListPrinter(YailList.makeList(namesDevices));
        } catch (Exception ex) {
            form.dispatchErrorOccurredEvent(this, "SearchPrinter", ErrorMessages.ERROR_BLUETOOTH_COULD_NOT_FIT_NUMBER_IN_BYTE, ex.getMessage());
        }
    }

    @SimpleEvent
    public void ListPrinter(YailList printers) {
        EventDispatcher.dispatchEvent(this, "ListPrinter", printers);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @SimpleFunction(description = "Enviar imprimir información, es necesario que se envie el")
    public void Print(String data, int indexPrinter) {
        try {

            if (indexPrinter < 1 || indexPrinter > bluetoothDevices.length) {
                Toast.makeText(activity, "Index printer not exist", Toast.LENGTH_SHORT).show();
                return;
            }

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothDevice = bluetoothDevices[indexPrinter-1];
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            final byte delimiter = 10;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            int bytesAvailable = inputStream.available();

            if (bytesAvailable > 0) {

                byte[] packetBytes = new byte[bytesAvailable];
                inputStream.read(packetBytes);

                for (int i = 0; i < bytesAvailable; i++) {

                    byte b = packetBytes[i];
                    if (b == delimiter) {

                        byte[] encodedBytes = new byte[readBufferPosition];
                        System.arraycopy(
                                readBuffer, 0,
                                encodedBytes, 0,
                                encodedBytes.length
                        );
                        readBufferPosition = 0;
                    } else {
                        readBuffer[readBufferPosition++] = b;
                    }
                }
            }

            outputStream.write(data.getBytes());
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
        } catch (Exception ex) {
            form.dispatchErrorOccurredEvent(this, "Print", ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_CONNECT, ex.getMessage());
        }
    }


}