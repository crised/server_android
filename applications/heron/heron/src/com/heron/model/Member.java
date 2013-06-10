package com.heron.model;

import java.lang.reflect.Type;
import java.util.Collection;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heron.provider.TelematicMember;

public class Member {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;

    public Member(String name, String email, String phoneNumber) {
        this(0, name, email, phoneNumber);
    }

    public Member(Integer id, String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ContentValues asContentValues() {
        ContentValues memberEntry = new ContentValues();
        memberEntry.put(TelematicMember.Members.MEMBER_ID, id);
        memberEntry.put(TelematicMember.Members.MEMBER_NAME, name);
        memberEntry.put(TelematicMember.Members.EMAIL, email);
        memberEntry.put(TelematicMember.Members.PHONE_NUMBER, phoneNumber);
        return memberEntry;
    }

    public String asJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Member.class);
    }

    public static Member memberFromContentValues(ContentValues cv) {
        return new Member(cv.getAsInteger(BaseColumns._ID), 
                cv.getAsString(TelematicMember.Members.MEMBER_NAME), 
                cv.getAsString(TelematicMember.Members.EMAIL), 
                cv.getAsString(TelematicMember.Members.PHONE_NUMBER));
    }

    public static Member memberFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Member.class);
    }

    public static Collection<Member> membersFromJson(String json) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Member>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

    public static String membersAsJson(Collection<Member> members) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Member>>() {
        }.getType();
        return gson.toJson(members, collectionType);
    }

    @Override
    public String toString() {
        return id + "/" + name + "/" + email + "/" + phoneNumber;
    }
}