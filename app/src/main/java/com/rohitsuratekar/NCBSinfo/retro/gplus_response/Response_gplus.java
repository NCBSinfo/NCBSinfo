
package com.rohitsuratekar.NCBSinfo.retro.gplus_response;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response_gplus {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("emails")
    @Expose
    private List<Email> emails = new ArrayList<Email>();
    @SerializedName("urls")
    @Expose
    private List<Url> urls = new ArrayList<Url>();
    @SerializedName("objectType")
    @Expose
    private String objectType;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("organizations")
    @Expose
    private List<Organization> organizations = new ArrayList<Organization>();
    @SerializedName("placesLived")
    @Expose
    private List<PlacesLived> placesLived = new ArrayList<PlacesLived>();
    @SerializedName("isPlusUser")
    @Expose
    private Boolean isPlusUser;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("cover")
    @Expose
    private Cover cover;

    /**
     * 
     * @return
     *     The kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * 
     * @param kind
     *     The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * 
     * @return
     *     The etag
     */
    public String getEtag() {
        return etag;
    }

    /**
     * 
     * @param etag
     *     The etag
     */
    public void setEtag(String etag) {
        this.etag = etag;
    }

    /**
     * 
     * @return
     *     The occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * 
     * @param occupation
     *     The occupation
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * 
     * @return
     *     The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 
     * @param gender
     *     The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 
     * @return
     *     The emails
     */
    public List<Email> getEmails() {
        return emails;
    }

    /**
     * 
     * @param emails
     *     The emails
     */
    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    /**
     * 
     * @return
     *     The urls
     */
    public List<Url> getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    /**
     * 
     * @return
     *     The objectType
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * 
     * @param objectType
     *     The objectType
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @return
     *     The name
     */
    public Name getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The image
     */
    public Image getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The organizations
     */
    public List<Organization> getOrganizations() {
        return organizations;
    }

    /**
     * 
     * @param organizations
     *     The organizations
     */
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    /**
     * 
     * @return
     *     The placesLived
     */
    public List<PlacesLived> getPlacesLived() {
        return placesLived;
    }

    /**
     * 
     * @param placesLived
     *     The placesLived
     */
    public void setPlacesLived(List<PlacesLived> placesLived) {
        this.placesLived = placesLived;
    }

    /**
     * 
     * @return
     *     The isPlusUser
     */
    public Boolean getIsPlusUser() {
        return isPlusUser;
    }

    /**
     * 
     * @param isPlusUser
     *     The isPlusUser
     */
    public void setIsPlusUser(Boolean isPlusUser) {
        this.isPlusUser = isPlusUser;
    }

    /**
     * 
     * @return
     *     The verified
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * 
     * @param verified
     *     The verified
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * 
     * @return
     *     The cover
     */
    public Cover getCover() {
        return cover;
    }

    /**
     * 
     * @param cover
     *     The cover
     */
    public void setCover(Cover cover) {
        this.cover = cover;
    }

}
