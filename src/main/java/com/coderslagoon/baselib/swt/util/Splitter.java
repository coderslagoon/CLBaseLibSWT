package com.coderslagoon.baselib.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;

public class Splitter {
    Composite  parent;
    Sash       sash;
    FormData   sdata;
    boolean    horizontal;
    FormLayout form = new FormLayout();

    public Splitter(Composite parent, boolean horizontal) {
        this.parent = parent;
        this.horizontal = horizontal;
        this.parent.setLayout(this.form);
    }

    public Sash sash() {
        return null == this.sash ? this.sash = new Sash(this.parent,
                this.horizontal ? SWT.VERTICAL : SWT.HORIZONTAL) : this.sash;
    }

    public void initialize(Control ctrl, Control ctrl2, int percent) {
        this.parent.setLayout(this.form);
        FormData fdata = new FormData();
        if (this.horizontal) {
            fdata.right  = new FormAttachment(this.sash, 0);
            fdata.bottom = new FormAttachment(100, 0);
        }
        else {
            fdata.right  = new FormAttachment(100, 0);
            fdata.bottom = new FormAttachment(this.sash, 0);
        }
        fdata.left = new FormAttachment(0, 0);
        fdata.top  = new FormAttachment(0, 0);
        ctrl.setLayoutData(fdata);
        this.sdata = new FormData();
        if (this.horizontal) {
            this.sdata.left   = new FormAttachment(percent, 0);
            this.sdata.top    = new FormAttachment(0, 0);
            this.sdata.bottom = new FormAttachment(100, 0);
        }
        else {
            this.sdata.left  = new FormAttachment(0, 0);
            this.sdata.top   = new FormAttachment(percent, 0);
            this.sdata.right = new FormAttachment(100, 0);
        }
        this.sash.setLayoutData(this.sdata);
        this.sash.addListener(SWT.MouseUp, new Listener() {
            public void handleEvent(Event e) {
              Rectangle rs = Splitter.this.sash.getBounds();
              Rectangle rp = Splitter.this.parent.getClientArea();
              if (Splitter.this.horizontal) {
                  int x = rs.x + e.x;
                  x = Math.max(0, x);
                  x = Math.min(rp.width - rs.width, x);
                  int pct = (int)Math.round((x * 100.0) / Math.max(1, rp.width));
                  Splitter.this.sdata.left   = new FormAttachment(pct, 0);
              }
              else {
                  int y = rs.y + e.y;
                  y = Math.max(0, y);
                  y = Math.min(rp.height - rs.height, y);
                  int pct = (int)Math.round((y * 100.0) / Math.max(1, rp.height));
                  Splitter.this.sdata.top = new FormAttachment(pct, 0);
              }
              Splitter.this.parent.layout();
            }
        });
        fdata = new FormData();
        if (this.horizontal) {
            fdata.left = new FormAttachment(this.sash, 0);
            fdata.top  = new FormAttachment(0, 0);
        }
        else {
            fdata.left = new FormAttachment(0, 0);
            fdata.top  = new FormAttachment(this.sash, 0);
        }
        fdata.right  = new FormAttachment(100, 0);
        fdata.bottom = new FormAttachment(100, 0);
        ctrl2.setLayoutData(fdata);
    }

    public int percentage() {
        Rectangle rs = this.sash.getBounds();
        Rectangle rp = this.parent.getClientArea();
        return (int) Math.round(((this.horizontal ? rs.x     : rs.y) * 100.0) /
                      Math.max(1, this.horizontal ? rp.width : rp.height));
    }
}
