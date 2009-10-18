package org.databene.domain.person;

import org.databene.commons.StringUtil;

import java.util.Date;
import java.text.DateFormat;

/**
 * (c) Copyright 2006 by Volker Bergmann
 * Created: 09.06.2006 21:51:25
 */
public class Person {

    private static final DateFormat df = DateFormat.getDateInstance();

    private String givenName;
    private String secondGivenName;
    private String familyName;
    private Gender gender;
    private String salutation;
    private String title;
    private Date birthDate;

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSecondGivenName() {
    	return secondGivenName;
    }

    public void setSecondGivenName(String secondGivenName) {
    	this.secondGivenName = secondGivenName;
    }

	public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return salutation + ' ' + (!StringUtil.isEmpty(title) ? title + " " : "") + givenName + ' ' + 
        	(secondGivenName != null ? secondGivenName + ' ' : "") + familyName + ", *" + df.format(birthDate);
    }
    
}
