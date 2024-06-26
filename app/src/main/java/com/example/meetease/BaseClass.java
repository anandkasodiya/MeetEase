package com.example.meetease;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class BaseClass extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int CAMERA_PERMISSION_REQUEST = 101;
    private static final int MAX_PERMISSION_ATTEMPTS = 4;
    private int permissionAttempts = 0;

    public boolean checkAndRequestPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, request both camera and media images permissions
            boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            boolean mediaImagesPermissionGranted = ContextCompat.checkSelfPermission(context, "android.permission.READ_MEDIA_IMAGES") == PackageManager.PERMISSION_GRANTED;

            if (!cameraPermissionGranted || !mediaImagesPermissionGranted) {
                if (permissionAttempts < MAX_PERMISSION_ATTEMPTS) {
                    String[] permissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES // New permission for media images
                    };
                    ActivityCompat.requestPermissions((Activity) context, permissions, CAMERA_PERMISSION_REQUEST);
                    permissionAttempts++;
                } else {
                    // User has denied the permissions three times. Guide them to the app manager.
                    Toast.makeText(context, "Please grant permissions from the app manager.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        } else {
            // For Android versions less than or equal to Android 12, request camera and the traditional read external storage permission
            if (permissionAttempts < MAX_PERMISSION_ATTEMPTS) {
                String[] permissions = new String[]{android.Manifest.permission.CAMERA, // New permission for media images
                        android.Manifest.permission.READ_EXTERNAL_STORAGE // Traditional permission for external storage
                };
                ActivityCompat.requestPermissions((Activity) context, permissions, CAMERA_PERMISSION_REQUEST);
                permissionAttempts++;
                return false;
            } else {
                // User has denied the permissions three times. Guide them to the app info.
                Toast.makeText(context, "Please grant permissions from the app info.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void openImageDialog(Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Select Image Source");
        dialogBuilder.setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera();
                    break;
                case 1:
                    openGallery();
                    break;
            }
        });
        dialogBuilder.create().show();
    }

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_IMAGE_GALLERY);
    }

    public Uri bitmapToUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);

        return Uri.parse(path);
    }
}
