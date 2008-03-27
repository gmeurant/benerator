package org.databene.benerator.factory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.benerator.Generator;
import org.databene.benerator.sample.ConstantGenerator;
import org.databene.benerator.wrapper.AlternativeGenerator;
import org.databene.benerator.wrapper.ByteArrayGenerator;
import org.databene.benerator.wrapper.ConvertingGenerator;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.TimeUtil;
import org.databene.commons.Validator;
import org.databene.commons.converter.AnyConverter;
import org.databene.commons.converter.ParseFormatConverter;
import org.databene.commons.converter.String2DateConverter;
import org.databene.commons.validator.StringLengthValidator;
import org.databene.model.data.DataModel;
import org.databene.model.data.PrimitiveType;
import org.databene.model.data.SimpleTypeDescriptor;
import org.databene.model.data.UnionSimpleTypeDescriptor;
import org.databene.model.function.Distribution;
import org.databene.model.function.Sequence;
import org.databene.platform.bean.BeanDescriptorProvider;
import static org.databene.model.data.SimpleTypeDescriptor.*;

public class SimpleTypeGeneratorFactory extends TypeGeneratorFactory {

    public static Generator<? extends Object> create(SimpleTypeDescriptor descriptor, boolean unique,
            Context context, GenerationSetup setup) {
        if (logger.isDebugEnabled())
            logger.debug("create(" + descriptor.getName() + ')');
        Generator<? extends Object> generator = null;
        // create a source generator
        generator = createByGeneratorName(descriptor, context);
        if (generator == null)
            generator = createSourceAttributeGenerator(descriptor, context, setup);
        if (generator == null)
            generator = createScriptGenerator(descriptor, context, setup.getDefaultScript());
        if (generator == null)
            generator = createSampleGenerator(descriptor, unique);
        if (generator == null)
            generator = createTypeGenerator(descriptor, unique, context, setup);
        if (generator == null)
            generator = createStringGenerator(descriptor, unique);
        // by now, we must have created a generator
        if (generator == null)
            throw new ConfigurationError("Don't know how to handle descriptor " + descriptor);
        // create wrappers
        generator = createConvertingGenerator(descriptor, generator);
        generator = createTypeConvertingGenerator(descriptor, generator);
        generator = createValidatingGenerator(descriptor, generator);
        generator = createProxy(descriptor, generator);
        if (logger.isDebugEnabled())
            logger.debug("Created " + generator);
        return generator;
    }
    
/*
    private static Generator<? extends Object> createTypeGenerator(PartDescriptor partDescriptor, Generator<? extends Object> generator) {
        if (partDescriptor.getType() == null)
            return generator;
        Class<? extends Object> targetType = javaClassFor(partDescriptor.getType());
        return createTypeGenerator(targetType, partDescriptor);
    }
*/
    private static <T> Generator<T> createTypeConvertingGenerator(
            SimpleTypeDescriptor descriptor, Generator<? extends Object> generator) {
        if (descriptor.getPrimitiveType() == null)
            return (Generator<T>) generator;
        PrimitiveType<T> primitiveType = descriptor.getPrimitiveType();
        Class<T> targetType = primitiveType.getJavaType();
        Converter<Object, T> converter = null;
        if (Date.class.equals(targetType) && generator.getGeneratedType() == String.class) {
            // String needs to be converted to Date
            if (descriptor.getPattern() != null) {
                // We can use the SimpleDateFormat with a pattern
                String pattern = descriptor.getPattern();
                converter = new ParseFormatConverter(Date.class, new SimpleDateFormat(pattern));
            } else {
                // we need to expect the standard date format
                converter = new String2DateConverter();
            }
        }
        if (converter == null)
            converter = new AnyConverter<Object, T>(targetType);
        return new ConvertingGenerator<Object, T>((Generator<Object>)generator, converter);
    }


    public static Generator<? extends Object> createTypeGenerator(
            SimpleTypeDescriptor descriptor, boolean unique, Context context, GenerationSetup setup) {
        if (descriptor instanceof UnionSimpleTypeDescriptor)
            return createUnionTypeGenerator((UnionSimpleTypeDescriptor) descriptor, context, setup);
        PrimitiveType<Object> primitiveType = descriptor.getPrimitiveType();
        if (primitiveType == null)
            return null;
        Class targetType = primitiveType.getJavaType();
        if (Number.class.isAssignableFrom(targetType)) {
            return createNumberGenerator(descriptor, targetType, unique);
        } else if (String.class.isAssignableFrom(targetType)) {
            return createStringGenerator(descriptor, unique);
        } else if (Boolean.class == targetType) {
            return createBooleanGenerator(descriptor);
        } else if (Character.class == targetType) {
            return createCharacterGenerator(descriptor, unique);
        } else if (Date.class == targetType) {
            return createDateGenerator(descriptor, unique);
        } else if (Timestamp.class == targetType) {
            return createTimestampGenerator(descriptor, unique);
        } else if (byte[].class == targetType) {
            return createByteArrayGenerator(descriptor);
        } else
            return null;
    }

