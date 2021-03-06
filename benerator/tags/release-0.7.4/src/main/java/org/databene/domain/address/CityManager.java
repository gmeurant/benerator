/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.domain.address;

import org.databene.document.csv.BeanCSVWriter;
import org.databene.document.csv.CSVLineIterator;
import org.databene.model.data.ComplexTypeDescriptor;
import org.databene.model.data.Entity;
import org.databene.platform.csv.CSVEntityIterator;
import org.databene.platform.java.BeanDescriptorProvider;
import org.databene.platform.java.Entity2JavaConverter;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.DataIterator;
import org.databene.webdecs.util.ConvertingDataIterator;
import org.databene.commons.*;
import org.databene.commons.converter.NoOpConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Reads and persists city files in CSV format (column header = property name).<br/>
 * <br/>
 * Created: 28.07.2007 15:21:12
 * @author Volker Bergmann
 */
public class CityManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityManager.class);
    
    public static void readCities(Country country) {
        String filename = "/org/databene/domain/address/city_" + country.getIsoCode() + ".csv";
        if (IOUtil.isURIAvailable(filename))
            readCities(country, filename, new HashMap<String, String>());
        else
        	LOGGER.warn("File not found: " + filename);
    }

    public static void readCities(Country country, String filename, Map<String, String> defaults) {
    	try {
	    	parseStateFile(country);
	        int warnCount = parseCityFile(country, filename, defaults);
	        if (warnCount > 0)
	            LOGGER.warn(warnCount + " warnings");
    	} catch (IOException e) {
    		throw new ConfigurationError("Error reading cities file: " + filename, e);
    	}
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
    private static void parseStateFile(Country country) {
		try {
			String uri = "/org/databene/domain/address/state_" + country.getIsoCode() + ".csv";
			ComplexTypeDescriptor stateDescriptor = (ComplexTypeDescriptor) new BeanDescriptorProvider().getTypeDescriptor(State.class.getName());
			DataIterator<State> iterator = new ConvertingDataIterator<Entity, State>(
					new CSVEntityIterator(uri, stateDescriptor, new NoOpConverter<String>(), ',', Encodings.UTF_8),
					(Converter) new Entity2JavaConverter());
			DataContainer<State> state = new DataContainer<State>();
			while ((state = iterator.next(state)) != null)
				country.addState(state.getData());
		} catch (FileNotFoundException e) {
			LOGGER.warn("No state definition file found: " + e.getMessage());
		}
	}

	private static int parseCityFile(Country country, String filename, Map<String, String> defaults) throws IOException {
		CSVLineIterator iterator = new CSVLineIterator(filename, ';', Encodings.UTF_8);
		DataContainer<String[]> container = new DataContainer<String[]>();
        String[] header = iterator.next(container).getData();
        int warnCount = 0;
        while ((container = iterator.next(container)) != null) {
            String[] cells = container.getData();
            if (cells.length == 0)
                continue;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(ArrayFormat.format(";", cells));
            if (cells.length == 1)
                continue;
            Map<String, String> instance = new HashMap<String, String>();
            for (int i = 0; i < cells.length; i++) {
                instance.put(header[i], cells[i]);
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(instance.toString());

            // create/setup state
            String stateId = instance.get("state.id");
            State state = country.getState(stateId);
            if (state == null) {
                state = new State(stateId);
                String stateName = instance.get("state.name");
				if (stateName != null) {
					stateName = StringUtil.normalizeName(stateName);
                	state.setName(stateName);
				}
				//logger.debug(state.getId() + "," + state.getName());
                country.addState(state);
            }

            CityId cityId;
            if (!StringUtil.isEmpty(instance.get("municipality")))
            	cityId = new CityId(instance.get("municipality"), null);
            else if (!StringUtil.isEmpty(instance.get("city")))
                cityId = new CityId(instance.get("city"), null);
            else if (!StringUtil.isEmpty(instance.get("name"))) {
                String cityName = instance.get("name");
                String cityNameExtension = instance.get("nameExtension");
                cityId = new CityId(cityName, cityNameExtension);
            } else 
            	throw new ParseException("Unable to parse city", instance.toString(), iterator.lineCount(), 1);

            // create/setup city
            CityHelper city = (CityHelper) state.getCity(cityId);
            String postalCode = instance.get("postalCode");
            String lang = getValue(instance, "language", defaults);
            if (city == null) {
                String areaCode = getValue(instance, "areaCode", defaults);
                if (StringUtil.isEmpty(areaCode)) {
                    warnCount++;
                    LOGGER.warn("areaCode is not provided for city: '" + cityId);
                }
                city = new CityHelper(state, cityId, new String[] { postalCode }, areaCode);
                if (!StringUtil.isEmpty(lang))
                	city.setLanguage(LocaleUtil.getLocale(lang));
                state.addCity(cityId, city);
                city.setState(state);
            } else
                city.addPostalCode(postalCode);
        }
		return warnCount;
	}

    public static void persistCities(Country country, String filename) throws IOException {
        // persist city data in standard format
        BeanCSVWriter<City> writer = new BeanCSVWriter<City>(new FileWriter(filename), ';',
                "state.country.isoCode", "state.id", "name", "nameExtension",
                "zipCode", "areaCode", "language");
        for (State state : country.getStates()) {
            for (City city : state.getCities())
                for (String zipCode : city.getPostalCodes()) {
                    ((CityHelper)city).setPostalCode(zipCode);
                    writer.writeElement(city);
                }
        }
        writer.close();
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private static String getValue(Map<String, String> instance, String key, Map<String, String> defaults) {
        String value = instance.get(key);
        if (value == null)
            value = defaults.get(key);
        return value;
    }

    public static class CityHelper extends City {

        private String postalCode;

        public CityHelper(State state, CityId cityId, String[] zipCodes, String areaCode) {
            super(state, cityId.getName(), cityId.getNameExtension(), zipCodes, areaCode);
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String zipCode) {
            this.postalCode = zipCode;
        }
    }

}
