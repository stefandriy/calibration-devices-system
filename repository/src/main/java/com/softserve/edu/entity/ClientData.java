package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class ClientData {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private String secondPhone;

    @Embedded
    private Address clientAddress;

    protected ClientData() {
    }

    public ClientData(String firstName, String lastName, String middleName,
                      String phone,String secondPhone, Address clientAddress) {
        this(firstName, lastName, middleName, null, phone,secondPhone, clientAddress);
    }

    public ClientData(String firstName, String lastName, String middleName, String email,
                      String phone,String secondPhone, Address clientAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.phone = phone;
        this.setSecondPhone(secondPhone);
        this.clientAddress = clientAddress;
    }

    public String getFullName(){
        return this.lastName + " " + this.firstName + " " + this.middleName;
    }

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public String getSecondPhone() {
		return secondPhone;
	}

	public void setSecondPhone(String secondPhone) {
		this.secondPhone = secondPhone;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("middleName", middleName)
                .append("email", email)
                .append("phone", phone)
                .append("secondPhone", secondPhone)
                .toString();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(firstName)
                .append(lastName)
                .append(middleName)
                .append(email)
                .append(phone)
                .append(secondPhone)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof ClientData){
            final ClientData other = (ClientData) obj;
            return new EqualsBuilder()
                    .append(firstName, other.firstName)
                    .append(lastName, other.lastName)
                    .append(middleName, other.middleName)
                    .append(email, other.email)
                    .append(phone, other.phone)
                    .append(secondPhone, other.secondPhone)
                    .isEquals();
        } else{
            return false;
        }
    }
}
