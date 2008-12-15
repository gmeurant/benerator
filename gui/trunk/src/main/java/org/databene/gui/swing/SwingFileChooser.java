/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import org.databene.commons.ui.FileChooser;
import org.databene.commons.ui.FileOperation;
import org.databene.commons.ui.FileTypeSupport;

/**
 * Swing implementation of the {@link FileChooser} interface.<br/>
 * <br/>
 * Created at 14.12.2008 14:34:50
 * @since 0.5.7
 * @author Volker Bergmann
 */

public class SwingFileChooser extends JFileChooser implements FileChooser {
	
	private static final long serialVersionUID = 3258145358496737942L;
	
	private FileOperation operation;

	public SwingFileChooser(FileTypeSupport supportedTypes, FileOperation operation) {
		this.operation = operation;
		super.setFileSelectionMode(fileSelectionMode(supportedTypes));
		super.setDialogType(dialogType(operation));
	}

	public File chooseFile(Component component) {
		int approval;
		switch (operation) {
			case open: approval = showOpenDialog(component); break;
			case save: approval = showSaveDialog(component); break;
			default: approval = showDialog(component, "TODO"); break; // TODO
		}
		return (approval == APPROVE_OPTION ? super.getSelectedFile() : null);
	}
	
	// private helpers -------------------------------------------------------------------------------------------------

	private int dialogType(FileOperation operation) {
		switch (operation) {
			case open: return JFileChooser.OPEN_DIALOG;
			case save: return JFileChooser.SAVE_DIALOG;
			default: return JFileChooser.CUSTOM_DIALOG;
		}
	}

	private int fileSelectionMode(FileTypeSupport supportedTypes) {
		throw new UnsupportedOperationException("SwingFileChooser.fileSelectionMode() is not implemented"); // TODO implement SwingFileChooser.fileSelectionMode
	}

}
