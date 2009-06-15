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
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

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
import org.databene.commons.BeanUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.FileUtil;
import org.databene.commons.IOUtil;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.OrderedMap;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.ToStringConverter;
import org.databene.html.DefaultHTMLTokenizer;
import org.databene.html.HTMLTokenizer;
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
	private static final String COMMENT_DBUNIT = "Create a valid predefined base data set for regression testing by " +
			"importing a DbUnit file";
	private static final String COMMENT_DROP_TABLES = "Drop the current tables and sequences if they already exist";
	private static final String COMMENT_CREATE_TABLES = "Create the tables and sequences";

    private static ToStringConverter toStringConverter = new ToStringConverter();


	public static boolean runProjectBuilder(final String name, final Archetype archetype, final IVMInstall jre,
	        final DBProjectInfo dbInfo, final GenerationConfig generationConfig, 
	        final IWorkbenchWindow workbenchWindow) {

		final Job job = new WorkspaceJob(Messages.getString("wizard.project.job.creating.project")) {
			@Override
			public IStatus runInWorkspace(final IProgressMonitor monitor) {
				File jar = (dbInfo != null && dbInfo.getConnectionInfo() != null ? 
						dbInfo.getConnectionInfo().getLibraryFile() :
						null);
				try {
					BeanUtil.executeInJarClassLoader(jar, new Callable<IProject>() {
						public IProject call() throws Exception {
							return createProject(name, archetype, jre, dbInfo, generationConfig, monitor, workbenchWindow);
	                    }
					});
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

    public static IProject createProject(final String name, Archetype archetype, IVMInstall vmInstall, DBProjectInfo dbInfo, 
    		GenerationConfig generationConfig, IProgressMonitor monitor, IWorkbenchWindow workbenchWindow) throws Exception {
        monitor.beginTask("Creating project " + name, 3);
        JDBCConnectionInfo connectionInfo = dbInfo.getConnectionInfo();
        SchemaInitializationInfo initInfo = dbInfo.getInitInfo();

        IProject project = createEclipseProject(name, monitor);
        File driverJar = (dbInfo.getConnectionInfo() != null ? dbInfo.getConnectionInfo().getLibraryFile() : null);
		createJavaProject(project, vmInstall, driverJar, monitor);		
		File projectRoot = createProjectContent(project, archetype, dbInfo, generationConfig, monitor);
        project.refreshLocal(IResource.DEPTH_INFINITE, monitor); // refresh project structure in Eclipse
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
    	return project;
    }

	private static File createProjectContent(IProject project, Archetype archetype, 
				DBProjectInfo dbInfo, GenerationConfig generationConfig, IProgressMonitor monitor)
            throws FileNotFoundException, IOException, ParseException {
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
            GenerationConfig generationConfig, IProgressMonitor monitor) throws IOException, ParseException {
    	IFile ifile = project.getFile("benerator.xml");
    	File descriptorFile = ifile.getLocation().toFile();

    	DefaultHTMLTokenizer tokenizer = new DefaultHTMLTokenizer(IOUtil.getReaderForURI(descriptorFile.getAbsolutePath()));
    	StringBuilder writer = new StringBuilder(2000);
    	while (tokenizer.nextToken() != HTMLTokenizer.END)
    		processToken(project, dbInfo, generationConfig, tokenizer, writer);
    	IOUtil.writeTextFile(descriptorFile.getAbsolutePath(), writer.toString());

    	monitor.worked(1);
    }

    private static void processToken(IProject project, DBProjectInfo dbInfo,
            GenerationConfig generationConfig, DefaultHTMLTokenizer tokenizer, StringBuilder writer) 
    			throws FileNotFoundException, IOException, ParseException {

		SchemaInitializationInfo initInfo = dbInfo.getInitInfo();
    	String lineSeparator = generationConfig.getLineSeparator();
    	if (lineSeparator == null)
    		lineSeparator = SystemInfo.getLineSeparator();
    	
    	switch (tokenizer.tokenType()) {
    		case HTMLTokenizer.START_TAG: {
    			String nodeName = tokenizer.name();
				if ("setup".equals(nodeName))
    				appendStartTag(nodeName, renderSetupAttributes(tokenizer, generationConfig), writer, lineSeparator);
				else if ("comment".equals(nodeName))
    				processComment(tokenizer, dbInfo, writer);
    			else
    				writer.append(tokenizer.text());
    			break;
    		}
    		case HTMLTokenizer.CLOSED_TAG: {
    			String nodeName = tokenizer.name();
    			if ("database".equals(nodeName) && dbInfo.getConnectionInfo() != null) {
    				appendElement(nodeName, renderDbAttributes(dbInfo, tokenizer), writer, "");
    			} else if ("execute".equals(nodeName)) {
    				Map<String, String> attributes = tokenizer.attributes();
    				String uri = attributes.get("uri");
    				if ("{drop_tables.sql}".equals(uri)) {
    			    	if (getDropScriptFile(initInfo) != null) {
    			    		File dropScriptFile = getDropScriptFile(initInfo);
    						copyToProject(dropScriptFile, project);
    						attributes.put("uri", dropScriptFile.getName());
    						appendElement(nodeName, attributes, writer, "");
    			    	}
    				} else if ("{create_tables.sql}".equals(uri)) {
    			    	if (getCreateScriptFile(initInfo) != null) {
    			    		File createScriptFile = getCreateScriptFile(initInfo);
    						copyToProject(createScriptFile, project);
    						attributes.put("uri", createScriptFile.getName());
    						appendElement(nodeName, attributes, writer, "");
    			    	}
    				} else
    					writer.append(tokenizer.text());
    			} else if ("create-entities".equals(nodeName)) {
    				Map<String, String> attributes = tokenizer.attributes();
    				if (DBUNIT_SNAPSHOT_FILENAME.equals(attributes.get("source"))) {
	    		    	if (isDbUnitSnapshot(dbInfo))
	    		    		appendElement(nodeName, attributes, writer, "");
    				} else if ("table".equals(attributes.get("name"))) {
    					createTables(dbInfo, writer, lineSeparator);
    				} else
    					writer.append(tokenizer.text());
    			} else
    				writer.append(tokenizer.text());
    			break;
    		}
    		default: writer.append(tokenizer.text());
    	}
    }

    private static void processComment(DefaultHTMLTokenizer tokenizer, DBProjectInfo dbInfo, StringBuilder writer) throws IOException, ParseException {
    	String startText = tokenizer.text();
	    int nextToken = tokenizer.nextToken();
	    if (nextToken == HTMLTokenizer.END_TAG) {
	    	writer.append(startText).append(tokenizer.text());
	    	return;
	    }
		if (nextToken != HTMLTokenizer.TEXT)
	    	throw new ParseException("Text expected in comment", -1);
		String commentText = tokenizer.text().trim();
		SchemaInitializationInfo initInfo = dbInfo.getInitInfo();
		if (   (COMMENT_DROP_TABLES.equals(commentText) && getDropScriptFile(initInfo) == null)
			|| (COMMENT_CREATE_TABLES.equals(commentText) && getCreateScriptFile(initInfo) == null) 
			|| (COMMENT_DBUNIT.equals(commentText) && !isDbUnitSnapshot(dbInfo))) {
				while (tokenizer.nextToken() != HTMLTokenizer.END_TAG) {
					// skip all elements until comment end
				}
		} else {
			// write comment start and content
			writer.append(startText).append(tokenizer.text());
		}
    }

	private static boolean isDbUnitSnapshot(DBProjectInfo dbInfo) {
    	SchemaInitializationInfo initInfo = dbInfo.getInitInfo();
    	return (initInfo != null && initInfo.isDbUnitSnapshot());
    }

	private static File getDropScriptFile(SchemaInitializationInfo initInfo) {
	    return (initInfo != null && initInfo.getDropScriptFile() != null ? initInfo.getDropScriptFile() : null);
    }

	private static File getCreateScriptFile(SchemaInitializationInfo initInfo) {
	    return (initInfo != null && initInfo.getCreateScriptFile() != null ? initInfo.getCreateScriptFile() : null);
    }

	private static Map<String, String> renderDbAttributes(DBProjectInfo dbInfo, DefaultHTMLTokenizer tokenizer) {
	    Map<String, String> attributes = tokenizer.attributes();
	    JDBCConnectionInfo con = dbInfo.getConnectionInfo();
	    attributes.put("url", con.getUrl());	    		
	    attributes.put("driver", con.getDriverClass());
	    if (!StringUtil.isEmpty(con.getUrl()))
	    	attributes.put("schema", con.getSchema());
	    attributes.put("user", con.getUser());
	    if (!StringUtil.isEmpty(con.getPassword()))
	    	attributes.put("password", con.getPassword());
	    return attributes;
    }

	private static Map<String, String> renderSetupAttributes(DefaultHTMLTokenizer tokenizer,
            GenerationConfig generationConfig) {
	    Map<String, String> attributes = tokenizer.attributes();
	    if (generationConfig.getEncoding() != null)
	    	attributes.put("defaultEncoding", generationConfig.getEncoding());
	    if (generationConfig.getDataset() != null)
	    	attributes.put("defaultDataset", generationConfig.getDataset());
	    if (generationConfig.getLocale() != null)
	    	attributes.put("defaultLocale", generationConfig.getLocale().toString());
	    if (generationConfig.getLineSeparator() != null)
	    	attributes.put("defaultLineSeparator", StringUtil.escape(generationConfig.getLineSeparator()));
	    return attributes;
    }

    private static void appendStartTag(String nodeName, Map<String, String> attributes, StringBuilder writer, String lineSeparator) {
	    writer.append('<').append(nodeName);
	    writeAttributes(attributes, writer, lineSeparator);
	    writer.append('>');
    }

    private static void appendElement(String nodeName, Map<String, String> attributes, StringBuilder writer, String lineSeparator) {
	    writer.append('<').append(nodeName);
	    writeAttributes(attributes, writer, lineSeparator);
	    writer.append("/>");
    }

	private static void writeAttributes(Map<String, String> attributes, StringBuilder writer, String lineSeparator) {
		int i = 0;
	    for (Map.Entry<String, String> attribute : attributes.entrySet()) {
	    	if (!StringUtil.isEmpty(lineSeparator) && i > 0) 
	    		writer.append(TAB).append(TAB);
	    	else
	    		writer.append(' ');
	    	writer.append(attribute.getKey()).append("=\"").append(attribute.getValue()).append("\"");
	    	if (i < attributes.size() - 1)
	    		writer.append(lineSeparator);
	    	i++;
	    }
    }

	private static void copyToProject(File srcFile, IProject project) throws FileNotFoundException, IOException {
        File projectRoot = project.getLocation().toFile();
    	File dstFile = new File(projectRoot, srcFile.getName());
    	FileUtil.copy(srcFile, dstFile, true);
    }

    private static void createTables(DBProjectInfo dbInfo, StringBuilder writer, String lineSeparator) {
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
			createEntity(iDesc, writer, lineSeparator);
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
	
	private static void createEntity(InstanceDescriptor descriptor, StringBuilder writer, String lineSeparator) {
        ComplexTypeDescriptor type = (ComplexTypeDescriptor) descriptor.getType();
        Map<String, String> attributes = new OrderedMap<String, String>();
        for (FeatureDetail<? extends Object> detail : descriptor.getDetails()) {
            Object value = detail.getValue();
            if (value != null && !NullSafeComparator.equals(value, detail.getDefault()))
                attributes.put(detail.getName(), toStringConverter.convert(value));
        }
        attributes.put("consumer", "db");
        appendStartTag("create-entities", attributes, writer, "");
        writer.append(lineSeparator);
        
        for (ComponentDescriptor cd : type.getComponents())
            addAttribute(cd, writer, lineSeparator);
        writer.append(TAB);
        appendEndElement("create-entities", writer);
        writer.append(lineSeparator);
        writer.append(lineSeparator);
        writer.append(TAB);
    }
	
    private static void appendEndElement(String nodeName, StringBuilder writer) {
	    writer.append("</").append(nodeName).append(">");
    }

	private static void addAttribute(ComponentDescriptor component, StringBuilder writer, String lineSeparator) {
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

        writer.append(TAB).append(TAB).append("<!--").append(elementName);
    	for (Map.Entry<String, String> entry : attributes.entrySet())
    		writer.append(' ').append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
    	writer.append(" /-->");
    	writer.append(lineSeparator);
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

	private static IJavaProject createJavaProject(IProject project, IVMInstall vmInstall, File driverJar, IProgressMonitor monitor)
            throws CoreException, JavaModelException {
	    // create Java project
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] classpathEntries = createFoldersAndClasspath(vmInstall, javaProject, driverJar);
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
    		IVMInstall vmInstall, IJavaProject javaProject, File driverJar) throws CoreException {
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
	    
	    // add custom driver JAR
	    if (driverJar != null)
	    	builder.append(JavaCore.newLibraryEntry(Path.fromOSString(driverJar.getAbsolutePath()), null, null));
               
		// done - return an array representation of the classpath
	    return builder.toArray();
    }

}
