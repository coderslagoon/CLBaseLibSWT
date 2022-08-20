package com.coderslagoon.baselib.swt.util;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class Converter {
    private Converter() {
    }

    public static ImageData bufferedImageToImageDataDirect(
            BufferedImage bimg) {
        ImageData result;
        PaletteData palette;

        if (bimg.getColorModel() instanceof DirectColorModel dcm) {

            palette = new PaletteData(
                    dcm.getRedMask(),
                    dcm.getGreenMask(),
                    dcm.getBlueMask());

            result = new ImageData(
                    bimg.getWidth(),
                    bimg.getHeight(),
                    dcm.getPixelSize(),
                    palette);

        }
        else if (bimg.getColorModel() instanceof ComponentColorModel) {
            palette = new PaletteData(
                    0x00ff0000,     // FIXME: confirm that this is always OK
                    0x0000ff00,
                    0x000000ff);

            result = new ImageData(
                    bimg.getWidth(),
                    bimg.getHeight(),
                    bimg.getColorModel().getPixelSize(),
                    palette);
        }
        else {
            return null;
        }

        WritableRaster raster = bimg.getRaster();

        int[] pixelArray = new int[3];

        RGB rgb = new RGB(0, 0, 0);

        int h = result.height;
        int w = result.width;

        for (int y = 0; y < h; y++) {
            // TODO: unroll that thing smartly
            for (int x = 0; x < w; x++) {
                raster.getPixel(x, y, pixelArray);

                // TODO: do the shifting by ourselves
                rgb.red   = pixelArray[0];
                rgb.green = pixelArray[1];
                rgb.blue  = pixelArray[2];

                int pixel = palette.getPixel(rgb);

                result.setPixel(x, y, pixel);
            }
        }

        return result;
    }
}
