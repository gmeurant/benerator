/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

package org.databene.commons.file;

import java.text.DecimalFormat;

import org.databene.commons.Scale;

/**
 * Formats a number in a human-friendly manner, e.g. a file size in megabytes.<br/><br/>
 * Created: 06.03.2011 15:12:55
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class FileSizeFormatter { // TODO rename to BinaryScaleFormatter
	
	private Scale scale;
	private DecimalFormat numberFormat;
	
	public FileSizeFormatter(Scale scale) {
		this.scale = scale;
		this.numberFormat = new DecimalFormat();
		setGroupingUsed(true);
	}
	
	public void setGroupingUsed(boolean groupingUsed) {
		this.numberFormat.setGroupingUsed(groupingUsed);
	}
	
	public String format(long fileSize) { // TODO rename parameter 
		if (scale != null)
			return applyScale(scale, fileSize);
		else
			return applyAutoScale(fileSize);
	}

	private String applyScale(Scale scale, long fileSize) { // TODO rename parameter
		return numberFormat.format(Math.floor((fileSize + scale.getFactor() - 1) / scale.getFactor())) + " " + scale.getDesignator() + "B";
	}

	private String applyAutoScale(long fileSize) { // TODO rename parameter
		if (fileSize >= 10 * Scale.TERA.getFactor())
			return applyScale(Scale.TERA, fileSize);
		else if (fileSize >= 10 * Scale.GIGA.getFactor())
			return applyScale(Scale.GIGA, fileSize);
		else if (fileSize >= 10 * Scale.MEGA.getFactor())
			return applyScale(Scale.MEGA, fileSize);
		else if (fileSize >= 10 * Scale.KILO.getFactor())
			return applyScale(Scale.KILO, fileSize);
		else 
			return applyScale(Scale.NONE, fileSize);
	}

}
