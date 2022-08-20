package com.coderslagoon.baselib.swt.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.coderslagoon.baselib.swt.dialogs.MessageBox2;
import com.coderslagoon.baselib.util.MiscUtils;

public class SWTUtil {
    public final static long processEvents(Display display) {
        long result = 0;
        while (display.readAndDispatch()) {
            result++;
        }
        return display.isDisposed() ? -1L : result;
    }

    public static void adjustButtonSize(Button btn, double factor) {
        adjustControlSize(btn, factor);
    }

    public static void adjustControlSize(Control ctl, double factor) {
        GridData data = (GridData)ctl.getLayoutData();
        Rectangle r = ctl.getBounds();
        data.widthHint = r.width + (int)(r.height * factor);
    }

    public static void adjustFontSize(Control ctl, int delta) {
        FontData fd = ctl.getFont().getFontData()[0];
        fd.setHeight(fd.getHeight() + delta);
        ctl.setFont(new Font(ctl.getDisplay(), fd));
    }

    public static boolean shellExecute(String fpath) {
        int pos = fpath.lastIndexOf('.');
        if (-1 == pos || pos == fpath.length() - 1) {
            return false;
        }
        Program prg = Program.findProgram(fpath.substring(pos).toLowerCase());
        if (null == prg) {
            return false;
        }
        return prg.execute(fpath);
    }

    public static int getTableColumnIndex(Table tbl, TableColumn tc) {
        int result = 0;
        for (TableColumn tc2 : tbl.getColumns()) {
            if (tc == tc2) {
                return result;
            }
            result++;
        }
        return -1;
    }

    public static void msgboxError(Throwable err, Shell parent) {
        StringWriter sw = new StringWriter();
        err.printStackTrace(new PrintWriter(sw));
        String text = MiscUtils.limitString(sw.toString(), 1024, "...");
        String title = err.getMessage();
        title = null == title ? "" : title;
        MessageBox2.standard(parent, SWT.OK | SWT.ICON_ERROR, text, title);
    }

    public static File openFileFromResource(
        Class<?> clazz, String res, String name, boolean force) {
        try {
            File result = new File(System.getProperty("java.io.tmpdir"), name);
            boolean create = force || !result.exists();
            if (create) {
                InputStream is = clazz.getResourceAsStream(res);
                byte[] cnt = MiscUtils.readInputStream(is);
                MiscUtils.writeFile(result,  cnt);
            }
            if (!shellExecute(result.getAbsolutePath())) {
                if (create) {
                    result.delete();
                }
                return null;
            }
            return result;
        }
        catch (IOException ioe) {
            return null;
        }
    }

    public static Menu getMainMenu(Display display, Shell shell) {
        if (MiscUtils.underOSX()) {
            return display.getMenuBar();
        }
        Menu result = new Menu(shell, SWT.BAR);
        shell.setMenuBar(result);
        return result;
    }
}
