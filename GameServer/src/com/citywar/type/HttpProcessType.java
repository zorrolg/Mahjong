package com.citywar.type;


public enum HttpProcessType
{
    
	SUCCESS,	
	FAIL,	
	PARA_ERROR,	
	VERIFY_ERROR,
	BUY_WAIT,	
	BUY_FAIL,	
    BUY_PARA_ERROR,    
    BUY_NO_ORDER_ERROR,    
    BUY_ORDER_ERROR,    
    BUY_SAVE_ERROR;

    
    
    private String channelName;
    private String errorInfo;
    private String returnInfo;

    private HttpProcessType()
    {
        this.channelName = "";
        errorInfo = "";
        returnInfo = "";
    }


    public String getValue()
    {
        return channelName;
    }
    
    public String getValueTag()
    {
        return channelName + "_";
    }
    
    public void setErrorInfo(String errorInfo)
    {
    	this.errorInfo = errorInfo;
    }
    
    public String getErrorInfo()
    {
        return errorInfo;
    }
        
    public void setReturnInfo(String returnInfo)
    {
    	this.returnInfo = returnInfo;
    }
    
    public String getReturnInfo()
    {
        return returnInfo;
    }
        
}