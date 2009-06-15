/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benclipse.wizards;

import java.util.Locale;

/**
 * JavaBean that wraps benerator configuration 
 * which relate to technical platforms or semantic categorization.<br/>
 * <br/>
 * Created at 24.02.2009 09:27:04
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class GenerationConfig {
	
	private String encoding;
	private String lineSeparator;
	private Locale locale;
	private String dataset;
	private boolean openGeneratedFiles;
	
	public GenerationConfig() {
		this(null, null, null, null /*, true*/);
    }

	public GenerationConfig(String encoding, String lineSeparator, Locale locale, String dataset/*, boolean openGeneratedFiles*/) {
	    this.encoding = encoding;
	    this.lineSeparator = lineSeparator;
	    this.locale = locale;
	    this.dataset = dataset;
	    //this.openGeneratedFiles = openGeneratedFiles;
    }

	public String getEncoding() {
    	return encoding;
    }

	public void setEncoding(String encoding) {
    	this.encoding = encoding;
    }

	public String getLineSeparator() {
    	return lineSeparator;
    }

	public void setLineSeparator(String lineSeparator) {
    	this.lineSeparator = lineSeparator;
    }

	public Locale getLocale() {
    	return locale;
    }

	public void setLocale(Locale locale) {
    	this.locale = locale;
    }

	public String getDataset() {
    	return dataset;
    }

	public void setDataset(String dataset) {
    	this.dataset = dataset;
    }

	public boolean isOpenGeneratedFiles() {
    	return openGeneratedFiles;
    }

	public void setOpenGeneratedFiles(boolean openGeneratedFiles) {
    	this.openGeneratedFiles = openGeneratedFiles;
    }

	
}
