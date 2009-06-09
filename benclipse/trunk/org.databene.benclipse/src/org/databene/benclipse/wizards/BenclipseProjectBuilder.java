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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.archetype.Archetype;
import org.databene.benclipse.core.BenclipseLogger;
import org.databene.benclipse.core.IBenclipseConstants;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.internal.EclipseUtil;
import org.databene.benclipse.internal.HsqlDbController;
import org.databene.benerator.main.DBSnapshotTool;
import org.databene.commons.ArrayBuilder;
import org.databene.commons.ArrayUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.FileUtil;
import org.databene.commons.Filter;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.ToStringConverter;
import org.databene.commons.xml.XMLUtil;
import org.databene.model.data.ComplexTypeDescriptor;
import org.databene.model.data.ComponentDescriptor;
import org.databene.model.data.DataModel;
import org.databene.model.data.FeatureDetail;
import org.databene.model.data.IdDescriptor;
import org.databene.model.data.InstanceDescriptor;
import org.databene.model.data.PartDescriptor;
import org.databene.model.data.ReferenceDescriptor;
import org.databene.model.data.SimpleTypeDescriptor;
import org.databene.model.data.TypeDescriptor;
import org.databene.platform.db.DBSystem;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.hsqldb.lib.StringUtil;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Creates a Benerator project in the Eclipse workspace.<br/>
 * <br/>
 * Created at 24.02.2009 09:31:58
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class BenclipseProjectBuilder { // TODO v0.6 merge with ArchetypeBuilder

    private static final String DBUNIT_SNAPSHOT_FILENAME = "base.dbunit.xml";
	private static final String SRC_FOLDER_NAME = "src";
    private final static Set<String> DB_CONSTRAINT_NAMES = CollectionUtil.toSet("nullable", "maxLength", "type");
	private static final char TAB = '\t';

    private static ToStringConverter toStringConverter = new ToStringConverter();


	public static boolean runProjectBuilder(final String name, final Archetype archetype, final IVMInstall jre,
	        final DBProjectInfo dbInfo, final GenerationConfig generationConfig, 
	        final IWorkbenchWindow workbenchWindow) {

		final Job job = new WorkspaceJob(Messages.getString("wizard.project.job.creating.project")) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				try {
					createProject(name, archetype, jre, dbInfo, generationConfig, monitor, workbenchWindow);
					return Status.OK_STATUS;
				} catch (CoreException e) {
					return e.getStatus();
				} catch (Exception e) {
					return new Status(Status.ERROR, BenclipsePlugin.PLUGIN_ID, e.getMessage(), e);
				} finally {
					monitor.done();
				}
			}
		};

		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				final IStatus result = event.getResult();
				if (!result.isOK()) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openError(
									BenclipsePlugin.getActiveWorkbenchShell(), 
									Messages.formatMessage("wizard.project.job.failed", name), 
							        result.getMessage());
						}
					});
				}
			}
		});
		job.schedule();
		return true;
	}

    public static boolean createProject(final String name, Archetype archetype, IVMInstall vmInstall, DBProjectInfo dbInfo, 
    		GenerationConfig generationConfig, IProgressMonitor monitor, IWorkbenchWindow workbenchWindow) throws Exception {
        monitor.beginTask("Creating project " + name, 3);
        JDBCConnectionInfo connectionInfo = dbInfo.getConnectionInfo();
        SchemaInitializationInfo initInfo = dbInfo.getInitInfo();

        IProject project = createEclipseProject(name, monitor);
		createJavaProject(project, vmInstall, monitor);		
		File projectRoot = createProjectContent(project, archetype, dbInfo, generationConfig, monitor);
        project.refreshLocal(IResource.DEPTH_INFINITE, monitor); // refresh project structure in Eclipse
        // TODO v0.6 expand project in navigator
        monitor.worked(1);

    	try {
            if (dbInfo.isStartHsql() && HsqlDbController.canBeStarted())
            	HsqlDbController.startHSQL(true);
        } catch (Exception ex) {
        	BenclipseLogger.log("Error starting built-in HSQL database", ex);
        }
        
        if (initInfo.isDbUnitSnapshot())
	        BenclipseDbUnitSnapshotCreator.createSnapshot(connectionInfo, new File(projectRoot, DBUNIT_SNAPSHOT_FILENAME));
        
        EclipseUtil.openInDefaultEditor(project.getFile("benerator.xml"), workbenchWindow);
    	return true;
    }

	private static File createProjectContent(IProject project, Archetype archetype, 
				DBProjectInfo dbInfo, GenerationConfig generationConfig, IProgressMonitor monitor)
            throws FileNotFoundException, IOException {
	    // create project files
        monitor.subTask("Creating files...");
        // copy archetype files
        File srcDir = archetype.getDirectory();
        File[] srcFiles = srcDir.listFiles();
        for (File srcFile : srcFiles) { 
        	if (!("ARCHETYPE-INF".equals(srcFile.getName()))) {
	        	copyToProject(srcFile, project);
        	}
        }
    	// TODO v0.6 remove XSD from template directory, include it by default
        createBeneratorXml(project, dbInfo, generationConfig, monitor);
		createDbUnitSnapshot(project, dbInfo, monitor);
	    return project.getLocation().toFile();
    }

    private static void createBeneratorXml(IProject project, DBProjectInfo dbInfo,
            GenerationConfig generationConfig, IProgressMonitor monitor) throws IOException {
    	IFile ifile = project.getFile("benerator.xml");
    	File descriptorFile = ifile.getLocation().toFile();
    	Document document = XMLUtil.parse(descriptorFile.getAbsolutePath());
    	Element root = document.getDocumentElement();
    	
    	// set up generation config
    	if (generationConfig.getEncoding() != null)
    		root.setAttribute("defaultEncoding", generationConfig.getEncoding());
    	if (generationConfig.getDataset() != null)
    		root.setAttribute("defaultDataset", generationConfig.getDataset());
    	if (generationConfig.getLocale() != null)
    		root.setAttribute("defaultLocale", generationConfig.getLocale().toString());
    	if (generationConfig.getLineSeparator() != null)
    		root.setAttribute("defaultLineSeparator", generationConfig.getLineSeparator());
    	
    	setupDb(dbInfo, root);
    	setupSqlFiles(project, dbInfo, root);
    	String lineSeparator = generationConfig.getLineSeparator();
    	if (lineSeparator == null)
    		lineSeparator = SystemInfo.getLineSeparator();
		setupDBTableGeneration(dbInfo, document, lineSeparator);
    	
    	save(document, descriptorFile);
    	monitor.worked(1);
    	
    }

	private static void setupSqlFiles(IProject project, DBProjectInfo dbInfo, Element root) 
			throws FileNotFoundException, IOException {
		SchemaInitializationInfo initInfo = dbInfo.getInitInfo();

		// drop script for database-related archetypes
    	if (initInfo != null && initInfo.getDropScriptFile() != null) {
    		File dropScriptFile = initInfo.getDropScriptFile();
			copyToProject(dropScriptFile, project);
			Element element = findElement("execute", "uri", "{drop_tables.sql}", root);
			element.setAttribute("uri", dropScriptFile.getName());
    	} else
    		removeElementIfExists("execute", "uri", "{drop_tables.sql}", root);
    	
		// create script for database-related archetypes
    	if (initInfo != null && initInfo.getCreateScriptFile() != null) {
    		File createScriptFile = initInfo.getCreateScriptFile();
			copyToProject(createScriptFile, project);
			Element element = findElement("execute", "uri", "{create_tables.sql}", root);
			element.setAttribute("uri", createScriptFile.getName());
    	} else
    		removeElementIfExists("execute", "uri", "{create_tables.sql}", root);
    	
    	// DBUnit snapshot import
    	if (initInfo == null || !initInfo.isDbUnitSnapshot())
    		removeElementIfExists("create-entities", "source", DBUNIT_SNAPSHOT_FILENAME, root);
    }

    private static Element findElement(String name, String attrName, String attrValue, Element root) {
	    return findElement(root, new NameAndAttributeFilter(name, attrName, attrValue));
    }

	private static void copyToProject(File srcFile, IProject project) throws FileNotFoundException, IOException {
        File projectRoot = project.getLocation().toFile();
    	File dstFile = new File(projectRoot, srcFile.getName());
    	FileUtil.copy(srcFile, dstFile, true);
    }

	private static void setupDBTableGeneration(DBProjectInfo dbInfo, Document document, String lineSeparator) {
		Element createTablesElement = findElement("create-entities", "name", "table", document.getDocumentElement());
		if (createTablesElement != null) {
			createTables(dbInfo, createTablesElement, lineSeparator);
			createTablesElement.getParentNode().removeChild(createTablesElement);
		}
    }

    private static void createTables(DBProjectInfo dbInfo, Element markerElement, String lineSeparator) {
    	DBSystem db = getDBSystem(dbInfo.getConnectionInfo());
    	TypeDescriptor[] descriptors = db.getTypeDescriptors();
		for (TypeDescriptor descriptor : descriptors) {
          	ComplexTypeDescriptor complexType = (ComplexTypeDescriptor) descriptor;
          	String name = complexType.getName();
          	InstanceDescriptor iDesc = new InstanceDescriptor(name, name);
          	if (dbInfo.getInitInfo() != null && dbInfo.getInitInfo().isDbUnitSnapshot())
          		iDesc.setCount(0L);
          	else
          		iDesc.setCount(db.countEntities(name));
			createEntity(iDesc, markerElement, lineSeparator);
       }
	}
    
    private static DBSystem getDBSystem(JDBCConnectionInfo connection) {
		DBSystem db = new DBSystem("db", connection.getUrl(), connection.getDriverClass(), 
    			connection.getUser(), connection.getPassword());
		if (connection.getSchema() != null)
			db.setSchema(connection.getSchema());
		DataModel.getDefaultInstance().addDescriptorProvider(db);
		return db;
    }
	
	private static void createEntity(InstanceDescriptor descriptor, Element markerElement, String lineSeparator) {
        ComplexTypeDescriptor type = (ComplexTypeDescriptor) descriptor.getType();
        Document document = markerElement.getOwnerDocument();
        Element createEntityElement = document.createElement("create-entities");
        markerElement.getParentNode().insertBefore(createEntityElement, markerElement);
        for (FeatureDetail<? extends Object> detail : descriptor.getDetails()) {
            Object value = detail.getValue();
            if (value != null && !NullSafeComparator.equals(value, detail.getDefault()))
                createEntityElement.setAttribute(detail.getName(), toStringConverter.convert(value));
        }
        createEntityElement.setAttribute("consumer", "db");
        
        Comment attributeInfo = null;
        for (ComponentDescriptor cd : type.getComponents())
            attributeInfo = addAttribute(cd, createEntityElement, lineSeparator);
        if (attributeInfo != null)
        	appendNewLineAfter(attributeInfo, 1, lineSeparator);
        insertNewLineBefore(markerElement, 1, lineSeparator);
        insertNewLineBefore(markerElement, 1, lineSeparator);
    }
	
    private static void appendNewLineAfter(Node node, int tabCount, String lineSeparator) {
	    node.getParentNode().appendChild(createNewLine(node.getOwnerDocument(), tabCount, lineSeparator));
    }

	private static Comment addAttribute(ComponentDescriptor component, Element parent, String lineSeparator) {
    	// normalize
    	boolean nullable = (component.isNullable() == null || component.isNullable());
		if (component.getMaxCount() != null && component.getMaxCount() == 1)
			component.setMaxCount(null);
		if (component.getMinCount() != null && component.getMinCount() == 1)
			component.setMinCount(null);
		if (nullable)
			component.setNullable(null);

        String elementName = null;
        if (component instanceof PartDescriptor)
            elementName = "attribute";
        else if (component instanceof ReferenceDescriptor)
        	elementName = "reference";
        else if (component instanceof IdDescriptor)
        	elementName = "id";
        else
            throw new UnsupportedOperationException("Component descriptor type not supported: " + 
                    component.getClass().getSimpleName());
        
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("name", component.getName());
        SimpleTypeDescriptor type = (SimpleTypeDescriptor)(component.getTypeName() != null ? 
        		DataModel.getDefaultInstance().getTypeDescriptor(component.getTypeName()) : 
        		component.getLocalType());
        if (type != null) {
			for (FeatureDetail<? extends Object> detail : type.getDetails())
	            format(detail, attributes);
	    }
        for (FeatureDetail<? extends Object> detail : component.getDetails())
            format(detail, attributes);
        if (nullable)
        	attributes.put("nullQuota", "1");

        StringBuilder builder = new StringBuilder();
    	builder.append(elementName);
    	for (Map.Entry<String, String> entry : attributes.entrySet())
    		builder.append(' ').append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
    	builder.append(" /");
    	Comment comment = insertCommentNode(parent, builder.toString());
        insertNewLineBefore(comment, 2, lineSeparator);
        return comment;
    }
    
	private static void insertNewLineBefore(Node element, int tabCount, String lineSeparator) {
		Text newLine = createNewLine(element.getOwnerDocument(), tabCount, lineSeparator);
        element.getParentNode().insertBefore(newLine, element);
	}

	private static Text createNewLine(Document document, int tabCount, String lineSeparator) {
	    StringBuilder builder = new StringBuilder(lineSeparator);
		for (int i = 0; i < tabCount; i++)
			builder.append(TAB);
        Text newLine = document.createTextNode(builder.toString());
	    return newLine;
    }

    private static Comment insertCommentNode(Element parent, String commentText) {
    	Comment comment = parent.getOwnerDocument().createComment(commentText);
    	parent.appendChild(comment);
    	return comment;
    }

	private static void format(FeatureDetail<? extends Object> detail, Map<String, String> attributes) {
		if (!"name".equals(detail.getName()) && detail.getValue() != null && !dbConstraint(detail.getName()))
		    attributes.put(detail.getName(), toStringConverter.convert(detail.getValue()));
	}
	
	private static boolean dbConstraint(String name) {
		return DB_CONSTRAINT_NAMES.contains(name);
	}
	
	private static void createDbUnitSnapshot(IProject project, DBProjectInfo dbInfo, IProgressMonitor monitor) {
		SchemaInitializationInfo initInfo = dbInfo.getInitInfo();
    	if (initInfo != null && initInfo.isDbUnitSnapshot()) {
    		monitor.setTaskName("Creating DbUnit snapshot");
        	File root = project.getLocation().toFile();
        	File dbUnitFile = new File(root, DBUNIT_SNAPSHOT_FILENAME);
        	JDBCConnectionInfo connection = dbInfo.getConnectionInfo();
    		DBSnapshotTool.export(connection.getUrl(), connection.getDriverClass(), connection.getSchema(), 
    			connection.getUser(), connection.getPassword(), dbUnitFile.getAbsolutePath(), null);
    		monitor.worked(1);
    	}
    }

	private static void removeElementIfExists(
			final String elementName, final String attrName, final String attribute, Element root) {
	    removeElementIfExists(root, new Filter<Element>() {
	    	public boolean accept(Element candidate) {
	    		return elementName.equals(candidate.getNodeName()) && attribute.equals(candidate.getAttribute(attrName));
	        }
	    });
    }

    private static void removeElementIfExists(Element root, Filter<Element> filter) {
    	Element element = findElement(root, filter);
    	if (element != null)
    		element.getParentNode().removeChild(element);
    }

	private static void setupDb(DBProjectInfo dbInfo, Element root) {
	    // setup 'db' bean in all DB related archetypes
    	JDBCConnectionInfo con = dbInfo.getConnectionInfo();
    	if (con == null)
    		return;
    	Element db = findElement(root, new Filter<Element>() {
			public boolean accept(Element candidate) {
				return "database".equals(candidate.getNodeName()) && "db".equals(candidate.getAttribute("id"));
            }
    	});
    	if (db != null) {
			db.setAttribute("url", con.getUrl());	    		
    		db.setAttribute("driver", con.getDriverClass());
    		if (!StringUtil.isEmpty(con.getUrl()))
    			db.setAttribute("schema", con.getSchema());
    		db.setAttribute("user", con.getUser());
    		if (!StringUtil.isEmpty(con.getPassword()))
    			db.setAttribute("password", con.getPassword());
    	}
    }

    private static Element findElement(Element parent, Filter<Element> filter) { // TODO v0.6 move this to XMLUtil
	    if (filter.accept(parent))
	    	return parent;
	    for (Element child : XMLUtil.getChildElements(parent)) {
	    	Element candidate = findElement(child, filter);
	    	if (candidate != null)
	    		return candidate;
	    }
	    return null;
    }

	private static void save(Document document, File file) throws FileNotFoundException { // TODO v0.6 move this method to XMLUtil
		try {
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        DOMSource source = new DOMSource(document);
	        FileOutputStream os = new FileOutputStream(file);
	        StreamResult result = new StreamResult(os);
	        transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new ConfigurationError(e);
		}
    }

	private static IJavaProject createJavaProject(IProject project, IVMInstall vmInstall, IProgressMonitor monitor)
            throws CoreException, JavaModelException {
	    // create Java project
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] classpathEntries = createFoldersAndClasspath(vmInstall, javaProject);
		javaProject.setRawClasspath(classpathEntries, monitor);
		
		// set Java compliance
		javaProject.setOption(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
		return javaProject;
    }

	private static IProject createEclipseProject(final String name, IProgressMonitor monitor) throws CoreException {
	    monitor.subTask("Creating workspace project...");
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceRoot.getProject(name);
		if (!project.exists())
			project.create(monitor);
		if (!project.isOpen())
			project.open(monitor);
		
		// set Java nature
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		if (!ArrayUtil.contains(natures, JavaCore.NATURE_ID)) {
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = JavaCore.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, monitor);
		}
	    return project;
    }

	private static IClasspathEntry[] createFoldersAndClasspath(
    		IVMInstall vmInstall, IJavaProject javaProject) throws CoreException {
	    ArrayBuilder<IClasspathEntry> builder = new ArrayBuilder<IClasspathEntry>(IClasspathEntry.class);
	    
	    // JRE classes
	    IPath jreContainerPath = JavaRuntime.newJREContainerPath(vmInstall);
		builder.append(JavaCore.newContainerEntry(jreContainerPath));
	    
	    // src
		IProject project = javaProject.getProject();
		IFolder srcFolder = project.getFolder(SRC_FOLDER_NAME);
		if (!srcFolder.exists())
			srcFolder.create(false, true, null);
	    builder.append(JavaCore.newSourceEntry(srcFolder.getFullPath()));

	    // refer to benerator libraries
	    builder.append(JavaCore.newContainerEntry(new Path(IBenclipseConstants.CONTAINER_ID)));
               
		// done - return an array representation of the classpath
	    return builder.toArray();
    }

	public static class NameAndAttributeFilter implements Filter<Element> { // TODO v0.6 move to commons XML utilities 

		private String name;
		private String attrName;
		private String attrValue;

		public NameAndAttributeFilter(String name, String attrName, String attrValue) {
	        this.name = name;
	        this.attrName = attrName;
	        this.attrValue = attrValue;
        }

		public boolean accept(Element candidate) {
	        return name.equals(candidate.getNodeName()) && attrValue.equals(candidate.getAttribute(attrName));
        }
		
	}
}
