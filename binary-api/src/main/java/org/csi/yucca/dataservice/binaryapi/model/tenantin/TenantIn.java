package org.csi.yucca.dataservice.binaryapi.model.tenantin;

import com.google.gson.annotations.Expose;

public class TenantIn {

    @Expose
    private Tenants tenants;

    /**
     * 
     * @return
     *     The tenants
     */
    public Tenants getTenants() {
        return tenants;
    }

    /**
     * 
     * @param tenants
     *     The tenants
     */
    public void setTenants(Tenants tenants) {
        this.tenants = tenants;
    }

}
