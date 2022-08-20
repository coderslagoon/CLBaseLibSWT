package com.coderslagoon.baselib.swt.util;

import java.util.Properties;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.util.Prp;

public class ShellProps {
    Shell      shell;
    Properties props;

    Point lastLocation;
    Point lastSize;

    final Prp.Int WIDTH;
    final Prp.Int HEIGHT;
    final Prp.Int TOP;
    final Prp.Int LEFT;
    final Prp.Int STYLE;

    public ShellProps(Shell shell, String prefix, Properties props, Point defsz, boolean fixed) {
        this.shell = shell;
        this.props = props;
        final int w, h;
        if (fixed) {
            this.WIDTH  =
            this.HEIGHT = null;
            w = defsz.x;
            h = defsz.y;
        }
        else {
            this.WIDTH  = new Prp.Int(prefix + "width" , null == defsz || -1 == defsz.x ? shell.getSize().x : defsz.x);
            this.HEIGHT = new Prp.Int(prefix + "height", null == defsz || -1 == defsz.y ? shell.getSize().y : defsz.y);
            w = this.WIDTH .get(this.props);
            h = this.HEIGHT.get(this.props);
        }
        this.TOP   = new Prp.Int(prefix + "top"  , -1);
        this.LEFT  = new Prp.Int(prefix + "left" , -1);
        this.STYLE = new Prp.Int(prefix + "style", 0);
        this.shell.setSize(this.lastSize = new Point(w, h));
        Rectangle ca = shell.getDisplay().getPrimaryMonitor().getClientArea();
        int scr_w = ca.width;
        int scr_h = ca.height;
        int x = this.LEFT.get(this.props); if (-1 == x) x = (scr_w - w) >> 1;
        int y = this.TOP .get(this.props); if (-1 == y) y = (scr_h - h) >> 1;
        this.shell.setLocation(this.lastLocation = new Point(x, y));
        this.shell.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                if (!ShellProps.this.shell.getMinimized() &&
                    !ShellProps.this.shell.getMaximized()) {
                    ShellProps.this.lastLocation = ShellProps.this.shell.getLocation();
                }
            }
            public void controlResized(ControlEvent e) {
                if (!ShellProps.this.shell.getMinimized() &&
                    !ShellProps.this.shell.getMaximized()) {
                    ShellProps.this.lastSize = ShellProps.this.shell.getSize();
                }
            }
        });
        this.shell.addShellListener(new ShellListener() {
            public void shellActivated  (ShellEvent e) { }
            public void shellDeactivated(ShellEvent e) { }
            public void shellDeiconified(ShellEvent e) { }
            public void shellIconified  (ShellEvent e) { }
            public void shellClosed(ShellEvent e) {
                ShellProps.this.store();
            }
        });
        switch (this.STYLE.get(this.props)) {
            case 1 : this.shell.setMinimized(true); break;
            case 2 : this.shell.setMaximized(true); break;
        }
    }

    protected void store() {
        this.LEFT  .set(this.props, this.lastLocation.x);
        this.TOP   .set(this.props, this.lastLocation.y);
        this.STYLE .set(this.props, this.shell.getMaximized() ? 2 :
                                    this.shell.getMinimized() ? 1 : 0);
        if (null != this.WIDTH ) this.WIDTH .set(this.props, this.lastSize.x);
        if (null != this.HEIGHT) this.HEIGHT.set(this.props, this.lastSize.y);
    }
}
