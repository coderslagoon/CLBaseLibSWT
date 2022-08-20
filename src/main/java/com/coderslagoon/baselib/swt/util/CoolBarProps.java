package com.coderslagoon.baselib.swt.util;

import java.util.Properties;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;

import com.coderslagoon.baselib.util.BaseLibException;
import com.coderslagoon.baselib.util.BinUtils;
import com.coderslagoon.baselib.util.Prp;

public class CoolBarProps {
    public static class Layout {
        int[] itemOrder;
        int[] wrapIndices;
        Point[] sizes;

        private Layout() {
        }

        public Layout(int[] itemOrder, int[] wrapIndices, Point[] sizes) throws BaseLibException {
            if (itemOrder.length != sizes.length) {
                throw new BaseLibException("more item orders than sizes or vice versa");
            }
            this.itemOrder = itemOrder;
            this.wrapIndices = wrapIndices;
            this.sizes = sizes;
        }

        public static Layout fromString(String expr, int hmin) throws BaseLibException {
            int pos = expr.indexOf(";");
            if (-1 == pos) {
                throw new BaseLibException("missing first semicolon");
            }
            int pos2 = expr.indexOf(";", pos + 1);
            if (-1 == pos2) {
                throw new BaseLibException("missing second semicolon");
            }
            Layout result = new Layout();
            try {
                result.itemOrder   = BinUtils.stringToIntArray(expr.substring(0, pos));
                result.wrapIndices = BinUtils.stringToIntArray(expr.substring(pos + 1, pos2));
                result.sizes       = stringToPoints(expr.substring(pos2 + 1));
                if (0 < hmin) {
                    for (int i = 0; i < result.sizes.length; i++) {
                        if (result.sizes[i].y < hmin) {
                            result.sizes[i].y = hmin;
                        }
                    }
                }
            }
            catch (NumberFormatException nfe) {
                throw new BaseLibException("invalid data");
            }
            return result;
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append(BinUtils.intArrayToString(this.itemOrder));
            result.append(';');
            result.append(null == this.wrapIndices ? "" : BinUtils.intArrayToString(this.wrapIndices));
            result.append(';');
            result.append(pointsToString(this.sizes));
            return result.toString();
        }

        static Point[] stringToPoints(String expr) throws BaseLibException {
            int[] points = BinUtils.stringToIntArray(expr);
            if (1 == (points.length & 1)) {
                throw new BaseLibException("invalid points data");
            }
            Point[] result = new Point[points.length >> 1];
            for (int i = 0, j = 0; i < result.length; i++, j+=2) {
                result[i] = new Point(points[j], points[j + 1]);
            }
            return result;
        }

        static String pointsToString(Point[] points) {
            if (null == points) {
                return "";
            }
            StringBuilder result = new StringBuilder();
            for (Point point : points) {
                if (0 < result.length()) {
                    result.append(',');
                }
                result.append(point.x);
                result.append(',');
                result.append(point.y);
            }
            return result.toString();
        }
    }

    final Prp.Str LAYOUT;

    CoolBar    coolbar;
    Properties props;

    public CoolBarProps(CoolBar coolbar, String prefix, Properties props, Layout defaultLayout) throws BaseLibException {
        this.LAYOUT = new Prp.Str(prefix + "layout", defaultLayout.toString());
        this.coolbar = coolbar;
        this.props = props;
        Layout layout = Layout.fromString(this.LAYOUT.get(this.props), -1);
        this.coolbar.setItemLayout(layout.itemOrder, layout.wrapIndices, layout.sizes);
    }

    public void store() throws BaseLibException {
        Layout layout = new Layout(
                this.coolbar.getItemOrder(),
                this.coolbar.getWrapIndices(),
                this.coolbar.getItemSizes());
        this.LAYOUT.set(this.props, layout.toString());
    }
}
