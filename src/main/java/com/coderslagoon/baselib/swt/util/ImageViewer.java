package com.coderslagoon.baselib.swt.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

import com.coderslagoon.baselib.imaging.Resizer;
import com.coderslagoon.baselib.imaging.Resolution;

public class ImageViewer {
    public static java.awt.Color COLOR_ACTIVE  = java.awt.Color.BLACK;
    public static java.awt.Color COLOR_PASSIVE = java.awt.Color.LIGHT_GRAY;

    boolean         active;
    boolean         buffered;
    Image           img;
    BufferedImage   bimg;
    File            file;
    Canvas          canvas;
    java.awt.Color  bgColor;

    public ImageViewer(Canvas canvas, boolean buffered, boolean active) {
        this.active   = active;
        this.buffered = buffered;
        this.canvas   = canvas;
        setBackgroundColor(active ? COLOR_ACTIVE : COLOR_PASSIVE, false);
        this.canvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent pe) {
                Rectangle rc = ImageViewer.this.canvas.getClientArea();

                if (null != ImageViewer.this.file) {
                    int w, h;
                    if (null == ImageViewer.this.img) {
                        w = h = -1;
                    }
                    else {
                        Rectangle ri = ImageViewer.this.img.getBounds();
                        w = ri.width;
                        h = ri.height;
                    }

                    if (rc.width  != w ||
                        rc.height != h) {
                        ImageViewer.this.img = null;
                        ImageData idata = load(rc.width, rc.height);
                        if (null != idata) {
                            ImageViewer.this.img = new Image(pe.gc.getDevice(), idata);
                        }
                    }
                }
                // FIXME: could we just partially draw what's really necessary?
                if ( ImageViewer.this.img == null ||
                    !ImageViewer.this.active) {
                    pe.gc.setBackground(new Color(
                            pe.gc.getDevice(),
                            ImageViewer.this.bgColor.getRed(),
                            ImageViewer.this.bgColor.getGreen(),
                            ImageViewer.this.bgColor.getBlue()));

                    pe.gc.fillRectangle(0, 0, rc.width, rc.height);
                }
                else {
                    pe.gc.drawImage(ImageViewer.this.img, 0, 0);
                }

            }
        });
    }

    public Canvas canvas() {
        return this.canvas;
    }

    public void setActive(boolean active) {
        if (!(this.active = active)) {
            setFile(null);
        }
        setBackgroundColor(active ? COLOR_ACTIVE : COLOR_PASSIVE, true);
    }

    public boolean active() {
        return this.active;
    }

    public void setFile(File file) {
        this.file = file;
        this.img = null;
        this.bimg = null;
        this.canvas.redraw();
    }

    public File file() {
        return this.file;
    }

    void setBackgroundColor(java.awt.Color bgColor, boolean redraw) {
        this.bgColor = bgColor;
        if (redraw) {
            this.canvas.redraw();
        }
    }

    ImageData load(int width, int height) {
        FileInputStream fos = null;
        try {
            BufferedImage bimg;
            if (null == this.bimg || !this.buffered) {
                fos = new FileInputStream(this.file);
                try {
                    bimg = ImageIO.read(fos);
                }
                catch (IIOException iioe) {
                    bimg = null;
                }
                catch (NegativeArraySizeException nase) {
                    bimg = null;
                }
                catch (ArrayIndexOutOfBoundsException aioobe) {
                    bimg = null;
                }
                catch (Throwable uncaught) {
                    bimg = null;
                }
                if (this.buffered) {
                    this.bimg = bimg;
                }
                if (null == this.bimg) {
                    return null;
                }
            }
            else {
                bimg = this.bimg;
            }

            Resizer.Options ropts = new Resizer.Options();
            ropts.resolution = new Resolution(width, height);
            ropts.rotate = Resizer.Options.Rotate.OFF;
            ropts.backgroundColor = this.bgColor;

            Resizer rsz = new Resizer(ropts);
            bimg = rsz.resize(bimg);

            return Converter.bufferedImageToImageDataDirect(bimg);
        }
        catch (Throwable err) {
            // TODO: proper logging
            err.printStackTrace(System.err);
            return null;
        }
        finally {
            if (null != fos) {
                try {
                    fos.close();
                }
                catch (IOException ioe) {
                }
            }
        }
    }
}
