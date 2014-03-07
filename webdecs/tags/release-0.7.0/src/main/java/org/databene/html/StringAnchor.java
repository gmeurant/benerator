/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

package org.databene.html;

/**
 * {@link Anchor} implementation for an arbitrary String-based URL.<br/><br/>
 * Created: 13.06.2011 12:50:11
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class StringAnchor extends Anchor {

	public final String href;

	public StringAnchor(String href, String label) {
		super(label);
		this.href = href;
	}

	public StringAnchor(String href, String label, String target) {
		super(label, target);
		this.href = href;
	}

	public static StringAnchor createAnchorForNewWindow(String href, String label) {
		return new StringAnchor(href, label, "_blank");
	}
	
	@Override
	public String toString() {
		return "<a href='" + href + "'" + (target != null ? " target='" + target + "'" : "") + ">" + label + "</a>";
	}
	
}