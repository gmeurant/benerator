/*
 * (c) Copyright 2008-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.domain.net;

import java.io.IOException;

import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.primitive.LightweightStringGenerator;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Converter;
import org.databene.commons.StringUtil;
import org.databene.commons.converter.ThreadSafeConverter;
import org.databene.domain.address.Country;
import org.databene.domain.organization.CompanyNameGenerator;
import org.databene.text.DelocalizingConverter;

/**
 * Generates web domains for companies.<br/><br/>
 * Created at 23.04.2008 23:04:10
 * @since 0.5.2
 * @author Volker Bergmann
 */
public class CompanyDomainGenerator extends LightweightStringGenerator {

	private CompanyNameGenerator companyNameGenerator;
	private TopLevelDomainGenerator tldGenerator;
	private Converter<String, String> normalizer;
	
	public CompanyDomainGenerator() {
		this(Country.getDefault().getIsoCode());
	}

	public CompanyDomainGenerator(String datasetName) {
		companyNameGenerator = new CompanyNameGenerator(false, false, false, datasetName);
		tldGenerator = new TopLevelDomainGenerator();
		normalizer = new Normalizer();
	}

	public void setDataset(String datasetName) {
		companyNameGenerator.setDataset(datasetName);
	}

	@Override
	public synchronized void init(BeneratorContext context) {
		companyNameGenerator.init(context);
		tldGenerator.init(context);
	    super.init(context);
	}
	
	public String generate() {
		return normalizer.convert(companyNameGenerator.generate()) + '.' + tldGenerator.generate();
	}
	
	private static final class Normalizer extends ThreadSafeConverter<String, String> {
		
		private DelocalizingConverter delocalizer;

		public Normalizer() {
			super(String.class, String.class);
			try {
				this.delocalizer = new DelocalizingConverter();
			} catch (IOException e) {
				throw new ConfigurationError(e);
			}
		}

		public String convert(String sourceValue) {
			sourceValue = StringUtil.normalizeSpace(sourceValue);
			sourceValue = delocalizer.convert(sourceValue);
			sourceValue = sourceValue.replace(' ', '-');
			return sourceValue.toLowerCase();
		}

	}

}
