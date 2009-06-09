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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.databene.benclipse.core.BenclipseLogger;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.swt.SWTUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.IOUtil;
import org.databene.commons.LocaleUtil;
import org.databene.commons.OrderedSet;
import org.databene.commons.StringUtil;
import org.databene.domain.address.Country;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * Benerator wizard page for defining platform-dependent and semantic settings.<br/>
 * <br/>
 * Created at 17.02.2009 08:32:01
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class GenerationConfigPage extends BeneratorWizardPage {
	
    private static final String ENCODINGS_FILE = "org/databene/benclipse/wizards/encodings.txt";

    private static final String NO_CHARSET = "";
	private static final String NO_SEPARATOR = "";
    private static final String NO_DATASET = "";
	private static final String NO_LOCALE = "";
	
	private ComboViewer encodingCombo;
	private ComboViewer separatorCombo;
	private ComboViewer localeCombo;
	private ComboViewer datasetCombo;
	private Button openGeneratedFilesButton;

	public GenerationConfigPage() {
    	super("GenerationConfigPage");
		setPageComplete(false);
		setTitle(Messages.getString("wizard.project.gensettings.title"));
		setDescription(Messages.getString("wizard.project.gensettings.description"));
		setPageComplete(true);
    }

    public void createControl(Composite parent) {
		Composite control = SWTUtil.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL, true);
		createTechnicalGroup(control);
	    new Label(control, SWT.NONE);
	    createDomainGroup(control);
	    new Label(control, SWT.NONE);
	    openGeneratedFilesButton = SWTUtil.createCheckbox(
	    		control, Messages.getString("wizard.project.gensettings.open"), null, "", 1);
	    openGeneratedFilesButton.setSelection(true);
		setControl(control);
    }

	private void createTechnicalGroup(Composite parent) {
		Group group = SWTUtil.createGroup(
				parent, Messages.getString("wizard.project.gensettings.technical"), 2, 1, GridData.FILL_HORIZONTAL);
	    createEncodingCombo(group);
		createLineSeparatorCombo(group);
    }

	private void createDomainGroup(Composite parent) {
		Group group = SWTUtil.createGroup(
				parent, Messages.getString("wizard.project.gensettings.domain"), 2, 1, GridData.FILL_HORIZONTAL);
	    createLocaleCombo(group);
	    createDatasetCombo(group);
    }

	private void createEncodingCombo(Composite parent) {
		SWTUtil.createLabel(parent, Messages.getString("wizard.project.gensettings.encoding"));
	    encodingCombo = new ComboViewer(parent, 
	    		SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
	    Combo combo = encodingCombo.getCombo();
	    combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		encodingCombo.setLabelProvider(new LabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		String charsetName = (String) element;
	    		if (NO_CHARSET.equals(charsetName))
	    			return Messages.getString("wizard.project.gensettings.system");
				return charsetName;
	    	}
	    });
		encodingCombo.setContentProvider(new DefaultContentProvider());
		ArrayList<String> charsetNames = new ArrayList<String>(30);
	    charsetNames.add(NO_CHARSET);
	    try {
			String[] tmp = IOUtil.readTextLines(ENCODINGS_FILE, false);
		    CollectionUtil.add(charsetNames, tmp);
	    } catch (IOException e) {
	    	BenclipseLogger.log("Error reading file " + ENCODINGS_FILE, e);
	    }
	    encodingCombo.setInput(charsetNames);
	    encodingCombo.setSelection(new StructuredSelection(NO_CHARSET));
    }

    private void createLineSeparatorCombo(Composite parent) {
		SWTUtil.createLabel(parent, Messages.getString("wizard.project.gensettings.line.separator"));
	    separatorCombo = new ComboViewer(parent, 
	    		SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
	    Combo combo = separatorCombo.getCombo();
	    combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		separatorCombo.setLabelProvider(new LabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		String separator = (String) element;
	    		if (NO_SEPARATOR.equals(separator))
	    			return Messages.getString("wizard.project.gensettings.system");
				return StringUtil.escape(separator);
	    	}
	    });
		separatorCombo.setContentProvider(new DefaultContentProvider());
	    separatorCombo.setInput(new String[] { "\r\n", "\n", NO_SEPARATOR});
	    separatorCombo.setSelection(new StructuredSelection(NO_SEPARATOR));
    }

    private void createLocaleCombo(Composite parent) {
	    SWTUtil.createLabel(parent, Messages.getString("wizard.project.gensettings.locale"));
	    localeCombo = new ComboViewer(parent, 
	    		SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
	    Combo combo = localeCombo.getCombo();
	    combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		localeCombo.setLabelProvider(new LabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		String localeCode = (String) element;
	    		if (NO_LOCALE.equals(localeCode))
	    			return Messages.getString("wizard.project.gensettings.system");
				Locale locale = LocaleUtil.getLocale(localeCode);
				return locale.toString() + " - " + locale.getDisplayName();
	    	}
	    });
		localeCombo.setContentProvider(new DefaultContentProvider());
		Locale defaultLocale = Locale.getDefault();
		Set<String> localeNames = new OrderedSet<String>();
		addLocaleAndParents(defaultLocale, localeNames);
		addLocaleAndParents(Locale.US, localeNames);
		addLocaleAndParents(Locale.UK, localeNames);
		addLocaleAndParents(Locale.CANADA_FRENCH, localeNames);
		addLocaleAndParents(Locale.GERMANY, localeNames);
		addLocaleAndParents(Locale.FRANCE, localeNames);
		addLocaleAndParents(Locale.ITALIAN, localeNames);
		addLocaleAndParents(Locale.JAPANESE, localeNames);
		addLocaleAndParents(Locale.CHINESE, localeNames);
		addLocaleAndParents(Locale.KOREAN, localeNames);
	    localeNames.add(NO_LOCALE);
	    
	    localeCombo.setInput(localeNames.toArray());
	    localeCombo.setSelection(new StructuredSelection(NO_LOCALE));
    }

	private void addLocaleAndParents(Locale defaultLocale, Set<String> localeNames) {
	    localeNames.add(defaultLocale.toString());
		Locale tmp = defaultLocale;
		Locale parentLocale = null;
		while ((parentLocale = LocaleUtil.parent(tmp)) != null) {
			localeNames.add(parentLocale.toString());
			tmp = parentLocale;
		}
    }

    private void createDatasetCombo(Composite parent) {
	    SWTUtil.createLabel(parent, Messages.getString("wizard.project.gensettings.dataset"));
	    
	    datasetCombo = new ComboViewer(parent, 
	    		SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
	    Combo combo = datasetCombo.getCombo();
	    combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		datasetCombo.setLabelProvider(new LabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		String isoCode = (String) element;
	    		if (NO_DATASET.equals(isoCode))
	    			return Messages.getString("wizard.project.gensettings.system");
				Country country = Country.getInstance(isoCode);
				return isoCode + (country != null ? " (" + country + ")" : NO_DATASET);
	    	}
	    });
		datasetCombo.setContentProvider(new DefaultContentProvider());
		
		OrderedSet<String> countryCodes = new OrderedSet<String>();
	    countryCodes.add(NO_DATASET);
	    addCountries(countryCodes, 
	    		Country.getDefault(), 
	    		Country.getFallback(),
	    		Country.US,
	    		Country.CANADA,
	    		Country.UNITED_KINGDOM,
	    		Country.GERMANY,
	    		Country.FRANCE,
	    		Country.ITALY,
	    		Country.SPAIN,
	    		Country.JAPAN,
	    		Country.CHINA,
	    		Country.KOREA_R
	    	);
		datasetCombo.setInput(countryCodes);
	    datasetCombo.setSelection(new StructuredSelection(NO_DATASET));
    }

	private void addCountries(OrderedSet<String> countryCodes, Country... countries) {
		for (Country country : countries)
			countryCodes.add(country.getIsoCode());
    }

    public GenerationConfig getGenerationConfig() {
    	// get combo box selections
		String encoding = getSelectedText(encodingCombo, NO_CHARSET);
    	String lineSeparator = getSelectedText(separatorCombo, NO_SEPARATOR);
    	String localeCode = getSelectedText(localeCombo, NO_LOCALE);
    	Locale locale = (localeCode != null ? LocaleUtil.getLocale(localeCode) : null);
    	String dataset = getSelectedText(datasetCombo, NO_DATASET);
    	
    	// resolve 'open files'
    	boolean open = openGeneratedFilesButton.getSelection();
    	
    	// create result
    	return new GenerationConfig(encoding, lineSeparator, locale, dataset, open);
    }

	private String getSelectedText(ComboViewer combo, String noSelection) {
	    String text = (String) ((IStructuredSelection) combo.getSelection()).getFirstElement();
	    if (StringUtil.isEmpty(noSelection))
	    	return null;
	    if (StringUtil.isEmpty(text))
	    	text = combo.getCombo().getText();
    	return (StringUtil.isEmpty(text) ? null : text);
    }
}