package org.databene.script.quickscript;

import java.io.IOException;

import org.databene.script.Script;
import org.databene.script.ScriptFactory;

public class QuickScriptFactory implements ScriptFactory{

    public Script parseText(String text) {
        return QuickScript.parseText(text);
    }

    public Script readFile(String uri) throws IOException {
        return new QuickScript(uri);
    }

}
