/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
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

package org.databene.commons;

import java.util.Arrays;

/**
 * Counts with an arbitrary radix.<br/>
 * <br/>
 * Created: 15.11.2007 16:05:34
 */
public class CustomCounter { // TODO assign an appropriate name and move to commons

    private int radix;
    private int[] digits;
    private boolean overrun;

    // constructors ----------------------------------------------------------------------------------------------------

    public CustomCounter(int radix, int length) {
        this.radix = radix;
        this.digits = new int[length];
        this.overrun = false;
        Arrays.fill(digits, 0);
    }

    // interface -------------------------------------------------------------------------------------------------------

    public int[] getDigits() {
        return digits;
    }

    private char mapDigit(int digit) {
        if (digit <= 9)
            return (char) ('0' + digit);
        else
            return (char) ('a' + digit - 10);
    }

    public void increment() {
        incrementDigit(0);
    }

    public boolean hasOverrun() {
        return overrun;
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    public String toString() {
        char[] tmp = new char[digits.length];
        for (int i = 0; i < digits.length; i++)
            tmp[i] = mapDigit(digits[digits.length - 1 - i]);
        return new String(tmp);
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void incrementDigit(int i) {
        if (i >= digits.length) {
            overrun = true;
            return;
        }
        if (digits[i] < radix - 1)
            digits[i]++;
        else {
            digits[i] = 0;
            if (i < digits.length - 1)
                incrementDigit(i + 1);
            else
                overrun = true;
        }
    }
}