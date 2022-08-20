package com.coderslagoon.baselib.swt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.coderslagoon.baselib.util.Combo;
import com.coderslagoon.baselib.util.NLS;

public class ToolTips implements NLS.Reg.Listener {
    final List<Combo.Two<Control, NLS.Str>> tooltips = new ArrayList<>();

    public Control add(Control ctrl, NLS.Str nlstr) {
        try {
            ctrl.setToolTipText(nlstr.s());
            this.tooltips.add(new Combo.Two<>(ctrl, nlstr));
        }
        catch (Exception e) {
            throw new Error(e);
        }
        return ctrl;
    }

    public void enabled(boolean state) {
        for (Combo.Two<Control, NLS.Str> tt : this.tooltips) {
            tt.t.setToolTipText(state ? tt.u.s() : "");
        }
    }

    public void shellListen(final Shell shell) {
        shell.addShellListener(new ShellListener() {
            public void shellActivated  (ShellEvent e) { }
            public void shellDeactivated(ShellEvent e) { }
            public void shellDeiconified(ShellEvent e) { }
            public void shellIconified  (ShellEvent e) { }
            public void shellClosed(ShellEvent e) {
                ListIterator<Combo.Two<Control, NLS.Str>> li = ToolTips.this.tooltips.listIterator();
                while (li.hasNext()) {
                    Combo.Two<Control, NLS.Str> tt = li.next();
                    if (tt.t.getShell() == shell) {
                        li.remove();
                    }
                }
            }
        });
    }

    public void onLoaded() {
        for (Combo.Two<Control, NLS.Str> tt : this.tooltips) {
            tt.t.setToolTipText(tt.u.s());
        }
    }
}
