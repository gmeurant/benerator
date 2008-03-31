/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.databene.benerator.sample.WeightedSample;
import org.databene.benerator.sample.WeightedSampleGenerator;
import org.databene.benerator.wrapper.GeneratorProxy;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Converter;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.NoOpConverter;
import org.databene.dataset.Dataset;
import org.databene.dataset.DatasetFactory;
import org.databene.document.csv.CSVLineIterator;

/**
 * Generates data from a csv file set that is organized as {@link Dataset}.
 * For different regions, different CSV versions may be provided by appending region suffixes,
 * similar to the JDK ResourceBundle handling.<br/>
 * <br/>
 * Created: 21.03.2008 16:32:04
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class DatasetCSVGenerator<E> extends GeneratorProxy <E> {
    
    // TODO v0.5.1 support uniqueness

    private String filenamePattern;
    private String datasetName;
    private String nesting;

    // constructors ----------------------------------------------------------------------------------------------------
    public DatasetCSVGenerator(String filenamePattern, String datasetName, String nesting) {
        this(filenamePattern, datasetName, nesting, SystemInfo.fileEncoding());
    }

    public DatasetCSVGenerator(String filenamePattern, String datasetName, String nesting, String encoding) {
        this(filenamePattern, datasetName, nesting, encoding, (Converter<String, E>) new NoOpConverter());
    }

    public DatasetCSVGenerator(String filenamePattern, String datasetName, String nesting, String encoding, Converter<String, E> converter) {
        super(new WeightedSampleGenerator<E>());
        ((WeightedSampleGenerator<E>)source).setSamples(createSamples(datasetName, nesting, filenamePattern, encoding, converter));
        this.nesting = nesting;
        this.filenamePattern = filenamePattern;
        this.datasetName = datasetName;
    }
    
    // private helpers -------------------------------------------------------------------------------------------------

    private static <T> List<WeightedSample<T>> createSamples(
            String datasetName, String nesting, String filenamePattern,
            String encoding, Converter<String, T> converter) {
        List<WeightedSample<T>> samples = new ArrayList<WeightedSample<T>>();
        String[] dataFilenames = DatasetFactory.getDataFiles(filenamePattern, datasetName, nesting);
        for (String dataFilename : dataFilenames)
            parse(dataFilename, encoding, converter, samples);
        return samples;
    }
    
    private static <T> void parse(String filename, String encoding, Converter<String, T> converter, List<WeightedSample<T>> samples) {
        try {
            CSVLineIterator iterator = new CSVLineIterator(filename, ',', encoding);
            while (iterator.hasNext()) {
                String[] tokens = iterator.next();
                if (tokens.length == 0)
                    continue;
                double weight = (tokens.length < 2 ? 1. : Double.parseDouble(tokens[1]));
                T value = converter.convert(tokens[0]);
                WeightedSample<T> sample = new WeightedSample<T>(value, weight);
                samples.add(sample);
            }
        } catch (IOException e) {
            throw new ConfigurationError(e);
        }
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    public String toString() {
        return getClass().getSimpleName() + '[' + filenamePattern + ',' + nesting + ':' + datasetName + ']';
    }
}
