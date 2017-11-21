package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * 二维码生成工具类
 * @author Administrator
 *
 */
public class QRCodeUtil {
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    @SuppressWarnings("unchecked")
	private static BufferedImage createImage(String content) throws Exception {
        @SuppressWarnings("rawtypes")
		Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        return image;
    }
    public static byte [] encode(String content) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "JPG", output);
        byte [] b = output.toByteArray();
        output.close();
        return b;
    }
    public static void main(String[] args) throws Exception {
        String text = "你信用卡套现么，我要买了，9999元"; //logo位置
        byte [] b = QRCodeUtil.encode(text);
        FileOutputStream fos = new FileOutputStream("abc.jpg");
        fos.write(b);
        fos.close();
    }
}
