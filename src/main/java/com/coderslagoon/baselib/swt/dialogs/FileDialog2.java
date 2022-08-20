package com.coderslagoon.baselib.swt.dialogs;

import java.io.File;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.util.MiscUtils;
import com.coderslagoon.baselib.util.Prp;

public class FileDialog2 extends FileDialog {
    final Properties props;
    final Prp.Str lastPath;
    final Prp.Int lastFilterIndex;

    public FileDialog2(Shell parent, int style, Properties props, String pfx) {
        super(parent, SWT.APPLICATION_MODAL | style);
        this.props = props;
        pfx += '.';
        final String PFX = "filedialog2.";
        this.lastPath        = new Prp.Str(PFX + pfx + "lastpath"     , "");
        this.lastFilterIndex = new Prp.Int(PFX + pfx + "lastfilteridx", 0);
    }

    @Override
    public String open() {
        setFilterPath(this.lastPath.get(this.props));
        int fidx = Math.max(0, getFilterNames().length - 1);
            fidx = Math.min(this.lastFilterIndex.get(this.props), fidx);
        setFilterIndex(fidx);
        String result = super.open();
        if (null != result) {
            this.lastFilterIndex.set(this.props, getFilterIndex());
            this.lastPath       .set(this.props,
                    new File(result).getParentFile().getAbsolutePath());
        }
        return result;
    }

    public String[] getFilePaths() {
        String fp = getFilterPath();
        if (null == fp) {
            return new String[0];
        }
        String[] result = getFileNames();
        File dir = new File(fp);
        for (int i = 0; i < result.length; i++) {
            result[i] = new File(dir, result[i]).getAbsolutePath();
        }
        return result;
    }

    public final static String ALL_FILTER =
            MiscUtils.underWindows() ? "*.*" : "*";

    @Override
    protected void checkSubclass() {
    }
}
