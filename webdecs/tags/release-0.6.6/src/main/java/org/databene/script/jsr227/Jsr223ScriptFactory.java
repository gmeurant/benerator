/*
 * (c) Copyright 2008-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.script.jsr227;

import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.databene.commons.FileUtil;
import org.databene.commons.IOUtil;
import org.databene.script.Script;
import org.databene.script.ScriptFactory;

/**
 * Creates {@link Jsr223Script}s.<br/>
 * <br/>
 * Created at 23.12.2008 07:35:08
 * @since 0.4.7
 * @author Volker Bergmann
 */

public class Jsr223ScriptFactory implements ScriptFactory {

	private static ScriptEngineManager factory = new ScriptEngineManager();
	
	private ScriptEngine engine;
	
	public Jsr223ScriptFactory(ScriptEngine engine) {
		this.engine = engine;
	}

	public Script parseText(String text) {
		return parseText(text, engine);
	}

	public Script readFile(String uri) throws IOException {
		String text = IOUtil.getContentOfURI(uri);
		String type = FileUtil.suffix(uri);
		return parseText(text, type);
	}
	
	public static Script parseText(String text, String engineId) {
		return new Jsr223Script(text, factory.getEngineByName(engineId));
	}

	private static Script parseText(String text, ScriptEngine engine) {
		return new Jsr223Script(text, engine);
	}

}
