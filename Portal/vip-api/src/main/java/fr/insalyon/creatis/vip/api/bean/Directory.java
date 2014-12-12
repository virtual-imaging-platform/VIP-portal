/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean;

import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class Directory extends File {
    
    List<File> files;
    String sshUrl; // ??? an ssh url where we can access the directory, e.g. vip.creatis.insa-lyon.fr:/home/user/data
    
    public Directory(){}
    
}
