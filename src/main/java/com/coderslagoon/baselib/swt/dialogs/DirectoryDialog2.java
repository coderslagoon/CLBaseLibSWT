package com.coderslagoon.baselib.swt.dialogs;

import java.io.File;
import java.util.Properties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.util.Prp;

public class DirectoryDialog2 extends DirectoryDialog {
    final Properties props;
    final Prp.Str    lastPath;

    public DirectoryDialog2(Shell parent, int style, Properties props, String pfx) {
        super(parent, SWT.APPLICATION_MODAL | style);
        this.props = props;
        pfx += '.';
        final String PFX = "directorydialog2.";
        this.lastPath = new Prp.Str(PFX + pfx + "lastpath", "");
    }

    @Override
    public String open() {
        setFilterPath(this.lastPath.get(this.props));
        String result = super.open();
        if (null != result) {
            this.lastPath.set(this.props, new File(result).getAbsolutePath());
        }
        return result;
    }

    @Override
    protected void checkSubclass() {
    }
}