    private static Generator<? extends Object> createUnionTypeGenerator(
            UnionSimpleTypeDescriptor descriptor, Context context, GenerationSetup setup) {
        int n = descriptor.getAlternatives().size();
        DataModel dataModel = DataModel.getDefaultInstance();
        Generator[] sources = new Generator[n];
        for (int i = 0; i < n; i++) {
            SimpleTypeDescriptor alternative = descriptor.getAlternatives().get(i);
            sources[i] = create(alternative, false, context, setup);
        }
        Class<Object> javaType = descriptor.getPrimitiveType().getJavaType();
        return new AlternativeGenerator(javaType, sources);
    }
    private static Generator<? extends Object> createByteArrayGenerator(
            SimpleTypeDescriptor descriptor) {
        Generator<Byte> byteGenerator = GeneratorFactory.getNumberGenerator(Byte.class, (byte)-128, (byte)127, (byte)1, Sequence.RANDOM, 0);
        return new ByteArrayGenerator(byteGenerator, 
                getMinLength(descriptor), getMaxLength(descriptor));
    }

    private static Generator<Timestamp> createTimestampGenerator(SimpleTypeDescriptor descriptor, boolean unique) {
        Generator<Date> dateGenerator = createDateGenerator(descriptor, unique);
        return new ConvertingGenerator<Date, Timestamp>(dateGenerator, new AnyConverter(Timestamp.class));
    }

    private static Generator<Date> createDateGenerator(SimpleTypeDescriptor descriptor, boolean unique) {
        Date min = parseDate(descriptor, MIN, TimeUtil.date(1970, 0, 1));
        Date max = parseDate(descriptor, MAX, TimeUtil.today().getTime());
        Date precisionDate = parseDate(descriptor, PRECISION, TimeUtil.date(1970, 0, 2));
        long precision = precisionDate.getTime() - TimeUtil.date(1970, 0, 1).getTime();
        Distribution distribution = getDistribution(descriptor, unique);
        return GeneratorFactory.getDateGenerator(min, max, precision, distribution, 0);
    }

    private static Generator<Character> createCharacterGenerator(SimpleTypeDescriptor descriptor, boolean unique) {
        String pattern = descriptor.getPattern();
        if (pattern == null)
            pattern = ".";
        Locale locale = getLocale(descriptor);
        if (unique)
            return GeneratorFactory.getUniqueCharacterGenerator(pattern, locale);
        else
            return GeneratorFactory.getCharacterGenerator(pattern, locale, 0);
    }

    private static Date parseDate(SimpleTypeDescriptor descriptor, String detailName, Date defaultDate) {
        try {
            String detail = (String) descriptor.getDeclaredDetailValue(detailName);
            if (detail != null) {
                DateFormat dateFormat = getPatternAsDateFormat(descriptor);
                return dateFormat.parse(detail);
            } else
                return defaultDate;
        } catch (ParseException e) {
            logger.error(e, e);
            return defaultDate;
        }
    }

    private static Generator<Boolean> createBooleanGenerator(SimpleTypeDescriptor descriptor) {
        Double trueQuota = descriptor.getTrueQuota();
        if (trueQuota == null)
            trueQuota = (Double) descriptor.getDetailDefault(TRUE_QUOTA);
        return GeneratorFactory.getBooleanGenerator(trueQuota, 0);
    }

    private static <T extends Number> Generator<T> createNumberGenerator(
            SimpleTypeDescriptor descriptor, Class<T> targetType, boolean unique) {
        T min = getNumberDetail(descriptor, MIN, targetType);
        T max = getNumberDetail(descriptor, MAX, targetType);
        if (min.equals(max)) {
            return new ConstantGenerator<T>(min);
        }
        Integer totalDigits = getNumberDetail(descriptor, "totalDigits", Integer.class);
        Integer fractionDigits = getNumberDetail(descriptor, "fractionDigits", Integer.class);
        T precision = getNumberDetail(descriptor, PRECISION, targetType);
        Distribution distribution = getDistribution(descriptor, unique);
        T variation1 = getNumberDetail(descriptor, VARIATION1, targetType);
        T variation2 = getNumberDetail(descriptor, VARIATION2, targetType);
        return GeneratorFactory.getNumberGenerator(
                targetType, min, max, totalDigits, fractionDigits, precision, distribution, variation1, variation2, 0);
    }

