package com.coderslagoon.baselib.swt.dialogs;

import java.util.Properties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.swt.NLS;
import com.coderslagoon.baselib.swt.util.SWTUtil;
import com.coderslagoon.baselib.util.BaseLibException;

public class LangDialog extends Dialog {
    public LangDialog(Shell parent, Properties props, String[][] lngs, String appName, final boolean force) {
        super(parent, props, null, SWT.TITLE | SWT.BORDER);

        setText(appName);

        GridLayout gridLayout = new GridLayout();
        setLayout(gridLayout);

        Label lbl = new Label(this, SWT.NONE);
        lbl.setText(NLS.DLG_LANG_TEXT.s());
        GridData data = new GridData (SWT.FILL, SWT.CENTER, false, false);
        lbl.setLayoutData(data);

        for (final String[] e : lngs) {
            Button btn = new Button (this, SWT.PUSH);
            btn.setText(e[1]);
            btn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
            btn.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event evt) {
                    onSelect(e[0]);
                }
            });
        }

        this.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event evt) {
                evt.doit = !force || null != LangDialog.this.id;
            };
        });

        pack();

        shellProps(parent, "lang");  // (*)
    }

    void onSelect(String id) {
        try {
            NLS.Reg.instance().load(this.id = id);
        }
        catch (BaseLibException ble) {
            SWTUtil.msgboxError(ble, getShell());
        }
        close();
    }

    public String id() {
        return this.id;
    }
    String id;

    // FIXME: hack to get around initialization of a window whose size is not
    //        fixed absolutely, but determined by the result of the layout (*)

    @Override
    protected boolean fixedSz() {
        return true;
    }

    @Override
    protected Point defSz(Shell parent) {
        return getSize();
    }
}
