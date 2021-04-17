package in.iot.lab.dmscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.libdmtx.DMTXImage;
import org.libdmtx.DMTXTag;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.IntBuffer;

import in.iot.lab.dmscanner.databinding.ActivityMainBinding;

public class MainActivity2 extends AppCompatActivity {

    private static final int REQ_CODE_PICK_IMAGE = 0;
    private static final int REQ_CODE_CAMERA = 1;
    private ActivityMainBinding mainBinding;
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mCodeScanner = new CodeScanner(this, mainBinding.scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
//            ScanResultDialog dialog = new ScanResultDialog(this, result);
//            dialog.setOnDismissListener(d -> mCodeScanner.startPreview());
//            dialog.show();
//            Toast.makeText(this," "+result.getText(),Toast.LENGTH_LONG).show();
            mainBinding.textView.setText(result.getText());
//            mCodeScanner.startPreview();
        }));
        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () -> Toast.makeText(this, "Error in scanning!!", Toast.LENGTH_LONG).show()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainBinding.textView.setOnClickListener(view -> {
            showChooser();
        });
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void showChooser() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("*/*");
        photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(
                Intent.createChooser(photoPickerIntent, "Select a File to Upload"),
                REQ_CODE_PICK_IMAGE);
    }

    private void decode(Bitmap img) {
        System.out.println("DECODING");
        int width = img.getWidth();
        int height = img.getHeight();
        System.out.println(img.getWidth());
        System.out.println(img.getHeight());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(img, (int) (img.getWidth() * 0.5), (int) (img.getHeight() * 0.5), true);
        int midx = scaledBitmap.getWidth() / 2;
        int midy = scaledBitmap.getHeight() / 2;
//    	Bitmap cropImg = Bitmap.createBitmap(scaledBitmap, midx - width / 2, midy - height/2, width, height);
        int size = scaledBitmap.getHeight() * scaledBitmap.getWidth();
        IntBuffer buff = IntBuffer.allocate(size);
        scaledBitmap.copyPixelsToBuffer(buff);
        DMTXImage dmtxImage = new DMTXImage(scaledBitmap.getWidth(), scaledBitmap.getHeight(), buff.array());
        DMTXTag[] tags = dmtxImage.getTags(5, 10000);
        System.out.println(tags.length);
        StringBuilder sb = new StringBuilder();
        sb.append(tags.length + " tags found\n");
        for (DMTXTag tag : tags) {
            sb.append(tag.id + "\n");
            System.out.println(tag.id);
        }
        String text = sb.toString();
        TextView textView = findViewById(R.id.textView);
        textView.setText(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = intent.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    decode(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == REQ_CODE_CAMERA) {
            Bitmap img = (Bitmap) intent.getExtras().get("data");
            decode(img);
        }
    }
}