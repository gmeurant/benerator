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

package org.databene.benclipse.archetype;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.commons.IOUtil;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

/**
 * Retrieves and manages Benerator archetypes.<br/>
 * <br/>
 * Created at 18.02.2009 07:35:52
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class ArchetypeManager {
	
    private static final String ARCHETYPE_DIRECTORY = "archetype";

    private Archetype[] archetypes;
	
	@SuppressWarnings("unchecked")
    private ArchetypeManager() throws IOException {
    	// parse all archetypes
        Bundle bundle = BenclipsePlugin.getDefault().getBundle();
        Enumeration<String> entryPaths = bundle.getEntryPaths(ARCHETYPE_DIRECTORY);
        List<Archetype> archetypeList = new ArrayList<Archetype>();
        while (entryPaths.hasMoreElements()) {
        	String entryPath = entryPaths.nextElement();
        	File file = fileFromEntryPath(entryPath, bundle);
        	if (file.isDirectory() && !".svn".equals(file.getName()))
        		archetypeList.add(parseArchetype(file));
        }
    	
		archetypes = new Archetype[archetypeList.size()];
		int i = 0;

		// order archetypes corresponding to order.txt
    	File orderFile = fileFromURL(bundle.getEntry(ARCHETYPE_DIRECTORY + "/order.txt"));
    	if (orderFile.exists()) {
    		String[] entries = IOUtil.readTextLines(orderFile.getAbsolutePath(), false);
    		for (String entry : entries) {
    			Archetype archetype = getArchetypeOfDir(entry, archetypeList);
    			if (archetype != null) {
    				archetypes[i++] = archetype;
    				archetypeList.remove(archetype);
    			}
    		}
    	}
    	
   		// if no order list was defined or archetypes were missing on the list, append the remaining archetypes
		for (Archetype archetype : archetypeList)
			archetypes[i++] = archetype;
	}

    private Archetype getArchetypeOfDir(String directoryName, List<Archetype> archetypeList) {
    	for (Archetype candidate : archetypeList)
    		if (candidate.getDirectory().getName().equals(directoryName))
    			return candidate;
    	return null;
    }

	private File fileFromEntryPath(String entryPath, Bundle bundle) throws IOException {
    	URL elementUrl = bundle.getEntry(entryPath);
    	return fileFromURL(elementUrl);
    }

	private File fileFromURL(URL elementUrl) throws IOException {
	    elementUrl = FileLocator.resolve(elementUrl);
    	IPath elementPath = new Path(new File(elementUrl.getFile()).getAbsolutePath());
    	return elementPath.toFile();
    }

	private Archetype parseArchetype(File directory) throws IOException {
		String name = parseArchetypeName(directory);
		return new Archetype(name, directory);
    }

	private String parseArchetypeName(File directory) throws IOException {
	    // initially choose the folder name as template name
	    String name = directory.getName();
	    
	    // then check if a name.properties exists
	    File nameFile = new File(directory, "ARCHETYPE-INF" + File.separator + "name.properties");
	    if (nameFile.exists()) {
            Map<String, String> names = IOUtil.readProperties(nameFile.getAbsolutePath());
            // try to get the name in the user's default locale
            String languageCode = Locale.getDefault().getLanguage();
            if (names.get(languageCode) != null)
            	name = names.get(languageCode);
            // if no such name was defined, fall back to the English name (if it exists)
            else if (names.get("en") != null)
            	name = names.get("en");
	    }
	    return name;
    }

	public Archetype[] getArchetypes() {
    	return archetypes;
    }
	
	private static ArchetypeManager instance;
	
	public static ArchetypeManager getInstance() throws IOException {
		if (instance == null)
			instance = new ArchetypeManager();
		return instance;
	}

    public Archetype getDefaultArchetype() {
	    for (Archetype candidate : archetypes)
	    	if ("simple".equals(candidate.getDirectory().getName()))
	    		return candidate;
	    return archetypes[0];
    }
	
}
