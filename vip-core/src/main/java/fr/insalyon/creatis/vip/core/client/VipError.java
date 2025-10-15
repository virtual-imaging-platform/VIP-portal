package fr.insalyon.creatis.vip.core.client;

public interface VipError {

    /*
     * to override to return internal error code
     * reserved codes for vip-modules:
     * - 1xxx : vip-core
     * - 2xxx : vip-application
     * - 3xxx : vip-application-importer
     * - 4xxx : vip-data-management
     * - 5xxx : vip-gatelab
     * - 6xxx : vip-social
     * - 7xxx : vip-visualization
     * - 8xxx : vip-api
     */

    Integer getCode();
    Integer getExpectedParameters();
    String getMessage();
}