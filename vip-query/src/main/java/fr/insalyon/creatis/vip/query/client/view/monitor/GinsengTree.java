/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author nouha
 */
public class GinsengTree {

    TreeNode[] GinsengData;
    TreeNode[] GinsengBaseData;

    public TreeNode[] getGinsengBaseData() {
        return GinsengBaseData;
    }

    public void setGinsengBaseData(TreeNode[] GinsengBaseData) {
        this.GinsengBaseData = GinsengBaseData;
    }

    public TreeNode[] getGinsengData() {
        return GinsengData;
    }

    public void setGinsengData(TreeNode[] GinsengData) {
        this.GinsengData = GinsengData;
    }

    public GinsengTree() {
        GinsengBaseData = new TreeNode[]{
            new GinsengTreeNode("2", "1", "http://e-ginseng.org/graph/I3S", "nothing", false, "",false),
            new GinsengTreeNode("3", "1", "http://e-ginseng.org/graph/IN2P3", "nothing", false, "",false),};
        GinsengData = new TreeNode[]{
            new GinsengTreeNode("265", "1", "Person", "nothing", false, "",false),
            new GinsengTreeNode("264", "1", "Medical Bag", "nothing", false, "",false),
            new GinsengTreeNode("263", "1", "Medical Event", "nothing", false, "",false),
            new GinsengTreeNode("262", "1", "Clinical Variable", "nothing", false, "",false),
            
            new GinsengTreeNode("105", "265", "First Name", "string", false, "",false),
            new GinsengTreeNode("106", "265", "Last Name", "string", false, "",false),
            new GinsengTreeNode("4", "265", "Patient", "nothing", false, "",false),
            new GinsengTreeNode("5", "265", "Physician", "nothing", false, "",false),
            new GinsengTreeNode("100", "265", "Adresse", "nothing", false, "",false),
         
            
            new GinsengTreeNode("107", "263", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("108", "263", "date", "date", false, "",false),
            new GinsengTreeNode("109", "263", "hasPysician", "nothing", false, "",false),
            new GinsengTreeNode("6", "263", "Baby", "nothing", false, "",false),
            new GinsengTreeNode("7", "263", "FCV", "nothing", false, "",false),
            new GinsengTreeNode("8", "263", "RSPA", "nothing", false, "",false),
            new GinsengTreeNode("9", "263", "DICOM_Image_Study", "nothing", false, "",false),
            new GinsengTreeNode("73", "263", "hasClinicalVariable", "nothing", false, "",false),
            
            new GinsengTreeNode("110", "262", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("111", "262", "label", "string", false, "",false),
            new GinsengTreeNode("112", "262", "value", "int", false, "",false),
            new GinsengTreeNode("113", "262", "unit", "int", false, "",false),
            new GinsengTreeNode("10", "262", "PregnancyDuration", "nothing", false, "",false),
            new GinsengTreeNode("11", "262", "Weight", "nothing", false, "",false),
            new GinsengTreeNode("12", "262", "DernierFCV", "nothing", false, "",false),
            new GinsengTreeNode("13", "262", "DateDebutGrossesse", "nothing", false, "",false),
            
            new GinsengTreeNode("14", "4", "ID", "int", false, "",false),
            new GinsengTreeNode("15", "4", "hasStudy", "study", false, "",false),
            new GinsengTreeNode("16", "4", "First Name", "string", false, "",false),
            new GinsengTreeNode("17", "4", "Last Name", "string", false, "",false),
            new GinsengTreeNode("45", "4", "hasMedicalBag", "nothing", false, "",false),
            new GinsengTreeNode("18", "4", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("19", "5", "First Name", "string", false, "",false),
            new GinsengTreeNode("20", "5", "Last Name", "string", false, "",false),
            new GinsengTreeNode("21", "5", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("22", "18", "Number", "int", false, "",false),
            new GinsengTreeNode("23", "18", "Street", "string", false, "",false),
            new GinsengTreeNode("24", "18", "Zip", "string", false, "",false),
            new GinsengTreeNode("25", "18", "City", "string", false, "",false),
            
            new GinsengTreeNode("44", "21", "Number", "int", false, "",false),
            new GinsengTreeNode("26", "21", "Street", "string", false, "",false),
            new GinsengTreeNode("27", "21", "Zip", "string", false, "",false),
            new GinsengTreeNode("28", "21", "City", "city", false, "",false),
            
            new GinsengTreeNode("29", "6", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("30", "6", "date", "date", false, "",false),
            new GinsengTreeNode("31", "6", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("32", "7", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("33", "7", "date", "date", false, "",false),
            new GinsengTreeNode("34", "7", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("35", "8", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("36", "8", "date", "date", false, "",false),
            new GinsengTreeNode("37", "8", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("38", "9", "externalRefID", "externalRefID", false, "",false),
            new GinsengTreeNode("39", "9", "date", "date", false, "",false),
            new GinsengTreeNode("40", "9", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("41", "264", "description", "string", false, "",false),
            new GinsengTreeNode("42", "264", "date", "date", false, "",false),
            new GinsengTreeNode("43", "264", "hasMedicalEvent", "nothing", false, "",false),
            
            new GinsengTreeNode("46", "10", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("47", "10", "label", "string", false, "",false),
            new GinsengTreeNode("48", "10", "value", "string", false, "",false),
            new GinsengTreeNode("49", "10", "unit", "string", false, "",false),
            new GinsengTreeNode("50", "11", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("51", "11", "label", "string", false, "",false),
            new GinsengTreeNode("52", "11", "value", "string", false, "",false),
            new GinsengTreeNode("53", "11", "unit", "string", false, "",false),
            new GinsengTreeNode("54", "12", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("55", "12", "label", "string", false, "",false),
            new GinsengTreeNode("56", "12", "value", "string", false, "",false),
            new GinsengTreeNode("57", "12", "unit", "string", false, "",false),
            
            new GinsengTreeNode("58", "13", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("59", "13", "label", "string", false, "",false),
            new GinsengTreeNode("60", "13", "value", "string", false, "",false),
            new GinsengTreeNode("61", "13", "unit", "string", false, "",false),
            //Medical Bag
            new GinsengTreeNode("62", "45", "description", "string", false, "",false),
            new GinsengTreeNode("63", "45", "date", "date", false, "",false),
            new GinsengTreeNode("64", "45", "hasMedicalEvent", "nothing", false, "",false),
            //Medical Event
            new GinsengTreeNode("65", "43", "Baby", "nothing", false, "",false),
            new GinsengTreeNode("66", "43", "FCV", "nothing", false, "",false),
            new GinsengTreeNode("67", "43", "RSPA", "nothing", false, "",false),
            new GinsengTreeNode("68", "43", "DICOM_Image_Study", "nothing", false, "",false),
            new GinsengTreeNode("86", "43", "hasClinicalVariable", "nothing", false, "",false),
            ///
             new GinsengTreeNode("121", "65", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("122", "65", "date", "date", false, "",false),
            new GinsengTreeNode("123", "65", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("124", "66", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("125", "66", "date", "date", false, "",false),
            new GinsengTreeNode("126", "66", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("127", "67", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("128", "67", "date", "date", false, "",false),
            new GinsengTreeNode("129", "67", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("130", "68", "externalRefID", "externalRefID", false, "",false),
            new GinsengTreeNode("131", "68", "date", "date", false, "",false),
            new GinsengTreeNode("132", "68", "hasPysician", "nothing", false, "",false),
            //medical event
            new GinsengTreeNode("69", "64", "Baby", "nothing", false, "",false),
            new GinsengTreeNode("70", "64", "FCV", "nothing", false, "",false),
            new GinsengTreeNode("71", "64", "RSPA", "nothing", false, "",false),
            new GinsengTreeNode("72", "64", "DICOM_Image_Study", "nothing", false, "",false),
            new GinsengTreeNode("87", "64", "hasClinicalVariable", "nothing", false, "",false),
            //
             new GinsengTreeNode("133", "69", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("134", "69", "date", "date", false, "",false),
            new GinsengTreeNode("135", "69", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("136", "70", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("137", "70", "date", "date", false, "",false),
            new GinsengTreeNode("138", "70", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("139", "71", "externalRefID", "int", false, "",false),
            new GinsengTreeNode("140", "71", "date", "date", false, "",false),
            new GinsengTreeNode("141", "71", "hasPysician", "nothing", false, "",false),
            
            new GinsengTreeNode("142", "72", "externalRefID", "externalRefID", false, "",false),
            new GinsengTreeNode("143", "72", "date", "date", false, "",false),
            new GinsengTreeNode("144", "72", "hasPysician", "nothing", false, "",false),
            
            
            //73 has benn used
            //hasPysician
            

            new GinsengTreeNode("74", "31", "First Name", "string", false, "",false),
            new GinsengTreeNode("75", "31", "Last Name", "string", false, "",false),
            new GinsengTreeNode("76", "31", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("77", "34", "First Name", "string", false, "",false),
            new GinsengTreeNode("78", "34", "Last Name", "string", false, "",false),
            new GinsengTreeNode("79", "34", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("80", "37", "First Name", "string", false, "",false),
            new GinsengTreeNode("81", "37", "Last Name", "string", false, "",false),
            new GinsengTreeNode("82", "37", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("83", "40", "First Name", "string", false, "",false),
            new GinsengTreeNode("84", "40", "Last Name", "string", false, "",false),
            new GinsengTreeNode("85", "40", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("205", "109", "First Name", "string", false, "",false),
            new GinsengTreeNode("206", "109", "Last Name", "string", false, "",false),
            new GinsengTreeNode("207", "109", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("208", "123", "First Name", "string", false, "",false),
            new GinsengTreeNode("209", "123", "Last Name", "string", false, "",false),
            new GinsengTreeNode("210", "123", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("211", "126", "First Name", "string", false, "",false),
            new GinsengTreeNode("212", "126", "Last Name", "string", false, "",false),
            new GinsengTreeNode("213", "126", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("214", "132", "First Name", "string", false, "",false),
            new GinsengTreeNode("215", "132", "Last Name", "string", false, "",false),
            new GinsengTreeNode("216", "132", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("217", "138", "First Name", "string", false, "",false),
            new GinsengTreeNode("218", "138", "Last Name", "string", false, "",false),
            new GinsengTreeNode("219", "138", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("220", "135", "First Name", "string", false, "",false),
            new GinsengTreeNode("221", "135", "Last Name", "string", false, "",false),
            new GinsengTreeNode("222", "135", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("223", "141", "First Name", "string", false, "",false),
            new GinsengTreeNode("224", "141", "Last Name", "string", false, "",false),
            new GinsengTreeNode("225", "141", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("226", "144", "First Name", "string", false, "",false),
            new GinsengTreeNode("227", "144", "Last Name", "string", false, "",false),
            new GinsengTreeNode("228", "144", "Adresse", "nothing", false, "",false),
            
            
        
            //clinical Variable
            new GinsengTreeNode("165", "86", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("166", "86", "label", "string", false, "",false),
            new GinsengTreeNode("167", "86", "value", "string", false, "",false),
            new GinsengTreeNode("168", "86", "unit", "string", false, "",false),
            new GinsengTreeNode("88", "86", "PregnancyDuration", "nothing", false, "",false),
            new GinsengTreeNode("89", "86", "Weight", "nothing", false, "",false),
            new GinsengTreeNode("90", "86", "DernierFCV", "nothing", false, "",false),
            new GinsengTreeNode("91", "86", "DateDebutGrossesse", "nothing", false, "",false),
            
             new GinsengTreeNode("169", "88", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("170", "88", "label", "string", false, "",false),
            new GinsengTreeNode("171", "88", "value", "string", false, "",false),
            new GinsengTreeNode("172", "88", "unit", "string", false, "",false),
            
            new GinsengTreeNode("173", "89", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("174", "89", "label", "string", false, "",false),
            new GinsengTreeNode("175", "89", "value", "string", false, "",false),
            new GinsengTreeNode("176", "89", "unit", "string", false, "",false),
            
            new GinsengTreeNode("177", "90", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("178", "90", "label", "string", false, "",false),
            new GinsengTreeNode("179", "90", "value", "string", false, "",false),
            new GinsengTreeNode("180", "90", "unit", "string", false, "",false),
            
            new GinsengTreeNode("181", "91", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("182", "91", "label", "string", false, "",false),
            new GinsengTreeNode("183", "91", "value", "string", false, "",false),
            new GinsengTreeNode("184", "91", "unit", "string", false, "",false),
            
            
            new GinsengTreeNode("185", "87", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("186", "87", "label", "string", false, "",false),
            new GinsengTreeNode("187", "87", "value", "string", false, "",false),
            new GinsengTreeNode("188", "87", "unit", "string", false, "",false),
            new GinsengTreeNode("92", "87", "PregnancyDuration", "nothing", false, "",false),
            new GinsengTreeNode("93", "87", "Weight", "nothing", false, "",false),
            new GinsengTreeNode("94", "87", "DernierFCV", "nothing", false, "",false),
            new GinsengTreeNode("95", "87", "DateDebutGrossesse", "nothing", false, "",false),
            
            
             new GinsengTreeNode("189", "92", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("190", "92", "label", "string", false, "",false),
            new GinsengTreeNode("191", "92", "value", "string", false, "",false),
            new GinsengTreeNode("192", "92", "unit", "string", false, "",false),
            
             new GinsengTreeNode("193", "93", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("194", "93", "label", "string", false, "",false),
            new GinsengTreeNode("195", "93", "value", "string", false, "",false),
            new GinsengTreeNode("196", "93", "unit", "string", false, "",false),
            
             new GinsengTreeNode("197", "94", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("198", "94", "label", "string", false, "",false),
            new GinsengTreeNode("199", "94", "value", "string", false, "",false),
            new GinsengTreeNode("200", "94", "unit", "string", false, "",false),
            
             new GinsengTreeNode("201", "95", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("202", "95", "label", "string", false, "",false),
            new GinsengTreeNode("203", "95", "value", "string", false, "",false),
            new GinsengTreeNode("204", "95", "unit", "string", false, "",false),
            
            
            
            
            
            
            
            new GinsengTreeNode("145", "73", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("146", "73", "label", "string", false, "",false),
            new GinsengTreeNode("147", "73", "value", "string", false, "",false),
            new GinsengTreeNode("148", "73", "unit", "string", false, "",false),
            new GinsengTreeNode("96", "73", "PregnancyDuration", "nothing", false, "",false),
            new GinsengTreeNode("97", "73", "Weight", "nothing", false, "",false),
            new GinsengTreeNode("98", "73", "DernierFCV", "nothing", false, "",false),
            new GinsengTreeNode("99", "73", "DateDebutGrossesse", "nothing", false, "",false),
            
             new GinsengTreeNode("149", "96", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("150", "96", "label", "string", false, "",false),
            new GinsengTreeNode("151", "96", "value", "string", false, "",false),
            new GinsengTreeNode("152", "96", "unit", "string", false, "",false),
            
             new GinsengTreeNode("153", "97", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("154", "97", "label", "string", false, "",false),
            new GinsengTreeNode("155", "97", "value", "string", false, "",false),
            new GinsengTreeNode("156", "97", "unit", "string", false, "",false),
            
             new GinsengTreeNode("157", "98", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("158", "98", "label", "string", false, "",false),
            new GinsengTreeNode("159", "98", "value", "string", false, "",false),
            new GinsengTreeNode("160", "98", "unit", "string", false, "",false),
            
             new GinsengTreeNode("161", "99", "acquisationDate", "date", false, "",false),
            new GinsengTreeNode("162", "99", "label", "string", false, "",false),
            new GinsengTreeNode("163", "99", "value", "string", false, "",false),
            new GinsengTreeNode("164", "99", "unit", "string", false, "",false),
            
            //ADRESSE
            new GinsengTreeNode("101", "100", "Number", "int", false, "",false),
            new GinsengTreeNode("102", "100", "Street", "nothing", false, "",false),
            new GinsengTreeNode("103", "100", "Zip", "string", false, "",false),
            new GinsengTreeNode("104", "100", "City", "city", false, "",false),
            
            new GinsengTreeNode("114", "109", "First Name", "string", false, "",false),
            new GinsengTreeNode("115", "109", "Last Name", "string", false, "",false),
            new GinsengTreeNode("116", "109", "Adresse", "nothing", false, "",false),
            
            new GinsengTreeNode("117", "116", "Number", "int", false, "",false),
            new GinsengTreeNode("118", "116", "Street", "string", false, "",false),
            new GinsengTreeNode("119", "116", "Zip", "string", false, "",false),
            new GinsengTreeNode("120", "116", "City", "city", false, "",false),
        
             new GinsengTreeNode("117", "76", "Number", "int", false, "",false),
            new GinsengTreeNode("118", "76", "Street", "string", false, "",false),
            new GinsengTreeNode("119", "76", "Zip", "string", false, "",false),
            new GinsengTreeNode("120", "76", "City", "city", false, "",false),
        
        
            new GinsengTreeNode("229", "79", "Number", "int", false, "",false),
            new GinsengTreeNode("230", "79", "Street", "string", false, "",false),
            new GinsengTreeNode("231", "79", "Zip", "string", false, "",false),
            new GinsengTreeNode("232", "79", "City", "city", false, "",false),
            
             new GinsengTreeNode("233", "82", "Number", "int", false, "",false),
            new GinsengTreeNode("234", "82", "Street", "string", false, "",false),
            new GinsengTreeNode("235", "82", "Zip", "string", false, "",false),
            new GinsengTreeNode("236", "82", "City", "city", false, "",false),
            
             new GinsengTreeNode("237", "85", "Number", "int", false, "",false),
            new GinsengTreeNode("238", "85", "Street", "string", false, "",false),
            new GinsengTreeNode("239", "85", "Zip", "string", false, "",false),
            new GinsengTreeNode("240", "85", "City", "city", false, "",false),
            
             new GinsengTreeNode("241", "207", "Number", "int", false, "",false),
            new GinsengTreeNode("242", "207", "Street", "string", false, "",false),
            new GinsengTreeNode("243", "207", "Zip", "string", false, "",false),
            new GinsengTreeNode("244", "207", "City", "city", false, "",false),
            
             new GinsengTreeNode("245", "210", "Number", "int", false, "",false),
            new GinsengTreeNode("246", "210", "Street", "string", false, "",false),
            new GinsengTreeNode("247", "210", "Zip", "string", false, "",false),
            new GinsengTreeNode("248", "210", "City", "city", false, "",false),
            
             new GinsengTreeNode("249", "213", "Number", "int", false, "",false),
            new GinsengTreeNode("250", "213", "Street", "string", false, "",false),
            new GinsengTreeNode("251", "213", "Zip", "string", false, "",false),
            new GinsengTreeNode("252", "213", "City", "city", false, "",false),
            
             new GinsengTreeNode("253", "216", "Number", "int", false, "",false),
            new GinsengTreeNode("254", "216", "Street", "string", false, "",false),
            new GinsengTreeNode("255", "216", "Zip", "string", false, "",false),
            new GinsengTreeNode("256", "216", "City", "city", false, "",false),
            
            new GinsengTreeNode("258", "219", "Street", "string", false, "",false),
            new GinsengTreeNode("258", "219", "Zip", "string", false, "",false),
            new GinsengTreeNode("259", "219", "City", "city", false, "",false),
            
             new GinsengTreeNode("260", "222", "Number", "int", false, "",false),
            new GinsengTreeNode("261", "222", "Street", "string", false, "",false),
            new GinsengTreeNode("266", "222", "Zip", "string", false, "",false),
            new GinsengTreeNode("267", "222", "City", "city", false, "",false),
            
             new GinsengTreeNode("268", "225", "Number", "int", false, "",false),
            new GinsengTreeNode("269", "225", "Street", "string", false, "",false),
            new GinsengTreeNode("270", "225", "Zip", "string", false, "",false),
            new GinsengTreeNode("271", "225", "City", "city", false, "",false),
            
             new GinsengTreeNode("272", "228", "Number", "int", false, "",false),
            new GinsengTreeNode("273", "228", "Street", "string", false, "",false),
            new GinsengTreeNode("274", "228", "Zip", "string", false, "",false),
            new GinsengTreeNode("275", "228", "City", "city", false, "",false),
            
             
        
        
        
        
        
        
        
        
        
        
        };





    }
}
