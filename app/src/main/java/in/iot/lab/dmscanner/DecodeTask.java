package in.iot.lab.dmscanner;


import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;

import org.libdmtx.DMTXImage;
import org.libdmtx.DMTXTag;

import java.nio.IntBuffer;

final class DecodeTask {
    private final byte[] mImage;
    private final Point mImageSize;
    private final Point mPreviewSize;
    private final Point mViewSize;
    private final Rect mViewFrameRect;
    private final int mOrientation;
    private final boolean mReverseHorizontal;

    public DecodeTask(@NonNull final byte[] image, @NonNull final Point imageSize,
                      @NonNull final Point previewSize, @NonNull final Point viewSize,
                      @NonNull final Rect viewFrameRect, final int orientation,
                      final boolean reverseHorizontal) {
        mImage = image;
        mImageSize = imageSize;
        mPreviewSize = previewSize;
        mViewSize = viewSize;
        mViewFrameRect = viewFrameRect;
        mOrientation = orientation;
        mReverseHorizontal = reverseHorizontal;
    }

    @Nullable
    @SuppressWarnings("SuspiciousNameCombination")
    public Result decode(@NonNull final MultiFormatReader reader) throws ReaderException {
        int imageWidth = mImageSize.getX();
        int imageHeight = mImageSize.getY();
        final int orientation = mOrientation;
        final byte[] image = Utils.rotateYuv(mImage, imageWidth, imageHeight, orientation);
        if (orientation == 90 || orientation == 270) {
            final int width = imageWidth;
            imageWidth = imageHeight;
            imageHeight = width;
        }
        final Rect frameRect =
                Utils.getImageFrameRect(imageWidth, imageHeight, mViewFrameRect, mPreviewSize,
                        mViewSize);
        final int frameWidth = frameRect.getWidth();
        final int frameHeight = frameRect.getHeight();
        if (frameWidth < 1 || frameHeight < 1) {
            return null;
        }
        PlanarYUVLuminanceSource2 luminanceSource = new PlanarYUVLuminanceSource2(image, imageWidth, imageHeight, frameRect.getLeft(),
                frameRect.getTop(), frameWidth, frameHeight, mReverseHorizontal);
        try {

            Result result = Utils.decodeLuminanceSource(reader,
                    luminanceSource);
            return result;
        } catch (ReaderException e) {
            String decode = decode(luminanceSource.renderCroppedGreyscaleBitmap());
            if (decode != null) {
                return new Result(decode, decode.getBytes(), null, null);
            }
        }
        return null;
    }


    private String decode(Bitmap img) {
        System.out.println("DECODING");
        System.out.println(img.getWidth());
        System.out.println(img.getHeight());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(img, (int) (img.getWidth() * 0.5), (int) (img.getHeight() * 0.5), true);
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
        if (tags.length == 0)
            return null;
        return sb.toString();
    }
}