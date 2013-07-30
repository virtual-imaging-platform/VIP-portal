/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.business;


import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


/**
 *
 * @author Boujelben
 */
public class QueryData implements QueryDAO {
     private final static Logger logger = Logger.getLogger(QueryData.class);
     private Connection connection;

    public QueryData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    
    
    
    
    @Override
    public List<String[]> getQueries() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT queryID, queryName FROM Query");
            ResultSet rs = ps.executeQuery();
            List<String[]> queries  = new ArrayList<String[]>();
            
            while (rs.next()) {
                
                int id=rs.getInt("queryID");
           
                 PreparedStatement ps2 = connection.prepareStatement("SELECT queryversionID, queryVersion, dateCreation FROM QueryVersion WHERE queryID=?");
                 
                
                 ps2.setInt(1,id);
                 ResultSet rs2 = ps2.executeQuery();
                
                while (rs2.next()) {
               Timestamp date=rs2.getTimestamp("dateCreation");
               
               
                    queries.add(new String[]{rs.getString("queryName"),date.toString(),rs2.getString("queryVersion"),rs2.getString("queryversionID")});
                }
                ps2.close();

            }
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
        
           
    
    
    @Override
    public String getDescription(Long queryVersionID) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT queryID FROM QueryVersion where queryVersionID=?");
            ps.setLong(1, queryVersionID);
            ResultSet rs = ps.executeQuery();
            
            String result=null;
            
            while (rs.next()) {
                
                long id=rs.getLong("queryID");
           
                 PreparedStatement ps2 = connection.prepareStatement("SELECT description FROM Query WHERE queryID=?");
                 
                
                 ps2.setLong(1,id);
                 ResultSet rs2 = ps2.executeQuery();
                
                while (rs2.next()) {
               result=rs2.getString("description");
                }
                ps2.close();

            }
            ps.close();
            return result;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
        
           
    @Override
    public List<String[]> getQuerie(Long queryversionid) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT queryName, description, body FROM Query q, QueryVersion v WHERE q.queryID=v.queryID AND v.queryVersionID=? ");
            ps.setLong(1, queryversionid);
            ResultSet rs = ps.executeQuery();
      
             List<String[]> queries  = new ArrayList<String[]>();
            
            while (rs.next()) {
                    queries.add(new String[]{rs.getString("queryName"),rs.getString("description"),rs.getString("body")});
                }
              
            
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
      
  
        
@Override
    public List<String[]> getVersion() throws DAOException {
       try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "queryName,dateCreation,queryVersion FROM"
                    + " Query query,QueryVersion queryversion "
                    +"query.queryID=queryVersion.queryID"
                    +"ORDER BY query.queryName");

            ResultSet rs = ps.executeQuery();
            List<String[]> queries = new ArrayList<String[]>();

            while (rs.next()) {
                
                queries.add(new String[]{rs.getString("queryName"), rs.getString("dateCreation"),rs.getString("queryVersion")});
            }
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    @Override
    public List<String[]> getParameterValue(Long queryExecutionID) throws DAOException {
       try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "value,parameterID From Value WHERE queryExecutionID=?");
            ps.setLong(1, queryExecutionID);

            ResultSet rs = ps.executeQuery();
            List<String[]> queries = new ArrayList<String[]>();

            while (rs.next()) {
                Long parameterID=rs.getLong("parameterID");
                 PreparedStatement ps2 = connection.prepareStatement("SELECT "
                    + "name From Parameter WHERE parameterID=?");
                 ps2.setLong(1, parameterID);
                   ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                    queries.add(new String[]{rs2.getString("name"), rs.getString("value")});
                    }
                ps2.close();
                
            }
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
   
