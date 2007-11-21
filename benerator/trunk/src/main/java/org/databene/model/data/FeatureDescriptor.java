package org.databene.model.data;

import org.databene.model.converter.AnyConverter;
import org.databene.model.Validator;
import org.databene.model.Operation;
import org.databene.commons.OrderedMap;
import org.databene.commons.BeanUtil;

import java.util.List;

/**
 * Created: 17.07.2006 21:30:45
 */
public abstract class FeatureDescriptor {

    protected FeatureDescriptor parent;
    protected OrderedMap<String, FeatureDetail> details;

    // constructor -----------------------------------------------------------------------------------------------------

    public FeatureDescriptor(String name, FeatureDescriptor parent) {
        this.details = new OrderedMap<String, FeatureDetail>();
        this.parent = parent;
        addDetailConfig("name", String.class, true, null);
        addDetailConfig("generator", Object.class, false, null);
        addDetailConfig("source", String.class, false, null);
        addDetailConfig("encoding", String.class, false, null);
        addDetailConfig("pattern", String.class, false, null);
        addDetailConfig("unique", Boolean.class, true, null);
        addDetailConfig("cyclic", Boolean.class, false, null);
        addDetailConfig("selector", String.class, false, null);
        addDetailConfig("mode", Mode.class, false, Mode.normal);
        addDetailConfig("proxy", Iteration.class, false, null);
        addDetailConfig("proxy-param1", Long.class, false, null);
        addDetailConfig("proxy-param2", Long.class, false, null);
        addDetailConfig("validator", String.class, false, null);
        setName(name);
    }

    // typed interface -------------------------------------------------------------------------------------------------

    public void setParent(FeatureDescriptor parent) {
        this.parent = parent;
    }

    public String getName() {
        return (String) getDetailValue("name");
    }

    public void setName(String name) {
        setDetail("name", name);
    }

    public String getPattern() {
        return (String)getDetailValue("pattern");
    }

    public void setPattern(String pattern) {
        setDetail("pattern", pattern);
    }

    public Boolean isUnique() {
        return (Boolean)getDetailValue("unique");
    }

    public void setUnique(Boolean unique) {
        setDetail("unique", unique);
    }

    public String getSelector() {
        return (String) getDetailValue("selector");
    }

    public void setSelector(String selector) {
        setDetail("selector", selector);
    }

    public Mode getMode() {
        return (Mode) getDetailValue("mode");
    }

    public void setMode(Mode mode) {
        setDetail("mode", mode);
    }

    public Boolean isCyclic() {
        return (Boolean ) getDetailValue("cyclic");
    }

    public void setCyclic(boolean cyclic) {
        setDetail("cyclic", cyclic);
    }

    public String getGenerator() {
        return (String) getDetailValue("generator");
    }

    public void setGenerator(String generatorName) {
        setDetail("generator", generatorName);
    }

    public Validator getValidator() {
        String validatorClassName = (String) getDetailValue("validator");
        if (validatorClassName == null)
            return null;
        return (Validator) BeanUtil.newInstance(validatorClassName);
    }

    public void setValidator(String validatorName) {
        setDetail("validator", validatorName);
    }

    public String getSource() {
        return (String) getDetailValue("source");
    }

    public void setSource(String source) {
        setDetail("source", source);
    }

    public String getEncoding() {
        return (String) getDetailValue("encoding");
    }

    public void setEncoding(String encoding) {
        setDetail("encoding", encoding);
    }

    public Iteration getProxy() {
        return (Iteration) getDetailValue("proxy");
    }

    public Long getProxyParam1() {
        return (Long) getDetailValue("proxy-param1");
    }

    public void setProxyParam1(Long param) {
        setDetail("proxy-param1", param);
    }

    public Long getProxyParam2() {
        return (Long) getDetailValue("proxy-param2");
    }

    public void setProxyParam2(Long param) {
        setDetail("proxy-param2", param);
    }

    // literal construction helpers ------------------------------------------------------------------------------------

    public FeatureDescriptor withParent(FeatureDescriptor parent) {
        this.parent = parent;
        return this;
    }

