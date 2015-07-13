/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

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
