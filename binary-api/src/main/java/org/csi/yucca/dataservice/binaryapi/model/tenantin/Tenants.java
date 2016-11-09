package org.csi.yucca.dataservice.binaryapi.model.tenantin;

import com.google.gson.annotations.Expose;

public class Tenants {

    @Expose
    private Tenant tenant;

    /**
     * 
     * @return
     *     The tenant
     */
    public Tenant getTenant() {
        return tenant;
    }

    /**
     * 
     * @param tenant
     *     The tenant
     */
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

}
