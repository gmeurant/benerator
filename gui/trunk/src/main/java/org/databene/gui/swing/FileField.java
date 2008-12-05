/*
 * (c) Copyright 2007-2008 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ArrayFormat;
import org.databene.commons.file.FilenameFormat;

import javax.swing.*;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Lets the user choose a {@link File} with {@link TextField} and {@link JFileChooser}.<br/>
 * <br/>
 * Created: 05.05.2007 00:10:38
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class FileField extends Box {
	
	private static final long serialVersionUID = 2088339867450647264L;
	
	public static int FILES_ONLY = JFileChooser.FILES_ONLY;
	public static int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;
	public static int FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES; 
	public static int OPEN_DIALOG = JFileChooser.OPEN_DIALOG; 
	public static int SAVE_DIALOG = JFileChooser.SAVE_DIALOG; 

    boolean fullPathDisplayed;
    private FilenameFormat filenameFormat;
    JTextField filenameField;
    JFileChooser chooser;
    private List<ActionListener> actionListeners;
    int operation;

    public FileField() {
        this(20);
    }

    public FileField(int columns) {
        this(columns, null, FILES_AND_DIRECTORIES, OPEN_DIALOG);
    }

    public FileField(int columns, File file, int selectionMode, int operation) {
    	super(BoxLayout.X_AXIS);
    	setBorder(null);
        this.fullPathDisplayed = true;
        this.operation = operation;
        filenameField = new JTextField(columns);
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(selectionMode);
        if (file != null && file.exists()) {
            chooser.setSelectedFile(file);
            filenameField.setText(file.getAbsolutePath());
        }
        filenameField.setEditable(false);
        add(filenameField, BorderLayout.CENTER);
        JButton button = new JButton("...");
        add(button, BorderLayout.EAST);
        button.addActionListener(new Listener());
        filenameFormat = new FilenameFormat(true);
        this.actionListeners = new ArrayList<ActionListener>();
    }

    public File getFile() {
        return chooser.getSelectedFile();
    }

    public void setFile(File file) {
        chooser.setSelectedFile(file);
        filenameField.setText(file.getAbsolutePath());
    }

    public File[] getFiles() {
        return chooser.getSelectedFiles();
    }

    public void setFiles(File[] files) {
        filenameField.setText(ArrayFormat.format(filenameFormat, ", ", files));
        chooser.setSelectedFiles(files);
    }

    public boolean isMultiSelectionEnabled() {
        return chooser.isMultiSelectionEnabled();
    }

    public void setMultiSelectionEnabled(boolean multiSelectionEnabled) {
        chooser.setMultiSelectionEnabled(multiSelectionEnabled);
    }

    public boolean isFullPathUsed() {
        return filenameFormat.isFullPathUsed();
    }

    public void setFullPathUsed(boolean fullPathUsed) {
        filenameFormat.setFullPathUsed(fullPathUsed);
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    // private helpers -------------------------------------------------------------------------------------------------

    void fireAction(String command) {
        ActionEvent e = new ActionEvent(this, 0, command);
        for (int i = actionListeners.size() - 1; i >= 0; i--)
            actionListeners.get(i).actionPerformed(e);
    }

    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            File file = null;
            String path = filenameField.getText();
            if (path.length() > 0) {
                file = new File(path);
                if (!file.exists())
                    file = null;
            }
            if (file != null) {
                chooser.setCurrentDirectory(file.getParentFile());
                chooser.setSelectedFile(file);
            }
            int action = (operation == OPEN_DIALOG ?
            		chooser.showOpenDialog(FileField.this) : 
            		chooser.showSaveDialog(FileField.this));
            if (action == JFileChooser.APPROVE_OPTION ) {
                File selectedFile = chooser.getSelectedFile();
                filenameField.setText(selectedFile.getAbsolutePath());
                fireAction("files");
            }
        }
    }
}
