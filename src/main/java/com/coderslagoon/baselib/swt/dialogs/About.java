package com.coderslagoon.baselib.swt.dialogs;

import java.io.InputStream;
import java.util.Properties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.swt.util.CompositeWalker;

public class About extends Dialog {
    protected Point   defSz(Shell parent) { return new Point(400, 200); }
    protected boolean fixedSz() { return true; }

    public About(Shell parent, Properties props,
            String caption, String product, String intro, String info, InputStream icon, Color iconBkgr) {
        super(parent, props, "about", SWT.DIALOG_TRIM);
        setText(caption);

        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        setLayout(gridLayout);

        Composite left = new Composite(this, SWT.NONE);
        GridData data = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
        data.widthHint = 68;
        left.setLayoutData(data);
        left.setBackground(iconBkgr);

        Composite right = new Composite(this, SWT.NONE);
        right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        right.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

        gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth       =
        gridLayout.marginHeight      =
        gridLayout.horizontalSpacing =
        gridLayout.verticalSpacing   = 0;
        left.setLayout(gridLayout);

        Composite spc = new Composite(left, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        spc.setLayoutData(data);

        Composite pic = new Composite(left, SWT.NONE);
        Image ico = new Image(getDisplay(), new ImageLoader().load(icon)[0]);
        Image img = new Image(getDisplay(), ico.getImageData().width, ico.getImageData().height);
        GC gc = new GC(img);
        gc.drawImage(ico, 0, 0);
        gc.dispose();
        ico.dispose();
        pic.setBackgroundImage(img);
        data = new GridData (SWT.CENTER, SWT.CENTER, false, false);
        data.widthHint  = img.getBounds().width;
        data.heightHint = img.getBounds().height;
        pic.setLayoutData(data);

        spc = new Composite(left, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        spc.setLayoutData (data);

        for (Control c : left.getChildren()) {
            c.setBackground(left.getBackground());
        }

        gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth       =
        gridLayout.marginHeight      =
        gridLayout.horizontalSpacing =
        gridLayout.verticalSpacing   = 0;
        right.setLayout(gridLayout);

        spc = new Composite(right, SWT.NONE);
        spc.setLayoutData(new GridData (SWT.FILL, SWT.FILL, true, true));

        Label lbl = new Label(right, SWT.NONE);
        lbl.setText(product);
        lbl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        lbl.getFont().getFontData()[0].setHeight(14);
        FontData fntdat = lbl.getFont().getFontData()[0];
        fntdat.setHeight((int)(fntdat.getHeight() * 1.5));
        fntdat.setStyle(SWT.BOLD);
        lbl.setFont(new Font(lbl.getDisplay(), fntdat));
        lbl.pack();

        spc = new Composite(right, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.heightHint = lbl.getBounds().height;
        spc.setLayoutData(data);

        if (null != intro) {
            lbl = new Label(right, SWT.NONE);
            lbl.setText(intro);
            lbl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        }
        
        lbl = new Label(right, SWT.NONE);
        lbl.setText(info);
        lbl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        spc = new Composite(right, SWT.NONE);
        spc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        for (Control c : right.getChildren()) {
            c.setBackground(right.getBackground());
        }

        new CompositeWalker() {
            protected boolean onControl(Control ctl) {
                ctl.addMouseListener(new MouseListener() {
                    public void mouseDoubleClick(MouseEvent mevt) { }
                    public void mouseDown       (MouseEvent mevt) { }
                    public void mouseUp         (MouseEvent mevt) { close();}
                });
                return true;
            }
        }.walk(this);

        right.pack();
        gridLayout.marginWidth = lbl.getBounds().height;
        pack();
    }
}
