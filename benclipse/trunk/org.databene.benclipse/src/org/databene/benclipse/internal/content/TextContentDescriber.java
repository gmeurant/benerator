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

package org.databene.benclipse.internal.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;

/**
 * Simple implementation of the {@link ITextContentDescriber} interface.<br/>
 * <br/>
 * Created at 10.02.2009 20:13:43
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class TextContentDescriber implements ITextContentDescriber {

	private final static QualifiedName[] SUPPORTED_OPTIONS = { IContentDescription.BYTE_ORDER_MARK };

	public int describe(Reader contents, IContentDescription description) throws IOException {
		return INDETERMINATE;
	}

	public int describe(InputStream contents, IContentDescription description) throws IOException {
		if (description == null || !description.isRequested(IContentDescription.BYTE_ORDER_MARK))
			return INDETERMINATE;
		byte[] bom = getByteOrderMark(contents);
		if (bom != null)
			description.setProperty(IContentDescription.BYTE_ORDER_MARK, bom);
		// we want to be pretty loose on detecting the text content type
		return INDETERMINATE;
	}

	public QualifiedName[] getSupportedOptions() {
		return SUPPORTED_OPTIONS;
	}

	byte[] getByteOrderMark(InputStream input) throws IOException {
		int first = input.read();
		if (first == 0xEF) {
			// look for the UTF-8 Byte Order Mark (BOM)
			int second = input.read();
			int third = input.read();
			if (second == 0xBB && third == 0xBF)
				return IContentDescription.BOM_UTF_8;
		} else if (first == 0xFE) {
			// look for the UTF-16 BOM
			if (input.read() == 0xFF)
				return IContentDescription.BOM_UTF_16BE;
		} else if (first == 0xFF) {
			if (input.read() == 0xFE)
				return IContentDescription.BOM_UTF_16LE;
		}
		return null;
	}
}
