package com.coderslagoon.baselib.swt.util;

import org.eclipse.swt.widgets.Event;

public interface Safe {

    public abstract class Listener implements org.eclipse.swt.widgets.Listener {

        protected abstract void unsafeHandleEvent(Event event);
        
        @Override
        public void handleEvent(Event event) {
            try {
                unsafeHandleEvent(event);
            }
            catch (Throwable err) {
                this.onError(err);
            }
        }
        
        protected void onError(Throwable err) {
            err.printStackTrace();
        }
    }
}
