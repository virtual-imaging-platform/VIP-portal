package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Rafael Silva
 */
public class Node implements IsSerializable {

    private String site;
    private String node_name;
    private int ncpus;
    private String cpu_model_name;
    private double cpu_mhz;
    private int cpu_cache_size;
    private double cpu_bogomips;
    private int mem_total;

    public Node() {
    }

    public Node(String site, String node_name, int ncpus, String cpu_model_name, double cpu_mhz, int cpu_cache_size, double cpu_bogomips, int mem_total) {
        this.site = site;
        this.node_name = node_name;
        this.ncpus = ncpus;
        this.cpu_model_name = cpu_model_name;
        this.cpu_mhz = cpu_mhz;
        this.cpu_cache_size = cpu_cache_size;
        this.cpu_bogomips = cpu_bogomips;
        this.mem_total = mem_total;
    }

    public double getCpu_bogomips() {
        return cpu_bogomips;
    }

    public int getCpu_cache_size() {
        return cpu_cache_size;
    }

    public double getCpu_mhz() {
        return cpu_mhz;
    }

    public String getCpu_model_name() {
        return cpu_model_name;
    }

    public int getMem_total() {
        return mem_total;
    }

    public int getNcpus() {
        return ncpus;
    }

    public String getNode_name() {
        return node_name;
    }

    public String getSite() {
        return site;
    }
}
