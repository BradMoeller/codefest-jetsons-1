package com.codefest_jetsons.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 12/4/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class QRCode extends View {
    private static final int DIMENSIONS_NOT_DEFINED = -1;
    private String dataValue = null;
    int pixelWidth = DIMENSIONS_NOT_DEFINED;
    int pixelHeight = DIMENSIONS_NOT_DEFINED;

    public QRCode(Context context, String dataValue) {
        super(context);
        this.dataValue = dataValue;
    }

    public QRCode(Context context, String dataValue, int pixelWidth, int pixelHeight) {
        this(context, dataValue);
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        try {
            generateBarcode(canvas);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((pixelWidth != DIMENSIONS_NOT_DEFINED) ? pixelWidth : 200,
                (pixelHeight != DIMENSIONS_NOT_DEFINED) ? pixelHeight : 50);
    }

    public void generateBarcode(Canvas canvas) throws Exception {
        com.google.zxing.qrcode.QRCodeWriter write = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = write.encode((dataValue != null) ? dataValue : "123321", BarcodeFormat.QR_CODE,
                    (pixelWidth != DIMENSIONS_NOT_DEFINED) ? pixelWidth : 200,
                    (pixelHeight != DIMENSIONS_NOT_DEFINED) ? pixelHeight : 50, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = null;
        if(bitMatrix != null) {
            bitmap = Bitmap.createBitmap((pixelWidth != DIMENSIONS_NOT_DEFINED) ? pixelWidth : 200,
                    (pixelHeight != DIMENSIONS_NOT_DEFINED) ? pixelHeight : 50, Bitmap.Config.ARGB_8888);

            for(int i = 0; i < bitMatrix.getWidth(); i++) {
                for(int j = 0; j < bitMatrix.getHeight(); j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.TRANSPARENT);
                }
            }
        }

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

}
