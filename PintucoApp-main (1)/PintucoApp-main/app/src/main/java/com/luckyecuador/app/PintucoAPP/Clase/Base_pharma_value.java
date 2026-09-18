package com.luckyecuador.app.PintucoAPP.Clase;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucky Ecuador on 24/04/2017.
 * Modificado por Joffre Herrera 10/02/2019
 */

public class Base_pharma_value {

    @SerializedName("id")
    public String id;//YA
    @SerializedName("region")
    public String region;//YA
    @SerializedName("customer_owner")
    public String customer_owner;//YA
    @SerializedName("tipo")
    public String tipo;//YA
    @SerializedName("pos_id")
    public String pos_id;//YA
    @SerializedName("channel_segment")
    public String channel_segment;//YA
    @SerializedName("pos_name")
    public String pos_name;//YA
    @SerializedName("pos_name_dpsm")
    public String pos_name_dpsm;
    @SerializedName("zone")
    public String zone;//YA
    @SerializedName("province")
    public String province;//YA
    @SerializedName("city")
    public String city;//YA
    @SerializedName("channel")
    public String channel;//YA
    @SerializedName("subchannel")
    public String subchannel;//YA
    @SerializedName("address")
    public String address;//YA
    @SerializedName("latitud")
    public String latitud;//YA
    @SerializedName("longitud")
    public String longitud;//YA
    @SerializedName("user")
    public String user;//YA
    @SerializedName("supervisor")
    public String supervisor;//YA
    @SerializedName("format")
    public String format;//YA
    @SerializedName("segmentacion")
    public String segmentacion;//YA
    @SerializedName("compras")
    public String compras;//YA
    @SerializedName("kam")
    public String kam;//YA
    @SerializedName("mercaderista")
    public String mercaderista;//YA
    @SerializedName("foto")
    public String foto;//YA
    @SerializedName("status")
    public String status;//YA
    @SerializedName("dpsm")
    public String dpsm;//YA
    @SerializedName("merchandising")
    public String merchandising;//YA
    @SerializedName("sales_executive")
    public String sales_executive;//YA


    public Base_pharma_value() {}

    public Base_pharma_value(String id, String region, String customer_owner, String tipo,
                             String pos_id, String channel_segment, String pos_name, String pos_name_dpsm,
                             String zone, String province, String city, String channel, String subchannel,
                             String address, String latitud, String longitud, String user,
                             String supervisor, String format, String segmentacion, String compras) {
        this.id = id;
        this.region = region;
        this.customer_owner = customer_owner;
        this.tipo = tipo;
        this.pos_id = pos_id;
        this.channel_segment = channel_segment;
        this.pos_name = pos_name;
        this.pos_name_dpsm = pos_name_dpsm;
        this.zone = zone;
        this.province = province;
        this.city = city;
        this.channel = channel;
        this.subchannel = subchannel;
        this.address = address;
        this.latitud = latitud;
        this.longitud = longitud;
        this.user = user;
        this.supervisor = supervisor;
        this.format = format;
        this.segmentacion = segmentacion;
        this.compras = compras;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCustomer_owner() {
        return customer_owner;
    }

    public void setCustomer_owner(String customer_owner) {
        this.customer_owner = customer_owner;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getChannel_segment() {
        return channel_segment;
    }

    public void setChannel_segment(String channel_segment) {
        this.channel_segment = channel_segment;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }

    public String getPos_name_dpsm() {
        return pos_name_dpsm;
    }

    public void setPos_name_dpsm(String pos_name_dpsm) {
        this.pos_name_dpsm = pos_name_dpsm;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubchannel() {
        return subchannel;
    }

    public void setSubchannel(String subchannel) {
        this.subchannel = subchannel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSegmentacion() {
        return segmentacion;
    }

    public void setSegmentacion(String segmentacion) {
        this.segmentacion = segmentacion;
    }

    public String getCompras() {
        return compras;
    }

    public void setCompras(String compras) {
        this.compras = compras;
    }
}