    public FeatureDescriptor withName(String name) {
        setName(name);
        return this;
    }

    public FeatureDescriptor withPattern(String pattern) {
        setPattern(pattern);
        return this;
    }

    public FeatureDescriptor withUnique(Boolean unique) {
        setDetail("unique", unique);
        return this;
    }

    public FeatureDescriptor withSelector(String selector) {
        setSelector(selector);
        return this;
    }

    public FeatureDescriptor withMode(Mode mode) {
        setMode(mode);
        return this;
    }

    public FeatureDescriptor withCyclic(boolean cyclic) {
        setCyclic(cyclic);
        return this;
    }

    public FeatureDescriptor withGenerator(String generatorName) {
        setGenerator(generatorName);
        return this;
    }

    public FeatureDescriptor withValidator(String validatorName) {
        setValidator(validatorName);
        return this;
    }

    public FeatureDescriptor withSource(String source) {
        setSource(source);
        return this;
    }

    public FeatureDescriptor withProxyParam1(Long param) {
        setProxyParam1(param);
        return this;
    }

    public FeatureDescriptor withProxyParam2(Long param) {
        setProxyParam2(param);
        return this;
    }

    // generic detail access -------------------------------------------------------------------------------------------

    public boolean supportsDetail(String name) {
        return (details.get(name) != null);

    }

    public Object getDeclaredDetailValue(String name) {
        return getDeclaredDetail(name).getValue();
    }

    public Object getDetailValue(String name) {
        FeatureDetail detail = getDeclaredDetail(name);
        Object value = detail.getValue();
        if (value == null && parent != null && parent.supportsDetail(name) && detail.isConstraint())
            value = parent.getDetailValue(name);
//        if (value == null)
//            value = detail.getDefault();
        return value;
    }

    public <T> void setDetail(String detailName, T detailValue) {
        FeatureDetail<T> detail = getDeclaredDetail(detailName);
        if (detail == null)
            throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support detail type: " + detailName);
        detail.setValue(AnyConverter.convert(detailValue, detail.getType()));
    }

    public Object getDetailDefault(String name) {
        return getDeclaredDetail(name).getDefault();
    }

    public List<FeatureDetail> getDetails() {
        return details.values();
    }

    // java.lang.io overrides ------------------------------------------------------------------------------------------

    public String toString() {
        StringBuilder buffer = new StringBuilder(getName()).append("[");
        boolean empty = true;
        for (FeatureDetail descriptor : details.values())
            if (descriptor.getValue() != null && !"name".equals(descriptor.getName())) {
                if (!empty)
                    buffer.append(", ");
                empty = false;
                buffer.append(descriptor.getName()).append('=').append(descriptor.getValue());
            }
        return buffer.append("]").toString();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final FeatureDescriptor that = (FeatureDescriptor) o;
        if (!this.details.equals(that.details)) // TODO consider case
            return false;
        return !(parent != null ? !parent.equals(that.parent) : that.parent != null);
    }

    public int hashCode() {
        return (parent != null ? parent.hashCode() : 0) * 29 + details.hashCode();
    }

    // helpers ---------------------------------------------------------------------------------------------------------

    protected Class getDetailType(String detailName) {
        FeatureDetail detail = details.get(detailName);
        if (detail == null)
            throw new UnsupportedOperationException("Feature detail not supported: " + detailName);
        return detail.getType();
    }

    protected <T> void addDetailConfig(String detailName, Class<T> detailType, boolean constraint, T defaultValue, Operation<T,T> combinator) {
        this.details.put(detailName, new FeatureDetail<T>(detailName, detailType, constraint, defaultValue, combinator));
    }

    protected <T> void addDetailConfig(String detailName, Class<T> detailType, boolean constraint, T defaultValue) {
        this.details.put(detailName, new FeatureDetail<T>(detailName, detailType, constraint, defaultValue));
    }

    protected FeatureDetail getDeclaredDetail(String name) {
        FeatureDetail detail = details.get(name);
        if (detail == null)
            throw new UnsupportedOperationException("Feature detail not supported: " + name);
        return detail;
    }

}