@Override
public void  removeVersion(Long versionid) throws DAOException {
           
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM QueryVersion WHERE queryVersionID=?");

           
            ps.setLong(1,versionid);
           
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
             

@Override
public void  removeQueryExecution(Long executionID) throws DAOException {
           
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM QueryExecution WHERE queryExecutionID=?");

           
            ps.setLong(1,executionID);
           
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
                      
       @Override
     public Integer  count(Long queryID) throws DAOException {
           
        try {
            PreparedStatement ps = connection.prepareStatement("select count(*) from QueryVersion WHERE queryID=?");
                    

           Integer numberOfRows = new Integer(0);
            ps.setLong(1,queryID);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
           numberOfRows = new Integer(rs.getInt(1));
           }
            ps.close();
        return numberOfRows;
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
       
    }
         
       @Override
     public Long  getQueryID(Long queryVersionID) throws DAOException {
           
        try {
            PreparedStatement ps = connection.prepareStatement("select queryID from QueryVersion WHERE queryVersionID=?");
                    

           
            ps.setLong(1,queryVersionID);
           ResultSet rs = ps.executeQuery();
           Long queryID=0L;
           if (rs.next()) {
           queryID = rs.getLong(1);
           }
            ps.close();
        return queryID;
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
       
    }
           
                      

          
    @Override
        public List<Long> addParameter(Parameter param) throws DAOException {
             
         try {
           PreparedStatement pss=connection.prepareStatement("SELECT body from QueryVersion WHERE queryVersionID=? ");
           pss.setLong(1, param.getQueryVersionID());
          
          ResultSet rss=pss.executeQuery();
          String body=null;
           while (rss.next()){
           body=rss.getString("body");
           }
           //String str[]=body.split("\\[");
             List<String> listparam=new ArrayList<String>(); 
            char b='a';
            
            char last='a';
        
           for (int i=0;i<body.length();i++){
               b=body.charAt(i);
               if(b=='['){
                   int k=0;
               for (int j=i;j<body.length();j++){
                       last=body.charAt(j);
                       
               if(last==']'&& k==0){
               listparam.add(body.substring(i+1,j));
               k=1;
               
               }
                   }
           }
           }
            List <Long> parametersid=new ArrayList<Long>(); 
          for (String s:listparam)
          {
              String str[]=s.split("\\;");
              try
              {
               PreparedStatement ps ;
                ps  = connection.prepareStatement(
                    "INSERT INTO Parameter(name, type, description, example, queryVersionID) "
                    + "VALUES (?, ?, ?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
           ps.setString(1,str[0]);
           ps.setString(2,str[1]);
           ps.setString(3,str[2]);
           ps.setString(4,str[3]);
           ps.setLong(5,param.getQueryVersionID());
           ps.execute();
           ResultSet rs = ps.getGeneratedKeys();
            Long idAuto_increment= new Long(0);
            while (rs.next()){
            idAuto_increment = rs.getLong(1);
            }
             
            ps.close();
           
              parametersid.add(idAuto_increment);
              } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
              
          }
          
          
           
         
            return parametersid; 
       
    } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

      
//methode ajoute un query et retourne un long l'id de la requete
@Override
    public Long add(Query query) throws DAOException {
        PreparedStatement ps = null;
         try {
            ps = connection.prepareStatement(
                    "INSERT INTO Query(description, queryName, queryMaker) "
                    + "VALUES (?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, query.getDescription());
            ps.setString(2, query.getName());
            ps.setString(3, query.getQueryMaker());
          
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            Long idAuto_increment= new Long(0);
            while (rs.next()){
            idAuto_increment = rs.getLong(1);
            }
             
            ps.close();
            return idAuto_increment;
            

        } catch (SQLException ex) {
            //if (ex.getMessage().contains("Duplicate entry")) {
               // logger.error("An application named \"" + application.getName() + "\" already exists.");
                //throw new DAOException("An application named \"" + application.getName() + "\" already exists.");
           // } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
         
        }
    


@Override
 public Long addVersion(QueryVersion version) throws DAOException {
         try {
           
            PreparedStatement ps2 = connection.prepareStatement(
                  "INSERT INTO QueryVersion(queryVersion, queryID, body, dateCreation) VALUES (?, ?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
            ps2.setString(1,version.getQueryVersion());
            ps2.setObject(2, version.getQueryID());
            ps2.setString(3,version.getBody());
            ps2.setTimestamp(4,getCurrentTimeStamp());
            ps2.execute();
            ResultSet rs = ps2.getGeneratedKeys();
            Long idAuto_increment= new Long(0);
            while (rs.next()){
            idAuto_increment = rs.getLong(1);
            }
            
            ps2.close();
           return idAuto_increment;
        } catch (SQLException ex) {
            //if (ex.getMessage().contains("Duplicate entry")) {
               // logger.error("An application named \"" + application.getName() + "\" already exists.");
                //throw new DAOException("An application named \"" + application.getName() + "\" already exists.");
           // } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
         
        }


 @Override
    public List<Parameter> getParameter(Long queryVersionID) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "parameterID, name, type, description, example "
                    + "FROM Parameter "
                    + "WHERE queryVersionID = ? ORDER BY parameterID DESC");

            ps.setLong(1, queryVersionID);
            ResultSet rs = ps.executeQuery();
            List<Parameter> parameters = new ArrayList<Parameter>();

            while (rs.next()) {
               parameters .add(new Parameter(rs.getString("name"),rs.getString("type"),rs.getString("description"),rs.getString("example"),rs.getLong("parameterID")));
            }

            ps.close();
            return parameters;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        
    }


    
      @Override
    public void updateQueryExecution(String urlResult, String status, Long executionID ) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "QueryExecution "
                    + "SET urlResult=?, status=? "
                   
                    + "WHERE queryExecutionID=?");
            
               

            ps.setString(1, urlResult);
            ps.setString(2, status);
            ps.setLong(3, executionID);
            ps.executeUpdate();
            ps.close();

         

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
      
      
      
        @Override
    public void updateQueryVersion(Long queryVersionID,String name, String description) throws DAOException {
  
            try {
            PreparedStatement ps1 = connection.prepareStatement(
            "select queryID from QueryVersion where queryVersionID=?");
                ps1.setLong(1, queryVersionID);
                ResultSet rs = ps1.executeQuery();
                Long queryID=0L;
                while(rs.next()){
                queryID=rs.getLong("queryID");
                }
                
            ps1.close();
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "Query "
                    + "SET description=?, queryName=? "
                   
                    + "WHERE queryID=?");
            
               

            ps.setString(1, description);
            ps.setString(2, name);
            ps.setLong(3, queryID);
            ps.executeUpdate();
            ps.close();

         

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

 @Override
 public Long addValue(Value value) throws DAOException {
         try {
           
            PreparedStatement ps = connection.prepareStatement(
                  "INSERT INTO Value(value, parameterID, queryExecutionID) VALUES (?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,value.getValue());
            ps.setLong(2, value.getParameterID());
            ps.setLong(3,value.getQueryExecutionID());
          
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            Long idAuto_increment= new Long(0);
            while (rs.next()){
            idAuto_increment = rs.getLong(1);
            }
            
            ps.close();
           return idAuto_increment;
        } catch (SQLException ex) {
           
                logger.error(ex);
                throw new DAOException(ex);
            }
         
        }
    
 
 
  @Override
 public Long addQueryExecution(QueryExecution queryExecution) throws DAOException {
      try {
           
            PreparedStatement ps = connection.prepareStatement(
                  "INSERT INTO QueryExecution(queryVersionID, dateExecution, executer,status, name, urlResult) VALUES (?, ?, ?, ?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1,queryExecution.getQueryVersionID());
            ps.setTimestamp(2,getCurrentTimeStamp());
            ps.setString(3,queryExecution.getExecuter());
            ps.setString(4,queryExecution.getStatus());
            ps.setString(5,queryExecution.getName());
           
         
            ps.setString(6,queryExecution.getUrlResult());
            
          
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            Long idAuto_increment= new Long(0);
            while (rs.next()){
            idAuto_increment = rs.getLong(1);
            }
            
            ps.close();
           return idAuto_increment;
        } catch (SQLException ex) {
           
                logger.error(ex);
                throw new DAOException(ex);
            }
  }
     
 @Override
    public List<String[]> getQueryHistory() throws DAOException {
       try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "queryExecutionID,name,queryName,queryVersion,executer,dateExecution,status,urlResult FROM "
                    + "Query query,QueryVersion queryversion,QueryExecution queryexe WHERE "
                    + "query.queryID=queryversion.queryID AND queryversion.queryVersionID=queryexe.queryVersionID "
                    + "ORDER BY queryexe.name");

            ResultSet rs = ps.executeQuery();
            List<String[]> queries = new ArrayList<String[]>();

            while (rs.next()) {
           
               Timestamp date=rs.getTimestamp("dateExecution");
               Long id=rs.getLong("queryExecutionID");
                queries.add(new String[]{id.toString(),rs.getString("name"),rs.getString("queryName"),rs.getString("queryVersion"),rs.getString("executer"),date.toString(), rs.getString("status"),rs.getString("urlResult")});
            }
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
  
  @Override
    public String getBody(Long queryVersionID, Long queryExecutionID,boolean parameter) throws DAOException {
      try {
      if (parameter==false)
      {
           PreparedStatement ps = connection.prepareStatement("SELECT "
                   + "body FROM QueryVersion where queryVersionID=?");
             ps.setLong(1, queryVersionID);
             ResultSet rs = ps.executeQuery();
              String body=null;
              while (rs.next()) {
           body=rs.getString("body");
              }
               ps.close();
              return body;
                   
      }
      else {
        
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "body, parameterID "
                    + "FROM QueryVersion v, Parameter p "
                    + "WHERE v.queryVersionID = p.queryVersionID AND v.queryVersionID=? ORDER BY p.parameterID DESC");

            ps.setLong(1, queryVersionID);
            ResultSet rs = ps.executeQuery();
            //List<String> value=new ArrayList<String>(); 
            int c=0;
            int nn=0;
            String body=null;
            String s=null;
            
            while (rs.next()) {
                
             Long n=rs.getLong("parameterID");
             if(nn==0){
             body=rs.getString("body");
             nn=1;
             }
              PreparedStatement ps2 = connection.prepareStatement("SELECT "
                    + "value "
                    + "FROM Value "
                    + "WHERE parameterID=? AND queryExecutionID=?");
              ps2.setLong(1, n);
              ps2.setLong(2,queryExecutionID);
              ResultSet rs2 = ps2.executeQuery();
       
              
              while (rs2.next()) { 
              for (int i=0; i<body.length();i++){
              char b=body.charAt(i);
               if(b=='['){
                    for (int j=i+1;j<body.length();j++){
                      char last=body.charAt(j);
                      int kk=0;
                      //substring j+1 non inclus
                     
                      if(last==']' && kk==0){
                         kk=1; 
                      c=j+1;
                      s=body.substring(i+1,j);
                     
                      }
               }
                    
               }
                   
               }
               body=body.replaceAll("\\["+s+"\\]",rs2.getString("value") );
              }
             
              ps2.close();
              
              
             
            }
            

            ps.close();
            return body;
      }
            

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        
    }

  
  
  
 
 
 
 
 
 
 
  
      private static java.sql.Timestamp getCurrentTimeStamp() {
 
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
 
	}
      
      
      
      
      
      
    }
        
    
    

    
    
    
