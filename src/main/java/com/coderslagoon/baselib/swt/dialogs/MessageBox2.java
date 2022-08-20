package com.coderslagoon.baselib.swt.dialogs;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.swt.NLS;
import com.coderslagoon.baselib.swt.util.SWTUtil;
import com.coderslagoon.baselib.util.BinUtils;

public class MessageBox2 extends Dialog {
    Button[] buttons;
    Button   chkAll;
    Button   defBtn;

    final static int GAP = 5;

    public MessageBox2(Shell parent, Properties props,  String cfg, int imgidx,
        String text, String title, String[] btns, String all, Integer defBtn) {
        super(parent, props, cfg, SWT.TITLE | SWT.BORDER | SWT.DIALOG_TRIM);

        setText(title);

        GridLayout gl = new GridLayout();
        gl.marginWidth = GAP;
        gl.marginHeight = GAP;
        gl.horizontalSpacing = GAP;
        gl.verticalSpacing = GAP;
        setLayout(gl);

        Composite top = new Composite(this, SWT.NONE);
        top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Composite bottom = new Composite(this, SWT.NONE);
        bottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        gl = new GridLayout(btns.length, false);
        gl.marginWidth = GAP;
        gl.marginHeight = GAP;
        gl.horizontalSpacing = GAP;
        gl.verticalSpacing = GAP;
        bottom.setLayout(gl);

        this.buttons = new Button[btns.length];
        for (int i = 0; i < btns.length; i++) {
            Button b = this.buttons[i] = new Button(bottom, SWT.PUSH);
            b.setText(btns[i]);
            b.pack(true);
            b.setLayoutData(i == btns.length - 1 ?
                new GridData(SWT.END      , SWT.FILL  , true , false) :
                new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
            SWTUtil.adjustButtonSize(b, .5);
            final int idx = i;
            b.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event evt) {
                    onButton(idx);
                }
            });
        }
        if (null != all) {
            // NOTE: storing state might make sense but is also dangerous
            this.chkAll = new Button(bottom, SWT.CHECK);
            this.chkAll.setText(all);
            this.chkAll.setLayoutData(new GridData(
                    SWT.BEGINNING, SWT.FILL, false, false, btns.length, 1));
        }

        gl = new GridLayout(2, false);
        gl.marginWidth = GAP;
        gl.marginHeight = GAP;
        gl.marginTop = GAP;
        gl.marginBottom = GAP;
        gl.horizontalSpacing = GAP * 4;
        gl.verticalSpacing = GAP;
        top.setLayout(gl);

        Label img = new Label(top, SWT.NONE);
        img.setImage(getDisplay().getSystemImage(imgidx));

        Label inf = new Label(top, SWT.NONE);
        inf.setText(text.replace("&", "&&"));
        inf.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        if (null != defBtn) {
            this.defBtn = this.buttons[defBtn];
        }
        pack();
        layout();

        Point sz = getSize();
        if (null == props) {
            Rectangle pb = parent.getBounds();
            int x = pb.x + Math.max(0, (pb.width  - sz.x) / 2);
            int y = pb.y + Math.max(0, (pb.height - sz.y) / 2);
            this.setLocation(x, y);
        }

        sz = getSize();
        Rectangle ca = getDisplay().getClientArea();
        ca.width  -= ca.width  >> 4;
        ca.height -= ca.height >> 4;
        boolean adjust = false;
        if (sz.x > ca.width) {
            sz.x = ca.width;
            adjust = true;
        }
        if (sz.y > ca.height) {
            sz.y = ca.height;
            adjust = true;
        }
        if (adjust) {
            setSize(sz);
        }
    }

    void onButton(int idx) {
        this.result = idx;
        this.all = null == this.chkAll ? null : this.chkAll.getSelection();
        close();
    }
    Integer result;
    Boolean all;

    public Integer show() {
        open();
        if (null != this.defBtn) {
            setDefaultButton(null);
            this.defBtn.setFocus();
            setDefaultButton(this.defBtn);
        }
        waitForClose();
        return this.result;
    }

    public Boolean all() {
        return this.all;
    }

    @Override
    protected boolean fixedSz() {
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////

    public static Integer standard(
            Shell parent, int style, String text, String title) {
        return standard(parent, style, text, title, 0);
    }

    public static Integer standard(
            Shell parent, int style, String text, String title, int defBtn) {

        int imgidx = style &             (SWT.ICON_ERROR   | SWT.ICON_INFORMATION |
                      SWT.ICON_QUESTION | SWT.ICON_WARNING | SWT.ICON_WORKING);
        int[] res;
        String[] btns;
        if (BinUtils.bitsSet(style, SWT.OK | SWT.CANCEL)) {
            res = new int[] { SWT.OK, SWT.CANCEL };
            btns = new String[] { NLS.MSGBOX2_OK.s(), NLS.MSGBOX2_CANCEL.s() };
        }
        else if (BinUtils.bitsSet(style, SWT.OK)) {
            res = new int[] { SWT.OK };
            btns = new String[] { NLS.MSGBOX2_OK.s() };
        }
        else if (BinUtils.bitsSet(style, SWT.YES | SWT.NO | SWT.CANCEL)) {
            res = new int[] { SWT.YES, SWT.NO, SWT.CANCEL };
            btns = new String[] { NLS.MSGBOX2_YES.s(), NLS.MSGBOX2_NO.s(), NLS.MSGBOX2_CANCEL.s() };
        }
        else if (BinUtils.bitsSet(style, SWT.YES | SWT.NO)) {
            res = new int[] { SWT.YES, SWT.NO };
            btns = new String[] { NLS.MSGBOX2_YES.s(), NLS.MSGBOX2_NO.s() };
        }
        else if (BinUtils.bitsSet(style, SWT.RETRY | SWT.CANCEL)) {
            res = new int[] { SWT.RETRY, SWT.CANCEL };
            btns = new String[] { NLS.MSGBOX2_RETRY.s(), NLS.MSGBOX2_CANCEL.s() };
        }
        else if (BinUtils.bitsSet(style, SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
            res = new int[] { SWT.ABORT, SWT.RETRY, SWT.CANCEL };
            btns = new String[] { NLS.MSGBOX2_ABORT.s(), NLS.MSGBOX2_RETRY.s(), NLS.MSGBOX2_CANCEL.s() };
        }
        else {
            throw new Error();
        }
        MessageBox2 mb = new MessageBox2(parent, null, null,
                imgidx, text, title, btns, null, defBtn);
        Integer mbres = mb.show();
        if (null == mbres) {
            return res[res.length - 1];
        }
        return res[mbres];
    }
}
