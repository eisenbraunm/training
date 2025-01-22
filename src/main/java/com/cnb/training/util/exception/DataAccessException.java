package com.cnb.training.util.exception;


import com.cnb.training.DAO.ChemistryDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DataAccessException extends RuntimeException{
    private Logger log = LogManager.getLogger(ChemistryDAO.class);
    public DataAccessException(String message) {
        super(  message.replaceAll("\\n", "<br/>"));
    }

    public DataAccessException(String message, Exception thrown) {
        super(message.replaceAll("\\n", "<br/>"),thrown);
        log.error(message +" from DA exception", thrown);
    }

    public DataAccessException(Exception thrown){
        super(thrown);
        log.error(thrown);
    }
}
