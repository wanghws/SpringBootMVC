package com.demo.api.commons.utils;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QRCodeUtil {


    // 二维码背景颜色
    private static final int PINK = 0xFFFF0067;
    // 二维码前景颜色
    private static final int WHITE = 0xFFFFFFFF;
    // 二维码图片格式
    private static final String IMAGE_TYPE = "png";
    private static final String CHAR_SET = "UTF-8";
    private static final int DEFAULT_SIZE = 462;
    private static final int DEFAULT_MARGIN = 0;


    public static void encode(String content, int size, int margin, OutputStream outputStream) throws Exception {
        //获取二维码流的形式并写入到输出流
        BufferedImage image = getBufferedImage(content, margin, size);
        if(image == null){
            return;
        }
        ImageIO.write(image, IMAGE_TYPE, outputStream);
    }

    /**
     * 二维码流的形式，包含文本内容
     * @param content 二维码文本内容
     * @param size 二维码尺寸
     * @return
     */
    public static BufferedImage getBufferedImage (String content, int size, int margin) throws WriterException {
        if (size <= 0 || size >= 1000){
            size = DEFAULT_SIZE;
        }

        if (margin < 0 || margin >= 100){
            margin = DEFAULT_MARGIN;
        }

        // 设置编码字符集
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        //设置编码
        hints.put(EncodeHintType.CHARACTER_SET, CHAR_SET);
        //设置容错率最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, margin);
        // 1、生成二维码
//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, size, size, hints);

        // 2、获取二维码宽高
        int codeWidth = bitMatrix.getWidth();
        int codeHeight = bitMatrix.getHeight();

        // 3、将二维码放入缓冲流
        BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < codeWidth; i++) {
            for (int j = 0; j < codeHeight; j++) {
                // 4、循环将二维码内容定入图片
                image.setRGB(i, j, bitMatrix.get(i, j) ? PINK : WHITE);
            }
        }
        return image;

    }
}