    public static Generator<String> createStringGenerator(SimpleTypeDescriptor descriptor, boolean unique) {
        // evaluate max length
        Integer maxLength = (Integer) descriptor.getDeclaredDetailValue(MAX_LENGTH);
        if (maxLength == null) {
            // maxLength was not set in this descriptor. So check the parent setting's value 
            // (it is interpreted as constraint which may be too high to be useful by default)
            maxLength = descriptor.getMaxLength();
            if (maxLength == null)
                maxLength = (Integer) descriptor.getDetailDefault(MAX_LENGTH);
            if (maxLength == null || maxLength > 10000)
                maxLength = 10000;
        }

        // evaluate min length
        Integer minLength = descriptor.getMinLength();
        if (minLength == null) {
            int defaultMinLength = (Integer) descriptor.getDetailDefault(MIN_LENGTH);
            minLength = Math.min(maxLength, defaultMinLength);
        }

        // evaluate pattern
        String pattern = descriptor.getPattern();
        if (pattern == null)
            pattern = (String) descriptor.getDetailDefault(PATTERN);
        if (pattern == null)
            pattern = "[A-Z]{" + minLength + ',' + (maxLength != null ? maxLength : 30) + '}';

        // evaluate locale
        Locale locale = descriptor.getLocale();
        if (locale == null)
            locale = (Locale) descriptor.getDetailDefault(LOCALE);
        // evaluate uniqueness and create generator
        if (unique)
            return GeneratorFactory.getUniqueRegexStringGenerator(pattern, minLength, maxLength, locale);
        else
            return GeneratorFactory.getRegexStringGenerator(pattern, minLength, maxLength, locale, 0);
    }
    
    // constuction helpers ---------------------------------------------------------------------------------------------
    
    protected static <T> Validator<T> createRestrictionValidator(
            SimpleTypeDescriptor descriptor, boolean nullable) {
        if ((descriptor.getMinLength() != null || descriptor.getMaxLength() != null) && "string".equals(descriptor.getName())) {
            Integer minLength = getMinLength(descriptor);
            Integer maxLength = getMaxLength(descriptor);
            return (Validator<T>)new StringLengthValidator(minLength, maxLength, nullable);
        }
        return null;
    }

    // descriptor access -----------------------------------------------------------------------------------------------

    protected static Integer getMinLength(SimpleTypeDescriptor descriptor) {
        Integer minLength = descriptor.getMinLength();
        if (minLength == null)
            minLength = 0;
        return minLength;
    }

    protected static Integer getMaxLength(SimpleTypeDescriptor descriptor) {
        // evaluate max length
        Integer maxLength = (Integer) descriptor.getDeclaredDetailValue(MAX_LENGTH);
        if (maxLength == null) {
            // maxLength was not set in this descriptor. So check the parent setting's value 
            // (it is interpreted as constraint which may be to high to be useful by default)
            maxLength = descriptor.getMaxLength();
            if (maxLength == null)
                maxLength = (Integer) descriptor.getDetailDefault(MAX_LENGTH);
            if (maxLength > 10000)
                maxLength = 10000;
        }
        return maxLength;
    }

    private static <T extends Number> T getNumberDetail(SimpleTypeDescriptor descriptor, String detailName, Class<T> targetType) {
        try {
            String detailValue = (String) descriptor.getDetailValue(detailName);
            if (detailValue == null)
                detailValue = (String) descriptor.getDetailDefault(detailName);
            return AnyConverter.convert(detailValue, targetType);
        } catch (ConversionException e) {
            throw new ConfigurationError(e);
        }
    }

    private static final BeanDescriptorProvider beanDescriptorProvider = new BeanDescriptorProvider();
    
    private static <T> T javaClassFor(String type) {
        return (T)beanDescriptorProvider.concreteType(type);
    }

    private SimpleTypeGeneratorFactory() {}
    
    private static final Log logger = LogFactory.getLog(ComponentGeneratorFactory.class);

}
