package com.luckyecuador.app.PintucoAPP.Clase;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucky Ecuador on 24/04/2017.
 * Modificado por Joffre Herrera 10/02/2019
 */

public class BasePharmaValue {

    @SerializedName("id")
    public String id;

    @SerializedName("channel")
    public String channel;

    @SerializedName("subchannel")
    public String subchannel;

    @SerializedName("channel_segment")
    public String channel_segment;

    @SerializedName("format")
    public String format;

    @SerializedName("customer_owner")
    public String customer_owner;

    @SerializedName("pos_id")
    public String pos_id;

    @SerializedName("pos_name")
    public String pos_name;

    @SerializedName("pos_name_dpsm")
    public String pos_name_dpsm;

    @SerializedName("zone")
    public String zone;
    
    @SerializedName("region")
    public String region;

    @SerializedName("province")
    public String province;

    @SerializedName("city")
    public String city;

    @SerializedName("address")
    public String address;

    @SerializedName("kam")
    public String kam;

    @SerializedName("sales_executive")
    public String sales_executive;

    @SerializedName("merchandising")
    public String merchandising;

    @SerializedName("supervisor")
    public String supervisor;

    @SerializedName("mercaderista")
    public String mercaderista;

    @SerializedName("user")
    public String user;

    @SerializedName("dpsm")
    public String dpsm;

    @SerializedName("status")
    public String status;

    @SerializedName("tipo")
    public String tipo;

    @SerializedName("latitud")
    public String latitud;
    
    @SerializedName("longitud")
    public String longitud;

    @SerializedName("foto")
    public String foto;

    @SerializedName("segmentacion")
    public String segmentacion;

    @SerializedName("compras")
    public String compras;

    @SerializedName("pass")
    public String pass;

    @SerializedName("numero_controller")
    public String numero_controller;

    @SerializedName("fecha_visita")
    public String fecha_visita;

    @SerializedName("device_id")
    public String device_id;

    @SerializedName("perimetro")
    public String perimetro;

    @SerializedName("distancia")
    public String distancia;

    @SerializedName("termometro")
    public String termometro;

    @SerializedName("modulo")
    public String modulo;

    @SerializedName("hora_inicio")
    public String hora_inicio;

    @SerializedName("hora_fin")
    public String hora_fin;

    public BasePharmaValue() {}

    public BasePharmaValue(String id, String channel, String subchannel, String channel_segment, String format,
                           String customer_owner, String pos_id, String pos_name, String pos_name_dpsm, String zone,
                           String region, String province, String city, String address, String kam, String sales_executive,
                           String merchandising, String supervisor, String mercaderista, String user, String dpsm,
                           String status, String tipo, String latitud, String longitud, String foto, String segmentacion,
                           String compras, String pass, String numero_controller, String fecha_visita, String device_id,
                           String perimetro, String distancia, String termometro,String hora_inicio, String hora_fin) {
        this.id = id;
        this.channel = channel;
        this.subchannel = subchannel;
        this.channel_segment = channel_segment;
        this.format = format;
        this.customer_owner = customer_owner;
        this.pos_id = pos_id;
        this.pos_name = pos_name;
        this.pos_name_dpsm = pos_name_dpsm;
        this.zone = zone;
        this.region = region;
        this.province = province;
        this.city = city;
        this.address = address;
        this.kam = kam;
        this.sales_executive = sales_executive;
        this.merchandising = merchandising;
        this.supervisor = supervisor;
        this.mercaderista = mercaderista;
        this.user = user;
        this.dpsm = dpsm;
        this.status = status;
        this.tipo = tipo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.segmentacion = segmentacion;
        this.compras = compras;
        this.pass = pass;
        this.numero_controller = numero_controller;
        this.fecha_visita = fecha_visita;
        this.device_id = device_id;
        this.perimetro = perimetro;
        this.distancia = distancia;
        this.termometro = termometro;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getChannel_segment() {
        return channel_segment;
    }

    public void setChannel_segment(String channel_segment) {
        this.channel_segment = channel_segment;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCustomer_owner() {
        return customer_owner;
    }

    public void setCustomer_owner(String customer_owner) {
        this.customer_owner = customer_owner;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKam() {
        return kam;
    }

    public void setKam(String kam) {
        this.kam = kam;
    }

    public String getSales_executive() {
        return sales_executive;
    }

    public void setSales_executive(String sales_executive) {
        this.sales_executive = sales_executive;
    }

    public String getMerchandising() {
        return merchandising;
    }

    public void setMerchandising(String merchandising) {
        this.merchandising = merchandising;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getMercaderista() {
        return mercaderista;
    }

    public void setMercaderista(String mercaderista) {
        this.mercaderista = mercaderista;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDpsm() {
        return dpsm;
    }

    public void setDpsm(String dpsm) {
        this.dpsm = dpsm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNumero_controller() {
        return numero_controller;
    }

    public void setNumero_controller(String numero_controller) {
        this.numero_controller = numero_controller;
    }

    public String getFecha_visita() {
        return fecha_visita;
    }

    public void setFecha_visita(String fecha_visita) {
        this.fecha_visita = fecha_visita;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPerimetro() {
        return perimetro;
    }

    public void setPerimetro(String perimetro) {
        this.perimetro = perimetro;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getTermometro() {
        return termometro;
    }

    public void setTermometro(String termometro) {
        this.termometro = termometro;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }
}