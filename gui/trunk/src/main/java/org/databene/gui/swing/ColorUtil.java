/*
 * (c) Copyright 2005-2008 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.gui.swing;

import java.awt.Color;

/**
 * Utility class for Color operations.<br/>
 * <br/>
 * Created: 22.05.2005 08:22:53
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ColorUtil {

    private static final double MAX_HUE = 1./3.;
    private static final double MIN_HUE = 0.;

    public static Color inverseHeatColor(double value, double saturation, double brightness) {
        if (value < 0)
            value = 0;
        if (value > 1)
            value = 1;
        double hue = MIN_HUE + value * (MAX_HUE - MIN_HUE);
        return Color.getHSBColor((float) hue, (float) saturation, (float) brightness);
    }

    public static Color heatColor(double value, double saturation, double brightness) {
        if (value < 0)
            value = 0;
        if (value > 1)
            value = 1;
        double hue = MAX_HUE - value * (MAX_HUE - MIN_HUE);
        return Color.getHSBColor((float) hue, (float) saturation, (float) brightness);
    }

    public static Color adaptBrightness(Color color, double grade) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] *= grade;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
}
