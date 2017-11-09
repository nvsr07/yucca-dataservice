package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.DettaglioStream;

public class TwitterInfoResponse extends Response {
	
    private String twtquery;
    private Double twtgeoloclat; 
    private Double twtgeoloclon; 
    private Double twtgeolocradius; 
    private String twtgeolocunit; 
    private String twtlang; 
    private String twtlocale; 
    private Integer twtcount; 
    private String twtresulttype; 
    private String twtuntil; 
    private Integer twtratepercentage; 
    private Long twtlastsearchid;
    
	public TwitterInfoResponse( DettaglioStream dettaglioStream ) {
		super();
		this.twtquery = dettaglioStream.getTwtquery();
		this.twtgeoloclat = dettaglioStream.getTwtgeoloclat();
		this.twtgeoloclon = dettaglioStream.getTwtgeoloclon();
		this.twtgeolocradius = dettaglioStream.getTwtgeolocradius();
		this.twtgeolocunit = dettaglioStream.getTwtgeolocunit();
		this.twtlang = dettaglioStream.getTwtlang();
		this.twtlocale = dettaglioStream.getTwtlocale();
		this.twtcount = dettaglioStream.getTwtcount();
		this.twtresulttype = dettaglioStream.getTwtresulttype();
		this.twtuntil = dettaglioStream.getTwtuntil();
		this.twtratepercentage = dettaglioStream.getTwtratepercentage();
		this.twtlastsearchid = dettaglioStream.getTwtlastsearchid();
	}
	
	public TwitterInfoResponse() {
		super();
	}
	public String getTwtquery() {
		return twtquery;
	}
	public void setTwtquery(String twtquery) {
		this.twtquery = twtquery;
	}
	public Double getTwtgeoloclat() {
		return twtgeoloclat;
	}
	public void setTwtgeoloclat(Double twtgeoloclat) {
		this.twtgeoloclat = twtgeoloclat;
	}
	public Double getTwtgeoloclon() {
		return twtgeoloclon;
	}
	public void setTwtgeoloclon(Double twtgeoloclon) {
		this.twtgeoloclon = twtgeoloclon;
	}
	public Double getTwtgeolocradius() {
		return twtgeolocradius;
	}
	public void setTwtgeolocradius(Double twtgeolocradius) {
		this.twtgeolocradius = twtgeolocradius;
	}
	public String getTwtgeolocunit() {
		return twtgeolocunit;
	}
	public void setTwtgeolocunit(String twtgeolocunit) {
		this.twtgeolocunit = twtgeolocunit;
	}
	public String getTwtlang() {
		return twtlang;
	}
	public void setTwtlang(String twtlang) {
		this.twtlang = twtlang;
	}
	public String getTwtlocale() {
		return twtlocale;
	}
	public void setTwtlocale(String twtlocale) {
		this.twtlocale = twtlocale;
	}
	public Integer getTwtcount() {
		return twtcount;
	}
	public void setTwtcount(Integer twtcount) {
		this.twtcount = twtcount;
	}
	public String getTwtresulttype() {
		return twtresulttype;
	}
	public void setTwtresulttype(String twtresulttype) {
		this.twtresulttype = twtresulttype;
	}
	public String getTwtuntil() {
		return twtuntil;
	}
	public void setTwtuntil(String twtuntil) {
		this.twtuntil = twtuntil;
	}
	public Integer getTwtratepercentage() {
		return twtratepercentage;
	}
	public void setTwtratepercentage(Integer twtratepercentage) {
		this.twtratepercentage = twtratepercentage;
	}
	public Long getTwtlastsearchid() {
		return twtlastsearchid;
	}
	public void setTwtlastsearchid(Long twtlastsearchid) {
		this.twtlastsearchid = twtlastsearchid;
	} 
	
	
}
