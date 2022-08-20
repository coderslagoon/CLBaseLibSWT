package com.coderslagoon.baselib.swt.dialogs;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.swt.util.ShellProps;

public abstract class Dialog extends Shell implements ShellListener {
    protected Properties props;
    protected Shell parentShell;

    public Dialog(Shell parent, Properties props, String propsPfx, int style) {
        super(parent, SWT.APPLICATION_MODAL | style);

        addShellListener(this);
        this.props = props;
        this.parentShell = parent;

        if (null != propsPfx && null != props) {
            shellProps(parent, propsPfx);
        }
    }

    protected void shellProps(Shell parent, String propsPfx) {
        new ShellProps(this,
                String.format("dlg.%s.", propsPfx),
                this.props,
                defSz(parent),
                fixedSz());
    }

    protected void checkSubclass() {
    }

    public void close() {
        if (!isDisposed()) {
            super.close();
        }
    }

    public void waitForClose() {
        Display d = getDisplay();
        while (!isDisposed()) {
            if (!d.readAndDispatch()) {
                d.sleep();
            }
        }
    }

    protected Point defSz(Shell parent) {
        int w = parent.getBounds().width;
        w -= w >> 3;
        return new Point(w, 0);
    }

    protected boolean fixedSz() {
        return false;
    }

    Menu disabledMenu;
    
    @Override
    public void open() {
		Menu mn = Display.getDefault().getMenuBar();
		if (null != mn && mn.getEnabled()) {
    		mn.setEnabled(false);
    		this.disabledMenu = mn;
		}
   		super.open();
    }

    public void shellActivated  (ShellEvent se) { }
    public void shellDeactivated(ShellEvent se) { }
    public void shellDeiconified(ShellEvent se) { }
    public void shellIconified  (ShellEvent se) { }
    
    public void shellClosed(ShellEvent se) {
    	if (null != this.disabledMenu) {
    		this.disabledMenu.setEnabled(true);
    		this.disabledMenu = null;
    	}    		
    }
}
