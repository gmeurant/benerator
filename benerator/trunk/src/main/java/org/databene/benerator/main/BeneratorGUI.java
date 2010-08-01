/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.main;

import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.databene.benerator.Version;
import org.databene.commons.FileUtil;
import org.databene.commons.IOUtil;
import org.databene.commons.SystemInfo;
import org.databene.gui.os.mac.MacApplicationAdapter;

import com.apple.eawt.Application;


/**
 * Small Swing application for editing and running Benerator descriptors.<br/><br/>
 * Created: 31.05.2010 08:05:35
 * @since 0.6.3
 * @author Volker Bergmann
 */
@SuppressWarnings("serial")
public class BeneratorGUI {
	
	static final File BUFFER_FILE = new File(BeneratorGUI.class.getSimpleName() + ".txt");
	
	public static void main(String[] args) throws Exception {
		if (SystemInfo.isMacOsx()) {
		    prepareMacVM();
		}
		new BeneratorGUIFrame().setVisible(true);
	}
	
	private static void prepareMacVM() {
	    System.setProperty("apple.awt.brushMetalLook", "true");
	    System.setProperty("apple.laf.useScreenMenuBar", "true");
	    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Benerator GUI");
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
	
	public static class BeneratorGUIFrame extends JFrame implements org.databene.gui.os.Application {
		
		JTextArea text;
		
		public BeneratorGUIFrame() throws IOException {
		    super("Benerator GUI");
		    if (SystemInfo.isMacOsx()) {
			    // create an instance of the Mac Application class, so i can handle the 
			    // mac quit event with the Mac ApplicationAdapter
			    Application macApplication = Application.getApplication();
			    MacApplicationAdapter macAdapter = new MacApplicationAdapter(this);
			    macApplication.addApplicationListener(macAdapter);
		    }
		    Container contentPane = getContentPane();
		    text = new JTextArea();
		    if (BUFFER_FILE.exists())
		    	text.setText(IOUtil.getContentOfURI(BUFFER_FILE.getAbsolutePath()));
		    contentPane.add(new JScrollPane(text));
			createMenuBar();
		    setSize(600, 400);
		    addWindowListener(new WindowAdapter() {
		    	@Override
		    	public void windowClosing(WindowEvent e) {
		    	    exit();
		    	}
		    });
	    }

		private void createMenuBar() {
		    JMenuBar menubar = new JMenuBar();
		    
		    // create file menu
		    JMenu fileMenu = new JMenu("File");
		    fileMenu.setMnemonic('F');
		    menubar.add(fileMenu);
		    
		    // create edit menu
		    JMenu editMenu = new JMenu("Edit");
		    editMenu.setMnemonic('E');
		    
		    createRunAction(editMenu);
		    menubar.add(editMenu);
		    
			setJMenuBar(menubar);
	    }

		private void createRunAction(JMenu editMenu) {
		    JMenuItem urlDecodeItem = editMenu.add(new RunAction());
		    urlDecodeItem.setAccelerator(
		    		KeyStroke.getKeyStroke('R',
		    	    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), 
		    	    false));
		    urlDecodeItem.setMnemonic('R');
	    }
		
		public void exit() {
			try {
		        String content = text.getText();
				IOUtil.writeTextFile(BUFFER_FILE.getAbsolutePath(), content);
	        } catch (IOException e) {
		        throw new RuntimeException(e);
	        } finally {
	        	System.exit(0);
	        }
		}

		private class RunAction extends AbstractAction {

			public RunAction() {
		        super("Run");
	        }

			public void actionPerformed(ActionEvent evt) {
				File file = null;
				try {
		            file = File.createTempFile("benerator-", ".ben.xml");
		            CharSequence builder = createXML();
		            IOUtil.writeTextFile(file.getAbsolutePath(), builder.toString());
		            Benerator.main(new String[] { file.getAbsolutePath() });
	            } catch (IOException e) {
		            throw new RuntimeException(e);
	            } finally {
	            	if (file != null)
	            		FileUtil.deleteIfExists(file);
	            }
	        }

			private CharSequence createXML() {
				if (text.getText().startsWith("<?xml"))
					return text.getText();
		        StringBuilder builder = new StringBuilder("<setup>");
		        builder.append(text.getText());
		        builder.append("</setup>");
		        return builder;
	        }
			
		}

		public void about() {
			JOptionPane.showMessageDialog(this, 
					"Benerator GUI " + Version.VERSION + SystemInfo.getLineSeparator() + 
					"(c) 2010 by Volker Bergmann");
        }

	}
	
}