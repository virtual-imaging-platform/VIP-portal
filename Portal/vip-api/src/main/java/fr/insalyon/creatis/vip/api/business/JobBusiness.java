/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Job;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class JobBusiness {
    public void newJob(Job job) throws ApiException { throw new ApiException("Not implemented yet");}
    public void deleteJob(String jobIdentifier) throws ApiException { throw new ApiException("Not implemented yet");} // i.e. clean
    public void killJob(String jobIdentifier) throws ApiException { throw new ApiException("Not implemented yet");}
    public Job getJob(String jobIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    public List<Job> getJobs() throws ApiException { throw new ApiException("Not implemented yet"); }
    public List<Job> getJobsOfService(String serviceIdentifier) throws ApiException { throw new ApiException("Not implemented yet");}
    public List<Job> getJobsOfUser(String userIdentifier) throws ApiException { throw new ApiException("Not implemented yet");}
}
