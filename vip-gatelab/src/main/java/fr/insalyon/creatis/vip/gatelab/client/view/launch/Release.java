package fr.insalyon.creatis.vip.gatelab.client.view.launch;

/**
 *
 * @author glatard
 */
public class Release implements Comparable<Release>{
   
    private class ReleaseNumber{
        public int major;
        public int minor;
        public int bug;
        public ReleaseNumber(){
            major = 0;
            minor = 0;
            bug =0; 
        }
    }
    
    private String releaseName;
    
    public Release(String name){
        releaseName = name;
    }

    public String getReleaseName() {
        return releaseName;
    }
    
    @Override
    public int compareTo(Release o) {
        ReleaseNumber _this = getReleaseNumber();
        ReleaseNumber _it = o.getReleaseNumber();
        
        if( _this.major > _it.major)
            return -1;
        if(_it.major > _this.major)
            return 1;
        //both major releases are equal
        if(_this.minor > _it.minor)
            return -1;
        if(_it.minor > _this.minor)
            return 1;
        //major and minor numbers are equal
        if(_this.bug > _it.bug)
            return -1;
        if(_it.bug > _this.bug)
            return 1;
        return 0;                  
    }
    
    public ReleaseNumber getReleaseNumber(){
      
     String[] s = releaseName.split("[0-9].*(.[0-9])+");

     String rName = releaseName;
     
    ReleaseNumber rn = new ReleaseNumber();
    try{
   
        for(String remove : s)
            rName = rName.replace(remove, "");
        
        String[] result = rName.split("\\.");
        
        switch(result.length){
            case 3:
                rn.bug = Integer.parseInt(result[2]);
            case 2:
                rn.minor = Integer.parseInt(result[1]);
            case 1:
                rn.major = Integer.parseInt(result[0]);
            default:
        }
    }catch(NumberFormatException e){}
   
        return rn;
    }
    
}
